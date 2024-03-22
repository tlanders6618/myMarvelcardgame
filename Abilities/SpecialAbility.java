package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 18/8/22
 * Filename: SpecialAbility
 * Purpose: To perform non status effect related functions on abilities; contains the SpecialAbility class and list of Helpers.
 */
import java.util.ArrayList; 
public abstract class SpecialAbility 
{
   int hashcode;
   public SpecialAbility()
   {
       hashcode=Card_HashCode.RandomCode();
   }
   public int Use(Character hero, Character target) //for before abs; called before ab is used, after choosing targets
   {
       return 0;
   }
   public void Use (Character caller, Character target, int dmg) //for after abs; called after attacking with an ab, before applying stateffs
   {
   }
   public int Use (Character user, int dmg, Character target) //only for helpers, ignore, and chain; not before or after abs; called after an ab is used
   {
       return dmg;
   }
   public void Undo (Character victim) //only for helpers; not before or after abs
   {
   }
}
//a target's helpers trigger onattacked and all helpers in hero.helpers are called/undone in battle, AFTER their hero's turn ends
class BonusTurnHelper extends SpecialAbility 
{
    boolean used=false;
    public BonusTurnHelper()
    {
    }
    @Override
    public void Undo (Character hero) //bonus turn triggers at the end of a hero turn
    {
        if (used==false&&hero.dead==false&&hero.activeability.channelled==false) //since helpers are triggered after using a channelled ability, right before hero starts their turn
        {
            Battle.Turn(hero, true); //hero can only take a bonus turn after finishing their turn, so it cannot be triggered after using a channelled skill
            used=true;
        }
        else
        {
            for (SpecialAbility h: hero.helpers)
            {
                System.out.println(h.toString());
                if (h.hashcode==this.hashcode)
                {
                    hero.helpers.remove(h); System.out.println ("test: turn helper successfully removed"); break; 
                }
            }
        }
    }
}
class Chain extends SpecialAbility //both chain and multichain have been merged into one; in here since it's technically not an afterab
{
    boolean multi=false; int damage;
    public Chain (boolean multi, Ability ability)
    {
        this.multi=multi; damage=ability.GetBaseDmg();
    }
    @Override
    public int Use (Character user, int ignore, Character victim)
    {
        Ability ab=user.activeability;
        ArrayList<Character> targets= new ArrayList<Character>();
        if (ab.aoe==true)
        {
            Character[] targets1= new Character[6];
            targets1=Battle.GetTeam(CoinFlip.TeamFlip(user.team1));
            targets=CoinFlip.ToList(targets1);
        }
        else
        {
            targets.add(Ability.GetRandomHero (user, false, false));
        }
        if ((multi==true&&victim.dead==true&&user.dead==false&&targets.size()>0&&ab.GetMultihit(false)>-1)||(multi==false&&victim.dead==true&&user.dead==false&&targets.size()>0))
        { //usability check
            if (multi==false)
            {
                System.out.println ("\n"+user.Cname+" used "+ab.oname);
                double d=damage/2;
                damage=5*(int)Math.floor(d/5);
            }
            if (user.binaries.contains("Missed")) 
            user.binaries.remove("Missed");
            int change=0;
            ArrayList<StatEff> toadd= new ArrayList<StatEff>();   
            if (ab.aoe==true)
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
                if (chump!=null) 
                {
                    while (ab.GetMultihit(false)>-1)
                    {
                        if (ab.aoe==false)
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
                        Damage_Stuff.CheckBlind(user);
                        for (SpecialAbility ob: ab.special)
                        {
                            change=ob.Use(user, chump); 
                            damage+=change;
                        } 
                        //removed attack options for elusive and causing health loss since they're so rare and so far don't overlap with chain; add back if needed
                        chump=user.Attack(user, chump, damage, ab.aoe);
                        for (SpecialAbility ob: ab.special)
                        {
                            ob.Use(user, chump, ab.GetDmgDealt()); 
                        } 
                        for (String[][] array: ab.tempstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); 
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                ab.selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                            {
                                ab.otherapply.add(New);
                            }
                        }
                        for (String[][] array: ab.statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array, user); 
                            if (array[0][4].equalsIgnoreCase("true"))
                            {
                                ab.selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                ab.otherapply.add(New);
                            }
                        }
                        toadd=Ability.ApplyStats(user, chump, ab.together, ab.selfapply, ab.otherapply); 
                        if (ab.aoe==false)
                        {
                            for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    int irrelevant=eff.UseEmpower(user, ab, false);
                                }
                            }
                        }
                        if (ab.selfapply.size()!=0)
                        {
                            ab.selfapply.removeAll(ab.selfapply); 
                        }
                        if (ab.otherapply.size()!=0)
                        {
                            ab.otherapply.removeAll(ab.otherapply);
                        }
                        if (ab.tempstrings.size()!=0) 
                        {
                            ab.tempstrings.removeAll(ab.tempstrings);
                        }
                        if (user.binaries.contains("Missed"))
                        {
                            user.binaries.remove("Missed");
                        }
                        ab.UseMultihit();
                        ab.ReturnDamage(0);
                        for (SpecialAbility ob: ab.special)
                        {
                            ob.Use(user, 616, chump); //for now this only activates chain
                        }
                        damage=ab.GetBaseDmg(); //reset damage 
                    }
                }
            }
            if (ab.aoe==true)
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
        return 616;
    }
}
class RedwingHelper extends SpecialAbility
{
    boolean used=false;
    int heal=0;
    public RedwingHelper ()
    {
    }
    @Override
    public int Use (Character victim, int dmg, Character attacker)
    {
        if (dmg>=120) //e.g. 125
        {
            used=true;
            System.out.println ("Redwing swoops into action, protecting "+victim.Cname+"!");
            victim.immunities.add("Debuffs"); 
            double dub= dmg/2; //cut dmg in half; e.g. 62.5
            dmg=5*(int)Math.ceil(dub/5); //then round up to find out how much the victim takes; e.g. 65
            heal=5*(int)Math.floor(dub/5); //the other half of the dmg prevented (e.g. 62.5) is rounded down (e.g. 60)
            dub=heal/2; //then divided by 2 to determine Falcon's mend value, e.g. 30
            heal=5*(int)Math.floor(dub/5); //and rounded down again to ensure it's a whole number, e.g. 30
        }        
        return dmg;
    }    
    @Override
    public void Undo (Character victim)
    {
        if (used==true)
        {
            Character[] team=null;
            if (victim.team1==true)
            team=Battle.GetTeam(true);
            else
            team=Battle.GetTeam(false);
            for (Character c: team)
            {
                if (c!=null&&c.summoned==false&&c.index==7&&c.dead==false&&!(c.binaries.contains("Stunned"))&&!(c.binaries.contains("Banished")))
                {
                    //c is falcon, but he must be alive and not incapacitated to use mend
                    Mend mend= new Mend (500, heal); mend.Use(c, victim, 616);
                    break;
                }
            }
            SpecialAbility remov=null;
            for (SpecialAbility f: victim.helpers) //redwing
            {
                if (f instanceof RedwingHelper)
                {
                    remov=f;
                    break; //since there can only be one redwing
                }
            }
            StatEff removee=null;
            for (StatEff ef: victim.effects) //the stateff that applies redwing and lets players know it's active
            {
                if (ef.getimmunityname().equalsIgnoreCase("Redwing"))
                {
                    removee=ef;
                    break; //since there can only be one redwing
                }
            }
            victim.remove(removee.hashcode, "normal");
            victim.helpers.remove(remov);
            victim.immunities.remove("Debuffs"); 
        }
    }
}