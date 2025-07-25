package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make Defence abilities.
 */
class DefAb extends Ability 
{
    /**
    * Constructor for all Defence abs. 
    * @param name The name of the ability.
    * @param targ How many targets the ability can have.
    * @param friend Who the ability targets.
    * @see Ability
    */
    public DefAb (String name, String targ, String friend, int cod)
    {
        super(name, targ, friend, 0, cod, 0, null);
    }
    public DefAb (String name, String targ, String friend, int cod, Trait[] traits)
    {
        super(name, targ, friend, 0, cod, 0, traits);
    }
    @Override
    public void PrintDesc ()
    {
        System.out.print("Defence ability. "); super.PrintDesc();
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //does nothing, since def abs have no corresponding disable debuff
    {
    }
    @Override
    public boolean CheckUse (Character user)
    {
        //def abs have no disable debuffs
        //restrictions to be added here if any def abs ever have them
        return super.CheckUse(user);
    }
}