package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 20/8/22
 * Filename: StaticPassive
 * Purpose: Split passives into two files because of its length; this is for passives that trigger only once.
 */
public class StaticPassive 
{
    public static void DraxOG (Character drax) //static passive
    {
        Card_CoinFlip.IgnoreTargeting(drax, true);
        drax.immunities.add("Buffs");
        drax.immunities.add("Persuaded");
        drax.ignores.add("Invisible");
        if (drax.team1==true)
        {
            System.out.println ("\nPlayer 1, choose Drax's Obsession.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose Drax's Obsession.");
        }
        Character[] foes=Battle.TargetFilter(drax, "enemy", "single");
        Obsession obsess= new Obsession();  
        foes[0].add(foes[0], obsess);
        drax.passivefriend[0]=foes[0];
        drax.passivecount=1;
        drax.ignores.remove("Invisible");
    }
    public static void FurySr (Character fury, int hp)
    {
        if (hp<=90&&fury.passivecount==0&&!(fury.binaries.contains("Stunned")))
        {
            Summon lmd= new Summon(1);
            fury.passivecount=1;
            Battle.SummonSomeone(fury, lmd);            
        }
    }
    public static void Bucky (Character barnes)
    {
        barnes.ignores.add("Defence");
    }
    public static void Falcon (Character falcon) 
    {
        if (falcon.team1==true)
        {
            System.out.println ("\nPlayer 1, choose a character for Redwing to protect. Type the number in front of their name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose a character for Redwing to protect. Type the number in front of their name.");
        }
        Redwing red= new Redwing(); 
        if (falcon.team1==true)
        {
            Character[] friends=Battle.team1;
            int index=Card_Selection.ChooseTargetFriend (friends);
            friends[index].add(friends[index], red);
        }
        else 
        { 
            Character[] friends=Battle.team2;
            int index=Card_Selection.ChooseTargetFriend (friends);
            friends[index].add(friends[index], red); 
        }
    }
    public static void WM (Character machine, int turns)
    {
        if (turns==0)
        {
            System.out.println ("\nHeat Signature Detection activated.");
            Card_CoinFlip.AddInescapable (machine, true);
            if (machine.team1==true)
            {
                System.out.println ("Player 1, choose an enemy to target.");
            }
            else
            {
                System.out.println ("Player 2, choose an enemy to target.");
            }
            Character[] foes=Battle.TargetFilter(machine, "enemy", "single");
            WMTarget bay= new WMTarget();
            foes[0].add(foes[0], bay);
            machine.passivefriend[0]=foes[0];
            machine.passivecount=bay.hashcode;
            Card_CoinFlip.AddInescapable (machine, false);
        }
    }
}