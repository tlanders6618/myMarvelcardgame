package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * <p>Date of creation: 2/3/25
 * <p>Purpose: To list all optional attributes an ability can have. Traits are a kind of boolean, distinct from special abilities.
 */
public enum Trait
{
    //Traits are self explanatory
    CHANNELLED, SINGLEUSE, IGNORE, TOGETHER, UNBOUND, ELUSIVE, CONTROL, 
    /** The ability causes health loss instead of doing damage */
    LOSE, 
    /** The ability causes max health reduction instead of doing damage */
    MAX, 
    MULTIUSE1, MULTIUSE2, MULTIUSE3, SEALED1, 
    //used for checkUse
    RESTRICTPENANCE, RESTRICTRULK;
}
