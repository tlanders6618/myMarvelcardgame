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
        Ability toret= null;
        switch (index) 
        {
            case 1: toret=MakeAbNickLMD(counter); break;
            case 7: return null;
            case 27: toret=MakeAbSpiderling(counter); break;
            case 28: toret=MakeAbNaught(counter); break;
            default: System.out.println ("Problem getting summon abilities");
        }
        return toret;
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
    public static Ability MakeAbNaught (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb rail= new BasicAb("Railgun", "single", "enemy", 30);
            String[] doo={"Tracer", "100", "616", "2", "false"}; String[][] anger=StatFactory.MakeParam(doo, null);
            String[] hungry={"Bleed", "100", "5", "2", "false"}; String[][] revenge=StatFactory.MakeParam(hungry, null);
            rail.AddStatString(anger); rail.AddStatString(revenge);
            return rail;
            case 1: BasicAb mortar= new BasicAb("Mortar Fire", "single", "enemy", 25); mortar.special.add(new DebuffMod(28, 616)); mortar.together=true;
            return mortar;
            case 3: OtherAb leo= new OtherAb ("Ejector Seat", "self", "self", 0); leo.singleuse=true; leo.channelled=true; leo.special.add(new Transformation(27, true));
            return leo;
        }
        return null; 
    }
    public static Ability MakeAbSpiderling (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb deploy= new BasicAb("Deploy Spider-Tracers", "single", "enemy", 25); 
            String[] watermelon={"Tracer", "100", "616", "2", "false"}; String[][] anger=StatFactory.MakeParam(watermelon, null); 
            String[] hungry={"Debilitate", "100", "5", "2", "false"}; String[][] revenge=StatFactory.MakeParam(hungry, null);
            deploy.AddStatString(anger); deploy.AddStatString(revenge);
            return deploy;
            case 1: BasicAb duty= new BasicAb("Anti-Symbiote Duty", "single", "enemy", 15); 
            String[] twofer={"Burn", "100", "5", "2", "false"}; String[][] twoface=StatFactory.MakeParam(twofer, null);
            duty.AddStatString(twoface); duty.AddStatString(twoface);
            return duty;
            case 2: OtherAb trans=new OtherAb("Pilot Arachnaught", "self", "self", 0); trans.singleuse=true; trans.channelled=true; trans.special.add(new Transformation(28, true));
            return trans;
        }
        return null; 
    }
    public static Ability MakeAbNickLMD (int counter)
    { 
        switch (counter)
        {
            case 0: BasicAb pistol= new BasicAb ("Concealed Pistol", "single", "enemy", 30); return pistol;
            case 1: BuffAb cache= new BuffAb ("Hidden Weapons Cache", "single", "ally inclusive", 0); 
            String[] sword= {"Intensify", "100", "10", "1", "knull"}; 
            String[][] redo=StatFactory.MakeParam(sword, null); cache.AddStatString(redo);
            return cache;
            default: return null;
        }
    }
}