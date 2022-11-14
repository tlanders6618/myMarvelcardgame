package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: OtherAb
 * Purpose: To make Other abilies.
 */
import java.util.ArrayList;
class OtherAb extends Ability
{
    String name;
    String oname;
    String target;
    String friendly;
    int cd=616; int dcd=0; //all abilities are initially displayed with 0 cooldown and switch to their listed cooldowns after use 
    boolean channelled=false; boolean interrupt=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); //ricochet, chain, steal, purify, nullify, extend, copy, take bonus turn, on crit, ignore status 
    //can only be used if hero has three effects, do extra damage based on target or self hp, repeat attack, apply debuff on crit, ignore defence, ignore targeting effects
    //ignore evade, do bonus damage if user has shock, do bonus dmg if target has shock
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    ArrayList<String[]> tempstrings= new ArrayList<String[]>();
    int multiuse=0;
    boolean attack=false;
    boolean singleuse=false; 
    boolean used=false; //only for single use abilities
    boolean usable=true; //keep this for penance
    boolean together=true; //whether status effects are applied separately or together
    public OtherAb ()
    {
    }
    public OtherAb (String aname, String atarget, String afriendly, int cooldown)
    {
        name=aname;
        oname=name;
        friendly=afriendly;
        target=atarget;
        cd=cooldown;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    {
        boolean typo=true; int uses=1; 
        System.out.println (user.Cname+" used "+oname);
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
        if (channelled==true)
        {
            ab.SetChannelled(user, ab, targets);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing and activate later
        {
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely
                {
                    int change=0; 
                    for (StatEff eff: user.effects) //get empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, 0, true);
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
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
                        else
                        {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
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
                       else
                       {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
                       }
                    }
                    if (attack==true&&chump.team1!=user.team1)
                    {
                        user.AttackNoDamage(user, chump, false); //let chump know he's been attacked
                    }
                    toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    user.onAttack(user); //activate relevant passives
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
                    for (StatEff eff: user.effects) //undo empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, 0, false);
                        }
                    }
                    --uses;
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
        //use ability
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
            name=oname; return name;
        }
        else if (g==false&&dcd>0)
        {
            name=oname+", usable again in "+dcd+" turn(s)";
            return name;
        }
        else
        {
            name=name+ " (disabled)";
            return name;
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
        return 0;
    }
    @Override
    public boolean CheckUse (Character user, Ability ab)
    {
        boolean okay=true;
        if (user.CheckFor(user, "Ability Disable")==true)
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