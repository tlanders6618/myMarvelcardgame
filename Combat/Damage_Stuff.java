package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 16/8/22
 * Filename: Damage_Stuff
 * Purpose: To perform damage, crit, damage reduction, and miss calculations, and get user input.
 */
import java.util.Scanner; import java.util.ArrayList;
public class Damage_Stuff
{
    public static int DamageFormula (Character dealer, Character chump, int dmg)
    {
        int CC=GetCC(dealer, chump);
        boolean crit=CoinFlip.Flip(CC);
        dmg=GetCritdmg(dealer, dmg, crit, chump);
        dmg=DamageIncrease(dealer, chump, dmg);
        dmg=DamageDecrease(dealer, crit, chump, dmg);
        return dmg;
    }
    public static int GetInput()
    {
        int choice; 
        Card_Game_Main.trash= new Scanner(System.in);
        try
        {
            choice=Card_Game_Main.trash.nextInt();
        }
        catch (Exception e)
        {
            choice=616;
        }
        return choice;
    }
    public static int GetCC (Character dealer, Character chump) //crit chance
    {
        int CC=dealer.CC;
        if (CC>0)
        {
            CC+=(chump.CritVul-chump.CritDR);
        }
        return CC;
    }
    public static int GetCritdmg (Character dealer, int dmg, boolean crit, Character chump) 
    {
        if (crit==true)
        {
            //the attack is a critical hit; damage is increased accordingly
            System.out.println(dealer.Cname+"'s attack was critical!");
            double ndmg= dmg*dealer.critdmg;
            dmg=5*(int)(Math.floor(ndmg/5)); //crit damage rounded down to nearest 5
            dealer.onCrit(dealer, chump);
        }
        return dmg;
    }
    public static int DamageIncrease (Character dealer, Character chump, int dmg) //dealer is the one doing the damage and chump is the one taking it
    {
        dmg=dmg+dealer.BD+chump.DV; //dealer's dmg boosts plus the chump's damage vulnerabilities
        return dmg;
    }
    public static int DamageDecrease (Character dealer, boolean crit, Character chump, int dmg)
    {
        if (dealer!=null&&crit==true)
        {
            dmg=dmg-(chump.ADR+chump.DR); //crits ignore resistance 
        }
        else if (dealer!=null&&dealer.ignores.contains("Defence"))
        {
            dmg=dmg-(chump.ADR+chump.DR+chump.PRDR); //ignore resistance but not resistance Effects
        }
        else if (dealer!=null&&dealer.ignores.contains("DR"))
        {
            dmg=dmg; //ignore all damage reduction
        }
        else //normal formula; accounts for all forms of DR
        {
            dmg=dmg-(chump.ADR+chump.RDR+chump.DR+chump.PRDR);
        }
        if (dmg<0)
        {
            dmg=0;
        }
        return dmg;
    }
    public static void CheckBlind (Character hero)
    {
        boolean nomiss=true; 
        if (!(hero.binaries.contains("Missed"))&&!(hero.immunities.contains("Missed"))&&hero.CheckFor(hero, "Blind", false)==true&&!(hero.ignores.contains("Blind")))
        {
            nomiss=CoinFlip.Flip(hero.accuracy);
            if (nomiss==false)
            {
                System.out.println ("\n"+hero.Cname+"'s attack missed.");
                hero.binaries.add("Missed"); //missed is automatically removed after a hero uses any ability on a target
            }
        }
    }
    public static void CheckEvade (Character dealer, Character target)
    {
        if (!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))&&(target.CheckFor(target, "Evade", false)==true||target.CheckFor(target, "Evasion", false)==true))
        {
            if (!(dealer.ignores.contains("Evade"))&&!(target.binaries.contains("Shattered"))&&!(target.binaries.contains("Stunned")))
            {
                for (StatEff effect: target.effects)
                {
                    if (effect.getimmunityname().equalsIgnoreCase("Evade"))
                    {
                        if (effect.getefftype().equalsIgnoreCase("Defence")&&!(dealer.ignores.contains("Defence"))) //evade is a defence effect
                        {
                            target.remove(target, effect.hashcode, "normal");
                            System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                            dealer.binaries.add("Missed");
                            break;
                        }
                        else if (effect.getefftype().equalsIgnoreCase("Other")) //evade Effects cannot be stopped
                        {
                            target.remove(target, effect.hashcode, "normal");
                            System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                            dealer.binaries.add("Missed");
                            break;
                        }
                    }
                    else if (effect.getimmunityname().equalsIgnoreCase("Evasion")&&!(dealer.ignores.contains("Evasion")))
                    {
                        boolean evade=CoinFlip.Flip((50+target.Cchance));
                        if (evade==true)
                        {
                            System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                            dealer.binaries.add("Missed");
                            break;
                        } 
                    }
                }
            }
        }
    }
    public static int CheckGuard (Character dealer, Character target, int dmg)
    {
        if (dmg>0&&!(dealer.ignores.contains("Guard"))&&!(target.binaries.contains("Stunned"))) //conditions that would prevent guard from triggering
        { 
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equals("Guard"))
                {
                    if (eff.getefftype().equals("Defence")&&!(dealer.ignores.contains("Defence")))
                    {
                        int odmg=dmg;
                        dmg=eff.UseGuard(dmg);
                        System.out.println ("\n"+target.Cname+"'s Guard reduced" +dealer.Cname+"'s attack damage by "+(odmg-dmg));
                        if (dmg<=0)
                        {
                            dmg=0;
                            break; //no point in wasting guards on nothing
                        }
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Other"))
                    {   
                        int odmg=dmg;
                        dmg=eff.UseGuard(dmg);
                        System.out.println ("\n"+target.Cname+"'s Guard reduced" +dealer.Cname+"'s attack damage by "+(odmg-dmg));
                        if (dmg<=0)
                        {
                            dmg=0;
                            break; //no point in wasting guards on nothing
                        }
                    }
                }
            }
        }
        return dmg;
    }
}