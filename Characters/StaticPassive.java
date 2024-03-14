package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 20/8/22
 * Filename: StaticPassive
 * Purpose: Split passives into two files because of its length; this is for passives that trigger only once per fight.
 */
import java.util.ArrayList;
public class StaticPassive 
{
    public static void Symbiote (Character venom, int vuln, boolean start) //efficient since they all have the same passive; fightstart and add/remove
    {
        if (start==true)
        venom.ignores.add("Evade");
        else
        venom.DV+=vuln;
    }
    public static int MODOC (Character george, Character target, boolean shield, boolean start, int dmg)
    {
        if (start==true) //onfightstart
        {
            CoinFlip.IgnoreTargeting(george, true); george.SHLD=100; george.passivecount=1; george.immunities.add("Debuffs");
        }
        else if (shield==false) //apply debuffs when attacking; called by his only attack ability's activatep
        {
            boolean stop=false;
            for (int i=0; i<5; i++)
            {
                if (stop==true) //stop checking for other kinds of abilities
                break;
                for (Ability a: target.abilities)
                {
                    if (a!=null&&!(a instanceof OtherAb))
                    {
                        switch (i)
                        {
                            case 0:
                            if (a instanceof DefAb)
                            {
                                String[] choochoo={"Shatter", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                george.activeability.AddTempString(nuclear); stop=true;
                            }
                            break;
                            case 1:
                            if (a instanceof HealAb)
                            {
                                String[] choochoo={"Afflicted", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                george.activeability.AddTempString(nuclear); stop=true;
                            }
                            break;
                            case 2:
                            if (a instanceof DebuffAb)
                            {
                                String[] choochoo={"Neutralise", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                george.activeability.AddTempString(nuclear); stop=true;
                            }
                            break;
                            case 3:
                            if (a instanceof BuffAb)
                            {
                                String[] choochoo={"Undermine", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                george.activeability.AddTempString(nuclear); stop=true;
                            }
                            break;
                            case 4: 
                            if (a instanceof AttackAb)
                            {
                                String[] choochoo={"Disarm", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                george.activeability.AddTempString(nuclear); stop=true;
                            }
                            break;
                        }
                        if (stop==true) //stop going through other abilities
                        break;
                    }
                }
            }
        }
        else if (george.passivecount==1) //shield is true; called at start of takedamage; passive only applies until shield is broken
        {
            if (!(target.ignores.contains("Shield"))&&!(target.ignores.contains("Defence"))) 
            {
                if (dmg>=100)
                {
                    System.out.println("You fool! You cannot penetrate MODOK's superior defences!");
                    dmg-=100;
                    if (dmg<0)
                    dmg=0;
                }
                if (george.SHLD-dmg<=0)
                {
                    george.immunities.remove("Debuffs"); george.passivecount=0;
                }
                return dmg;
            }
        }
        return dmg;
    }
    public static void Binary (Character binary, boolean in) //when transforming into binary; in is whether transform is into binary or out of binary
    {
        if (in==true)
        {
            binary.passivecount=10;
            System.out.println(binary.Cname+" gained 10 Energy.");
            for (StatEff e: binary.effects) //update displayed energy count
            {
                if (e instanceof Tracker)
                {
                    e.Attacked(binary, null, 616);
                }
            }
            binary.immunities.add("Buffs"); binary.immunities.add("Debuffs"); binary.immunities.add("Heal"); binary.immunities.add("Defence");
            ArrayList<StatEff> r= new ArrayList<StatEff>();
            for (StatEff e: binary.effects)
            {
                if (!(e instanceof Tracker)&&!(e.getefftype().equals("Other")))
                r.add(e);
            }
            for (StatEff e: r)
            {
                binary.remove(binary, e.hashcode, "normal");
            }
        }
        else //leaving binary state
        {
            binary.immunities.remove("Buffs"); binary.immunities.remove("Debuffs"); binary.immunities.remove("Heal"); binary.immunities.remove("Defence");
        }
    }
    public static void CM (Character carol) //fightstart
    {
        carol.immunities.add("Poison");
        Tracker NRG= new Tracker("Energy: ");
        carol.effects.add(NRG);
        NRG.onApply(carol);
    }
    public static void OGVenom (Character eddie) //choose ally to watch over at fight start
    {
        if (eddie.team1==true)
        {
            System.out.println ("\nPlayer 1, choose a character for Venom (Eddie Brock) to watch over. Type the number in front of their name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose a character for Venom (Eddie Brock) to watch over. Type the number in front of their name.");
        }
        Character[] friends=null;
        friends=Battle.GetTeammates(eddie);
        ResistanceE res= new ResistanceE (500, 10, 616);
        int index=Card_Selection.ChooseTargetFriend (friends);
        StatEff.CheckApply(eddie, friends[index], res);
        eddie.passivecount=res.hashcode;
        eddie.passivefriend[0]=friends[index];
        friends[index].add(friends[index], new Tracker ("Watched by Venom (Eddie Brock)"));
    }
    public static void WolvieTracker (Character wolvie) //initialise tracker on fightstart
    {
        Tracker frenzy= new Tracker("Damage Taken: ");
        wolvie.effects.add(frenzy);
        frenzy.onApply(wolvie);
    }
    public static void Drax (Character arthur, Character target, boolean knife) //in here bc I assume this'll be used once per fight due to its strict requirements
    {
        if (knife==true) //called by twin blades
        {
            arthur.passivecount=1;
        }
        else if (target==null) //called by hero.onattack; undoes passive after attack finished; onturnend would cause bugs by applying bd to all aoe instead of those with high hp
        {
            if (arthur.passivecount==-2)
            {
                arthur.passivecount=0;
                arthur.BD-=30;
                StatEff bl=null;
                for (StatEff e: arthur.effects)
                {
                    if (e.geteffname().equals("Twin Blades active"))
                    {
                        bl=e; break;
                    }
                }
                arthur.remove(arthur, bl.hashcode, "silent");
            }
            else if (arthur.passivecount==-1)
            {
                arthur.passivecount=0;
                arthur.BD-=15;
            }
        }
        else //activate passive; called by both hero.attack and knife slash since debuffmod occurs before attacking
        {
            int HP=target.maxHP;
            double tenth=HP*0.75; //75% of target maxhp
            tenth=5*(int)Math.ceil(tenth/5); //rounded up to nearest multiple of 5
            if (target.HP>=tenth) //then passive triggers
            {
                if (arthur.passivecount==1) //triggers twice
                {
                    arthur.passivecount=-2;
                    arthur.BD+=30;
                }
                else if (arthur.passivecount==0)
                {
                    arthur.passivecount=-1;
                    arthur.BD+=15;
                }
            }
        }
    }
    public static void DraxOG (Character drax) //choosing obsession at fight start
    {
        CoinFlip.IgnoreTargeting(drax, true);
        drax.immunities.add("Buffs");
        drax.immunities.add("Persuaded");
        drax.ignores.add("Invisible"); //so it doesn't interfere with choosing his obsession
        if (drax.team1==true)
        {
            System.out.println ("\nPlayer 1, choose Drax's Obsession.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose Drax's Obsession.");
        }
        Character[] foes=Battle.TargetFilter(drax, "enemy", "single");
        Obsession obsess= new Obsession();  
        foes[0].add(foes[0], obsess);
        drax.passivefriend[0]=foes[0];
        drax.passivecount=1;
        drax.ignores.remove("Invisible");
    }
    public static void FurySr (Character fury, int hp) //after an hpchange
    {
        if (fury.passivecount==0&&hp<=90&&!(fury.binaries.contains("Stunned")))
        {
            Summon lmd= new Summon(1);
            lmd.team1=fury.team1;
            fury.passivecount=1;
            Battle.SummonSomeone(fury, lmd);            
        }
    }
    public static void Bucky (Character barnes) //triggered at fight start
    {
        barnes.ignores.add("Defence");
    }
    public static void Falcon (Character falcon) //triggered at fight start
    {
        if (falcon.team1==true)
        {
            System.out.println ("\nPlayer 1, choose a character for Redwing to protect. Type the number in front of their name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose a character for Redwing to protect. Type the number in front of their name.");
        }
        Redwing red= new Redwing(); 
        Character[] friends=null;
        if (falcon.team1==true)
        {
            friends=Battle.team1;
        }
        else 
        {
            friends=Battle.team2;
        }
        int index=Card_Selection.ChooseTargetFriend (friends);
        friends[index].add(friends[index], red);
    }
    public static void WM (Character machine) //triggered onturn
    {
        if (!(machine.binaries.contains("Stunned"))&&machine.turn==0)
        {
            System.out.println ("\nHeat Signature Detection activated.");
            CoinFlip.AddInescapable (machine, true);
            if (machine.team1==true)
            {
                System.out.println ("Player 1, choose an enemy to target.");
            }
            else
            {
                System.out.println ("Player 2, choose an enemy to target.");
            }
            Character[] foes=Battle.TargetFilter(machine, "enemy", "single");
            WMTarget bay= new WMTarget();
            StatEff.CheckApply(machine, foes[0], bay);
            machine.passivefriend[0]=foes[0];
            machine.passivecount=bay.hashcode;
            CoinFlip.AddInescapable (machine, false);
        }
    }
}