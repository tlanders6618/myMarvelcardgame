package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: AttackAb
 * Purpose: To make attack abilities.
 */
import java.util.ArrayList;
class AttackAb extends Ability
{
    String name="616";
    String oname;
    String target;
    String friendly;
    int cd=616; int dcd=0; //all abilities are displayed with 0 cooldown and switch to their listed cooldowns after use 
    boolean channelled=false; boolean interrupt=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); //ricochet, chain, steal, purify, nullify, extend, copy, take bonus turn, on crit, ignore status 
    //can only be used if hero has three effects, do extra damage based on target or self hp, repeat attack, apply debuff on crit, ignore defence, ignore targeting effects
    //ignore evade, do bonus damage if user has shock, do bonus dmg if target has shock
    //need separate stateff application method for together and separate
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    ArrayList<String[]> tempstrings= new ArrayList<String[]>();
    int damage=616; int odamage=616;
    int dmgdealt=616;
    int multihit=0; //how many times to repeat the attack; the number of + signs
    int multiuse=0;
    boolean singleuse=false; boolean lose=false; //whether the target directly loses health instead of taking damage
    boolean used=false; //only for single use abilities
    boolean usable=true; //keep this for penance
    boolean together=true; //whether status effects are applied separately or together
    public AttackAb ()
    {
    }
    public AttackAb (String aname, String atype, String afriendly, int dmg, int cooldown)
    {
        name=aname;
        oname=name;
        friendly=afriendly;
        target=atype;
        damage=dmg;
        odamage=damage;
        cd=cooldown;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    {
        boolean typo=true; int uses=1; boolean aoe=false; 
        System.out.println ("\n"+user.Cname+" used "+oname);
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();        
        if (multiuse>0)
        {
            System.out.println ("How many extra times would you like to use this ability?");
            do
            {
                uses=Damage_Stuff.GetInput();
                ++uses;
                if (uses>0&&uses<=multiuse)
                {
                    typo=false;
                }
            }
            while (typo==true);
        }
        int multi=multihit; int omulti=multihit;
        if (channelled==true)
        {
            ab.SetChannelled(user, ab, targets);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            int change=0;
            if (target.equalsIgnoreCase("aoe"))
            {
                aoe=true;
                for (StatEff eff: user.effects) //get empowerments
                {
                   if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                   {
                       change=eff.UseEmpower(user, ab, damage, true);
                   }
                }
            }
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely
                {
                    do 
                    {
                        if (aoe==false)
                        {
                            for (StatEff eff: user.effects) //get empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ab, damage, true);
                                }
                            }
                        }
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                        } 
                        damage+=change;
                        if (user.ignores.contains("Status effects"))
                        {
                            chump.HP-=damage;
                            if (chump.HP<=0)
                            {
                                chump.onLethalDamage(chump, user, "attack");
                            }
                        }
                        else if (lose==true)
                        {
                            chump.HP-=damage; //need to rewrite this to account for protect
                            chump.onAttacked(chump, user, 0);
                        }
                        else 
                        {
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, damage); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        for (String[] array: tempstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array); 
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) 
                            {
                                otherapply.add(New);
                            }
                        }
                        for (String[] array: statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                otherapply.add(New);
                            }
                        }
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                        user.onAttack(user); //activate relevant passives
                        if (aoe==false)
                        {
                            for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ab, damage, false);
                                }
                            }
                        }
                        if (selfapply.size()!=0)
                        {
                            selfapply.removeAll(selfapply); //ensures every status effect is unique, to avoid bugs
                        }
                        if (otherapply.size()!=0)
                        {
                            otherapply.removeAll(otherapply);
                        }
                        if (tempstrings.size()!=0) //these effects are only sometimes applied with attacks, hence the name temp; they're reset afterwards
                        {
                            tempstrings.removeAll(tempstrings);
                        }
                        if (user.binaries.contains("Missed"))
                        {
                            user.binaries.remove("Missed");
                        }
                        --multi;
                        damage=odamage; //reset damage 
                    }
                    while (multi>-1); //then repeat the attack for each multihit
                    --uses;
                    multi=omulti; //reset the multihit counter for the next use
                }
                if (aoe==true)
                {
                    for (StatEff eff: user.effects) //undo empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, damage, false);
                        }
                    }
                }
            }
        }
        if (interrupt==true)
        {
            interrupt=false;
        }
        if (singleuse==true)
        {
            used=true;
        }
        else
        {
            dcd=cd;
        }
        return toadd;
    }
    @Override 
    public int ReturnDamage (int dm)
    {
        return damage;
    }
    @Override
    public void AddStatString(String[] f)
    {
        statstrings.add(f);
    }
    @Override
    public void AddTempString(String[] f)
    {
        tempstrings.add(f);
    }
    @Override
    public boolean GetChannelled()
    {
        return channelled;
    }
    @Override 
    public void SetChannelled(Character user, Ability ab, ArrayList<Character> targets) //WIP
    {
    }
    @Override 
    public void ActivateChannelled()
    {
        if (channelled==true&&interrupt==false)
        {
            System.out.println (oname+"'s channelling has finished.");
        }
    }
    @Override
    public void InterruptChannelled()
    {
        interrupt=true;
    }
    @Override 
    public String GetAbName (Character hero, Ability ab)
    {
        boolean g=CheckUse(hero, ab);
        if (g==true&&dcd==0)
        {
            String nname=oname; return nname;
        }
        else if (g==false&&dcd>0)
        {
            String nname=oname+", usable again in "+dcd+" turn(s)";
            return nname;
        }
        else
        {
            String nname=name+ "(disabled)";
            return nname;
        }
    }
    @Override
    public String GetTargetType()
    {
        return target;
    }
    @Override
    public String GetFriendly()
    {
        return friendly;
    }
    @Override
    public boolean GetTogether()
    {
        return together;
    }
    @Override
    public int GetAbDamage (Character user, Ability ab) //this isn't used for attacking; it's for passives like Jean Grey's 
    {
        int ndamage=damage+user.BD+user.PBD;
        return ndamage;
    }
    @Override
    public boolean CheckUse (Character user, Ability ab)
    {
        boolean okay=true;
        if (user.CheckFor(user, "Disarm")==true||user.CheckFor(user, "Ability Disable")==true)
        {
            okay=false;
        }
        else if (singleuse==true&&used==true)
        {
            okay=false;
        }
        else if (usable==false)
        {
            okay=false;
        }
        else if (dcd>0) 
        {
            okay=false;
        }
        return okay;
    }
    @Override 
    public void CDReduction (int amount)
    {
        if (singleuse==true)
        {
            //it doesn't have a cooldown
        }
        else if (dcd<=0)
        {
            //no negative cooldowns
        }
        else 
        {
            dcd-=amount;
            if (dcd<0)
            {
                dcd=0; //no negative cooldowns
            }
        }   
    }
}