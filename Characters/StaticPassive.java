package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 20/8/22
 * Filename: StaticPassive
 * Purpose: Split passives into two files because of its length; this is for passives that trigger only once per fight, or trigger infrequently, or are extremely simple to implement.
 */
import java.util.ArrayList;
public class StaticPassive 
{
    public static StatEff InstaConversion (Character target, StatEff e, String toadd, int strength, int dur) //hero.add; for electro, zzzax, etc
    {
        String[] string={toadd, Integer.toString(e.chance), Integer.toString(strength), Integer.toString(dur), "true"}; String[][] her=StatFactory.MakeParam(string, null);
        StatEff brand=StatFactory.MakeStat(her, target);
        System.out.println(target.Cname+"'s "+e.geteffname()+" was converted into a "+brand.geteffname()+"!");
        return brand;
    }
    public static void Symbiote (Character venom, int vuln, boolean start) //efficient since they all have the same passive; fightstart and add/remove
    {
        if (start==true)
        venom.ignores.add("Evade");
        else
        venom.DV+=vuln;
    }
    //2.9: Fearsome Foes of Spider-Man
    public static void Kraven (Character sergei, Character prey, boolean attacking) //beforeattack and onattack
    {
        if (attacking==true&&prey.CheckFor("Snare", false)==true)
        {
            sergei.ignores.add("Blind"); sergei.ignores.add("Evade");
            sergei.passivecount=1;
        }
        else if (attacking==false&&sergei.passivecount==1) //don't want to remove bonuses if they weren't gained in the first place
        {
            sergei.ignores.remove("Blind"); sergei.ignores.remove("Evade");
            sergei.passivecount=0;
        }
    }
    //2.8: Defenders
    public static void Surfer (Character norrin) //fightstart
    {
        CoinFlip.StatImmune(norrin, true); norrin.immunities.add("Interrupt");
    }
    public static int LukeCage (Character carl, int dmg, boolean start)
    {
        if (start==true) //fightstart
        {
            carl.immunities.add("Burn"); carl.immunities.add("Bleed"); carl.immunities.add("Shock");
        }
        else if (dmg>0&&dmg<50)//takedamage
        {
            System.out.println("Unbreakable!"); dmg-=25;
        }
        if (dmg<0)
        return 0;
        else
        return dmg;
    }
    public static void DD (Character matt, Character attacker, boolean start)
    {
        if (start==true) //fightstart
        {
            matt.ignores.add("Blind"); matt.ignores.add("Invisible");
        }
        else //attacked; counter doesn't trigger if stunned or attacker ignores it so no need to check for it here
        {
            ArrayList<StatEff> concurrentmodificationexception3electricboogalooboogaloo= new ArrayList<StatEff>(); //counter is removed after use :)
            concurrentmodificationexception3electricboogalooboogaloo.addAll(matt.effects);
            for (StatEff eff: concurrentmodificationexception3electricboogalooboogaloo)
            {
                if (eff.getimmunityname().equals("Counter")) //trigger all his remaining Counters
                {
                    eff.Attacked(matt, attacker, 0); //dmg dealt by the attacker doesn't matter for counter so it'll just send over 0
                }  
            }
        }
    }
    //2.1: Sinister 6
    public static void Rhino (Character alexei) //fightstart
    {
        alexei.immunities.add("Vulnerable"); alexei.immunities.add("Suppression"); alexei.immunities.add("Reduce"); alexei.immunities.add("Terror"); 
        alexei.BlDR+=10; ResistanceE me= new ResistanceE(500, 10, 616); alexei.add(me);
    }
    public static void Sandy (Character will) //fightstart
    {
        will.immunities.add("Bleed"); will.immunities.add("Shock"); will.immunities.add("Disarm"); will.ignores.add("Counter");
    }
    public static void Vulture (Character adrian, Character prey) //beforeattack
    {
        if (prey.HP<120)
        {
            System.out.println("The Vulture circles his prey...");
            String[] sean={"Wound", "50", "616", "1", "false"}; String[][] hannity=StatFactory.MakeParam(sean, null);
            adrian.activeability.AddTempString(hannity);
        }
    }
    //2.0: Original
    public static void Deadpool (Character wade, String cause, Character vic) 
    {
        if (cause.equals("heal")&&!(wade.binaries.contains("Stunned"))) //onturn
        {
            wade.Healed(30, true);
        }
        else if (vic!=null&&cause.equals("kill")) //onkill
        {
            int t=1;
            for (Ability a: wade.abilities)
            {
                if (a!=null&&a.dcd>0)
                {
                    if (t==1)
                    {
                        System.out.println(""); t--; //to put space in between kill message and cd reduction, but only once and only if there are any cds to reduce
                    }
                    System.out.println(a.GetAbName(wade)+" had its cooldown reduced by 1 turn(s).");
                    a.CDReduction(1);
                }
            }
        }
        else if (vic!=null&&cause.equals("attack")&&vic.summoned==true) 
        {
            if (wade.passivecount==0) //beforeattack
            {
                wade.BD+=15; wade.passivecount=1;
            }
            else if (wade.passivecount==1) //onattack
            {
                wade.BD-=15; wade.passivecount=0;
            }
        }
    }
    public static void BB (Character blackagar, boolean start)
    {
        if (start==true) //fightstart
        {
            blackagar.immunities.add("Control"); Tracker rage= new Tracker ("Electrons: "); blackagar.effects.add(rage); rage.onApply(blackagar);
        }
        else if (!(blackagar.binaries.contains("Stunned"))&&blackagar.turn%2!=0) //odd numbers only since the first turn is turn 0; every other turn; onturn
        {
            blackagar.passivecount++; System.out.println(blackagar.Cname+" gained 1 Electron.");
            for (StatEff e: blackagar.effects) //update tracker to accurately show E since it otherwise only updates onturnend
            {
                if (e instanceof Tracker&&e.geteffname().equals("Electrons: "+(blackagar.passivecount-1)))
                {
                    e.onTurnEnd(blackagar); break;
                }
            }
        }
    }
    public static void Hulk (Character banner, boolean start) //fightstart and hpchange
    {
        if (start==true)
        {
            banner.immunities.add("Terror"); banner.immunities.add("Poison"); banner.immunities.add("Control"); 
            banner.immunities.add("Persuaded"); Tracker rage= new Tracker ("Rage: "); banner.effects.add(rage); rage.onApply(banner);
        }
        else
        {
            int dif=banner.maxHP-banner.HP;
            int number=0;
            if (dif>=50&&dif<100)
            number=5;
            else if (dif>=100&&dif<150)
            number=10;
            else if (dif>=150&&dif<200)
            number=15;
            else if (dif>=200&&dif<250)
            number=20;
            else if (dif>=250&&dif<300) //technically possible with apocalypse, but 300 missing health is impossible so it stops checking here
            number=25;
            banner.ADR=number; banner.PBD=number; banner.passivecount=number;
            for (StatEff e: banner.effects) //update rage tracker
            {
                if (e instanceof Tracker)
                {
                    e.Attacked(banner, null, 616);
                }
            }
        }
    }
    public static void Brawn (Character cho) //fightstart
    {
        cho.immunities.add("Poison"); cho.immunities.add("Control");
    }
    public static void DOOM (Character doctor, String occassion, Character fool)
    {
        if (occassion.equals("start"))
        {
            doctor.immunities.add("Burn"); doctor.immunities.add("Freeze"); doctor.immunities.add("Shock"); doctor.immunities.add("Persuaded"); doctor.immunities.add("Control");
            Tracker er=new Tracker("Titanium Battlesuit armed"); doctor.effects.add(er);
        }
        else if (occassion.equals("turn")&&!(doctor.binaries.contains("Stunned")))
        {
            doctor.passivecount=0; boolean got=false; 
            Tracker er=new Tracker("Titanium Battlesuit armed");
            for (StatEff e: doctor.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Titanium Battlesuit armed"))
                {
                    got=true; break;
                }
            }
            if (got==false)
            doctor.effects.add(er);
        }
        else if (occassion.equals("attacked")&&!(doctor.binaries.contains("Stunned")))
        {
            if (doctor.passivecount==0) //battlesuit armed
            {
                doctor.passivecount=1; StatEff too=null;
                for (StatEff e: doctor.effects)
                {
                    if (e instanceof Tracker&&e.geteffname().equals("Titanium Battlesuit armed"))
                    {
                        too=e; break;
                    }
                }
                doctor.effects.remove(too);
                Shock e= new Shock(500, 20, 1);
                boolean yes=CoinFlip.Flip(500+doctor.Cchance);
                if (yes==true)
                StatEff.CheckApply(doctor, fool, e);
                else
                StatEff.applyfail(doctor, e, "chance");
            }
        }
    }
    public static void Ultron (Character hypocrite) //fightstart
    {
        CoinFlip.RobotImmunities(hypocrite, true); hypocrite.immunities.add("Snare"); hypocrite.immunities.add("Steal"); hypocrite.immunities.add("Control");
    }
    public static int MODOC (Character george, Character target, String cause, int dmg)
    {
        if (cause.equals("start")) //onfightstart
        {
            CoinFlip.IgnoreTargeting(george, true); george.SHLD=100; george.passivecount=1; george.immunities.add("Debuffs");
        }
        else if (cause.equals("attack")) //apply debuffs when attacking; called by beforeattack
        {
            boolean stop=false;
            for (int i=0; i<5; i++)
            {
                if (stop==true) //stop checking for other kinds of abilities bc debuff has been determined
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
                                if (george.activeability!=null)
                                george.activeability.AddTempString(nuclear); 
                                stop=true;
                            }
                            break;
                            case 1:
                            if (a instanceof HealAb)
                            {
                                String[] choochoo={"Afflicted", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                if (george.activeability!=null)
                                george.activeability.AddTempString(nuclear); 
                                stop=true;
                            }
                            break;
                            case 2:
                            if (a instanceof DebuffAb)
                            {
                                String[] choochoo={"Neutralise", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                if (george.activeability!=null)
                                george.activeability.AddTempString(nuclear); 
                                stop=true;
                            }
                            break;
                            case 3:
                            if (a instanceof BuffAb)
                            {
                                String[] choochoo={"Undermine", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                if (george.activeability!=null)
                                george.activeability.AddTempString(nuclear); 
                                stop=true;
                            }
                            break;
                            case 4: 
                            if (a instanceof AttackAb)
                            {
                                String[] choochoo={"Disarm", "500", "616", "1", "false"}; String[][] nuclear=StatFactory.MakeParam(choochoo, null); 
                                if (george.activeability!=null)
                                george.activeability.AddTempString(nuclear); 
                                stop=true;
                            }
                            break;
                        }
                        if (stop==true) //stop going through other abilities
                        break;
                    }
                }
            }
        }
        else if (cause.equals("attacked")&&george.passivecount==1) //called at start of takedamage; passive only applies until shield is broken
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
    public static void Flash (Character user) //called beforeattack; here bc it's short
    {
        if (user.passivecount<=5) //attacking while losing control 
        {
            int amount=15-user.ADR; 
            System.out.println (user.Cname+" took "+amount+" damage");
            user.TakeDamage(user, amount, false);
            String[]akaban={"Bleed", "100", "10", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
        }
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
                binary.remove(e.hashcode, "normal");
            }
        }
        else //leaving binary state
        {
            binary.immunities.remove("Buffs"); binary.immunities.remove("Debuffs"); binary.immunities.remove("Heal"); binary.immunities.remove("Defence");
        }
    }
    public static void CM (Character carol, boolean start) 
    {
        if (start==true) //fightstart
        {
            carol.immunities.add("Poison");
            Tracker NRG= new Tracker("Energy: ");
            carol.effects.add(NRG);
            NRG.onApply(carol);
        }
        else //onattack
        {
            System.out.println(carol.Cname+" gained 1 Energy.");
            ++carol.passivecount;
            for (StatEff e: carol.effects) //update displayed energy count
            {
                if (e instanceof Tracker&&e.geteffname().equals("Energy: "+(carol.passivecount-1)))
                {
                    e.Attacked(carol, null, 616);
                }
            }
            if (carol.passivecount>=5) 
            carol.Transform(24, false);
        }
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
        friends[index].add(new Tracker ("Watched by Venom (Eddie Brock)"));
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
                arthur.remove(bl.hashcode, "silent");
            }
            else if (arthur.passivecount==-1)
            {
                arthur.passivecount=0;
                arthur.BD-=15;
            }
        }
        else //activate his passive; called by both hero.beforeattack and knife slash since debuffmod occurs before attacking
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
        foes[0].add(obsess);
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
        friends[index].add(red);
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
            TargetE bay= new TargetE(500, 5, 616);
            StatEff.CheckApply(machine, foes[0], bay);
            if (foes[0].effects.contains(bay))
            {
                machine.passivefriend[0]=foes[0];
                machine.passivecount=bay.hashcode;
                foes[0].immunities.add("Invisible");
                ArrayList<StatEff> concurrentmodificationexception2electricboogaloo= new ArrayList<StatEff>();
                for (StatEff eff: foes[0].effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Invisible"))
                    {
                        concurrentmodificationexception2electricboogaloo.add(eff);
                    }
                }
                for (StatEff eff: concurrentmodificationexception2electricboogaloo)
                {
                    foes[0].remove(eff.hashcode, "normal");
                }
            }
            CoinFlip.AddInescapable (machine, false);
        }
    }
}