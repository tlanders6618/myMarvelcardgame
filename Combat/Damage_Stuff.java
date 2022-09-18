package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 16/8/22
 * Filename: Damage_Stuff
 * Purpose: To perform damage calculations and other misc. functions.
 */
import java.util.Scanner;
public class Damage_Stuff
{
    public static int DamageFormula (Character dealer, Character chump, int dmg)
    {
        int CC=GetCC(dealer, chump);
        boolean crit=Card_CoinFlip.Flip(CC);
        dmg=GetCritdmg(dealer, dmg, crit);
        dmg=DamageIncrease(dealer, chump, dmg);
        dmg=DamageDecrease(crit, chump, dmg);
        return dmg;
    }
    public static int GetCC (Character dealer, Character chump)
    {
        int CC;
        if (dealer.CC>0)
        {
            CC=(dealer.CC+chump.CritVul)-chump.CritDR;
        }
        else
        {
            CC=dealer.CC-chump.CritDR;
        }
        return CC;
    }
    public static int GetCritdmg (Character dealer, int dmg, boolean crit) //dealer is the lad or lass doing the damage
    {
        if (crit==true)
        {
            //the attacker is a critical hit; damage is increased accordingly
            double ndmg= dmg*dealer.critdmg;
            dmg=5*(int)(Math.floor(ndmg/5)); //crit damage rounded down to nearest 5
        }
        return dmg;
    }
    public static int DamageIncrease (Character dealer, Character chump, int dmg) //dealer is the lad or lass doing the damage and chump is the one taking it
    {
        dmg=dmg+dealer.BD+chump.DV;
        return dmg;
    }
    public static int DamageDecrease (boolean crit, Character chump, int dmg)
    {
        if (crit==true)
        {
            dmg=dmg-(chump.ADR+chump.DR); //ignore resistance
        }
        else
        {
            dmg=dmg-(chump.ADR+chump.RDR+chump.DR);
        }
        if (dmg<0)
        {
            dmg=0;
        }
        return dmg;
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
    public static int GetInput()
    {
        Scanner choose= new Scanner(System.in); 
        int choice;
        try
        {
            choice=choose.nextInt();
        }
        catch (Exception e)
        {
            choice=616;
        }
        choose.close();
        return choice;
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
}