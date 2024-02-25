package myMarvelcardgamepack;
public class Dev_Notes
{
    /** 
     * ***FILES***
     * Abilities:
     * -Ability
     * -AbilityListPlayer
     * -AbilityListSummon
     * -AfterAbility
     * -AttackAb
     * -BasicAb
     * -BeforeAbility
     * -BuffAb
     * -DebuffAb
     * -DefenceAb
     * -HealAb
     * -OtherAb
     * -SpecialAbility
     * 
     * Characters:
     * -ActivePassive
     * -Character
     * -Hero
     * -StaticPassive
     * -Summon
     * -SummonPassive
     * 
     * Combat:
     * -Battle 
     * -CardGameMain
     * -CardHashcode
     * -CardSelection
     * -CoinFlip
     * -DamageStuff
     * -Scoreboard
     * 
     * Other:
     * -DevNotes
     * -Test
     * 
     * Status Effects:
     * -BuffEff
     * -DebuffEff
     * -DefEff
     * -HealEff
     * -OtherEff
     * -StatEff
     * -StatFactory
     * 
     * 
     * all new methods go under cardhashcode which is empty for some reason
     * 
     * Snared 3rd in turn order gets moved to first and everyone else moves down one in array
     * daredevil blindside empowerment has to add an obj to beforeab that makes the ab miss; cannot add missed without letting beforeab make char immune to miss
     * 
     * To Do
     * fix turn skip criteria; think about elixir and chars with no attack abs
     * add altname for stateff and check for immunity to it when applying stateffs
     * double check glossary desc before doing assist and multitarget
     * card selection doesnt distinguish between taunt def and taunt other (tauntE doesnt exist)
     * change counter and reflect to work with attacked method instead of manually checking for them
     * change untargetable to check for binaries instead of having no counter
     * rewrite lose hp attack option
     * attack method no longer trigger onattacked for aoe abs; attackab must trigger it if ab is aoe
     * 
     * Keep in mind:
     * Add status effects to the game (and check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria)
     * When making changes to status effects, make the change to its Other counterpart too (e.g. change ShatterE when changing Shatter)
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * 
     * Known issues   
     * using counter causes null exception, most likely due to checking for a stateff to apply and finding none
     * x23 multichain doesn't do third hit or keeps going until 3 kills or hits two different enemies
     * something wrong with x23 crit calc
     * 
     * Balance changes:
     * x23 regen strength
     * berserker frenzy and gamora and modern fury kill mode trigger while stunned update passive desc 
     * 
     * 
     * Edited (i.e. need to update):
     * EVERYTHING
     * 
     * to test:
     * counter attack must trigger the took damage method for the attacker, with boolean true
     * does tracker display, update properly?
     * berserker rage triggers once
     * x23 crit chance triggers once
     * drain works
     * 
     * Always add ricochet before multichain in an ability's afterabs
     * always add ignore miss as an ability's first beforeab
     * 
     * Preparation for checkfor -> check binaries conversion
     * ability
     * character
     * hero
     * summon
     * card selection
     * stateff
     * battle
     * 
     * */
} 