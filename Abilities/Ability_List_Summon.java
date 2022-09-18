package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 7/8/22
 * Filename: Ability_List_Summon
 * Purpose: Lists all summon abilities.
 */
public class Ability_List_Summon
{
    static Ability[][] AbilityListS = new Ability[30][5]; //rows then column e.g. 1,0 means row 2 column 1. Visualise tic tac toe board
    //columns are for abilities rows are for characters
    public static Ability GetAb (int index, int counter)
    {
        return AbilityListS[index][counter];
    }
}