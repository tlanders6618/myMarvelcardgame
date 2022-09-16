package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 20/8/22
 * Filename: StaticPassive
 * Purpose: Split Passives into two files because of its length; this is for passives that trigger only once.
 */
public class StaticPassive 
{
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
            Damage_Stuff.IgnoreTargeting(machine, true);
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
            machine.passivecount=bay.getcode();
            Damage_Stuff.IgnoreTargeting(machine, false);
        }
    }
}