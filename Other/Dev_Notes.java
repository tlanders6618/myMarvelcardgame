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
     * make sure all basicabs work with assist as intended from now on
     * 
     * Always add ricochet before multichain in an ability's afterabs
     * always add ignore miss as an ability's first beforeab OR change every miss to check for ignoring miss and remove immunity to miss from game
     * iceman's passive can just use debuffmod for deciding whether to apply freeze or not
     * 
     * To Do
     * when adding vulnerable remember to make its effect unaffected by strength bc amplify
     * If a character is the only one on their team, they cannot be Banished. All characters on a team cannot be Banished; banish interact already accounted for by targeting stuff
     * 
     * Keep in mind:
     * For unique Other effects, check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * New characters need inhp and setname, then passive descs and abs, then passives + adding them to hero methods, then bug testing and finally check balancing before release
     * All AoE attack abs call the attack method, which contains onattack, so all AoE attacks trigger onattack once for each target hit, instead of once after the attack ends
     * Passives should not apply if Stunned
     * 
     * */
     
} 