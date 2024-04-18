package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/7/22
 * Filename: ActivePassive
 * Purpose: Makes characters' passives since I now realise they're too different from abilities to be in the same class; for passives activated repeatedly in a fight.
 */
import java.util.ArrayList;
public class ActivePassive 
{
    public static void Sandy (Character baker, String o)
    {
        if (o.equals("ult")) //activatep; use sandstorm 
        {
            ArrayList<StatEff> opp= new ArrayList<StatEff>(); opp.addAll(baker.effects);
            for (StatEff e: opp)
            {
                if (!(e.getefftype().equals("Secret")))
                baker.remove(e.hashcode, "normal");
            }
            String[]blast={"Safeguard", "500", "616", "1", "true"}; String[][] loopy=StatFactory.MakeParam(blast, null); baker.activeability.AddTempString(loopy);
            Character[] friends=Battle.GetTeammates(baker);
            Character[] foes=Battle.GetTeam(CoinFlip.TeamFlip(baker.team1));
            for (Character c: friends)
            {
                if (c!=null)
                {
                    Blind k=new Blind(500, 1, baker); StatEff.CheckApply(baker, c, k);
                }
            }
            for (Character c: foes)
            {
                if (c!=null)
                {
                    Blind k=new Blind(500, 1, baker); StatEff.CheckApply(baker, c, k);
                }
            }
            baker.passivecount=4;
            Tracker clunt=new Tracker("Sand Storm active: "); baker.effects.add(clunt); clunt.onApply(baker);
        }
        else if (o.equals("turn")) //onturn and onallyturn; sandstorm dmg
        {
            if (baker.passivecount>0&&baker.dead==false&&!(baker.binaries.contains("Stunned")))
            {
                --baker.passivecount;
                Character[] friends=Battle.GetTeammates(baker);
                Character[] foes=Battle.GetTeam(CoinFlip.TeamFlip(baker.team1));
                for (Character chump: foes)
                {
                    if (chump!=null)
                    {
                        int damage=20;
                        damage-=chump.ADR;
                        if (damage<0)
                        damage=0;
                        System.out.println ("\n"+baker.Cname+" did "+damage+" damage to "+chump.Cname);
                        chump.TakeDamage(chump, damage, false);
                    }
                }
                for (Character chump: friends)
                {
                    if (chump!=null)
                    {
                        int damage=10;
                        damage-=chump.ADR;
                        if (damage<0)
                        damage=0;
                        System.out.println ("\n"+baker.Cname+" did "+damage+" damage to "+chump.Cname);
                        chump.TakeDamage(chump, damage, false);
                    }
                }
                StatEff hope=null;
                for (StatEff e: baker.effects)
                {
                    if (e instanceof Tracker&& e.geteffname().equals("Sand Storm active: "+(baker.passivecount+1)+" turns"))
                    {
                        if (baker.passivecount>0)
                        e.onApply(baker);
                        else
                        hope=e;
                        break;
                    }
                }
                if (hope!=null)
                {
                    baker.remove(hope.hashcode, "silent"); System.out.println(baker.Cname+"'s Sand Storm ended.");
                }
            }
        }
        else //if (o.equals("burn")) //add
        {
            ArrayList<StatEff> burns= new ArrayList<StatEff>();
            for (StatEff e: baker.effects)
            {
                if (e.getimmunityname().equals("Burn"))
                burns.add(e);
            }
            if (burns.size()>1)
            {
                System.out.print("\n");
                baker.remove(burns.get(0).hashcode, "normal"); baker.remove(burns.get(1).hashcode, "normal");
                StunE hope= new StunE(500, 1); StatEff.CheckApply(baker, baker, hope);
            }
        }
    }
    public static void Cain (Character marko, String time, int old)
    {
        if (time.equals("turn")&&marko.passivecount<5) //onturn
        {
            marko.passivecount++;
            System.out.println(marko.Cname+" gained 1 Momentum.");
            if (marko.passivecount==5)
            {
                System.out.println(marko.Cname+" is unstoppable!"); marko.immunities.add("Debuffs");
            }
            for (StatEff e: marko.effects) //update tracker to accurately show M since it otherwise only updates onturnend
            {
                if (e instanceof Tracker&&e.geteffname().equals("Momentum: "+(marko.passivecount-1)))
                {
                    e.onTurnEnd(marko); break;
                }
            }
        }
        else if (time.equals("change")) //hpchange
        {
            if (old>100&marko.HP<=100) //fallen below threshold; lost bonuses
            {
                marko.ADR-=10; marko.immunities.remove("Control");
                StatEff too=null;
                for (StatEff e: marko.effects)
                {
                    if (e.geteffname().equals("Cyttorak's Blessing active")&&e instanceof Tracker)
                    {
                        too=e; break;
                    }
                }
                marko.effects.remove(too);
                Tracker salt= new Tracker("Cyttorak's Blessing lost"); marko.effects.add(salt);
            }
            else if (old<=100&marko.HP>100) //healed back above threshold; gain bonuses
            {
                marko.ADR+=10; marko.immunities.add("Control");
                StatEff too=null;
                for (StatEff e: marko.effects)
                {
                    if (e instanceof Tracker&&e.geteffname().equals("Cyttorak's Blessing lost"))
                    {
                        too=e; break;
                    }
                }
                marko.effects.remove(too);
                Tracker salt= new Tracker("Cyttorak's Blessing active"); marko.effects.add(salt);
            }
        }
        else if (time.equals("start")) //fightstart
        {
            marko.immunities.add("Snare"); marko.immunities.add("Stun"); marko.ADR+=10; marko.immunities.add("Control");
            Tracker rage= new Tracker("Momentum: "); marko.effects.add(rage); rage.onApply(marko);
            Tracker salt= new Tracker("Cyttorak's Blessing active"); marko.effects.add(salt);
        }
        else if (time.equals("attack")&&marko.passivecount<5) //onattack
        {
            marko.passivecount++;
            System.out.println(marko.Cname+" gained 1 Momentum.");
            if (marko.passivecount==5)
            {
                System.out.println(marko.Cname+" is unstoppable!"); marko.immunities.add("Debuffs");
            }
        }
    }
    public static void Flash (Character eugene, int change) //called by activatep and onattacked
    {
        int old=eugene.passivecount; 
        if (change<0)
        System.out.println(eugene.Cname+" lost "+Math.abs(change)+" Control Points.");
        else
        System.out.println(eugene.Cname+" gained "+change+" Control Points.");
        eugene.passivecount+=change; //tracker auto updates turnend and when attacked so no need to do it here
        if (eugene.passivecount>10)
        eugene.passivecount=10;
        if (eugene.passivecount<0)
        eugene.passivecount=0;
        int New=eugene.passivecount;
        if (old<=5&&New>5) //losing control when at or under 5 C; this is for changing to in check
        {
            System.out.println(eugene.Cname+" is In Check.");
            eugene.BD-=15; eugene.Cchance+=50;
        }
        else if (old>5&&New<=5) //in check while at or above 6 C; this is for changing to losing control
        {
            System.out.println(eugene.Cname+" is Losing Control!");
            eugene.BD+=15; eugene.Cchance-=50;
        }
        //otherwise do nothing since flash's current state hasn't changed
    }
    public static void Binary (Character binary) //for consuming energy; onturn, onallyturn, onenemyturn
    {
        if (binary.dead==false&&!(binary.binaries.contains("Banished"))&&!(binary.binaries.contains("Stunned")))
        {
            binary.passivecount--;
            System.out.println(binary.Cname+" lost 1 Energy.");
            Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(binary.team1));
            for (Character n: enemies)
            {
                if (n!=null)
                {
                    int damage=5;
                    damage-=n.ADR;
                    if (damage<0)
                    damage=0;
                    n.TakeDamage(n, binary, damage, true);
                }
            }
            for (StatEff e: binary.effects) //update displayed energy count
            {
                if (e instanceof Tracker)
                {
                    e.Attacked(binary, null, 616);
                }
            }
        }
        if (binary.dead==false&&binary.passivecount<=0)
        {
            System.out.println(binary.Cname+" ran out of Energy."); binary.Transform(23, false);
        }
    }
    public static void CM (Character carol, boolean turn, int dmg)
    {
        if (turn==true&&!(carol.binaries.contains("Stunned"))&&!(carol.binaries.contains("Banished"))) //onturn
        {
            System.out.println(carol.Cname+" gained 1 Energy.");
            ++carol.passivecount;
        }
        else if (dmg>80&&!(carol.binaries.contains("Stunned"))) //tookdamage 
        {
            System.out.println(carol.Cname+" gained 1 Energy.");
            ++carol.passivecount;
        }
        for (StatEff e: carol.effects) //update displayed energy count
        {
            if (e instanceof Tracker)
            {
                e.Attacked(carol, null, 616);
            }
        }
        if (carol.passivecount>=5) 
        carol.Transform(24, false);
    }
    public static void Superior (Character tolliver, Character evildoer, boolean attack) //attack and onattack; cardselection lets him select targets with tracers
    {
        if (attack==true&&evildoer.CheckFor("Tracer", false)==true)
        {
            CoinFlip.AddInescapable(tolliver, true);
            tolliver.passivecount=1;
        }
        else if (attack==false&&tolliver.passivecount==1) //don't want to remove bonuses if they weren't gained in the first place
        {
            CoinFlip.AddInescapable(tolliver, false);
            tolliver.passivecount=0;
        }
    }
    public static void Spidey (Character peter, Character villain, boolean harm, boolean aoe) //his second passive is an if statement under onallytargeted since it's so simple
    {
        if (harm==false&&!(peter.binaries.contains("Stunned"))) //called onturn
        {
            Evade numb= new Evade(500);
            boolean yes=CoinFlip.Flip(500+peter.Cchance);
            if (yes==true)
            StatEff.CheckApply(peter, peter, numb);
            else
            StatEff.applyfail(peter, numb, "chance");
        }
        else if (aoe==true) //called by hero.ontargeted, before he can take dmg
        {
            if (!(villain.binaries.contains("Missed"))&&!(villain.immunities.contains("Missed"))&&!(villain.ignores.contains("Evade")))
            {
                if (!(peter.binaries.contains("Shattered"))&&!(peter.binaries.contains("Stunned"))) //conditions that would prevent him from evading
                {
                    System.out.println ("\n"+peter.Cname+" Evaded "+villain.Cname+"'s attack!");
                    villain.binaries.add("Missed");
                }
            }   
        }
    }
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
        if (!(attacker.ignores.contains("Counter"))&&!(eddie.binaries.contains("Stunned")))
        {
            if (attacked==eddie.passivefriend[0])
            {
                int dmg=40;
                System.out.println ("\nThis one is under our protection!");
                dmg-=attacker.ADR;
                System.out.println ("\n"+eddie.Cname+" did "+dmg+" damage to "+attacker.Cname);
                attacker.TakeDamage(attacker, dmg, false);  
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
            for (StatEff e: wolvie.effects) //to update after an elusive attack since they don't trigger the tracker's .attacked 
            {
                if (e instanceof Tracker)
                {
                    e.Attacked(wolvie, null, 0);
                }
            }
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
                    wolvie.remove(e.hashcode, "normal");
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
    public static void X23 (Character laura, Character victim, boolean crit, boolean before)
    {
        if (crit==false) //hero.beforeattack and hero.onattack
        {
            if (before==true&&laura.passivecount==0&&victim.HP<90) //check before attacking
            {
                laura.passivecount=1; laura.CC+=50; 
            }
            else if (before==false&&laura.passivecount==1) //undo after attacking
            {
                laura.passivecount=0; laura.CC-=50; 
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
            drax.onDeath(null, "DoT");
        }
        else if (attack==true) //add one more obsession to the target
        {
            if (victim==drax.passivefriend[0]&&drax.passivecount<3&&(!(drax.binaries.contains("Missed"))))
            {
                if (victim.CheckFor("Obsession", false)==true)
                {
                    boolean yes=CoinFlip.Flip(500+drax.Cchance);
                    Obsession obs= new Obsession ();
                    if (yes==true)
                    {
                        drax.passivefriend[0].add(obs);
                        drax.passivecount++;
                    }
                    else
                    StatEff.applyfail(drax, obs, "chance");
                }
            }
        }
        return true;
    }
    public static void FuryJr (Character marcus, String cause, boolean summoned)
    {
        if (cause.equals("activate")) //called by fury's kill mode ability
        {
            marcus.passivecount=1; marcus.Cchance+=50; marcus.BD+=15;
            System.out.println ("Kill Mode enabled.");
            Tracker a= new Tracker ("Kill Mode active"); marcus.effects.add(a);
        }
        else if (cause.equals("onturn")) //called onturn
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
                    System.out.println ("Kill Mode disabled."); StatEff t=null;
                    for (StatEff e: marcus.effects)
                    {
                        if (e instanceof Tracker&&e.geteffname().equals("Kill Mode active"))
                        {
                            t=e; break;
                        }
                    }
                    marcus.remove(t.hashcode, "silent");
                }
                else
                {
                    marcus.HP-=15;
                    System.out.println ("\n"+marcus.Cname+" lost 15 health");
                    if (marcus.HP<=0)
                    {
                        marcus.HP=0;
                        marcus.onLethalDamage(null, "other");
                    }
                }
            }
        }
        else if (cause.equals("allyturn")&&marcus.passivecount==1&&summoned==false&&marcus.dead==false) //called on ally turn
        {
            marcus.HP-=15;
            System.out.println ("\n"+marcus.Cname+" lost 15 health");
            if (marcus.HP<=0)
            {
                marcus.HP=0;
                marcus.onLethalDamage(null, "other");
            }
        }
    }
    public static void StarLord (Character quill)
    {
        if (!(quill.binaries.contains("Stunned"))) //called onturn
        {
            if (quill.turn%2!=0) //odd numbers only since the first turn is turn 0; every other turn
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
        }
    }
    public static void CaptainA (Character cap)
    {
        if (!(cap.binaries.contains("Shattered"))&&!(cap.binaries.contains("Stunned"))) //onturn and fight start
        {
            cap.Shielded(20);
        }
    }
    public static void IM (Character tony, StatEff buff) //called by hero.remove
    {
        Empower emp= new Empower (buff.power, buff.duration, tony.Cname, 4); 
        tony.add(emp);
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
        if (!(attacker.ignores.contains("Counter"))&&!(knight.binaries.contains("Stunned")))
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&eff.getProtector().equals(knight))
                {
                    int dmg=55;
                    System.out.println ("\nThe Lunar Protector strikes back!");
                    dmg-=attacker.ADR;
                    System.out.println ("\n"+knight.Cname+" did "+dmg+" damage to "+attacker.Cname);
                    attacker.TakeDamage(attacker, dmg, false);  
                    break;
                }
            }
        }
    }    
}