package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: BuffAb
 * Purpose: To make buff abilities.
 */
import java.util.ArrayList;
class BuffAb extends Ability
{
    public BuffAb ()
    {
    }
    public BuffAb (String aname, String atarget, String afriendly, int cod)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atarget;
        this.cd=cod;
        if (afriendly.equalsIgnoreCase("enemy"))
        {
            this.attack=true;
        }
        if (atarget.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    @Override
    public boolean CheckUse (Character user, Ability ab)
    {
        if ((user.CheckFor(user, "Undermine", false)==true&&ab.ignore==false)||user.CheckFor(user, "Suppression", false)==true)
        {
            return false;
        }
        else if (singleuse==true&&used==true)
        {
            return false;
        }
        else if (usable==false)
        {
            return false;
        }
        else if (dcd>0) 
        {
            return false;
        }
        return true;
    }
}