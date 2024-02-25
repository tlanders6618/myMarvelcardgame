package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 25/7/22
 * Filename: Ability_List_Player
 * Purpose: Creates/lists all character abilities.
 */
public class Ability_List_Player
{
    public static Ability GetAb (int index, int counter, boolean copy)
    {
        //abs construction: String name, String type, String friendly, int dmg, int cooldown
        //check MakeStatString for stateff array format
        //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
        //type is single, self, multitarg, random, or aoe 
        Ability toret=new BasicAb ("Knick Knack Paddy Whack", "single", "enemy", 35);
        switch (index) //since making a giant array of dozens of elements was very slow, this should be faster, albeit longer
        {
            case 1: toret=Ability_List_Player.MakeAbMK(counter); break;
            case 2: toret=Ability_List_Player.MakeAbGam(counter); break;
            case 3: toret=Ability_List_Player.MakeAbPun(counter); break;
            case 4: toret=Ability_List_Player.MakeAbIM(counter); break;
            case 5: toret=Ability_List_Player.MakeAbWM(counter); break;
            case 6: toret=Ability_List_Player.MakeAbCap(counter); break;
            case 7: toret=Ability_List_Player.MakeAbFalc(counter); break;
            case 8: toret=Ability_List_Player.MakeAbBucky(counter); break;
            case 9: toret=Ability_List_Player.MakeAbStarLord(counter); break;
            case 10: toret=Ability_List_Player.MakeAbNickSr(counter); break;
            case 11: toret=Ability_List_Player.MakeAbNickJr(counter); break;
            case 12: toret=Ability_List_Player.MakeAbOGDrax(counter); break;
            case 13: toret=Ability_List_Player.MakeAbDrax(counter); break;
            case 14: toret=Ability_List_Player.MakeAbX23(counter); break;
            case 15: toret=Ability_List_Player.MakeAbWolvie(counter); break;
            default: System.out.println ("Problem getting hero abilities");
        }
        return toret;
    }    
    public static Ability MakeAbName (int counter) //template
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
    public static Ability MakeAbWolvie (int counter)  
    {
        switch (counter)
        {
            case 0: BasicAb slash= new BasicAb ("X-Slash", "single", "enemy", 35); String[] bleed= {"Bleed", "50", "20", "1", "false"}; 
            String[][] real=StatFactory.MakeParam(bleed, null); slash.AddStatString(real);
            return slash;
            case 1: BasicAb punch =new BasicAb ("Primal Punch", "single", "enemy", 45); 
            return punch; 
            case 4: AttackAb best= new AttackAb ("Best There Is", "single", "enemy", 100, 5); best.special.add(new DamageCounterRemove ("Debuffs", true, 10, true));
            best.special.add(new DamageCounterRemove ("Defence", true, 10, true)); best.special.add(new DamageCounterRemove ("Buffs", true, 10, true));
            best.special.add(new DamageCounterRemove ("Heal", true, 10, true)); 
            return best;
            default: return null;
        }
    }
    public static Ability MakeAbX23 (int counter)  
    {
        switch (counter)
        {
            case 0: BasicAb kick =new BasicAb ("Karate Kick", "single", "enemy", 45); 
            return kick; 
            case 1: BasicAb slash= new BasicAb("Precision Slash", "single", "enemy", 35); String[] precision= {"Precision", "100", "616", "2", "true"};
            String[][] toret=StatFactory.MakeParam(precision, null); slash.AddStatString(toret); 
            return slash;
            case 3: AttackAb out= new AttackAb ("Bleed Them Out", "single", "enemy", 60, 3); String[] bleed= {"Bleed", "100", "20", "2", "false"};
            String[][] bleeding=StatFactory.MakeParam(bleed, null); out.AddStatString(bleeding);
            return out;
            case 4: AttackAb triple= new AttackAb("Triple Slash", "single", "enemy", 30, 3); triple.multihit=2; triple.special.add(new Multichain (3, triple));
            return triple;
            default: return null;
        }
    }
    public static Ability MakeAbDrax (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb headbutt =new BasicAb ("Headbutt", "single", "enemy", 40); String[] doom={"Drain", "500", "50", "2", "true"};
            String[][] drain=StatFactory.MakeParam(doom, null); headbutt.AddStatString(drain);
            return headbutt; 
            case 1: AttackAb twins= new AttackAb("Twin Blades", "single", "enemy", 80, 3); 
            String[] despair={"Bleed", "100", "5", "2", "false"};
            String[] knock={"Bleed", "100", "5", "2", "false"}; 
            String[][] redo=StatFactory.MakeParam(despair, null); String[][] bloody=StatFactory.MakeParam(knock, null);
            twins.AddStatString(redo); twins.AddStatString(bloody); twins.together=false;
            return twins; 
            case 2: DefAb fierce= new DefAb ("Fierce Protector", "single", "ally exclusive", 3); String[] gloom={"Protect", "500", "616", "1", "true"};
            String[] one={"Counter", "100", "40", "1", "true"}; String[] two={"Counter", "100", "40", "1", "true"}; 
            String[][] prot=StatFactory.MakeParam(gloom, null); String[][] uno=StatFactory.MakeParam(one, null); String[][] dos=StatFactory.MakeParam(two, null);
            fierce.AddStatString(prot); fierce.AddStatString(uno); fierce.AddStatString(dos); fierce.together=false;
            return fierce; 
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "single", "enemy", 80, 3); dice.special.add (new Ricochet (500));
            return dice; 
            case 4: AttackAb destroy= new AttackAb ("The Destroyer", "single", "enemy", 120, 4); destroy.special.add(new ApplyShatter (100, 1, true));
            return destroy;
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
            case 2: BuffAb cloak= new BuffAb ("Cloaking Tech", "self", "self", 3); 
            String[] tech={"Invisible", "100", "616", "2", "true"}; String[][] redo=StatFactory.MakeParam(tech, null); 
            String[] fast= {"Speed", "100", "616", "2", "true"}; String[][] sped=StatFactory.MakeParam(fast, null); 
            cloak.AddStatString(redo); cloak.AddStatString(sped);
            return cloak;
            case 3: OtherAb kill= new OtherAb ("Kill Mode", "self", "self", 0); kill.singleuse=true; kill.unbound=true; kill.special.add(new ActivateP(11));
            return kill;
            case 4: AttackAb poke= new AttackAb ("Double Tap", "single", "enemy", 40, 2); poke.multihit=1; poke.special.add (new Ignore ("Missed", "passive", 616));
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
            expert.special.add(new Extend (500, 2, "random", blood, 1, privy, false, true));
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
            case 3: DebuffAb eagle= new DebuffAb ("Eagle Eyed", "single", "enemy", 3); 
            String[] targ={"Target", "100", "20", "1", "false"}; String[][] target=StatFactory.MakeParam(targ, null); 
            String[] sexy= {"Neutralise", "100", "616", "1", "false"}; String[][] charming=StatFactory.MakeParam(sexy, null);
            eagle.AddStatString (target); eagle.AddStatString (charming);
            return eagle;
            case 4: AttackAb swarm= new AttackAb ("Bird Swarm", "AoE", "enemy", 70, 4); 
            String[] scary={"Daze", "50", "616", "1", "false"}; String[][] fred=StatFactory.MakeParam(scary, null); 
            swarm.AddStatString(fred); swarm.together=false;
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
            molotov.AddStatString (fry); molotov.AddStatString (light); molotov.together=false;
            return molotov;
            case 1: AttackAb punished= new AttackAb ("Punished", "single", "enemy", 75, 3); 
            String[] shred= {"Bleed", "50", "10", "2", "false"}; String[][] heat=StatFactory.MakeParam(shred, null); 
            String[] dead= {"Neutralise", "50", "616", "1", "false"}; String[][] head=StatFactory.MakeParam(dead, null);
            punished.AddStatString (heat); punished.AddStatString(head); punished.together=false;
            return punished;
            case 2: AttackAb supfire= new AttackAb ("Suppressing Fire", "AoE", "enemy", 65, 3); 
            return supfire;
            case 3: BuffAb punwep= new BuffAb ("Weapons Expert", "self", "self", 0); 
            String[] asunder={"FocusE", "100", "616", "2", "true"}; String[][] me=StatFactory.MakeParam(asunder, null); 
            String[] evie={"Intensify", "100", "15", "2", "true"}; String[][] redo=StatFactory.MakeParam(evie, null);             
            punwep.AddStatString (redo); punwep.AddStatString (me); punwep.together=false; 
            return punwep;
            case 4: AttackAb retri= new AttackAb ("Retribution", "single", "enemy", 120, 5);
            String [] naught= {"Wound", "50", "616", "1", "false"}; String[][] brought=StatFactory.MakeParam(naught, null);
            retri.AddStatString(brought);
            return retri;
            default: return null;
        }
    }
    public static Ability MakeAbGam (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb concstr= new BasicAb ("Pommel Smash", "single", "enemy", 45); 
            String[] hug={"Intensify", "100", "5", "2", "true"}; String[][] redo=StatFactory.MakeParam(hug, null); 
            concstr.AddStatString(redo);
            return concstr;
            case 3: AttackAb finese= new AttackAb ("Bladed Finese", "single", "enemy", 80, 3); 
            finese.special.add (new DebuffMod (2, 1));
            return finese;
            case 4: AttackAb assass= new AttackAb ("Assassinate", "single", "enemy", 100, 5); 
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
            case 2: DefAb lunarp= new DefAb ("Lunar Protector", "single", "Ally exclusive", 2); 
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