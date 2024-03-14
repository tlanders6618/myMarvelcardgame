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
        StatEff prot= new ProtectE (500, 616);
        prot.PrepareProtect(lmd, lmd.mysummoner);
        StatEff.CheckApply(lmd, lmd, prot);
    }
}