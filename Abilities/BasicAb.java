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
    public BasicAb ()
    {
    }
    public BasicAb (String aname, String atarget, String afriendly, int dmg)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atarget;
        this.damage=dmg;
        this.odamage=damage;
        this.attack=true;
        if (atarget.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    public BasicAb (String aname, String atarget, String afriendly, int dmg, int mult)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atarget;
        this.damage=dmg;
        this.odamage=damage;
        this.attack=true;
        this.multihit=mult;
        this.omulti=mult;
        if (atarget.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    @Override
    public void PrintDesc (boolean ignore)
    {
        System.out.print("Basic attack. "); 
        super.PrintDesc(true); //calls attackab's printdesc, not ability's printdesc
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    {
        boolean typo=true; int uses=1; 
        System.out.println (user.Cname+" used "+oname); ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (user.binaries.contains("Missed")) //to prevent a miss if the hero's assist/counterattack was evaded after they last attacked (miss is otherwise cleared after attacking)
        user.binaries.remove("Missed");
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
        while (uses>0) //repeat the attack for each multiuse
        {
            int change=0;
            if (targets.size()<=0)
            {
                uses=-1;
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
            }
            for (Character chump: targets) //use the ability on its target
            {
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) //if null, skip entirely to avoid null exception
                {
                    do 
                    {
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, ab, true);
                                damage+=change;
                            }
                        }
                        if (elusive==true)
                        {
                            blind=true; evade=true; //no need to check blind or evade for elusive abs since they aren't attacks
                        }
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                            damage+=change;
                        } 
                        if (elusive==true) 
                        {
                            damage-=chump.ADR;
                            if (damage<0)
                            damage=0;
                            if (this.odamage>0||this.damage>0) //only print damage if attack was meant to do damage
                            System.out.println ("\n"+user.Cname+" did "+damage+" damage to "+chump.Cname);
                            chump.TakeDamage(chump, damage, false);
                        }
                        else if (lose==true) 
                        {
                            chump=user.AttackNoDamage(user, chump, damage, aoe, false);
                        }
                        else if (max==true)
                        {
                            chump=user.AttackNoDamage(user, chump, damage, aoe, true);
                        }
                        else 
                        {
                            if (this.odamage>0||this.damage>0)
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                            else
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking
                        } 
                        for (String[][] array: tempstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); 
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
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
                        for (String[][] array: statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); //this is how selfapply and other apply are populated
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if ((!(user.binaries.contains("Missed"))||user.immunities.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                            //they cannot apply effects if the target evaded/they are blind
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
                        ArrayList<StatEff> holder=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                        toadd.addAll(holder);
                        if (aoe==false) 
                        {
                            for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    int irrelevant=eff.UseEmpower(user, ab, false);
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
                        this.blind=false; this.evade=false;
                        this.UseMultihit();
                        dmgdealt=0;
                        damage=odamage; //reset damage 
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, 616, chump); //for now this only activates chain
                        }
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
                }
            }
            --uses;
            if (aoe==true)
            {
                for (String[][] array: statstrings) //statstrings are checked once for each of the ab's targets, which would cause effs meant for self to be applied multiple times
                {  
                    if (array[0][4].equalsIgnoreCase("true aoe")) //this allows them to only be applied once per ab use
                    {
                        StatEff New=StatFactory.MakeStat(array, user); 
                        toadd.add(New);
                    }
                }
                for (StatEff eff: user.effects) //undo empowerments
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        int irrelevant=eff.UseEmpower(user, ab, false);
                    }
                }
            }
        }
        return toadd;
    }
    @Override 
    public void SetChannelled (Character hero, Ability ab, ArrayList<Character> targets)
    {
    }
    @Override 
    public void InterruptChannelled (Character hero, Ability ab)
    {
    }
    @Override 
    public ArrayList<StatEff> ActivateChannelled(Character t, Ability a)
    {
        return null;
    }
    @Override 
    public String GetAbName (Character hero)
    {
        boolean g=this.CheckUse(hero); 
        if (g==true)
        {
            return this.oname;
        }
        else
        {
            return this.oname+ "(disabled)";
        }
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if (user.CheckFor("Disarm", false)==true&&this.ignore==false)
        {
            return false;
        }
        return true;
    }
    @Override 
    public void CDReduction (int g)
    { 
    }
}