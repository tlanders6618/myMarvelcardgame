package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: HealEff
 * Purpose: To list all the Heal effects in one file.
 */
public abstract class HealEff extends StatEff
{
    public HealEff (int c, Character p)
    {
        this.chance=c; this.prog=p; this.id=CardCode.RandomCode();
    }
    @Override 
    public String getefftype()
    {
        return "Heal";
    }
}
class Drain extends HealEff
{
    boolean half;
    @Override
    public String getimmunityname()
    {
        return "Drain";
    }
    @Override
    public String geteffname()
    {
        if (this.half==true)
        {
            return "Drain: Half, "+this.duration+" turn(s)";
        }
        else 
        {
            return "Drain: Full, "+this.duration+" turn(s)";
        }
    }
    public Drain (int c, boolean half, int ndur, Character p) 
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
        if (half==false)
        this.half=false;
        else 
        this.half=true;
    }
    @Override
    public void onApply (Character target)
    {
        if (this.half==true)
        target.lifesteal+=50;
        else
        target.lifesteal+=100;
    }
    @Override
    public void Nullified (Character target)
    {
        if (this.half==true)
        target.lifesteal-=50;
        else
        target.lifesteal-=100;
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
    public Recovery (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
    public Regen (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
    }
    @Override
    public void onTurnStart (Character hero)
    {
        hero.Healed(this.power, false, true);
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(this.id, "normal");
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
    }
}