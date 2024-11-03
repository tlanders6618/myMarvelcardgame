package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 7/8/22
 * Filename: Ability_List_Summon
 * Purpose: Lists all summon abilities.
 */
public class Ability_List_Summon
{
    public static Ability GetAb (int index, int counter, boolean copy) //copy is for characters like rogue
    {
        //attack abs construction: String name, String type, String friendly, int dmg, int cooldown
        //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
        //type is single, self, multitarg, random, or aoe  
        switch (index) 
        {
            //2.0
            case 1: return MakeAbNickLMD(counter); 
            case 27: return MakeAbSpiderling(counter); 
            case 28: return MakeAbNaught(counter);
            case 2: return MakeAbRTrooper(counter); 
            case 3: return MakeAbCrushbot(counter); 
            case 4: return MakeAbDrone(counter); 
            case 5: return MakeAbLilDoomie(counter); 
            case 6: return MakeAbDaemon(counter);
            //The rest
            case 7: return null;
            case 12: return MakeAbGiganto(counter);
            case 14: return MakeAbBruin(counter);
            case 15: return MakeAbRinger(counter);
            case 16: return MakeAbSquid(counter);
            default: System.out.println ("Problem getting summon abilities");
        }
        return null;
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
            default: return null; 
        }
    }
    public static Ability MakeAbSquid (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rend= new BasicAb("Tentacle Whip", "single", "enemy", 35);
            return rend;
            case 2: AttackAb r= new AttackAb("Ink Spray", "single", "enemy", 70, 4);
            String[] truth={"Blind", "100", "616", "1", "false"}; String[][] social=StatFactory.MakeParam(truth, null); r.AddStatString(social);
            return r;
            case 3: AttackAb p= new AttackAb("Tentacle Crush", "single", "enemy", 70, 4); 
            String[] i={"Disrupt", "100", "616", "1", "false"}; String[][] z=StatFactory.MakeParam(i, null); p.AddStatString(z);
            return p;
            default: return null; 
        }
    }
    public static Ability MakeAbRinger (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rend= new BasicAb("Ring Shot", "single", "enemy", 35);
            return rend;
            case 1: AttackAb e= new AttackAb("Explosive Rings", "single", "enemy", 30, 2, 1);
            return e;
            case 3: AttackAb c= new AttackAb("Constrictor Rings", "single", "enemy", 80, 4);
            return c;
            default: return null; 
        }
    }
    public static Ability MakeAbBruin (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rend= new BasicAb("Body Slam", "single", "enemy", 35);
            return rend;
            case 1: BasicAb r= new BasicAb("Lacerate", "single", "enemy", 60, 2);
            return r;
            case 3: AttackAb p= new AttackAb("Bellow", "single", "enemy", 70, 4); p.together=true;
            String[] i={"Provoke", "100", "616", "1", "false"}; String[][] z=StatFactory.MakeParam(i, null); p.AddStatString(z);
            String[] truth={"Resistance", "100", "15", "1", "true"}; String[][] social=StatFactory.MakeParam(truth, null); p.AddStatString(social);
            return p;
            default: return null; 
        }
    }
    public static Ability MakeAbGiganto (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rend= new BasicAb("Chomp", "single", "enemy", 60);
            return rend;
            default: return null; 
        }
    }
    public static Ability MakeAbDaemon (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rend= new BasicAb("Unnerving Strike", "single", "enemy", 25); 
            String[] carrot={"Provoke", "100", "616", "1", "false"}; String[][] garrote=StatFactory.MakeParam(carrot, null); rend.AddStatString(garrote);
            return rend;
            case 1: BasicAb blast= new BasicAb("Rend", "single", "enemy", 35); 
            return blast;
            default: return null; 
        }
    }
    public static Ability MakeAbLilDoomie (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb blast= new BasicAb("Electric Blast", "single", "enemy", 35); blast.special.add(new Ignore("Invisible", "always", 616));
            return blast;
            case 1: BasicAb core= new BasicAb("Self Destruct", "single", "enemy", 60); core.ignore=true; core.special.add(new Suicide());
            return core;
            default: return null; 
        }
    }
    public static Ability MakeAbDrone (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb laser= new BasicAb("Laser Beam", "single", "enemy", 30); laser.special.add(new DebuffMod(4));
            laser.desc="100% chance to Copy 1 random buff(s) on the target. \nIf they have none, randomly gain Bulwark, Focus, or Placebo (Buff) for 2 turns.";
            return laser;
            case 1: BasicAb core= new BasicAb("Core Meltdown", "single", "enemy", 60); core.special.add(new DamageCounterRemove("Buffs", true, 10, true, false, false));
            core.special.add(new Suicide()); core.ignore=true; 
            return core;
            default: return null; 
        }
    }
    public static Ability MakeAbCrushbot (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb d= new BasicAb("Crush", "single", "enemy", 35);
            return d;
            case 1: BasicAb c= new BasicAb("Destroy", "lowest", "enemy", 25);
            return c;
            default: return null; 
        }
    }
    public static Ability MakeAbRTrooper (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rpg= new BasicAb("RPG", "single", "enemy", 30); rpg.special.add(new Ricochet(500));
            return rpg;
            case 1: BasicAb aoe= new BasicAb("Rocket Barrage", "AoE", "enemy", 20); 
            return aoe;
            default: return null; 
        }
    }
    public static Ability MakeAbNaught (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rail= new BasicAb("Railgun", "single", "enemy", 30);
            String[] doo={"Tracer", "100", "616", "2", "false"}; String[][] anger=StatFactory.MakeParam(doo, null);
            String[] hungry={"Bleed", "100", "5", "2", "false"}; String[][] revenge=StatFactory.MakeParam(hungry, null);
            rail.AddStatString(anger); rail.AddStatString(revenge);
            return rail;
            case 1: BasicAb mortar= new BasicAb("Mortar Fire", "single", "enemy", 30); mortar.special.add(new DebuffMod(28)); mortar.together=true;
            mortar.desc="100% chance to apply Weakness: 5 for 1 turn(s). 100% chance to apply Provoke for 1 turn(s). \n+1 duration to both if the target is Summoned.";
            return mortar;
            case 3: OtherAb leo= new OtherAb ("Ejector Seat", "self", "self", 0); leo.singleuse=true; leo.channelled=true; leo.special.add(new Transformation(27, true, true));
            return leo;
            default: return null; 
        }
    }
    public static Ability MakeAbSpiderling (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb deploy= new BasicAb("Deploy Spider-Tracers", "single", "enemy", 30); 
            String[] watermelon={"Tracer", "100", "616", "2", "false"}; String[][] anger=StatFactory.MakeParam(watermelon, null); 
            String[] hungry={"Debilitate", "100", "5", "2", "false"}; String[][] revenge=StatFactory.MakeParam(hungry, null);
            deploy.AddStatString(anger); deploy.AddStatString(revenge);
            return deploy;
            case 1: BasicAb duty= new BasicAb("Anti-Symbiote Duty", "single", "enemy", 20); 
            String[] twofer={"Burn", "100", "5", "2", "false"}; String[][] twoface=StatFactory.MakeParam(twofer, null);
            duty.AddStatString(twoface); duty.AddStatString(twoface);
            return duty;
            case 2: OtherAb trans=new OtherAb("Pilot Arachnaught", "self", "self", 0); trans.singleuse=true; trans.channelled=true; 
            trans.special.add(new Transformation(28, true, true));
            return trans;
            default: return null; 
        }
    }
    public static Ability MakeAbNickLMD (int counter)
    { 
        switch (counter)
        {
            case 0: BasicAb pistol= new BasicAb ("Concealed Pistol", "single", "enemy", 35); return pistol;
            case 1: BuffAb cache= new BuffAb ("Hidden Weapons Cache", "single", "ally inclusive", 0); 
            String[] sword= {"Intensify", "100", "15", "1", "knull"}; 
            String[][] redo=StatFactory.MakeParam(sword, null); cache.AddStatString(redo);
            return cache;
            default: return null;
        }
    }
}