package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 7/8/22
 * Filename: Ability_List_Summon
 * Purpose: Lists all summon abilities.
 */
public class Ability_List_Summon
{
    public static Ability GetAb (int index, int counter)
    {
        //attack abs construction: String name, String type, String friendly, int dmg, int cooldown
        //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
        //type is single, self, multitarg, random, or aoe  
        Ability[] toret= new Ability[1];
        switch (index) //since making a giant array of hundreds of elements was very slow, this should be faster, albeit longer
        {
            case 1: toret[0]=Ability_List_Summon.MakeAbNickLMD(counter); break;
            default: System.out.println ("Problem getting summon abilities");
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
    public static Ability MakeAbNickLMD (int counter)
    {
        switch (counter)
        {
            case 0: BasicAb pistol= new BasicAb ("Concealed Pistol", "single", "enemy", 30); return pistol;
            default: return null;
        }
    }
}