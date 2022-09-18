package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_CoinFlip
 * Purpose: To determine whether an effect applies or fails to apply.
 */
public class Card_CoinFlip
{
    public static boolean Flip (int chance)
    {
        //applies to critical hits and status effect application
        boolean success=false;
        if (chance>=100)
        {
            if (chance>=500)
            {
                System.out.println ("Constructor must have failed to correctly set the chance because it's over 500."); 
            }
            success=true;
        }
        else if (chance==50)
        {
            int heads=(int)Math.round(Math.random()); //random number between 1 and 0, rounded to the nearest whole number, i.e. 1 or 0
            if (heads==0)
            {
                success=true;
            }
            else if (heads==1)
            {
                //tails you lose
            }
        }
        else if (chance<=0)
        {
            //still a failure
        }
        return success;
    }
}