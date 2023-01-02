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
        //for triggering passives
        switch (lad.index)
        {
            case 1: SummonPassive.NickLMD(lad); break;
        }
        int ahcounter=0; //number of duplicate summons
        if (lad.team1==true) //this is so summons don't all have the same name (e.g. three enemies named "Thug")
        {
            for (Character hero: Battle.team1)
            {
                if (hero instanceof Summon)
                {
                    String name=Character.SetName(index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            for (Character hero: Battle.team1dead)
            {
                if (hero instanceof Summon)
                {
                    String name=super.SetName(index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }            
        }
        else
        {
            for (Character hero: Battle.team2)
            {
                if (hero instanceof Summon)
                {
                    String name=Summon.SetName(index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            for (Character hero: Battle.team2dead)
            {
                if (hero instanceof Summon)
                {
                    String name=SetName(index, true);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
        }
        if (ahcounter>1)
        {
            lad.Cname=Summon.AlterSumName(lad.index, ahcounter);
        }
    }
    public static String AlterSumName (int index, int counter)
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
    @Override
    public void onTurn (Character hero, boolean notbonus)
    {
        boolean go=true;
        if (!(hero.binaries.contains("Stunned")))
        {
            if (go==true) //if they haven't already taken their turn
            {
                ++hero.turn;
            }
            boolean team=hero.team1;
            Character[] friends=Battle.GetTeammates(hero, team);
            for (Character friend: friends)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    friend.onAllyTurn(hero, friend, false);
                }
            }
            team=Card_CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
            Character[] foes=Battle.GetTeammates(hero, team);
            for (Character foe: foes)
            {
                if (foe!=null&&foe.CheckFor(foe, "Banish")==false)
                {
                    foe.onEnemyTurn(hero, foe, false);
                }
            }
            if (hero.team1==true) //potentially notify dead friends like Phoenix that it's time to revive
            {
                for (Character deadlad: Battle.team1dead)
                {
                    deadlad.onAllyTurn (hero, deadlad, false);
                }
            }
            else
            {
                for (Character deadlad: Battle.team2dead)
                {
                    deadlad.onAllyTurn (hero, deadlad, false);
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
    public void onAttack (Character hero, Character victim)
    {
        //triggered after a hero attacks
    }
    @Override
    public void onAttacked(Character attacked, Character attacker, int dmg)
    {
        if (attacked.dead==false&&!(attacked.binaries.contains("Stunned"))&&!(attacker.ignores.contains("Counter")))
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Counter"))
                {
                    eff.UseCounter(attacked, attacker); 
                    attacked.remove(attacked, eff.hashcode, false);
                    break;
                }
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
        if (hero.HP<=0&&dot==true)
        {
            hero.HP=0;
            hero.onLethalDamage(hero, true, "DOT");
        }
        else if (hero.HP<=0&&dot==false)
        {
            hero.HP=0;
            hero.onLethalDamage(hero, false, "other");
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
        if (hero.activeability[0]!=null)
        {
            hero.activeability[0].InterruptChannelled();
        }
        hero.HP=0;
        hero.dead=true;
        hero.dmgtaken=0;
        ArrayList <StatEff> removeme= new ArrayList<StatEff>();
        removeme.addAll(hero.effects);       
        for (StatEff eff: removeme)
        {
            hero.remove(hero, eff.hashcode, true); 
        }
        hero.turn=0;
        if (killer!=null)
        {
            System.out.println(killer.Cname+" has killed "+hero.Cname);
        }
        else
        {
            System.out.println(hero.Cname+" has died");
        }
        Character[] people=Battle.GetTeammates(hero, hero.team1);
        for (Character friend: people)
        {
            if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
            {
                friend.onAllyDeath(friend, hero, killer);
            }
        }
        boolean foe=Card_CoinFlip.TeamFlip(hero.team1); //to get their enemies
        Character[] enemies=Battle.GetTeammates(hero, foe);
        for (Character ant: enemies)
        {
            if (ant!=null&&ant.CheckFor(ant, "Banish")==false)
            {
                ant.onEnemyDeath(ant, hero, killer);
            }
        }
        if (hero.team1==true)
        {
            Battle.AddDead(hero);
        }
        else
        {
            Battle.AddDead(hero);
        }
        hero.Torder=616;
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
    public void onBuffed(Character hero, StatEff buff, boolean add)
    {
    }
    @Override
    public void onDebuffed (Character hero, StatEff debuff, boolean add) //true if the stat is being added and false for removal
    {
    }
    @Override
    public void onHealEffed (Character hero, StatEff heal, boolean add)
    {
    }
    @Override
    public void onDefEffed (Character hero, StatEff def, boolean add)
    {
    }
    @Override
    public void onOtherEffed (Character hero, StatEff otr, boolean add)
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