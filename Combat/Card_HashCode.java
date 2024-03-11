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
    public static boolean CheckSkip (Character hero) //heroes can only skip their turn if they have no usable abilities
    {
        boolean usable=true; int use=0;
        for (Ability a: hero.abilities)
        {
            if (a!=null&&a.CheckUse(hero, a)==true)
            {
                if (a.friendly.equalsIgnoreCase("ally exclusive")||a.friendly.equalsIgnoreCase("both")) //requires an ally to target
                {
                    Character[] list=Battle.GetTeammates(hero);
                    for (Character c: list)
                    {
                        if (c!=null) //only considered usable if the hero still has allies they can use the ab on
                        {
                            ++use; break;
                        }
                    }
                }
                else //ability targets self or an enemy
                ++use;
            }
        }
        if (use>0)
        return false;
        else 
        return true;
    }
}