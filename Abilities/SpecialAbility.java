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
//a target's helpers trigger onattacked and all helpers are called in battle, AFTER their hero's turn ends
class BonusTurnHelper extends SpecialAbility 
{
    boolean used=false;
    public BonusTurnHelper()
    {
    }
    @Override
    public void Undo (Character hero) //bonus turn triggers at the end of a hero turn
    {
        if (used==false&&hero.dead==false&&hero.activeability.channelled==false)  //since helpers are triggered after using a channelled ability, right before hero starts their turn
        {
            Battle.Turn(hero, true); //hero can only take a bonus turn after finishing their turn, so it cannot be triggered after using a channelled skill
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
            victim.remove(victim, removee.hashcode, "normal");
            victim.helpers.remove(remov);
            victim.immunities.remove("Debuffs"); 
        }
    }
}