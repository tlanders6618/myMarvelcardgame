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
    int damage=616; //dmg after empowerment, beforeab, etc
    int odamage=616; //base dmg
    int dmgdealt=0; //for ricochet; determined by the hero.attack method where damagecalc is down
    int multihit=0; //how many hits are left in a multihit attack
    int omulti=0; //how many times to repeat the attack; the number of + signs
    boolean max=false; //whether the target directly loses max health instead of taking damage
    boolean lose=false; //whether the target directly loses health instead of taking damage
    boolean elusive=false;
    public AttackAb ()
    {
    }
    public AttackAb (String aname, String atype, String afriendly, int dmg, int cooldown)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atype;
        this.damage=dmg;
        this.odamage=damage;
        this.cd=cooldown;
        this.attack=true;
        if (atype.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    public AttackAb (String aname, String atype, String afriendly, int dmg, int cooldown, int mult)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atype;
        this.damage=dmg;
        this.odamage=damage;
        this.cd=cooldown;
        this.attack=true;
        this.multihit=mult;
        this.omulti=mult;
        if (atype.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets)
    { 
        boolean typo=true; int uses=1;   
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (multiuse>0)
        {
            System.out.println ("How many extra times would you like to use this ability?");
            do
            {
                uses=Damage_Stuff.GetInput();
                if (uses>=0&&uses<=multiuse)
                {
                    typo=false;
                }
                ++uses;
            }
            while (typo==true);
        }
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
                if (chump!=null) //if null, skip entirely
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
                        else if (lose==true) //modified version of attacknodamage method
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
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
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
                        }
                        for (String[][] array: statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); //this is how selfapply and other apply are populated
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                otherapply.add(New);
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
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, 616, chump); //for now this only activates chain
                        }
                        damage=odamage; //reset damage 
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
                }
                --uses;
            }
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
        if (singleuse==true)
        {
            used=true;
        }
        else
        {
            dcd+=cd;
        }
        return toadd;
    }
    @Override 
    public int GetBaseDmg ()
    {
        return odamage;
    }
    @Override
    public int GetMultihit(boolean original) //for chain
    {
        if (original==true)
        return omulti;
        else
        return multihit;
    }
    @Override
    public void UseMultihit () //for multichain
    {
        --multihit;
    }
    @Override
    public void ReturnDamage (int d)
    {
        dmgdealt=d;
    }
    @Override
    public int GetDmgDealt () //for chain, and some hero passives
    {
        return dmgdealt;
    }
    @Override 
    public ArrayList<StatEff> ActivateChannelled(Character user, Ability ab)
    {
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();   
        if (user.binaries.contains("Missed")) //to prevent a miss if the hero's assist/counterattack was evaded after they last attacked (miss is otherwise cleared after attacking)
        user.binaries.remove("Missed");
        if (channelled==true&&interrupt==true) 
        {
            interrupt=false; //reset it so the ab is not permanently unusable
            if (singleuse==true)
            {
                used=true;
            }
            else
            {
                dcd=cd;
            }
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            System.out.println (oname+"'s channelling finished.");
            System.out.println (user.Cname+" used "+oname+"!");
            StatEff remove= null;
            for (StatEff e: user.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+ab.oname))
                {
                    remove=e; break;
                }
            }
            if (remove!=null)
            user.effects.remove(remove);  
            int omulti=multihit;
            int change=0;
            if (ctargets.size()<=0)
            {
                System.out.println(oname+" could not be used due to a lack of eligible targets."); //but can still apply stateffs to self
                for (String[][] array: tempstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array, user); 
                    if (array[0][4].equalsIgnoreCase("true"))
                    {
                        selfapply.add(New);
                    }
                }
                for (String[][] array: statstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array, user); //this is how selfapply and other apply are populated
                    if (array[0][4].equalsIgnoreCase("true"))
                    {
                        selfapply.add(New);
                    }
                }
                ArrayList<StatEff> n= new ArrayList<StatEff>(); //empty array since there's no other targets to apply stateffs to 
                toadd=Ability.ApplyStats(user, null, together, selfapply, n); 
                if (selfapply.size()!=0)
                {
                    selfapply.removeAll(selfapply);
                }
                if (tempstrings.size()!=0) 
                {
                    tempstrings.removeAll(tempstrings);
                }
                return toadd; 
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
            for (Character chump: ctargets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely
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
                        else if (lose==true) //modified version of attacknodamage method
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
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
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
                        }
                        for (String[][] array: statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); //this is how selfapply and other apply are populated
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                otherapply.add(New);
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
                        damage=odamage; //reset damage 
                        dmgdealt=0;
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, 616, chump); //for now this only activates chain
                        }
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
                }
            }
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
        //don't go on cooldown bc useab already took care of it
        return toadd;
    }
    @Override
    public boolean CheckUse (Character user)
    {
        boolean okay=true;
        if ((user.CheckFor("Disarm", false)==true&&this.ignore==false)||user.CheckFor("Suppression", false)==true)
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