package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_HashCode
 * Purpose: Gives objects unique identifier numbers.
 */
public class Card_HashCode
{
    static int counter=1;
    public static int RandomCode()
    {
        int code=counter; 
        ++counter; //to ensure every number is different
        return code;
    }
}