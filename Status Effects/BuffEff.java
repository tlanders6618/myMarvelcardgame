package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: BuffEff
 * Purpose: To list all the buffs in one file.
 */
import java.util.ArrayList;
public abstract class BuffEff extends StatEff 
{
    public BuffEff ()
    {
    }
}
class ChanceUp extends BuffEff 
{
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int oduration; //original; for Copying
    @Override
    public int getduration (boolean copy)
    {
        if (copy==false)
        {
            return duration;
        }
        else
        {
            return oduration;
        }
    }
    @Override
    public void Extended (int dur)
    {
        duration+=dur;
    }
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return false;
    }
    @Override
    public String getimmunityname()
    {
        return "Chance Up";
    }
    @Override 
    public String getefftype()
    {
        return "Buffs";
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
            name="Chance Up, "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Chance Up";
            return name;
        }
    }
    public ChanceUp (int nchance)
    {
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public ChanceUp (int nchance, int nduration)
    {
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
    public void onApply (Character target)
    {
        target.Cchance+=50;      
    }
    @Override
    public void Nullified (Character target)
    {
        target.Cchance-=50;
    }
}
class DamageUp extends BuffEff 
{
    int strength=616;
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int oduration; //original; for Copying
    @Override
    public int getduration (boolean copy)
    {
        if (copy==false)
        {
            return duration;
        }
        else
        {
            return oduration;
        }
    }
    @Override
    public void Extended (int dur)
    {
        duration+=dur;
    }
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return false;
    }
    @Override
    public String getimmunityname()
    {
        return "Damage Up";
    }
    @Override 
    public String getefftype()
    {
        return "Buffs";
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
            name="Damage Up: "+strength+", "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Damage Up: "+strength;
            return name;
        }
    }
    public DamageUp (int nchance, int nstrength)
    {
        strength=nstrength;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public DamageUp (int nchance, int nstrength, int nduration)
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
    public void onApply (Character target)
    {
        target.BD+=strength;            
    }
    @Override
    public void Nullified (Character target)
    {
        target.BD-=strength;
    }
}
class Invisible extends BuffEff
{
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int oduration=616; //original; for Copying
    @Override
    public int getduration (boolean copy)
    {
        if (copy==false)
        {
            return duration;
        }
        else
        {
            return oduration;
        }
    }
    @Override
    public void Extended (int dur)
    {
        duration+=dur;
    }
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return false;
    }
    @Override
    public String getimmunityname()
    {
        return "Invisible";
    }
    @Override 
    public String getefftype()
    {
        return "Buffs";
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
            name="Invisible, "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Invisible";
            return name;
        }
    }
    public Invisible (int nchance)
    {
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Invisible (int nchance, int nduration)
    {
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
            hero.removeB(hero, "Invisible");
        }
    }
    @Override
    public void Nullified (Character target)
    {
        target.removeB(target, "Invisible");
    }
    public void onApply (Character target)
    {
        target.binaries.add("Invisible"); ArrayList<StatEff> r= new ArrayList<StatEff>();
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("taunt"))
            {
                r.add(eff);
            }
        }
        for (StatEff e: r)
        {
            target.remove(target, e.getcode());
        }
    }
}