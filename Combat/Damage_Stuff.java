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
    static boolean print=false; //whether to print that the hero's attack failed to crit; only if crit chance was over 0, as decided below in getcc and getcritdmg
    public static int DamageFormula (Character dealer, Character chump, int dmg)
    {
        if (dealer.CC>0) //player is expecting (potential) crit if cc is over 0, so print something to acknowledge the crit calculation
        print=true;
        int CC=GetCC(dealer, chump);
        boolean crit=CoinFlip.Flip(CC);
        dmg=GetCritdmg(dealer, dmg, crit, chump);
        dmg=DamageIncrease(dealer, chump, dmg);
        dmg=DamageDecrease(dealer, crit, chump, dmg);
        print=false;
        return dmg;
    }
    public static void CheckBarrier (Character hero, Character dealer, int dmg) //called as part of every damage calculation; for taking health dmg 
    {
        if (hero.CheckFor("Barrier", false)==true)
        {
            if (dealer!=null&&(dealer.ignores.contains("Barrier")||dealer.ignores.contains("Defence")))
            {
            }
            else if (hero.BHP>=dmg) 
            {
                if (dealer==null) //update barrier strength if dmg was dot, sourceless, or hp loss, since hploss attacks have base dmg of 0 so they don't update barrier in .attacked
                {
                    for (StatEff e: hero.effects) //barrier.attacked is already triggered by hero.attacked; so only call it here if hero took dmg when the dealer was null; see above
                    {
                        if (e.getimmunityname().equals("Barrier")) 
                        {
                            e.Attacked(hero, dealer, dmg); 
                        }
                    }
                }
            }
            else if (hero.BHP<dmg) //barrier broken; can't absorb all the damage
            {
                hero.HP=(hero.HP+hero.BHP)-dmg; 
                if (dealer==null)
                {
                    for (StatEff e: hero.effects)
                    {
                        if (e.getimmunityname().equals("Barrier"))
                        {
                            e.Attacked(hero, dealer, dmg);
                        }
                    }
                }
            }
        }
        else
        {
            if (hero.immunities.contains("Damage")) //for speedball, slapstick, etc
            {
                dmg=0; //(mainly bc speedball) cannot just do ADR+=999, or reflect, dmgtaken, etc will not work, so instead set dmg=0 after it's dealt but before being harmed
            }
            hero.HP-=dmg;
        }
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
        int CC=dealer.CC-dealer.nCC;
        if (dealer.CC>0) 
        {
            CC+=chump.CritVul; 
        }
        CC-=chump.CritDR;
        return CC;
    }
    public static int GetCritdmg (Character dealer, int dmg, boolean crit, Character chump) 
    {
        if (crit==true) //the attack is a critical hit; damage is increased accordingly
        {
            //if (print==true)
            System.out.println(dealer.Cname+"'s attack was critical!");
            double ndmg= dmg*dealer.critdmg;
            dmg=5*(int)(Math.floor(ndmg/5)); //crit damage rounded down to nearest 5
            dealer.onCrit(chump);
        }
        else
        {
            if (print==true)
            System.out.println(dealer.Cname+"'s attack failed to crit.");
        }
        return dmg;
    }
    public static int DamageIncrease (Character dealer, Character chump, int dmg) //dealer is the one doing the damage and chump is the one taking it
    {
        dmg=dmg+dealer.BD+dealer.PBD+chump.DV; //dealer's dmg boosts plus the chump's damage vulnerabilities
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
    public static void ElusiveDmg (Character dealer, Character target, int dmg, String cause)
    {
        if (target.immunities.contains("Damage"))
        dmg=0; //should print dmg statement even if immune, to avoid confusion
        else
        {
            dmg-=target.ADR;
            if (dmg<0)
            dmg=0;
        }
        if (dealer!=null) 
        {
            switch (cause)
            {
                case "counter": System.out.println("\n"+dealer+" counterattacked "+target+" for "+dmg+" damage!"); break;
                case "reflect": System.out.println ("\n"+dealer+" Reflected "+dmg+" damage back to "+target); break; 
                case "ricochet": System.out.println ("\n"+target+" took "+dmg+" Ricochet damage"); break; //ricochet from an ability
                case "default": System.out.println ("\n"+dealer+" did "+dmg+" damage to "+target); break;
            }
        }
        else 
        {
            switch (cause)
            {
                case "countdown": System.out.println("\n"+target+" took "+dmg+" damage from their Countdown"); break;
                case "ricochet": System.out.println ("\n"+target+" took "+dmg+" Ricochet damage"); break; //ricochet from shock
                case "default": System.out.println ("\n"+target+" took "+dmg+" damage"); break; //used for selfdmg/battlefield effs
            }
        }
        target.TakeDamage(dmg, false); 
    }
    public static void CheckBlind (Character hero)
    {
        boolean nomiss=true; 
        if (!(hero.binaries.contains("Missed"))&&!(hero.immunities.contains("Missed"))&&hero.CheckFor("Blind", false)==true&&!(hero.ignores.contains("Blind")))
        {
            nomiss=CoinFlip.Flip(hero.accuracy);
            if (nomiss==false)
            {
                System.out.println ("\n"+hero.Cname+"'s attack missed.");
                hero.binaries.add("Missed"); //missed is automatically removed after a hero uses any ability on a target
            }
            else
            System.out.println("\n"+hero.Cname+"'s attack hit!");
        }
    }
    public static void CheckEvade (Character dealer, Character target)
    {
        if (!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))&&(target.CheckFor("Evade", false)==true||target.CheckFor("Evasion", false)==true))
        {
            if (!(dealer.ignores.contains("Evade"))&&!(target.binaries.contains("Shattered"))&&!(target.binaries.contains("Stunned"))&&target.CheckFor("Soaked", false)==false)
            {
                for (StatEff effect: target.effects)
                {
                    if (effect.getimmunityname().equalsIgnoreCase("Evade")) //evade Effects can't be stop, but regular evade can be ignored if attacker ignores defence
                    {
                        if (effect.getefftype().equalsIgnoreCase("Other")||(effect.getefftype().equalsIgnoreCase("Defence")&&!(dealer.ignores.contains("Defence"))))
                        {
                            System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                            target.remove(effect.id, "normal");
                            dealer.binaries.add("Missed");
                            target.onEvade(dealer);
                            break;
                        }
                    }
                    else if (effect.getimmunityname().equalsIgnoreCase("Evasion")&&!(dealer.ignores.contains("Evasion")))
                    {
                        boolean evade=CoinFlip.Flip((50+target.Cchance));
                        if (evade==true)
                        {
                            System.out.println ("\n"+target.Cname+" successfully Evaded "+dealer.Cname+"'s attack!");
                            dealer.binaries.add("Missed");
                            target.onEvade(dealer);
                            break;
                        } 
                        else
                        System.out.println (target.Cname+" failed to Evade "+dealer.Cname+"'s attack.");
                    }
                }
            }
        }
    }
    public static int CheckGuard (Character dealer, Character target, int dmg) //called by character.attack
    {
        if (dmg>0&&!(dealer.ignores.contains("Guard"))&&!(target.binaries.contains("Stunned"))) //conditions that would prevent guard from triggering
        { 
            ArrayList<StatEff> neffs= new ArrayList<StatEff>(target.effects);
            for (StatEff eff: neffs)
            {
                if (eff.getimmunityname().equals("Guard"))
                {
                    if (eff.getefftype().equalsIgnoreCase("Other")||(eff.getefftype().equals("Defence")&&!(dealer.ignores.contains("Defence"))))
                    {
                        dmg=eff.UseGuard(dealer, target, dmg); 
                        if (dmg==0) 
                        break; //no point in wasting guards on nothing
                    }
                }
            }
        }
        return dmg;
    }
}