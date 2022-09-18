package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: OtherEff
 * Purpose: To list all the Other status effects in one file.
 */
 
public abstract class OtherEff extends StatEff implements Statlistener
{
    public OtherEff ()
    {
    }
}
class DamageUpE extends OtherEff implements Statlistener
{
    int strength=616;
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=true;
    int hashcode;
    int oduration; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
    @Override
    public String getimmunityname()
    {
        return "Damage Up";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public int getpower()
    {
        return strength;
    }
    @Override 
    public int getcode()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        if (duration<500)
        {
            name="Damage Up Effect: "+strength+", "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Damage Up Effect: "+strength;
            return name;
        }
    }
    public DamageUpE (int nchance, int nstrength)
    {
        strength=nstrength;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public DamageUpE (int nchance, int nstrength, int nduration)
    {
        strength=nstrength;
        duration=nduration;
        oduration=duration;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onTurnStart (Character hero)
    {
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onApply (Character target)
    {
        target.BD+=strength;
    }
    @Override
    public void Nullified(Character target)
    {
        target.BD-=strength;
    }
}
class EvadeE extends OtherEff
{
    String name="Evade Effect";
    int hashcode;
    int chance=616;
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
class Redwing extends OtherEff implements Statlistener
{
    int hashcode;
    String name="Redwing";
    int oduration=616;
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
class ResistanceE extends OtherEff implements Statlistener
{
    boolean stackable=true;
    int hashcode;
    String name;
    int duration=616;
    int oduration=616;
    int power=616;
    int chance=616;
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
    @Override
    public String getimmunityname()
    {
        return "Resistance";
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
        if (duration>500)
        {
            name="Resistance Effect: "+power;
        }
        else
        {
            name="Resistance Effect: "+power+", "+duration+" turn(s)";
        }
        return name;
    }
    @Override
    public int getpower()
    {
        return power;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public ResistanceE (Character hero, int nchance, int npower, int ndur) 
    {
        duration=ndur;
        oduration=duration;
        hashcode=Card_HashCode.RandomCode();
    }
    public ResistanceE (Character hero, int nchance, int npower) 
    {
        chance=nchance;
        power=npower;
        hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character hero) 
    {
        hero.DR+=power;
    }
    @Override
    public void Nullified(Character target)
    {
        target.DR-=power;
    }
}
class ShatterE extends OtherEff implements Statlistener
{
    int duration;
    int oduration;
    int chance=616;
    String name;
    boolean stackable=true;
    int hashcode;
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
    @Override
    public String getimmunityname()
    {
        return "Shatter";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public void onTurnStart (Character hero)
    {
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onApply (Character target)
    {
        target.SHLD=0;
        for (StatEff eff: target.effects)
        {
            if (eff.getefftype().equalsIgnoreCase("Defence"))
            {
                target.remove(target, eff.getcode());
            }
        }
    }
    public ShatterE (int nchance, int dur)
    {
        duration=dur;
        oduration=duration;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override 
    public int getcode()
    {
        return hashcode;
    }
    @Override
    public String geteffname() 
    {
       if (duration<500)
        {
            name="Shatter Effect, "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Shatter Effect";
            return name;
        }
    }
    @Override
    public void Nullified(Character target)
    {
    }
}
class WMTarget extends OtherEff implements Statlistener
{
    String name= "Target Effect: 5";
    boolean stackable=true;
    int hashcode;
    int power=5;
    int oduration=616; 
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
        return 200;
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