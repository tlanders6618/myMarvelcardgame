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
   String desc; //for printdesc for abs
   public SpecialAbility()
   {
       this.hashcode=CardCode.RandomCode();
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
   public boolean CheckApply ()
   {
       return false;
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
        if (used==false&&hero.dead==false&&hero.activeability.channelled==false&&!(hero.binaries.contains("Banished"))) //cannot take bonus turn during linked banish
        {
            used=true; //activeab must be false so hero only takes a bonus turn after finishing their turn, so it cannot be triggered immediately after using a channelled skill 
            if (Battle.CheckWin(Battle.tturns%2)==0) //do not take bonus turn after killing last enemy, or else game cannot end
            Battle.Turn(hero, true);           
        }
        else //bonus turn helpers are single use only; removed immediately after use
        {
            hero.helpers.remove(this);
        }
    }
}
class Chain extends SpecialAbility //both chain and multichain have been merged into one; in here since it's technically not an afterab
{
    boolean multi=false; //multichain
    int base; //base damage of attack, since each chain cuts it in half
    int auses; //number of times to chain
    boolean used=false; //chain has been activated
    public Chain (boolean m, Ability ability)
    {
        multi=m; base=ability.GetBaseDmg();
        if (multi==true)
        {
            this.desc="If the target dies before the last hit, continue the attack on a random enemy. ";
            auses=ability.GetMultihit(true); //set number; cannot chain more than multihit
        }
        else
        {
            this.desc="Chain. ";
            if (ability.aoe==true)
            auses=0;
            else
            auses=1; //guaranteed to chain at least once on kill
        }
    }
    @Override
    public int Use (Character user, int ignore, Character victim) //after hero finishes attacking and using afterabs
    {
        Ability ab=user.activeability; 
        //single target attacks chain if they kill their target; aoe attacks chain if the attack killed at least one target (chain only occurs after all targets have been hit)
        if (ab.done==true&&used==false&&(ab.aoe==false&&victim.dead==true||ab.aoe==true&&auses>0))
        {
            ArrayList<Character> targets= new ArrayList<Character>(); targets.add(victim);
            used=true; //subsequent kills should just ++auses instead of activating another chain loop
            if (this.multi==true)
            auses=ab.GetMultihit(false)+1; //+1 due to attackabs having do while (multihit>-1) instead of >0, so it's counted differently and needs to be adjusted
            while (auses>0) 
            { 
                if (ab.aoe==true) //update targets to remove dead one(s)
                {
                    targets=CoinFlip.ToList(Battle.GetTeam(CoinFlip.TeamFlip(user.team1))); //getting enemy team
                }
                else if (targets.get(0).dead==true) //single target chain or multichain; last target died, so get new one
                { 
                    targets.set(0,Ability.GetRandomHero (user, victim, false, false)); //returns null if there are no heroes left
                    if (targets.get(0)==null) //if all enemies are dead, fail below usability check and end chain
                    targets.remove(0);
                }
                if (user.dead==false&&targets.size()>0) //usability check
                { 
                    if (multi==false) //regular chain
                    {
                        System.out.println ("\n"+user.Cname+" Chained "+ab.oname); 
                        double d=base/2;
                        base=5*(int)Math.floor(d/5);
                    }
                }
                else //even if chain was triggered, if it isn't useable then it ends to avoid an infinite loop
                {
                    ResetChain(ab); 
                    break;
                }
                ArrayList<StatEff> toadd= new ArrayList<StatEff>();   
                for (Character chump: targets) //use the ability on its target(s)
                {
                    if (chump!=null) //does not check for control due to there being no overlap between chain and control as of 4.5; change if needed
                    {
                        int change=0; int damage=base;
                        Damage_Stuff.CheckBlind(user);
                        for (SpecialAbility ob: ab.special) //beforeabs
                        {
                            change=ob.Use(user, chump); 
                            damage+=change;
                        } 
                        //removed attack options for max hp reduction and health loss since they're so rare and so far don't overlap with chain; add back if needed
                        if (ab.elusive==true&&(ab.GetBaseDmg()>0||damage>0)) //only try to do damage if attack was meant to do damage; abs that call assists shouldn't print
                        {
                            Damage_Stuff.ElusiveDmg(user, chump, damage, "default");
                        }
                        else
                        {
                            chump=user.Attack(user, chump, damage, ab.aoe);
                        }
                        for (SpecialAbility ob: ab.special) //afterabs
                        {
                            ob.Use(user, chump, ab.GetDmgDealt()); 
                        } 
                        ab.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                        ab.UseStatStrings(user, chump, false);
                        toadd=Ability.ApplyStats(user, chump, ab.together, ab.selfapply, ab.otherapply); 
                        ab.ResetAb(user); //clears selfapply, resets blind/evade checks, and removes tempstrings and missed
                        if (ab.aoe==false)
                        {
                            --this.auses;
                            if (this.multi==true) 
                            ab.UseMultihit(); 
                        }
                        for (SpecialAbility ob: ab.special)
                        {
                            ob.Use(user, 616, chump); //check if the attack can chain again
                        }
                    }
                }
                if (ab.aoe==true)
                --this.auses;
                if (toadd.size()>0&&user.dead==false) 
                {
                    for (StatEff eff: toadd) //does the same thing Use does; saves stateffs to be applied to self so ab can apply them to user
                    {
                        user.activeability.selfapply.add(eff);
                    }
                }
            }
            //chain ends
            ResetChain(ab); //reset base only after chain is done being called; otherwise damage should remain half of what it was the previous call
        }
        else if (this.multi==false&&victim.dead==true) //chain one more time for each kill; occurs when chain is called before attack is over, or chain triggers itself
        {
            this.auses++;
            if (ab.done==true&&this.used==false) //so the last enemy killed by aoe attack still triggers chain; above case excludes this
            this.Use(user, 616, victim);
        }
        return 616;
    }
    public void ResetChain(Ability a) 
    {
        this.used=false; 
        this.base=a.GetBaseDmg();
        if (this.multi==true)
        this.auses=a.GetMultihit(true)+1;
        else if (a.aoe==true)
        auses=0;
        else
        auses=1;        
    }
}
class RedwingHelper extends SpecialAbility //called by character.attack, after damage formula and before taking damage; elusive attacks don't check for redwing
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
            victim.remove(removee.id, "normal");
            victim.helpers.remove(remov);
            victim.immunities.remove("Debuffs"); 
        }
    }
}
class Use extends SpecialAbility //specialab so empowerments can be undone before it's triggered, since empowerments are supposed to only affect one ab at a time
{
    Ability ab; String condition; 
    public Use (Ability ability, String usecase) //uses an ability outside of a turn, for characters like paladin and unstoppable colossus
    {
        this.ab=ability; this.condition=usecase; 
        switch (condition)
        {
            case "maw": this.desc="If the target has Persuaded, make them Use one of their abilities."; break;
            case "default": this.desc="Use "+ab.oname+". "; break; //ability is always used, no matter what
        }
    }
    @Override
    public int Use (Character hero, int dmgdealt, Character victim) //after hero finishes attacking and using afterabs
    {
        if (CheckCond(hero, dmgdealt, victim)==true) //meets use condition, e.g. paladin needs to do 60+ damage with his basic attack
        {
            Character user=null; //user is the one using the ab and should always be the one the ability belongs to
            ArrayList<Character> targets=null;
            if (hero.index==67) //ebony maw makes his target use an ability on anyone he wants (ignores targeting effs since he does, but not invisible/untargetable)
            {
                user=victim;
                targets=Battle.ChooseTarget(hero, "either", ab.target);
            }
            else //the hero is using one of their abs
            {
                user=hero;
                targets=Battle.ChooseTarget(hero, ab.friendly, ab.target);
            }
            int ocd=ab.dcd; //save original cooldown, since useab sets it on cooldown and Use shouldn't do that
            ArrayList<StatEff> toapply=ab.UseAb(user, targets); //stateffs to be applied to user
            ab.dcd=ocd;
            if (toapply.size()>0&&user.dead==false) 
            {
                if (hero.index==67) //it's the maw's turn, so apply stateffs directly to the victim
                {
                    for (StatEff eff: toapply)
                    {
                        StatEff.CheckApply(user, user, eff);
                    }
                }
                else
                {
                    for (StatEff eff: toapply) //it's the hero's turn, so they should only gain the stateffs after turn end, to avoid premature expiry
                    {
                        user.activeability.selfapply.add(eff);
                    }
                }
            }
        }
        return 0;
    }
    private boolean CheckCond (Character hero, int dmgdealt, Character vic) 
    {
        boolean toret=true;
        switch (condition)
        {
            case "maw": toret=(vic.CheckFor("Persuaded", false)==true); break;
        }
        return toret;
    }
}