package myMarvelcardgamepack;
public class Dev_Notes
{
    /** 
     * un-uncomment the thing in battle ALWAYS
     * 
     * To Do
     * MAKE LUNAR PROTECTOR A BINARY
     * overide turn method with true/false for qs
     * change empowerments to work with aoe across the board
     * change skip turn criteria
     * make shatter method
     * fix returndamage method for ricochet
     * merge attack ignore shield with normal attack method
     * BEFORE DOING FURY OVERRIDE SUMMON METHODS
     * add altname for stateff
     * add alttype (damageing vs not) for stateff
     * double check glossary desc before doing assist and multitarget
     * counter attack must trigger the took damage method for the attacker, with boolean true
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
     * SHATTER ATTACKS MUST IGNORE EVADE IF SUCCESSFULLY APPLIED but in lower case
     * terror and provoke should not work if the caster is dead (@Override if hero who applied is dead return 616)
     * 
     * Balance changes
     * reduce cd of im boost ?
     * wm passive and ws det now inescapable
     * add inescapable (ig target eff, plus blind, plus invis)
     * 
     * Edited (i.e. need to update):
     * 
     * Think about vapor and gambit ctd
     * 
     * Preparation for checkfor -> check binaries conversion
     * ability
     * character
     * stateff
     * battle (banish)
     * 
     * */
} 
