package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: AttackAb
 * Purpose: To make attack abilities.
 */
import java.util.ArrayList;
class DebuffAb extends Ability
{
    boolean interrupt=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); 
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    ArrayList<String[]> tempstrings= new ArrayList<String[]>();
    boolean usable=true; 
    int multiuse=0;
    public DebuffAb ()
    {
    }
    public DebuffAb (String aname, String atarget, String afriendly, int cod)
    {
        this.name=aname;
        this.oname=name;
        this.friendly=afriendly;
        this.target=atarget;
        this.cd=cod;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    {
        boolean typo=true; int uses=1; boolean aoe=false; 
        System.out.println (user.Cname+" used "+oname);
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (target.equalsIgnoreCase("aoe"))
        {
            aoe=true;
        }
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
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            if (targets.size()<=0)
            {
                uses=-1;
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
            }
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
                    if (user.ignores.contains("Status effects"))
                    {
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    }
                    else
                    {
                        chump=user.AttackNoDamage(user, chump, aoe); //check for evasion, protect, and blind
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    }
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    }                     
                    user.onAttack(user, chump); //activate relevant passives
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
                }
                --uses;
            }
        }
        if (singleuse==true)
        {
            used=true;
        }
        else
        {
            dcd=cd;
        }
        if (interrupt==true)
        {
            interrupt=false;
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
    public void SetChannelled(Character user, Ability ab, ArrayList<Character> targets)
    {
    }
    @Override 
    public void ActivateChannelled()
    {
        if (channelled==true&&interrupt==false)
        {
            System.out.println (oname+"'s channelling has finished.");
        }
        interrupt=false;
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
        if (user.CheckFor(user, "Debuff Disable")==true||user.CheckFor(user, "Ability Disable")==true)
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