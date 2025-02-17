package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: AttackAb
 * Purpose: To make attack abilities.
 */
import java.util.ArrayList;
class DebuffAb extends Ability
{
    public DebuffAb (String aname, String atarget, String afriendly, int cod)
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
        System.out.print("Debuff ability. "); 
        if (this.ignore==true)
        System.out.print("Ignores Neutralise. ");
        super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //enusures ab ignores its disable debuff both when checking useability, and when applying stateffs
    {
        if (add==true)
        user.ignores.add("Neutralise");
        else
        user.ignores.remove("Neutralise");
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if ((user.CheckFor("Neutralise", false)==true&&this.ignore==false)||user.CheckFor("Suppression", false)==true||user.CheckFor("Persuaded", false)==true)
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