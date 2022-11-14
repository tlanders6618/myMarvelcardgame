package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 18/8/22
 * Filename: SpecialAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
import java.util.ArrayList; 
public abstract class SpecialAbility 
{
   int identifier;
   public SpecialAbility()
   {
       identifier=Card_HashCode.RandomCode();
   }
   public int Use(Character hero, Character target) //for before abs
   {
       return 0;
   }
   public void Use (Character caller, Character target, int dmg) //for after abs
   {
   }
   public int Use (Character victim, int dmg, Character attacker) //only for special abs; not before or after abs
   {
       return dmg;
   }
   public void Undo (Character victim) //only for special abs; not before or after abs
   {
   }
}
class BonusTurnHelper extends SpecialAbility
{
    boolean used=false;
    public BonusTurnHelper()
    {
    }
    @Override
    public void Undo (Character hero)
    {
        if (used==false)
        {
            Battle.BonusTurn(hero);
            used=true;
        }
        else
        {
            for (SpecialAbility h: hero.helpers)
            {
                System.out.println(h.toString());
                if (h.identifier==identifier)
                {
                    hero.helpers.remove(h); System.out.println ("test: turn helper successfully removed"); break; 
                }
            }
        }
    }
}
class RedwingHelper extends SpecialAbility
{
    boolean used=false;
    public RedwingHelper ()
    {
    }
    @Override
    public int Use (Character victim, int dmg, Character attacker)
    {
        if (dmg>=120)
        {
            System.out.println ("Redwing takes the attack for "+victim.Cname+"!");
            double dub= dmg/2;
            dmg=5*(int)Math.ceil(dub/5);
            victim.immunities.add("Debuffs"); 
            used=true;
        }        
        return dmg;
    }    
    @Override
    public void Undo (Character victim)
    {
        if (used==true)
        {
            SpecialAbility remov=null;
            for (SpecialAbility f: victim.helpers)
            {
                if (f instanceof RedwingHelper)
                {
                    remov=f;
                    break; //since there can only be one redwing
                }
            }
            StatEff removee=null;
            for (StatEff ef: victim.effects)
            {
                if (ef.getimmunityname().equalsIgnoreCase("Redwing"))
                {
                    removee=ef;
                    break; //since there can only be one redwing
                }
            }
            victim.remove(victim, removee.getcode());
            victim.helpers.remove(remov);
            victim.immunities.remove("Debuffs"); 
        }
    }
}