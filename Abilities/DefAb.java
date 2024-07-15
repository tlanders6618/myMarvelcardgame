package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: DefAb
 * Purpose: To make Defence abilities.
 */
import java.util.ArrayList;
class DefAb extends Ability 
{
    public DefAb (String aname, String atarget, String afriendly, int cod)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atarget;
        this.cd=cod;
        if (afriendly.equalsIgnoreCase("enemy")||afriendly.equalsIgnoreCase("both")||afriendly.equalsIgnoreCase("either")) //anything other than ally or self
        {
            this.attack=true;
        }
        if (atarget.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
    }
    @Override
    public void PrintDesc (boolean ignore)
    {
        System.out.print("Defence ability. "); super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //does nothing, since def abs have no corresponding disable debuff
    {
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if (user.CheckFor("Suppression", false)==true)
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