package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: HealEff
 * Purpose: To list all the Heal effects in one file.
 */
public abstract class HealEff extends StatEff
{
    public HealEff ()
    {
    }
}
class Recovery extends HealEff 
{
    int strength=616;
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
        return "Recovery";
    }
    @Override 
    public String getefftype()
    {
        return "Heal";
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
            name="Recovery: "+strength+", "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Recovery: "+strength;
            return name;
        }
    }
    public Recovery (int nchance, int nstrength)
    {
        strength=nstrength;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Recovery (int nchance, int nstrength, int nduration)
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
    }
    @Override
    public void Nullified (Character target)
    {
    }
}
