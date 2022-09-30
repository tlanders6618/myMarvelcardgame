package myMarvelcardgamepack;
public class Dev_Notes
{
    /**
     * To Do
     * need to add turnend method 
     * change empowerments to work with aoe across the board
     * change skip turn criteria
     * fix evade
     * fix shatter
     * make ws abilities
     * ws needs a passive
     * test sl and ws before rolling out
     * BEFORE DOING FURY OVERRIDE SUMMON METHODS
     * 
     * Before adding any new content:
     * Add status effects to the game (and check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria)
     * Be careful about typos (e.g. !(contains) instead of contains); re read everything
     * Remember that characters' turn orders are set to 616 at death; on rez, they need to be reset     
     * Make sure to add new status effects to the factory first
     * Evade method justevaded binary eff should be cleared on turnend
     * When making changes to status effects, make the change to its Other counterpart too (e.g. change ShatterE when changing Shatter)
     * When making changes to character methods (especially with passives), make the change to the corresponding overriden Summon methods too
     * 
     * Known issues   
     * SHATTER MUST IGNORE EVADE IF SUCCESSFULLY APPLIED
     * nullify, steal, etc should not ignore evade
     * status effects are not meant to be applied to heroes who evade
     * 
     * Balance changes
     * reduce cd of im boost ?
     * make sl wound 1 turn (was 2)
     * 
     * Edited (i.e. need to update):
     * debuffeff (countdown)
     * stateff (checkapply, checkstacking, applyfail, statfactory)
     * ability (getranomdenemy, ablistplayer)
     * */
} 
