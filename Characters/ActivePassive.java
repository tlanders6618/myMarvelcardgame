package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class.
 */
public class ActivePassive 
{
    public static void DraxOG (Character drax, boolean attack, Character victim) //occurs on turnend
    {
        if (attack==false&&drax.binaries.contains("Death")) //undying rage was triggered
        {
            int deaths=0;
            for (String b: drax.binaries)
            {
                if (b.equals("Death")) 
                {
                    ++deaths;
                }
            }
            for (int i=0; i<deaths; i++) //in case his passive is triggered more than once, this ensures drax won't die in the event of a resurrection
            {
                drax.binaries.remove("Death");
            }
            drax.onDeath(drax, null, "DoT");
        }
        else if (attack==true) //add one more obsession to the target
        {
            if (victim==drax.passivefriend[0]&&drax.passivecount<3&&!(drax.binaries.contains("Missed")))
            {
                if (victim.CheckFor(victim, "Obsession")==true)
                {
                    Obsession obs= new Obsession ();
                    drax.passivefriend[0].add(drax.passivefriend[0], obs);
                    drax.passivecount++;
                }
            }
        }
    }
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
            Confidence heal= new Confidence (500, 15);
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
        if (cap.CheckFor(cap, "Shatter")==false&&!(cap.binaries.contains("Stunned")))
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
                gam.ignores.add("Protect");
            }
            else
            {
                gam.Cchance-=50; 
                gam.ignores.remove("Protect");
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
                    Damage_Stuff.CheckBlind(knight);
                    if (!(knight.binaries.contains("Missed")))
                    {
                        System.out.println ("\nThe Lunar Protector strikes back!");
                        Damage_Stuff.CheckEvade(knight, attacker);
                        if (!(knight.binaries.contains("Missed")))
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