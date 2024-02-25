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
        //Need to manually assign team affiliation when creating a summon
        index=Sindex;
        summoned=true;
        hash=Card_HashCode.RandomCode();
        Cname=SetName(index, true);
        size=SetSizeSum(index);
        if (Sindex==13)
        {
            //clone health and abilities depend on who they're a clone of
        }
        else
        {
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
        eff.onApply(hero);
        if (!(eff.getefftype().equals("Secret"))&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))) 
        //due to taunt/protect interaction; no point in announcing it being added if it's instantly removed
        {
            System.out.println ("\n"+hero.Cname+" gained a(n) "+eff.geteffname());
        }
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
                    Character[] team=Battle.GetTeammates(hero, true); boolean solo=true;
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
                    Character[] team=Battle.GetTeammates(hero, false); boolean solo=true;
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
        if (hero.dead==false)
        {
            boolean team=hero.team1;
            Character[] friends=Battle.GetTeammates(hero, team);
            for (Character friend: friends)
            {
                if (friend!=null&&!(friend.binaries.contains("Banished")))
                {
                   friend.onAllyTurn(hero, friend, true);
                }
            }
            team=CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
            Character[] foes=Battle.GetTeammates(hero, team);
            for (Character foe: foes)
            {
                if (foe!=null&&!(foe.binaries.contains("Banished")))
                {
                    foe.onEnemyTurn(hero, foe, true);
                }
            }
            if (hero.team1==true) //potentially notify dead friends like Phoenix that it's time to revive
            {
                for (Character deadlad: Battle.team1dead)
                {
                    deadlad.onAllyTurn (hero, deadlad, true);
                }
            }
            else
            {
                for (Character deadlad: Battle.team2dead)
                {
                    deadlad.onAllyTurn (hero, deadlad, true);
                }
            }
        }
    }
    @Override
    public void OnTurnEnd (Character hero, boolean notbonus)
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
        if ((dealer.binaries.contains("Missed")))
        {
            dmg=0;
        }
        if ((!(dealer.binaries.contains("Missed"))))
        {
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
            dmg=Damage_Stuff.CheckGuard(dealer, target, dmg);      
            for (SpecialAbility h: target.helpers) //for special things like redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
            dmg=target.TakeDamage(target, dealer, dmg, aoe);
        }
        dealer.activeability.ReturnDamage(dmg); //tells the ability how much dmg the attack did
        if (target.dead==false)
        {
            target.onAttacked(target, dealer, dmg);
        }
        else
        {
            Character[] friends=Battle.GetTeammates(target, target.team1);
            for (Character friend: friends)
            {
                if (friend!=null&&!(friend.binaries.contains("Banished")))
                {
                    friend.onAllyAttacked(friend, target, dealer, dmg);
                }
            }
        }
        if (dealer.dead==false)
        {
            dealer.CheckDrain(dealer, target, dmg);
        }
        dealer.onAttack(dealer, target); //activate relevant passives
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
    public void OnCrit (Character target)
    {
    }
    @Override
    public void onAttacked(Character attacked, Character attacker, int dmg)
    {
        if (attacked.dead==false)
        {
            if (!(attacked.binaries.contains("Stunned"))&&!(attacker.ignores.contains("Counter")))
            {
                for (StatEff eff: attacked.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Counter"))
                    {
                        eff.UseCounter(attacked, attacker); 
                        attacked.remove(attacked, eff.hashcode, "normal");
                        break;
                    }
                }
            }
            for (StatEff eff: attacked.effects)
            {
                eff.Attacked(attacked);
            }
        } 
        Character[] friends=Battle.GetTeammates(attacked, attacked.team1);
        for (Character friend: friends)
        {
            if (friend!=null&&(friend.CheckFor(friend, "Banish")==false))
            {
                friend.onAllyAttacked(friend, attacked, attacker, dmg);
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
    public Character onTargeted (Character attacker, Character target, int dmg, boolean aoe)
    {
        Character ntarg=target;
        if (aoe==false&&target.CheckFor(target, "Protect")==true&&!(attacker.ignores.contains("Protect")))
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&!(eff.getProtector().equals(target)))
                {
                    if (eff.getefftype().equalsIgnoreCase("Defence")&&!(attacker.ignores.contains("Defence"))) 
                    //if the target has protect and the attacker doesn't ignore it, their protector instead takes the hit
                    {
                        ntarg=eff.getProtector();
                        System.out.println(ntarg.Cname+" protected "+target.Cname+"!");
                        break;
                    } 
                    else if (eff.getefftype().equalsIgnoreCase("Other")) 
                    //if the target has a protect Effect, their protector always takes the hit
                    {
                        ntarg=eff.getProtector();
                        System.out.println(ntarg.Cname+" protected "+target.Cname+"!");
                        break;
                    }            
                }
            }
        }
        //switch goes down here, after target sorting
        if (target.team1==true)
        {
            for (Character friend: Battle.team1)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, attacker, target, dmg, aoe);
                }
            }
        }
        else if (target.team1==false)
        {
            for (Character friend: Battle.team2)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, attacker, target, dmg, aoe);
                }
            }
        }
        if (ntarg!=null&&ntarg.CheckFor(ntarg, "Banish")==false)
        {
            return ntarg;
        }
        else
        {
            return target;
        }
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
            hero.onLethalDamage(hero, true, "DOT");
        }
        else if (hero.HP<=0&&dot==false&&!(hero.binaries.contains("Immortal")))
        {
            hero.onLethalDamage(hero, false, "other");
        }
        if (hero.dead==false)
        {
            //hero.HPChange(hero, h, hero.HP); //commented out for now since no summons use this method
        }
    }
    @Override
    public void TookDamage (Character hero, Character dealer, int dmg)
    {
        //for taking damage from a hero
        System.out.println ("\n"+dealer.Cname+" did "+dmg+" damage to "+hero.Cname);
        hero.dmgtaken+=dmg;
        if (hero.HP<=0)
        {
            hero.HP=0;
            hero.onLethalDamage(hero, dealer, "attack");
        }
        else
        {
            for (StatEff eff: hero.effects)
            {
               if (eff.getimmunityname().equalsIgnoreCase("Reflect"))
               {
                   eff.UseReflect(dealer, dmg);
                   break;
               }
            }
        }
    }
    @Override
    public void onLethalDamage (Character hero, Character killer, String dmgtype)
    {
        hero.onDeath(hero, killer, dmgtype);
    }
    @Override
    public void onLethalDamage (Character hero, boolean killer, String dmgtype)
    {
        hero.onDeath(hero, null, dmgtype);
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
        Character[] people=Battle.GetTeammates(hero, hero.team1);
        for (Character friend: people)
        {
            if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
            {
                friend.onAllyDeath(friend, hero, killer);
            }
        }
        boolean foe=CoinFlip.TeamFlip(hero.team1); //to get their enemies
        Character[] enemies=Battle.GetTeammates(hero, foe);
        for (Character ant: enemies)
        {
            if (ant!=null&&ant.CheckFor(ant, "Banish")==false)
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
    public void Transform (Character hero, int newindex, boolean greater) //new index is the index number of the character being transformed into
    {
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
        Character[] people=Battle.GetTeammates(hero, hero.team1);
        for (Character friend: people)
        {
            if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
            {
                ok=friend.onAllyControlled(friend, hero, controller);
            }
        }
        Character[] enemies=Battle.GetTeammates(hero, hero.team1);
        for (Character ant: enemies)
        {
            if (ant!=null&&ant.CheckFor(ant, "Banish")==false)
            {
                ok=ant.onEnemyControlled(ant, hero, controller);
            }
        }
        return ok;
    }
}