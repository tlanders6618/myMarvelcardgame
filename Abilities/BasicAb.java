package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: BasicAb
 * Purpose: To make basic attacks.
 */
import java.util.ArrayList;
class BasicAb extends AttackAb
{
    String name="616";
    String oname;
    String target;
    String friendly;
    int cd=0; int dcd=0; //all abilities are displayed with 0 cooldown and switch to their listed cooldowns after use 
    final static boolean channelled=false;
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); //ricochet, chain, steal, etc
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<String[]> statstrings= new ArrayList<String[]>();
    ArrayList<String[]> tempstrings= new ArrayList<String[]>();
    int damage=616; int odamage=616;
    int multihit=0; //how many times to repeat the attack; the number of + signs
    int multiuse=0;
    boolean together=true; //whether status effects are applied separately or together
    public BasicAb ()
    {
    }
    public BasicAb (String aname, String atarget, String afriendly, int dmg)
    {
        name=aname;
        oname=name;
        friendly=afriendly;
        target=atarget;
        damage=dmg;
        odamage=damage;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    {
        boolean typo=true; int uses=1; boolean aoe=false;
        System.out.println (user.Cname+" used "+oname); ArrayList<StatEff> toadd= new ArrayList<StatEff>();
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
        int multi=multihit; int omulti=multihit;
        while (uses>0) //repeat the attack for each multiuse
        {
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely to avoid null exception
                {
                    do 
                    {
                        int change=0; 
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, ab, damage, true);
                            }
                        }
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking
                        }                         
                        damage+=change;
                        if (user.ignores.contains("Status effects"))
                        {
                            chump.HP-=damage;
                        }
                        else if (lose==true)
                        {
                            chump.HP-=damage;
                            chump.onAttacked(chump, user, 0);
                        }
                        else 
                        {
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                        }
                        for (StatEff eff: user.effects) //undo empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, ab, damage, false);
                            }
                        }   
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, damage); //apply unique ability functions after attacking
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
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);                        
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
                        --multi;
                        damage=odamage; //reset damage 
                    }
                    while (multi>-1); //then repeat the attack for each multihit
                    --uses;
                    multi=omulti; //reset the multihit counter for the next use
                }
            }
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
    public void ActivateChannelled()
    {
    }
    @Override 
    public String GetAbName (Character hero, Ability ab)
    {
        boolean g=CheckUse(hero, ab); 
        if (g==true)
        {
            name=oname; return name;
        }
        else
        {
            name=name+ "(disabled)";
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
        int ndamage=damage+user.BD+user.PBD;
        return ndamage;
    }
    @Override
    public boolean CheckUse (Character user, Ability ab)
    {
        boolean okay=true;
        if (user.CheckFor(user, "Disarm")==true)
        {
            okay=false;
        }
        return okay;
    }
    @Override 
    public void CDReduction (int g)
    { 
    }
}