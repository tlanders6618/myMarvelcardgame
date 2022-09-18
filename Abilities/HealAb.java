package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: HealAb
 * Purpose: To make Heal abilities.
 */
import java.util.ArrayList;
class HealAb extends Ability
{
    String name;
    String oname;
    String target;
    String friendly;
    int cd=616; int dcd=0; //all abilities are displayed with 0 cooldown and switch to their listed cooldowns after use 
    boolean channelled=false; boolean interrupt=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); 
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    int multiuse=0;
    boolean singleuse=false;
    boolean used=false;
    boolean usable=true;
    boolean together=true;
    public HealAb ()
    {
    }
    public HealAb (String aname, String atarget, String afriendly, int cod)
    {
        name=aname;
        oname=name;
        friendly=afriendly;
        target=atarget;
        cd=cod;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, Character[] targets)
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
                    for (String[] array: statstrings)
                    {  
                       StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                       if (array[4].equalsIgnoreCase("true"))
                       {
                           selfapply.add(New);
                       }
                       else if (array[4].equalsIgnoreCase("false"))
                       {
                           otherapply.add(New);
                       }
                    }
                    toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    if (selfapply.size()!=0)
                    {
                        selfapply.removeAll(selfapply); //ensures every status effect is unique, to avoid bugs
                    }
                    if (otherapply.size()!=0)
                    {
                        otherapply.removeAll(otherapply);
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
    public boolean GetChannelled()
    {
        return channelled;
    }
    @Override 
    public void SetChannelled(Character user, Ability ab, Character[] targets)
    {
    }
    @Override 
    public void ActivateChannelled()
    {
        if (interrupt==false)
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
        return 0;
    }
    @Override
    public boolean CheckUse (Character user, Ability ab)
    {
        boolean okay=true;
        if (user.CheckFor(user, "Heal Disable")==true||user.CheckFor(user, "Ability Disable")==true)
        {
            okay=false;
        }
        if (singleuse==true&&used==true)
        {
            okay=false;
        }
        if (usable==false)
        {
            okay=false;
        }
        if (dcd>0) 
        {
            okay=false;
        }
        return okay;
    }
    @Override 
    public void CDReduction ()
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
            --dcd;
            if (dcd<0)
            {
                dcd=0; //no negative cooldowns
            }
        }   
    }
}