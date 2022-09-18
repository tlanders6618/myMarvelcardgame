package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: AfterAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
public abstract class AfterAbility extends SpecialAbility //applied after a hero attacks
{
    public AfterAbility()
    {
    }
}
class AddIgnore extends AfterAbility
{
    String condition; String ignorethis; int threshold; //health threshold, needed for lads like Corvus
    public AddIgnore (String igthis, String condie, int thresh)
    {
        condition=condie; ignorethis=igthis; threshold=thresh;
    }
    @Override
    public int Use (Character user, Character target)
    {
        boolean okay=CheckIgCondition(condition, target, threshold);
        if (okay==true)
        {
            user.ignores.add (ignorethis);
        }
        return 0;
    }
    @Override 
    public void Use (Character user, Character target, int ignoreme)
    {
        user.ignores.remove (ignorethis);
    }
    public static boolean CheckIgCondition (String condition, Character target, int hp)
    { 
        switch (condition) 
        {
            case "none": return true; //will add more as needed
            default: return false;
        }
    }
}
class Confidence extends AfterAbility
{
    int amount; int chance; Ricochet r= new Ricochet(1);
    public Confidence(int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character mj, int ignore) 
    {
        if (caller.CheckFor(caller, "Heal Disable")==false) //confidence is a heal ability
        {
            boolean success=Card_CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&mj.dead==false&&!(mj.binaries.contains("Banished")))
            {
                mj.Healed(mj, amount);
                mj.Shielded(mj, amount);
            }
        }
    }
}
class Ricochet extends AfterAbility
{
    int chance;
    public Ricochet(int cchance)
    {
        chance=cchance;
    }
    @Override
    public void Use (Character user, Character ignorethis, int dmg) //user is the one doing the damage
    { 
        boolean success=Card_CoinFlip.Flip(chance+user.Cchance);
        if (success==true&&dmg>5)
        {
            Ability.DoRicochetDmg (dmg, user, false);
        }
    }
}