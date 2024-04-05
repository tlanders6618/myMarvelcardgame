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
    public OtherEff ()
    {
    }
}
class Empower extends OtherEff
{
    String name; //of hero who made the empowerment
    int index; //of hero who made the empowerment
    int uses; //most cases 1, but not for iron man, whose empower can be used twice or more
    boolean used=false;
    @Override
    public String getimmunityname()
    {
        return "Empower";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public String geteffname() //since every Empower has a unique effect, they don't use standard eff names
    {
        return "Empower "+power+": "+name+", "+uses+" use(s)";
    }
    public Empower (int power, int use, String nname, int index)
    {
        name=nname;
        this.index=index;
        this.power=power;
        uses=use;
        this.stackable=true;
    }
    @Override
    public void onApply (Character target) 
    {
    }
    @Override
    public int UseEmpower(Character hero, Ability ab, boolean use) //use is true for applying effects and false when undoing an empowerment 
    {
        int value=0; 
        if (use==true) //try to activate empowerment
        {
            switch (index) //has to be this way since every empowerment is unique
            {
                case 4: case 39: //39 is for damagecounterremove when intense==true; or else the dmg boost only applies to the first enemy hit by the attack
                if (ab instanceof AttackAb) //damage boost only applies to abs that do dmg
                {
                    used=true;
                    value=this.power; break;
                }
            }
        }
        else
        {
            if (used==true)
            {
                used=false;
                --uses;
            }
        }
        return value;
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        if (uses<=0)
        {
            if (index==39)
            hero.remove(this.hashcode, "silent");
            else
            hero.remove(this.hashcode, "normal");
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
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public String geteffname()
    {
        return "Evade Effect";
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public EvadeE(int nchance) 
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    @Override
    public void onApply (Character target) 
    {
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
    public String getefftype() 
    {
        return "Other";
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
    public FocusE (int nchance, int nduration)
    {
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
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
    public String getefftype()
    {
        return "Other";
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
    public IntensifyE (int nchance, int nstrength, int nduration)
    {
        this.power=nstrength;
        this.duration=nduration;
        this.oduration=nduration;
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
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
class Obsession extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Obsession";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public String geteffname()
    {
        return "Obsession Effect";
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public Obsession() 
    {
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true; 
    }
    @Override
    public void onApply (Character target) 
    {
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
            protector.remove(this.hashcode, "normal");
        }
    }
    @Override
    public String getimmunityname()
    {
        return "Protect";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
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
            hero.remove(this.hashcode, "normal");
        }
        else
        myfriend.lessprotected();
    }
    public ProtectE (int chancce, int ndur) 
    {
        this.chance=chancce;
        this.duration=ndur;
        this.hashcode=Card_HashCode.RandomCode();   
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
            if (e.getimmunityname().equals("Protect")&&e.hashcode!=this.hashcode) //check if the protector has a protect other than this one; protect shouldn't stack with ProtectE
            {
                dupe=true; break;
            }
        }
        for (StatEff e: weakling.effects)
        {
            if (e.getimmunityname().equals("Protect")&&e.hashcode!=this.hashcode)
            {
                dupe=true; break;
            }
        } 
        if (taunter==true)
        {
            System.out.println ("Taunting characters cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.hashcode, "silent");
        }
        else if (invis==true)
        {
            System.out.println ("Invisible characters cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.hashcode, "silent");
        }
        else if (dupe==true)
        {
            System.out.println ("Characters who already have Protect cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(this.hashcode, "silent");
        }
        else 
        {
            ProtectedE pr= new ProtectedE(this.duration);
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
            weakling.remove(myfriend.hashcode, "normal");
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
    public void onApply(Character target)
    {        
    }
    @Override
    public String getimmunityname()
    {
        return "Protect";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    public ProtectedE (int ndur)
    {
        this.duration=ndur; this.hashcode=Card_HashCode.RandomCode();
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
            weakling.remove(this.hashcode, "normal");
        }
    }
    @Override
    public void Nullified(Character target)
    {
        removed=true;
        if (myfriend!=null&&myfriend.removed==false)
        {
            protector.remove(myfriend.hashcode, "normal");
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
    public String getefftype()
    {
        return "Other"; 
    }
    @Override
    public String geteffname()
    {
        return "Redwing Effect";
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public Redwing () 
    {
        this.hashcode=Card_HashCode.RandomCode(); 
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
class ResistanceE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Resistance";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
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
    public ResistanceE (int nchance, int npower, int ndur) 
    {
        this.chance=nchance;
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
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
class SnareE extends OtherEff 
{
    @Override
    public String getimmunityname()
    {
        return "Snare";
    }
    @Override 
    public String getefftype() 
    {
        return "Other";
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
    public SnareE (int nchance, int nduration)
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
class StunE extends OtherEff
{
    @Override
    public String getimmunityname()
    {
        return "Stun";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    public void onApply (Character target)
    {
       target.binaries.add("Stunned");
       if (target.activeability!=null&&target.activeability.channelled==true)
       {
           target.activeability.InterruptChannelled(target, target.activeability);
       }
    }
    public StunE (int nchance, int d)
    {
        this.chance=nchance;
        this.hashcode=Card_HashCode.RandomCode();
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
    public String getefftype()
    {
        return "Other";
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
    public TargetE (int nchance, int npow, int ndur)
    {
        this.duration=ndur;
        this.oduration=ndur;
        this.chance=nchance;
        this.power=npow;
        this.hashcode=Card_HashCode.RandomCode();
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
    public String getefftype()
    {
        return "Other"; 
    }
    @Override
    public String geteffname()
    {
        return "Tracer Effect, "+this.duration+" turn(s)";
    }
    public Tracer (int c, int d) 
    {
        this.hashcode=Card_HashCode.RandomCode(); 
        this.chance=c;
        this.duration=d;
        this.oduration=d;
        this.stackable=true;
    }
    @Override
    public void onApply (Character hero) 
    {
    }
}
class WMTarget extends OtherEff  
{ 
    public WMTarget () 
    {
        this.hashcode=Card_HashCode.RandomCode(); this.power=5; this.stackable=true;
    }
    @Override
    public void onApply (Character target)
    {
        target.DV+=power;
        target.immunities.add("Invisible");
        ArrayList<StatEff> concurrentmodificationexception2electricboogaloo= new ArrayList<StatEff>();
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Invisible"))
            {
                concurrentmodificationexception2electricboogaloo.add(eff);
            }
        }
        for (StatEff eff: concurrentmodificationexception2electricboogaloo)
        {
            target.remove(eff.hashcode, "normal");
        }
    }
    @Override
    public String getimmunityname()
    {
        return "Target";
    }
    @Override 
    public String getefftype()
    {
        return "Other";
    }
    @Override
    public String geteffname()
    {
        return "Target Effect: 5";
    }
    @Override
    public void onTurnEnd (Character hero)
    {
    }
    @Override
    public void Nullified(Character target)
    {
        target.DV-=power;
        target.immunities.remove("Invisible");
    }
}