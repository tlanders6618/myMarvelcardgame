package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: OtherEff
 * Purpose: To list all the Other status effects in one file.
 */
import java.util.ArrayList; 
public abstract class OtherEff extends StatEff 
{
    public OtherEff (int c, Character p)
    {
        this.chance=c; this.prog=p; this.id=CardCode.RandomCode();
    }
    @Override 
    public String getefftype()
    {
        return "Other"; 
    }
}
class BleedE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Bleed";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Bleed Effect: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Bleed Effect: "+this.power;
            return name;
        }
    }
    public BleedE (int c, int nstrength, int nduration, Character p)
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
class CounterE extends OtherEff 
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
        String name;
        if (duration<100)
        {
            name="Counter Effect: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Counter Effect: "+this.power;
            return name;
        }
    }
    public CounterE (int c, int nstrength, int nduration, Character p, String[] stat)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
        if (stat!=null)
        {
            statstrings.add(stat);
        }
    }
    @Override
    public void Attacked (Character hero, Character attacker, int ignore)
    {
        if (!(attacker.ignores.contains("Counter"))&&!(hero.binaries.contains("Stunned"))&&attacker.dead==false)
        {   
            int dmg=this.power; 
            Damage_Stuff.ElusiveDmg(hero, attacker, dmg, "counter");
            if (statstrings.size()>0)
            {
                for (String[] array: statstrings)
                {
                    String[][] toapply=StatFactory.MakeParam(array, null);
                    StatEff New=StatFactory.MakeStat(toapply, hero); 
                    int chance=New.chance;
                    if (CoinFlip.Flip(chance+hero.Cchance)==true)
                    StatEff.CheckApply(hero, attacker, New);
                    else
                    StatEff.applyfail(hero, New, "chance");
                }
            }
            hero.remove(this.id, "normal"); //counter is consumed after use
        }
    }
}
class DazeE extends OtherEff 
{
    boolean mimi;
    @Override
    public String getimmunityname()
    {
        return "Daze";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Daze Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Daze Effect";
        }
    }
    public DazeE (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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
class Empower extends OtherEff
{
    String name; //of hero who made the empowerment
    int index; //of hero who made the empowerment
    int uses; //most cases 1
    boolean used=false;
    @Override
    public String getimmunityname()
    {
        return "Empower";
    }
    @Override
    public String geteffname() //since every Empower has a unique effect, they don't use standard eff names
    {
        if (power>0)
        return "Empower "+power+": "+name+", "+uses+" use(s)";
        else
        return "Empower: "+name+", "+this.uses+" use(s)";
    }
    public Empower (int c, int power, int use, Character p)
    {
        super(c, p);    
        this.power=power;
        name=p.Cname;
        index=p.index;
        uses=use;
        this.stackable=true;
    }
    @Override
    public int UseEmpower(Character hero, Ability ab, boolean use) //use is true for applying effects and false when undoing an empowerment 
    {
        int value=0; 
        if (use==true&&this.uses>0) //try to activate empowerment; called at start of using ab
        {
            switch (index) //has to be this way since every empowerment is unique
            {
                case 4: case 39: case 77: //for beforeabs to boost the dmg of an aoe attack, or else the dmg boost only applies to the first enemy hit by the attack
                if (ab instanceof AttackAb) //damage boost only applies to abs that do dmg; also used for iron man's passive
                {
                    used=true; value=this.power; 
                }
                break;
                case 88:
                if (ab.attack==true)
                {
                    used=true; String[] poio={"Poison", "50", "10", "2", "false"}; String[][] dp=StatFactory.MakeParam(poio, null); ab.AddTempString(dp); 
                }
                break;
                case 95: 
                if (used==false) //or else aoe abs will activate it multiple times and leave the hero with permanent negative stat chance
                {
                    hero.Cchance-=100; used=true;
                }
                break;
                case 102: 
                if (ab instanceof AttackAb&& !(ab instanceof BasicAb)) 
                {
                    used=true; String[] poio={"Burn", "500", "10", "1", "false"}; String[][] dp=StatFactory.MakeParam(poio, null); ab.AddTempString(dp); 
                }
                break;
            }
        }
        else
        {
            if (used==true) //let empowerment know it's been used and undo its effects if necssary; called at end of using ab
            {
                used=false;
                --this.uses;
                switch (index) 
                {
                    case 95: hero.Cchance+=100; break;
                    case 102: 
                    if (ab.GetMultihit(false)>0) //to ensure each hit of a multihit attack gets to apply the burn; only count as a use once all hits have finished
                    this.uses++;
                    break;
                }
            }
        }
        return value;
    }
    @Override
    public void onTurnEnd (Character hero) //solely for removing empowerments
    {
        if (uses<=0)
        {
            if (index==39||index==77) //silent empower that lets dmg boosting beforeabs work properly with aoe
            hero.remove(this.id, "silent");
            else
            hero.remove(this.id, "normal");
        }
    }
}
class EvadeE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Evade";
    }
    @Override
    public String geteffname()
    {
        return "Evade Effect";
    }
    public EvadeE (int c, Character p) 
    {
        super(c, p);
        this.stackable=true;
    }
}
class FocusE extends OtherEff 
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
            return "Focus Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Focus Effect";
        }
    }
    public FocusE (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
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
class IntensifyE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Intensify";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration<100)
        {
            name="Intensify Effect: "+this.power+", "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Intensify Effect: "+this.power;
            return name;
        }
    }
    public IntensifyE (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
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
class Nauseated extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Nauseated";
    }
    @Override
    public String geteffname()
    {
        return "Nauseated Effect, "+this.duration+" turn(s)";
    }
    public Nauseated (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
    }
}
class Obsession extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Obsession";
    }
    @Override
    public String geteffname()
    {
        return "Obsession Effect";
    }
    public Obsession(Character p) 
    {
        super(500, p);
        this.id=CardCode.RandomCode();
        this.stackable=true; 
        this.prog=p;
    }
}
class PrecisionE extends OtherEff 
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
            return "Precision Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Precision Effect";
        }
    }
    public PrecisionE (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=true;
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
class ProtectE extends OtherEff
{
    Character protector;
    Character weakling;
    ProtectedE myfriend;
    boolean removed=false; //to prevent infinite loop of protect and protected trying to remove each other; this lets them know whether it's necessary or not
    @Override
    public void Extended (int dur, Character ignore)
    {
        this.duration+=dur;
        if (myfriend.duration<this.duration)
        myfriend.duration+=dur;
        if (this.duration<=0)
        {
            protector.remove(this.id, "normal");
        }
    }
    @Override
    public String getimmunityname()
    {
        return "Protect";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration>100)
        {
            name="(Effect) Protecting: "+weakling.Cname;
        }
        else
        {
            name="(Effect) Protecting: "+weakling.Cname+", "+this.duration+" turn(s)";
        }
        return name;
    }
    @Override
    public void onTurnEnd(Character hero) 
    {
        --this.duration; 
        if (this.duration<=0) 
        {
            removed=true;
            hero.remove(this.id, "normal");
        }
        else
        myfriend.lessprotected();
    }
    public ProtectE (int c, int ndur, Character p) 
    {
        super(c, p);
        this.duration=ndur;
    }
    @Override
    public void PrepareProtect (Character prot, Character weak)
    {
        protector=prot; weakling=weak;
    }
    @Override
    public void onApply (Character hero) 
    {
        boolean taunter=false, invis=false, dupe=false;        
        if (weakling.CheckFor("Taunt", false)==true||protector.CheckFor("Taunt", false)==true)
        taunter=true;  
        if (weakling.CheckFor("Invisible", false)==true||protector.CheckFor("Invisible", false)==true)
        invis=true;  
        for (StatEff e: protector.effects)
        {
            if (e.getimmunityname().equals("Protect")&&e.id!=this.id) //check if the protector has a protect other than this one; protect shouldn't stack with ProtectE
            {
                dupe=true; break;
            }
        }
        for (StatEff e: weakling.effects)
        {
            if (e.getimmunityname().equals("Protect")&&e.id!=this.id)
            {
                dupe=true; break;
            }
        } 
        if (taunter==true)
        {
            System.out.println ("Taunting characters cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.id, "silent");
        }
        else if (invis==true)
        {
            System.out.println ("Invisible characters cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.id, "silent");
        }
        else if (dupe==true)
        {
            System.out.println ("Characters who already have Protect cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.id, "silent");
        }
        else 
        {
            ProtectedE pr= new ProtectedE(this.chance, this.duration, this.prog);
            myfriend=pr;
            pr.myfriend=this;
            pr.PrepareProtect(protector, weakling);
            weakling.add(pr);
            String s;
            if (duration>500)
            {
                s="(Effect) Protecting: "+weakling.Cname;
            }
            else
            {
                s="(Effect) Protecting: "+weakling.Cname+", "+this.duration+" turn(s)"; //since the add method doesn't print anything for protect effects
            }
            System.out.println ("\n"+protector.Cname+" gained "+s);
            System.out.println ("\n"+weakling.Cname+" gained "+pr.geteffname());
        }
    }
    @Override
    public void Nullified(Character target)
    {
        removed=true;
        if (myfriend!=null&&myfriend.removed==false) 
        {
            weakling.remove(myfriend.id, "normal");
        }
    }
    @Override
    public Character getProtector ()
    {
        return protector;
    }
}
class ProtectedE extends OtherEff 
{
    Character protector;
    Character weakling;
    ProtectE myfriend;
    boolean removed=false;
    @Override
    public void Extended (int dur, Character ignore)
    {
        this.duration+=dur;
        for (StatEff f: protector.effects)
        {
            if (f.getimmunityname().equalsIgnoreCase("protect")&&f.duration<this.duration)
            {
                f.Extended(dur, null);
            }
        }
    }
    @Override
    public String getimmunityname()
    {
        return "Protect";
    }
    public ProtectedE (int c, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
    }
    @Override
    public Character getProtector ()
    {
        return protector;
    }
    @Override
    public void PrepareProtect (Character prot, Character weak)
    {
        protector=prot; weakling=weak;
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration>100)
        {
            name="(Effect) Protected by: "+protector.Cname;
        }
        else
        {
            name="(Effect) Protected by: "+protector.Cname+", "+this.duration+" turn(s)";
        }
        return name;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public void lessprotected()
    {
        --this.duration;
        if (this.duration<=0)
        {
            removed=true;
            weakling.remove(this.id, "normal");
        }
    }
    @Override
    public void Nullified(Character target)
    {
        removed=true;
        if (myfriend!=null&&myfriend.removed==false)
        {
            protector.remove(myfriend.id, "normal");
        }
    }
}
class Redwing extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Redwing";
    }
    @Override
    public String geteffname()
    {
        return "Redwing Effect";
    }
    public Redwing (Character p) 
    {
        super(500, p);
    }
    @Override
    public void onApply (Character hero) 
    {
        hero.helpers.add(new RedwingHelper());
    }
    @Override
    public void Nullified(Character target)
    {
        SpecialAbility concurrentmodificationexception=null;
        for (SpecialAbility f: target.helpers)
        {
            if (f instanceof RedwingHelper)
            {
                concurrentmodificationexception=f; break; 
            }
        }
        if (concurrentmodificationexception!=null)
        {
            target.helpers.remove(concurrentmodificationexception);
        }
    }
}
class ReflectE extends OtherEff
{
    boolean half;
    @Override
    public String getimmunityname()
    {
        return "Reflect";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (half==true)
        name="Reflect: Half Effect";
        else
        name="Reflect: Full Effect";
        if (this.duration<100)
        {
            return name+", "+this.duration+" turn(s)";
        }
        else
        {
            return name;
        }
    }
    public ReflectE (int c, boolean h, int d, Character p)
    {
        super(c, p);
        this.duration=d;
        this.oduration=d;
        if (h==false)
        this.half=false;
        else 
        this.half=true;
    }
    @Override
    public void Attacked(Character hero, Character attacker, int dmg)
    {
        if (!(attacker.ignores.contains("Reflect")))
        {
            if (half==true)
            {
                double ndmg=dmg*0.5;
                dmg=5*(int)(Math.floor(ndmg/5));
            }
            Damage_Stuff.ElusiveDmg(hero, attacker, dmg, "reflect");
        }
    }
}
class ResistanceE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Resistance";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration>500)
        {
            name="Resistance Effect: "+this.power;
        }
        else
        {
            name="Resistance Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        return name;
    }
    public ResistanceE (int c, int npower, int ndur, Character p) 
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
        this.stackable=true;
    }
    @Override
    public void onApply (Character hero) 
    {
        hero.PRDR+=power;
    }
    @Override
    public void Nullified(Character hero)
    {
        hero.PRDR-=power;   
    }
}
class ShatterE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Shatter";
    }
    @Override
    public String geteffname() 
    {
       String name;
       if (duration<100)
       {
            name="Shatter Effect, "+this.duration+" turn(s)";
            return name;
       }
       else
       {
            name="Shatter Effect";
            return name;
       }
    }
    public ShatterE (int c, int dur, Character p)
    {
        super(c, p);
        this.duration=dur;
        this.oduration=dur;
        this.stackable=true;
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
class SnareE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Snare";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration<100)
        {
            name="Snare Effect, "+this.duration+" turn(s)";
            return name;
        }
        else
        {
            name="Snare Effect";
            return name;
        }
    }
    public SnareE (int c, int nduration, Character p)
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
class Soaked extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Soaked";
    }
    public void onApply (Character target)
    {
        target.immunities.add("Evade"); target.immunities.add("Evasion");
        ArrayList<StatEff>modexception= new ArrayList<StatEff>();
        if (target.effects.size()>0)
        {
            modexception.addAll(target.effects);
        }
        for (StatEff eff: modexception)
        {
            if (eff.getimmunityname().equals("Evade")||eff.getimmunityname().equals("Evasion"))
            {
                target.remove(eff.id, "normal");
            }
        }
    }
    public Soaked (int c, int d, Character p)
    {
        super(c, p);
        this.duration=d; 
        this.oduration=d;
        this.stackable=true;
    }
    @Override
    public String geteffname() 
    {
       return "Soaked Effect, "+duration+ " turn(s)"; 
    }
    @Override
    public void Nullified (Character target)
    {
        target.immunities.remove("Evade"); target.immunities.remove("Evasion");
    }
}
class StunE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Stun";
    }
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability!=null&&target.activeability.channelled==true)
       {
           target.activeability.InterruptChannelled(target, target.activeability);
       }
    }
    public StunE (int c, int d, Character p)
    {
        super(c, p);
        this.duration=d; 
        this.oduration=d;
        this.stackable=true;
    }
    @Override
    public String geteffname() 
    {
       return "Stun Effect, "+duration+ " turn(s)"; 
    }
    @Override
    public void Nullified (Character target)
    {
        target.binaries.remove("Stunned");
    }
}
class TargetE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Target";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Target Effect: "+power+", "+duration+" turn(s)";
        }
        else
        {
            return "Target Effect: "+power;
        }
    }
    public TargetE (int c, int npow, int ndur, Character p)
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
        this.power=npow;
        this.stackable=true;
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
class Tracer extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Tracer";
    }
    @Override
    public String geteffname()
    {
        return "Tracer Effect, "+this.duration+" turn(s)";
    }
    public Tracer (int c, int d, Character p) 
    {
        super(c, p);
        this.duration=d;
        this.oduration=d;
        this.stackable=true;
    }
}