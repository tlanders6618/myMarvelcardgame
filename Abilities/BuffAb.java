package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make buff abilities.
 */
class BuffAb extends Ability
{
    public BuffAb (String name, String targ, String friend, int cod)
    {
        super(name, targ, friend, 0, cod, 0, null);
    }
    public BuffAb (String name, String targ, String friend, int cod, Trait[] trait)
    {
        super(name, targ, friend, 0, cod, 0, trait);
    }
    @Override
    public void PrintDesc ()
    {
        System.out.print("Buff ability. ");
        if (this.getIgnore()==true)
        System.out.print("Ignores Undermine. ");
        super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //ensures ab ignores its disable debuff both when checking useability, and when applying stateffs
    {
        if (add==true)
        user.ignores.add("Undermine");
        else
        user.ignores.remove("Undermine");
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if ((user.CheckFor("Undermine", false)==true&&this.getIgnore()==false))
        {
            return false;
        }
        //restrictions to be added here if any heal abs ever have them
        //even if free of disable debuff, must check suppression, cooldowns, etc
        return super.CheckUse(user);
    }
}