package myMarvelcardgamepack;
public class Dev_Notes
{
    /** 
     * un-uncomment the thing in battle ALWAYS
     * 
     * To Do
     * BANISH MUST --P1HEROES WHEN APPLIED
     * make boolean aoe and boolean attack attributes of all abs by default
     * check for blind before doing beforeabs and make damage counter not trigger on blind
     * make tracker stateff
     * change empowerments to work with aoe across the board
     * change skip turn criteria
     * make shatter method
     * merge attack ignore shield with normal attack method
     * add altname for stateff and check for immunity to it when applying stateffs
     * add alttype (damaging vs not) for stateff
     * double check glossary desc before doing assist and multitarget
     * card selection doesnt distinguish between taunt def and taunt other
     * 
     * Before adding any new content:
     * Add status effects to the game (and check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria)
     * Be careful about typos (e.g. !(contains) instead of contains); re read everything
     * Remember that characters' turn orders are set to 616 at death; on rez, they need to be reset     
     * Make sure to add new status effects to the factory  
     * When making changes to status effects, make the change to its Other counterpart too (e.g. change ShatterE when changing Shatter)
     * When making changes to character methods make the change to the corresponding overriden Summon methods too
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * 
     * Known issues   
     * SHATTER ATTACKS MUST IGNORE EVADE IF SUCCESSFULLY APPLIED but in lower case (make method)
     * lunar protector doesn't trigger if the protected target is killed by the attack
     * 
     * Balance changes:
     * 
     * Edited (i.e. need to update):
     * 
     * to test:
     * counter attack must trigger the took damage method for the attacker, with boolean true
     * 
     * Think about vapor and gambit ctd
     * 
     * Preparation for checkfor -> check binaries conversion
     * ability
     * character
     * card selection
     * stateff
     * battle (banish)
     * 
     * */
} 
