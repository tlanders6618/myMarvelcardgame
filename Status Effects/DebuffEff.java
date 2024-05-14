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
    @Override 
    public String getefftype()
    {
        return "Debuffs";
    }
}
class Afflicted extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Afflicted";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Afflicted, "+this.duration+" turn(s)";
        }
        else
        {
            name="Afflicted";
        }
        return name;
    }
    public Afflicted (int nchance, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Afflicted (int nchance)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
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
    public String getalttype() 
    {
        return "damaging";
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "bleed");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.hashcode, "normal");
            }
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
class Blind extends DebuffEff
{
    boolean mimi;
    @Override
    public String getimmunityname()
    {
        return "Blind";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname() 
    {
       if (this.duration<100)
       {
           return "Blind, "+this.duration+" turn(s)";
       }
       else
       {
           return "Blind";
       }
    }
    public Blind (int nchance, int ndur, Character q)
    {
        this.chance=nchance;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
        if (q.index==73)
        mimi=true;
        else
        mimi=false;
    }
    public void onApply (Character target)
    {
        if (mimi==true)
        target.accuracy-=100;
        else
        target.accuracy-=50;
    }
    @Override
    public void Nullified (Character target)
    {
        if (mimi==true)
        target.accuracy+=100;
        else
        target.accuracy+=50;
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
    public String getalttype() 
    {
        return "damaging";
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it would cause a null exception
        {
            hero.DOTdmg(this.power, "burn");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.hashcode, "normal");
            }
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
class Countdown extends DebuffEff 
{
    ArrayList<String[]>statstrings= new ArrayList<String[]>();
    ArrayList<String[]> specials= new ArrayList<String[]>(); //ricochet, extend, etc
    @Override
    public String getimmunityname()
    {
        return "Countdown";
    }
    @Override
    public String getalttype() 
    {
        return "damaging";
    }
    @Override
    public String geteffname()
    {
        String name="Countdown: "+this.power+", "+this.duration+" turn(s)";
        return name;
    }
    public Countdown (int nchance, int pow, int ndur, String[] neff)
    {
        this.duration=ndur;
        this.chance=nchance;
        this.power=pow; 
        this.oduration=ndur;
        if (neff!=null)
        {
            if (neff[1]==null) //it's a one word array like ricochet, with the word in neff[0]
            {
                specials.add(neff);
            }
            else
            {
                statstrings.add(neff); 
            }
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
            hero.remove(this.hashcode, "normal");
            int dmg=this.power; 
            dmg=dmg-hero.ADR; //countdown dmg not affected by resistance
            if (dmg<0)
            dmg=0;
            System.out.println("\n"+hero.Cname+" took "+dmg+" damage from their Countdown");
            dmg=hero.TakeDamage(hero, dmg, true);       
            if (specials.size()>0) 
            {
                for (String[] eff: specials)
                {
                    if (eff[0].equalsIgnoreCase("Ricochet")) 
                    {
                        Ability.DoRicochetDmg (dmg, hero, hero, true, null);
                    }
                }
            }
            if (statstrings.size()>0&&hero.dead==false) //they survived the damage, so they can gain status effects from the countdown
            {
                for (String[] array: statstrings)
                {  
                    String[][] neweff=StatFactory.MakeParam(array, null);
                    StatEff New=StatFactory.MakeStat(neweff, hero); 
                    StatEff.CheckApply(null, hero, New);
                }
            }
        }
    }
    @Override
    public void onApply (Character target)
    {
    }
}
class Daze extends DebuffEff 
{
    boolean mimi;
    @Override
    public String getimmunityname()
    {
        return "Daze";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
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
    public Daze (int nchance, int ndur, Character q)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        if (q.index==73)
        mimi=true;
        else
        mimi=false;
    }
    @Override
    public void onApply (Character target)
    {
        if (mimi==true)
        target.Cchance-=100;
        else
        target.Cchance-=50;
    }
    @Override
    public void Nullified (Character target)
    {
        if (mimi==true)
        target.Cchance+=100;
        else
        target.Cchance+=50;
    }
}
class Debilitate extends DebuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Debilitate";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Debilitate: "+power+", "+duration+" turn(s)";
        }
        else
        {
            return "Debilitate: "+power;
        }
    }
    public Debilitate (int nchance, int npow, int ndur)
    {
        this.chance=nchance;
        this.power=npow;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
    }
    @Override
    public void Attacked (StatEff e)
    {
        if (e.getefftype().equals("Debuffs")&&e.hashcode!=this.hashcode)
        {
            e.power+=this.power;
        }
    }
}
class Disarm extends DebuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Disarm";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname() 
    {
       if (this.duration<100)
       {
           return "Disarm, "+this.duration+" turn(s)";
       }
       else
       {
           return "Disarm";
       }
    }
    public Disarm (int nchance, int ndur)
    {
        this.chance=nchance;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {
    }
}
class Disorient extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Disorient";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Disorient, "+this.duration+" turn(s)";
        }
        else
        {
            return "Disorient";
        }
    }
    public Disorient (int nchance, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character target)
    {
        target.CC-=50;
    }
    @Override
    public void Nullified (Character target)
    {
        target.CC+=50;
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
    public String getalttype() 
    {
        return "nondamaging";
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
class Neutralise extends DebuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Neutralise";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname() 
    {
       if (this.duration<100)
       {
           return "Neutralise, "+this.duration+" turn(s)";
       }
       else
       {
           return "Neutralise";
       }
    }
    public Neutralise (int nchance, int ndur)
    {
        this.chance=nchance;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {
    }
}
class PlaceboD extends DebuffEff
{
    @Override
    public String getimmunityname()
    {
        return "Placebo (Debuff)";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Placebo (Debuff), "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Placebo (Debuff)";
            return name;
        }
    }
    public PlaceboD (int nchance, int nduration)
    {
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
class Poison extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Poison";
    }
    @Override
    public String getalttype() 
    {
        return "damaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Poison: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Poison: "+this.power;
            return name;
        }
    }
    public Poison (int nchance, int nstrength, int nduration)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "poison");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.hashcode, "normal");
            }
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
class Provoke extends DebuffEff 
{
    Character progenitor;
    @Override
    public String getimmunityname()
    {
        return "Provoke";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<500)
        {
            name="Provoke: "+progenitor.Cname+", "+this.duration+" turn(s)";
        }
        else
        {
            name="Provoke: "+progenitor.Cname;
        }
        return name;
    }
    public Provoke (int nchance, int ndur, Character Q)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        progenitor=Q;
    }
    @Override
    public void onApply (Character target)
    {
        ArrayList<StatEff> remove= new ArrayList<StatEff>();
        for (StatEff e: target.effects)
        {
            if (e.getimmunityname().equals("Terror")&&e.UseTerrorProvoke()==progenitor) //can't be forced to both attack and not attack the same person
            {
                remove.add(e);
            }
        }
        for (StatEff e: remove)
        {
            target.remove( e.hashcode, "normal");
        }
    }
    @Override
    public Character UseTerrorProvoke ()
    {
        if (!(progenitor.binaries.contains("Invisible"))&&progenitor.targetable==true&&!(progenitor.binaries.contains("Banished"))) 
        return progenitor; //if they're taunting, protecting, or protected, provoke still applies; they just need to be targetable 
        else 
        return null;
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
    public String getalttype() 
    {
        return "nondamaging";
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
    public Shatter (int nchance, int dur)
    {
        this.duration=dur;
        this.oduration=dur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {
        target.binaries.add("Shattered");
        target.SHLD=0;
        ArrayList<StatEff>modexception= new ArrayList<StatEff>();
        if (target.effects.size()>0)
        {
            modexception.addAll(target.effects);
        }
        for (StatEff eff: modexception)
        {
            if (eff.getefftype().equalsIgnoreCase("Defence"))
            {
                target.remove(eff.hashcode, "normal");
            }
        }
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Shattered");
    }
}
class Shock extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Shock";
    }
    @Override
    public String getalttype() 
    {
        return "damaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Shock: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Shock: "+this.power;
            return name;
        }
    }
    public Shock (int nchance, int nstrength, int nduration)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "shock");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.hashcode, "normal");
            }
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
class Snare extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Snare";
    }
    @Override
    public String getalttype()
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Snare, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Snare";
            return name;
        }
    }
    public Snare (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)
    {
        Battle.Snared(target);
    }
    @Override
    public void Nullified (Character target)
    {
        Battle.Speeded(target);
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
    public String getalttype() 
    {
        return "nondamaging";
    }
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability!=null&&target.activeability.channelled==true)
       {
           target.activeability.InterruptChannelled(target, target.activeability);
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
class Suppression extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Suppression";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname() 
    {
       String name;
       if (duration<100)
       {
            name="Suppression, "+this.duration+" turn(s)";
            return name;
       }
       else
       {
            name="Suppression";
            return name;
       }
    }
    public Suppression (int nchance, int dur)
    {
        this.duration=dur;
        this.oduration=dur;
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
class Target extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Target";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
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
class Terror extends DebuffEff 
{
    Character progenitor;
    @Override
    public String getimmunityname()
    {
        return "Terror";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<500)
        {
            name="Terror: "+progenitor.Cname+", "+this.duration+" turn(s)";
        }
        else
        {
            name="Terror: "+progenitor.Cname;
        }
        return name;
    }
    public Terror (int nchance, int ndur, Character Q)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        progenitor=Q;
    }
    @Override
    public void onApply (Character target)
    {
        ArrayList<StatEff> remove= new ArrayList<StatEff>();
        for (StatEff e: target.effects)
        {
            if (e.getimmunityname().equals("Provoke")&&e.UseTerrorProvoke()==progenitor) //can't be forced to both attack and not attack the same person
            {
                remove.add(e);
            }
        }
        for (StatEff e: remove)
        {
            target.remove(e.hashcode, "normal");
        }
    }
    @Override
    public Character UseTerrorProvoke ()
    {
        if (progenitor.CheckFor("Taunt", false)==false&&progenitor.CheckFor("Protect", false)==false) //taunt and protect supersede terror
        return progenitor;
        else 
        return null;
    }
}
class Undermine extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Undermine";
    }
    @Override
    public String getalttype()
    {
        return "nondamaging";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Undermine, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Undermine";
            return name;
        }
    }
    public Undermine (int nchance, int nduration)
    {
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
class Weakness extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Weakness";
    }
    @Override
    public String getalttype() 
    {
        return "nondamaging";
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
    public String getalttype() 
    {
        return "nondamaging";
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