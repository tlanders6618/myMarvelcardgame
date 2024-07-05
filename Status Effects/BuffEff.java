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
    boolean myriad=true;
    public BuffEff ()
    {
    }
    @Override 
    public String getefftype() 
    {
        return "Buffs";
    }
}
class Bulwark extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Bulwark";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Bulwark, "+this.duration+" turn(s)";
        }
        else
        {
            return "Bulwark";
        }
    }
    public Bulwark (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override 
    public void onApply (Character target)
    {
        target.CritDR+=50;      
    }
    @Override
    public void Nullified (Character target)
    {
        target.CritDR-=50;
    }
}
class Counter extends BuffEff 
{
    ArrayList<String[]>statstrings= new ArrayList<String[]>();
    @Override
    public String getimmunityname()
    {
        return "Counter";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return"Counter: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Counter: "+this.power;
        }
    }
    public Counter (int nchance, int nstrength, int nduration, Character p, String[] stat)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
        this.myriad=false;
        this.prog=p;
        if (stat!=null)
        {
            statstrings.add(stat);
        }
    }
    @Override
    public void Attacked (Character hero, Character attacker, int ignore) //dmg is elusive
    {
        if (!(attacker.ignores.contains("Counter"))&&!(hero.binaries.contains("Stunned"))&&attacker.dead==false)
        {   
            int dmg=this.power; 
            dmg-=attacker.ADR;
            System.out.println (hero.Cname+" counterattacks for "+dmg+" damage!");
            attacker.TakeDamage(dmg, false);  
            if (statstrings.size()>0)
            {
                for (String[] array: statstrings)
                {
                    String[][] toapply=StatFactory.MakeParam(array, null);
                    StatEff New=StatFactory.MakeStat(toapply, hero); 
                    StatEff.CheckApply(hero, attacker, New);
                }
            }
            hero.remove(this.hashcode, "normal"); //counter is consumed after use
        }
    }
}
class Evasion extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Evasion";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Evasion, "+this.duration+" turn(s)";
        }
        else
        {
            return "Evasion";
        }
    }
    public Evasion (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
}
class Ferocity extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Ferocity";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Ferocity, "+this.duration+" turn(s)";
        }
        else
        {
            return "Ferocity";
        }
    }
    public Ferocity (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override 
    public void onApply (Character target)
    {
        target.critdmg+=0.5;
    }
    @Override
    public void Nullified (Character target)
    {
        target.critdmg-=0.5;
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
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Focus, "+this.duration+" turn(s)";
        }
        else
        {
            return "Focus";
        }
    }
    public Focus (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override 
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
class Intensify extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Intensify";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Intensify: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Intensify: "+this.power;
        }
    }
    public Intensify (int nchance, int nstrength, int nduration, Character p)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.myriad=false;
        this.prog=p;
    }
    @Override
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
class Invisible extends BuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Invisible";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Invisible, "+this.duration+" turn(s)";
        }
        else
        {
            return "Invisible";
        }
    }
    public Invisible (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override 
    public void onApply (Character target)
    {
        target.binaries.add("Invisible"); 
        ArrayList<StatEff> r= new ArrayList<StatEff>();
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equals("Taunt")||eff.getimmunityname().equals("Protect")) //cannot taunt and be invisible, so taunt is removed
            {
                r.add(eff);
            }
        }
        for (StatEff e: r)
        {
            target.remove(e.hashcode, "normal");
        }
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Invisible");
    }
}
class MightyBlows extends BuffEff 
{
    ApplyShatter n= new ApplyShatter(50, 0, false, false, true);
    @Override
    public String getimmunityname()
    {
        return "Mighty Blows";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Mighty Blows, "+this.duration+" turn(s)";
        }
        else
        {
            return "Mighty Blows";
        }
    }
    public MightyBlows (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override
    public void onApply (Character target)
    {   
        for (Ability a: target.abilities)
        {
            if (a!=null&&a.attack==true)
            {
                a.special.add(n);
            }
        }
        for (Ability[] arr: target.transabs) //to avoid bugs if a character gains the eff and then transforms
        {
            for (Ability a: arr)
            {
                if (a!=null&&a.attack==true)
                {
                    a.special.add(n);
                }
            }
        }
    }
    @Override
    public void Nullified (Character target)
    {
        for (Ability a: target.abilities)
        {
            if (a!=null&&a.attack==true)
            {
                a.special.remove(n);
            }
        }
        for (Ability[] arr: target.transabs)
        {
            for (Ability a: arr)
            {
                if (a!=null&&a.attack==true)
                {
                    a.special.remove(n);
                }
            }
        }
    }
}
class PlaceboB extends BuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Placebo (Buff)";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Placebo (Buff), "+this.duration+" turn(s)";
        }
        else
        {
            return "Placebo (Buff)";
        }
    }
    public PlaceboB (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
}
class Precision extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Precision";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Precision, "+this.duration+" turn(s)";
        }
        else
        {
            return "Precision";
        }
    }
    public Precision (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
        this.prog=p;
    }
    @Override
    public void onApply (Character target)
    {
        target.CC+=50;      
    }
    @Override
    public void Nullified (Character target)
    {
        target.CC-=50;
    }
}
class Safeguard extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Safeguard";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Safeguard, "+this.duration+" turn(s)";
        }
        else
        {
            return "Safeguard";
        }
    }
    public Safeguard (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
}
class Speed extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Speed";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Speed, "+this.duration+" turn(s)";
        }
        else
        {
            return "Speed";
        }
    }
    public Speed (int nchance, int nduration, Character p)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.prog=p;
    }
    @Override
    public void onApply (Character target)
    {
        Battle.Speeded(target);
    }
    @Override
    public void Nullified (Character target)
    {
        Battle.Snared(target);
    }
}