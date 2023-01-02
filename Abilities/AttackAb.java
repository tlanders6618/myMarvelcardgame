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
    boolean interrupt=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); 
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    ArrayList<String[]> tempstrings= new ArrayList<String[]>();
    int damage=616; int odamage=616;
    int dmgdealt=0;
    int multihit=0; //how many times to repeat the attack; the number of + signs
    boolean lose=false; //whether the target directly loses health instead of taking damage
    boolean usable=true; //keep this for penance
    int multiuse=0;
    public AttackAb ()
    {
    }
    public AttackAb (String aname, String atype, String afriendly, int dmg, int cooldown)
    {
        this.name=aname;
        this.oname=name;
        this.friendly=afriendly;
        this.target=atype;
        this.damage=dmg;
        this.odamage=damage;
        this.cd=cooldown;
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
            if (targets.size()<=0)
            {
                uses=-1;
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
            }
            else if (target.equalsIgnoreCase("aoe"))
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
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
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
                        user.onAttack(user, chump); //activate relevant passives
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
                        dmgdealt=0;
                    }
                    while (multi>-1); //then repeat the attack for each multihit
                    multi=omulti; //reset the multihit counter for the next use
                }
                --uses;
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
    public int ReturnBaseDmg ()
    {
        return odamage;
    }
    @Override
    public void ReturnDamage (int d)
    {
        dmgdealt=d;
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
}