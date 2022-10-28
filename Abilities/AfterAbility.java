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
        int todeal=0;
        if (type==true) //look for effs to be activated
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getefftype().equalsIgnoreCase(name))
                {
                    toact.add(eff);
                }
            }
        }
        else //(type==false)
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase(name))
                {
                    toact.add(eff);
                }
            }
        }
        if (toact.size()>0) //then reduce their duration to 0
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
            todeal-=target.ADR; todeal-=target.DR; todeal-=target.RDR;
            if (todeal>0&&target.dead==false)
            {
                System.out.println("\n"+target.Cname+" took "+todeal+" damage");
                target.TakeDamage(target, todeal, false);                
            }
        }
    }
}
class AddIgnore extends AfterAbility
{
    String condition; String ignorethis; int threshold; //health threshold, needed for lads like Corvus
    public AddIgnore (String igthis, String condie, int thresh)
    {
        condition=condie; ignorethis=igthis; threshold=thresh;
    }
    @Override
    public int Use (Character user, Character target)
    {
        boolean okay=CheckIgCondition(condition, target, threshold);
        if (okay==true)
        {
            user.ignores.add (ignorethis);
        }
        return 0;
    }
    @Override 
    public void Use (Character user, Character target, int ignoreme)
    {
        user.ignores.remove (ignorethis);
    }
    public static boolean CheckIgCondition (String condition, Character target, int hp)
    { 
        switch (condition) 
        {
            case "none": return true; //will add more as needed
            default: return false;
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
