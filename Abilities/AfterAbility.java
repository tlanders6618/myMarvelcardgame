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
class Activate extends AfterAbility
{
    boolean type; //determines whether to get name or type
    String name; //of eff to activate
    int dmg;
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
            toact=Card_CoinFlip.GetEffs(target, "any", name);
        }
        else
        {
            toact=Card_CoinFlip.GetEffs(target, name, "any");
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
                while (eff.getduration(false)>0);
                todeal+=dmg;
            }
            if (target.dead==false)
            {
                boolean nomiss=true;
                if (user.ignores.contains("Defence"))
                {
                    nomiss=Damage_Stuff.CheckBlind(user);
                    if (nomiss==true)
                    {
                        boolean evade=Damage_Stuff.CheckEvade(user, target, true);
                        if (evade==false)
                        {
                            todeal-=target.ADR; todeal-=target.DR;
                            todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                            System.out.println("\n"+target.Cname+" took "+todeal+" damage");
                            target.TakeDamageIgShield(target, todeal, false);  
                        }
                    }
                }
                else
                {
                    nomiss=Damage_Stuff.CheckBlind(user);
                    if (nomiss==true)
                    {
                        boolean evade=Damage_Stuff.CheckEvade(user, target, true);
                        if (evade==false)
                        {
                            todeal-=target.ADR; todeal-=target.DR; todeal-=target.RDR;
                            todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                            System.out.println("\n"+target.Cname+" took "+todeal+" damage");
                            target.TakeDamage(target, todeal, false);    
                        }
                    }   
                }    
            }
        }
    }
}
class Confidence extends AfterAbility
{
    int amount; int chance; Ricochet r= new Ricochet(1);
    public Confidence(int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character mj, int ignore) 
    {
        if (caller.CheckFor(caller, "Heal Disable")==false&&!(mj.immunities.contains("Heal"))) //confidence is a heal ability
        {
            boolean success=Card_CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&mj.dead==false)
            {
                mj.Healed(mj, amount);
                mj.Shielded(mj, amount);
            }
        }
    }
}
class Extend extends AfterAbility
{
    String[] effname; String[] efftype;
    int chance; 
    int duration;
    int number;
    String type;
    boolean together; //true for together and false for separate
    public Extend (int echance, int num, String t, String[] ename, String[] etype, int dur, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; efftype=etype; duration=dur; type=t;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        if (!(target.immunities.contains("Extend"))&&!(target.immunities.contains("Other"))) 
        {
            if (target.team1!=user.team1&&!(user.binaries.contains("Missed"))) //no need to check for misses on self or a teammate
            { 
                ArrayList <StatEff> effs=Card_CoinFlip.GetEffs(target, effname, efftype);
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
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println (hero.Cname+", choose "+todo+" effect(s) to Extend on "+target.Cname+".");
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
                StatEff toext= effs.get(index);
                System.out.println(target.Cname+"'s "+toext.geteffname()+" was Extended by "+duration+" turn(s)");
                toext.Extended(duration);                     
                effs.remove(index);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void ExtendRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Extended by "+duration+" turn(s)");
                get.Extended(duration); 
                effs.remove(rando);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void ExtendAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+duration+" turn(s)");
                    eff.Extended(duration); 
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+duration+" turn(s)");
                    eff.Extended(duration); 
                }
                else
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                }
            }
        }
    }
}
class Nullify extends AfterAbility
{
    String[] effname;
    int chance; 
    int duration;
    int number;
    String type;
    boolean together; //true for together and false for separate
    public Nullify (int echance, String t, int num, String[] ename, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; type=t;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        if (!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))&&!(user.binaries.contains("Missed"))) 
        {
            int h=effname.length; String[] efftype=new String[h];
            for (int i=0; i<h; i++)
            {
                efftype[i]="Buffs";
            }
            ArrayList <StatEff> effs=Card_CoinFlip.GetEffs(target, effname, efftype);  
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
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println (hero.Cname+", choose "+todo+" buff(s) to Nullify on "+target.Cname+".");
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
                target.remove(target, ton.getcode());     
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Nullified!");
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
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
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
                target.remove(target, get.getcode());
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Nullified!");
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
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    target.remove(target, eff.getcode()); 
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
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
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    target.remove(target, eff.getcode()); 
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class Ricochet extends AfterAbility
{
    int chance;
    public Ricochet(int cchance)
    {
        chance=cchance; 
    }
    @Override
    public void Use (Character user, Character target, int dmg) //user is the one doing the damage
    { 
        boolean success=Card_CoinFlip.Flip(chance+user.Cchance);
        if (success==true&&dmg>5)
        {
            Ability.DoRicochetDmg (dmg, target, true);            
        }
    }
}