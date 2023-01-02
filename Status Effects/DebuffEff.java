package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: DebuffEff
 * Purpose: To list all the debuffs in one file because there's a lot of them.
 */
import java.util.ArrayList;
public abstract class DebuffEff extends StatEff 
{
    public DebuffEff ()
    {
    }
}
class Bleed extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Bleed";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Bleed: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Bleed: "+this.power;
            return name;
        }
    }
    public Bleed (int nchance, int nstrength)
    {
        this.power=nstrength;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    public Bleed (int nchance, int nstrength, int nduration)
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
        hero.DOTdmg(hero, this.power, "bleed");
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(hero, this.hashcode, false);
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
    @Override
    public void Nullified (Character target)
    {
    }
}
class Burn extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Burn";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Burn: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Burn: "+this.power;
            return name;
        }
    }
    public Burn (int nchance, int nstrength)
    {
        this.power=nstrength;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    public Burn (int nchance, int nstrength, int nduration)
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
        hero.DOTdmg(hero, this.power, "burn");
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(hero, this.hashcode, false);
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
        target.DV+=5;
    }
    @Override
    public void Nullified (Character target)
    {
        target.DV-=5;
    }
}
class Daze extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Daze";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Daze, "+this.duration+" turn(s)";
        }
        else
        {
            name="Daze";
        }
        return name;
    }
    public Daze (int nchance, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Daze (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.Cchance-=50;
    }
    @Override
    public void Nullified (Character target)
    {
        target.Cchance+=50;
    }
}
class Countdown extends DebuffEff 
{
    ArrayList<String[]>statstrings= new ArrayList<String[]>();
    ArrayList<String> specials= new ArrayList<String>(); //ricochet, extend, etc
    @Override
    public String getimmunityname()
    {
        return "Countdown";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name="Countdown: "+this.power+", "+this.duration+" turn(s)";
        return name;
    }
    public Countdown (int nchance, int pow, int ndur, String sp)
    {
        this.duration=ndur;
        this.chance=nchance;
        this.power=pow;
        this.oduration=ndur;
        if (sp!=null)
        {
            specials.add(sp);
        }
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(hero, this.hashcode, false);
            int dmg=this.power; dmg=Damage_Stuff.DamageDecrease(null, false, hero, dmg);
            System.out.println("\n"+hero.Cname+" took "+dmg+" damage from their Countdown");
            dmg=hero.TakeDamage(hero, dmg, true);            
            if (statstrings.size()>0&&hero.dead==false) //they survived the damage, so they can gain status effects
            {
                for (String[] array: statstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                    hero.add(hero, New);
                }
            }
            if (specials.size()>0) 
            {
                for (String eff: specials)
                {
                    if (eff.equalsIgnoreCase("Ricochet")) //the only special thing countdowns can do as of 3.1 is Ricochet, so an if statement is fine for now
                    {
                        Ability.DoRicochetDmg (dmg, hero, true);
                    }
                }
            }
        }
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
class Disrupt extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Disrupt";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Disrupt, "+this.duration+" turn(s)";
        }
        else
        {
            name="Disrupt";
        }
        return name;
    }
    public Disrupt (int nchance, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Disrupt (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.immunities.add("Buffs");
    }
    @Override
    public void Nullified (Character target)
    {
        target.immunities.remove("Buffs");
    }
}
class Shatter extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Shatter";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    public void onApply (Character target)
    {
        target.binaries.add("Shattered");
        target.SHLD=0;
        ArrayList<StatEff>modexception= new ArrayList<StatEff>();
        if (target.effects.size()!=0)
        {
            modexception.addAll(target.effects);
        }
        for (StatEff eff: modexception)
        {
            if (eff.getefftype().equalsIgnoreCase("Defence"))
            {
                target.remove(target, eff.hashcode, false);
            }
        }
    }
    public Shatter (int nchance, int dur)
    {
        this.duration=dur;
        this.oduration=dur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public String geteffname() 
    {
       String name;
       if (duration<100)
       {
            name="Shatter, "+this.duration+" turn(s)";
            return name;
       }
       else
       {
            name="Shatter";
            return name;
       }
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Shattered");
    }
}
class Stun extends DebuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Stun";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability[0]!=null&&target.activeability[0].channelled==true)
       {
           target.activeability[0].InterruptChannelled();
       }
    }
    public Stun (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.duration=1; 
        this.oduration=1;
    }
    @Override
    public String geteffname() 
    {
       return "Stun, "+duration+ " turn(s)"; 
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Stunned");
    }
}
class Target extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Target";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Target: "+power+", "+duration+" turn(s)";
        }
        else
        {
            name="Target: "+power;
        }
        return name;
    }
    public Target (int nchance, int npow, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.power=npow;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Target (int nchance, int npow)
    {
        this.power=npow;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.DV+=power;
    }
    @Override
    public void Nullified (Character target)
    {
        target.DV-=power;
    }
}
class Weakness extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Weakness";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<500)
        {
            name="Weakness: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            name="Weakness: "+this.power;
        }
        return name;
    }
    public Weakness (int nchance, int npow, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.power=npow;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Weakness (int nchance, int npow)
    {
        this.power=npow;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.BD-=power;
    }
    @Override
    public void Nullified (Character target)
    {
        target.BD+=power;
    }
}
class Wound extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Wound";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Wound, "+this.duration+" turn(s)";
        }
        else
        {
            name="Wound";
        }
        return name;
    }
    public Wound (int nchance, int ndur)
    {
        this.duration=ndur;
        this.chance=nchance;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Wound (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.binaries.add("Wounded");
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Wounded");
    }
}