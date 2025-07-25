package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make Other abilities.
 */
class OtherAb extends Ability
{
    public OtherAb (String name, String targ, String friend, int cod)
    {
        super(name, targ, friend, 0, cod, 0, null);
    }
    public OtherAb (String name, String targ, String friend, int cod, Trait[] traits)
    {
        super(name, targ, friend, 0, cod, 0, traits);
    }
    @Override
    /**
     * {@inheritDoc}
     */
    public void PrintDesc ()
    {
        System.out.print("Other ability. "); super.PrintDesc();
    }
    @Override
    /**
     * {@inheritDoc}
     */
    public void CheckIgnore(Character user, boolean add) //does nothing, since otherabs have no corresponding disable debuff to ignore
    {
    }
    @Override
    /**
     * {@inheritDoc}
     */
    public boolean CheckUse (Character user)
    {
        int res=this.getRestriction();
        if (res!=616) //if the ab has a restriction
        {
            switch (res) //check specific condition
            {
                case 78: //rulk's aura ab
                if (CoinFlip.GetStatCount(user, "Burn", "Other")<3)
                return false;
            }
        }
        //even if restriction is not active, must check suppression, cooldowns, etc
        return super.CheckUse(user);
    }
}