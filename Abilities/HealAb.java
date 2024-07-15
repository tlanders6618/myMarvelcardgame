package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: HealAb
 * Purpose: To make Heal abilities.
 */
import java.util.ArrayList;
class HealAb extends Ability
{
    public HealAb (String aname, String atarget, String afriendly, int cod)
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
        System.out.print("Heal ability. "); 
        if (this.ignore==true)
        System.out.print("Ignores Afflicted. ");
        super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //enusures ab ignores its disable debuff both when checking useability, and when applying stateffs
    {
        if (add==true)
        user.ignores.add("Afflicted");
        else
        user.ignores.remove("Afflicted");
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if ((user.CheckFor("Afflicted", false)==true&&this.ignore==false)||user.CheckFor("Suppression", false)==true)
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