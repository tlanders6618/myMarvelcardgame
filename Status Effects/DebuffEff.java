package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: DebuffEff
 * Purpose: To list all the debuffs in one file.
 */
import java.util.ArrayList;
public abstract class DebuffEff extends StatEff 
{
    public DebuffEff (int c, Character p)
    {
        this.chance=c; this.prog=p; this.id=CardCode.RandomCode();
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
        if (this.duration<100)
        {
            return "Afflicted, "+this.duration+" turn(s)";
        }
        else
        {
            return "Afflicted";
        }
    }
    public Afflicted (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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
        if (duration<100)
        {
            return "Bleed: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Bleed: "+this.power;
        }
    }
    public Bleed (int c, int nstrength, int nduration, Character p)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "bleed");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.id, "normal");
            }
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
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
    public Blind (int c, int ndur, Character q)
    {
        super(c, q);
        this.duration=ndur;
        this.oduration=ndur;
        if (q.index==73)
        mimi=true;
        else
        mimi=false;
    }
    @Override
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
        if (duration<100)
        {
            return "Burn: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Burn: "+this.power;
        }
    }
    public Burn (int c, int nstrength, int nduration, Character p)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it would cause a null exception
        {
            hero.DOTdmg(this.power, "burn");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.id, "normal");
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
        return "Countdown: "+this.power+", "+this.duration+" turn(s)";
    }
    public Countdown (int c, int pow, int ndur, Character p, String[] neff)
    {
        super(c, p);
        this.duration=ndur;
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
        this.stackable=true;
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(this.id, "normal");
            int dmg=this.power; 
            Damage_Stuff.ElusiveDmg(null, hero, dmg, "countdown");
            dmg-=hero.ADR; //so ricochet is accurate
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
                    StatEff New=StatFactory.MakeStat(neweff, null); 
                    StatEff.CheckApply(null, hero, New);
                }
            }
        }
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
        if (this.duration<100)
        {
            return "Daze, "+this.duration+" turn(s)";
        }
        else
        {
            return "Daze";
        }
    }
    public Daze (int c, int ndur, Character q)
    {
        super(c, q);
        this.duration=ndur;
        this.oduration=ndur;
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
    public Debilitate (int c, int npow, int ndur, Character p)
    {
        super(c, p);
        this.power=npow;
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void Attacked (StatEff e)
    {
        if (e.getefftype().equals("Debuffs")&&e.id!=this.id)
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
    public Disarm (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
    }
}
class Disorient extends DebuffEff 
{
    boolean mimi;
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
    public Disorient (int c, int ndur, Character Q)
    {
        super(c, Q);
        this.duration=ndur;
        this.oduration=ndur;
        this.stackable=true;
        if (Q.index==73)
        mimi=true;
        else
        mimi=false;
    }
    @Override
    public void onApply (Character target)
    {
        if (mimi==false)
        target.nCC+=50;
        else
        target.nCC+=100;
    }
    @Override
    public void Nullified (Character target)
    {
        if (mimi==false)
        target.nCC-=50;
        else
        target.nCC-=100;
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
        if (duration<100)
        {
            return "Disrupt, "+this.duration+" turn(s)";
        }
        else
        {
            return "Disrupt";
        }
    }
    public Disrupt (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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
    public Neutralise (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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
        if (duration<100)
        {
            return "Placebo (Debuff), "+this.duration+" turn(s)";
        }
        else
        {
            return "Placebo (Debuff)";
        }
    }
    public PlaceboD (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
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
        if (duration<100)
        {
            return "Poison: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Poison: "+this.power;
        }
    }
    public Poison (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.id=CardCode.RandomCode();
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
                hero.remove(this.id, "normal");
            }
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
    }
}
class Provoke extends DebuffEff 
{
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
        if (duration<500)
        {
            return "Provoke: "+this.prog.Cname+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Provoke: "+this.prog.Cname;
        }
    }
    public Provoke (int c, int ndur, Character Q)
    {
        super(c, Q);
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void onApply (Character target)
    {
        ArrayList<StatEff> remove= new ArrayList<StatEff>();
        for (StatEff e: target.effects)
        {
            if (e.getimmunityname().equals("Terror")&&e.UseTerrorProvoke()==this.prog) //can't be forced to both attack and not attack the same person
            {
                remove.add(e);
            }
        }
        for (StatEff e: remove)
        {
            target.remove(e.id, "normal");
        }
    }
    @Override
    public Character UseTerrorProvoke ()
    {
        if (this.prog.dead==false&&!(this.prog.binaries.contains("Invisible"))&&this.prog.targetable==true&&!(this.prog.binaries.contains("Banished"))) 
        return this.prog; //if they're taunting, protecting, or protected, provoke still applies; they just need to be targetable 
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
       if (duration<100)
       {
            return "Shatter, "+this.duration+" turn(s)";
       }
       else
       {
            return "Shatter";
       }
    }
    public Shatter (int c, int dur, Character p)
    {
        super(c, p);
        this.duration=dur;
        this.oduration=dur;
    }
    @Override
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
                target.remove(eff.id, "normal");
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
        if (duration<100)
        {
            return "Shock: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Shock: "+this.power;
        }
    }
    public Shock (int c, int nstrength, int nduration, Character p)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "shock");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.id, "normal");
            }
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
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
        if (this.duration<100)
        {
            return "Snare, "+this.duration+" turn(s)";
        }
        else
        {
            return "Snare";
        }
    }
    public Snare (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
    }
    @Override
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
    @Override
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability!=null&&target.activeability.channelled==true)
       {
           target.activeability.InterruptChannelled(target, target.activeability);
       }
    }
    public Stun (int c, Character p)
    {
        super(c, p);
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
       if (duration<100)
       {
            return "Suppression, "+this.duration+" turn(s)";
       }
       else
       {
            return "Suppression";
       }
    }
    public Suppression (int c, int dur, Character p)
    {
        super(c, p);
        this.duration=dur;
        this.oduration=dur;
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
        if (duration<100)
        {
            return "Target: "+power+", "+duration+" turn(s)";
        }
        else
        {
            return "Target: "+power;
        }
    }
    public Target (int c, int npow, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
        this.power=npow;
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
        if (duration<500)
        {
            return "Terror: "+this.prog.Cname+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Terror: "+this.prog.Cname;
        }
    }
    public Terror (int c, int ndur, Character Q)
    {
        super(c, Q);
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void onApply (Character target)
    {
        ArrayList<StatEff> remove= new ArrayList<StatEff>();
        for (StatEff e: target.effects)
        {
            if (e.getimmunityname().equals("Provoke")&&e.UseTerrorProvoke()==this.prog) //can't be forced to both attack and not attack the same person
            {
                remove.add(e);
            }
        }
        for (StatEff e: remove)
        {
            target.remove(e.id, "normal");
        }
    }
    @Override
    public Character UseTerrorProvoke ()
    {
        if (this.prog.CheckFor("Taunt", false)==false&&this.prog.CheckFor("Protect", false)==false) //taunt and protect supersede terror
        return this.prog;
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
        if (this.duration<100)
        {
            return"Undermine, "+this.duration+" turn(s)";
        }
        else
        {
            return "Undermine";
        }
    }
    public Undermine (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
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
        if (duration<500)
        {
            return"Weakness: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return"Weakness: "+this.power;
        }
    }
    public Weakness (int c, int npow, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
        this.power=npow;
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
class Wither extends DebuffEff 
{
    @Override
    public String getimmunityname()
    {
        return "Wither";
    }
    @Override
    public String getalttype() 
    {
        return "damaging";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Wither: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Wither: "+this.power;
        }
    }
    public Wither (int c, int nstrength, int nduration, Character p)
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
        if (hero!=null) //after hero dies, their spot in the team array becomes null; if they die from one dot and another tries to tick down, it causes a null exception
        {
            hero.DOTdmg(this.power, "wither");
            --this.duration;
            if (this.duration<=0)
            {
                hero.remove(this.id, "normal");
            }
        }
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        //do nothing
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
        if (this.duration<100)
        {
            return "Wound, "+this.duration+" turn(s)";
        }
        else
        {
            return "Wound";
        }
    }
    public Wound (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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