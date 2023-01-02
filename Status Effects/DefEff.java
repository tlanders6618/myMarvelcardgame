package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: DefEff
 * Purpose: To list all the Defence effects in one file.
 */
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
    @Override
    public void Nullified (Character target)
    {
    }
    @Override
    public void onTurnEnd(Character hero)
    {
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
    Protected[] myfriend=new Protected[1];
    @Override
    public void Extended (int dur)
    {
        this.duration+=dur;
        myfriend[0].duration+=dur;
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
        myfriend[0].lessprotected(616);
        if (this.duration<=0) 
        {
            hero.remove(hero, this.hashcode, false);
        }
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
            myfriend[0]=null;
            hero.remove(hero, this.hashcode, true);
        }
        else 
        {
            Protected pr= new Protected(this.duration);
            myfriend[0]=pr;
            pr.PrepareProtect(protector, weakling);
            weakling.add(weakling, pr);
            pr.lessprotected(this.hashcode); //send over the protect's hashcode in case the protected is nullified
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
        if (myfriend[0]!=null)
        {
            weakling.remove(weakling, myfriend[0].hashcode, false);
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
        for (String binary: hero.binaries)
        {
            if (binary.equalsIgnoreCase("Untargetable"))
            {
                hero.binaries.remove(binary);
            }
        }
        for (StatEff eff: hero.effects) //taunting heroes cannot be protected
        {
            if (eff.getimmunityname().equals("Invisible")||eff.getimmunityname().equals("Protect"))
            {
                hero.remove(hero, eff.hashcode, false);
            }
        }
    }
    @Override
    public void Nullified(Character target)
    {
    }
}