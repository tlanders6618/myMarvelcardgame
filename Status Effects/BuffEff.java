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
            name="Counter: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Counter: "+this.power;
            return name;
        }
    }
    public Counter (int nchance, int nstrength, int nduration, String[] stat)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
        this.myriad=false;
        if (stat!=null)
        {
            statstrings.add(stat);
        }
    }
    @Override
    public void onApply (Character target)
    {          
    }
    @Override
    public void Attacked (Character hero, Character attacker, int ignore) //dmg based on status effects like damage up weakness and resistance
    {
        if (hero.binaries.contains("Missed"))
        hero.binaries.remove("Missed");
        int dmg=this.power; 
        if (!(attacker.ignores.contains("Counter"))&&!(hero.binaries.contains("Stunned")))
        {
            Damage_Stuff.CheckBlind(hero);
            if (!(hero.binaries.contains("Missed")))
            Damage_Stuff.CheckEvade(hero, attacker);
        }
        if (!(attacker.ignores.contains("Counter"))&&!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Missed")))
        {   
            dmg=Damage_Stuff.DamageFormula(hero, attacker, dmg);
            dmg=Damage_Stuff.CheckGuard(hero, attacker, dmg);
            System.out.println (hero.Cname+" counterattacks!");
            attacker.TakeDamage(attacker, hero, dmg, false); 
            if (statstrings.size()>0)
            {
                for (String[] array: statstrings)
                {
                    String[][] toapply=StatFactory.MakeParam(array, null);
                    StatEff New=StatFactory.MakeStat(toapply, hero); 
                    StatEff.CheckApply(hero, attacker, New);
                }
            }
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
            name="Evasion, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Evasion";
            return name;
        }
    }
    public Evasion (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Evasion (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {     
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
class Intensify extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Intensify";
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
            name="Intensify: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Intensify: "+this.power;
            return name;
        }
    }
    public Intensify (int nchance, int nstrength)
    {
        this.power=nstrength;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.myriad=false;
    }
    public Intensify (int nchance, int nstrength, int nduration)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.myriad=false;
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
            if (eff.getimmunityname().equals("Taunt")||eff.getimmunityname().equals("Protect")) //cannot taunt and be invisible, so taunt is removed
            {
                r.add(eff);
            }
        }
        for (StatEff e: r)
        {
            target.remove(target, e.hashcode, "normal");
        }
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
            name="Precision, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Precision";
            return name;
        }
    }
    public Precision (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    public Precision (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
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
class Speed extends BuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Speed";
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
            name="Speed, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Speed";
            return name;
        }
    }
    public Speed (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Speed (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
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