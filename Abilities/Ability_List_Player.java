package myMarvelcardgamepack;

/**
 * @author Timothy Landers
 * Date of creation: 25/7/22
 * Purpose: Creates/lists all character abilities.
 */
public class Ability_List_Player
{ 
    /**
    * Returns the given ability for the given hero. Whether the ability is being copied or not must be specified.
    * <p> This is because copied abilities are made differently, due to the implentation of some heroes' abilities being intertwined with their passives.
    * @param index The index of the hero whose abilities are being retrieved.
    * @param counter The number of the ability being retrieved, e.g. the hero's 4th ability. The numbering is identical to the order abilities are listed on their cards.
    * @param copy Whether the ability is being made for the original hero or being copied (e.g. by Rogue).
    * @return The requested ability.
    * @see Ability
    */
    public static Ability GetAb (int index, int counter, boolean copy) //copy is for rogue and super adaptoid to copy abs
    {
        //abs construction: String name, String type, String friendly, int dmg, int cooldown
        //check MakeStatString for stateff array format
        //friendly means ally inc, ally exc, enemy, both, either, or self 
        //type is single, self, multitarg, random, or aoe 
        switch (index) //since making a giant array of dozens of elements was very slow, this should be faster, albeit still long
        {
            //2.0: Original
            case 1: return MakeAbMK(counter); 
            case 2: return MakeAbGam(counter, copy); 
            case 3: return MakeAbPun(counter); 
            case 4: return MakeAbIM(counter); 
            case 5: return MakeAbWM(counter); 
            case 6: return MakeAbCap(counter); 
            case 7: return MakeAbFalc(counter); 
            case 8: return MakeAbBucky(counter); 
            case 9: return MakeAbStarLord(counter); 
            case 10: return MakeAbNickSr(counter); 
            case 11: return MakeAbNickJr(counter); 
            case 12: return MakeAbOGDrax(counter); 
            case 13: return MakeAbDrax(counter, copy); 
            case 14: return MakeAbX23(counter);
            case 15: return MakeAbWolvie(counter); 
            case 16: return MakeAbOGVenom(counter); 
            case 17: return MakeAbVenom(counter); 
            case 18: return MakeAbSpidey(counter); 
            case 19: return MakeAbMiles(counter); 
            case 20: return MakeAbSuperior(counter); 
            case 21: return MakeAbStorm(counter); 
            case 22: return MakeAbKK(counter); 
            case 23: return MakeAbCM(counter); 
            case 24: return MakeAbBinary(counter); 
            case 25: return MakeAbFlash(counter); 
            case 26: return MakeAbMODORK(counter); 
            case 27: return MakeAbUltron(counter); 
            case 28: return MakeAbDOOM(counter); 
            case 29: return MakeAbStrange(counter); 
            case 30: return MakeAbBrawn(counter, copy);
            case 31: return MakeAbHulk(counter);
            case 32: return MakeAbBB(counter, copy);
            case 33: return MakeAbDeadpool(counter);
            case 34: return MakeAbSkull(counter);
            case 35: return MakeAbCain(counter);
            //2.1: Sinister 6
            case 36: return MakeAbVulture(counter);
            case 37: return MakeAbMysterio(counter);
            case 38: return MakeAbOck(counter);
            case 39: return MakeAbElectro(counter);
            case 40: return MakeAbSandy(counter);
            case 41: return MakeAbRhino(counter);
            //2.5: Thanos Arrives
            case 61: return MakeAbThanos(counter);
            case 62: return MakeAbGauntlet(counter, copy);
            case 63: return MakeAbCorvus(counter);
            case 64: return MakeAbProxima(counter);
            case 65: return MakeAbSupergiant(counter);
            case 66: return MakeAbDwarf(counter);
            case 67: return MakeAbMaw(counter, copy);
            //2.6: U-Foes
            case 68: return MakeAbVector(counter, copy);
            case 69: return MakeAbXRay(counter);
            case 70: return MakeAbIronclad(counter);
            case 71: return MakeAbVapor(counter);
            //2.7: Thunderbolts
            case 72: return MakeAbZemo(counter);
            case 73: return MakeAbMimi(counter);
            case 74: return MakeAbSongbird(counter, copy);
            case 75: return MakeAbMoonstone(counter);
            case 76: return MakeAbSpeedball(counter);
            case 77: return MakeAbPenance(counter);
            case 78: return MakeAbRulk(counter);
            case 79: return MakeAbRadioMan(counter);
            case 80: return MakeAbScarecrow(counter);
            //2.8: Defenders
            case 81: return MakeAbDD(counter);
            case 82: return MakeAbFist(counter);
            case 83: return MakeAbCage(counter);
            case 84: return MakeAbNamor(counter);
            case 85: return MakeAbSurfer(counter);
            //2.9: Fearsome Foes
            case 86: return MakeAbKraven(counter);
            case 87: return MakeAbLizard(counter);
            case 88: return MakeAbScorpion(counter);
            case 89: return MakeAbHydro(counter);
            case 90: return MakeAbCabbage(counter);
            case 91: return MakeAbGobby(counter);
            case 92: return MakeAbRoblin(counter);
            case 93: return MakeAbHobgobby(counter);
            case 94: return MakeAbUrich(counter);
            //2.10: Marvellous Mutants
            case 95: return MakeAbFrost(counter);
            case 96: return MakeAbDiamond(counter);
            case 97: return MakeAbAngel(counter);
            case 98: return MakeAbAA(counter);
            case 99: return MakeAbColossus(counter);
            case 100: return MakeAbElixirG(counter);
            case 101: return MakeAbElixirB(counter);
            case 102: return MakeAbGambit(counter, copy);
            case 103: return MakeAbCrawler(counter);
            case 104: return MakeAbBishop(counter);
            case 105: return MakeAbImmortal(counter);
            //default statement
            default: System.out.println ("Problem getting hero abilities");
        }
        return null;
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
    //2.10: Marvellous Mutants
    public static Ability MakeAbImmortal (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb ram= new BasicAb("Punch", "single", "enemy", 40);
            String[] shazam={"Provoke", "50", "616", "1", "false"}; String[][] shamaz=StatFactory.MakeParam(shazam, null); ram.AddStatString(shamaz);
            return ram;
            case 1: DefAb heal= new DefAb ("Human Shield", "single", "ally exclusive", 4); 
            String[] protect={"Protect", "100", "616", "1", "true"}; String[][] serve=StatFactory.MakeParam(protect, null); heal.AddStatString(serve);
            return heal;
            case 2: DefAb stale= new DefAb("Aggravating Insults", "self", "self", 4);
            String[] bleed={"Taunt", "100", "616", "1", "true"}; String[][] rb=StatFactory.MakeParam(bleed, null); stale.AddStatString(rb);
            return stale; 
            default: return null;
        }
    }
    public static Ability MakeAbBishop (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pew= new BasicAb("Laser Shot", "single", "enemy", 45);
            return pew;
            case 1: BasicAb zen= new BasicAb("Careful Release", "single", "enemy", 40); zen.special.add(new DebuffMod(104, 1));
            zen.desc="Consume 40 Energy to do +40 damage.";
            return zen;
            case 4: AttackAb yatta= new AttackAb("Diffusion Wave", "single", "enemy", 60, 3); yatta.special.add(new ApplyShatter(500, 0, false, false, false));
            yatta.special.add(new DebuffMod(104, 2)); yatta.desc="Do extra damage equal to Energy and lose all Energy.";
            return yatta;
            default: return null;
        }
    }
    public static Ability MakeAbCrawler (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Sabre Slash", "single", "enemy", 35); 
            String[] despair={"Bleed", "100", "15", "1", "false"}; String[][] redo=StatFactory.MakeParam(despair, null); cdart.AddStatString(redo); 
            return cdart;
            case 1: BuffAb gard= new BuffAb("Evasive Action", "self", "self", 0);
            String[] siren={"Evasion", "100", "616", "2", "true"}; String[][] head=StatFactory.MakeParam(siren, null); gard.AddStatString(head);
            String[] moren={"Intensify", "100", "10", "2", "true"}; String[][] dwarf=StatFactory.MakeParam(moren, null); gard.AddStatString(dwarf);
            return gard;
            case 2: AttackAb behind= new AttackAb("Behind You", "single", "enemy", 70, 2); behind.together=true;
            String[] word={"Provoke", "100", "616", "1", "false"}; String[][] association=StatFactory.MakeParam(word, null); behind.AddStatString(association);
            String[] house={"Evasion", "100", "616", "1", "true"}; String[][] die=StatFactory.MakeParam(house, null); behind.AddStatString(die);
            return behind;
            case 4: AttackAb stab= new AttackAb("Bamf Barrage", "single", "enemy", 25, 5, 2); 
            String[] length={"Bleed", "100", "15", "1", "false"}; String[][] girth=StatFactory.MakeParam(length, null); stab.AddStatString(girth);
            return stab;
            default: return null;
        }
    }
    public static Ability MakeAbGambit (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb pow= new BasicAb("Bo Staff Slam", "single", "enemy", 45);
            return pow;
            case 1: OtherAb trigger= new OtherAb("Stacked Deck", "self", "self", 3); trigger.unbound=true;
            String[] sour={"Empower", "500", "616", "2", "true"}; String[][] burn=StatFactory.MakeParam(sour, null); trigger.AddStatString(burn);
            trigger.desc="Gain a(n) Empower for 2 use(s), making abilities apply Burn: 5 for 1 turn(s).";
            return trigger;
            case 3: AttackAb three= new AttackAb("Three of a Kind", "single", "enemy", 5, 3, 2); 
            if (copy==false)
            {
                three.special.add(new DebuffMod(102));
                three.desc="100% chance to apply Countdown: 25 for 1 turn(s). ";
            }
            else
            {
                String[] ctd={"Countdown", "100", "25", "1", "false"}; String[][] down=StatFactory.MakeParam(ctd, null); three.AddStatString(down);
            }
            return three;
            case 4: AttackAb full= new AttackAb("Full House", "AoE", "enemy", 30, 3);
            if (copy==false)
            {
                full.special.add(new DebuffMod(102));
                full.desc="100% chance to apply Countdown: 25 for 1 turn(s). ";
            }
            else
            {
                String[] ctd={"Countdown", "100", "25", "1", "false"}; String[][] down=StatFactory.MakeParam(ctd, null); full.AddStatString(down);
            }
            return full;
            default: return null;
        }
    }
    public static Ability MakeAbElixirB (int counter)
    {
        switch (counter)
        {
            case 0: DebuffAb a= new DebuffAb("Atrophy", "single", "enemy", 0);
            String[] bing={"Weakness", "100", "40", "1", "false"}; String[][] bong=StatFactory.MakeParam(bing, null); a.AddStatString(bong);
            return a;
            case 1: DebuffAb w= new DebuffAb("Necrosis", "single", "enemy", 0); 
            String[] wide={"Wither", "100", "25", "2", "false"}; String[][] wider=StatFactory.MakeParam(wide, null); w.AddStatString(wider);
            return w;
            case 2: DebuffAb d= new DebuffAb("Decay", "single", "enemy", 0);
            String[] deb={"Debilitate", "100", "15", "2", "false"}; String[][] debby=StatFactory.MakeParam(deb, null); d.AddStatString(debby);
            return d;
            case 3: OtherAb f= new OtherAb("Fester", "single", "enemy", 0); f.special.add(new Amplify(500, "any", "damaging debuffs", 20, true));
            return f;
            case 4: OtherAb t= new OtherAb("Focus", "self", "self", 0); t.channelled=true; t.special.add(new Transformation(100, false, false));
            return t;
            default: return null;
        }
    }
    public static Ability MakeAbElixirG (int counter)
    {
        switch (counter)
        {
            case 0: HealAb touch=new HealAb("Healing Touch", "single", "ally inclusive", 0); touch.special.add(new Mend(500, 40));
            return touch;
            case 1: HealAb grow= new HealAb("Healing Aura", "AoE", "ally inclusive", 3);
            String[] healme= {"Regen", "500", "30", "2", "knull"}; String[][] stealth=StatFactory.MakeParam(healme, null); grow.AddStatString(stealth);
            return grow;
            case 2: OtherAb transfer= new OtherAb("Life Transfer", "multitarget", "ally inclusive", 3); transfer.special.add(new MultiMod(100));
            transfer.desc="The first target sacrifices 40 health and the second target regains 80 health.";
            return transfer;
            case 3: HealAb rez= new HealAb("Resurrect", "rez", "ally inclusive", 4); rez.channelled=true; 
            rez.special.add(new DebuffMod(100)); rez.desc="Resurrects the target with 100 health. If Elixir is targeted, he will instead Resurrect after his next death.";
            return rez;
            case 4: OtherAb h= new OtherAb("Death Touch", "single", "enemy", 0); h.special.add(new ActivatePassive(100)); h.special.add(new Transformation(101, false, false));
            h.desc="The target loses half of their health (max 150).";
            return h;
            default: return null;
        }
    }
    public static Ability MakeAbColossus (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pow= new BasicAb("Steel Fists", "single", "enemy", 45);
            return pow;
            case 1: AttackAb a= new AttackAb("Colossal Punch", "single", "enemy", 90, 3);
            String[] nanny={"Disorient", "100", "616", "1", "false"}; String[][] mcphee=StatFactory.MakeParam(nanny, null); a.AddStatString(mcphee);
            return a;
            case 3: DefAb stale= new DefAb("Stalwart Defender", "self", "self", 3);
            String[] bleed={"Taunt", "500", "616", "1", "true"}; String[][] rb=StatFactory.MakeParam(bleed, null); stale.AddStatString(rb);
            String[] poison={"Resistance", "500", "20", "1", "true"}; String[][] rp=StatFactory.MakeParam(poison, null); stale.AddStatString(rp);
            String[] bulwark={"Bulwark", "500", "616", "2", "true"}; String[][] blame=StatFactory.MakeParam(bulwark, null); stale.AddStatString(blame);
            return stale;
            case 4: OtherAb ball= new OtherAb ("Fastball Special", "single", "ally exclusive", 5); ball.channelled=true; ball.special.add(new BonusTurn());
            return ball;
            default: return null;
        }
    }
    public static Ability MakeAbAA (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pow= new BasicAb("Razor Wing Strike", "single", "enemy", 35);
            return pow;
            case 3: DebuffAb fling= new DebuffAb("Feather Fling", "single", "enemy", 4); fling.multiuse=2;
            String[] bleed={"Bleed", "500", "15", "2", "false"}; String[][] rb=StatFactory.MakeParam(bleed, null); 
            String[] poison={"Poison", "500", "15", "2", "false"}; String[][] rp=StatFactory.MakeParam(poison, null);
            fling.special.add(new ChooseStat(rb, rp, null));
            return fling;
            case 4: AttackAb barrage= new AttackAb("Feather Barrage", "single", "enemy", 15, 4, 2);
            return barrage;
            default: return null;
        }
    }
    public static Ability MakeAbAngel (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pow= new BasicAb("Swooping Blow", "single", "enemy", 45);
            return pow;
            case 1: AttackAb store= new AttackAb("Wing Swipe", "single", "enemy", 70, 2);
            String[] nanny={"Provoke", "100", "616", "1", "false"}; String[][] mcphee=StatFactory.MakeParam(nanny, null); store.AddStatString(mcphee);
            return store;
            case 2: DefAb heal= new DefAb ("Guardian Angel", "single", "ally exclusive", 3); 
            String[] protect={"Protect", "500", "616", "1", "true"}; String[][] serve=StatFactory.MakeParam(protect, null); heal.AddStatString(serve);
            return heal;
            case 4: HealAb special= new HealAb("Blood Transfusion", "single", "ally exclusive", 0); special.multiuse=2; special.special.add(new ActivatePassive(97));
            special.desc="Remove a Bleed from self to apply Mend: 35.";
            return special;
            default: return null;
        }
    }
    public static Ability MakeAbDiamond (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pow= new BasicAb("Glamorous Kick", "single", "enemy", 50);
            return pow;
            case 4: OtherAb pootis= new OtherAb("Mind Over Matter", "self", "self", 0); pootis.special.add(new Transformation(95, false, false));
            String[] e={"Evasion", "500", "616", "1", "true"}; String[][] everyman=StatFactory.MakeParam(e, null); pootis.AddStatString(everyman);
            return pootis;
            default: return null;
        }
    }
    public static Ability MakeAbFrost (int counter)
    {
        switch (counter)
        {
            case 0: OtherAb hail= new OtherAb ("Imperious Command", "single", "enemy", 0); hail.control=true; hail.elusive=true;
            hail.special.add(new Assist(true, 1, 10, true, 0, null, 500, false));
            return hail;
            case 1: OtherAb emp= new OtherAb("Psychic Meddling", "single", "enemy", 0); emp.control=true; emp.elusive=true;
            String[] well={"Empower", "500", "616", "1", "false"}; String[][] youknow=StatFactory.MakeParam(well, null); emp.AddStatString(youknow);
            emp.desc="Apply a(n) Empower for 1 use(s), granting -100% status chance.";
            return emp;
            case 2: OtherAb ass= new OtherAb("Mental Assault", "single", "enemy", 3); ass.control=true;
            ass.special.add(new Assist(true, 1, 10, true, 0, null, 500, false)); ass.special.add(new Assist(false, 1, 10, true, 0, null, 500, false));
            String[] story={"Daze Effect", "500", "616", "1", "false"}; String[][] mark=StatFactory.MakeParam(story, null); ass.AddStatString(mark);
            return ass;
            case 3: OtherAb queen= new OtherAb("White Queen", "single", "ally exclusive", 4); queen.control=true; queen.special.add(new ReduceCD(false, 1));
            return queen;
            case 4: OtherAb trans= new OtherAb("Girl's Best Friend", "self", "self", 0); trans.special.add(new Transformation(96, false, false));
            //String[] verde={"Taunt", "500", "616", "1", "true"}; String[][] mordo=StatFactory.MakeParam(verde, null); trans.AddStatString(mordo);
            return trans;
            default: return null;
        }
    }
    //2.9: Fearsome Foes of Spider-Man
    public static Ability MakeAbUrich (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Flying Punch", "single", "enemy", 45); 
            return cdart;
            case 1: AttackAb bat= new AttackAb("Razor Bats", "single", "enemy", 15, 3, 1);
            String[] sting= {"Bleed", "100", "15", "2", "false"}; String[][] sing=StatFactory.MakeParam(sting, null); bat.AddStatString(sing);
            return bat;
            case 2: AttackAb darn= new AttackAb ("Lunatic Laugh", "random Bleed", "enemy", 40, 3); darn.special.add(new Amplify(100, "Bleed", "Debuffs", 20, true));
            return darn;
            case 3: AttackAb sword= new AttackAb("Flaming Sword", "single", "enemy", 30, 3); sword.together=true;
            String[] run={"Burn", "100", "30", "2", "false"}; String[][] hide=StatFactory.MakeParam(run, null); sword.AddStatString(hide);
            String[] lope={"Wound", "100", "616", "2", "false"}; String[][] subprime=StatFactory.MakeParam(lope, null); sword.AddStatString(subprime);
            return sword;
            default: return null;
        }
    }
    public static Ability MakeAbHobgobby (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Glider Slam", "single", "enemy", 40); 
            return cdart;
            case 1: AttackAb lol= new AttackAb("Shock Gloves", "single", "enemy", 90, 3);
            String[] frozen={"Blind", "100", "616", "1", "false"}; String[][] intime=StatFactory.MakeParam(frozen, null); lol.AddStatString(intime);
            String[] easygame={"Provoke", "100", "616", "1", "false"}; String[][] easylife=StatFactory.MakeParam(easygame, null); lol.AddStatString(easylife);
            return lol;
            case 2: AttackAb jack= new AttackAb("Explosive Jack-o'-Lantern", "single", "enemy", 90, 3);
            String[] target={"Target", "100", "10", "2", "false"}; String[][] haha=StatFactory.MakeParam(target, null); jack.AddStatString(haha);
            return jack;
            case 3: OtherAb french= new OtherAb("Villain Franchisor", "self", "self", 0); french.special.add(new Summoning(14, 15, 16)); french.special.add(new SelfDMG(60, true));
            return french;
            default: return null;
        }
    }
    public static Ability MakeAbRoblin (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Skewer", "single", "enemy", 35); 
            String[] despair={"Bleed", "50", "20", "1", "false"}; String[][] redo=StatFactory.MakeParam(despair, null); cdart.AddStatString(redo); 
            return cdart;
            case 1: AttackAb cbomb= new AttackAb("Carnage Bombs", "single", "enemy", 5, 3, 1);
            String[] crusade={"Burn", "100", "5", "2", "false"}; String[][] nexu=StatFactory.MakeParam(crusade, null); cbomb.AddStatString(nexu);
            String[] crusader={"Bleed", "100", "5", "2", "false"}; String[][] nexo=StatFactory.MakeParam(crusader, null); cbomb.AddStatString(nexo);
            return cbomb;
            case 4: AttackAb crain= new AttackAb ("Rain of Destruction", "random 3", "enemy", 5, 5);
            String[] crusades={"Burn", "100", "5", "2", "false"}; String[][] next=StatFactory.MakeParam(crusades, null); crain.AddStatString(next);
            String[] stop={"Bleed", "100", "5", "2", "false"}; String[][] star=StatFactory.MakeParam(stop, null); crain.AddStatString(star);
            return crain;
            default: return null;
        }
    }
    public static Ability MakeAbGobby (int counter)
    {
        switch (counter)
        {
            case 0: DebuffAb sparkitup= new DebuffAb("Goblin Sparks", "single", "enemy", 0);
            String[] imam={"Shock", "100", "35", "1", "false"}; String[][] mosque=StatFactory.MakeParam(imam, null); sparkitup.AddStatString(mosque);
            return sparkitup;
            case 1: DebuffAb ghost= new DebuffAb("Gas Ghost", "single", "enemy", 2);
            String[] mecca={"Poison", "100", "45", "2", "false"}; String[][] aisha=StatFactory.MakeParam(mecca, null); ghost.AddStatString(aisha);
            return ghost;
            case 3: AttackAb bomb= new AttackAb("Pumpkin Bomb", "single", "enemy", 80, 3);
            String[] muhammad={"Weakness", "100", "20", "2", "false"}; String[][] pbuh=StatFactory.MakeParam(muhammad, null);
            String[] haram={"Poison", "100", "15", "2", "false"}; String[][] halal=StatFactory.MakeParam(haram, null);
            String[] jihad={"Target", "100", "10", "2", "false"}; String[][] quran=StatFactory.MakeParam(jihad, null);
            bomb.special.add(new ChooseStat(pbuh, halal, quran));
            return bomb;
            case 4: AttackAb run= new AttackAb("Pumpkin Bombing", "random 3", "enemy", 30, 4); run.special.add(new DebuffMod(91));
            run.desc="Can apply either Weakness, Poison, or Target.";
            return run;
            default: return null;
        }
    }
    public static Ability MakeAbCabbage (int counter) //carbage
    {
        switch (counter)
        {
            case 0: BasicAb pill= new BasicAb ("Cleave", "multitarget", "enemy", 20);
            String[] nader={"Bleed", "100", "5", "2", "false"}; String[][] vader=StatFactory.MakeParam(nader, null); pill.AddStatString(vader);
            return pill;
            case 1: AttackAb split= new AttackAb ("Hack", "single", "enemy", 70, 2); 
            String[] wolverine={"Bleed", "100", "5", "2", "false"}; String[][] mack=StatFactory.MakeParam(wolverine, null); split.AddStatString(mack); split.AddStatString(mack);
            return split;
            case 4: AttackAb slay= new AttackAb("Slaughter", "single", "enemy", 100, 4); slay.special.add(new DamageCounterRemove("Bleed", false, 5, false, false, false));
            return slay;
            default: return null;
        }
    }
    public static Ability MakeAbHydro (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb chomp= new BasicAb("Torrent", "AoE", "enemy", 30);
            return chomp;
            case 1: BuffAb tyre= new BuffAb("Liquidate", "AoE", "enemy", 2); tyre.together=true;
            String[] bl={"Soaked"}; String[] ast={"Other"}; tyre.special.add(new Extend(500, 616, "all", bl, ast, 1, false, true, false)); 
            String[] excuse={"Evasion", "100", "616", "1", "true aoe"}; String[][] me=StatFactory.MakeParam(excuse, null); tyre.AddStatString(me);
            String[] need={"Safeguard", "100", "616", "1", "true aoe"}; String[][] attention=StatFactory.MakeParam(need, null); tyre.AddStatString(attention); 
            return tyre;
            case 3: AttackAb whirl= new AttackAb("Whirlpool", "AoE", "enemy", 60, 4); whirl.special.add(new Ignore("Evade", "always", 616)); whirl.channelled=true;
            String[] fork={"Soaked", "500", "616", "2", "false"}; String[][] mean=StatFactory.MakeParam(fork, null); whirl.AddStatString(mean);
            return whirl;
            case 4: OtherAb drown= new OtherAb("Drown", "single", "enemy", 2); 
            drown.special.add(new ActivatePassive(89)); drown.desc="If the target has Soaked, causes 60 health loss.";
            return drown;
            default: return null;
        }
    }
    public static Ability MakeAbScorpion (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb chomp= new BasicAb("Tail Slam", "single", "enemy", 45);
            return chomp;
            case 1: DebuffAb spray= new DebuffAb("Acid Spray", "single", "enemy", 3);
            String[] heat={"Poison", "100", "105", "1", "false"}; String[][] bringin=StatFactory.MakeParam(heat, null); spray.AddStatString(bringin);
            return spray;
            case 2: OtherAb barb= new OtherAb("Poisonous Barb", "self", "self", 3); barb.unbound=true;
            String[] tryit={"Empower", "500", "616", "3", "true"}; String[][] bomb=StatFactory.MakeParam(tryit, null); barb.AddStatString(bomb);
            barb.desc="Gain a(n) Empower for 3 use(s), granting a 50% chance to apply Poison: 10 for 2 turn(s).";
            return barb;
            case 3: AttackAb sting= new AttackAb("Stinger Barrage", "single", "enemy", 30, 3, 2);
            return sting;
            case 4: AttackAb scorp= new AttackAb("Scorpion Sting", "single", "enemy", 80, 3); scorp.special.add(new Amplify (100, "Poison", "Debuffs", 20, true));
            return scorp;
            default: return null;
        }
    }
    public static Ability MakeAbLizard (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb chomp= new BasicAb("Bite", "single", "enemy", 45);
            return chomp;
            case 1: AttackAb jumpup= new AttackAb("Tooth and Claw", "single", "enemy", 45, 3, 1); 
            return jumpup;
            case 2: HealAb nightmare= new HealAb("Reptilian Regeneration", "self", "self", 4); nightmare.unbound=true;
            String[] horror={"Regen", "500", "35", "2", "true"}; String[][] longhaul=StatFactory.MakeParam(horror, null); nightmare.AddStatString(longhaul);
            return nightmare;
            case 3: AttackAb suture= new AttackAb("Tail Whip", "single", "enemy", 90, 3);
            String[] despair={"Afflicted", "100", "616", "1", "false"}; String[][] hate=StatFactory.MakeParam(despair, null); suture.AddStatString(hate);
            return suture;
            case 4: DefAb dread= new DefAb("Reptile Supremacy", "self", "self", 3); dread.special.add(new Confidence(500, 35));
            String[] night={"Taunt", "500", "616", "1", "true"}; String[][] stalker=StatFactory.MakeParam(night, null); dread.AddStatString(stalker);
            return dread;
            default: return null;
        }
    }
    public static Ability MakeAbKraven (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Savage Slash", "single", "enemy", 35); 
            String[] despair={"Bleed", "50", "20", "1", "false"}; String[][] redo=StatFactory.MakeParam(despair, null); cdart.AddStatString(redo); 
            return cdart;
            case 1: AttackAb booby= new AttackAb("Booby Trap", "single", "enemy", 35, 2); booby.together=true; 
            String[] negative={"Disorient Effect", "100", "616", "2", "false"}; String[][] martin=StatFactory.MakeParam(negative, null); booby.AddStatString(martin);
            String[] punk={"Disarm", "100", "616", "1", "false"}; String[][] frog= StatFactory.MakeParam(punk, null); booby.AddStatString(frog);
            return booby;
            case 3: BuffAb yummy= new BuffAb("Calypso Serum", "self", "self", 4); yummy.unbound=true;
            String[] bleed={"Drain", "100", "true", "2", "true"}; String[][] raccoon=StatFactory.MakeParam(bleed, null); yummy.AddStatString(raccoon);
            String[] goddess= {"Intensify", "100", "15", "2", "true"}; String[][] scooter=StatFactory.MakeParam(goddess, null); yummy.AddStatString(scooter);
            String[] ascendant={"Focus", "100", "616", "2", "true"}; String[][] vagabond=StatFactory.MakeParam(ascendant, null); yummy.AddStatString(vagabond);
            return yummy;
            case 4: AttackAb pounce= new AttackAb("Ambush", "single", "enemy", 90, 3); pounce.desc="\nOn kill with this ability, convert all Intensify and Focus on self to Effects.";
            pounce.special.add(new Ignore("DR", "enemy has Disorient", 616)); pounce.special.add(new ActivatePassive(86));
            return pounce;
            default: return null;
        }
    }
    //2.8: Defenders
    public static Ability MakeAbSurfer (int counter)
    {
        switch (counter)
        {
            case 0: OtherAb knock= new OtherAb("Suppress", "single", "enemy", 4); knock.channelled=true; 
            knock.special.add(new Nullify(500, 616, "all", "any", false, true)); //nullify before suppression
            String[] statesman={"Suppression", "500", "616", "1", "false"}; String[][] weakness=StatFactory.MakeParam(statesman, null); knock.AddStatString(weakness);
            return knock;
            case 1: AttackAb herald= new AttackAb("Annihilate", "single", "enemy", 100, 4); herald.channelled=true; herald.elusive=true;
            return herald;
            case 3: HealAb restore= new HealAb("Restore", "single", "ally exclusive", 4); restore.channelled=true; restore.special.add(new MendPassive(100));
            return restore;
            case 4: HealAb sentinel= new HealAb("Sentinel of the Spaceways", "AoE", "ally exclusive", 4); sentinel.channelled=true; 
            sentinel.special.add(new Purify(500, 616, "all", "any", false, true)); sentinel.special.add(new Confidence(500, 35)); //purify, then confidence
            return sentinel;
            default: return null;
        }
    }
    public static Ability MakeAbNamor (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Trident Stab", "single", "enemy", 35); 
            String[] despair={"Bleed", "100", "15", "1", "false"}; String[][] redo=StatFactory.MakeParam(despair, null); cdart.AddStatString(redo); 
            return cdart; 
            case 1: AttackAb peter= new AttackAb("Trident of Neptune", "single", "enemy", 0, 0); peter.special.add(new DebuffMod(84));
            peter.desc="If possible, Giganto performs an Assist on the target. Otherwise, does +45 damage.";
            return peter;
            case 2: BuffAb siren= new BuffAb("Imperius Rex", "self", "self", 0); siren.together=true;
            String[] negative={"Speed", "500", "616", "2", "true"}; String[][] martin=StatFactory.MakeParam(negative, null); siren.AddStatString(martin);
            String[] punk={"Safeguard", "500", "616", "2", "true"}; String[][] frog= StatFactory.MakeParam(punk, null); siren.AddStatString(frog);
            String[] knock={"Intensify", "500", "20", "2", "true"}; String[][] nock=StatFactory.MakeParam(knock, null); siren.AddStatString(nock);
            return siren;
            case 3: AttackAb house= new AttackAb("Tidal Wave", "AoE", "enemy", 60, 4); house.special.add(new ActivatePassive(84));
            house.desc="Convert Burn on the target to Target.";
            return house;
            case 4: OtherAb ploopy= new OtherAb("Horn of Proteus", "self", "self", 5); ploopy.channelled=true; ploopy.special.add(new Summoning (12));
            return ploopy;
            default: return null;
        }
    }
    public static Ability MakeAbCage (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb stroke= new BasicAb ("Uppercut", "single", "enemy", 45);
            return stroke;
            case 1: DefAb bring= new DefAb("Bring It On", "self", "self", 3); 
            String[] negative={"Taunt", "500", "616", "1", "true"}; String[][] martin=StatFactory.MakeParam(negative, null); bring.AddStatString(martin);
            String[] punk={"Resistance", "500", "15", "1", "true"}; String[][] frog= StatFactory.MakeParam(punk, null); bring.AddStatString(frog);
            return bring;
            case 4: AttackAb e= new AttackAb("Beatdown", "single", "enemy", 100, 3);
            String [] oblivion={"Disrupt", "100", "616", "1", "false"}; String[][] catchup=StatFactory.MakeParam(oblivion, null); e.AddStatString(catchup);
            String[] bastardsword={"Undermine", "100", "616", "1", "false"}; String[][] longbow=StatFactory.MakeParam(bastardsword, null); e.AddStatString(longbow);
            return e;
            default: return null;
        }
    }
    public static Ability MakeAbFist (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb stroke= new BasicAb ("Pressure Point Strike", "single", "enemy", 45);
            return stroke;
            case 1: HealAb buff= new HealAb("Focus Chi", "single", "ally inclusive", 0); buff.together=true;
            String[] negative={"Recovery", "100", "20", "2", "knull"}; String[][] martin=StatFactory.MakeParam(negative, null); buff.AddStatString(martin);
            String[] punk={"Intensify", "100", "5", "2", "knull"}; String[][] frog= StatFactory.MakeParam(punk, null); buff.AddStatString(frog);
            return buff;
            case 2: OtherAb as= new OtherAb("Purifying Chi", "single", "ally inclusive", 3); as.special.add(new Purify(100, 1, "chosen", "any", false, true));
            return as;
            case 3: HealAb snore= new HealAb("Healing Chi", "single", "ally inclusive", 3); snore.special.add(new Mend (100, 70));
            return snore;
            case 4: AttackAb fist= new AttackAb("The Iron Fist", "single", "enemy", 100, 4); fist.together=true;
            fist.special.add(new ApplyShatter(500, 1, true, true, false));
            return fist;
            default: return null;
        }
    }
    public static Ability MakeAbDD (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb stroke= new BasicAb ("Billy Club Strike", "single", "enemy", 35);
            String[] heck={"Counter Effect", "100", "20", "616", "true"}; String[][] heaven=StatFactory.MakeParam(heck, null); stroke.AddStatString(heaven);
            return stroke;
            case 1: AttackAb toss= new AttackAb ("Baton Throw", "single", "enemy", 50, 2);
            String[] blast={"Counter Effect", "50", "30", "616", "true"}; String[][] rau=StatFactory.MakeParam(blast, null); toss.AddStatString(rau);
            toss.special.add(new Ricochet(500, rau));
            return toss;
            case 3: AttackAb hand= new AttackAb ("Hand to Hand", "single", "enemy", 70, 3);
            String[] to={"Counter Effect", "100", "40", "616", "true"}; String[][] tohand= StatFactory.MakeParam(to, null); hand.AddStatString(tohand);
            return hand;
            case 4: AttackAb hurl= new AttackAb ("Blindside", "single", "enemy", 90, 3); hurl.together=true;
            String[] negative={"Provoke", "100", "616", "1", "false"}; String[][] martin=StatFactory.MakeParam(negative, null); hurl.AddStatString(martin);
            String[] punk={"Evade", "100", "616", "616", "true"}; String[][] frog= StatFactory.MakeParam(punk, null); hurl.AddStatString(frog);
            return hurl;
            default: return null;
        }
    }
    //2.7: Thunderbolts
    public static Ability MakeAbScarecrow (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch=new BasicAb("Pitchfork Stab", "single", "enemy", 45); punch.ignore=true;
            return punch;
            case 2: OtherAb fright= new OtherAb("Trained Crows", "AoE", "enemy", 0); 
            String[] bloody={"Fear", "100", "616", "3", "false"}; String[][] tears=StatFactory.MakeParam(bloody, null); fright.AddStatString(tears);
            return fright;
            case 3: OtherAb terrify=new OtherAb("Terrify", "AoE", "enemy", 0); terrify.unbound=true; terrify.special.add(new ActivatePassive(80));
            terrify.desc="100% chance to apply Fear for 3 turns once for each Fear or Terror on the target.";
            return terrify;
            case 4: OtherAb feed= new OtherAb("Feeding Off Fear", "AoE", "enemy", 0); feed.elusive=true; 
            feed.special.add(new DebuffMod(80)); feed.desc="Remove all Fear and Terror from enemies, and gain Intensify: 5 and Regen: 5 for each.";
            return feed;
            default: return null;
        }
    }
    public static Ability MakeAbRadioMan (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch=new BasicAb("Irradiated Touch", "single", "enemy", 35);
            String[] reefer={"Poison", "50", "20", "1", "false"}; String[][] free=StatFactory.MakeParam(reefer, null); punch.AddStatString(free);
            return punch;
            case 1: DebuffAb radpoison=new DebuffAb("Radiation Poisoning", "single", "enemy", 3); radpoison.unbound=true; 
            radpoison.special.add(new DebuffMod(79)); radpoison.desc="If the target has Poison, 100% chance to apply Afflicted for 1 turn(s).";
            return radpoison;
            case 2: DebuffAb touch= new DebuffAb("Contamination", "single", "enemy", 3);
            String[] arum={"Poison", "100", "35", "3", "false"}; String[][] fresh=StatFactory.MakeParam(arum, null); touch.AddStatString(fresh);
            return touch;
            case 3: OtherAb aura= new OtherAb("Radioactive Aura", "self", "self", 3);
            String[] defender={"Aura", "100", "616", "2", "true"}; String[] pain={"Poison", "50", "30", "1", "false"}; String[][] earth=StatFactory.MakeParam(defender, pain);
            String[] doctor={"Focus", "100", "616", "2", "true"}; String[][] sand=StatFactory.MakeParam(doctor, null); aura.AddStatString(sand); aura.AddStatString(earth);
            return aura;
            case 4: BasicAb melt= new BasicAb("Meltdown", "single", "enemy", 35); melt.special.add(new Activate("Poison", "Debuffs", 0));
            return melt;
            default: return null;
        }
    }
    public static Ability MakeAbRulk (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch=new BasicAb("Searing Smash", "single", "enemy", 45);
            return punch;
            case 1: OtherAb heat=new OtherAb("Overheat", "self", "self", 0); heat.multiuse=2;
            String[] bright={"Burn Effect", "100", "0", "3", "true"}; String[][] burn=StatFactory.MakeParam(bright, null); heat.AddStatString(burn);
            return heat;
            case 3: OtherAb red= new OtherAb("Seeing Red", "self", "self", 2); red.unbound=true; red.restricted=true; red.restriction=78; red.together=true;
            String[] kangaroo={"Intensify", "500", "20", "2", "true"}; String[][] porcupine=StatFactory.MakeParam(kangaroo, null);
            String[] oracular={"Aura", "500", "616", "2", "true"}; String[] brush={"Burn", "50", "20", "1", "false"}; String[][] aura=StatFactory.MakeParam(oracular, brush);
            red.AddStatString(aura); red.AddStatString(porcupine); red.desc="Requires at least 3 Burn Effects to be used. ";
            return red;
            case 4: AttackAb sear= new AttackAb("Blistering Assault", "single", "enemy", 100, 4); sear.special.add(new Boost(100));
            return sear;
            default: return null;
        }
    }
    public static Ability MakeAbPenance (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch=new BasicAb("Penitent Punch", "single", "enemy", 45);
            String[] rick={"Provoke", "100", "616", "1", "false"}; String[][] oshay=StatFactory.MakeParam(rick, null); punch.AddStatString(oshay);
            return punch;
            case 1: OtherAb rake=new OtherAb("Spike Rake", "self", "self", 0); rake.special.add(new DebuffMod(77,3)); rake.special.add(new SelfDMG(60, false)); 
            return rake;
            case 3: AttackAb blast=new AttackAb("Kinetic Explosion", "single", "enemy", 100, 0); blast.restricted=true; blast.restriction=77;
            blast.special.add(new DebuffMod(77,1)); blast.special.add(new Ricochet(500)); blast.desc="Requires and consumes 3 Pain on use. ";
            return blast;
            case 4: BasicAb bloom= new BasicAb("Explosive Shockwave", "AoE", "enemy", 35); bloom.special.add(new DebuffMod(77,2));
            bloom.desc="Remove all Pain and do +15 damage for each.";
            return bloom;
            default: return null;
        }
    }
    public static Ability MakeAbSpeedball (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb boom= new BasicAb("Bubble Blast", "single", "enemy", 20, 1);
            return boom;
            case 1: AttackAb sling= new AttackAb("Clear The Way", "single", "enemy", 40, 2);
            String[] rick={"Provoke", "100", "616", "1", "false"}; String[][] oshay=StatFactory.MakeParam(rick, null); 
            sling.AddStatString(oshay); sling.special.add(new Ricochet(500, oshay));
            return sling;
            case 2: DefAb intercept= new DefAb("Rebound Intercept", "single", "ally exclusive", 2);
            String[] ex={"Protect", "500", "616", "1", "true"}; String[][] or=StatFactory.MakeParam(ex, null); intercept.AddStatString(or);
            return intercept;
            case 4: BasicAb blast= new BasicAb("Momentum Release", "single", "enemy", 35); blast.special.add(new MissingDMG(5, 15, true));
            return blast;
            default: return null;
        }
    }
    public static Ability MakeAbMoonstone (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb storeek = new BasicAb("Photon Blast", "single", "enemy", 40);
            String[] boom={"Intensify", "500", "10", "1", "true"}; String[][] pow=StatFactory.MakeParam(boom, null); storeek.AddStatString(pow);
            return storeek;
            case 1: OtherAb steal= new OtherAb("Hypnotise", "single", "enemy", 2); steal.unbound=true; steal.control=true;
            String[] sesame={"Placebo (Buff)", "500", "616", "equal", "false"}; String[][] street=StatFactory.MakeParam(sesame, null);
            steal.special.add(new CopySteal(500, 1, "chosen", new String[] {"any"}, new String[] {"Buffs"}, true, true, street));
            return steal;
            case 4: AttackAb warp= new AttackAb("Intangible Strike", "single", "enemy", 80, 3); warp.special.add(new DamageCounterSimple(15, "Placebo (Buff)", false, false, true));
            warp.special.add(new DamageCounterSimple(15, "Buffs", true, true, true));
            return warp;
            default: return null;
        }
    }
    public static Ability MakeAbSongbird (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb a= new BasicAb("Acoustikinesis", "single", "enemy", 35); 
            if (copy==false)
            a.special.add(new ActivatePassive(1));
            else
            a.special.add(new ActivatePassive(74));
            a.desc="Apply Mend: 25 to the ally with the most missing HP.";
            return a;
            case 3: HealAb s= new HealAb("Serenade", "single", "ally exclusive", 3); 
            if (copy==false)
            {
                s.special.add(new ActivatePassive(2)); s.desc="Apply Mend: 60.";
            }
            else
            s.special.add(new Mend(500, 60));
            return s;
            case 4: BuffAb b= new BuffAb("Uplifting Melody", "self", "self", 3); b.unbound=true; b.special.add(new Purify(500, 1, "random", "Afflicted", true, true));
            String[] buff={"Precision", "500", "616", "2", "true"}; String[][] fish=StatFactory.MakeParam(buff, null); b.AddStatString(fish);
            return b;
            default: return null;
        }
    }
    public static Ability MakeAbMimi (int counter)
    {
        switch (counter)
        {
            case 1: AttackAb a= new AttackAb("E Minor: Dizziness", "single", "enemy", 60, 3); 
            String[] disorient={"Disorient", "100", "616", "1", "false"}; String[][] doom=StatFactory.MakeParam(disorient, null); a.AddStatString(doom);
            return a;
            case 2: AttackAb b= new AttackAb("B Flat: Euphoria", "single", "enemy", 60, 3);
            String[] dizzy={"Daze", "100", "616", "1", "false"}; String[][] david=StatFactory.MakeParam(dizzy, null); b.AddStatString(david);
            return b;
            case 3: AttackAb major= new AttackAb("A Major: Blindness", "single", "enemy", 60, 3);
            String[] bozo={"Blind", "100", "616", "1", "false"}; String[][] clown=StatFactory.MakeParam(bozo, null); major.AddStatString(clown);
            return major;
            case 4: AttackAb top= new AttackAb("F Minor: Nausea", "single", "enemy", 60, 3);
            String[] andrew={"Nauseated", "100", "616", "2", "false"}; String[][] spineless=StatFactory.MakeParam(andrew, null); top.AddStatString(spineless);
            return top;
            default: return null;
        }
    }
    public static Ability MakeAbZemo (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb lunge= new BasicAb("Deadly Lunge", "single", "enemy", 35);
            String[] heart={"Precision", "100", "616", "616", "true"}; String[][] rend=StatFactory.MakeParam(heart, null); lunge.AddStatString(rend);
            return lunge;
            case 1: DefAb parry= new DefAb("Parry", "self", "self", 3); parry.unbound=true; parry.together=true;
            String[] happy={"Guard", "100", "45", "1", "true"}; String[][] meal=StatFactory.MakeParam(happy, null); parry.AddStatString(meal);
            String[] freaky={"Safeguard", "100", "616", "2", "true"}; String[][] hate=StatFactory.MakeParam(freaky, null); parry.AddStatString(hate);
            return parry;
            case 4: AttackAb dom= new AttackAb("Dominating Blow", "single", "enemy", 60, 3); dom.special.add(new ActivatePassive(72)); dom.desc="On kill, take a bonus turn. ";
            String[] rush={"Ferocity", "100", "616", "616", "true"}; String[][] heat=StatFactory.MakeParam(rush, null); dom.AddStatString(heat);
            return dom;
            default: return null;
        }
    }
    //2.6: U-Foes
    public static Ability MakeAbVapor (int counter)
    {
        switch (counter)
        {
            case 0: DebuffAb melt= new DebuffAb("Hydrogen Sulfide", "single", "enemy", 0); melt.special.add(new DebuffMod(71)); 
            melt.desc="If the target has a Defence effect, 100% chance to apply Poison: 60 for 1 turn(s).";
            return melt;
            case 1: DebuffAb normal= new DebuffAb("Arsine", "single", "enemy", 0); 
            String[] pew={"Poison", "100", "30", "2", "false"}; String[][] pewpew=StatFactory.MakeParam(pew, null); normal.AddStatString(pewpew);
            return normal;
            case 3: DebuffAb amplify=new DebuffAb("Chlorine Trifluoride", "single", "enemy", 0); 
            String[] clone={"Chlorine", "100", "616", "2", "false"}; String[][] triplicate=StatFactory.MakeParam(clone, null); amplify.AddStatString(triplicate);
            return amplify;
            case 4: DebuffAb boom=new DebuffAb("Hydrogen", "single", "enemy", 0); 
            String[] boomboom={"Hydrogen", "100", "616", "2", "false"}; String[][] blammo=StatFactory.MakeParam(boomboom, null); boom.AddStatString(blammo);
            return boom;
            default: return null;
        }
    }
    public static Ability MakeAbIronclad (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch= new BasicAb("Slam", "single", "enemy", 35); 
            return punch;
            case 3: BuffAb inc=new BuffAb("Increase Density", "self", "self", 4); hold=inc;
            String[] blaze={"Intensify Effect", "500", "30", "2", "true"}; String[][] blade=StatFactory.MakeParam(blaze, null); inc.AddStatString(blade);
            String[] chicken={"Resistance Effect", "500", "30", "2", "true"}; String[][] golden=StatFactory.MakeParam(chicken, null); inc.AddStatString(golden);
            return inc;
            case 4: DefAb bruise= new DefAb("Bruiser", "self", "self", 4); 
            String[] tikka={"Taunt", "500", "616", "1", "true"}; String[][] locke=StatFactory.MakeParam(tikka, null); bruise.AddStatString(locke);
            bruise.special.add(new Use(hold, "default")); //in this order so it matches card desc
            return bruise;
            default: return null;
        }
    }
    public static Ability MakeAbXRay (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch= new BasicAb("Radiation Blast", "single", "enemy", 45); punch.lose=true;
            return punch;
            case 1: AttackAb midas= new AttackAb("Radiation Wave", "AoE", "enemy", 45, 3); midas.lose=true;
            return midas;
            case 3: DebuffAb con= new DebuffAb("Concentrated Radiation", "single", "enemy", 3);
            String[] blaze={"Burn", "500", "50", "2", "false"}; String[][] blade=StatFactory.MakeParam(blaze, null); con.AddStatString(blade);
            String[] chicken={"Afflicted", "500", "616", "1", "false"}; String[][] golden=StatFactory.MakeParam(chicken, null); con.AddStatString(golden);
            return con;
            case 4: AttackAb gilded= new AttackAb("Heavy Radiation", "single", "enemy", 90, 3); gilded.lose=true;
            return gilded;
            default: return null;
        }
    }
    public static Ability MakeAbVector (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb punch= new BasicAb("Kinetic Blast", "single", "enemy", 45);
            return punch;
            case 1: OtherAb slow= new OtherAb("Decelerate Matter", "single", "enemy", 0);
            String[] sour={"Empower", "100", "-45", "1", "false"}; String[][] burn=StatFactory.MakeParam(sour, null); slow.AddStatString(burn);
            slow.desc="100% chance to apply a(n) Empower for 1 use(s), making attack skills do -45 damage.";
            if (copy==false) //vector's repel reality
            {
                slow.special.add(new Nullify(100, 1, "random", "any", false, true));
            }
            return slow;
            case 2: OtherAb block= new OtherAb("Block Matter", "single", "ally inclusive", 0); 
            String[] fall={"Guard Effect", "100", "45", "1", "knull"}; String[][] darkness=StatFactory.MakeParam(fall, null); block.AddStatString(darkness);
            if (copy==false) //vector's repel reality
            {
                block.special.add(new Purify(100, 1, "random", "any", false, true));
            }
            return block;
            default: return null;
        }
    }
    //2.5: Thanos Arrives
    public static Ability MakeAbMaw (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: OtherAb insid=new OtherAb("Insidious Whisper", "single", "enemy", 0);
            String[] sid={"Whisper", "500", "616", "616", "false"}; String[][] ous=StatFactory.MakeParam(sid, null); insid.AddStatString(ous);
            if (copy==false)
            {
                insid.elusive=true; insid.special.add(new Ignore ("targeting effects", "always", 616));
            }
            return insid;
            case 1: 
            if (copy==false)
            {
                //power.elusive=true; power.special.add(new Ignore ("targeting effects", "always", 616));
            }
            default: return null;
        }
    }
    public static Ability MakeAbDwarf (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch=new BasicAb("Axe Slam", "single", "enemy", 45); punch.special.add(new DamageCounterSimple(45, "Provoke", false, true, true));
            return punch;
            case 1: AttackAb bigpunch=new AttackAb("Axe Throw", "single", "enemy", 100, 3); 
            return bigpunch;
            case 2: BuffAb skin=new BuffAb("Thick Skin", "self", "self", 3); 
            String[] res={"Resistance", "100", "30", "2", "true"}; String[][] rez=StatFactory.MakeParam(res, null); skin.AddStatString(rez);
            String[] safe={"Safeguard", "100", "616", "2", "true"}; String[][] guard=StatFactory.MakeParam(safe, null); skin.AddStatString(guard);
            return skin;
            case 3: DefAb sidian=new DefAb("Obsidian Guard", "single", "ally exclusive", 4); sidian.special.add(new ActivatePassive(66));
            String[] gloom={"Protect", "500", "616", "1", "true"}; String[][] chic=StatFactory.MakeParam(gloom, null); sidian.AddStatString(chic); 
            sidian.desc="The Protected ally gains a copy of Black Dwarf's buffs and Defence effects.";
            return sidian;
            case 4: AttackAb rick=new AttackAb("Devastating Cleave", "single", "enemy", 100, 4); rick.special.add(new Ricochet(50));
            return rick;
            default: return null;
        }
    }
    public static Ability MakeAbSupergiant (int counter)
    {
        switch (counter)
        {
            case 0: OtherAb assist=new OtherAb("Command", "single", "enemy", 0); assist.desc="Must be Dominated. ";
            Assist a= new Assist (true, 1, 0, true, 0, null, 500, true); a.condition="Dominated"; assist.special.add(a);
            return assist;
            case 1: OtherAb dom=new OtherAb("Dominate", "single", "enemy", 0); dom.control=true; dom.channelled=true;
            String[] blast={"Dominate", "500", "616", "616", "false"}; String[][] it=StatFactory.MakeParam(blast, null); dom.AddStatString(it);
            return dom;
            case 4: OtherAb kill=new OtherAb("Feast", "single", "ally exclusive", 0); kill.channelled=true; kill.special.add(new ActivatePassive(65));
            kill.desc="If the target is Dominated, kill them and regain 100 HP.";
            return kill;
            default: return null;
        }
    }
    public static Ability MakeAbProxima (int counter)
    {
        switch (counter)
        {
            case 0: return new BasicAb("Impale", "single", "enemy", 45);
            case 1: DebuffAb tip=new DebuffAb("Poisonous Tip", "single", "enemy", 3);
            String[] boogy={"Poison Effect", "100", "20", "10", "false"}; String[][] oogy=StatFactory.MakeParam(boogy, null); tip.AddStatString(oogy);
            return tip;
            case 3: AttackAb power=new AttackAb("Infinite Mass Entanglement", "single", "enemy", 100, 4); 
            String[] stunner={"Stun Effect", "100", "616", "1", "false"}; String[][] hook=StatFactory.MakeParam(stunner, null); power.AddStatString(hook);
            return power;
            case 4: AttackAb mask=new AttackAb("Last Light", "single", "enemy", 100, 4);
            String[] clash={"Wound Effect", "100", "616", "1", "false"}; String[][] cow=StatFactory.MakeParam(clash, null); mask.AddStatString(cow);
            return mask;
            default: return null;
        }
    }
    public static Ability MakeAbCorvus (int counter)
    {
        switch (counter)
        {
            case 0: return new BasicAb("Quick Stab", "single", "enemy", 35);
            case 1: AttackAb slash=new AttackAb("Piercing Slash", "single", "enemy", 70, 3);
            String[] start={"Wound", "100", "616", "1", "false"}; String[][] finish=StatFactory.MakeParam(start, null); slash.AddStatString(finish);
            return slash;
            case 4: AttackAb power=new AttackAb("Execute", "single", "enemy", 60, 3); power.special.add(new Ignore ("inescapable", "always", 616));
            return power;
            default: return null;
        }
    }
    public static Ability MakeAbGauntlet (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb power=new BasicAb("Power", "AoE", "enemy", 200); power.special.add(new Chain(false, power));
            if (copy==false)
            {
                power.elusive=true; power.special.add(new Ignore ("inescapable", "always", 616));
            }
            return power;
            case 4: OtherAb will=new OtherAb("Reality", "AoE", "enemy", 3); will.unbound=true; will.special.add(new Strip("any", "any", true));
            if (copy==false)
            {
                will.elusive=true; will.special.add(new Ignore ("inescapable", "always", 616));
            }
            return will;
            default: return null;
        }
    }
    public static Ability MakeAbThanos (int counter)
    {
        switch (counter)
        {
            case 0: return new BasicAb("Titan Punch", "single", "enemy", 45);
            case 1: return new BasicAb("Energy Pillars", "AoE", "enemy", 30); 
            default: return null;
        }
    }
    //2.1: Sinister Six
    public static Ability MakeAbRhino (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb ram= new BasicAb("Ram", "single", "enemy", 35);
            String[] shazam={"Provoke", "50", "616", "1", "false"}; String[][] shamaz=StatFactory.MakeParam(shazam, null); ram.AddStatString(shamaz);
            return ram;
            case 3: AttackAb rhino= new AttackAb("Rhino Charge", "single", "enemy", 100, 4);
            String[] brave={"Taunt", "100", "616", "1", "true"}; String[][] bold=StatFactory.MakeParam(brave, null); rhino.AddStatString(bold);
            return rhino;
            case 4: AttackAb minotaur= new AttackAb("Stampede", "AoE", "enemy", 60, 4); minotaur.channelled=true;
            String[] wash={"Provoke", "100", "616", "1", "false"}; String[][] after=StatFactory.MakeParam(wash, null); minotaur.AddStatString(after);
            return minotaur;
            default: return null;
        }
    }
    public static Ability MakeAbSandy (int counter)
    {
        switch (counter)
        {
            case 0: return new BasicAb("Sand Blast", "AoE", "enemy", 30);
            case 1: return new AttackAb("Sand Wave", "AoE", "enemy", 50, 3);
            case 4: OtherAb storm= new OtherAb("Sandstorm", "self", "self", 0); storm.singleuse=true; storm.special.add(new ActivateP()); 
            storm.desc="Activate Sandstorm, applying Blind to everyone and doing 20 Elusive damage to everyone on friendly turns.";
            return storm;
            default: return null;
        }
    }
    public static Ability MakeAbElectro (int counter)
    {
        switch (counter)
        {
            case 0: DebuffAb ball= new DebuffAb("Ball Lightning", "single", "enemy", 0);
            String[] orb={"Shock", "100", "40", "1", "false"}; String[][] shockinghuh=StatFactory.MakeParam(orb, null); ball.AddStatString(shockinghuh);
            return ball;
            case 1: DebuffAb shimmy= new DebuffAb("Shocking Touch", "single", "enemy", 0); shimmy.multiuse=2; shimmy.special.add(new ActivateP(39));
            shimmy.desc="Convert an Intensify Effect on self to Shock with +10 strength and apply it to the target.";
            return shimmy;
            case 3: OtherAb surge= new OtherAb("Electrical Surge", "self", "self", 0); surge.multiuse=2;
            String[] thee={"Shock", "500", "20", "2", "true"}; String[][] thine=StatFactory.MakeParam(thee, null); surge.AddStatString(thine);
            String[] me={"Target Effect", "500", "5", "1", "true"}; String[][] mine=StatFactory.MakeParam(me, null); surge.AddStatString(mine);
            return surge;
            case 4: BasicAb bolt= new BasicAb("Electric Discharge", "AoE", "enemy", 35); bolt.special.add(new DamageCounterRemove("Intensify", false, 5, true, true, true));
            return bolt;
            default: return null;
        }
    }
    public static Ability MakeAbOck (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb punch= new BasicAb("Tentacle Punch", "single", "enemy", 45);
            return punch;
            case 1: BuffAb plan= new BuffAb("Sinister Plan", "single", "ally exclusive", 2);
            String[] me={"Focus", "100", "616", "2", "true"}; String[][] mine=StatFactory.MakeParam(me, null); plan.AddStatString(mine);
            String[] thee={"Focus", "100", "616", "2", "false"}; String[][] thine=StatFactory.MakeParam(thee, null); plan.AddStatString(thine);
            return plan;
            case 2: OtherAb bigbrain= new OtherAb("Superior Intellect", "single", "ally inclusive", 2); 
            String[] name={"any"}; String[] type={"Buffs"}; bigbrain.special.add(new Extend(500, 1, "chosen", name, type, 1, false, true, true));
            return bigbrain;
            case 3: AttackAb arm= new AttackAb("Armed and Dangerous", "single", "enemy", 90, 3);
            String[] disarm={"Disarm", "100", "616", "1", "false"}; String[][] d=StatFactory.MakeParam(disarm, null); arm.AddStatString(d);
            return arm;
            case 4: AttackAb fore= new AttackAb("Four Armed Fury", "single", "enemy", 25, 4, 3); 
            String[] gimme={"Placebo (Debuff)", "50", "616", "2", "false"}; String[][]dat=StatFactory.MakeParam(gimme, null); fore.AddStatString(dat);
            return fore;
            default: return null;
        }
    }
    public static Ability MakeAbMysterio (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb vanish= new BasicAb("Disappearing Act", "single", "enemy", 40); 
            String[] milk={"Invisible", "100", "616", "1", "true"}; String[][] teat=StatFactory.MakeParam(milk, null); vanish.AddStatString(teat);
            return vanish;
            case 1: DebuffAb pyro= new DebuffAb("Pyrotechnics", "single", "enemy", 2);
            String[] tired={"Burn", "100", "40", "2", "false"}; String[][] sonic=StatFactory.MakeParam(tired, null); pyro.AddStatString(sonic);
            return pyro;
            case 2: OtherAb mental= new OtherAb("Hallucinogenic Gas", "single", "enemy", 3); mental.control=true; 
            mental.special.add(new Assist(false, 1, 40, false, 50, null, 500, true));
            return mental;
            case 3: OtherAb light= new OtherAb("Master of Illusions", "self", "self", 4); light.channelled=true;
            light.special.add(new Summoning(7)); light.special.add(new Summoning(7)); light.special.add(new Summoning(7));
            return light;
            case 4: AttackAb sun= new AttackAb("Light Spectacle", "AoE", "enemy", 70, 5); sun.channelled=true;
            String[] blind={"Blind", "100", "616", "1", "false"}; String[][] girlfriend=StatFactory.MakeParam(blind, null); sun.AddStatString(girlfriend);
            return sun;
            default: return null;
        }
    }
    public static Ability MakeAbVulture (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb swoop= new BasicAb("Wing Swipe", "single", "enemy", 40);
            return swoop;
            case 1: AttackAb fight= new AttackAb("Fight and Flight", "single", "enemy", 80, 3); fight.special.add(new DebuffMod(36));
            fight.desc="Randomly apply Afflicted, Neutralise, or Undermine for 1 turn.";
            return fight;
            case 3: AttackAb flight= new AttackAb("Aerial Attack", "single", "enemy", 80, 3); 
            String[] sean={"Afflicted", "100", "616", "1", "false"}; String[][] hannity=StatFactory.MakeParam(sean, null);
            String[] tucker={"Neutralise", "100", "616", "1", "false"}; String[][] carlson=StatFactory.MakeParam(tucker, null);
            String[] ben={"Undermine", "100", "616", "1", "false"}; String[][] shapiro=StatFactory.MakeParam(ben, null);
            flight.special.add(new ChooseStat(hannity, carlson, shapiro));
            return flight;
            case 4: AttackAb floop= new AttackAb("One Fell Swoop", "single", "enemy", 80, 3); floop.special.add(new Boost (50));
            return floop;
            default: return null;
        }
    }
    //2.0: Original 
    public static Ability MakeAbCain (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb smash= new BasicAb("Staggering Blow", "single", "enemy", 45); smash.special.add(new DebuffMod(35, 1)); smash.desc="Does +20 damage if self has 5 M.";
            return smash;
            case 1: AttackAb smash2= new AttackAb("Rampage", "single", "enemy", 80, 3);
            String[] smash3={"Taunt", "100", "616", "1", "true"}; String[][] smash4=StatFactory.MakeParam(smash3, null); smash2.AddStatString(smash4);
            return smash2;
            case 4: AttackAb smash5= new AttackAb("Unstoppable Charge", "single", "enemy", 100, 5); 
            smash5.special.add(new Ignore("Protect", "passive", 5)); smash5.special.add(new Ignore("Taunt", "passive", 5)); smash5.special.add(new DebuffMod(35, 5));
            smash5.desc="Does +50 damage and removes 5 M if self has 5 M.";
            return smash5;
            default: return null;
        }
    }
    public static Ability MakeAbSkull (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb cut=new BasicAb("Aimed Shot", "single", "enemy", 40); cut.special.add(new DamageCounter("Debuffs", true, 5, false, true));
            return cut;
            case 1: DebuffAb dust=new DebuffAb ("Dust of Death", "single", "enemy", 2);
            String[] stick={"Poison", "100", "45", "2", "false"}; String[][] teddy=StatFactory.MakeParam(stick, null); dust.AddStatString(teddy);
            return dust;
            case 2: DebuffAb master=new DebuffAb("Master Tactician", "single", "enemy", 3); master.unbound=true;
            String[] hook={"Vulnerable", "100", "616", "2", "false"}; String[][]tama=StatFactory.MakeParam(hook, null); master.AddStatString(tama);
            String[] toa={"Disorient", "100", "616", "2", "false"}; String[][] glam=StatFactory.MakeParam(toa, null); master.AddStatString(glam);
            return master;
            case 3: OtherAb cube= new OtherAb("Storm Assault", "single", "enemy", 3); Assist asia= new Assist(true, 2, 0, true, 0, null, 500, true); asia.skull=true;
            cube.special.add(asia); cube.desc="They do +5 damage for each unique Debuff on the target. ";
            return cube;
            case 4: DebuffAb real= new DebuffAb("Cosmic Cube Unleashed", "AoE", "enemy", 0); real.ignore=true; real.singleuse=true; real.dcd=1; real.channelled=true;
            real.together=true; real.special.add(new Ignore("Neutralise", "always", 616)); real.special.add(new Ignore ("Missed", "always", 616)); //must go before applyshatter
            real.special.add(new ApplyShatter(500, 1, true, false, false)); 
            String[] comer={"Wound", "500", "616", "1", "false"}; String[][] james=StatFactory.MakeParam(comer, null); real.AddStatString(james);
            String[] dickens={"Daze", "500", "616", "1", "false"}; String[][] charles=StatFactory.MakeParam(dickens, null); real.AddStatString(charles);
            return real;
            default: return null;
        }
    }
    public static Ability MakeAbDeadpool (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb bang= new BasicAb("Bang Bang", "single", "enemy", 20, 1); 
            return bang;
            case 1: AttackAb slash= new AttackAb("Hack and Slash", "single", "enemy", 65, 3); 
            String[] meat={"Bleed", "100", "15", "2", "false"}; String[][] bleed=StatFactory.MakeParam(meat, null); slash.AddStatString(bleed);
            slash.special.add(new Ricochet(500, bleed));
            return slash;
            case 4: AttackAb awesome= new AttackAb("Awesome Finisher", "lowest", "enemy", 90, 5); awesome.special.add(new Ignore ("Evade", "always", 616));
            return awesome;
            default: return null;
        }
    }
    public static Ability MakeAbBB (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb blow= new BasicAb("Kingly Blow", "single", "enemy", 50);
            return blow;
            case 2: AttackAb blast= new AttackAb("Electron Blast", "single", "enemy", 80, 3); blast.ignore=true; blast.desc="+20 damage for each E.";
            if (copy==false)
            blast.special.add(new DebuffMod(32, 3));
            return blast;
            case 3: AttackAb whisper= new AttackAb("Quasi-Sonic Whisper", "single", "enemy", 100, 3); whisper.ignore=true;
            if (copy==true)
            whisper.special.add(new ApplyShatter(100, 0, false, false, false));
            else
            {
                whisper.special.add(new DebuffMod(32, 4)); whisper.desc="100% chance to apply Shatter. +50% status chance and +1 duration for each E.";
            }
            return whisper;
            default: return null;
        }
    }
    public static Ability MakeAbHulk (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pumel= new BasicAb("Pummel", "single", "enemy", 45);
            return pumel;
            case 1: DefAb taunt= new DefAb("Furious Roar", "self", "self", 3); 
            String[] special={"Taunt", "100", "616", "1", "true"}; String[][] butch=StatFactory.MakeParam(special, null); taunt.AddStatString(butch);
            return taunt;
            case 3: AttackAb smash= new AttackAb("Hulk Smash", "single", "enemy", 80, 2);
            return smash;
            case 4: AttackAb sing= new AttackAb("Strongest There Is", "single", "enemy", 120, 5); sing.special.add(new DamageCounterSimple(120, 55, true, false));
            return sing;
            default: return null;
        }
    }
    public static Ability MakeAbBrawn (int counter, boolean copy)
    {
        switch (counter)
        {
            case 0: BasicAb sack= new BasicAb ("Calculated Smash", "single", "enemy", 40); 
            if (copy==false)
            {
                String[] p={"Buffs", "Defence", "Heal"}; sack.special.add(new BeforeNullify(50, 1, "random", "any", false, true, p));
            }
            else
            {
                String[] p={"Buffs"}; sack.special.add(new BeforeNullify(50, 1, "random", "any", false, true, p));
            }
            return sack;
            case 3: OtherAb nullify= new OtherAb("Brains", "single", "enemy", 3); nullify.unbound=true; nullify.elusive=true;
            if (copy==false)
            {
                String[] p={"Buffs", "Defence", "Heal"}; nullify.special.add(new BeforeNullify(500, 1, "chosen", "any", false, true, p));
            }
            else
            {
                String[] p={"Buffs"}; nullify.special.add(new BeforeNullify(500, 1, "chosen", "any", false, true, p));
            }
            return nullify;
            case 4: AttackAb smash= new AttackAb("Brawn", "single", "enemy", 80, 3); String[] ner={"Buffs", "Defence", "Heal"};
            smash.special.add(new DamageCounterSimple(40, ner, true, false, false));
            return smash;
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
            String[] horse={"Regen", "500", "50", "1", "knull"}; String[][] mockery=StatFactory.MakeParam(horse, null); eye.AddStatString(mockery);
            return eye;
            case 2: DefAb diamond= new DefAb("Seven Rings of Raggadorr", "single", "ally inclusive", 3); 
            String[] ash={"Resistance", "100", "40", "1", "knull"}; String[][] freeze=StatFactory.MakeParam(ash, null); diamond.AddStatString(freeze);
            return diamond;
            case 3: OtherAb xtreme= new OtherAb("Sorcerer Supreme", "self", "self", 4); xtreme.unbound=true; xtreme.special.add(new ReduceCD(true, 1)); 
            return xtreme; 
            case 4: AttackAb band= new AttackAb("Crimson Bands of Cyttorak", "single", "enemy", 100, 4); band.together=true; band.channelled=true;
            String[] mane={"Stun Effect", "100", "616", "1", "false"}; String[][] man=StatFactory.MakeParam(mane, null); band.AddStatString(man);
            String[] wolf={"Wound Effect", "100", "616", "1", "false"}; String[][] cycle=StatFactory.MakeParam(wolf, null); band.AddStatString(cycle);
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
            case 1: DefAb b= new DefAb("Personal Force Field", "self", "self", 4); b.unbound=true; b.special.add(new ActivatePassive());
            String[] opera={"Barrier", "500", "80", "2", "true"}; String[][] ophidian=StatFactory.MakeParam(opera, null); b.AddStatString(ophidian);
            b.desc="Seal Magical Blast for 2 turns. ";
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
            ray.special.add(new CopySteal(100, 1, "random", name, type, true, true, null));
            return ray;
            case 1: BasicAb shoot= new BasicAb("Disintegration Beam", "single", "enemy", 40); String[] mane={"any"}; String[] tepy={"Buffs"};
            shoot.special.add(new Extend(100, 616, "all", mane, tepy, 1, true, true, false));
            return shoot;
            case 3: OtherAb s= new OtherAb("Summon Drones", "self", "self", 0); s.special.add(new Summoning(4));
            String[] safe={"Safeguard", "500", "616", "1", "true"}; String[][] guard=StatFactory.MakeParam(safe, null); s.AddStatString(guard);
            return s;
            case 4: AttackAb d= new AttackAb("Annihilate", "single", "enemy", 70, 3); d.special.add(new DamageCounter("Buffs", true, 20, true, true)); 
            d.special.add(new Ignore("Defence", "always", 616)); d.special.add(new ActivatePassive(27)); d.desc="On kill, Extend all Buffs on self by 1 turn(s). ";
            return d;
            default: return null;
        }
    }
    public static Ability MakeAbMODORK (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb brain= new BasicAb ("Brain Blast", "single", "enemy", 50); 
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
            case 0: BasicAb fire= new BasicAb("Covering Fire", "single", "enemy", 40); fire.desc="Gain 5 C.";
            fire.special.add(new Nullify(50, 1, "random", "any", false, true)); fire.special.add(new ActivatePassive(5)); 
            return fire;
            case 1: BasicAb s= new BasicAb("Symbiotic Assault", "single", "enemy", 40); s.special.add(new ActivatePassive(-5)); s.desc="Lose 5 C.";
            return s;
            case 4: AttackAb shock= new AttackAb ("Shock and Awe", "single", "enemy", 80, 2); shock.special.add(new DebuffMod(25));
            shock.desc="100% chance to apply Undermine for 1 turn(s) if C is above 5.";
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
            case 0: BasicAb mighty= new BasicAb("Mighty Punch", "single", "enemy", 45); 
            return mighty;
            case 1: AttackAb photonic= new AttackAb("Photonic Blast", "single", "enemy", 80, 2); photonic.special.add(new ApplyShatter(100, 0, false, false, false));
            return photonic;
            default: return null;
        }
    }
    public static Ability MakeAbKK (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb talon= new BasicAb("Morpho Punch", "single", "enemy", 45); 
            return talon;
            case 1: AttackAb bash= new AttackAb("Embiggened Bash", "single", "enemy", 80, 2); bash.special.add(new ActivatePassive(22));
            bash.desc="With Evasion, Drain: Half. With Mighty Blows, 100% chance to Nullify.";
            return bash;
            case 2: BuffAb grow= new BuffAb("Grow", "self", "self", 3); grow.together=true;
            String[] bam={"Mighty Blows", "100", "616", "2", "true"}; String[][]bang=StatFactory.MakeParam(bam, null); grow.AddStatString(bang);
            String[] pew={"Focus", "100", "616", "2", "true"}; String[][] pewpew=StatFactory.MakeParam(pew, null); grow.AddStatString(pewpew);
            return grow;
            case 3: BuffAb shrink= new BuffAb("Shrink", "self", "self", 3); shrink.together=true;
            String[] bamf={"Evasion", "100", "616", "2", "true"}; String[][]banged=StatFactory.MakeParam(bamf, null); shrink.AddStatString(banged);
            String[] pewer={"Focus", "100", "616", "2", "true"}; String[][] pewpewer=StatFactory.MakeParam(pewer, null); shrink.AddStatString(pewpewer);
            return shrink;
            case 4: AttackAb smash= new AttackAb("Marvellous Finish", "single", "enemy", 120, 4); smash.special.add(new DebuffMod (22));
            smash.desc="With Evasion, 100% chance to apply a(n) Stun. With Mighty Blows, 100% chance to apply Wound for 1 turn(s).";
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
            String[] cresht= {"Poison", "100", "30", "2", "false"}; String[][] peaks=StatFactory.MakeParam(cresht, null); rain.AddStatString(peaks);
            return rain;
            case 4: AttackAb tornado= new AttackAb("Tornado", "AoE", "enemy", 90, 0); tornado.elusive=true; tornado.channelled=true; tornado.singleuse=true; tornado.dcd=1;
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
            case 1: AttackAb assault= new AttackAb("Web Assault", "single", "enemy", 75, 2); assault.special.add(new DamageCounterSimple(15, "Tracer", false, false, true));
            return assault;
            case 2: DebuffAb swarm=new DebuffAb ("Spider-Bot Swarm", "AoE", "enemy", 3); swarm.special.add(new DebuffMod(20));
            swarm.desc="100% chance to apply Countdown: 55 for 1 turn(s). If the target has Tracer, apply Countdown: 65 instead.";
            return swarm;
            case 3: OtherAb summon=new OtherAb("Superior Spider", "self", "self", 0); summon.special.add(new Summoning(27)); summon.unbound=true;
            return summon;
            default: return null;
        }
    }
    public static Ability MakeAbMiles (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb blast=new BasicAb("Venom Blast", "single", "enemy", 35); 
            String[] shockinghuh= {"Shock", "100", "5", "3", "false"}; String[][] hahadie=StatFactory.MakeParam(shockinghuh, null); blast.AddStatString(hahadie);
            return blast;
            case 1: BuffAb camo= new BuffAb("Spider Camouflage", "self", "self", 3); camo.together=true; camo.unbound=true;
            String[] swift={"Invisible", "100", "616", "2", "true"}; String[][] sand=StatFactory.MakeParam(swift, null); camo.AddStatString(sand);
            String[] taylor={"Evasion", "100", "616", "2", "true"}; String[][] bold=StatFactory.MakeParam(taylor, null); camo.AddStatString(bold);
            return camo;
            case 2: AttackAb sneak= new AttackAb("Sneak Attack", "single", "enemy", 90, 3);
            sneak.special.add(new DamageCounterSimple(20, "Invisible", false, true, true));
            return sneak;
            case 3: AttackAb thing= new AttackAb("Anything a Spider Can", "single", "enemy", 90, 3);
            thing.special.add(new DebuffMod (19)); thing.desc="Apply Undermine for 1 turn(s) if the target has Shock.";
            return thing;
            case 4: AttackAb ultimate= new AttackAb("Ultimate Spider-Man", "single", "enemy", 110, 5);
            ultimate.special.add(new DamageCounterSimple(20, "Invisible", false, true, true)); ultimate.special.add(new DamageCounterSimple(20, "Shock", false, false, true));
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
            case 1: DebuffAb quip= new DebuffAb ("Distracting Quip", "single", "enemy", 0); quip.ignore=true; quip.special.add(new Ignore("targeting effects", "always", 616));
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
            case 3: AttackAb come= new AttackAb ("Come To Me", "single", "enemy", 90, 3); come.special.add(new DamageCounterSimple (20, "Stun", false, false, true));
            return come;
            case 4: AttackAb devour= new AttackAb ("Devour", "single", "enemy", 90, 4); devour.special.add(new ActivatePassive(17));
            devour.desc="On kill with this ability, reduce its cooldown by 2 turns and gain Regen: 45 for 2 turns. ";
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
            String[] bigamy= {"Terror", "100", "616", "1", "false"}; String[][] moron= StatFactory.MakeParam(bigamy, null); venom.AddStatString(moron);
            return venom;
            default: return null;
        }
    }
    public static Ability MakeAbWolvie (int counter)  
    {
        switch (counter)
        {
            case 0: BasicAb slash= new BasicAb ("X-Slash", "single", "enemy", 45); 
            String[] bleed= {"Bleed", "50", "15", "1", "false"}; String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
            return slash;
            case 1: BasicAb punch =new BasicAb ("Primal Punch", "single", "enemy", 45); punch.special.add(new Purify(50, 1, "random", "any", true, true));
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
            case 1: OtherAb twins= new OtherAb("Twin Blades", "self", "self", 0); twins.desc="Drax's next attack that would activate his passive activates it twice.";
            if (copy==false)
            {
                twins.special.add(new ActivateP()); twins.special.add (new Update (13));
            }
            return twins; 
            case 2: 
            AttackAb knife= new AttackAb ("Knife Slash", "single", "enemy", 35, 3); knife.desc="100% chance to apply Bleed: 35 for 2 turn(s).";
            if (copy==true)
            {
                String[] bloody= {"Bleed", "100", "35", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); knife.AddStatString(gore);
            }
            else
            knife.special.add (new DebuffMod (13));
            return knife; 
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "AoE", "enemy", 55, 3); 
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
            case 1: BasicAb blow= new BasicAb ("Finishing Blow", "single", "enemy", 35); blow.special.add(new DamageCounterRemove ("Obsession", false, 35, false, false, false));
            return blow;
            default: return null;
        }
    }
    public static Ability MakeAbNickJr (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb slam= new BasicAb ("Body Slam", "single", "enemy", 45); slam.ignore=true;
            return slam;
            case 1: BasicAb slash= new BasicAb ("Combat Knife", "single", "enemy", 35); 
            String[] bleed= {"Bleed", "50", "20", "1", "false"}; String[][] red=StatFactory.MakeParam(bleed, null); slash.AddStatString(red);
            return slash;
            case 2: BuffAb cloak= new BuffAb ("Cloaking Tech", "self", "self", 3); cloak.together=true;
            String[] tech={"Invisible", "100", "616", "1", "true"}; String[][] redo=StatFactory.MakeParam(tech, null); cloak.AddStatString(redo);
            String[] fast= {"Speed", "100", "616", "1", "true"}; String[][] sped=StatFactory.MakeParam(fast, null); cloak.AddStatString(sped); 
            return cloak;
            case 3: OtherAb kill= new OtherAb ("Kill Mode", "self", "self", 0); kill.singleuse=true; kill.unbound=true; kill.special.add(new ActivateP());
            kill.desc="Activate Kill Mode, granting +15 damage at the cost of losing 15 health on friendly turns. ";
            return kill;
            case 4: AttackAb poke= new AttackAb ("Double Tap", "single", "enemy", 40, 2, 1); poke.special.add (new Ignore ("Missed", "passive", 1));
            return poke;
            default: return null;
        }
    }
    public static Ability MakeAbNickSr (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb smg= new BasicAb ("SMG", "AoE", "enemy", 30);
            return smg;
            case 1: BuffAb frag= new BuffAb ("Tactical Advantage", "single", "ally inclusive", 0); 
            String[] swordm= {"Speed", "100", "616", "2", "knull"}; String[][] redmo=StatFactory.MakeParam(swordm, null); frag.AddStatString(redmo);
            return frag;
            case 2: BuffAb cache= new BuffAb ("Hidden Weapons Cache", "single", "ally inclusive", 0); 
            String[] sword= {"Intensify", "100", "15", "1", "knull"}; String[][] redo=StatFactory.MakeParam(sword, null); cache.AddStatString(redo);
            return cache;
            case 4: AttackAb air= new AttackAb ("Airstrike", "AoE", "enemy", 80, 5); air.channelled=true; air.special.add(new Ignore ("Missed", "always", 616));
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
            case 1: HealAb pump= new HealAb ("Pump It Up", "AoE", "ally inclusive", 0); pump.together=true;
            String[] dance= {"Recovery", "500", "10", "2", "knull"}; String[][] redo=StatFactory.MakeParam(dance, null); pump.AddStatString(redo);
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
            case 1: DebuffAb gren= new DebuffAb ("Grenade Lob", "single", "enemy", 0); gren.desc="The Countdown damage Ricochets.";
            String[] explode= {"Countdown", "100", "60", "2", "false"}; String[] ricochet= {"Ricochet"}; 
            String[][] count=StatFactory.MakeParam(explode, ricochet); gren.AddStatString(count);
            return gren; 
            case 2: AttackAb expert= new AttackAb("Expert Marksman", "single", "enemy", 40, 2); 
            expert.special.add(new Extend (500, 2, "random", new String[]{"Countdown"}, new String[]{"any"}, 1, false, true, false));
            String[] decoy={"Countdown", "100", "40", "2", "false"}; String[][] countdown=StatFactory.MakeParam(decoy, null); expert.AddStatString(countdown); 
            return expert;
            case 3: OtherAb det= new OtherAb("Detonator", "single", "enemy", 2); det.special.add(new Activate ("Countdown", "any", 60));
            det.special.add(new Ignore ("inescapable", "always", 616)); 
            return det;
            default: return null;
        }
    }
    public static Ability MakeAbFalc (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb bomb= new BasicAb ("Divebomb", "single", "enemy", 35); 
            String[] weak= {"Weakness", "100", "15", "1", "false"}; String[][] redo=StatFactory.MakeParam(weak, null); 
            bomb.AddStatString(redo);
            return bomb;
            case 3: DebuffAb eagle= new DebuffAb ("Eagle Eyed", "single", "enemy", 3); eagle.together=true;
            String[] targ={"Target", "100", "20", "1", "false"}; String[][] target=StatFactory.MakeParam(targ, null); 
            String[] sexy= {"Neutralise", "100", "616", "1", "false"}; String[][] charming=StatFactory.MakeParam(sexy, null);
            eagle.AddStatString (target); eagle.AddStatString (charming);
            return eagle;
            case 4: AttackAb swarm= new AttackAb ("Bird Swarm", "AoE", "enemy", 60, 4); 
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
            String[] whelmed= {"Resistance", "500", "20", "1", "true"}; String[][] viper=StatFactory.MakeParam(whelmed, null);
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
            String[] rewind={"Debilitate", "500", "10", "2", "true"}; String[][] overpower=StatFactory.MakeParam(rewind, null);
            boost.AddStatString (redo); boost.AddStatString (overpower); boost.special.add (new SelfDMG (30, false));
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
            case 2: AttackAb supfire= new AttackAb ("Suppressing Fire", "AoE", "enemy", 55, 3); 
            return supfire;
            case 3: BuffAb punwep= new BuffAb ("Weapons Expert", "self", "self", 0); 
            String[] asunder={"Focus Effect", "100", "616", "2", "true"}; String[][] me=StatFactory.MakeParam(asunder, null); 
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
                String[] mello={"Bleed Effect", "50", "20", "1", "false"}; String[][] baby=StatFactory.MakeParam(mello, null);  finese.AddStatString(baby);
            }
            else
            {
                finese.special.add (new DebuffMod (2, 1)); finese.desc="50% chance to apply Bleed Effect: 20 for 1 turn. ";
            }
            return finese;
            case 4: 
            AttackAb assass= new AttackAb ("Assassinate", "single", "enemy", 100, 5); 
            if (copy==true)
            {
                String[] near={"Bleed Effect", "50", "30", "1", "false"}; String[][] fin=StatFactory.MakeParam(near, null); assass.AddStatString(fin); 
            }
            else
            {
                assass.special.add (new DebuffMod (2, 2)); assass.desc="50% chance to apply Bleed Effect: 30 for 1 turn. ";
            }
            return assass;
            default: return null;
        }
    }
    public static Ability MakeAbMK (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Crescent Dart", "single", "enemy", 35); 
            String[] despair={"Bleed", "100", "15", "1", "false"}; String[][] redo=StatFactory.MakeParam(despair, null); cdart.AddStatString(redo); 
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
