package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class.
 */
import java.util.ArrayList;
public class ActivePassive 
{
    public static void Venom (Character macdonald) //called by hero.onkill
    {
        boolean yes=CoinFlip.Flip(500+macdonald.Cchance);
        Focus add= new Focus (500, 1);
        if (yes==true)
        StatEff.CheckApply(macdonald, macdonald, add);
        else
        StatEff.applyfail(macdonald, add, "chance");
        add.duration+=1; //to prevent it from instantly expiring when the current turn ends
    }
    public static void OGVenom (Character eddie, Character attacked, Character attacker) //called by hero.onallyattacked
    {
        if (!(attacker.ignores.contains("Counter")))
        {
            if (attacked==eddie.passivefriend[0])
            {
                int dmg=35;
                System.out.println ("\nThis one is under our protection!");
                Damage_Stuff.CheckBlind(eddie);
                if ((!(eddie.binaries.contains("Missed"))||eddie.immunities.contains("Missed")))
                {
                    Damage_Stuff.CheckEvade(eddie, attacker);
                    if (!(eddie.binaries.contains("Missed")))
                    {
                        dmg=Damage_Stuff.DamageFormula(eddie, attacker, dmg);
                        dmg=Damage_Stuff.CheckGuard(eddie, attacker, dmg);
                        attacker.TakeDamage(attacker, eddie, dmg, false);                         
                    }
                }
            }
        }
        if (eddie.binaries.contains("Missed")) //if his counterattack was evaded before; needed since miss is normally only cleared after using an ab
        eddie.binaries.remove("Missed");
    }
    public static void Wolvie (Character wolvie, boolean regen)
    {
        if (regen==true&&!(wolvie.binaries.contains("Stunned"))) //called by hero.onattacked
        {
            boolean yes=CoinFlip.Flip(500+wolvie.Cchance);
            Regen volkya= new Regen (500, 15, 1);
            if (yes==true)
            StatEff.CheckApply(wolvie, wolvie, volkya);
            else
            StatEff.applyfail(wolvie, volkya, "chance");
        }
        else //called by hero.tookdamage
        {
            if (wolvie.passivecount==0&&wolvie.dmgtaken>=180)
            {
                wolvie.passivecount=1;
                System.out.println("\nWolverine has gone berserk!");
                ArrayList<StatEff> removal= new ArrayList<StatEff>();
                for (StatEff e: wolvie.effects)
                {
                    if (!(e.getefftype().equals("Secret"))&&!(e.getimmunityname().equals("Regen")))
                    removal.add(e);
                }
                for (StatEff e: removal)
                {
                    wolvie.remove(wolvie, e.hashcode, "normal");
                }
                wolvie.ADR+=15;
                wolvie.immunities.add("Persuaded"); wolvie.immunities.add("Control"); CoinFlip.IgnoreTargeting(wolvie, true);
                boolean yes=CoinFlip.Flip(500+wolvie.Cchance);
                StatEff d= new IntensifyE(500, 15, 616);
                if (yes==true)
                StatEff.CheckApply(wolvie, wolvie, d);
                else
                StatEff.applyfail(wolvie, d, "chance");
                yes=CoinFlip.Flip(500+wolvie.Cchance);
                StatEff f= new FocusE(500, 616);
                if (yes==true)
                StatEff.CheckApply(wolvie, wolvie, f);
                else
                StatEff.applyfail(wolvie, f, "chance");
                BasicAb slash= new BasicAb ("X-Slash", "random", "enemy", 35); 
                String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
                BasicAb punch =new BasicAb ("Primal Punch", "random", "enemy", 45); 
                wolvie.abilities[0]=slash;
                wolvie.abilities[1]=punch;
            }
        }
    }
    public static void X23 (Character laura, Character victim, boolean crit)
    {
        if (crit==false) //hero.attack and hero.onattack
        {
            if (laura.passivecount==0&&victim.HP<90) //check before attacking
            {
                laura.passivecount=1; 
                laura.CC+=50;
            }
            else if (laura.passivecount==1) //undo after attacking
            {
                laura.CC-=50;
                laura.passivecount=0;
            }
        }
        else //check regen on hero.oncrit
        {
            boolean yes=CoinFlip.Flip(50+laura.Cchance);
            Regen RV= new Regen (500, 15, 1);
            if (yes==true)
            StatEff.CheckApply(laura, laura, RV);
            else
            StatEff.applyfail(laura, RV, "chance");
        }
    }
    public static boolean DraxOG (Character drax, boolean attack, Character victim, String dmgtype) //called by onattack and turnend
    {
        if (drax==victim) //undying rage was possibly triggered by drax taking lethaldmg
        {
            if (!(drax.binaries.contains("Stunned"))&&dmgtype.equalsIgnoreCase("dot")) 
            { 
                if (!(drax.binaries.contains("Death"))) //no need to trigger message for each tick of dot, just the first
                {
                    System.out.println("\nDrax's rage is undying!");
                }
                drax.binaries.add("Death"); 
                return false; //whether he dies or not
            } 
            else
            return true;
        }
        else if (attack==false&&drax.binaries.contains("Death")) //turn end after undying rage was triggered
        {
            int deaths=0;
            for (String b: drax.binaries)
            {
                if (b.equals("Death")) 
                {
                    ++deaths;
                }
            }
            for (int i=0; i<deaths; i++) //in case his passive is triggered more than once; this ensures drax won't die after being resurrected
            {
                drax.binaries.remove("Death");
            }
            drax.onDeath(drax, null, "DoT");
        }
        else if (attack==true) //add one more obsession to the target
        {
            if (victim==drax.passivefriend[0]&&drax.passivecount<3&&(!(drax.binaries.contains("Missed"))))
            {
                if (victim.CheckFor(victim, "Obsession", false)==true)
                {
                    boolean yes=CoinFlip.Flip(500+drax.Cchance);
                    Obsession obs= new Obsession ();
                    if (yes==true)
                    {
                        drax.passivefriend[0].add(drax.passivefriend[0], obs);
                        drax.passivecount++;
                    }
                    else
                    StatEff.applyfail(drax, obs, "chance");
                }
            }
        }
        return true;
    }
    public static void FuryJr (Character marcus, boolean selfturn, boolean allyturn, boolean summoned, boolean activate)
    {
        if (activate==true) //called by fury's kill mode ability
        {
            marcus.passivecount=1; marcus.Cchance+=50; marcus.BD+=15;
            System.out.println ("Kill Mode enabled.");
        }
        else if (selfturn==true) //called onturn
        {
            if (marcus.passivecount==1) //killmode is active
            {
                System.out.println("\nDeactivate Kill Mode (type the number, not the word)?");
                System.out.println("1. No"); System.out.println ("2. Yes");
                int choice=16;
                do
                {
                    choice=Damage_Stuff.GetInput();
                }
                while (choice!=1&&choice!=2);
                if (choice==2)
                {
                    marcus.passivecount=0; marcus.Cchance-=50; marcus.BD-=15;
                    System.out.println ("Kill Mode disabled.");
                }
                else
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
        }
        else if (allyturn==true&&marcus.passivecount==1&&summoned==false&&marcus.dead==false) //called on ally turn
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
    public static boolean StarLord (Character quill)
    {
        if (!(quill.binaries.contains("Stunned"))) //called onturn
        {
            if (quill.turn!=0&&quill.turn%2==0) //even numbers only; every other turn
            {
                Confidence heal= new Confidence (500, 15);
                Character[] targets= new Character[6];
                targets=Battle.GetTeam(quill.team1);
                System.out.println ("\nDance break!");
                for (Character me: targets)
                {
                    if (me!=null)
                    {
                        heal.Use(quill, me, 616);
                    }
                }
            }
            return false; //so his turn counter does not increment twice
        }
        return true;
    }
    public static void CaptainA (Character cap)
    {
        if (!(cap.binaries.contains("Shattered"))&&!(cap.binaries.contains("Stunned"))) //onturn and fight start
        {
            cap.Shielded(cap, 20);
        }
    }
    public static void IM (Character tony, StatEff buff) //called by hero.remove
    {
        Empower emp= new Empower (buff.power, buff.duration, tony.Cname, 4); 
        tony.add(tony, emp);
    }
    public static void Gamora (Character gam, StatEff buff, boolean add) //add is whether the buff is being added or removed; called by hero.remove and hero.add
    {
        if (add==true)
        {
            gam.Cchance+=50;
            gam.ignores.add("Protect");
            gam.immunities.add("Steal");
        }
        else
        {
            gam.Cchance-=50; 
            gam.ignores.remove("Protect");
            gam.immunities.remove("Steal");
        }
    }
    public static void MoonKnight(Character knight, Character attacked, Character attacker) //called by onallyattacked
    { 
        if (!(attacker.ignores.contains("Counter")))
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&eff.getProtector().equals(knight))
                {
                    int dmg=55;
                    System.out.println ("\nThe Lunar Protector strikes back!");
                    Damage_Stuff.CheckBlind(knight);
                    if ((!(knight.binaries.contains("Missed"))||knight.immunities.contains("Missed")))
                    {
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
        if (knight.binaries.contains("Missed")) //if his counterattack was evaded before; needed since miss is normally only cleared after using an ab
        knight.binaries.remove("Missed");
    }    
}