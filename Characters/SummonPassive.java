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
    public static void NickLMD (Summon lmd)
    {
        CoinFlip.RobotImmunities(lmd, true);
        StatEff prot= new ProtectE (500, 616);
        prot.PrepareProtect(lmd, lmd.mysummoner);
        StatEff.CheckApply(lmd, lmd, prot);
    }
}