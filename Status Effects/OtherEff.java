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
        this.stackable=true; //by default, all other effs are stackable
    }
    @Override 
    public String getefftype()
    {
        return "Other"; 
    }
}
class Aura extends OtherEff
{
    String[][] statstring;
    @Override
    public String getimmunityname()
    {
        return "Aura";
    }
    @Override
    public String geteffname()
    {
        String ename=statstring[0][0]+": "+statstring[0][2];
        if (duration<100)
        {
            return "Aura: "+ename+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Aura: "+ename;
        }
    }
    public Aura (int c, int nduration, Character p, String[] stat)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
        statstring=StatFactory.MakeParam(stat, null);
    }
    @Override
    public void Attacked (Character hero, Character attacker, int ignore) //called by hero beforeattack and attacked; aoe check done there
    {
        if (attacker==hero) //hero is attacking; add tempstring
        {   
            hero.activeability.AddTempString(statstring);
        }
        else //hero was attacked; debuff the attacker; ignores stun
        {
            StatEff eff=StatFactory.MakeStat(statstring, hero); 
            if (CoinFlip.Flip(50+hero.Cchance)==true)
            StatEff.CheckApply(hero, attacker, eff);
            else
            StatEff.applyfail(attacker, eff, "chance");
        }
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
    public String getalttype() 
    {
        return "damaging";
    }
    @Override
    public String geteffname()
    {
        if (duration<100)
        {
            return "Bleed Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Bleed Effect: "+this.power;
        }
    }
    public BleedE (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
class BurnE extends OtherEff 
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
            return "Burn Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Burn Effect: "+this.power;
        }
    }
    public BurnE (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
class Chlorine extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Chlorine";
    }
    @Override
    public String geteffname()
    {
        return "Chlorine Effect, "+duration+" turn(s)";
    }
    public Chlorine (int ch, int ndur, Character p)
    {
        super(ch, p);
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void Attacked (Character hero, StatEff e)
    {
        if (e.getimmunityname().equals("Burn")&&e.getefftype().equals("Debuffs"))
        {
            int inc=30;
            System.out.println("\n"+hero+"'s Chlorine increased the strength of their "+inc+" by 30!");
            e.power+=inc;
            hero.remove(this.id, "normal");
        }
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
        if (duration<100)
        {
            return "Counter Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Counter Effect: "+this.power;
        }
    }
    public CounterE (int c, int nstrength, int nduration, Character p, String[] stat)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
class DisorientE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Disorient";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Disorient Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Disorient Effect";
        }
    }
    public DisorientE (int c, int ndur, Character Q)
    {
        super(c, Q);
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void onApply (Character target)
    {
        target.nCC+=50;
    }
    @Override
    public void Nullified (Character target)
    {
        target.nCC-=50;
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
        if (power!=616)
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
    }
    @Override
    public int UseEmpower(Character hero, Ability ab, boolean use) //use is true for applying effects and false when undoing an empowerment 
    {
        int value=0; 
        if (use==true&&this.uses>0) //try to activate empowerment; called at start of using ab
        {
            switch (index) //has to be this way since every empowerment is unique
            {
                case 4: case 39: case 68: case 77: 
                //for beforeabs to boost the dmg of an aoe attack, or else the dmg boost only applies to the first enemy hit by the attack; also used for iron man and vector
                if (ab instanceof AttackAb) //damage boost only applies to abs that do dmg
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
    }
}
class Fear extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Fear";
    }
    @Override
    public String getalttype()
    {
        return "Terror";
    }
    @Override
    public String geteffname()
    {
        return "Fear Effect, "+this.duration+" turn(s)";
    }
    public Fear (int c, int d, Character p) 
    {
        super(c, p);
        this.duration=d;
        this.oduration=d;
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
class GuardE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Guard";
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            return "Guard Effect: "+this.power;
        }
        else
        {
            return "Guard Effect: "+this.power+", "+this.duration+" attack(s)";
        }
    }
    public GuardE (int c, int npower, int ndur, Character p) 
    //does nothing on its own; all handled by checkguard, called by character.attack as part of dmg calc, before the takedamage stuff
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
        //this.stackable=false;
    }
    @Override
    public void onTurnEnd (Character hero) //overriden to avoid decreasing duration on turn
    {
    }
    @Override
    public int UseGuard (Character dealer, Character targ, int dmg) 
    {
        int odmg=dmg;
        dmg-=this.power;
        this.duration--;
        if (this.power>=0)
        System.out.println ("\n"+targ+"'s Guard reduced " +dealer+"'s attack damage by "+Math.abs(odmg-dmg));
        else //for quake
        System.out.println ("\n"+targ+"'s Guard increased " +dealer+"'s attack damage by "+Math.abs(this.power));
        if (this.duration<=0)
        targ.remove(this.id, "normal");
        if (dmg<0)
        return 0;
        else
        return dmg;
    }
    @Override
    public void Extended (int d, Character hero)
    {
        //cannot be extended
    }
}
class Hydrogen extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Hydrogen";
    }
    @Override
    public String geteffname()
    {
        return "Hydrogen Effect, "+duration+" turn(s)";
    }
    public Hydrogen (int ch, int ndur, Character p)
    {
        super(ch, p);
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void Attacked (Character hero, StatEff e)
    {
        if (e.getimmunityname().equals("Burn")&&e.getefftype().equals("Debuffs"))
        {
            Damage_Stuff.ElusiveDmg(null, hero, 60, "default");
            hero.remove(this.id, "normal");
        }
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
        if (duration<100)
        {
            return "Intensify Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Intensify Effect: "+this.power;
        }
    }
    public IntensifyE (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
        if (duration>100)
        {
            return "(Effect) Protecting: "+weakling.Cname;
        }
        else
        {
            return "(Effect) Protecting: "+weakling.Cname+", "+this.duration+" turn(s)";
        }
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
            weakling.add(pr, false);
            String s;
            if (duration>500)
            {
                s="(Effect) Protecting: "+weakling.Cname;
            }
            else
            {
                s="(Effect) Protecting: "+weakling.Cname+", "+this.duration+" turn(s)"; //since the add method doesn't print anything for protect effects
            }
            System.out.println ("\n"+protector.Cname+" gained a(n) "+s);
            System.out.println ("\n"+weakling.Cname+" gained a(n) "+pr.geteffname());
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
class RegenE extends OtherEff 
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
            return "Regen Effect: "+this.power+", "+this.duration+" turn(s)";
        }
        else
        {
            return "Regen Effect: "+this.power;
        }
    }
    public RegenE (int c, int nstrength, int nduration, Character p)
    {
        super(c, p);
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
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
        if (duration>100)
        {
            return "Resistance Effect: "+this.power;
        }
        else
        {
            return "Resistance Effect: "+this.power+", "+this.duration+" turn(s)";
        }
    }
    public ResistanceE (int c, int npower, int ndur, Character p) 
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
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
       if (duration<100)
       {
            return "Shatter Effect, "+this.duration+" turn(s)";
       }
       else
       {
            return "Shatter Effect";
       }
    }
    public ShatterE (int c, int dur, Character p)
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
        if (this.duration<100)
        {
            return "Snare Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Snare Effect";
        }
    }
    public SnareE (int c, int nduration, Character p)
    {
        super(c, p);
        this.duration=nduration;
        this.oduration=nduration;
        this.stackable=false;
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --this.duration;
        if (this.duration<=0)
        {
            hero.remove(this.id, "normal");
            hero.add(new StunE(500, 1, this.prog), true);
        }
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
    @Override
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability!=null&&target.activeability.channelled==true)
       {
           target.activeability.InterruptChannelled(target);
       }
    }
    public StunE (int c, int d, Character p)
    {
        super(c, p);
        this.duration=d; 
        this.oduration=d;
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
    }
}
class WoundE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Wound";
    }
    @Override
    public String geteffname()
    {
        if (this.duration<100)
        {
            return "Wound Effect, "+this.duration+" turn(s)";
        }
        else
        {
            return "Wound Effect";
        }
    }
    public WoundE (int c, int ndur, Character p)
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