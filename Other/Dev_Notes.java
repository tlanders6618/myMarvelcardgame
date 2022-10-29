package myMarvelcardgamepack;
public class Dev_Notes
{
    /** 
     * un-uncomment the thing in battle ALWAYS
     * 
     * To Do
     * need to add turnend method 
     * overide turn method with true/false for qs
     * change empowerments to work with aoe across the board
     * change skip turn criteria
     * make evade method (Evade method justevaded binary eff should be cleared on turnend)
     * make shatter method
     * BEFORE DOING FURY OVERRIDE SUMMON METHODS
     * add altname for stateff
     * attack methods must all have evasion consideration added
     * attackigdef maynot count for bd or crit
     * 
     * Before adding any new content:
     * Add status effects to the game (and check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria)
     * Be careful about typos (e.g. !(contains) instead of contains); re read everything
     * Remember that characters' turn orders are set to 616 at death; on rez, they need to be reset     
     * Make sure to add new status effects to the factory  
     * When making changes to status effects, make the change to its Other counterpart too (e.g. change ShatterE when changing Shatter)
     * When making changes to character methods (especially with passives), make the change to the corresponding overriden Summon methods too
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * 
     * Known issues   
     * SHATTER ATTACKS MUST IGNORE EVADE IF SUCCESSFULLY APPLIED but in lower case
     * nullify, steal, etc should not ignore evade
     * status effects are not meant to be applied to heroes who evade
     * terror and provoke should not work if the caster is dead
     * 
     * Balance changes
     * reduce cd of im boost ?
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
