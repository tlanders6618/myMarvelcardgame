package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: DefEff
 * Purpose: To list all the Defence effects in one file because there's a lot of them.
 */
abstract class DefEff extends StatEff
{
    public DefEff ()
    {
    }
}
class Evade extends DefEff
{
    String name="Evade";
    int hashcode;
    int chance=616;
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
        return "Evade";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void Nullified (Character target)
    {
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public Evade (int achance) 
    {
        chance=achance;
        hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character target)  
    {
    }
}
class Protect extends DefEff
{
    boolean stackable=false;
    int hashcode;
    Character protector;
    Character weakling;
    String name;
    int duration=616;
    int chance=616;    
    Protected[] myfriend=new Protected[1];
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
        return "Protect";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            name="Protecting: "+weakling.Cname;
        }
        else
        {
            name="Protecting: "+weakling.Cname+", "+duration+" turn(s)";
        }
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void onTurnEnd(Character hero) 
    {
        --duration; 
        myfriend[0].lessprotected(616);
        if (duration<=0) 
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public Protect (int chancce, int ndur) 
    {
        chance=chancce;
        duration=ndur;
        hashcode=Card_HashCode.RandomCode();   
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
            hero.remove(hero, hashcode);
        }
        else 
        {
            Protected pr= new Protected(duration);
            myfriend[0]=pr;
            pr.PrepareProtect(protector, weakling);
            weakling.add(weakling, pr);
            pr.lessprotected(hashcode); //send over the protect's hashcode in case the protected is nullified
        }
    }
    @Override
    public void Nullified(Character target)
    {
        if (myfriend[0]!=null)
        {
            weakling.remove(weakling, myfriend[0].getcode());
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
    boolean stackable=false;
    int hashcode;
    Character protector;
    Character weakling;
    String name;
    int duration=616;
    int chance=616;
    int procode; //hashcode of protector's Protect
    @Override
    public void onApply(Character target)
    {        
    }
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
        return "Protect";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    public Protected (int ndur)
    {
        duration=ndur; hashcode=Card_HashCode.RandomCode();
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
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            name="Protected by: "+protector.Cname;
        }
        else
        {
            name="Protected by: "+protector.Cname+", "+duration+" turn(s)";
        }
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
    }
    public void lessprotected(int code)
    {
        if (code==616)
        {
            --duration;
        }
        else
        {
            procode=code;
        }
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    @Override
    public void Nullified(Character target)
    {
        target.remove(target, hashcode); //target should be the weakling     
        protector.remove(protector, procode);
    }
}
class Resistance extends DefEff
{
    boolean stackable=false;
    int hashcode;
    String name;
    int duration=616;
    int oduration=616;
    int power=616;
    int chance=616;
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
        return "Resistance";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            name="Resistance: "+power;
        }
        else
        {
            name="Resistance: "+power+", "+duration+" turn(s)";
        }
        return name;
    }
    @Override
    public int getpower()
    {
        return power;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public Resistance (int nchance, int npower, int ndur) 
    {
        chance=nchance;
        power=npower;
        duration=ndur;
        oduration=duration;
        hashcode=Card_HashCode.RandomCode();
    }
    public Resistance (int nchance, int npower) 
    {
        chance=nchance;
        power=npower;
        hashcode=Card_HashCode.RandomCode();
    }
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
    boolean stackable=false;
    int hashcode;
    String name;
    int duration=616;
    int chance=499;
    int oduration=616;
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
        return "Taunt";
    }
    @Override 
    public String getefftype()
    {
        return "Defence";
    }
    @Override
    public int getcode ()
    {
        return hashcode;
    }
    @Override
    public String geteffname()
    {
        if (duration>500)
        {
            name="Taunt";
        }
        else
        {
            name="Taunt, "+duration+" turn(s)";
        }
        return name;
    }
    @Override
    public int getpower()
    {
        return 616;
    }
    @Override
    public void onTurnEnd(Character hero)
    {
        --duration;
        if (duration<=0)
        {
            hero.remove(hero, hashcode);
        }
    }
    @Override
    public void onTurnStart(Character hero)
    {
    }
    public Taunt (int chan, int ndur) 
    {
        chance=chan;
        duration=ndur;
        oduration=duration;
        hashcode=Card_HashCode.RandomCode();
    }
    public Taunt (int cham) 
    {
        chance=cham;
        hashcode=Card_HashCode.RandomCode();
    }
    public void onApply (Character hero) 
    {
        for (String binary: hero.binaries)
        {
            if (binary.equalsIgnoreCase("Invisible"))
            {
                hero.binaries.remove(binary);
            }
            else if (binary.equalsIgnoreCase("Untargetable"))
            {
                hero.binaries.remove(binary);
            }
        }
        for (StatEff eff: hero.effects) //taunting heroes cannot be protected
        {
            if (eff instanceof Protect||eff instanceof Protected)
            {
                hero.remove(hero, eff.getcode());
            }
        }
    }
    @Override
    public void Nullified(Character target)
    {
    }
}