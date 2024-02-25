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
            hero.remove(hero, this.hashcode, "normal");
        }
    }
    public abstract String geteffname(); //name to be displayed on scoreboard, including strength and duration
    public abstract void onApply(Character target);
    public abstract String getimmunityname(); //just the generic name of the eff, e.g. Target
    public abstract String getefftype();
    public String getalttype() //this is primarily for debuffeffs to override; damaging vs non damaging
    {
        return "knull";
    }
    public void Nullified(Character target) //triggered when a stateff is removed to undo its effects; not necessarily meant for only when nullified
    {
    }
    public void Extended (int d, Character hero)
    {
        this.duration+=d;
        if (this.duration<=0)
        {
            hero.remove(hero, this.hashcode, "normal");
        }
    }
    public void PrepareProtect (Character tank, Character weak)
    {
    }
    public void UseCounter(Character hero, Character attacker) 
    {
        if (hero.binaries.contains("Missed")) //if his counterattack was evaded before; needed since miss is normally only cleared after using an ab
        hero.binaries.remove("Missed");
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
    public int UseEmpower(Character user, Ability b, boolean a) //buff stat chance/attack or add stats to apply, then --uses; if uses=0, remove from user
    {
        return 0;
    }
    public int UseTerrorProvoke() //return hashcode of lad caller is afraid of unless lad is invisible or dead
    {
        return 616;
    }
    public void Attacked(Character hero) //used for paralyse, vapor's countdowns, etc
    {
    }
    public void Attacked(StatEff e) //used for vapor's countdowns and debilitate
    {
    }
    public static void applyfail (Character hero, StatEff eff, String cause) //error message for stateff application failure
    {
        String start;
        if (hero!=null)
        start=hero.Cname+"'s "+eff.geteffname();
        else
        start=eff.geteffname();
        switch (cause)
        {
            case "immune": System.out.println(start+" failed to apply due to an immunity."); break;
            case "chance": System.out.println(start+" failed to apply due to chance."); break;
            case "conflict": System.out.println(start+" could not be applied due to a conflicting status effect."); break; //shatter and disable debuffs
            case "dupe": System.out.println(start+" could not be applied due to a duplicate status effect."); break;
            default: System.out.println("Forgot to program an error message for this kind of stateff failure."); 
        }
    }
    public static void CheckApply (Character hero, Character target, StatEff effect) //see if stateffs can be applied to their target; called after Ability.ApplyStats
    {
        if (target.immunities.contains(effect.getefftype())||target.immunities.contains(effect.getimmunityname()))
        {
            StatEff.applyfail(hero, effect, "immune");
        }
        else if (effect.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Neutralise")==true)
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Undermine")==true)
        { 
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Afflicted")==true)
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (target.dead==false)
        {
            boolean apple=effect.CheckStacking(target, effect, effect.stackable); 
            if (apple==true)
            {
                target.add(target, effect);
            }
            else
            {
                StatEff.applyfail(hero, effect, "dupe");
            }
        }
    }
    public boolean CheckStacking (Character target, StatEff effect, boolean stackable) //checks if a duplicate stateff may be applied
    {
        if (stackable==true)
        {
            return true;
        }
        else //if (stackable==false) //non stackable debuffs won't be applied unless their strength is higher than the one already present on the hero
        {
            for (StatEff targeteff: target.effects)
            {
                if (targeteff.getimmunityname().equals(effect.getimmunityname())&&targeteff.getefftype().equals(effect.getefftype())) 
                {   //there's already an effect of the exact same type trying to be applied
                    if (effect.power>targeteff.power) //but if the new effect is stronger, it replaces the old one
                    {
                        target.remove(target, targeteff.hashcode, "normal"); 
                        return true;
                    }
                    else
                    {
                        return false;
                    } 
                }
            }
            return true; //if the eff is non stackable but the hero doesn't already have it
        }
    }
}