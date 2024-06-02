package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/7/22
 * Filename: StatEff
 * Purpose: Has types of status effects.
 */
public abstract class StatEff 
{   
    int duration=616; int chance=500; int power=616;
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
    public void onTurnEnd (Character hero) //by default all stateffs do this; only dot ticks down on turnstart
    {
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(this.hashcode, "normal");
        }
    }
    public abstract String geteffname(); //name to be displayed on scoreboard, including strength and duration
    public abstract void onApply(Character target);
    public abstract String getimmunityname(); //just the generic name of the eff, e.g. Target
    public abstract String getefftype();
    public String getalttype() //this is primarily for debuffeffs to override; damaging vs non damaging; also used for things like iceman's freeze 
    {
        return "knull";
    }
    public void Nullified(Character target) //triggered when a stateff is removed to undo its effects; not necessarily meant for only when Nullified
    {
    }
    public void Extended (int d, Character hero)
    {
        this.duration+=d;
        if (this.duration<=0)
        {
            hero.remove(this.hashcode, "normal");
        }
    }
    public void PrepareProtect (Character tank, Character weak)
    {
    }
    public int UseGuard (int dmg) //dmg -= guard strength and guard loses a charge
    {
        return dmg;
    }
    public void UseBanish() //literally just --duration
    {
    }
    public int UseEmpower(Character user, Ability b, boolean a) //buff stat chance/attack or add tempstrings to apply
    {
        return 0;
    }
    public Character UseTerrorProvoke() //return lad caller is afraid of/provoked by
    {
        return null;
    }
    public void Attacked(Character hero, Character attacker, int dmg) //called when hero is attacked; used for paralyse, counter, reflect, etc
    {
        if (hero.binaries.contains("Missed")) //if his counterattack was evaded before; needed since miss is normally only cleared after using an ab
        hero.binaries.remove("Missed");
    }
    public void Attacked(StatEff e) //called when hero gains a stateff; for debilitate, fortify, etc
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
        else if (effect.getefftype().equalsIgnoreCase("Debuffs")&&hero!=null&&hero.CheckFor("Neutralise", false)==true&&!(hero.ignores.contains("Neutralise")))
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Buffs")&&hero!=null&&hero.CheckFor("Undermine", false)==true&&!(hero.ignores.contains("Undermine")))
        { 
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getefftype().equalsIgnoreCase("Heal")&&hero!=null&&hero.CheckFor("Afflicted", false)==true&&!(hero.ignores.contains("Afflicted")))
        {
            StatEff.applyfail(hero, effect, "conflict");
        }
        else if (effect.getimmunityname().equals("Speed")||effect.getimmunityname().equals("Snare"))
        {
            boolean snare=false, speed=false;
            if (effect.getimmunityname().equals("Speed"))
            speed=true;
            else if (effect.getimmunityname().equals("Snare"))
            snare=true;
            Character[] friends=Battle.GetTeammates(target);
            boolean good=true;
            for (int i=0; i<friends.length; i++) //can only apply if no teammates have it; max one per team
            {
                if (speed==true)
                {
                    if (friends[i]!=null&&friends[i].CheckFor("Speed", false)==true) 
                    {
                        good=false; break;
                    }
                }
                else if (snare==true)
                {
                    if (friends[i]!=null&&friends[i].CheckFor("Snare", false)==true)
                    {
                        good=false; break;
                    }
                }
            }
            if (good==true) //no duplicates on teammates but still need to check for duplicates on self; no double snare/speed allowed
            {
                boolean apple=effect.CheckStacking(target, effect, effect.stackable); 
                if (apple==true)
                {
                    target.add(effect);
                }
                else
                StatEff.applyfail(hero, effect, "dupe");
            }
            else
            StatEff.applyfail(hero, effect, "dupe");
        }
        else if (target.dead==false)
        {
            boolean apple=effect.CheckStacking(target, effect, effect.stackable); 
            if (apple==true)
            target.add(effect);
            else
            StatEff.applyfail(hero, effect, "dupe");
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
                        target.remove(targeteff.hashcode, "normal"); 
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