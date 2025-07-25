package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 15/8/22
 * <p>Purpose: To make attack abilities.
 */
class AttackAb extends Ability
{
    public AttackAb (String aname, String atarget, String afriendly, int dmg, int cooldown)
    {
        super(aname, atarget, afriendly, dmg, cooldown, 0, null);
    }
    public AttackAb (String aname, String atarget, String afriendly, int dmg, int cooldown, Trait[] c)
    {
        super(aname, atarget, afriendly, dmg, cooldown, 0, c);
    }
    public AttackAb (String aname, String atarget, String afriendly, int dmg, int cooldown, int mult)
    {
        super(aname, atarget, afriendly, dmg, cooldown, mult, null);
    }
    public AttackAb (String aname, String atarget, String afriendly, int dmg, int cooldown, int mult, Trait[] c)
    {
        super(aname, atarget, afriendly, dmg, cooldown, mult, c);
    }
    @Override
    public void CheckIgnore(Character user, boolean add) 
    {
        //does nothing, since disarm doesn't need to be ignored with binaries since it doesn't affect stateff application
    }
    @Override
    public boolean CheckUse (Character user)
    {
        if ((user.CheckFor("Disarm", false)==true&&this.getIgnore()==false))
        {
            return false;
        }
        int res=this.getRestriction();
        if (res!=616)
        {
            switch (res)
            {
                case 77: //penance's ab #4
                if (user.passivecount<3)
                return false;
            }
        }
        return super.CheckUse(user);
    }
}