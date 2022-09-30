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
    int strength=616;
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=true;
    int hashcode;
    int oduration=616; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
            name="Bleed: "+strength+", "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Bleed: "+strength;
            return name;
        }
    }
    public Bleed (int nchance, int nstrength)
    {
        strength=nstrength;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Bleed (int nchance, int nstrength, int nduration)
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
        hero.DOTdmg(hero, strength, "bleed");
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
    }
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
    int strength=616;
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=true;
    int hashcode;
    int oduration=616; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
            name="Burn: "+strength+", "+duration+" turn(s)";
            return name;
        }
        else
        {
            name="Burn: "+strength;
            return name;
        }
    }
    public Burn (int nchance, int nstrength)
    {
        strength=nstrength;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Burn (int nchance, int nstrength, int nduration)
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
        hero.DOTdmg(hero, strength, "burn");
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
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
class ChanceDown extends DebuffEff 
{
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=false;
    int hashcode;
    int oduration=616; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
    @Override
    public String getimmunityname()
    {
        return "Chance Down";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
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
            name="Chance Down, "+duration+" turn(s)";
        }
        else
        {
            name="Chance Down";
        }
        return name;
    }
    public ChanceDown (int nchance, int ndur)
    {
        duration=ndur;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public ChanceDown (int nchance)
    {
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
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int power=616;
    int oduration=616; //original; for Copying
    ArrayList<StatEff>toapply= new ArrayList<StatEff>();
    ArrayList<String> specials= new ArrayList<String>(); //ricochet, extend, etc
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return true;
    }
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
    public int getpower()
    {
        return power;
    }
    @Override 
    public int getcode()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        name="Countdown, "+duration+" turn(s)";
        return name;
    }
    public Countdown (int nchance, int ndur, int pow)
    {
        duration=ndur;
        chance=nchance;
        power=pow;
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
            int dmg=power; dmg=Damage_Stuff.DamageDecrease(false, hero, dmg);
            dmg=hero.TakeDamage(hero, dmg, false);
            if (toapply.size()>0&&hero.dead==false) //they survived the damage, so they can gain status effects
            {
                for (StatEff eff: toapply)
                {
                    eff.CheckApply(null, hero, eff);
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
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int oduration=616; //original; for Copying
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
        return "Disrupt";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
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
            name="Disrupt, "+duration+" turn(s)";
        }
        else
        {
            name="Disrupt";
        }
        return name;
    }
    public Disrupt (int nchance, int ndur)
    {
        duration=ndur;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Disrupt (int nchance)
    {
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
    int duration=616;
    int oduration=616;
    int chance=616;
    String name;
    boolean stackable=false;
    int hashcode;
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
                target.remove(target, eff.getcode());
            }
        }
    }
    public Shatter (int nchance, int dur)
    {
        duration=dur;
        oduration=duration;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
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
            name="Shatter, "+duration+" turn(s)";
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
        target.removeB(target, "Shattered");
    }
}
class Stun extends DebuffEff
{
    int duration=1;
    int oduration=1;
    int chance=616;
    String name="Stun, "+duration+ " turn(s)";
    boolean stackable=false;
    int hashcode;
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
       target.binaries.add("Stunned");
       if (target.activeability[0]!=null&&target.activeability[0].GetChannelled()==true)
       {
           target.activeability[0].InterruptChannelled();
       }
    }
    public Stun (int nchance)
    {
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
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
       return name; 
    }
    @Override
    public void Nullified (Character target)
    {
        target.removeB(target, "Stunned");
    }
}
class Target extends DebuffEff 
{
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=false;
    int hashcode;
    int power=616;
    int oduration=616; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
    public int getpower()
    {
        return power;
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
        duration=ndur;
        oduration=duration;
        chance=nchance;
        power=npow;
        hashcode=Card_HashCode.RandomCode();
    }
    public Target (int nchance, int npow)
    {
        power=npow;
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
    int duration=616;
    int chance=616;
    String name;
    boolean stackable=false;
    int hashcode;
    int power=616;
    int oduration=616; //original; for Copying
    @Override
    public int getchance()
    {
        return chance;
    }
    @Override
    public boolean getstackable()
    {
        return stackable;
    }
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
    public int getpower()
    {
        return power;
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
            name="Weakness: "+power+", "+duration+" turn(s)";
        }
        else
        {
            name="Weakness: "+power;
        }
        return name;
    }
    public Weakness (int nchance, int npow, int ndur)
    {
        duration=ndur;
        oduration=duration;
        chance=nchance;
        power=npow;
        hashcode=Card_HashCode.RandomCode();
    }
    public Weakness (int nchance, int npow)
    {
        power=npow;
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
    int duration=616;
    int chance=616;
    String name;
    int hashcode;
    int oduration=616; //original; for Copying
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
        return "Wound";
    }
    @Override 
    public String getefftype()
    {
        return "Debuffs";
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
            name="Wound, "+duration+" turn(s)";
        }
        else
        {
            name="Wound";
        }
        return name;
    }
    public Wound (int nchance, int ndur)
    {
        duration=ndur;
        chance=nchance;
        hashcode=Card_HashCode.RandomCode();
    }
    public Wound (int nchance)
    {
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
    @Override
    public void onApply (Character target)
    {
    }
    @Override
    public void Nullified (Character target)
    {
    }
}
