package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: DefEff
 * Purpose: To list all the Defence effects in one file.
 */
import java.util.ArrayList;
abstract class DefEff extends StatEff
{
    public DefEff ()
    {
    }
}
class Evade extends DefEff
{
    @Override
    public String getimmunityname()
    {
        return "Evade";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public String geteffname()
    {
        return "Evade";
    }
    public Evade (int achance) 
    {
        this.chance=achance;
        this.hashcode=Card_HashCode.RandomCode();
        this.stackable=true;
    }
    @Override
    public void onApply (Character target)  
    {
    }
}
class Protect extends DefEff
{
    Character protector;
    Character weakling;
    Protected myfriend;
    boolean removed=false; //to prevent infinite loop of protect and protected trying to remove each other; this lets them know whether it's necessary or not
    @Override
    public void Extended (int dur, Character ignore)
    {
        this.duration+=dur; 
        if (myfriend.duration<this.duration)
        myfriend.duration+=dur;
        if (this.duration<=0)
        {
            protector.remove(protector, this.hashcode, "normal");
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
        return "Defence";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration>100)
        {
            name="Protecting: "+weakling.Cname;
        }
        else
        {
            name="Protecting: "+weakling.Cname+", "+this.duration+" turn(s)";
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
            hero.remove(hero, this.hashcode, "normal");
        }
        else
        myfriend.lessprotected();
    }
    public Protect (int chancce, int ndur) 
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
        if (Character.CheckFor(weakling, "Taunt", false)==true||Character.CheckFor(protector, "Taunt", false)==true)
        taunter=true;  
        if (Character.CheckFor(weakling, "Invisible", false)==true||Character.CheckFor(protector, "Invisible", false)==true)
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
            hero.remove(hero, this.hashcode, "silent");
        }
        else if (invis==true)
        {
            System.out.println ("Invisible characters cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(hero, this.hashcode, "silent");
        }
        else if (dupe==true)
        {
            System.out.println ("Characters who already have Protect cannot be Protected.");
            myfriend=null;
            removed=true;
            hero.remove(hero, this.hashcode, "silent");
        }
        else 
        {
            Protected pr= new Protected(this.duration);
            myfriend=pr;
            pr.myfriend=this;
            pr.PrepareProtect(protector, weakling);
            weakling.add(weakling, pr);
            String s;
            if (duration>500)
            {
                s="Protecting: "+weakling.Cname;
            }
            else
            {
                s="Protecting: "+weakling.Cname+", "+this.duration+" turn(s)"; //since the add method doesn't print anything for protect effects
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
            weakling.remove(weakling, myfriend.hashcode, "normal");
        }
    }
    @Override
    public Character getProtector ()
    {
        return protector;
    }
}
class Protected extends DefEff 
{
    Character protector;
    Character weakling;
    Protect myfriend;
    boolean removed;
    @Override
    public void Extended (int dur, Character ignore)
    {
        this.duration+=dur;
        for (StatEff f: protector.effects)
        {
            if (f.hashcode==myfriend.hashcode&&f.getimmunityname().equalsIgnoreCase("protect")&&f.duration<this.duration)
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
        return "Defence";
    }
    public Protected (int ndur)
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
            name="Protected by: "+protector.Cname;
        }
        else
        {
            name="Protected by: "+protector.Cname+", "+this.duration+" turn(s)";
        }
        return name;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public void lessprotected ()
    {
        --this.duration;
        if (this.duration<=0)
        {
            removed=true;
            weakling.remove(weakling, this.hashcode, "normal");
        }
    }
    @Override
    public void Nullified(Character target)
    {
        removed=true;
        if (myfriend!=null&&myfriend.removed==false)
        {
            protector.remove(protector, myfriend.hashcode, "normal");
        }
    }
}
class Resistance extends DefEff
{
    @Override
    public String getimmunityname()
    {
        return "Resistance";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (duration>500)
        {
            name="Resistance: "+this.power;
        }
        else
        {
            name="Resistance: "+this.power+", "+this.duration+" turn(s)";
        }
        return name;
    }
    public Resistance (int nchance, int npower, int ndur) 
    {
        this.chance=nchance;
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Resistance (int nchance, int npower) 
    {
        this.chance=nchance;
        this.power=npower;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character hero) 
    {
        hero.RDR+=power;
    }
    @Override
    public void Nullified(Character target)
    {
        target.RDR-=power;   
    }
}
class Taunt extends DefEff 
{
    @Override
    public String getimmunityname()
    {
        return "Taunt";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public String geteffname()
    {
        String name;
        if (this.duration>100)
        {
            name="Taunt";
        }
        else
        {
            name="Taunt, "+this.duration+" turn(s)";
        }
        return name;
    }
    public Taunt (int chan, int ndur) 
    {
        this.chance=chan;
        this.duration=ndur;
        this.oduration=ndur;
        this.hashcode=Card_HashCode.RandomCode();
    }
    public Taunt (int cham) 
    {
        this.chance=cham;
        this.hashcode=Card_HashCode.RandomCode();
    }
    @Override
    public void onApply (Character hero) 
    {
        if (hero.targetable==false)
        hero.targetable=true;
        ArrayList<StatEff> effs= new ArrayList<StatEff>();
        for (StatEff eff: hero.effects) //taunting heroes cannot be protected or invisible
        {
            if (eff.getimmunityname().equals("Invisible")||eff.getimmunityname().equals("Protect"))
            {
                effs.add(eff);
            }
        }
        for (StatEff eff: effs) //taunting heroes cannot be protected or invisible
        {
            hero.remove(hero, eff.hashcode, "normal");
        }
    }
}