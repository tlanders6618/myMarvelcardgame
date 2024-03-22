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
class Drain extends HealEff
{
    @Override
    public String getimmunityname()
    {
        return "Drain";
    }
    @Override 
    public String getefftype()
    {
        return "Heal";
    }
    @Override
    public String geteffname()
    {
        String name="Error fetching Drain name"; //just in case something is wrong with power
        if (this.power==50)
        {
            name="Drain: Half, "+this.duration+" turn(s)";
        }
        else if (this.power==100)
        {
            name="Drain: Full, "+this.duration+" turn(s)";
        }
        return name;
    }
    public Drain (int nchance, int pow, int ndur) //50 for drain half and 100 for drain full
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.power=pow;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.lifesteal+=this.power;
    }
    @Override
    public void Nullified (Character target)
    {
        target.lifesteal-=this.power;
    }
}
class Recovery extends HealEff 
{
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
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Recovery: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Recovery: "+this.power;
            return name;
        }
    }
    public Recovery (int nchance, int nstrength)
    {
        this.power=nstrength;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Recovery (int nchance, int nstrength, int nduration)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
    }
}
class Regen extends HealEff 
{
    @Override
    public String getimmunityname()
    {
        return "Regen";
    }
    @Override 
    public String getefftype()
    {
        return "Heal";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Regen: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Regen: "+this.power;
            return name;
        }
    }
    public Regen (int nchance, int nstrength, int nduration)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    @Override
    public void onTurnStart (Character hero)
    {
        Character.Healed(hero, this.power, false);
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(this.hashcode, "normal");
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
    }
    @Override
    public void onApply (Character target)
    {
    }
}