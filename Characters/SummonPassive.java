package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/11/22
 * Filename: SummonPassive
 * Purpose: Contains all of the passives for Summons.
 */
import java.util.ArrayList;
public class SummonPassive
{
    public static void Decoy (Character decoy) //onsummon
    {
        decoy.add(new Taunt(500, 616)); CoinFlip.StatImmune(decoy, true); decoy.binaries.add("Stunned");
    }
    public static int Daemon (Character matt, boolean start, Character attacker, int dmg)
    {
        if (start==true) //onsummon
        {
            boolean yes=CoinFlip.Flip(50); matt.immunities.add("Persuaded");
            if (yes==true)
            {
                Provoke g= new Provoke(500, 1, matt);
                Character jill=Ability.GetRandomHero(matt, false, false);
                StatEff.CheckApply(matt, jill, g);
            }
            else
            System.out.println(matt.Cname+"'s Provoke failed to apply due to chance.");
        }
        else //takedamage
        {
            if (attacker.CheckFor("Provoke", false)==true)
            {
                dmg-=10; 
            }
        }
        return dmg;
    }
    public static void LilDoomie (Character lil, boolean start, Character ally)
    {
        if (start==true) //onsummon
        {
            CoinFlip.RobotImmunities(lil, true); lil.immunities.add("Persuaded"); lil.immunities.add("Control");
        }
        else //allydeath
        {
            if (ally==lil.passivefriend[0]&&lil.dead==false)
            {
                System.out.println("CONNECTION LOST. SELF TERMINATION IN 3...2...1...");
                lil.onDeath(null, "self");
            }
        }
    }
    public static void Drone (Character husk, boolean start, StatEff effecter)
    {
        if (start==true) //onsummon
        {
            CoinFlip.RobotImmunities(husk, true); husk.immunities.add("Persuaded"); husk.immunities.add("Control");
        }
        else //hero.add
        {
            if (!(husk.binaries.contains("Stunned"))&&husk.passivefriend[0]!=null&&husk.passivefriend[0].index==27) //only works on ultron due to the message printing his name
            {
                Character target=husk.passivefriend[0]; //aka ultron
                String name=effecter.getimmunityname(); int dur=effecter.oduration; int pow=effecter.power;
                String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "false"}; String[][] morbintime=StatFactory.MakeParam(morb, null);
                StatEff e=StatFactory.MakeStat(morbintime, target);  
                if (target.immunities.contains(e.getefftype())||target.immunities.contains(e.getimmunityname()))
                {
                    System.out.println(husk.Cname+"'s "+e.geteffname()+" could not be applied to Ultron due to an immunity.");
                }
                else if (husk.CheckFor("Undermine", false)==true&&!(husk.ignores.contains("Undermine")))
                { 
                    System.out.println(husk.Cname+"'s "+e.geteffname()+" could not be applied to Ultron due to a conflicting status effect.");
                }
                else if (e.getimmunityname().equals("Speed"))
                {
                    System.out.println(husk.Cname+"'s "+e.geteffname()+" could not be applied to Ultron due to a duplicate status effect.");
                }
                else if (target.dead==false)
                {
                    boolean apple=e.CheckStacking(target, e, e.stackable); 
                    if (apple==true)
                    target.add(e);
                    else
                    System.out.println(husk.Cname+"'s "+e.geteffname()+" could not be applied to Ultron due to a duplicate status effect.");
                }
            }
        }
    }
    public static void Crushbot (Character bot, boolean start)
    {
        if (start==true) //onsummon
        {
            CoinFlip.RobotImmunities(bot, true); bot.immunities.add("Persuaded");
        }
        else if (!(bot.binaries.contains("Stunned"))) //onturn
        {
            System.out.println("\nSEARCHING...CRUSHBOT TARGET ACQUIRED");
            ArrayList<Character> low=Battle.ChooseTarget(bot, "enemy", "lowest");
            Character law=low.get(0);
            Target enron= new Target(100, 5, 1);
            boolean yes=CoinFlip.Flip(100+bot.Cchance);
            if (yes==true)
            StatEff.CheckApply(bot, law, enron);
            else
            StatEff.applyfail(bot, enron, "chance");
        }
    }
    public static void NickLMD (Summon lmd) //onsummon
    {
        CoinFlip.RobotImmunities(lmd, true);
        if (lmd.passivefriend[0]!=null&&lmd.passivefriend[0].dead==false)
        {
            StatEff prot= new ProtectE (500, 616);
            prot.PrepareProtect(lmd, lmd.passivefriend[0]);
            StatEff.CheckApply(lmd, lmd, prot);
        }
    }
}