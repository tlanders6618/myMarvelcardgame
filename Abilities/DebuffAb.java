package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make debuff abilities.
 */
class DebuffAb extends Ability
{
    public DebuffAb (String name, String targ, String friend, int cod)
    {
        super(name, targ, friend, 0, cod, 0, null);
    }
    public DebuffAb (String name, String targ, String friend, int cod, Trait[] trait)
    {
        super(name, targ, friend, 0, cod, 0, trait);
    }
    @Override
    public void PrintDesc ()
    {
        System.out.print("Debuff ability. "); 
        if (this.getIgnore()==true)
        System.out.print("Ignores Neutralise. ");
        super.PrintDesc(false);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) //ensures ab ignores its disable debuff both when checking useability, and when applying stateffs
    {
        if (add==true)
        user.ignores.add("Neutralise");
        else
        user.ignores.remove("Neutralise");
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if (user.CheckFor("Neutralise", false)==true&&this.getIgnore()==false)
        {
            return false;
        }
        //restrictions to be added here if any debuff abs ever have them
        //even if free of disable debuff, must check suppression, cooldowns, etc
        return super.CheckUse(user);
    }
}