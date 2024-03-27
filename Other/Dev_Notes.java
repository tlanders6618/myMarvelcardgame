package myMarvelcardgamepack;
public class Dev_Notes
{
    /** 
     * ***FILES***
     * Abilities (13):
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
     * Characters (6):
     * -ActivePassive
     * -Character
     * -Hero
     * -StaticPassive
     * -Summon
     * -SummonPassive
     * 
     * Combat (7):
     * -Battle 
     * -CardGameMain
     * -CardHashcode
     * -CardSelection
     * -CoinFlip
     * -DamageStuff
     * -Scoreboard
     * 
     * Other (2):
     * -DevNotes
     * -Test
     * 
     * Status Effects (7):
     * -BuffEff
     * -DebuffEff
     * -DefEff
     * -HealEff
     * -OtherEff
     * -StatEff
     * -StatFactory
     * 
     * all new methods go under cardhashcode which is empty for some reason
     * 
     * Always add ricochet before multichain in an ability's afterabs
     * always add ignore miss as an ability's first beforeab OR change every miss to check for ignoring miss and remove immunity to miss from game
     * daredevil blindside empowerment has to add an obj to beforeab that makes the ab miss; cannot add missed without letting beforeab make char immune to miss
     * iceman's passive can just use debuffmod for deciding whether to apply freeze or not
     * 
     * To Do
     * double check glossary desc before doing multitarget
     * attack method no longer trigger onattacked for aoe abs; attackab must trigger it if ab is aoe
     * 
     * Keep in mind:
     * For unique Other effects, check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * 
     * Known issues   
     * using counter causes null exception, most likely due to checking for a stateff to apply and finding none
     * 
     * to test:
     * counter attack must trigger the took damage method for the attacker, with boolean true
     * drain works
     * 
     * */
} 