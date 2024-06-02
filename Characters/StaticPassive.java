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
    public static void Symbiote (Character venom, int vuln) //efficient since they all have the same passive; add/remove
    {
        venom.DV+=vuln;
    }
    //2.10: Marvellous Mutants
    public static void Elixir (Character josh) //ondeath
    {
        if (josh.passivecount==1)
        {
            josh.passivecount=0; Rez rez= new Rez(500, 100, true); rez.Use(josh, josh, 0); 
            ArrayList<StatEff> effects=josh.effects;
            for (StatEff e: effects)
            {
                if (e instanceof Tracker&& e.getimmunityname().equals("Elixir will Resurrect after dying"))
                {
                    josh.remove(e.hashcode, "silent"); break;
                }
            }
        }
    }
    public static void Frost (Character emma) //hero.transform; transforming into diamond form
    {
        ArrayList<StatEff> jail= new ArrayList<StatEff>(); jail.addAll(emma.effects);
        for (StatEff e: jail) //remove stateffs emma is immune to
        {
            String n=e.getimmunityname();
            if (e.getefftype().equals("Heal")||n.equals("Stun")||(e.getalttype().equals("damaging")&&!(n.equals("Countdown")&&!(n.equals("Wither")))))
            emma.remove(e.hashcode, "normal");
        }
        Taunt elephant=new Taunt (500, 1); //gains taunt due to passive, so anyone transforming into her would gain her passive and thus taunt
        boolean dew=CoinFlip.Flip(500+emma.Cchance);
        if (dew==true)
        StatEff.CheckApply(emma, emma, elephant);
        else
        StatEff.applyfail(emma, elephant, "chance");
        if (Battle.team1[Battle.P1active]==emma||Battle.team2[Battle.P2active]==emma)
        elephant.duration++; //to avoid premature expiry when transforming on turn, but not when forcibly transformed by ebony maw off turn 
    }
    //2.9: Fearsome Foes of Spider-Man
    public static void Phil (Character nye) ///hpchange
    {
        if (nye.passivecount==0&&nye.HP<130&&!(nye.binaries.contains("Stunned")))
        {
            System.out.println("\nOutburst!");
            nye.passivecount=1;
            //gain drain
            Drain putty= new Drain(500, false, 1); 
            boolean goal=CoinFlip.Flip(500+nye.Cchance);
            if (goal==true) 
            StatEff.CheckApply(nye, nye, putty);
            else
            StatEff.applyfail(nye, putty, "chance");
            //gain focus
            FocusE pumpkin= new FocusE(500, 1); 
            goal=CoinFlip.Flip(500+nye.Cchance);
            if (goal==true) 
            StatEff.CheckApply(nye, nye, pumpkin);
            else
            StatEff.applyfail(nye, pumpkin, "chance");
            //reset one cd
            Ability victim=null; 
            if (nye.team1==true)
            System.out.println ("\nPlayer 1, choose one of "+nye.Cname+"'s abilities to have its cooldown reset. Type its number, not its name.");
            else
            System.out.println ("\nPlayer 2, choose one of "+nye.Cname+"'s abilities to have its cooldown reset. Type its number, not its name.");
            for (int i=0; i<5; i++)
            {
                int a=i+1;
                if (nye.abilities[i]!=null&&!(nye.abilities[i] instanceof BasicAb))
                {
                    System.out.println (a+": "+nye.abilities[i].GetAbName(nye));  
                }
            }
            int choice=-1; boolean good=false;
            do
            {
                choice=Damage_Stuff.GetInput(); 
                --choice; //to get the index number since the number entered was the ability number
                if (choice<5&&choice>=0&&nye.abilities[choice]!=null&&!(nye.abilities[choice] instanceof BasicAb))
                {
                    good=true;
                    victim=nye.abilities[choice];
                }
            }
            while (good==false);
            if (victim.dcd>0)
            {
                System.out.println(nye.Cname+"'s "+victim.GetAbName(nye)+" had its cooldown reset.");
                victim.CDReduction(100);
            }
        }
    }
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
    public static int LukeCage (Character carl, int dmg) //takedamage
    {
        if (dmg>0&&dmg<50)
        {
            System.out.println("Unbreakable!"); dmg-=25;
        }
        if (dmg<0)
        return 0;
        else
        return dmg;
    }
    public static void DD (Character matt, Character attacker) //attacked; counter doesn't trigger if stunned or attacker ignores it so no need to check for it here
    {
        ArrayList<StatEff> concurrentmodificationexception3electricboogalooboogaloo= new ArrayList<StatEff>(); //counter is removed after use :)
        concurrentmodificationexception3electricboogalooboogaloo.addAll(matt.effects);
        for (StatEff eff: concurrentmodificationexception3electricboogalooboogaloo)
        {
            if (eff.getimmunityname().equals("Counter")) //trigger all his remaining Counters
            eff.Attacked(matt, attacker, 0); //dmg dealt by the attacker doesn't matter for counter so it'll just send over 0
        }
    }
    //2.1: Sinister 6
    public static void Rhino (Character alexei) //fightstart
    {
       ResistanceE me= new ResistanceE(500, 15, 616); 
       boolean goal=CoinFlip.Flip(500+alexei.Cchance);
       if (goal==true) 
       StatEff.CheckApply(alexei, alexei, me);
       else
       StatEff.applyfail(alexei, me, "chance");
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
            wade.Healed(30, true, false);
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
                    System.out.println(wade.Cname+"'s "+a.GetAbName(wade)+" had its cooldown reduced by 1 turn(s).");
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
    public static void BB (Character blackagar) //onturn
    {
        if (!(blackagar.binaries.contains("Stunned"))&&blackagar.turn%2!=0) //odd numbers only since the first turn is turn 0; must trigger every other turn
        {
            blackagar.passivecount++; System.out.println(blackagar.Cname+" gained 1 Electron.");
            for (StatEff e: blackagar.effects) //update tracker to accurately show E since it otherwise only updates onturnend
            {
                if (e instanceof Tracker&&e.getimmunityname().equals("Electrons: "))
                {
                    e.onTurnEnd(blackagar); break;
                }
            }
        }
    }
    public static void DOOM (Character doctor, String occassion, Character fool)
    {
        if (occassion.equals("turn")&&!(doctor.binaries.contains("Stunned")))
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
            if (doctor.passivecount==0) //battlesuit is armed
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
    public static int MODOC (Character george, Character target, String cause, int dmg)
    {
        if (cause.equals("start")) //onfightstart
        {
            george.SHLD=100; george.passivecount=1; george.immunities.add("Debuffs"); //only gains debuff immunity while shield is active, so this doesnt go under hero.addimmune
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
    public static void Binary (Character binary) //hero.transform; transforming into binary
    {
        ArrayList<StatEff> r= new ArrayList<StatEff>(); //remove all stateffs from self, plus shield since it's technically a def eff
        for (StatEff e: binary.effects) 
        {
            if (!(e instanceof Tracker))
            r.add(e);
        }
        for (StatEff e: r)
        {
            binary.remove(e.hashcode, "normal");
        }
        binary.SHLD=0;
        binary.passivecount=5;
        System.out.println(binary.Cname+" gained "+binary.passivecount+" Energy.");
        for (StatEff e: binary.effects) //update displayed energy count
        {
            if (e instanceof Tracker&&e.getimmunityname().equals("Energy: "))
            e.Attacked(binary, null, 616);
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
        Character allt=Card_Selection.ChooseTargetFriend (friends);
        StatEff.CheckApply(eddie, allt, res);
        eddie.passivecount=res.hashcode;
        eddie.passivefriend[0]=allt;
        allt.add(new Tracker ("Watched by Venom (Eddie Brock)"));
    }
    public static void Drax (Character arthur, Character target, String time) //in here bc I assume this'll be used once per fight due to its strict requirements
    {
        if (time.equals("knife")) //called by activatep on twin blades
        {
            arthur.passivecount=1; //indicates that twin blades is active, so the next attack should apply its bonuses if possible; if not, wait until an attack does to remove it
        }
        else if (time.equals("battack")) //activate his passive; called by both hero.beforeattack and knife slash since debuffmod occurs before attacking
        {
            int HP=target.maxHP;
            double tenth=HP*0.75; //75% of target maxhp
            HP=5*(int)Math.ceil(tenth/5.0); //rounded up to nearest multiple of 5
            if (target.HP>=HP) //then passive triggers
            {
                if (arthur.passivecount==1||arthur.passivecount==10) //twin blades
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
        else if (time.equals("onattack")) //hero.onattack; undoes passive bd after attack finished
        {
            if (arthur.passivecount==-2) //twin blades active
            {
                arthur.passivecount=10; arthur.BD-=30; //instead of 1, becomes 10 to indicate that twin blades was used
            }
            else if (arthur.passivecount==-1)
            {
                arthur.passivecount=0; arthur.BD-=15;
            }
        }
        else if (time.equals("turnend")&&arthur.activeability!=null&&arthur.activeability!=arthur.abilities[1]) //reset twin blades, but only if he used it already; see below 
        {
            if (arthur.passivecount==10) //attacks with twin blades active that didn't trigger it (e.g. against an enemy with 5 HP) shouldn't cause it to be removed
            {
                arthur.passivecount=0;
                if (arthur.dead==false) //if he died, effects would already be empty; otherwise, remove twin blades tracker to show it's been used
                {
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
            }
        }
    }
    public static void DraxOG (Character drax) //choosing obsession at fight start
    {
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
        StatEff.CheckApply(drax, foes[0], obsess);
        drax.passivefriend[0]=foes[0];
        drax.ignores.remove("Invisible");
        if (foes[0].effects.contains(obsess)) //only if the apply was successful
        drax.passivecount=1;
    }
    public static void FurySr (Character fury) //after an hpchange
    {
        if (fury.passivecount==0&&fury.HP<=90&&!(fury.binaries.contains("Stunned")))
        {
            Summon lmd= new Summon(1);
            lmd.team1=fury.team1;
            fury.passivecount=1;
            Battle.SummonSomeone(fury, lmd);            
        }
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
        Character alliance=Card_Selection.ChooseTargetFriend (friends);
        StatEff.CheckApply(falcon, alliance, red);
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