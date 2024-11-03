package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: AttackAb
 * Purpose: To make attack abilities.
 */
import java.util.ArrayList;
import java.util.Iterator;
class AttackAb extends Ability
{
    int damage=616; //dmg after empowerment, beforeab, etc
    int odamage=616; //base dmg
    int dmgdealt=0; //for ricochet; determined by the hero.attack method where damagecalc is down
    int multihit=0; //how many hits are left in a multihit attack
    int omulti=0; //how many times to repeat the attack; the number of + signs
    boolean max=false; //whether the target directly loses max health instead of taking damage
    boolean lose=false; //whether the target directly loses health instead of taking damage
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
    public void PrintDesc (boolean printed) 
    {
        if (printed==false) //whether basicab.printdesc was called or not, since it already prints that it's a basic attack
        System.out.print("Attack ability. "); 
        if (this.lose==true)
        System.out.print("Causes "+damage+" HP loss. ");
        else if (this.max==true)
        System.out.print("Reduces max HP by "+damage+". ");
        else
        System.out.print("Does "+damage+" damage. ");
        if (this.multihit>0)
        System.out.print("Multihit: "+this.omulti+". ");
        if (this.ignore==true)
        System.out.print("Ignores Disarm. ");
        super.PrintDesc(false);
    }
    @Override
    public boolean GetLose() //for assists
    {
        return this.lose;
    }
    @Override
    public boolean GetMax()
    {
        return this.max;
    }
    @Override
    public ArrayList<StatEff> UseAb (Character user, ArrayList<Character> targets) //doesn't call check ignore since disarm doesn't require it
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
        if (this.channelled==true)
        {
            this.SetChannelled(user, targets);
        }
        else
        {
            System.out.println (user+" used "+this.oname);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            if (targets.size()<=0)
            {
                System.out.println(this.oname+" could not be used due to a lack of eligible targets.");
                return null;
            }
            Iterator<Character> iterator=targets.iterator();
            while (iterator.hasNext()==true)
            {
                Character chump=iterator.next();
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) //if null, skip entirely
                {
                    do 
                    {
                        int change=0;
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, this, true);
                                damage+=change;
                                if (damage<0)
                                damage=0;
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
                            if (damage<0)
                            damage=0;
                        } 
                        if (elusive==true&&(this.odamage>0||this.damage>0)) //only try to do damage if attack was meant to do damage; abs that call assists shouldn't print
                        {
                            Damage_Stuff.ElusiveDmg(user, chump, damage, "default");
                        }
                        else if (lose==true) //modified version of attacknodamage method
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, false); //ondamage since loss and reduce are unaffected by dmg modification
                        }
                        else if (max==true)
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, true);
                        }
                        else 
                        {
                            if (this.odamage>0||this.damage>0) //only call attack method if attack was meant to do dmg
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                            else
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        this.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                        this.UseStatStrings(user, chump, false);
                        //then see what can be applied and what fails; only successful selfapply effs are added to toadd and returned to be applied after ab usage
                        toadd.addAll(Ability.ApplyStats(user, chump, together, selfapply, otherapply)); 
                        if (aoe==false)
                        {
                            for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    int irrelevant=eff.UseEmpower(user, this, false);
                                }
                            }
                        }
                        this.ResetAb(user); //clears selfapply, resets blind/evade checks, and removes tempstrings and missed
                        this.UseMultihit();
                        if (iterator.hasNext()==false)
                        this.done=true;
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, dmgdealt, chump); 
                        }
                        if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                        {
                            toadd.addAll(selfapply);
                            selfapply.removeAll(selfapply); 
                        }
                        this.dmgdealt=0;
                        damage=odamage; //reset damage 
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
                }
                --uses;
            }
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
                        int irrelevant=eff.UseEmpower(user, this, false);
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
        this.done=false;
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
    public void UseMultihit () //also for chain
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
        if (channelled==true&&interrupt==true) 
        {
            interrupt=false; //reset it so the ab is not permanently unusable
            if (singleuse==true)
            {
                used=true;
            }
            else
            {
                dcd+=cd;
            }
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            finished=true; //so if they die in the middle of using a channelled ab, it won't print "channel was interrupted" on death
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
            if (ctargets.size()<1)
            {
                System.out.println(oname+" could not be used due to a lack of eligible targets."); //but can still apply stateffs to self
                this.UseStatStrings(user, null, true);
                this.UseStatStrings(user, null, false);
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
            Iterator<Character> iterator=ctargets.iterator();
            while (iterator.hasNext()==true) //use ab on each target
            {
                Character chump=iterator.next();
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) //if null, skip entirely
                {
                    do 
                    {
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, ab, true);
                                damage+=change;
                                if (damage<0)
                                damage=0;
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
                            if (damage<0)
                            damage=0;
                        } 
                        if (elusive==true&&(this.odamage>0||this.damage>0)) //only print damage if attack was meant to do damage; abs that call assists shouldn't print
                        {
                            Damage_Stuff.ElusiveDmg(user, chump, damage, "default");
                        }
                        else if (lose==true) //modified version of attacknodamage method
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, false); //ondamage since loss and reduce are unaffected by dmg modification
                        }
                        else if (max==true)
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, true);
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
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        this.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                        this.UseStatStrings(user, chump, false);
                        toadd.addAll(Ability.ApplyStats(user, chump, together, selfapply, otherapply));
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
                        this.ResetAb(user); //clears selfapply, resets blind/evade checks, and removes tempstrings and missed
                        this.UseMultihit();
                        if (iterator.hasNext()==false)
                        this.done=true; //done using ab because no more targets for it to affect
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, dmgdealt, chump); 
                        }
                        if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                        {
                            toadd.addAll(selfapply);
                            selfapply.removeAll(selfapply); 
                        }
                        this.dmgdealt=0;
                        damage=odamage; //reset damage 
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
                }
            }
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
            ArrayList<StatEff> errands= new ArrayList<StatEff>(); errands.addAll(user.effects);
            for (StatEff eff: errands) //even though empowers won't activate again once used, they only expire onturnend and channelled abs activate on turnstart
            {
                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                eff.onTurnEnd(user); //removes used up empowerments from scoreboard after channelled ab use, to avoid confusion/the appearance of a bug
            }
        } 
        //don't go on cooldown bc useab already took care of it
        this.done=false; //reset for next use
        return toadd;
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //does nothing, since disarm doesn't need to be ignored with binaries
    {
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if ((user.CheckFor("Disarm", false)==true&&this.ignore==false)||user.CheckFor("Suppression", false)==true)
        {
            return false;
        }
        else if (singleuse==true&&used==true)
        {
            return false;
        }
        else if (usable==false)
        {
            return false;
        }
        else if (dcd>0) 
        {
            return false;
        }
        if (this.restricted==true)
        {
            switch (this.restriction)
            {
                case 77: //penance's ab #4
                if (user.passivecount<3)
                return false;
            }
        }
        return true;
    }
}