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
    //2.10: Marvellous Mutants
    public static void AA (Character aa, Character vic) //onattack
    {
        boolean bleed=CoinFlip.Flip(50);
        if (bleed==true)
        {
            String[] blood={"Bleed", "500", "15", "2", "false"}; String[][] rb=StatFactory.MakeParam(blood, null); aa.activeability.AddTempString(rb);
            //System.out.println("Bleed: "+CoinFlip.GetStatCount(vic, "Bleed", "any"));
        }
        if (CoinFlip.GetStatCount(vic, "Bleed", "any")>=3) //must be separate from above or else feather fling doesn't consistently apply the bonus debuff
        {
            String[] sick={"Weakness", "500", "30", "1", "false"}; String[][] sickness=StatFactory.MakeParam(sick, null); aa.activeability.AddTempString(sickness);
        }
        boolean poison=CoinFlip.Flip(50);
        if (poison==true)
        {
            String[] hemlock={"Poison", "500", "15", "2", "false"}; String[][] hemlocket=StatFactory.MakeParam(hemlock, null); aa.activeability.AddTempString(hemlocket);
            //System.out.println("Poison: "+CoinFlip.GetStatCount(vic, "Poison", "any"));
        }
        if (CoinFlip.GetStatCount(vic, "Poison", "any")>=3) 
        {
            String[] dead={"Wound", "500", "616", "1", "false"}; String[][] death=StatFactory.MakeParam(dead, null); aa.activeability.AddTempString(death);
        }
    }
    public static void Angel (Character warren, boolean turn, int dmg)
    {
        if (turn==true&&warren.CheckFor("Bleed", false)==true) //onturn; occurs even if stunned; don't want to print "angel was healed for 0 health" if he has no bleeds to heal with
        {
            int num=CoinFlip.GetStatCount(warren, "Bleed", "any"); num*=20; warren.Healed(num, true, false);
        }
        else if (dmg>0) //both verions of took damage; occurs even if stunned
        {
            BleedE bleed= new BleedE(500, 0, 2);
            boolean it=CoinFlip.Flip(500+warren.Cchance);
            if (it==true)
            StatEff.CheckApply(warren, warren, bleed);
            else
            StatEff.applyfail(warren, bleed, "chance");
        }
    }
    //2.9: Fearsome Foes of Spider-Man
    public static void OGHobby (Character kingsley, Character killer) //onenemydeath
    {
        if (killer!=null) //passive occurs even if stunned
        {
            if (killer.passivefriend[0]!=null&&killer.passivefriend[0]==kingsley) //all summons save their summoners as passivefriend[0]
            {
                kingsley.Healed(100, true, false);
                FocusE pumpkin= new FocusE(500, 1); 
                boolean goal=CoinFlip.Flip(500+kingsley.Cchance);
                if (goal==true) 
                StatEff.CheckApply(kingsley, kingsley, pumpkin);
                else
                StatEff.applyfail(kingsley, pumpkin, "chance");
                for (int i=0; i<5; i++)
                {
                    if (kingsley.abilities[i]!=null&&!(kingsley.abilities[i] instanceof BasicAb)&&kingsley.abilities[i].dcd>0)
                    {
                        System.out.println(kingsley.Cname+"'s "+kingsley.abilities[i].GetAbName(kingsley)+" had its cooldown reset.");
                        kingsley.abilities[i].CDReduction(100);
                    }
                }
            }
        }
    }
    public static void Roblin (Character kasborn, Character spider, String oc)
    {
        if (oc.equals("start")) //fightstart
        {
            kasborn.immunities.add("Burn"); kasborn.immunities.add("Snare");
        }
        else if (oc.equals("attack")&&spider.dead==false) //onattack; don't gain intensify if attack killed its target
        {
            IntensifyE pumpkin= new IntensifyE(500, 5, 616); 
            boolean goal=CoinFlip.Flip(500+kasborn.Cchance);
            if (goal==true) 
            StatEff.CheckApply(kasborn, kasborn, pumpkin);
            else
            StatEff.applyfail(kasborn, pumpkin, "chance");
        }
        else if (oc.equals("gain")) //add; successfuly gaining intensifye from passive
        {
            ++kasborn.passivecount; 
            if (kasborn.passivecount==3)
            kasborn.ignores.add("Evade");
            if (kasborn.passivecount==5)
            kasborn.Cchance+=50;
        }
        else if (oc.equals("death")) //enemydeath
        {
            ArrayList<StatEff> popcorn= new ArrayList<StatEff>();
            popcorn.addAll(kasborn.effects);
            if (kasborn.passivecount>0) //or else there are no effs to remove
            System.out.println(kasborn.Cname+"'s Intensify Effect(s) expired."); //instead of printing it 3-5+ times (once for each eff), print one message for all of them
            for (StatEff e: popcorn)
            {
                if (e.getimmunityname().equals("Intensify")&&e.getefftype().equals("Other"))
                {
                    kasborn.remove(e.hashcode, "silent");
                }
            }
            if (kasborn.passivecount>=5)
            {
                kasborn.ignores.remove("Evade"); kasborn.Cchance-=50;
            }
            else if (kasborn.passivecount>=3)
            kasborn.ignores.remove("Evade");
            kasborn.passivecount=0;
        }
    }
    public static void Carnage (Character cletus, int dur) //onenemygain
    {
        if (!(cletus.binaries.contains("Stunned")))
        {
            IntensifyE john= new IntensifyE(500, 5, dur);
            boolean goal=CoinFlip.Flip(500+cletus.Cchance);
            if (goal==true) //if bleed was applied by a teammate or enemy (i.e. on someone else's turn), carriage gains the intensify immediately
            StatEff.CheckApply(cletus, cletus, john);
            else
            StatEff.applyfail(cletus, john, "chance");
            if (Battle.team1[Battle.P1active]==cletus||Battle.team2[Battle.P2active]==cletus) //but if gained on cottage's turn, it needs +1 dur so it doesn't prematurely tick
            john.duration++;
        }
    }
    //2.1: Sinister 6
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
                        int damage=20;
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
    //2.0: Original
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
        if (binary.dead==false&&!(binary.binaries.contains("Banished"))&&!(binary.binaries.contains("Stunned"))&&binary.passivecount>0)
        {
            binary.passivecount--;
            System.out.println(binary.Cname+" lost 1 Energy.");
            Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(binary.team1));
            for (Character n: enemies)
            {
                if (n!=null)
                {
                    int damage=10;
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
            if (binary.passivecount==0)
            {
                System.out.println(binary.Cname+" ran out of Energy."); //binary.Transform(23, false);
                ArrayList<StatEff> jerker= new ArrayList<StatEff>(); jerker.addAll(binary.effects);
                for (StatEff e: jerker) //remove tracker since it is useless now that she has no way to gain any energy
                {
                    if (e instanceof Tracker && e.geteffname().equals("Energy: 0"))
                    {
                        binary.remove(e.hashcode, "silent");
                    }
                }
            }
        }
    }
    public static void CM (Character carol, boolean turn, int dmg)
    {
        if (turn==true&&!(carol.binaries.contains("Stunned"))) //onturn
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
    public static void Superior (Character tolliver, Character evildoer, boolean attack) //beforeattack and onattack; cardselection lets him select targets with tracers
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
            if (!(villain.binaries.contains("Missed"))&&!(villain.immunities.contains("Missed"))&&!(villain.ignores.contains("Evade"))&&peter.CheckFor("Soaked", false)==false)
            {
                if (!(peter.binaries.contains("Shattered"))&&!(peter.binaries.contains("Stunned"))) //conditions that would prevent him from evading
                {
                    boolean go=true;
                    if (villain.activeability!=null)
                    {
                        for (SpecialAbility a: villain.activeability.special)
                        {
                            if (a instanceof ApplyShatter&&a.CheckApply()==true) //cannot evade attacks that apply shatter
                            {
                                go=false; break;
                            }
                        }
                    }
                    if (go==true)
                    {
                        System.out.println ("\n"+peter.Cname+" Evaded "+villain.Cname+"'s attack!");
                        villain.binaries.add("Missed");
                    }
                }
            }   
        }
    }
    public static void Venom (Character macdonald) //called by hero.onkill
    {
        String[] focusi={"Focus", "500", "616", "1", "true"}; String[][] focus=StatFactory.MakeParam(focusi, null); macdonald.activeability.AddTempString(focus);
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
                BasicAb slash= new BasicAb ("X-Slash", "random 1", "enemy", 35); 
                String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
                BasicAb punch =new BasicAb ("Primal Punch", "random 1", "enemy", 35); punch.special.add(new Purify(50, 1, "random", "any", true, true));
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
        if (drax==victim) //onlethaldmg; check if undying rage should be triggered
        {
            if (!(drax.binaries.contains("Stunned"))&&dmgtype.equalsIgnoreCase("dot")) 
            { 
                if (!(drax.binaries.contains("Death"))) //no need to trigger message for each tick of dot, just the first
                {
                    System.out.println("\nDrax's rage is undying!");
                }
                drax.binaries.add("Death"); 
                return false; //whether he should die or not
            } 
            else
            return true;
        }
        else if (attack==false&&drax.dead==false&&drax.binaries.contains("Death")) //onturnend; if undying rage was triggered earlier, he finally dies now
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
        else if (attack==true) //onattack; add one more obsession to the target
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
        Empower emp= new Empower (500, buff.power, buff.duration, tony.Cname, 4); 
        tony.add(emp);
    }
    public static void Gamora (Character gam, StatEff buff, boolean add) //add is whether the buff is being added or removed; called by hero.remove and hero.add
    {
        if (add==true)
        {
            gam.Cchance+=50; gam.ignores.add("Protect"); gam.immunities.add("Steal"); gam.ignores.add("Counter");
        }
        else
        {
            gam.Cchance-=50; gam.ignores.remove("Protect"); gam.immunities.remove("Steal"); gam.ignores.remove("Counter");
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