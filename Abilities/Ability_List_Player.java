package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 25/7/22
 * Filename: Ability_List_Player
 * Purpose: Creates/lists all character abilities.
 */
public class Ability_List_Player
{
    public static Ability GetAb (int index, int counter)
    {
        //attack abs construction: String name, String type, String friendly, int dmg, int cooldown
        //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
        //type is single, self, multitarg, random, or aoe  
        Ability[] toret= new Ability[1];
        switch (index) //since making a giant array of hundreds of elements was very slow, this should be faster, albeit longer
        {
            case 1: toret[0]=Ability_List_Player.MakeAbMK(counter); break;
            case 2: toret[0]=Ability_List_Player.MakeAbGam(counter); break;
            case 3: toret[0]=Ability_List_Player.MakeAbPun(counter); break;
            case 4: toret[0]=Ability_List_Player.MakeAbIM(counter); break;
            case 5: toret[0]=Ability_List_Player.MakeAbWM(counter); break;
            case 6: toret[0]=Ability_List_Player.MakeAbCap(counter); break;
            case 7: toret[0]=Ability_List_Player.MakeAbFalc(counter); break;
            case 8: toret[0]=Ability_List_Player.MakeAbBucky(counter); break;
            case 9: toret[0]=Ability_List_Player.MakeAbStarLord(counter); break;
            default: System.out.println ("Problem getting abilities");
        }
        return toret[0];
    }    
    public static Ability MakeAbName (int counter)
    {
        switch (counter)
        {
            case 0:
            case 1:
            case 2: 
            case 3: 
            case 4: 
        }
        return null; 
    }
    public static Ability MakeAbStarLord (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb talon= new BasicAb ("Jet Slam", "single", "enemy", 45);
            return talon;
            case 1: HealAb pump= new HealAb ("Pump It Up", "AoE", "ally inclusive", 3); String[] dance= StatFactory.SetParam("Recovery", "300", "10", "2", "false");
            pump.AddStatString(dance);
            return pump;
            case 2: return null;
            case 3: AttackAb clarice= new AttackAb ("Clarice", "single", "enemy", 90, 3); String[] one=StatFactory.SetParam ("Burn", "100", "15", "2", "false");
            String[] two=StatFactory.SetParam ("Stun", "100", "616", "616", "false"); clarice.special.add(new ChooseStat (one, two, null));
            return clarice;
            case 4: AttackAb terry= new AttackAb ("Terry", "single", "enemy", 90, 3); String[] uno=StatFactory.SetParam ("Wound", "100", "1", "616", "false");
            String[] dos=StatFactory.SetParam ("Disrupt", "100", "1", "616", "false"); terry.special.add(new ChooseStat (uno, dos, null));
            return terry;
        }
        return null; 
    }
    public static Ability MakeAbBucky (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb shot= new BasicAb ("Precision Shot", "single", "enemy", 45);
            return shot;
            case 1: DebuffAb gren= new DebuffAb ("Grenade Lob", "single", "enemy", 0); 
            String[] explode= StatFactory.SetParam("Countdown", "100", "55", "2", "false", "Ricochet"); gren.AddStatString(explode);
            return gren; 
            case 2: AttackAb expert= new AttackAb("Expert Marksman", "single", "enemy", 35, 2); 
            String[] decoy=StatFactory.SetParam("Countdown", "100", "35", "2", "false", "knull"); expert.AddStatString(decoy); 
            String[] blood= new String[2];
            String[] privy= new String[2]; 
            blood[0]="Countdown"; privy[0]="Debuffs"; blood[1]="Countdown"; privy[1]="Other";
            expert.special.add(new Extend (300, false, "2", blood, privy, 1, true));
            return expert;
            case 3: OtherAb det= new OtherAb("Detonator", "single", "enemy", 2); det.special.add(new Activate (false, "Countdown", 70)); det.attack=true;
            return det;
            case 4: return null;
        }
        return null; 
    }
    public static Ability MakeAbFalc (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb bomb= new BasicAb ("Divebomb", "single", "enemy", 35); String[] weak= StatFactory.SetParam("Weakness", "100", "10", "1", "false");
            bomb.AddStatString(weak);
            return bomb;
            case 1: BasicAb talon= new BasicAb ("Talon Strike", "single", "enemy", 45);
            return talon;
            case 2: return null;
            case 3: DebuffAb eagle= new DebuffAb ("Eagle Eyed", "single", "enemy", 3); String[] ratg=StatFactory.SetParam("Target", "100", "20", "1", "false");
            eagle.AddStatString (ratg);
            return eagle;
            case 4: AttackAb swarm= new AttackAb ("Bird Swarm", "AoE", "enemy", 70, 4); String[] scary=StatFactory.SetParam("Chance Down", "50", "1", "616", "false");
            swarm.together=false; swarm.AddStatString(scary);
            return swarm;
        }
        return null; //in case something goes wrong, the default is null
    }
    public static Ability MakeAbCap (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb toss= new BasicAb ("Shield Throw", "single", "enemy", 40); toss.special.add(new Ricochet (300)); 
            return toss;
            case 1: AttackAb bash= new AttackAb ("Shield Bash", "single", "enemy", 90, 3); String[] stunner= StatFactory.SetParam("Stun", "100", "616", "616", "false");
            bash.AddStatString (stunner); 
            return bash;
            case 2: return null; 
            case 3: DefAb star= new DefAb ("Star Spangled Avenger", "self", "self", 3); String[] banner=StatFactory.SetParam("Taunt", "300", "1", "616", "true");
            star.AddStatString(banner); String[] whelmed= StatFactory.SetParam("Resistance", "300", "10", "1", "true"); star.AddStatString(whelmed);
            return star;
            case 4: HealAb lib= new HealAb ("Sentinel of Liberty", "AoE", "ally inclusive", 3); lib.special.add(new Confidence(300, 30));
            return lib;
        }
        return null; //in case something goes wrong, the default is null
    }
    public static Ability MakeAbWM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pc= new BasicAb ("Plasma Cutter", "single", "enemy", 45); 
            return pc;
            case 1: BasicAb gat= new BasicAb ("Gatling Guns", "AoE", "enemy", 35); 
            return gat;
            case 2: return null;
            case 3: AttackAb ft= new AttackAb ("Flamethrower", "single", "enemy", 80, 3); String[] pyre=StatFactory.SetParam("Burn", "100", "20", "1", "false");
            ft.AddStatString (pyre); 
            return ft;
            case 4: AttackAb army= new AttackAb ("Army of One", "single", "enemy", 120, 4); String[] holy=new String[1]; holy[0]="all";
            army.special.add (new BeforeNullify (100, "all", false, holy, true)); 
            return army;
        }
        return null; //in case something goes wrong, the default is null
    }
    public static Ability MakeAbIM (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rep= new BasicAb ("Repulsor Blast", "single", "enemy", 50); 
            return rep;
            case 1: BasicAb miss= new BasicAb ("Micro-Missile Barrage", "AoE", "enemy", 40); 
            return miss; 
            case 2: HealAb recharge= new HealAb ("Recharge", "self", "self", 3); recharge.special.add (new Confidence (300, 30));  
            return recharge;
            case 3: BuffAb boost= new BuffAb ("Power Boost", "self", "self", 3); String[] Antidisestablishmentarianism= StatFactory.SetParam("Damage Up", "300", "30", "1", "true");
            boost.AddStatString (Antidisestablishmentarianism); boost.special.add (new SelfDMG (30));
            return boost;
            case 4: AttackAb uni= new AttackAb ("Unibeam", "single", "enemy", 100, 3); 
            return uni;
        }
        return null; //in case something goes wrong, the default is null
    }
    public static Ability MakeAbPun (int counter)
    {
        switch (counter)
        {
            case 0: AttackAb molotov= new AttackAb ("Molotov Cocktail", "single", "enemy", 50, 2); String[] rend=StatFactory.SetParam("Burn", "50", "10", "2", "false");
            molotov.AddStatString (rend); 
            return molotov;
            case 1: AttackAb supfire= new AttackAb ("Suppressing Fire", "AoE", "enemy", 50, 2); 
            return supfire;
            case 2: BuffAb punwep= new BuffAb ("Weapons Expert", "self", "self", 0); String[] asunder=StatFactory.SetParam("Chance Up", "100", "2", "616", "true");
            String[] eviscerate=StatFactory.SetParam("Damage Up", "100", "15", "2", "true"); punwep.AddStatString (asunder); punwep.AddStatString (eviscerate); 
            punwep.together=false; 
            return punwep;
            case 3: AttackAb punished= new AttackAb ("Punished", "single", "enemy", 50, 2); String[] shred= StatFactory.SetParam("Bleed", "50", "10", "2", "false");
            punished.AddStatString (shred); 
            return punished;
            case 4: AttackAb retri= new AttackAb ("Retribution", "single", "enemy", 120, 5); 
            return retri;
        }
        return null;
    }
    public static Ability MakeAbGam (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb concstr= new BasicAb ("Concussive Strike", "single", "enemy", 45); 
            return concstr;
            case 1: BasicAb gam= new BasicAb ("Pommel Smash", "single", "enemy", 35); String[] hug=StatFactory.SetParam("Damage Up", "100", "5", "2", "true"); 
            gam.AddStatString(hug);
            return gam;
            case 2: return null;
            case 3: AttackAb gbfin= new AttackAb ("Bladed Finese", "single", "enemy", 70, 3); String[] mellow=StatFactory.SetParam ("Bleed", "50", "15", "2", "false");
            gbfin.AddStatString(mellow);
            return gbfin;
            case 4: AttackAb assass= new AttackAb ("Assassinate", "single", "enemy", 90, 5); String[] candy= StatFactory.SetParam ("Bleed", "50", "40", "1", "false");
            assass.AddStatString(candy); 
            return assass;
        }
        return null;
    }
    public static Ability MakeAbMK (int counter) 
    {
        String[] p= new String[5];
        switch (counter)
        {
            case 0: BasicAb cdart =new BasicAb ("Crescent Dart", "single", "enemy", 35); String[] despair=StatFactory.SetParam("Bleed", "100", "10", "1", "false");
            cdart.AddStatString(despair); 
            return cdart; 
            case 1: BasicAb mbarr= new BasicAb("Mooncopter Barrage", "AoE", "enemy", 35);   
            return mbarr; 
            case 2: DefAb lunarp= new DefAb ("Lunar Protector", "single", "Ally exclusive", 2); String[] gloom=StatFactory.SetParam ("Protect", "300", "1", "616", "true");
            lunarp.AddStatString(gloom); 
            return lunarp; 
            case 3: return null; 
            case 4: AttackAb Khonshu= new AttackAb ("Fist of Khonshu", "single", "enemy", 110, 4); String[] loss=StatFactory.SetParam("Stun", "100", "616", "616", "false");
            Khonshu.AddStatString (loss); 
            return Khonshu;
        }
        return null;
    }    
}
