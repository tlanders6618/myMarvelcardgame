package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_CoinFlip
 * Purpose: To determine whether an effect applies or fails to apply, and perform other misc. functions.
 */
import java.util.ArrayList;
public class Card_CoinFlip
{
    public static boolean Flip (int chance)
    {
        //applies to critical hits and status effect application
        boolean success=false;
        if (chance>=100)
        {
            success=true;
        }
        else if (chance==50)
        {
            int heads=(int)Math.round(Math.random()); //random number between 1 and 0, rounded to the nearest whole number, i.e. 1 or 0
            if (heads==0)
            {
                success=true;
            }
            else if (heads==1)
            {
                //tails you lose
            }
        }
        else if (chance<=0)
        {
            //still a failure
        }
        return success;
    }
    public static boolean TeamFlip (boolean team)
    {
        if (team==false)
        {
            team=true;
        }
        else if (team==true)
        {
            team=false;
        }
        return team;
    }
    public static ArrayList GetEffs (Character hero, String[] effname, String[] efftype)
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (int i=0; i<effname.length; i++)
        {
            for (StatEff eff: hero.effects)
            {
               if ((eff.getimmunityname().equalsIgnoreCase(effname[i])||effname[i].equalsIgnoreCase("any"))&&(eff.getefftype().equalsIgnoreCase(efftype[i])||efftype[i].equalsIgnoreCase("any")))
               {
                   effs.add(eff);
               }
            }
        }
        return effs;
    }
    public static ArrayList GetEffs (Character hero, String effname, String efftype) //for getting all debuffs, buffs, etc
    {
        ArrayList<StatEff> effs=new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
            if ((eff.getimmunityname().equalsIgnoreCase(effname)||effname.equalsIgnoreCase("any"))&&(eff.getefftype().equalsIgnoreCase(efftype)||efftype.equalsIgnoreCase("any")))
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
    public static void AddInescapable (Character hero, boolean add)
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
    public static void DMGDebImmune (Character hero, boolean add)
    {
       if (add==true)
       {
            hero.immunities.add ("Bleed");
            hero.immunities.add ("Poison");
            hero.immunities.add ("Shock");
            hero.immunities.add ("Burn");
            hero.immunities.add ("Countdown");
       }
       else
       {
            hero.immunities.remove ("Bleed");
            hero.immunities.remove ("Poison");
            hero.immunities.remove ("Shock");
            hero.immunities.remove ("Burn");
            hero.immunities.remove ("Countdown");
       }
    }
    public static ArrayList ToList (Character[] list)
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