package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 15/8/22
 * Filename: BasicAb
 * Purpose: To make basic attacks.
 */
import java.util.ArrayList;
class BasicAb extends AttackAb
{
    public BasicAb (String aname, String atype, String afriendly, int dmg)
    {
        super(aname, atype, afriendly, dmg, 0);
    }
    public BasicAb (String aname, String atype, String afriendly, int dmg, int mult)
    {
        super(aname, atype, afriendly, dmg, 0, mult);
    }
    @Override
    public void PrintDesc (boolean ignore)
    {
        System.out.print("Basic attack. "); 
        super.PrintDesc(true); //calls attackab's printdesc, not ability's printdesc
    }
    @Override 
    public void SetChannelled (Character hero, Ability ab, ArrayList<Character> targets)
    {
    }
    @Override 
    public void InterruptChannelled (Character hero, Ability ab)
    {
    }
    @Override 
    public ArrayList<StatEff> ActivateChannelled(Character t, Ability a)
    {
        return null;
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if (user.CheckFor("Disarm", false)==true&&this.ignore==false)
        {
            return false;
        }
        else if (this.dcd>0)
        {
            return false;
        }
        return true;
    }
}