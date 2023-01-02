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
class DamageUp extends BuffEff 
{
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
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Damage Up: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Damage Up: "+this.power;
            return name;
        }
    }
    public DamageUp (int nchance, int nstrength)
    {
        this.power=nstrength;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public DamageUp (int nchance, int nstrength, int nduration)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {
        target.BD+=this.power;            
    }
    @Override
    public void Nullified (Character target)
    {
        target.BD-=this.power;
    }
}
class Focus extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Focus";
    }
    @Override 
    public String getefftype() 
    {
        return "Buffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Focus, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Focus";
            return name;
        }
    }
    public Focus (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Focus (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
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
class Invisible extends BuffEff
{
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
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Invisible, "+this.duration+" turn(s)";
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
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Invisible (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Invisible");
    }
    public void onApply (Character target)
    {
        target.binaries.add("Invisible"); 
        ArrayList<StatEff> r= new ArrayList<StatEff>();
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("taunt")) //cannot taunt and be invisible
            {
                r.add(eff);
            }
        }
        for (StatEff e: r)
        {
            target.remove(target, e.hashcode, false);
        }
    }
}