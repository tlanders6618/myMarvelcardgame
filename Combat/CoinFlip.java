package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_CoinFlip
 * Purpose: To determine whether an effect applies or fails to apply, and perform other misc. functions.
 */
import java.util.ArrayList;
public class CoinFlip
{
    public static boolean Flip (int chance) //checks whether an ability/stateff succeeds or fails to apply
    {
        if (chance>=100)
        return true;
        else if (chance==50)
        {
            int heads=(int)(Math.round(Math.random())); //random number between 1 and 0, rounded to the nearest whole number, i.e. 1 or 0
            if (heads==0)
            {
                //System.out.println("FAIL");
                return true;
            }
            else if (heads==1)
            {
                return false;
            }
        }
        return false;
    }
    public static boolean TeamFlip (boolean team) //returns boolean that's the opposite of the one sent over
    {
        if (team==false)
        {
            return true;
        }
        else //if (team==true)
        {
            return false;
        }
    }
    public static ArrayList GetEffs (Character hero, String effname, String efftype) //for getting all debuffs, buffs, etc on a target
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        { 
            if ((effname.equalsIgnoreCase("any")||eff.getimmunityname().equalsIgnoreCase(effname))&&(efftype.equalsIgnoreCase("any")||eff.getefftype().equalsIgnoreCase(efftype)))
            {
                effs.add(eff);
            }
         }
        return effs;
    }
    public static ArrayList GetEffs (Character hero, String[] effname, String[] efftype) //only adds effs that match both the immunityname and efftype unless "any" is the name/type
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (int i=0; i<effname.length; i++)
        {
            for (StatEff eff: hero.effects)
            {
               if ((effname[i].equalsIgnoreCase("any")||eff.getimmunityname().equalsIgnoreCase(effname[i]))&&(efftype[i].equalsIgnoreCase("any")||eff.getefftype().equalsIgnoreCase(efftype[i])))
               {
                   effs.add(eff); 
               }
            }
        }
        return effs;
    }
    public static ArrayList GetEffs (Character hero, String[] effname, String[] efftype, String ex, boolean type) //same as above but excluding a certain type
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (int i=0; i<effname.length; i++)
        {
            for (StatEff eff: hero.effects)
            {
               if ((effname[i].equalsIgnoreCase("any")||eff.getimmunityname().equalsIgnoreCase(effname[i]))&&(efftype[i].equalsIgnoreCase("any")||eff.getefftype().equalsIgnoreCase(efftype[i])))
               {
                    if (type==false&&!(eff.getimmunityname().equals(ex)))
                    effs.add(eff);
                    else if (type==true&&!(eff.getefftype().equals(ex)))
                    effs.add(eff);
               }
            }
        }
        return effs;
    }
    public static ArrayList GetEffsND (Character hero, boolean stunner) //for non damaging debuffs; stunner is whether stun should be included or not
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
           if (eff.getalttype().equals("nondamaging")&&eff.getefftype().equalsIgnoreCase("Debuffs"))
           {
               if (stunner==true)
               effs.add(eff); 
               else if (stunner==false&&!(eff.getimmunityname().equals("Stun")))
               effs.add(eff); 
           }
        }
        return effs;
    }
    public static ArrayList GetEffsD (Character hero, boolean debuff) //for damaging debuffs/effects
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
            if (eff.getalttype().equals("damaging"))
            {
                if (debuff==true)
                {
                    if (eff.getefftype().equalsIgnoreCase("Debuffs"))
                    {
                        effs.add(eff); 
                    }
                }
                else
                {
                    if (eff.getefftype().equalsIgnoreCase("Debuffs")||eff.getefftype().equalsIgnoreCase("Other"))
                    {
                        effs.add(eff); 
                    }
                }
            }
        }
        return effs;
    }
    public static int GetStatCount (Character hero, String name, String type)
    {
        int num=0;
        for (StatEff eff: hero.effects)
        {
            if ((eff.getimmunityname().equalsIgnoreCase(name)||name.equalsIgnoreCase("any"))&&(eff.getefftype().equalsIgnoreCase(type)||type.equalsIgnoreCase("any")))
            {
                ++num;
            }
        }
        return num;
    }
    public static StatEff GetRandomStatEff (Character hero, String name, String type) //ensure size is appropriate before calling this and whatnot
    {
        int go=CoinFlip.GetStatCount (hero, name, type); //gets random stateffs of a specific type, e.g. debuffs
        if (go>0)
        {
            if (name.equalsIgnoreCase("any")&&!(type.equalsIgnoreCase("any")))
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getefftype().equalsIgnoreCase(type)));
                return hero.effects.get(rando); 
            }
            else if (type.equalsIgnoreCase("any")&&!(name.equalsIgnoreCase("any")))
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getimmunityname().equalsIgnoreCase(name)));
                return hero.effects.get(rando); //returns the effect in the array at the index of the given random index number
            }
            else if (type.equalsIgnoreCase("any")&&name.equalsIgnoreCase("any"))
            {
                int rando=hero.effects.size(); 
                rando= (int)(Math.random()*hero.effects.size());
                return hero.effects.get(rando); 
            }
            else
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getefftype().equalsIgnoreCase(type))||!(hero.effects.get(rando).getimmunityname().equalsIgnoreCase(name)));
                return hero.effects.get(rando); 
            }
        }
        else
        {
            return null;
        }
    }
    public static void IgnoreTargeting (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.ignores.add ("Taunt");
            hero.ignores.add ("Protect");
            hero.ignores.add ("Terror");
            hero.ignores.add ("Provoke");
        }
        else
        {
            hero.ignores.remove ("Taunt");
            hero.ignores.remove ("Protect");
            hero.ignores.remove ("Terror");
            hero.ignores.remove ("Provoke");
        }
    }
    public static void AddInescapable (Character hero, boolean add) //can ignore everything except untargetable and banish
    {
        if (add==true)
        { 
            hero.ignores.add ("Taunt");
            hero.ignores.add ("Protect");
            hero.ignores.add ("Terror");
            hero.ignores.add ("Provoke");
            hero.ignores.add ("Invisible");
            hero.ignores.add ("Blind");
            hero.ignores.add ("Evade");
        }
        else
        { 
            hero.ignores.remove ("Taunt");
            hero.ignores.remove ("Protect");
            hero.ignores.remove ("Terror");
            hero.ignores.remove ("Provoke");
            hero.ignores.remove ("Invisible");
            hero.ignores.remove ("Blind");
            hero.ignores.remove ("Evade");
        }
    }
    public static void RobotImmunities (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.immunities.add ("Bleed");
            hero.immunities.add ("Poison");
            hero.immunities.add ("Copy");
            hero.WiDR+=999;
        }
        else
        {
            hero.immunities.remove ("Bleed");
            hero.immunities.remove ("Poison");
            hero.immunities.remove ("Copy");
            hero.WiDR-=999;
        }
    }
    public static void DOTImmune (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.immunities.add ("Bleed");
            hero.immunities.add ("Poison");
            hero.immunities.add ("Shock");
            hero.immunities.add ("Burn");
        }
        else
        {
            hero.immunities.remove ("Bleed");
            hero.immunities.remove ("Poison");
            hero.immunities.remove ("Shock");
            hero.immunities.remove ("Burn");
        }
    }
    public static void DMGImmune (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.BlDR+=999;
            hero.PoDR+=999;
            hero.ShDR+=999;
            hero.BuDR+=999;
            hero.WiDR+=999;
            hero.immunities.add("Damage"); //means enemies can do dmg to hero for things like ricochet and reflect, but hero takes 0 (none of it); different from doing ADR+=999
        }
        else
        {
            hero.BlDR-=999;
            hero.PoDR-=999;
            hero.ShDR-=999;
            hero.BuDR-=999;
            hero.WiDR-=999;
            hero.immunities.remove("Damage");
        }
    }
    public static void StatImmune (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.immunities.add ("Buffs");
            hero.immunities.add ("Debuffs");
            hero.immunities.add ("Heal");
            hero.immunities.add ("Defence");
            hero.immunities.add ("Other");
        }
        else
        {
            hero.immunities.remove ("Buffs");
            hero.immunities.remove ("Debuffs");
            hero.immunities.remove ("Heal");
            hero.immunities.remove ("Defence");
            hero.immunities.remove ("Other");
        }
    }
    public static ArrayList ToList (Character[] list) //converts array to arraylist and returns arraylist
    {
        ArrayList<Character> team= new ArrayList<Character>();
        for (Character c: list)
        {
            if (c!=null)
            {
                team.add(c);
            }
        }   
        return team;
    }
}