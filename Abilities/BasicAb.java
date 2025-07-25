package myMarvelcardgamepack;

import java.util.ArrayList;
/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make basic attacks.
 */
class BasicAb extends AttackAb
{
    public BasicAb (String aname, String atype, String afriendly, int dmg)
    {
        super(aname, atype, afriendly, dmg, 0, 0, null);
    }
    public BasicAb (String aname, String atype, String afriendly, int dmg, Trait[] trait)
    {
        super(aname, atype, afriendly, dmg, 0, 0, trait);
    }
    public BasicAb (String aname, String atype, String afriendly, int dmg, int mult)
    {
        super(aname, atype, afriendly, dmg, 0, mult, null);
    }
    public BasicAb (String aname, String atype, String afriendly, int dmg, int mult, Trait[] trait)
    {
        super(aname, atype, afriendly, dmg, 0, mult, trait);
    }
    @Override 
    public void PrepChannelled (Character hero, ArrayList<Character> targets)
    {
    }
    @Override 
    public void InterruptChannelled (Character hero)
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
        if (user.CheckFor("Disarm", false)==true&&this.getIgnore()==false)
        {
            return false;
        }
        else if (this.getCD(true)>0) //sealed
        {
            return false;
        }
        //basic abs don't have restrictions, or else they'd be attack abs
        return true;
    }
}