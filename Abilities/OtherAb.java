package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: OtherAb
 * Purpose: To make Other abilities.
 */
import java.util.ArrayList;
class OtherAb extends Ability
{
    public OtherAb (String aname, String atarget, String afriendly, int cooldown)
    {
        this.oname=aname;
        this.friendly=afriendly;
        this.target=atarget;
        this.cd=cooldown;
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
        System.out.print("Other ability. "); super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //does nothing, since otherabs have no corresponding disable debuff to ignore
    {
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if (user.CheckFor("Suppression", false)==true||user.CheckFor("Persuaded", false)==true)
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
        if (this.restricted==true)
        {
            switch (this.restriction)
            {
                case 78: //rulk's aura ab
                if (CoinFlip.GetStatCount(user, "Burn", "Other")<3)
                return false;
            }
        }
        return true;
    }
}