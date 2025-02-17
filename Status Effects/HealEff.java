package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * Date of creation: 10/8/22
 * Purpose: To list and implement all the game's Heal effects in one file.
 */
public abstract class HealEff extends StatEff
{
    /**
    * Constructor for all Heal effects. Initialises the effect's status chance, progenitor, and id.
    * <p> Individual Heal effects override StatEff methods as needed for their implementation, and have their own constructors that also call this one.
    * @param c The effect's status chance.
    * @param p The effect's progenitor.
    * @see StatEff
    */
    public HealEff (int c, Character p)
    {
        this.chance=c; this.prog=p; this.id=CardCode.RandomCode();
    }
    /** 
     * The same for all Heal effects. 
     * @return "Heal" 
     */
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
        if (this.duration<100)
        {
            return "Recovery: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Recovery: "+this.power;
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
        if (this.duration<100)
        {
            return "Regen: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Regen: "+this.power;
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