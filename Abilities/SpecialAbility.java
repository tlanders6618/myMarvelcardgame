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
   public SpecialAbility()
   {
   }
   public int Use(Character hero, Character target)
   {
       return 0;
   }
   public void Use (Character caller, Character target, int dmg) 
   {
   }
   public int Use (Character victim, int dmg, Character attacker)
   {
       return dmg;
   }
   public void Undo (Character victim)
   {
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
            SpecialAbility[] remov= new SpecialAbility[1];
            for (SpecialAbility f: victim.helpers)
            {
                if (f instanceof RedwingHelper)
                {
                    remov[0]=f;
                    break; //since there can only be one redwing
                }
            }
            StatEff[] removee= new StatEff[1];
            for (StatEff ef: victim.effects)
            {
                if (ef.getimmunityname().equalsIgnoreCase("Redwing"))
                {
                    removee[0]=ef;
                    break; //since there can only be one redwing
                }
            }
            victim.remove(victim, removee[0].getcode());
            victim.helpers.remove(remov[0]);
            victim.immunities.remove("Debuffs"); 
        }
    }
}