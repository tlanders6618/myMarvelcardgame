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
        else if (team==true)
        {
            return false;
        }
        return team;
    }
    public static ArrayList GetEffs (Character hero, String effname, String efftype) //for getting all debuffs, buffs, etc on a target
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
            if ((effname.equalsIgnoreCase("any")||eff.getimmunityname().equalsIgnoreCase(effname))&&(efftype.equalsIgnoreCase("any"))||eff.getefftype().equalsIgnoreCase(efftype))
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
    public static ArrayList GetEffsND (Character hero) //for non damaging debuffs
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
           if (eff.getalttype().equals("nondamaging")&&eff.getefftype().equalsIgnoreCase("Debuffs"))
           {
               effs.add(eff); 
           }
        }
        return effs;
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
        }
        else
        {
            hero.ignores.remove ("Taunt");
            hero.ignores.remove ("Protect");
            hero.ignores.remove ("Terror");
            hero.ignores.remove ("Provoke");
            hero.ignores.remove ("Invisible");
            hero.ignores.remove ("Blind");
        }
    }
    public static void RobotImmunities (Character hero, boolean add)
    {
        if (add==true)
        {
            hero.immunities.add ("Bleed");
            hero.immunities.add ("Poison");
            hero.immunities.add ("Copy");
            hero.WiDR=+999;
        }
        else
        {
            hero.immunities.remove ("Bleed");
            hero.immunities.remove ("Poison");
            hero.immunities.remove ("Copy");
            hero.WiDR=-999;
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