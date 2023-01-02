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
    @Override
    public void Nullified (Character target)
    {
    }
}