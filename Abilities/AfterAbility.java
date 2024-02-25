package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: AfterAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
import java.util.ArrayList;
public abstract class AfterAbility extends SpecialAbility //applied after a hero attacks
{
    public AfterAbility()
    {
    }
}
class Activate extends AfterAbility //forcibly ticks all stateffs of a given type on the target down to 0, optionally doing extra dmg for each
{
    boolean type; //determines whether to get name or type
    String name; //of eff to activate
    int dmg; //dmg to be dealt for each eff activated
    public Activate (boolean t, String n, int d)
    {
        type=t; name=n; dmg=d;
    }
    @Override 
    public void Use (Character user, Character target, int ignoreme)
    {
        ArrayList<StatEff> toact= new ArrayList<StatEff>();
        if (type==true) 
        {
            toact=CoinFlip.GetEffs(target, "any", name);
        }
        else
        {
            toact=CoinFlip.GetEffs(target, name, "any");
        }
        int todeal=0;
        if (toact.size()>0&&!(user.binaries.contains("Missed"))) //then reduce their duration to 0
        {
            for (StatEff eff: toact) 
            {
                do 
                { 
                    eff.onTurnEnd(target); 
                }
                while (eff.duration>0);
                todeal+=dmg; //doing extra dmg for each eff target had
            }
            if (todeal>0&&target.dead==false)
            {
                if (user.ignores.contains("Defence"))
                {
                    todeal-=target.ADR; todeal-=target.DR; todeal-=target.PRDR;
                    todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                    target.TakeDamage(target, user, todeal, false);  
                }
                else
                {
                    todeal-=target.ADR; todeal-=target.DR; todeal-=target.RDR; todeal-=target.PRDR;
                    todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                    target.TakeDamage(target, user, todeal, false);    
                }    
            }
        }
    }
}
class Confidence extends AfterAbility
{
    int amount; int chance; 
    public Confidence(int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (caller.CheckFor(caller, "Afflicted")==false) //confidence is a heal ability
        {
            boolean success=CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&target.dead==false)
            {
                Character.Healed(target, amount, false);
                Character.Shielded(target, amount);
            }
        }
        else
        System.out.println (caller.Cname+"'s Confidence failed to apply due to a conflicting status effect.");
    }
}
class Extend extends AfterAbility
{
    String[] effname; //name of eff(s) to extend since mandarin can extend more than just one type
    String[] efftype; //including nondmging debuffs
    int chance; 
    int number; //of effs to extend
    int turns; //number of turns to extend eff by 
    String type; //whether all effs are extended or just some; chosen or random
    boolean together; //true for together and false for separate
    boolean self;
    public Extend (int echance, int num, String type, String[] ename, int turns, String[] etype, boolean self, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; efftype=etype; this.type=type; this.self=self; this.turns=turns;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before affecting an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Extend"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=null;
            if (efftype[0].equals("nondamaging"))
            effs=CoinFlip.GetEffsND(target);
            else
            effs=CoinFlip.GetEffs(target, effname, efftype); //the effs on the target eligible to be extended
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    ExtendChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    ExtendRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    ExtendAll(user, target, effs);
                }
                else
                {
                    System.out.println("Extend failed due to a spelling error.");
                }
            }
        }
        else
        {
            System.out.println (user.Cname+"'s Extend failed to apply due to an immunity.");
        }
    }
    public void ExtendChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" effect(s) to Extend on "+target.Cname+".");
            System.out.println ("Enter the number next to it, not its name."); 
            for (int i=0; i<todo; i++)
            {
                boolean falg=false; int counter=0; int index=616; 
                for (StatEff eff: effs)
                {
                    int o=counter+1;  
                    System.out.println(o+": "+eff.geteffname());
                    ++counter;
                }            
                do
                {
                    index=Damage_Stuff.GetInput();
                    --index;
                    if (index>=0&&index<effs.size())
                    {
                        falg=true;
                    }
                }
                while (falg==false);
                StatEff ton= effs.get(index);
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Extended by "+turns+" turn(s)!");
                ton.Extended(turns, target);   
                effs.remove(ton); //can't extend the same stateff twice with one usage of Extend
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void ExtendRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0)
        {
            for (int i=0; i<todo; i++)
            {
                int rando=(int) (Math.random()*(effs.size()-1));
                StatEff get=effs.get(rando);
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Extended by "+turns+" turn(s)!");
                get.Extended(turns, target);      
                effs.remove(get);
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void ExtendAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+turns+" turn(s)!");
                    eff.Extended(turns, target);
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+turns+" turn(s)!");
                    eff.Extended(turns, target);                    
                }
                else
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                }
            }
        }
    }
}
class Mend extends AfterAbility
{
    int amount; int chance; 
    public Mend (int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (caller.CheckFor(caller, "Afflicted")==false) 
        {
            boolean success=CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&target.dead==false)
            {
                Character.Healed(target, amount, false);
            }
        }
        else
        System.out.println (caller.Cname+"'s Mend failed to apply due to a conflicting status effect.");
    }
}
class Multichain extends AfterAbility //continue multihit attack on another target
//might need to disable after an attack to ensure it cant trigger more than once
{ 
    int multis; //number of hits in attack
    int current=0; //current hit of attack
    AttackAb ability; 
    boolean used=false;
    public Multichain(int multiply, AttackAb ability)
    {
        multis=multiply; this.ability=ability;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (current<multis)
        {
            ++current;
            if (used==false&&target.dead==true) //doesn't activate on final hit because there's no more dmg to deal
            {
                used=true;
                UseMultichain(caller);
            }
        }
        if (current>multis)
        {
            current=0; //reset counter because attack is over
            used=false;
        }
    }
    public void UseMultichain (Character user) //similar to chain; precondition of ability's aoe being false; code is copied from attackab.use()
    {
        int uses=multis-current; 
        int odamage=ability.odamage; int damage=odamage;
        ArrayList<StatEff> chumpapply= new ArrayList<StatEff>(); //to apply to the new, random target
        //continue functions from last hit since they were cut off
        for (String[][] array: ability.tempstrings)
        {  
            StatEff New=StatFactory.MakeStat(array); 
            if (array[0][4].equalsIgnoreCase("true"))
            {
                ability.selfapply.add(New);
            }
            //doesn't change otherapply since attack's original target is dead and these effects are from that
        }
        for (String[][] array: ability.statstrings)
        {  
            StatEff New=StatFactory.MakeStat(array); 
            if (array[0][4].equalsIgnoreCase("true"))
            {
                ability.selfapply.add(New);
            }
        }
        for (StatEff eff: user.effects) //undo empowerments
        {
            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
            {
                //int n=eff.UseEmpower(user, ability, damage, false);
            }
        }
        if (user.binaries.contains("Missed"))
        {
            user.binaries.remove("Missed");
        }
        ability.damage=odamage; //reset damage 
        ability.dmgdealt=0;
        //then start attack on new target
        Character chump=Ability.GetRandomHero (user, false);
        while (uses>0) 
        {
            int change=0; 
            if (chump.dead==true)
            chump=Ability.GetRandomHero (user, false);
            if (chump!=null)
            {
                for (StatEff eff: user.effects) //get empowerments for the rest of the attack
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        //change=eff.UseEmpower(user, ability, damage, true);
                    }
                } 
                for (SpecialAbility ob: ability.special)
                {
                    change=ob.Use(user, chump); //apply before abs
                } 
                damage+=change;
                chump=user.Attack(user, chump, damage, false); //damage formula is calculated here
                for (SpecialAbility ob: ability.special)
                {
                    ob.Use(user, chump, ability.dmgdealt); //activates after abs
                } 
                for (String[][] array: ability.tempstrings) 
                {  
                    StatEff New=StatFactory.MakeStat(array); 
                    if (array[0][4].equalsIgnoreCase("true"))
                    {
                        ability.selfapply.add(New);
                    }
                    else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                    {
                        chumpapply.add(New);
                    }
                }
                for (String[][] array: ability.statstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array); 
                    if (array[0][4].equalsIgnoreCase("true"))
                    {
                        ability.selfapply.add(New);
                    }
                    else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                    {
                        chumpapply.add(New);
                    }
                }
                ArrayList<StatEff> emptylist= new ArrayList<StatEff>(); //empty list in order to meet parameters
                emptylist=Ability.ApplyStats(user, chump, ability.together, emptylist, chumpapply);
                for (StatEff eff: user.effects) //undo empowerments
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        //change=eff.UseEmpower(user, ability, damage, false);
                    }
                }
                if (user.binaries.contains("Missed"))
                {
                    user.binaries.remove("Missed");
                }
                damage=odamage; //reset damage 
                ability.dmgdealt=0;
            }
            --uses;
            --ability.multihit;
        }
    }
}        
class Nullify extends AfterAbility
{
    String effname;
    int chance; 
    int number; //of buffs to nullify
    String type; //whether all buffs are nullified or some; chosen orr andom
    String efftype="Buffs";
    boolean together; //true for together and false for separate
    boolean self;
    public Nullify (int echance, int num, String type, String ename, boolean self, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; this.type=type; this.self=self;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before nullifying an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, effname, efftype); //the buffs on the target eligible to be nullified
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    NullifyChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    NullifyRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    NullifyAll(user, target, effs);
                }
                else
                {
                    System.out.println("Nullify failed due to a spelling error.");
                }
            }
        }
        else
        {
            System.out.println (user.Cname+"'s Nullify failed to apply due to an immunity.");
        }
    }
    public void NullifyChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" buff(s) to Nullify from "+target.Cname+".");
            System.out.println ("Enter the number next to it, not its name."); 
            for (int i=0; i<todo; i++)
            {
                boolean falg=false; int counter=0; int index=616; 
                for (StatEff eff: effs)
                {
                    int o=counter+1;  
                    System.out.println(o+": "+eff.geteffname());
                    ++counter;
                }            
                do
                {
                    index=Damage_Stuff.GetInput();
                    --index;
                    if (index>=0&&index<effs.size())
                    {
                        falg=true;
                    }
                }
                while (falg==false);
                StatEff ton= effs.get(index);
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Nullified!");
                target.remove(target, ton.hashcode, "nullify");                     
                effs.remove(index);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void NullifyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0)
        {
            for (int i=0; i<todo; i++)
            {
                int rando=(int) (Math.random()*(effs.size()-1));
                StatEff get=effs.get(rando);
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Nullified!");
                target.remove(target, get.hashcode, "nullify");                
                effs.remove(rando);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void NullifyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                    target.remove(target, eff.hashcode, "nullify"); 
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                    target.remove(target, eff.hashcode, "nullify");                     
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class Purify extends AfterAbility
{
    String effname; //name of debuff(s) to be purified
    String efftype="Debuffs";
    int chance; 
    int number;
    String type; //chosen, random, or all
    boolean together; //true for together and false for separate
    boolean self;
    public Purify (int echance, int num, String t, String ename, boolean self, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; type=t; this.self=self;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before purifying an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Purify"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, effname, efftype); //only gather the target's debuffs
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    PurifyChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    PurifyRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    PurifyAll(user, target, effs);
                }
                else
                {
                    System.out.println("Purify failed due to a spelling error.");
                }
            }
        }
        else
        {
            System.out.println (user.Cname+"'s Purify failed to apply due to an immunity.");
        }
    }
    public void PurifyChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" debuff(s) to Purify from "+target.Cname+"."); 
            System.out.println ("Enter the number next to it, not its name."); 
            for (int i=0; i<todo; i++)
            {
                boolean falg=false; int counter=0; int index=616; 
                for (StatEff eff: effs) //list the debuffs
                {
                    int o=counter+1;  
                    System.out.println(o+": "+eff.geteffname());
                    ++counter;
                }            
                do
                {
                    index=Damage_Stuff.GetInput();
                    --index;
                    if (index>=0&&index<effs.size()) //valid number typed in
                    {
                        falg=true;
                    }
                }
                while (falg==false);
                StatEff ton= effs.get(index);                  
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Purified!");
                target.remove(target, ton.hashcode, "purify");   
                effs.remove(index);
                if (effs.size()<=0)
                {
                    break; //end loop
                }
            }
        }
    }
    public void PurifyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0)
        {
            for (int i=0; i<todo; i++)
            {
                int rando=(int) (Math.random()*(effs.size()-1));
                StatEff get=effs.get(rando);
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Purified!");
                target.remove(target, get.hashcode, "purify");
                effs.remove(rando);
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void PurifyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Purified!");
                    target.remove(target, eff.hashcode, "purify"); 
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {                    
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Purified!");
                    target.remove(target, eff.hashcode, "purify"); 
                }
                else
                {
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                }
            }
        }
    }
}
class Ricochet extends AfterAbility //do ricochet damage
{
    int chance;
    public Ricochet(int cchance)
    {
        chance=cchance; 
    }
    @Override
    public void Use (Character user, Character target, int dmg) //user is the one doing the damage 
    { 
        if ((!(user.binaries.contains("Missed"))||user.immunities.contains("Missed")))
        {
            boolean success=CoinFlip.Flip(chance+user.Cchance);
            if (success==true&&dmg>5) //sends over ab's dmg dealt for ricochet calculation
            {
                Ability.DoRicochetDmg (dmg, target, true);            
            }
        }
    }
}