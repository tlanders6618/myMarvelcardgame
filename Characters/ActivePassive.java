package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class.
 */
public class ActivePassive 
{
    public static void CaptainA (Character cap)
    {
        if (cap.CheckFor(cap, "Shatter")==false&&!(cap.binaries.contains("Stunned")))
        {
            cap.Shielded(cap, 10);
        }
    }
    public static void Gamora (Character gam, StatEff buff, boolean inc)
    {
        if (buff.getimmunityname().equalsIgnoreCase("Damage Up"))
        {
            if (inc==true)
            {
                gam.Cchance+=50;
            }
            else
            {
                gam.Cchance-=50;
            }
        }
    }
    public static void MoonKnight(Character knight, Character attacked, Character attacker)
    { 
        if (!(attacker.ignores.contains("Counter"))&&knight.CheckFor(knight, "Stun")==false&&attacked.hash!=knight.hash)
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&eff.getProtector().equals(knight))
                {
                    int dmg=45;
                    boolean nomiss=true;
                    if (knight.CheckFor(knight, "Blind")==true)
                    {
                        nomiss=Card_CoinFlip.Flip(knight.accuracy);
                    }
                    if (nomiss==true)
                    {
                        dmg=Damage_Stuff.DamageFormula(knight, attacker, dmg);
                        System.out.println ("\nThe Lunar Protector strikes back!");
                        attacker.TakeDamage(attacker, knight, dmg, false);                         
                    }
                    break;
                }
            }
        }
    }    
}