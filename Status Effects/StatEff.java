package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/7/22
 * Filename: StatEff
 * Purpose: Has types of status effects.
 */
public abstract class StatEff 
{   
    int duration=616; int chance=616; int power=616;
    int oduration=616; //for copy/steal 
    int hashcode; boolean stackable=false; 
    public void StatEff ()
    {   
    }
    public Character getProtector()
    {
        return null;
    }
    public void onTurnStart (Character hero)
    {
    }
    public void onTurnEnd (Character hero)
    {
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(hero, this.hashcode, false);
        }
    }
    public abstract String geteffname();
    public abstract void Nullified(Character target); 
    public abstract void onApply(Character target);
    public abstract String getimmunityname();
    public abstract String getefftype();
    public void Extended (int d)
    {
        this.duration+=d;
    }
    public void PrepareProtect (Character tank, Character weak)
    {
    }
    public void UseCounter(Character hero, Character attacker) //dmg based on status effects like damage up weakness and resistance
    {
        if (attacker.dead==false)
        { //calc damage and do it to them
            //dont forget to add activation text
        }
    }
    public int UseGuard (int dmg) //dmg -= guard strength and guard loses a charge
    {
        return dmg;
    }
    public void UseReflect(Character dealer, int dmg) //dealer takes dmg/2, using health loss method not take damage method
    {
        if (dealer.dead==false)
        { //calc damage and do it to them
            //dont forget to add activation text
        }
    }
    public void UseBanish() //literally just --duration
    {
    }
    public int UseEmpower(Character user, Ability b, int dmg, boolean a) //buff stat chance/attack or add stats to apply, then --uses; if uses=0, remove from user
    {
        return dmg;
    }
    public int UseTerrorProvoke() //return hashcode of lad caller is afraid of unless lad is invisible or dead
    {
        return 616;
    }
    public static void applyfail (Character hero, StatEff eff)
    {
        if (hero!=null)
        {
            System.out.println (hero.Cname+"'s "+eff.geteffname()+" could not be applied due to an immunity, chance, or a conflicting status effect");
        }
        else
        {
            System.out.println (eff.geteffname()+" could not be applied due to an immunity, chance, or a conflicting status effect");
        }
    }
    public void CheckApply (Character hero, Character target, StatEff effect)
    {
        boolean apple=true; 
        if (target.immunities.contains(effect.getefftype())||target.immunities.contains(effect.getimmunityname()))
        {
            StatEff.applyfail(hero, effect);
            apple=false;
        }
        else if (effect.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
        {
            StatEff.applyfail(hero, effect);
            apple=false;
        }
        else if (apple==true&&target.dead==false)
        {
            apple=effect.CheckStacking(target, effect, effect.stackable); 
            if (apple==true)
            {
                target.add(target, effect);
            }
            else
            {
                StatEff.applyfail(hero, effect);
            }
        }
    }
    public boolean CheckStacking (Character target, StatEff effect, boolean stackable)
    {
        //checks stackability
        boolean applied=false; 
        if (stackable==true)
        {
            applied=true;
        }
        else if (stackable==false) //non stackable debuffs won't be applied unless their value is higher than the one already present on the hero
        {
            boolean present=false; 
            for (StatEff targeteff: target.effects)
            {
                if (targeteff.getimmunityname().equals(effect.getimmunityname())&&targeteff.getefftype().equals(effect.getefftype())) 
                {
                    present=true; //there's already an effect of the type trying to be applied
                    if (targeteff.power<effect.power) //but if the new effect is stronger, it replaces the old one
                    {
                        target.remove(target, targeteff.hashcode, false); 
                        applied=true;
                    }
                    else
                    {
                        applied=false;
                    }
                    break; 
                }
            }
            if (present==false)
            {
                applied=true;
            }
        }
        return applied;
    }
}