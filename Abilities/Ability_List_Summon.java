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
            case 1: toret=Ability_List_Summon.MakeAbNickLMD(counter); break;
            case 7: return null;
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