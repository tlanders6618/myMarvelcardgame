package myMarvelcardgamepack;
public class Dev_Notes
{
    /**
     * To Do
     * make evade method
     * learn how others can download code and launch game
     * need to add turnend method 
     * change empowerments to work with aoe across the board
     * change skip turn criteria
     * Add wound
     * Add sl abilities
     * add sl index to card selection
     * add sl passive
     * Replace attackab, basicab, and ability with java files so I can read
     *
     * Edited
     * Debuffeff
     * Ability
     * Ability list player
     * Ability list summon
     * Damage stuff
     * Added recovery to heal eff
     * AttackAb now has tempstrings and addtempstring method; all abs must have same
     * new beforeab choosestat for sl
     * minor comment to special ab
     * 
     * Before adding any new content:
     * Add status effects to the game (and check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria)
     * When adding passives, make sure Summons already have that method overloaded so there isn't switch confusion
     * Be careful about typos (e.g. !(contains) instead of contains); re read everything
     * Remember that characters' turn orders are set to 616 at death; on rez, they need to be reset     
     * Make sure to add new status effects to the factory first
     * Evade method justevaded binary eff should be cleared on turnend
     * When making changes to status effects, make the change to its Other counterpart too (e.g. change ShatterE when changing Shatter)
     * 
     * Known issues   
     * SHATTER MUST IGNORE EVADE IF SUCCESSFULLY APPLIED
     * nullify, steal, etc should not ignore evade
     * status effects are not meant to be applied to heroes who evade
     * 
     * Balance changes
     * reduce cd of im boost ?
     * 
     * */
} 
