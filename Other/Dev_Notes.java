package myMarvelcardgamepack;
public class Dev_Notes
{
    /*
     * ***FILES***
     * Abilities (13):
     * -Ability (update)
     * -AbilityListPlayer (update)
     * -AbilityListSummon
     * -AfterAbility
     * -AttackAb
     * -BasicAb
     * -BeforeAbility
     * -BuffAb
     * -DebuffAb
     * -DefAb
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
     * -CardCode
     * -CardSelection (doc done)
     * -CoinFlip
     * -DamageStuff
     * -Scoreboard
     * 
     * Other (2):
     * -DevNotes
     * -Test
     * 
     * Status Effects (7):
     * -BuffEff (doc done)
     * -DebuffEff (doc done)
     * -DefEff (doc done)
     * -HealEff (doc done)
     * -OtherEff (doc done)
     * -StatEff
     * -StatFactory
     * 
     * New characters need inhp and setname, then passive descs and abs, then passives + adding them to hero methods, then bug testing and finally check balancing before release
     * 
     * all new methods go under cardcode which is empty for some reason
     * 
     * always add ignore miss as an ability's first beforeab OR change every miss to check for ignoring miss and remove immunity to miss from game
     * iceman's passive can just use debuffmod for deciding whether to apply freeze or not
     * healthbar swap should not work on "No Heal" binary or max hp reduction immunity
     * 
     * Keep in mind:
     * For unique Other effects, check for their interaction methods, e.g. characters cannot evade while submerged so change evade criteria
     * Sourceless damage (e.g. reflection) must print the took damage statement on their own; tookdamage will not
     * All AoE attack abs call the attack method, which contains onattack, so all AoE attacks trigger onattack once for each target hit, instead of once after the attack ends
     * Passives should not apply if Stunned
     * Always put break after switch statements; half of the bugs related to passives are the result of this
     * 
     * */
     
} 
