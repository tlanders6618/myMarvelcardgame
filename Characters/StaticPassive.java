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
    public static void Burn (Character venom, int vuln) //efficient since symbiotes all have the same passive, as well as human torch, mephisto, etc; add/remove
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
                if (e instanceof Tracker&& e.getimmunityname().equals(josh+" will Resurrect after dying"))
                {
                    josh.remove(e.id, "silent"); break;
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
            emma.remove(e.id, "normal");
        }
        Taunt elephant=new Taunt (500, 1, emma); //gains taunt due to passive, so anyone transforming into her would gain her passive and thus taunt
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
            Drain putty= new Drain(500, false, 1, nye); 
            boolean goal=CoinFlip.Flip(500+nye.Cchance);
            if (goal==true) 
            StatEff.CheckApply(nye, nye, putty);
            else
            StatEff.applyfail(nye, putty, "chance");
            //gain focus
            FocusE pumpkin= new FocusE(500, 1, nye); 
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
        if (attacking==true&&prey.CheckFor("Disorient", false)==true)
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
        concurrentmodificationexception3electricboogalooboogaloo.addAll(matt.effects); //same boogaloo as the one in hero.attacked
        for (StatEff eff: concurrentmodificationexception3electricboogalooboogaloo)
        {
            if (eff.getimmunityname().equals("Counter")) //trigger all his remaining Counters
            eff.Attacked(matt, attacker, 0); //dmg dealt by the attacker doesn't matter for counter so it'll just send over 0
        }
    }
    //2.7: Thunderbolts
    public static void Zemo (Character helmut, boolean turn)
    {
        if (turn==true&&!(helmut.binaries.contains("Stunned"))) //onturn
        {
            if (helmut.CheckFor("Guard", false)==true)
            { 
                ArrayList<StatEff> bigboy= new ArrayList<StatEff>(helmut.effects);
                for (StatEff e: bigboy)
                {
                    if (e.getimmunityname().equals("Guard"))
                    {
                        helmut.remove(e.id, "normal");
                    }
                }
            }
        }
        else //onattack
        {
            if (helmut.passivecount==0&&helmut.activeability.oname.equals("Deadly Lunge"))
            {
                System.out.println("En garde!");
                helmut.passivecount=1;
                Precision p= new Precision(500, 616, helmut);
                boolean add=CoinFlip.Flip(500+helmut.Cchance);
                if (add==true)
                StatEff.CheckApply(helmut, helmut, p);
                else
                StatEff.applyfail(helmut, p, "chance");
            }
        }
    }
    //2.5: Thanos Arrives
    public static void Maw (Character ebony) //just ondeath; all the maw's other passives are implemented under the whisper and persuaded effs because that was easier
    { 
        for (Character c: Battle.GetTeammates(ebony))
        {
            if (c!=null&&c.CheckFor("Persuaded", false)==true)
            {
                ArrayList<StatEff> opp= new ArrayList<StatEff>(c.effects); 
                for (StatEff e: opp)
                {
                    if (e.getimmunityname().equals("Persuaded"))
                    c.remove(e.id, "normal");
                }
            }
        }
    }
    public static void Supergiant (Character superg) //onturn and ondeath; uses superg.dead==false/true instead of param boolean, so both if statements are checked by both calls
    {
        if (superg.dead==false&&superg.turn==0&&!(superg.binaries.contains("Stunned"))) //first turn only; dead must be false since turn counter resets ondeath
        {
            if (superg.team1==true)
            {
                System.out.println ("\nPlayer 1, choose an enemy for Supergiant to apply Terror to. Type the number in front of their name.");
            }
            else
            {
                System.out.println ("\nPlayer 2, choose an enemy for Supergiant to apply Terror to. Type the number in front of their name.");
            }
            CoinFlip.AddInescapable (superg, true);
            Character[] foes=Battle.TargetFilter(superg, "enemy", "single");
            Terror bay= new Terror(500, 1, superg);
            if (foes[0].immunities.contains("Control"))
            {
                System.out.println(superg+"'s Mind Games had no effect due to "+foes[0]+"'s immunity to Control.");
            }
            else
            {
                StatEff.CheckApply(superg, foes[0], bay); 
            }
            CoinFlip.AddInescapable (superg, false);
        }
        else if (superg.dead==true) //ondeath; remove all dominates
        {
            for (Character c: Battle.GetTeammates(superg))
            {
                if (c!=null&&c.CheckFor("Dominate", false)==true)
                {
                    ArrayList<StatEff> opp= new ArrayList<StatEff>(c.effects); 
                    for (StatEff e: opp)
                    {
                        if (e.getimmunityname().equals("Dominate"))
                        c.remove(e.id, "normal");
                    }
                }
            }
        }
    }
    public static void Corvus (Character glaive, String cuz, StatEff eff) //onlethal damage and remove and onrez
    {
        if (glaive.passivecount==0&&cuz.equals("lethal")) //all passives ignore stun
        {
            glaive.passivecount=1;
            glaive.CC-=100;
            GuardE guard= new GuardE(500, 0, 1, glaive);
            if (CoinFlip.Flip(500+glaive.Cchance)==true)
            {
                if (StatEff.CheckFail(glaive, glaive, guard)==false) //only immortal if guard can be applied, or else it couldn't be removed and he'd literally never die
                {
                    System.out.println("\nIt is not my time. Not yet...");
                    glaive.binaries.add("No Heal"); //to avoid missing health restoration; he's supposed to be dead after losing guard, not have an entire second life
                    for (StatEff e: new ArrayList<StatEff>(glaive.effects))
                    {
                        glaive.remove(e.id, "normal");
                    }
                    glaive.add(guard, true);
                    CoinFlip.StatImmune(glaive, true);
                    glaive.binaries.add("Immortal"); 
                } 
                else
                {
                    System.out.println("\nHow can this be? My glaive...fails me."); //other than Leader or Kang, nothing should prevent it
                }
            }
            else
            {
                StatEff.applyfail(glaive, guard, "chance");
                System.out.println("\nHow can this be? My glaive...fails me.");
            }
        }
        else if (cuz.equals("remove")&&glaive.passivecount==1&&eff.prog==glaive&&eff.getimmunityname().equals("Guard")&&eff.getefftype().equals("Other")) //his guard was removed
        {
            glaive.binaries.remove("Immortal"); CoinFlip.StatImmune(glaive, false); //or else he'd be immune to resurrect
        }
        else if (cuz.equals("rez"))
        {
            glaive.passivecount=0; glaive.CC+=100; //reset passives so they can be used again during his new life
        }
    }
    public static void Gauntlet (Character thanos) //hero.transform
    {
        Character[] foes=Battle.GetTeam(CoinFlip.TeamFlip(thanos.team1));
        boolean good=false; //if transformation triggered by last enemy dying, do not take turn or else game won't be able to end due to lack of targets
        for (Character c: foes)
        {
            if (c!=null&&c.dead==false)
            {
                good=true; break;
            }
        }
        if (good==true)
        {
            if (!(thanos.binaries.contains("Stunned"))&&(Battle.team1[Battle.P1active]==thanos||Battle.team2[Battle.P2active]==thanos)) //transformation happens on turn start
            thanos.helpers.add(new BonusTurnHelper()); //only use helper if not stunned, to avoid interrupting turn
            else //transformation happens on someone else's turn or after thanos skips turn due to stun; should take turn immediately
            Battle.Turn(thanos, true);   
        }
    }
    //2.1: Sinister 6
    public static void Rhino (Character alexei) //fightstart
    {
       ResistanceE me= new ResistanceE(500, 15, 616, alexei); 
       boolean goal=CoinFlip.Flip(500+alexei.Cchance);
       if (goal==true) 
       StatEff.CheckApply(alexei, alexei, me);
       else
       StatEff.applyfail(alexei, me, "chance");
    }
    public static void Sandy (Character baker, String o)
    {
        if (o.equals("ult")) //activatep; use sandstorm 
        {
            ArrayList<StatEff> opp= new ArrayList<StatEff>(baker.effects); 
            for (StatEff e: opp)
            {
                if (!(e.getefftype().equals("Secret")))
                baker.remove(e.id, "normal");
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
                        Damage_Stuff.ElusiveDmg(baker, chump, 20, "default");
                    }
                }
                for (Character chump: friends)
                {
                    if (chump!=null)
                    {
                        Damage_Stuff.ElusiveDmg(baker, chump, 20, "default");
                    }
                }
                for (StatEff e: new ArrayList<StatEff>(baker.effects))
                {
                    if (e instanceof Tracker&& e.geteffname().equals("Sand Storm active: "+(baker.passivecount+1)+" turns"))
                    {
                        if (baker.passivecount>0) //update tracker
                        e.onApply(baker); 
                        else
                        {
                            baker.remove(e.id, "silent"); System.out.println(baker.Cname+"'s Sand Storm ended.");
                        }
                        break;
                    }
                }
            }
        }
        else //if (o.equals("burn")) //add
        {
            ArrayList<StatEff> burns= CoinFlip.GetEffs(baker, "Burn", "any");
            if (burns.size()>1) //2 or more
            {
                System.out.print("\n");
                baker.remove(burns.get(0).id, "normal"); baker.remove(burns.get(1).id, "normal");
                StunE hope= new StunE(500, 1, baker); StatEff.CheckApply(baker, baker, hope);
            }
        }
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
                Shock e= new Shock(500, 20, 1, doctor);
                boolean yes=CoinFlip.Flip(500+doctor.Cchance);
                if (yes==true)
                StatEff.CheckApply(doctor, fool, e);
                else
                StatEff.applyfail(fool, e, "chance");
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
            binary.remove(e.id, "normal");
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
            System.out.println ("\nPlayer 1, choose a character for "+eddie+" to watch over. Type the number in front of their name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose a character for "+eddie+" to watch over. Type the number in front of their name.");
        }
        Character[] friends=Battle.GetTeammates(eddie);
        ResistanceE res= new ResistanceE (500, 10, 616, eddie);
        Character friend=Card_Selection.ChooseTargetFriend (friends);
        StatEff.CheckApply(eddie, friend, res);
        eddie.passivecount=res.id;
        eddie.passivefriend.add(0,friend); //add, not set, since arraylist starts empty and technically has no index 0 yet
        friend.add(new Tracker ("Watched by Venom (Eddie Brock)"), false);
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
        else if (time.equals("turnend")&&arthur.activeability!=null&&!(arthur.activeability.oname.equals("Twin Blades"))) //reset twin blades, but only if he used it already
        {
            if (arthur.passivecount==10) //can tell he consumed it if his passivecount has been changed and his last used ability was something other than twin blades
            {
                arthur.passivecount=0; //attacks with twin blades active that didn't trigger it (e.g. against an enemy with 5 HP) shouldn't cause it to be removed
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
                    arthur.remove(bl.id, "silent");
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
        Nametag obsess=new Nametag(500, 616, 616, drax);
        StatEff.CheckApply(drax, foes[0], obsess);
        drax.passivefriend.add(0,foes[0]); //add, not set, since arraylist starts empty and technically has no index 0 yet
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
        Redwing red= new Redwing(falcon); 
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
            TargetE bay= new TargetE(500, 5, 616, machine);
            StatEff.CheckApply(machine, foes[0], bay);
            if (foes[0].effects.contains(bay))
            {
                machine.passivefriend.add(0,foes[0]); //add, not set, since arraylist starts empty and technically has no index 0 yet
                machine.passivecount=bay.id;
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
                    foes[0].remove(eff.id, "normal");
                }
            }
            CoinFlip.AddInescapable (machine, false);
        }
    }
}