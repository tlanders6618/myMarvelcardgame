package myMarvelcardgamepack;

import java.util.ArrayList;
/**
 * @author Timothy Landers
 * Date of creation: 8/8/22
 * Purpose: To list and implement all the game's Defence effects in one file.
 */
public abstract class DefEff extends StatEff
{
    /**
    * Constructor for all Defence effects. Initialises the effect's status chance, progenitor, and id.
    * <p> Individual Defence effects override StatEff methods as needed for their implementation, and have their own constructors that also call this one.
    * @param c The effect's status chance.
    * @param p The effect's progenitor.
    * @see StatEff
    */
    public DefEff (int c, Character p)
    {
        this.chance=c; this.prog=p; this.id=CardCode.RandomCode();
    }
    /** 
     * The same for all Defence effects. 
     * @return "Defence" 
     */
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
}
class Barrier extends DefEff
{
    @Override
    public String getimmunityname()
    {
        return "Barrier";
    }
    @Override
    public String geteffname()
    {
        return "Barrier: "+this.power+", "+this.duration+" turn(s)";
    }
    public Barrier (int c, int npower, int ndur, Character p) 
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
    }
    @Override
    public void onTurnEnd (Character hero)
    {
        --this.duration; 
        if (this.duration<=0||this.power==0)
        {
            hero.remove(this.id, "normal");
        }
    }
    @Override
    public void onApply (Character hero) 
    {
        hero.BHP+=this.power;
    }
    @Override
    public void Attacked (Character hero, Character attacker, int dmg)
    {
        this.power-=dmg;
        hero.BHP-=dmg; 
        if (this.power<0)
        this.power=0;
        if (hero.BHP<0)
        hero.BHP=0;
    }
    @Override
    public void Nullified(Character target)
    {  
        target.BHP-=this.power; 
        if (target.BHP<0)
        target.BHP=0;
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
    public String geteffname()
    {
        return "Evade";
    }
    public Evade (int achance, Character p) 
    {
        super(achance, p);
        this.stackable=true;
    }
}
class Guard extends DefEff
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
            return "Guard: "+this.power;
        }
        else
        {
            return "Guard: "+this.power+", "+this.duration+" attack(s)";
        }
    }
    public Guard (int c, int npower, int ndur, Character p) 
    //does nothing on its own; all handled by checkguard, called by character.attack as part of dmg calc, before the takedamage stuff
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
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
            return "Protecting: "+weakling.Cname;
        }
        else
        {
            return "Protecting: "+weakling.Cname+", "+this.duration+" turn(s)";
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
    public Protect (int c, int ndur, Character p) 
    {
        super(c, p);
        this.duration=ndur; //odur remains at 616 bc protect cannot be copied
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
            Protected pr= new Protected(this.chance, this.duration, this.prog);
            myfriend=pr;
            pr.myfriend=this;
            pr.PrepareProtect(protector, weakling);
            weakling.add(pr, false);
            String s;
            if (duration>500)
            {
                s="Protecting: "+weakling.Cname;
            }
            else
            {
                s="Protecting: "+weakling.Cname+", "+this.duration+" turn(s)"; //since the add method doesn't print anything for protect effects
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
            if (f.id==myfriend.id&&f.getimmunityname().equalsIgnoreCase("protect")&&f.duration<this.duration)
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
    public Protected (int c, int ndur, Character p)
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
class Resistance extends DefEff
{
    @Override
    public String getimmunityname()
    {
        return "Resistance";
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            return "Resistance: "+this.power;
        }
        else
        {
            return "Resistance: "+this.power+", "+this.duration+" turn(s)";
        }
    }
    public Resistance (int c, int npower, int ndur, Character p) 
    {
        super(c, p);
        this.power=npower;
        this.duration=ndur;
        this.oduration=ndur;
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
    public String geteffname()
    {
        if (this.duration>100)
        {
            return "Taunt";
        }
        else
        {
            return "Taunt, "+this.duration+" turn(s)";
        }
    }
    public Taunt (int c, int ndur, Character p) 
    {
        super(c, p);
        this.duration=ndur;
        this.oduration=ndur;
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
            hero.remove(eff.id, "normal");
        }
    }
}