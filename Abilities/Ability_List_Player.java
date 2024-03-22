package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 25/7/22
 * Filename: Ability_List_Player
 * Purpose: Creates/lists all character abilities.
 */
public class Ability_List_Player
{
    public static Ability GetAb (int index, int counter, boolean copy) //copy is for rogue and super adaptoid to copy abs
    {
        //abs construction: String name, String type, String friendly, int dmg, int cooldown
        //check MakeStatString for stateff array format
        //friendly means ally inc, ally exc, enemy, both, either, or self 
        //type is single, self, multitarg, random, or aoe 
        Ability toret=null;
        switch (index) //since making a giant array of dozens of elements was very slow, this should be faster, albeit longer
        {
            case 1: toret=MakeAbMK(counter); break;
            case 2: toret=MakeAbGam(counter, copy); break;
            case 3: toret=MakeAbPun(counter); break;
            case 4: toret=MakeAbIM(counter); break;
            case 5: toret=MakeAbWM(counter); break;
            case 6: toret=MakeAbCap(counter); break;
            case 7: toret=MakeAbFalc(counter); break;
            case 8: toret=MakeAbBucky(counter); break;
            case 9: toret=MakeAbStarLord(counter); break;
            case 10: toret=MakeAbNickSr(counter); break;
            case 11: toret=MakeAbNickJr(counter); break;
            case 12: toret=MakeAbOGDrax(counter); break;
            case 13: toret=MakeAbDrax(counter, copy); break;
            case 14: toret=MakeAbX23(counter); break;
            case 15: toret=MakeAbWolvie(counter); break;
            case 16: toret=MakeAbOGVenom(counter); break;
            case 17: toret=MakeAbVenom(counter); break;
            case 18: toret=MakeAbSpidey(counter); break;
            case 19: toret=MakeAbMiles(counter); break;
            case 20: toret=MakeAbSuperior(counter); break;
            case 21: toret=MakeAbStorm(counter); break;
            case 22: toret=MakeAbKK(counter); break;
            case 23: toret=MakeAbCM(counter); break;
            case 24: toret=MakeAbBinary(counter); break;
            case 25: toret=MakeAbFlash(counter); break;
            case 26: toret=MakeAbMODORK(counter); break;
            case 27: toret=MakeAbUltron(counter); break;
            case 28: toret=MakeAbDOOM(counter); break;
            case 29: toret=MakeAbStrange(counter); break;
            default: System.out.println ("Problem getting hero abilities");
        }
        return toret;
    }    
    //template
    public static Ability MakeAbName (int counter)
    {
        switch (counter)
        {
            case 0: 
            case 1:
            case 2: 
            case 3: 
            case 4: 
            default: return null;
        }
    }
    public static Ability MakeAbStrange(int counter)
    {
        switch (counter)
        {
            case 0: BasicAb baldack= new BasicAb("Bolt of Balthakk", "single", "enemy", 40); baldack.special.add(new Nullify(100, 1, "random", "any", false, true));
            return baldack;
            case 1: HealAb eye=new HealAb("Eye of Agamotto", "single", "ally inclusive", 3); eye.special.add(new Purify(500, 1, "random", "any", false, true));
            String[] horse={"Regen", "500", "45", "1", "knull"}; String[][] mockery=StatFactory.MakeParam(horse, null); eye.AddStatString(mockery);
            return eye;
            case 2: DefAb diamond= new DefAb("Seven Rings of Raggadorr", "single", "ally inclusive", 3); 
            String[] ash={"Resistance", "100", "15", "1", "knull"}; String[][] freeze=StatFactory.MakeParam(ash, null); diamond.AddStatString(freeze);
            return diamond;
            case 3: OtherAb xtreme= new OtherAb("Sorcerer Supreme", "self", "self", 4); xtreme.unbound=true; xtreme.special.add(new ReduceCD(true, 1)); 
            return xtreme;
            case 4: AttackAb band= new AttackAb("Crimson Bands of Cyttorak", "single", "enemy", 90, 4); band.together=true;
            String[] mane={"StunE", "100", "616", "1", "false"}; String[][] man=StatFactory.MakeParam(mane, null); band.AddStatString(man);
            String[] wolf={"SnareE", "100", "616", "1", "false"}; String[][] cycle=StatFactory.MakeParam(wolf, null); band.AddStatString(cycle);
            return band;
            default: return null;
        }
    }
    public static Ability MakeAbDOOM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb blast= new BasicAb("Magical Blast", "single", "enemy", 45); blast.special.add(new Ignore("Invisible", "always", 616));
            return blast;
            case 1: DefAb b= new DefAb("Personal Force Field", "self", "self", 4); 
            String[] opera={"Barrier", "500", "80", "2", "true"}; String[][] ophidian=StatFactory.MakeParam(opera, null); b.AddStatString(ophidian);
            return b;
            case 3: OtherAb s= new OtherAb("Master Summoner", "self", "self", 4); s.multiuse=1; s.special.add(new Summoning (5, 6));
            return s;
            case 4: HealAb time= new HealAb("Time Platform", "single", "ally inclusive", 5); time.channelled=true; time.special.add(new MendPassive(10000));
            return time;
            default: return null;
        }
    }
    public static Ability MakeAbUltron (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb ray= new BasicAb("Encephalo Ray", "single", "enemy", 40); String[] name={"any"}; String[] type={"Buffs"};
            ray.special.add(new CopySteal(100, 1, "random", name, type, true, true));
            return ray;
            case 1: BasicAb shoot= new BasicAb("Disintegration Beam", "single", "enemy", 40); String[] mane={"any"}; String[] tepy={"Buffs"};
            shoot.special.add(new Extend(100, 616, "all", mane, tepy, 1, true, true, false));
            return shoot;
            case 3: OtherAb s= new OtherAb("Summon Drones", "self", "self", 0); s.special.add(new Summoning(4));
            return s;
            case 4: AttackAb d= new AttackAb("Annihilate", "single", "enemy", 70, 3); d.special.add(new DamageCounter("Buffs", true, 20, true, true)); 
            d.special.add(new Ignore("Defence", "always", 616));
            return d;
            default: return null;
        }
    }
    public static Ability MakeAbMODORK (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb brain= new BasicAb ("Brain Blast", "single", "enemy", 50); brain.special.add(new ActivateP());
            return brain;
            case 4: OtherAb aim= new OtherAb("AIM Overlord", "self", "self", 3); aim.unbound=true; aim.special.add(new Summoning(2, 3));
            return aim;
            default: return null;
        }
    }
    public static Ability MakeAbFlash (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb s= new BasicAb("Symbiotic Assault", "single", "enemy", 40); s.special.add(new DebuffMod(25)); s.special.add(new ActivatePassive(-5)); 
            return s; //debuffmod allows ab to apply bleed and deal selfdmg if flash is losing control
            case 1: BasicAb fire= new BasicAb("Covering Fire", "single", "enemy", 40); fire.special.add(new DebuffMod(25)); 
            fire.special.add(new Nullify(50, 1, "random", "any", false, true)); fire.special.add(new ActivatePassive(5)); 
            return fire;
            case 4: AttackAb shock= new AttackAb ("Shock and Awe", "single", "enemy", 80, 2); shock.special.add(new DebuffMod(25,5));
            return shock;
            default: return null;
        }
    }
    public static Ability MakeAbBinary (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch= new BasicAb("Megaton Punch", "single", "enemy", 60);
            return punch;
            case 1: BasicAb wave= new BasicAb("Photonic Wave", "AoE", "enemy", 40); 
            return wave;
            default: return null;
        }
    }
    public static Ability MakeAbCM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb mighty= new BasicAb("Mighty Punch", "single", "enemy", 45); mighty.special.add(new ActivateP(1));
            return mighty;
            case 1: AttackAb photonic= new AttackAb("Photonic Blast", "single", "enemy", 60, 2); photonic.special.add(new ApplyShatter(50, 0, false));
            photonic.special.add(new ActivateP(2)); 
            return photonic;
            case 3: AttackAb photon= new AttackAb("Photon Barrage", "single", "enemy", 80, 3); photon.special.add(new ApplyShatter(100, 0, false));
            photon.special.add(new ActivateP(2)); 
            return photon;
            default: return null;
        }
    }
    public static Ability MakeAbKK (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb talon= new BasicAb("Morpho Punch", "single", "enemy", 45); 
            return talon;
            case 1: AttackAb bash= new AttackAb("Embiggened Bash", "single", "enemy", 80, 2); bash.special.add(new DebuffMod (22,2));
            return bash;
            case 2: BuffAb grow= new BuffAb("Grow", "self", "self", 3); grow.together=true;
            String[] bam={"Mighty Blows", "100", "616", "2", "true"}; String[][]bang=StatFactory.MakeParam(bam, null); grow.AddStatString(bang);
            String[] pew={"Focus", "100", "616", "2", "true"}; String[][] pewpew=StatFactory.MakeParam(pew, null); grow.AddStatString(pewpew);
            return grow;
            case 3: BuffAb shrink= new BuffAb("Shrink", "self", "self", 3); shrink.together=true;
            String[] bamf={"Evasion", "100", "616", "2", "true"}; String[][]banged=StatFactory.MakeParam(bamf, null); shrink.AddStatString(banged);
            String[] pewer={"Focus", "100", "616", "2", "true"}; String[][] pewpewer=StatFactory.MakeParam(pewer, null); shrink.AddStatString(pewpewer);
            return shrink;
            case 4: AttackAb smash= new AttackAb("Marvellous Finish", "single", "enemy", 120, 4); smash.special.add(new DebuffMod (22,5));
            return smash;
            default: return null;
        }
    }
    public static Ability MakeAbStorm (int counter)
    {
        switch (counter)
        {
            case 0: DebuffAb storke= new DebuffAb ("Lightning Strike", "single", "enemy", 0); 
            String[] talon= {"Shock", "100", "35", "1", "false"}; String[][] coffee=StatFactory.MakeParam(talon, null); storke.AddStatString(coffee);
            return storke;
            case 1: DebuffAb stunner= new DebuffAb ("Flash Freeze", "single", "enemy", 2); 
            String[] covfefe= {"Stun", "100", "616", "616", "false"}; String[][] press=StatFactory.MakeParam(covfefe, null); stunner.AddStatString(press);
            return stunner;
            case 2: DebuffAb hailstorm= new DebuffAb("Hail Barrage", "single", "enemy", 3); hailstorm.special.add(new Ignore("Counter", "always", 616));
            String[] snare={"Bleed", "100", "55", "2", "false"}; String[][] incoming=StatFactory.MakeParam(snare, null); hailstorm.AddStatString(incoming);
            return hailstorm;
            case 3: DebuffAb rain= new DebuffAb ("Acid Rain", "AoE", "enemy", 3); rain.special.add(new Ignore("Evade", "always", 616));
            String[] cresht= {"Poison", "100", "40", "2", "false"}; String[][] peaks=StatFactory.MakeParam(cresht, null); rain.AddStatString(peaks);
            return rain;
            case 4: AttackAb tornado= new AttackAb("Tornado", "AoE", "enemy", 100, 0); tornado.elusive=true; tornado.channelled=true; tornado.singleuse=true; tornado.dcd=1;
            return tornado;
            default: return null;
        }
    }
    public static Ability MakeAbSuperior (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb talon= new BasicAb("Talon Slash", "single", "enemy", 35); 
            String[] sorek={"Bleed", "100", "5", "2", "false"}; String [][] veda=StatFactory.MakeParam(sorek, null); talon.AddStatString(veda);
            String[] vikta={"Tracer", "100", "616", "2", "false"}; String[][] ahiid=StatFactory.MakeParam(vikta, null); talon.AddStatString(ahiid);
            return talon;
            case 1: AttackAb assault= new AttackAb("Web Assault", "single", "enemy", 75, 2); assault.special.add(new DamageCounterSimple(15, "Tracer", false, false));
            return assault;
            case 2: DebuffAb swarm=new DebuffAb ("Spider-Bot Swarm", "AoE", "enemy", 3); swarm.special.add(new DebuffMod(20));
            return swarm;
            case 3: OtherAb summon=new OtherAb("Superior Spider", "self", "self", 0); summon.special.add(new Summoning(27));
            return summon;
            default: return null;
        }
    }
    public static Ability MakeAbMiles (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb venom=new BasicAb("Venom Blast", "single", "enemy", 35); 
            String[] shockinghuh= {"Shock", "100", "5", "3", "false"}; String[][] hahadie=StatFactory.MakeParam(shockinghuh, null); venom.AddStatString(hahadie);
            return venom;
            case 1: BuffAb camo= new BuffAb("Spider Camouflage", "self", "self", 2); camo.together=true;
            String[] swift={"Invisible", "100", "616", "2", "true"}; String[][] sand=StatFactory.MakeParam(swift, null); camo.AddStatString(sand);
            String[] taylor={"Evasion", "100", "616", "2", "true"}; String[][] bold=StatFactory.MakeParam(taylor, null); camo.AddStatString(bold);
            return camo;
            case 2: AttackAb sneak= new AttackAb("Sneak Attack", "single", "enemy", 90, 3);
            sneak.special.add(new DamageCounterSimple(20, "Invisible", false, true));
            return sneak;
            case 3: AttackAb thing= new AttackAb("Anything a Spider Can", "single", "enemy", 90, 3);
            thing.special.add(new DebuffMod (19));
            return thing;
            case 4: AttackAb ultimate= new AttackAb("Ultimate Spider-Man", "single", "enemy", 110, 5);
            ultimate.special.add(new DamageCounterSimple(20, "Invisible", false, true)); ultimate.special.add(new DamageCounterSimple(20, "Shock", false, false));
            return ultimate;
            default: return null;
        }
    }
    public static Ability MakeAbSpidey (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb swing= new BasicAb ("Web Swing", "single", "enemy", 40);
            return swing;
            case 1: DebuffAb quip= new DebuffAb ("Distracting Quip", "single", "enemy", 0); quip.special.add(new Ignore("Neutralise", "always", 616)); quip.ignore=true;
            String[] braindrain= {"Daze", "500", "616", "1", "false"}; String[][] scat=StatFactory.MakeParam(braindrain, null); quip.AddStatString(scat); 
            return quip;
            case 3: AttackAb barrage= new AttackAb("Web Barrage", "single", "enemy", 90, 3); 
            String[] breaker={"Snare", "100", "616", "2", "false"}; String[][] gatling=StatFactory.MakeParam(breaker, null); barrage.AddStatString(gatling);
            return barrage;
            default: return null;
        }
    }
    public static Ability MakeAbVenom (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb slash= new BasicAb ("Deadly Maw", "single", "enemy", 35); 
            String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
            return slash;
            case 1: AttackAb food= new AttackAb ("Play With Your Food", "single", "enemy", 70, 2); 
            String[] gatman= {"Snare", "100", "616", "1", "false"}; String[][] dummy= StatFactory.MakeParam(gatman, null); food.AddStatString(dummy);
            return food;
            case 3: AttackAb come= new AttackAb ("Come To Me", "single", "enemy", 90, 3); come.special.add(new DamageCounterSimple (20, "Snare", false, false));
            return come;
            case 4: AttackAb devour= new AttackAb ("Devour", "single", "enemy", 90, 4); devour.special.add(new ActivatePassive());
            return devour;
            default: return null;
        }
    }
    public static Ability MakeAbOGVenom (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb whip =new BasicAb ("Tendril Whip", "single", "enemy", 40); 
            return whip; 
            case 1: AttackAb maw= new AttackAb ("Maul", "single", "enemy", 40, 2);
            String[] gorr= {"Bleed", "100", "25", "2", "false"}; String[][] lightning=StatFactory.MakeParam(gorr, null); maw.AddStatString(lightning);
            return maw;
            case 4: AttackAb venom= new AttackAb ("We Are Venom", "single", "enemy", 110, 4); 
            String[] bigamy= {"Terror", "100", "616", "1", "false"}; String[][] moron= StatFactory.MakeParam(bigamy, null); 
            venom.AddStatString(moron);
            return venom;
            default: return null;
        }
    }
    public static Ability MakeAbWolvie (int counter)  
    {
        switch (counter)
        {
            case 0: BasicAb slash= new BasicAb ("X-Slash", "single", "enemy", 35); 
            String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
            return slash;
            case 1: BasicAb punch =new BasicAb ("Primal Punch", "single", "enemy", 45); 
            return punch; 
            default: return null;
        }
    }
    public static Ability MakeAbX23 (int counter)  
    {
        switch (counter)
        {
            case 0: BasicAb slash= new BasicAb("Precision Slash", "single", "enemy", 35); 
            String[] precision= {"Precision", "100", "616", "2", "true"}; String[][] toret=StatFactory.MakeParam(precision, null); slash.AddStatString(toret); 
            return slash;
            case 1: AttackAb out= new AttackAb ("Bleed Them Out", "single", "enemy", 35, 3); 
            String[] bleed= {"Bleed", "100", "35", "2", "false"}; String[][] bleeding=StatFactory.MakeParam(bleed, null); out.AddStatString(bleeding);
            return out;
            case 4: AttackAb triple= new AttackAb("Triple Slash", "single", "enemy", 30, 4, 2); triple.special.add (new Chain(true, triple));
            return triple;
            default: return null;
        }
    }
    public static Ability MakeAbDrax (int counter, boolean copy) 
    {
        switch (counter)
        {
            case 0: BasicAb headbutt =new BasicAb ("Headbutt", "single", "enemy", 40); 
            return headbutt; 
            case 1: OtherAb twins= new OtherAb("Twin Blades", "self", "self", 0); 
            if (copy==false)
            {
                twins.special.add(new ActivateP()); twins.special.add (new Update (13));
            }
            return twins; 
            case 2: 
            AttackAb knife= new AttackAb ("Knife Slash", "single", "enemy", 35, 3); 
            if (copy==true)
            {
                String[] bloody= {"Bleed", "100", "35", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); knife.AddStatString(gore);
            }
            else
            knife.special.add (new DebuffMod (13));
            return knife; 
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "AoE", "enemy", 70, 3); 
            return dice; 
            default: return null;
        }
    }  
    public static Ability MakeAbOGDrax (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb blast= new BasicAb ("Cosmic Blast", "single", "enemy", 45); blast.special.add(new DamageCounter ("Obsession", false, 5, false, false));
            return blast;  
            case 1: BasicAb blow= new BasicAb ("Finishing Blow", "single", "enemy", 40); blow.special.add(new DamageCounterRemove ("Obsession", false, 40, false));
            return blow;
            default: return null;
        }
    }
    public static Ability MakeAbNickJr (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb slam= new BasicAb ("Body Slam", "single", "enemy", 45);
            return slam;
            case 1: BasicAb slash= new BasicAb ("Combat Knife", "single", "enemy", 35); 
            String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] red=StatFactory.MakeParam(bleed, null); slash.AddStatString(red);
            return slash;
            case 2: BuffAb cloak= new BuffAb ("Cloaking Tech", "self", "self", 3); cloak.together=true;
            String[] tech={"Invisible", "100", "616", "2", "true"}; String[][] redo=StatFactory.MakeParam(tech, null); 
            String[] fast= {"Speed", "100", "616", "2", "true"}; String[][] sped=StatFactory.MakeParam(fast, null); 
            cloak.AddStatString(redo); cloak.AddStatString(sped);
            return cloak;
            case 3: OtherAb kill= new OtherAb ("Kill Mode", "self", "self", 0); kill.singleuse=true; kill.unbound=true; kill.special.add(new ActivateP());
            return kill;
            case 4: AttackAb poke= new AttackAb ("Double Tap", "single", "enemy", 40, 2, 1); poke.special.add (new Ignore ("Missed", "passive", 616));
            return poke;
            default: return null;
        }
    }
    public static Ability MakeAbNickSr (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb frag= new BasicAb ("Frag Grenade", "single", "enemy", 45); frag.special.add (new Ricochet (500));
            return frag;
            case 1: BasicAb smg= new BasicAb ("SMG", "AoE", "enemy", 35);
            return smg;
            case 2: BuffAb cache= new BuffAb ("Hidden Weapons Cache", "single", "ally inclusive", 0); 
            String[] sword= {"Intensify", "100", "15", "1", "knull"}; 
            String[][] redo=StatFactory.MakeParam(sword, null); cache.AddStatString(redo);
            return cache;
            case 4: AttackAb air= new AttackAb ("Airstrike", "AoE", "enemy", 90, 5);
            air.special.add(new Ignore ("Missed", "always", 616));
            return air;
            default: return null;
        }
    }
    public static Ability MakeAbStarLord (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb talon= new BasicAb ("Jet Slam", "single", "enemy", 45);
            return talon;
            case 1: HealAb pump= new HealAb ("Pump It Up", "AoE", "ally inclusive", 0); String[] dance= {"Recovery", "500", "10", "2", "false"};
            String[][] redo=StatFactory.MakeParam(dance, null); pump.AddStatString(redo);
            return pump;
            case 3: AttackAb clarice= new AttackAb ("Clarice", "single", "enemy", 90, 3); 
            String[] one={"Burn", "100", "10", "2", "false"}; String[] two={"Stun", "100", "616", "616", "false"}; 
            String[][] stat1=StatFactory.MakeParam(one, null); String[][] stat2=StatFactory.MakeParam(two, null);
            clarice.special.add(new ChooseStat (stat1, stat2, null));
            return clarice;
            case 4: AttackAb terry= new AttackAb ("Terry", "single", "enemy", 90, 3); 
            String[] uno={"Wound", "100", "616", "1", "false"}; String[] dos={"Disrupt", "100", "616", "1", "false"}; 
            String[][] stats=StatFactory.MakeParam(uno, null); String[][] statistic=StatFactory.MakeParam(dos, null);
            terry.special.add(new ChooseStat (stats, statistic, null));
            return terry;
            default: return null;
        }
    }
    public static Ability MakeAbBucky (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb shot= new BasicAb ("Precise Shot", "single", "enemy", 45);
            return shot;
            case 1: DebuffAb gren= new DebuffAb ("Grenade Lob", "single", "enemy", 0); 
            String[] explode= {"Countdown", "100", "55", "2", "false"}; String[] ricochet= {"Ricochet"}; 
            String[][] count=StatFactory.MakeParam(explode, ricochet); gren.AddStatString(count);
            return gren; 
            case 2: AttackAb expert= new AttackAb("Expert Marksman", "single", "enemy", 35, 2); 
            String[] decoy={"Countdown", "100", "35", "2", "false"}; String[][] countdown=StatFactory.MakeParam(decoy, null);
            expert.AddStatString(countdown); 
            String[] blood= {"Countdown"};
            String[] privy={"any"}; 
            expert.special.add(new Extend (500, 2, "random", blood, privy, 1, false, true, false));
            return expert;
            case 3: OtherAb det= new OtherAb("Detonator", "single", "enemy", 2); det.special.add(new Activate (false, "Countdown", 60));
            det.special.add(new Ignore ("always", "inescapable", 616)); 
            return det;
            default: return null;
        }
    }
    public static Ability MakeAbFalc (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb bomb= new BasicAb ("Divebomb", "single", "enemy", 35); 
            String[] weak= {"Weakness", "100", "10", "1", "false"}; String[][] redo=StatFactory.MakeParam(weak, null); 
            bomb.AddStatString(redo);
            return bomb;
            case 3: DebuffAb eagle= new DebuffAb ("Eagle Eyed", "single", "enemy", 3); eagle.together=true;
            String[] targ={"Target", "100", "20", "1", "false"}; String[][] target=StatFactory.MakeParam(targ, null); 
            String[] sexy= {"Neutralise", "100", "616", "1", "false"}; String[][] charming=StatFactory.MakeParam(sexy, null);
            eagle.AddStatString (target); eagle.AddStatString (charming);
            return eagle;
            case 4: AttackAb swarm= new AttackAb ("Bird Swarm", "AoE", "enemy", 70, 4); 
            String[] scary={"Daze", "50", "616", "1", "false"}; String[][] fred=StatFactory.MakeParam(scary, null); 
            swarm.AddStatString(fred); 
            return swarm;
            default: return null;
        }
    }
    public static Ability MakeAbCap (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb toss= new BasicAb ("Shield Throw", "single", "enemy", 40); toss.special.add(new Ricochet (500)); 
            return toss;
            case 1: AttackAb bash= new AttackAb ("Shield Bash", "single", "enemy", 90, 3); 
            String[] stunner={"Stun", "100", "616", "616", "false"}; String[][] redo=StatFactory.MakeParam(stunner, null);
            bash.AddStatString (redo); 
            return bash;
            case 3: DefAb star= new DefAb ("Star Spangled Avenger", "self", "self", 3); String[] banner={"Taunt", "500", "616", "1", "true"};
            String[][] cut=StatFactory.MakeParam(banner, null); star.AddStatString(cut); 
            String[] whelmed= {"Resistance", "500", "10", "1", "true"}; String[][] viper=StatFactory.MakeParam(whelmed, null);
            star.AddStatString(viper);
            return star;
            case 4: HealAb lib= new HealAb ("Sentinel of Liberty", "AoE", "ally inclusive", 4); lib.special.add(new Confidence(500, 30)); lib.channelled=true;
            return lib;
            default: return null;
        }
    }
    public static Ability MakeAbWM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pc= new BasicAb ("Plasma Cutter", "single", "enemy", 45); 
            return pc;
            case 1: BasicAb gat= new BasicAb ("Gatling Guns", "AoE", "enemy", 30); 
            return gat;
            case 3: AttackAb ft= new AttackAb ("Flamethrower", "single", "enemy", 80, 3); 
            String[] pyre={"Burn", "100", "20", "1", "false"}; String[][] redo=StatFactory.MakeParam(pyre, null);
            ft.AddStatString (redo); 
            return ft;
            case 4: AttackAb army= new AttackAb ("Army of One", "single", "enemy", 120, 4); 
            String[] gertrude= new String[1]; gertrude[0]="Buffs"; army.special.add (new BeforeNullify (100, 616, "all", "any", false, true, gertrude)); 
            return army;
            default: return null;
        }
    }
    public static Ability MakeAbIM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rep= new BasicAb ("Repulsor Blast", "single", "enemy", 45); 
            return rep;
            case 1: BuffAb boost= new BuffAb ("Power Boost", "self", "self", 0); 
            String[] Antidisestablishmentarianism={"Intensify", "500", "30", "2", "true"}; String[][] redo=StatFactory.MakeParam(Antidisestablishmentarianism, null);
            String[] rewind={"Debilitate", "500", "15", "2", "true"}; String[][] overpower=StatFactory.MakeParam(rewind, null);
            boost.AddStatString (redo); boost.AddStatString (overpower); boost.special.add (new SelfDMG (45));
            return boost;
            case 2: HealAb recharge= new HealAb ("Recharge", "self", "self", 3); 
            recharge.special.add (new Confidence (500, 30)); recharge.special.add (new Purify (500, 1, "random", "any", true, true));
            return recharge;
            case 4: AttackAb uni= new AttackAb ("Unibeam", "single", "enemy", 100, 3); uni.special.add (new Nullify (500, 1, "random", "Intensify", true, true));
            return uni;
            default: return null;
        }
    }
    public static Ability MakeAbPun (int counter)
    {
        switch (counter)
        {
            case 0: AttackAb molotov= new AttackAb ("Molotov Cocktail", "single", "enemy", 75, 3); 
            String[] rend={"Burn", "50", "10", "2", "false"}; String[][] fry=StatFactory.MakeParam(rend, null); 
            String[] bright= {"Disrupt", "50", "616", "1", "false"}; String[][] light=StatFactory.MakeParam(bright, null);
            molotov.AddStatString (fry); molotov.AddStatString (light); 
            return molotov;
            case 1: AttackAb punished= new AttackAb ("Punished", "single", "enemy", 75, 3); 
            String[] shred= {"Bleed", "50", "10", "2", "false"}; String[][] heat=StatFactory.MakeParam(shred, null); 
            String[] dead= {"Neutralise", "50", "616", "1", "false"}; String[][] head=StatFactory.MakeParam(dead, null);
            punished.AddStatString (heat); punished.AddStatString(head); 
            return punished;
            case 2: AttackAb supfire= new AttackAb ("Suppressing Fire", "AoE", "enemy", 65, 3); 
            return supfire;
            case 3: BuffAb punwep= new BuffAb ("Weapons Expert", "self", "self", 0); 
            String[] asunder={"FocusE", "100", "616", "2", "true"}; String[][] me=StatFactory.MakeParam(asunder, null); 
            String[] evie={"Intensify", "100", "15", "2", "true"}; String[][] redo=StatFactory.MakeParam(evie, null);             
            punwep.AddStatString (redo); punwep.AddStatString (me); 
            return punwep;
            case 4: AttackAb retri= new AttackAb ("Retribution", "single", "enemy", 120, 5);
            String [] naught= {"Wound", "50", "616", "1", "false"}; String[][] brought=StatFactory.MakeParam(naught, null);
            retri.AddStatString(brought);
            return retri;
            default: return null;
        }
    }
    public static Ability MakeAbGam (int counter, boolean copy) 
    {
        switch (counter)
        {
            case 0: BasicAb concstr= new BasicAb ("Pommel Smash", "single", "enemy", 45); 
            String[] hug={"Intensify", "100", "5", "2", "true"}; String[][] redo=StatFactory.MakeParam(hug, null); 
            concstr.AddStatString(redo);
            return concstr;
            case 3: 
            AttackAb finese= new AttackAb ("Bladed Finese", "single", "enemy", 80, 3); 
            if (copy==true)
            {
                String[] mello={"Bleed", "50", "20", "1", "false"}; String[][] baby=StatFactory.MakeParam(mello, null);  finese.AddStatString(baby);
            }
            else
            finese.special.add (new DebuffMod (2, 1));
            return finese;
            case 4: 
            AttackAb assass= new AttackAb ("Assassinate", "single", "enemy", 100, 5); 
            if (copy==true)
            {
                String[] near={"Bleed", "50", "30", "1", "false"}; String[][] fin=StatFactory.MakeParam(near, null); assass.AddStatString(fin); 
            }
            else
            assass.special.add (new DebuffMod (2, 2));
            return assass;
            default: return null;
        }
    }
    public static Ability MakeAbMK (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Crescent Dart", "single", "enemy", 35); String[] despair={"Bleed", "100", "15", "1", "false"};
            String[][] redo=StatFactory.MakeParam(despair, null);
            cdart.AddStatString(redo); 
            return cdart; 
            case 1: BasicAb mbarr= new BasicAb("Mooncopter Barrage", "AoE", "enemy", 30);   
            return mbarr; 
            case 2: DefAb lunarp= new DefAb ("Lunar Protector", "single", "ally exclusive", 2); 
            String[] gloom={"Protect", "500", "616", "1", "true"}; String[][] chic=StatFactory.MakeParam(gloom, null); lunarp.AddStatString(chic); 
            String[] doom= {"Evade", "500", "616", "616", "true"}; String[][] chicken=StatFactory.MakeParam(doom, null); lunarp.AddStatString(chicken);
            return lunarp; 
            case 4: AttackAb Khonshu= new AttackAb ("Fist of Khonshu", "single", "enemy", 110, 4); 
            String[] loss={"Stun", "100", "616", "616", "false"}; String[][] crowd=StatFactory.MakeParam(loss, null); Khonshu.AddStatString (crowd); 
            return Khonshu;
            default: return null;
        }
    }    
}