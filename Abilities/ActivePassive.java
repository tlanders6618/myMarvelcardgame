package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class.
 */
public class ActivePassive 
{
    public static void Wolvie (Character wolvie, boolean regen)
    {
        if (regen==true&&!(wolvie.binaries.contains("Stunned"))&&!(wolvie.binaries.contains("Afflicted"))) //gain regen after being attacked
        {
            boolean yes=CoinFlip.Flip(50+wolvie.Cchance);
            if (yes==true)
            {
                String[] heal= {"Regen", "500", "20", "1", "true"};
                String[][] healme=StatFactory.MakeParam(heal, null);
                StatEff add=StatFactory.MakeStat(healme);
                StatEff.CheckApply(wolvie, wolvie, add);
            }
        }
        else //check berserker trigger after taking dmg
        {
            if (wolvie.passivecount==0&&wolvie.dmgtaken>=180)
            {
                wolvie.passivecount=1;
                System.out.println("\nWolverine has gone berserk!");
                wolvie.ADR+=10;
                wolvie.immunities.add("Persuaded"); wolvie.immunities.add("Control"); 
                boolean yes=CoinFlip.Flip(500+wolvie.Cchance);
                if (yes==true)
                {
                    StatEff d= new IntensifyE(500, 15, 616);
                    StatEff.CheckApply(wolvie, wolvie, d);
                }
                yes=CoinFlip.Flip(500+wolvie.Cchance);
                if (yes==true)
                {
                    StatEff f= new FocusE(500, 616);
                    StatEff.CheckApply(wolvie, wolvie, f);
                }
            }
        }
    }
    public static void X23 (Character laura, Character victim, boolean crit)
    {
        if (crit==true) //X23 is attacking
        {
            if (laura.passivecount==0&&victim.HP<100)
            {
                laura.passivecount=1;
                laura.CC+=50;
                System.out.println("\nTEST X23 crit before attack: "+laura.CC);
            }
            else if (laura.passivecount==1)
            {
                laura.CC-=50;
                laura.passivecount=0;
                System.out.println("TEST X23 crit after attack: "+laura.CC+"\n");
            }
        }
        else //check regen
        {
            boolean yes=CoinFlip.Flip(50+laura.Cchance);
            if (yes==true&&!(laura.binaries.contains("Afflicted")))
            {
                String[] reg= {"Regen", "500", "25", "1", "true"};
                String[][] regen=StatFactory.MakeParam (reg, null);
                StatEff add=StatFactory.MakeStat(regen);
                StatEff.CheckApply(laura, laura, add);
            }
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
            if (victim==drax.passivefriend[0]&&drax.passivecount<3&&(!(drax.binaries.contains("Missed"))||drax.immunities.contains("Missed")))
            {
                if (victim.CheckFor(victim, "Obsession")==true)
                {
                    boolean yes=CoinFlip.Flip(500+drax.Cchance);
                    if (yes==true)
                    {
                        Obsession obs= new Obsession ();
                        drax.passivefriend[0].add(drax.passivefriend[0], obs);
                        drax.passivecount++;
                    }
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