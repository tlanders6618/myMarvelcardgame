package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: OtherEff
 * Purpose: To list all the Other status effects in one file.
 */
 
public abstract class OtherEff extends StatEff 
{
    public OtherEff ()
    {
    }
}
class EvadeE extends OtherEff
{
    String name="Evade Effect";
    int hashcode;
    int chance=616;
    @Override
    public int getduration (boolean copy)
    {
        return 616;
    }
    @Override
    public void Extended (int dur)
    {
    }
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return true;
    }
    @Override
    public String getimmunityname()
    {
        return "Evade";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void Nullified(Character target)
    {
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public EvadeE(int nchance) 
    {
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target) 
    {
    }
}
class Redwing extends OtherEff
{
    int hashcode;
    String name="Redwing";
    int oduration=616;
    @Override
    public int getduration (boolean copy)
    {
        return 616;
    }
    @Override
    public void Extended (int dur)
    {
    }
    @Override
    public int getchance()
    {
        return 200;
    }
    @Override
    public boolean getstackable()
    {
        return false;
    }
    @Override
    public String getimmunityname()
    {
        return "Redwing";
    }
    @Override 
    public String getefftype()
    {
        return "Other"; 
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public Redwing () 
    {
        hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character hero) 
    {
        hero.helpers.add(new RedwingHelper());
    }
    @Override
    public void Nullified(Character target)
    {
        for (SpecialAbility f: target.helpers)
        {
            if (f instanceof RedwingHelper)
            {
                target.helpers.remove(f);
                break; //since there can only be one redwing
            }
        }
    }
}
class WMTarget extends OtherEff 
{
    String name= "Target Effect: 5";
    boolean stackable=true;
    int hashcode;
    int power=5;
    @Override
    public int getduration (boolean copy)
    {
        return 616;
    }
    @Override
    public void Extended (int dur)
    {
    }
    public WMTarget ()
    {
        hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.DV+=power;
        target.immunities.add("Invisible");
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Invisible"))
            {
                target.remove(target, eff.getcode());
            }
        }
    }
    @Override
    public int getchance()
    {
        return 300;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
    @Override
    public String getimmunityname()
    {
        return "Target";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public int getpower()
    {
        return power;
    }
    @Override 
    public int getcode()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public void onTurnStart (Character hero)
    {
    }
    @Override
    public void onTurnEnd (Character hero)
    {
    }
    @Override
    public void Nullified(Character target)
    {
        target.DV-=power;
        target.immunities.remove("Invisible");
    }
}