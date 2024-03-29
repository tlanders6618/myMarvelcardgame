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
            if (this.aoe==true)
            {
                for (StatEff eff: user.effects) //get empowerments
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        change=eff.UseEmpower(user, ab, true);
                        damage+=change;
                    }
                }
            }
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely to avoid null exception
                {
                    do 
                    {
                        if (this.aoe==false)
                        {
                            for (StatEff eff: user.effects) //get empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ab, true);
                                    damage+=change;
                                }
                            }
                        }
                        if (elusive==true)
                        blind=true; //no need to check blind for elusive abs since they aren't attacks
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                            damage+=change;
                        } 
                        if (blind==false) //only check blind once per attack
                        Damage_Stuff.CheckBlind(user);
                        if (elusive==true) 
                        {
                            damage-=chump.ADR;
                            if (damage<0)
                            damage=0;
                            chump.TakeDamage(chump, user, damage, this.aoe);
                        }
                        else if (lose==true) 
                        {
                            user.AttackNoDamage(user, chump, damage, aoe, false);
                        }
                        else if (max==true)
                        {
                            user.AttackNoDamage(user, chump, damage, aoe, true);
                        }
                        else 
                        {
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
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
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);  
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