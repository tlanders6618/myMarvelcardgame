package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class.
 */
public class ActivePassive 
{
    public static void FuryJr (Character marcus, boolean act, boolean summoned)
    {
        if (act==true)
        {
            if (marcus.passivecount==1) //kill mode is already active
            {
                marcus.passivecount=0; marcus.Cchance-=50; marcus.PBD-=30;
                System.out.println ("Kill Mode disabled.");
            }
            else
            {
                marcus.passivecount=1; marcus.Cchance+=50; marcus.PBD+=30;
                System.out.println ("Kill Mode enabled.");
            }
        }
        if (marcus.passivecount==1&&summoned==false)
        {
            marcus.HP-=15;
            System.out.println ("\n"+marcus.Cname+" lost 15 health");
            if (marcus.HP<=0)
            {
                marcus.HP=0;
                marcus.onLethalDamage(marcus, null, "other");
            }
        }
    }
    public static void StarLord (Character quill)
    {
        ++quill.turn;
        if (quill.turn%2==0) //even numbers only
        {
            Confidence heal= new Confidence (200, 10);
            Character[] targets= new Character[6];
            targets=Battle.GetTeammates(quill, quill.team1);
            System.out.println ("\nDance break!");
            for (Character me: targets)
            {
                if (me!=null)
                {
                    heal.Use(quill, me, 616);
                }
            }
            heal.Use(quill, quill, 616);
        }
    }
    public static void CaptainA (Character cap)
    {
        if (cap.CheckFor(cap, "Shatter")==false&&cap.CheckFor(cap, "Stun")==false)
        {
            cap.Shielded(cap, 20);
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
        if (!(attacker.ignores.contains("Counter"))&&!(knight.binaries.contains("Stunned"))&&attacked.hash!=knight.hash)
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&eff.getProtector().equals(knight))
                {
                    int dmg=45;
                    boolean nomiss=true;
                    nomiss=Damage_Stuff.CheckBlind(knight);
                    if (nomiss==true)
                    {
                        System.out.println ("\nThe Lunar Protector strikes back!");
                        boolean evade=Damage_Stuff.CheckEvade(knight, attacker, nomiss);
                        if (evade==false)
                        {
                            dmg=Damage_Stuff.DamageFormula(knight, attacker, dmg);
                            dmg=Damage_Stuff.CheckGuard(knight, attacker, dmg);
                            attacker.TakeDamage(attacker, knight, dmg, false);                         
                        }
                    }
                    break;
                }
            }
        }
    }    
}