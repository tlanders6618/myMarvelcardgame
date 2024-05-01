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
        switch (index) //since making a giant array of dozens of elements was very slow, this should be faster, albeit longer
        {
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
            case 36: return MakeAbVulture(counter);
            case 37: return MakeAbMysterio(counter);
            case 38: return MakeAbOck(counter);
            case 39: return MakeAbElectro(counter);
            case 40: return MakeAbSandy(counter);
            case 41: return MakeAbRhino(counter);
            case 81: return MakeAbDD(counter);
            case 82: return MakeAbFist(counter);
            case 83: return MakeAbCage(counter);
            case 84: return MakeAbNamor(counter);
            case 85: return MakeAbSurfer(counter);
            case 86: return MakeAbKraven(counter);
            case 87: return MakeAbLizard(counter);
            case 88: return MakeAbScorpion(counter);
            case 89: return MakeAbHydro(counter);
            case 90: return MakeAbCabbage(counter);
            case 91: return MakeAbGobby(counter);
            case 92: return MakeAbRoblin(counter);
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
    //2.9: Fearsome Foes of Spider-Man
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
            case 1: AttackAb split= new AttackAb ("Hack", "single", "enemy", 60, 2); 
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
            drown.special.add(new ActivatePassive(89)); drown.desc="If the target has Soaked, causes 60 HP loss.";
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
            String[] tryit={"Empower", "500", "10", "3", "true"}; String[][] bomb=StatFactory.MakeParam(tryit, null); barb.AddStatString(bomb);
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
            case 0: BasicAb chomp= new BasicAb("Bite", "single", "enemy", 40);
            return chomp;
            case 1: AttackAb jumpup= new AttackAb("Tooth and Claw", "single", "enemy", 45, 3, 1); 
            return jumpup;
            case 2: HealAb nightmare= new HealAb("Reptilian Regeneration", "self", "self", 3);
            String[] horror={"Regen", "500", "45", "2", "true"}; String[][] longhaul=StatFactory.MakeParam(horror, null); nightmare.AddStatString(longhaul);
            return nightmare;
            case 3: AttackAb suture= new AttackAb("Tail Whip", "single", "enemy", 90, 3);
            String[] despair={"Afflicted", "100", "616", "1", "false"}; String[][] hate=StatFactory.MakeParam(despair, null); suture.AddStatString(hate);
            return suture;
            case 4: DefAb dread= new DefAb("Reptile Supremacy", "self", "self", 3); dread.special.add(new Confidence(500, 25));
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
            case 1: OtherAb booby= new OtherAb("Booby Trap", "single", "enemy", 2); booby.together=true; 
            String[] negative={"Snare Effect", "100", "616", "2", "false"}; String[][] martin=StatFactory.MakeParam(negative, null); booby.AddStatString(martin);
            String[] punk={"Disarm", "100", "616", "1", "false"}; String[][] frog= StatFactory.MakeParam(punk, null); booby.AddStatString(frog);
            return booby;
            case 3: BuffAb yummy= new BuffAb("Calypso Serum", "self", "self", 4); yummy.unbound=true;
            String[] bleed={"Speed", "100", "616", "2", "true"}; String[][] raccoon=StatFactory.MakeParam(bleed, null); yummy.AddStatString(raccoon);
            String[] goddess= {"Intensify", "100", "15", "2", "true"}; String[][] scooter=StatFactory.MakeParam(goddess, null); yummy.AddStatString(scooter);
            String[] ascendant={"Focus", "100", "616", "2", "true"}; String[][] vagabond=StatFactory.MakeParam(ascendant, null); yummy.AddStatString(vagabond);
            return yummy;
            case 4: AttackAb pounce= new AttackAb("Ambush", "single", "enemy", 90, 3); pounce.desc="\nOn kill with this ability, convert all Intensify and Focus on self to Effects.";
            pounce.special.add(new Ignore("Defence", "enemy has Snare", 616)); pounce.special.add(new ActivatePassive(86));
            return pounce;
            default: return null;
        }
    }
    //2.8: Defenders
    public static Ability MakeAbSurfer (int counter)
    {
        switch (counter)
        {
            case 0: OtherAb knock= new OtherAb("Suppress", "single", "enemy", 4); 
            knock.channelled=true; knock.special.add(new Nullify(500, 616, "all", "any", false, true)); 
            String[] statesman={"Suppression", "500", "616", "1", "false"}; String[][] weakness=StatFactory.MakeParam(statesman, null); knock.AddStatString(weakness);
            return knock;
            case 1: AttackAb herald= new AttackAb("Annihilate", "single", "enemy", 100, 4); herald.channelled=true; herald.elusive=true;
            return herald;
            case 3: HealAb restore= new HealAb("Restore", "single", "ally exclusive", 4); restore.channelled=true; restore.special.add(new Mend(500, 100));
            return restore;
            case 4: HealAb sentinel= new HealAb("Sentinel of the Spaceways", "AoE", "ally exclusive", 4); 
            sentinel.channelled=true; sentinel.special.add(new Purify(500, 616, "all", "any", false, true)); sentinel.special.add(new Confidence(500, 20)); 
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
            String[] negative={"Mighty Blows", "500", "616", "2", "true"}; String[][] martin=StatFactory.MakeParam(negative, null); siren.AddStatString(martin);
            String[] punk={"Safeguard", "500", "616", "2", "true"}; String[][] frog= StatFactory.MakeParam(punk, null); siren.AddStatString(frog);
            String[] knock={"Focus", "500", "616", "2", "true"}; String[][] nock=StatFactory.MakeParam(knock, null); siren.AddStatString(nock);
            return siren;
            case 3: AttackAb house= new AttackAb("Tidal Wave", "AoE", "enemy", 60, 4); house.special.add(new Boost(50)); house.special.add(new ActivatePassive(84));
            house.desc="Convert Burn debuffs on the target to Target Effects.";
            return house;
            case 4: OtherAb ploopy= new OtherAb("Horn of Proteus", "self", "self", 0); ploopy.singleuse=true; ploopy.channelled=true; ploopy.special.add(new Summoning (12));
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
            String[] punk={"Resistance", "500", "5", "2", "true"}; String[][] frog= StatFactory.MakeParam(punk, null); bring.AddStatString(frog);
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
            fist.special.add(new ApplyShatter(500, 1, true, true));
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
            case 4: AttackAb minotaur= new AttackAb("Stampede", "AoE", "enemy", 50, 4); 
            String[] wash={"Provoke", "100", "616", "1", "false"}; String[][] after=StatFactory.MakeParam(wash, null); minotaur.AddStatString(after);
            return minotaur;
            default: return null;
        }
    }
    public static Ability MakeAbSandy (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb blast= new BasicAb("Sand Blast", "AoE", "enemy", 30);
            return blast;
            case 1: AttackAb wave= new AttackAb("Sand Wave", "AoE", "enemy", 50, 3);
            return wave;
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
            String[] me={"Target Effect", "500", "10", "1", "true"}; String[][] mine=StatFactory.MakeParam(me, null); surge.AddStatString(mine);
            return surge;
            case 4: BasicAb bolt= new BasicAb("Electric Discharge", "AoE", "enemy", 30); bolt.special.add(new DamageCounterRemove("Intensify", false, 5, true, true, true));
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
            case 1: AttackAb arm= new AttackAb("Armed and Dangerous", "single", "enemy", 70, 2);
            String[] disarm={"Disarm", "50", "616", "1", "false"}; String[][] d=StatFactory.MakeParam(disarm, null); arm.AddStatString(d);
            return arm;
            case 2: BuffAb plan= new BuffAb("Sinister Plan", "single", "ally exclusive", 3);
            String[] me={"Focus", "100", "616", "2", "true"}; String[][] mine=StatFactory.MakeParam(me, null); plan.AddStatString(mine);
            String[] thee={"Focus", "100", "616", "2", "false"}; String[][] thine=StatFactory.MakeParam(thee, null); plan.AddStatString(thine);
            return plan;
            case 3: OtherAb bigbrain= new OtherAb("Superior Intellect", "single", "ally inclusive", 2); 
            String[] name={"any"}; String[] type={"Buffs"}; bigbrain.special.add(new Extend(500, 1, "chosen", name, type, 1, false, true, true));
            return bigbrain;
            case 4: AttackAb fore= new AttackAb("Four Armed Fury", "single", "enemy", 25, 4, 3); 
            String[] gimme={"Disorient", "0", "616", "1", "false"}; String[][]dat=StatFactory.MakeParam(gimme, null); fore.AddStatString(dat);
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
            case 3: OtherAb light= new OtherAb("Master of Illusions", "self", "self", 4); 
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
            case 1: AttackAb smash2= new AttackAb("Rampage", "single", "enemy", 80, 2);
            String[] smash3={"Taunt", "100", "616", "1", "true"}; String[][] smash4=StatFactory.MakeParam(smash3, null); smash2.AddStatString(smash4);
            return smash2;
            case 4: AttackAb smash5= new AttackAb("Unstoppable Charge", "single", "enemy", 120, 5); 
            smash5.special.add(new Ignore("Protect", "passive", 5)); smash5.special.add(new Ignore("Taunt", "passive", 5)); smash5.special.add(new DebuffMod(35, 5));
            smash5.desc="Does +30 damage and removes 5 M if self has 5 M.";
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
            case 2: AttackAb master=new AttackAb("Master Tactician", "single", "enemy", 80, 3);
            String[] hook={"Disrupt", "100", "616", "2", "false"}; String[][]tama=StatFactory.MakeParam(hook, null); master.AddStatString(tama);
            String[] toa={"Disorient", "100", "616", "2", "false"}; String[][] glam=StatFactory.MakeParam(toa, null); master.AddStatString(glam);
            return master;
            case 3: OtherAb cube= new OtherAb("Storm Assault", "single", "enemy", 3); Assist asia= new Assist(true, 2, 0, true, 0, null, 500, true); asia.skull=true;
            cube.special.add(asia); cube.attack=false; cube.desc="They do +5 damage for each unique Debuff on the target. ";
            return cube;
            case 4: OtherAb real= new OtherAb("Cosmic Cube Unleashed", "AoE", "enemy", 0); real.special.add(new ApplyShatter(500, 1, true, false)); 
            real.special.add(new Ignore("Neutralise", "always", 616)); real.together=true; real.special.add(new Ignore ("Missed", "always", 616)); real.singleuse=true; real.dcd=1;
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
            case 0: BasicAb blow= new BasicAb("Kingly Blow", "single", "enemy", 45);
            return blow;
            case 2: AttackAb blast= new AttackAb("Electron Blast", "single", "enemy", 80, 3); blast.ignore=true; blast.desc="+20 damage for each E.";
            if (copy==false)
            blast.special.add(new DebuffMod(32, 3));
            return blast;
            case 3: AttackAb whisper= new AttackAb("Quasi-Sonic Whisper", "single", "enemy", 100, 3); whisper.ignore=true;
            if (copy==true)
            whisper.special.add(new ApplyShatter(100, 0, false, false));
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
            String[] horse={"Regen", "500", "45", "1", "knull"}; String[][] mockery=StatFactory.MakeParam(horse, null); eye.AddStatString(mockery);
            return eye;
            case 2: DefAb diamond= new DefAb("Seven Rings of Raggadorr", "single", "ally inclusive", 3); 
            String[] ash={"Resistance", "100", "20", "1", "knull"}; String[][] freeze=StatFactory.MakeParam(ash, null); diamond.AddStatString(freeze);
            return diamond;
            case 3: OtherAb xtreme= new OtherAb("Sorcerer Supreme", "self", "self", 4); xtreme.unbound=true; xtreme.special.add(new ReduceCD(true, 1)); 
            return xtreme; 
            case 4: AttackAb band= new AttackAb("Crimson Bands of Cyttorak", "single", "enemy", 90, 4); band.together=true;
            String[] mane={"Stun Effect", "100", "616", "1", "false"}; String[][] man=StatFactory.MakeParam(mane, null); band.AddStatString(man);
            String[] wolf={"Snare Effect", "100", "616", "1", "false"}; String[][] cycle=StatFactory.MakeParam(wolf, null); band.AddStatString(cycle);
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
            case 0: BasicAb s= new BasicAb("Symbiotic Assault", "single", "enemy", 40); s.special.add(new ActivatePassive(-5)); s.desc="Lose 5 C.";
            return s;
            case 1: BasicAb fire= new BasicAb("Covering Fire", "single", "enemy", 40); fire.desc="Gain 5 C.";
            fire.special.add(new Nullify(50, 1, "random", "any", false, true)); fire.special.add(new ActivatePassive(5)); 
            return fire;
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
            case 1: AttackAb photonic= new AttackAb("Photonic Blast", "single", "enemy", 60, 2); photonic.special.add(new ApplyShatter(50, 0, false, false));
            photonic.special.add(new ActivateP()); 
            return photonic;
            case 3: AttackAb photon= new AttackAb("Photon Barrage", "single", "enemy", 80, 3); photon.special.add(new ApplyShatter(100, 0, false, false));
            photon.special.add(new ActivateP()); 
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
            bash.desc="\nWith Evasion, 100% chance to apply Provoke for 1 turn(s). With Mighty Blows, 100% chance to apply Terror for 1 turn(s).";
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
            smash.desc="\nWith Evasion, 100% chance to apply Disarm for 1 turn(s). With Mighty Blows, 100% chance to apply Shatter for 1 turn(s).";
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
            case 3: AttackAb come= new AttackAb ("Come To Me", "single", "enemy", 90, 3); come.special.add(new DamageCounterSimple (20, "Snare", false, false, true));
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
            case 1: BasicAb punch =new BasicAb ("Primal Punch", "single", "enemy", 35); punch.special.add(new Purify(50, 1, "random", "any", true, true));
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
            case 1: OtherAb twins= new OtherAb("Twin Blades", "self", "self", 0); twins.desc="Drax's next activation of his passive occurs twice.";
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
            case 1: BasicAb blow= new BasicAb ("Finishing Blow", "single", "enemy", 40); blow.special.add(new DamageCounterRemove ("Obsession", false, 40, false, false, false));
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
            kill.desc="Activate Kill Mode, granting +15 damage at the cost of -15 health loss on friendly turns. ";
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
            case 1: BuffAb frag= new BuffAb ("Overwhelming Firepower", "single", "ally inclusive", 0); 
            String[] swordm= {"Mighty Blows", "100", "616", "2", "knull"}; String[][] redmo=StatFactory.MakeParam(swordm, null); frag.AddStatString(redmo);
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