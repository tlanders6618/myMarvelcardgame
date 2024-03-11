package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 26/7/22
 * Filename: Summon
 * Purpose: To make summons.
 */
import java.util.ArrayList; 
public class Summon extends Character
{
    Character mysummoner;
    public Summon (int Sindex)
    {
        //Same as with characters
        //Need to manually assign team affiliation when creating a summon; not done in constructor
        index=Sindex;
        summoned=true;
        hash=Card_HashCode.RandomCode();
        size=SetSizeSum(index);
        if (Sindex==13)
        {
            //clone health, name, and abilities depend on who they're a clone of
        }
        else
        {
            Cname=SetName(index, true);
            HP=InHP (index, true);
            abilities=Ability.AssignAbSum(index);
        }
        maxHP=HP;
    }
    public void onSummon (Summon lad)
    {
        switch (lad.index) //for triggering on summon passives
        {
            case 1: SummonPassive.NickLMD(lad); break;
            case 7: lad.binaries.add("Stunned"); break;
        }
        CheckSumDupes(lad);
    }
    public void CheckSumDupes (Character lad)
    {
        int dupes=0; //number of duplicate summons
        if (lad.team1==true) 
        {
            for (Character hero: Battle.team1)
            {
                if (hero!=null&&hero.summoned==true&&hero.index==lad.index)
                {
                    String name= hero.SetName(hero.index, true); //check the summoned hero's original name to compare with the new summon's original name
                    if (name.equals(lad.Cname))
                    {
                        ++dupes;
                    }
                }
            }
            for (Character hero: Battle.team1dead)
            {
                if (hero!=null&&hero.summoned==true&&hero.index==lad.index)
                {
                    String name= hero.SetName(hero.index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++dupes;
                    }
                }
            }            
        }
        else
        {
            for (Character hero: Battle.team2)
            {
                if (hero!=null&&hero.summoned==true&&hero.index==lad.index)
                {
                    String name= hero.SetName(hero.index, true); 
                    if (name.equals(lad.Cname))
                    {
                        ++dupes;
                    }
                }
            }
            for (Character hero: Battle.team2dead)
            {
                if (hero!=null&&hero.summoned==true&&hero.index==lad.index)
                {
                    String name= hero.SetName(hero.index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++dupes;
                    }
                }
            }
        }
        if (dupes>1) //this is so summons don't all have the same name (e.g. three enemies named "Thug")
        {
            lad.Cname=Summon.AlterSumName(lad.index, dupes);
        }
    }
    public static String AlterSumName (int index, int counter) //adds a number to summon name to avoid confusion; e.g. Thug 1, Thug 2
    {
        String name;
        switch (index)
        {
            case 1: name="Nick Fury LMD "+counter+" (Summon)"; break;
            case 2: name="AIM Rocket Trooper "+counter+"(Summon)";break;
            case 3: name="AIM Crushbot "+counter+" (Summon)"; break;
            case 4: name="Ultron Drone "+counter+"(Summon)"; break;
            case 5: name="Doombot "+counter+" (Summon)"; break;
            case 6: name="Lesser Demon "+counter+" (Summon)"; break;
            case 7: name="Holographic Decoy "+counter+" (Summon)"; break;
            case 8: name="Ice Golem "+counter+" (Summon)"; break;
            case 9: name="HYDRA Trooper "+counter+" (Summon)"; break;
            case 10: name="Thug "+counter+" (Summon)"; break;
            case 11: name="Mirror Image "+counter+" (Summon)"; break;
            case 12: name="Giganto "+counter+" (Summon)"; break;
            case 27: name="Spiderling "+counter+" (Summon)"; break;
            case 28: name="Arachnaught "+counter+" (Summon)"; break;
            default: name="Error altering Summon name";
        }    
        return name;
    }
    public static int SetSizeSum (int index)
    {
        switch (index)
        {
            case 12: case 13: case 14: case 15: case 16:
            index=2; break;
            default: index=1;
        }
        return index;
    }
    //Below are overridden to avoid conflict between summmon and hero indexes
    @Override
    public void add (Character hero, StatEff eff) //adding a stateff
    {
        hero.effects.add(eff); 
        String type=eff.getefftype();
        for (StatEff e: hero.effects) //for stateffs that react to other stateffs
        {
            e.Attacked(eff);
        }
        if (!(eff.getefftype().equals("Secret"))&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))) 
        //due to taunt/protect interaction; no point in announcing it being added if it's instantly removed
        {
            System.out.println ("\n"+hero.Cname+" gained a(n) "+eff.geteffname());
        }
        eff.onApply(hero);
    }
    @Override
    public void remove (Character hero, int removalcode, String how) //removes status effects
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.hashcode==removalcode)
            {
                String name=eff.getimmunityname(); String type=eff.getefftype();
                eff.Nullified(hero);
                //redundant to print the message if something like nullify already does, so it's only printed if nullify is false
                if (how.equals("normal")&&!(eff.getimmunityname().equals("Protect")||eff.getimmunityname().equals("Evade")))                 
                {
                    System.out.println (hero.Cname+"'s "+eff.getimmunityname()+" expired."); 
                }      
                hero.effects.remove(eff);
                break; //end the for each loop
            }
        }
    }
    @Override
    public void onTurn (Character hero, boolean notbonus)
    {
        boolean go=true;
        switch (index)
        {
            case 7: 
            if (hero.team1==true) 
            {
                if (Battle.p1solo==true) //to prevent infinite turns if a decoy is the only ones left alive on its team
                hero.binaries.remove("Stunned");
                else //to prevent infinite turns if there is more than one hero on the team, but they're all decoys
                {
                    Character[] team=Battle.GetTeammates(hero); boolean solo=true;
                    for (Character c: team)
                    {
                        if (c!=null&&(c.summoned==false||c.index!=7))
                        {
                            solo=false; break;
                        }
                    }
                    if (solo==true)
                    hero.binaries.remove("Stunned");
                 }
            }
            else
            {
                if (Battle.p2solo==true) 
                hero.binaries.remove("Stunned");
                else 
                {
                    Character[] team=Battle.GetTeammates(hero); boolean solo=true;
                    for (Character c: team)
                    {
                        if (c!=null&&(c.summoned==false||c.index!=7))
                        {
                            solo=false; break;
                        }
                    }
                    if (solo==true)
                    hero.binaries.remove("Stunned");
                }
            }
            break;
        }
        if (go==true)
        ++hero.turn;
        super.onTurn(hero, notbonus);
    }
    @Override
    public void onTurnEnd (Character hero, boolean notbonus)
    {
    }
    @Override
    public void onAllyTurn (Character ally, Character hero, boolean summoned) //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    {
    }
    @Override
    public void onEnemyTurn (Character enemy, Character hero, boolean summoned)
    {
    }
    @Override
    public void onFightStart(Character hero)
    {
    }
    @Override
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe)
    {
        //for normal damage from attack skills
        target=target.onTargeted(dealer, target, dmg, aoe);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed")))) //only check if dealer isn't immune to miss and hasn't missed already; can't miss twice
        {
            Damage_Stuff.CheckEvade(dealer, target); //blind is checked when activating the ab; evade is checked here once the target has been selected
        }
        if ((!(dealer.binaries.contains("Missed")))&&target.dead==false)
        {
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
            dmg=Damage_Stuff.CheckGuard(dealer, target, dmg);      
            for (SpecialAbility h: target.helpers) //for special things like redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
            dmg=target.TakeDamage(target, dealer, dmg, aoe);
        }
        else
        dmg=0;
        dealer.activeability.ReturnDamage(dmg); //tells the ability how much dmg the attack did
        dealer.onAttack(dealer, target); //activate relevant passives
        if (target.dead==false)
        {
            target.onAttacked(target, dealer, dmg);
        }
        Character[] friends=Battle.GetTeammates(target);
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyAttacked(friend, target, dealer, dmg);
            }
        }
        if (dealer.dead==false)
        {
            dealer.CheckDrain(dealer, target, dmg);
        }
        return target;
    }
    @Override
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe)
    {
        //modified version of attack method since debuff skills do no damage
        target=target.onTargeted(dealer, target, 0, aoe);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            Damage_Stuff.CheckEvade(dealer, target);
        }
        target.onAttacked(target, dealer, 0);
        dealer.onAttack(dealer, target);
        return target;
    }
    @Override
    public void onAttack (Character hero, Character victim)
    {
        //triggered after a hero attacks
    }
    @Override
    public void onCrit (Character hero, Character target)
    {
    }
    @Override
    public void onAttacked(Character attacked, Character attacker, int dmg)
    {
        if (attacked.dead==false)
        {
            for (StatEff eff: attacked.effects)
            {
                eff.Attacked(attacked, attacker, dmg);
            }
        } 
    }
    @Override
    public void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg)
    {
    }
    @Override
    public void HPChange (Character hero, int oldhp, int newhp)
    {
    }
    @Override
    public Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe)
    {
        Character ntarget=target;      
        return ntarget;
    }
    @Override
    public int TakeDamage (Character target, Character dealer, int dmg, boolean aoe) //true for aoe, false for single target
    {
        //this is the default method for taking damage
        //this checks if shield is strong enough to prevent health damage from an enemy attack
        if (dealer.ignores.contains("Shield")||dealer.ignores.contains("Defence"))
        {
            target.HP-=dmg; 
        }
        else if (SHLD>=dmg&&dmg>0) 
        {
            target.SHLD-=dmg; 
        }
        else if (SHLD<dmg&&dmg>0) //shield broken; can't absorb all the damage
        {
            target.HP=(target.HP+target.SHLD)-dmg; 
            target.SHLD=0;
        }
        target.TookDamage(target, dealer, dmg);
        return dmg;
    }
    @Override
    public int TakeDamage (Character target, int dmg, boolean dot) //true for dot and false for all other types of dmg
    {
        //for taking sourceless damage
        if (SHLD>=dmg&&dmg>0) 
        {
            target.SHLD-=dmg; 
        }
        else if (SHLD<dmg&&dmg>0)
        {
            target.HP=(target.HP+target.SHLD)-dmg; 
            target.SHLD=0;
        }
        target.TookDamage(target, dot, dmg);
        return dmg;
    }
    @Override
    public void TookDamage (Character hero, boolean dot, int dmg) //true for dot and false for all other types
    { 
        hero.dmgtaken+=dmg;
        int h=hero.HP; h+=dmg; //for tracking hp changes for passives
        if (hero.HP<=0)
        hero.HP=0;
        if (hero.HP<=0&&dot==true&&!(hero.binaries.contains("Immortal")))
        {
            hero.onLethalDamage(hero, null, "DOT");
        }
        else if (hero.HP<=0&&dot==false&&!(hero.binaries.contains("Immortal")))
        {
            hero.onLethalDamage(hero, null, "other");
        }
        if (hero.dead==false)
        {
            //hero.HPChange(hero, h, hero.HP); //commented out for now since no summons use this method
        }
    }
    @Override
    public void TookDamage (Character hero, Character dealer, int dmg) //for taking damage from a hero
    {
        System.out.println ("\n"+dealer.Cname+" did "+dmg+" damage to "+hero.Cname);
        hero.dmgtaken+=dmg;
        if (hero.HP<=0)
        {
            hero.HP=0;
            hero.onLethalDamage(hero, dealer, "attack");
        }
        else
        {
            //check passive
        }
    }
    @Override
    public void onLethalDamage (Character hero, Character killer, String dmgtype)
    {
        hero.onDeath(hero, killer, dmgtype);
    }
    @Override
    public void onDeath (Character hero, Character killer, String dmgtype)
    {
        if (hero.activeability!=null&&hero.activeability.channelled==true)
        {
            hero.activeability.InterruptChannelled(hero, hero.activeability);
        }
        hero.HP=0;
        hero.dead=true;
        hero.dmgtaken=0;
        hero.turn=0;
        ArrayList <StatEff> removeme= new ArrayList<StatEff>();
        removeme.addAll(hero.effects);    
        if (killer!=null)
        {
            System.out.println(killer.Cname+" killed "+hero.Cname);
        }
        else
        {
            System.out.println(hero.Cname+" has died");
        }
        for (StatEff eff: removeme)
        {
            if (!(eff instanceof Tracker))
            {
                hero.remove(hero, eff.hashcode, "silent"); 
            }
        }
        Character[] people=Battle.GetTeammates(hero);
        for (Character friend: people)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyDeath(friend, hero, killer);
            }
        }
        boolean foe=CoinFlip.TeamFlip(hero.team1); //to get their enemies
        Character[] enemies=Battle.GetTeam(foe);
        for (Character ant: enemies)
        {
            if (ant!=null&&!(ant.binaries.contains("Banished")))
            {
                ant.onEnemyDeath(ant, hero, killer);
            }
        }
        Battle.AddDead(hero);
    }
    @Override
    public void onAllyDeath (Character bystander, Character deadfriend, Character killer)
    {
        if (killer!=null)
        {
        }
    }
    @Override
    public void onEnemyDeath (Character bystander, Character deadfoe, Character killer)
    {
        if (killer!=null&&bystander.hash==killer.hash)
        {
            killer.onKill(killer, deadfoe);
        }
    }
    @Override
    public void onKill (Character killer, Character victim)
    {
    } 
    @Override 
    public void onRez (Character hero, Character healer)
    {
    }
    @Override
    public void Transform (Character hero, int newindex, boolean greater) //new index is the index number of the character being transformed into
    {
        if (!(hero.immunities.contains("Other")))
        {
            String old=hero.Cname; 
            String New=SetName(newindex, true); 
            if (greater==false)
            System.out.println(old+" Transformed into "+New+"!");
            else
            System.out.println(old+" Transformed (Greater) into "+New+"!");
            hero.Cname=New; 
            if (hero.transabs[0][0]==null) //transforming for the first time, not counting legion since he's special
            {
                hero.transabs[0]=hero.abilities;
                Ability[] newabilities=Ability.AssignAbSum(newindex);
                hero.abilities=newabilities;
            }
            else //already transformed earlier and now transforming back
            {
                Ability[] temp=hero.transabs[0];
                hero.transabs[0]=hero.abilities;
                hero.abilities=temp;
            }
            if (greater==true) //greater transformation is ocurring
            {
                hero.maxHP=InHP(newindex, true); 
                hero.HP=maxHP;
                hero.SHLD=0;
                ArrayList <StatEff> removeme= new ArrayList<StatEff>();
                removeme.addAll(hero.effects);        
                for (StatEff eff: removeme)
                {
                    if (!(eff instanceof Tracker))
                    hero.remove(hero, eff.hashcode, "normal"); 
                }
                if (hero.index==28) //if statement since there are only 2 summons with transform right now
                {
                    CoinFlip.RobotImmunities(hero, false); //if arachnaught is transforming into someone else, it loses its immunities
                }
                else if (newindex==28)
                {
                    CoinFlip.RobotImmunities(hero, true); //when transforming into an arachnaught, gain its immunities
                }
            }
            hero.index=newindex; 
            CheckSumDupes(hero);
        }
        else
        System.out.println(hero.Cname+"'s Transformation failed due to an immunity.");
    }
    @Override
    public void onAllySummon (Character summoner, Summon newfriend)
    {
    }
    @Override
    public void onEnemySummon (Character summoner, Summon newfoe)
    {
    }
    @Override
    public boolean onAllyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        return ok;
    }
    @Override
    public boolean onEnemyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        return ok;
    }
    @Override
    public boolean onSelfControlled (Character hero, Character controller)
    {
        boolean ok=true;
        Character[] people=Battle.GetTeammates(hero); 
        for (Character friend: people)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                ok=friend.onAllyControlled(friend, hero, controller);
            }
        }
        Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
        for (Character ant: enemies)
        {
            if (ant!=null&&!(ant.binaries.contains("Banished")))
            {
                ok=ant.onEnemyControlled(ant, hero, controller);
            }
        }
        return ok;
    }
}