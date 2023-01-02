package myMarvelcardgamepack;

        
/**
* Designer: Timothy Landers
* Date: 1/12/22
* Filename: Hero
* Purpose: Creates heroes.
*/
import java.util.ArrayList; 
import java.util.Scanner;
public class Hero extends Character
{
    public Hero (int Pindex)
    {
        index=Pindex; //character's semi-unique identifier number                
        //set identifiers
        HP=InHP (index, false);
        maxHP=HP;
        Cname=SetName(index, false);
        hash=Card_HashCode.RandomCode();                
        //set abilities
        abilities=Ability.AssignAb(index);     
    }
    @Override
    public void onTurn (Character hero, boolean notbonus)
    {
        boolean go=true;
        switch (hero.index)
        {
            case 5: StaticPassive.WM(hero, hero.turn); break;
            case 6: ActivePassive.CaptainA(hero); break;
            case 9: ActivePassive.StarLord(hero); go=false; break; 
            case 11: 
            if (hero.passivecount==1)
            {
                System.out.println("Deactivate Kill Mode?");
                System.out.println("0. Yes"); System.out.println ("1. No");
                int choice=16;
                do
                {
                    choice=Damage_Stuff.GetInput();
                }
                while (choice!=1&&choice!=0);
                if (choice==0)
                {
                    ActivePassive.FuryJr(hero, true, false); 
                }
            }
            break;
        }
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
        switch (hero.index)
        {
            case 11: ActivePassive.FuryJr(hero, false, false); break;
            case 12: ActivePassive.DraxOG(hero, false, null); break;
        }
    }
    @Override
    public void onAllyTurn (Character ally, Character hero, boolean summoned) //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    {
        switch (index)
        {
            case 11: ActivePassive.FuryJr(hero, false, summoned); break;
        }
    }
    @Override
    public void onEnemyTurn (Character enemy, Character hero, boolean summoned)
    {
    }
    @Override
    public void onFightStart(Character hero)
    {
        switch (hero.index)
        {
            case 6: ActivePassive.CaptainA(hero); break;
            case 7: StaticPassive.Falcon(hero); break;
            case 8: StaticPassive.Bucky(hero); break;
            case 12: StaticPassive.DraxOG(hero); break;
        }
    }
    @Override
    public void onAttack (Character hero, Character victim)
    {
        //triggered after a hero attacks
        switch (hero.index)
        {
            case 12: ActivePassive.DraxOG(hero, true, victim); break;
        }
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
        switch (ally.index)
        {
            case 1: ActivePassive.MoonKnight(ally, hurtfriend, attacker); break;
        }
    }
    @Override
    public void HPChange (Character hero, int oldhp, int newhp)
    {
        switch (hero.index)
        {
            case 10: StaticPassive.FurySr(hero, newhp); break;
        }
    }
    @Override
    public Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe)
    {
        Character ntarget=target;
        //do spidey, thing, jean switch here        
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
        int h=hero.HP; h+=dmg;
        if (hero.HP<=0&&dot==true&&!(hero.binaries.contains("Immortal")))
        {
            hero.HP=0;
            hero.onLethalDamage(hero, true, "DOT");
        }
        else if (hero.HP<=0&&dot==false&&!(hero.binaries.contains("Immortal")))
        {
            hero.HP=0;
            hero.onLethalDamage(hero, false, "other");
        }
        if (hero.dead==false)
        {
            hero.HPChange(hero, h, hero.HP);
        }
    }
    @Override
    public void TookDamage (Character hero, Character dealer, int dmg)
    {
        //for taking damage from a hero
        System.out.println ("\n"+dealer.Cname+" did "+dmg+" damage to "+hero.Cname);
        hero.dmgtaken+=dmg; 
        int h=hero.HP; h+=dmg;
        if (hero.HP<=0&&!(hero.binaries.contains("Immortal")))
        {
            hero.HP=0;
            hero.onLethalDamage(hero, dealer, "attack");
        }
        else
        {
            hero.HPChange(hero, h, hero.HP);
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
        boolean die=true;
        switch (hero.index)
        {
            case 12: 
            if (dmgtype.equalsIgnoreCase("dot")) 
            { 
                if (!(hero.binaries.contains("Death")))
                {
                    System.out.println("\nDrax's rage is undying!");
                }
                die=false; hero.binaries.add("Death"); 
            } 
            break;
        }
        if (die==true)
        {
            hero.onDeath(hero, null, dmgtype);
        }
    }
    @Override
    public void onDeath (Character hero, Character killer, String dmgtype)
    {
        switch (hero.index)
        {
            case 5:     
            if (hero.passivefriend[0]!=null)
            {
                hero.passivefriend[0].remove(passivefriend[0], passivecount, true); //remove war machine's heat signature detection
            }
            break;            
        }
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
        //switch
    } 
    @Override
    public void Transform (Character hero, int newindex, boolean greater) //new index is the index number of the character being transformed into
    {
        Ability[] newabilities = new Ability[5];
        newabilities=Ability.AssignAb(newindex);
        hero.abilities=newabilities;
        hero.Cname=SetName(newindex, false); 
        hero.index=newindex; 
        hero.dmgtaken=0;
        if (greater==true) //greater transformation is ocurring
        {
            hero.maxHP=InHP(newindex, false); 
            hero.HP=maxHP;
            ArrayList <StatEff> removeme= new ArrayList<StatEff>();
            removeme.addAll(hero.effects);        
            for (StatEff eff: removeme)
            {
                hero.remove(hero, eff.hashcode, false); 
            }
        }
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
        switch (hero.index)
        {
            case 2: ActivePassive.Gamora(hero, buff, add); break;
        } 
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