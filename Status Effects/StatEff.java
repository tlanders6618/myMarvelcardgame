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
    int id; boolean stackable=false; 
    Character prog; //progenitor who made the stateff; needed for elsa, magneto, etc
    public void StatEff () //bluej won't let me modify this for some reason, so all its subclasses instead have the same redundant constructor
    {   
    }
    @Override
    public String toString()
    {
        return this.geteffname();
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
            hero.remove(this.id, "normal");
        }
    }
    public abstract String geteffname(); //name to be displayed on scoreboard, including strength and duration
    public abstract String getimmunityname(); //just the generic name of the eff, e.g. Target
    public abstract String getefftype();
    public String getalttype() //this is primarily for debuffeffs to override; damaging vs non damaging; also used for things like scarecrow's fear
    {
        return "knull";
    }
    public void onApply(Character target)
    {
    }
    public void Nullified(Character target) //triggered when a stateff is removed to undo its effects; not necessarily meant for only when Nullified
    {
    }
    public void Extended (int d, Character hero)
    {
        this.duration+=d;
        if (this.duration<=0)
        {
            hero.remove(this.id, "normal");
        }
    }
    public void PrepareProtect (Character tank, Character weak)
    {
    }
    public int UseGuard (Character dealer, Character targ, int dmg) //dmg -= guard strength and guard loses a charge
    {
        return dmg;
    }
    public static boolean CheckBanish (Character target) //return whether banish can be applied to target or not
    {
        if ((target.team1==true&&Battle.p1solo==true)||(target.team1==false&&Battle.p2solo==true)) //cannot be banished if solo
        {
            return false;
        }
        else //cannot be banished if all teammates already are
        {
            Character[] team=Battle.GetTeammates(target);
            for (Character ch: team)
            {
                if (ch!=null&&!(ch.binaries.contains("Banished"))) //ch is not banished, so target can be
                return true;
            }
            return false;
        }
    }
    public void UseBanish(Character c) //needed for linked banish
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
    }
    public void Attacked(StatEff e) //called when hero gains a stateff; for debilitate, fortify, etc
    {
    }
    public static void applyfail (Character target, StatEff eff, String cause) //error message for stateff application failure
    {
        String start;
        if (eff.prog!=null)
        start=eff.prog+"'s "+eff.getimmunityname();
        else
        start=eff.getimmunityname();
        switch (cause)
        {
            case "immune": System.out.println(start+" failed to apply to "+target+" due to an immunity."); break;
            case "chance": System.out.println(start+" failed to apply to "+target+" due to chance."); break;
            case "conflict": System.out.println(start+" could not be applied to "+target+" due to a conflicting status effect."); break; //shatter, safeguard, disrupt, disable debuffs
            case "dupe": System.out.println(start+" could not be applied to "+target+" due to a duplicate status effect."); break;
            case "banish": System.out.println(start+" could not be applied to "+target+" due to not meeting the Banish conditions."); break; //checkbanish failed, as per the glossary
            default: System.out.println("Forgot to program an error message for this kind of stateff failure."); 
        }
        for (Character c: Battle.team1)
        {
            if (c!=null)
            c.StatFailed(target, eff, cause);
        }
        for (Character c: Battle.team2)
        {
            if (c!=null)
            c.StatFailed(target, eff, cause);
        }
    }
    public static void CheckApply (Character hero, Character target, StatEff effect) //try to apply eff to target after checking for conflict; called after Ability.ApplyStats
    {   
        if (StatEff.CheckFail(hero, target, effect)==false)
        {
            target.add(effect, true);
        } //else check fail already prints the failure message so do nothing
    }
    public static boolean CheckFail (Character hero, Character target, StatEff effect) //same as above, but returns whether eff can be applied without applying it
    {   //checks everything but chance
        if (target.dead==true)
        {
            return true;
        }
        else if (target.immunities.contains(effect.getefftype())||target.immunities.contains(effect.getimmunityname())||target.immunities.contains(effect.getalttype()))
        {
            StatEff.applyfail(target, effect, "immune");
            return true;
        }
        else if (effect.getefftype().equalsIgnoreCase("Debuffs")&&((target.CheckFor("Safeguard", false)==true)||(hero!=null&&hero.CheckFor("Neutralise", false)==true&&!(hero.ignores.contains("Neutralise")))))
        {
            StatEff.applyfail(target, effect, "conflict");
            return true;
        }
        else if (effect.getefftype().equalsIgnoreCase("Buffs")&&((target.CheckFor("Disrupt", false)==true)||(hero!=null&&hero.CheckFor("Undermine", false)==true&&!(hero.ignores.contains("Undermine")))))
        {
            StatEff.applyfail(target, effect, "conflict");
            return true;
        }
        else if (effect.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
        {
            StatEff.applyfail(target, effect, "conflict");
            return true;
        }
        else if (effect.getefftype().equalsIgnoreCase("Heal")&&hero!=null&&hero.CheckFor("Afflicted", false)==true&&!(hero.ignores.contains("Afflicted")))
        {
            StatEff.applyfail(target, effect, "conflict");
            return true;
        }
        else if (effect.getimmunityname().equals("Speed")||effect.getimmunityname().equals("Snare"))
        {
            boolean snare=false, speed=false;
            if (effect.getimmunityname().equals("Speed"))
            speed=true;
            else if (effect.getimmunityname().equals("Snare"))
            snare=true;
            Character[] friends=Battle.GetTeammates(hero);
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
            if (good==false) //no duplicates on teammates but still need to check for duplicates on self; no double snare/speed allowed
            {
                StatEff.applyfail(target, effect, "dupe");
                return true;
            } //else run below code
        }
        //occurs if none of the above cases returned false
        boolean apple=effect.CheckStacking(target); 
        if (apple==true)
        return false;
        else
        {
            StatEff.applyfail(target, effect, "dupe");
            return true;
        }
    }
    public boolean CheckStacking (Character target) //checks if a stateff about to be applied to the target is a duplicate and if so, if it should be applied
    {
        if (this.stackable==true)
        { 
            return true;
        }
        else //if (stackable==false) //non stackable debuffs won't be applied unless their strength is higher than the one already present on the hero
        { 
            for (StatEff eff: target.effects)
            { 
                if (eff.getimmunityname().equals(this.getimmunityname())&&eff.getefftype().equals(this.getefftype())) 
                {   //there's already an effect of the exact same type trying to be applied 
                    if (this.power>eff.power) //but if the new effect is stronger, it replaces the old one
                    {
                        target.remove(eff.id, "normal"); 
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