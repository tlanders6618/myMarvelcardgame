package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 10/8/22
 * Filename: OtherEff
 * Purpose: To list all the Other status effects in one file.
 */
 
public abstract class OtherEff extends StatEff 
{
    public OtherEff ()
    {
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
    public void Nullified(Character target)
    {
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
    public void Nullified(Character target)
    {
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
    @Override
    public void Extended (int dur)
    {
        this.duration+=dur;
        myfriend.duration+=dur;
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
        myfriend.lessprotected(616);
        if (this.duration<=0) 
        {
            hero.remove(hero, this.hashcode, false);
        }
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
        boolean taunter=false;        
        for (StatEff eff: weakling.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Taunt"))
            {
                taunter=true;
                break;
            }
        }
        for (StatEff eff: protector.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Taunt"))
            {
                taunter=true;
                break;
            }
        }
        if (taunter==true)
        {
            System.out.println ("Taunting characters cannot be Protected.");
            myfriend=null;
            hero.remove(hero, this.hashcode, true);
        }
        else 
        {
            ProtectedE pr= new ProtectedE(this.duration);
            myfriend=pr;
            pr.PrepareProtect(protector, weakling);
            weakling.add(weakling, pr);
            pr.lessprotected(this.hashcode); //send over the protect's hashcode in case the protected is nullified
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
        if (myfriend!=null)
        {
            weakling.remove(weakling, myfriend.hashcode, false);
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
    int procode; //hashcode of protector's Protect
    @Override
    public void Extended (int dur)
    {
        for (StatEff f: protector.effects)
        {
            if (f.getimmunityname().equalsIgnoreCase("protect"))
            {
                f.Extended(dur);
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
    public void lessprotected(int code)
    {
        if (code==616)
        {
            --this.duration;
        }
        else
        {
            procode=code;
        }
    }
    @Override
    public void Nullified(Character target)
    {
        target.remove(target, this.hashcode, false); //target should be the one being protected     
        protector.remove(protector, procode, false);
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
        this.hashcode=Card_HashCode.RandomCode(); this.stackable=true;
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
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Invisible"))
            {
                target.remove(target, eff.hashcode, false);
            }
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