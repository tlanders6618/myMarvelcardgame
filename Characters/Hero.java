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
            case 5: StaticPassive.WM(hero); break;
            case 6: ActivePassive.CaptainA(hero); break;
            case 9: go=ActivePassive.StarLord(hero); break; 
            case 11: ActivePassive.FuryJr (hero, true, false, false, false); break;
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
                   friend.onAllyTurn(hero, friend, false);
                }
            }
            team=CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
            Character[] foes=Battle.GetTeammates(hero, team);
            for (Character foe: foes)
            {
                if (foe!=null&&!(foe.binaries.contains("Banished")))
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
            case 12: ActivePassive.DraxOG(hero, false, null, null); break;
        }
    }
    @Override
    public void onAllyTurn (Character ally, Character hero, boolean summoned) //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    {
        if (!(hero.binaries.contains("Banished")))
        {
            switch (index)
            {
                case 11: ActivePassive.FuryJr(hero, false, true, summoned, false); break;
            }
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
            case 15: StaticPassive.WolvieTracker(hero); break;
        }
    }
    @Override
    public void add (Character hero, StatEff eff) //adding a stateff
    {
        hero.effects.add(eff);        
        String type=eff.getefftype();
        switch (hero.index)
        {
            case 2: 
            if (type.equals("Buffs"))
            ActivePassive.Gamora(hero, eff, true); 
            break;
        }
        for (StatEff e: hero.effects) //for stateffs that react to other stateffs like fortify
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
    public void remove (Character hero, int removalcode, String how) //removes status effects; how is how it was removed, meaning purify or nullify or steal or normal (i.e. expiry)
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.hashcode==removalcode)
            {
                String name=eff.getimmunityname(); String type=eff.getefftype();
                eff.Nullified(hero);
                //redundant to print the message if something like nullify already does, so it's only printed if the stateff wasn't nullified or stolen or purified
                if (how.equals("normal"))           
                {
                    System.out.println (hero.Cname+"'s "+eff.getimmunityname()+" expired."); 
                }                
                switch (hero.index)
                {
                    case 2: 
                    if (name.equals("Intensify"))
                    {
                        ActivePassive.Gamora(hero, eff, false); 
                        break;
                    }
                    case 4:
                    if (name.equals("Intensify")&&(how.equals("nullify")||how.equals("steal")))
                    {
                        ActivePassive.IM(hero, eff); 
                        break;
                    }
                }     
                hero.effects.remove(eff);
                break; //end the for each loop
            }
        }
    }
    @Override
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe)
    {
        //for normal damage from attack skills
        target=target.onTargeted(dealer, target, dmg, aoe);
        switch (dealer.index)
        {
            case 14: ActivePassive.X23(dealer, target, true); break;
        }
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed")))) //only check if dealer isn't immune to miss and hasn't missed already; can't miss twice 
        { 
            Damage_Stuff.CheckEvade(dealer, target); //blind is checked when activating the ab; evade is checked here once the target has been selected
        }
        if ((!(dealer.binaries.contains("Missed")))&&target.dead==false) //check that they're alive for mulitihit attacks
        {
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
            dmg=Damage_Stuff.CheckGuard(dealer, target, dmg);      
            for (SpecialAbility h: target.helpers) //for redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
            dmg=target.TakeDamage(target, dealer, dmg, aoe);
        }
        else
        dmg=0;
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
            dealer.onAttack(dealer, target); //activate relevant passives after attacking
        }
        return target;
    }
    @Override
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe)
    {
        //modified version of attack method since debuff skills cannot do damage
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
        switch (hero.index)
        {
            case 12: ActivePassive.DraxOG(hero, true, victim, null); break;
            case 14: ActivePassive.X23(hero, victim, true); break;
        }
    }
    @Override
    public void OnCrit (Character target)
    {
        switch (this.index)
        {
            case 14: ActivePassive.X23(this, target, false); break;
        }
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
            switch (attacked.index)
            {
                case 15: ActivePassive.Wolvie(attacked, true); break;
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
    public void onAllyAttacked(Character hero, Character hurtfriend, Character attacker, int dmg) 
    {
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
            switch (hero.index)
            {
                case 1: ActivePassive.MoonKnight(hero, hurtfriend, attacker); break;
            }
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
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
            //do spidey, thing, jean switch here   
        }
        return ntarget;
    }
    @Override
    public int TakeDamage (Character target, Character dealer, int dmg, boolean aoe) //true for aoe, false for single target
    {
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
        if (dmg>0&&SHLD>=dmg) 
        {
            target.SHLD-=dmg; 
        }
        else if (dmg>0&&SHLD<dmg)
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
            hero.HPChange(hero, h, hero.HP);
            switch (hero.index)
            {
                case 15: ActivePassive.Wolvie(hero, false); break;
            }
        }
    }
    @Override
    public void TookDamage (Character hero, Character dealer, int dmg) //for taking damage from a hero
    {
        System.out.println ("\n"+dealer.Cname+" did "+dmg+" damage to "+hero.Cname);
        hero.dmgtaken+=dmg; 
        int h=hero.HP; h+=dmg;
        if (hero.HP<=0)
        hero.HP=0;
        if (hero.HP<=0&&!(hero.binaries.contains("Immortal")))
        {
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
            switch (hero.index)
            {
                case 15: ActivePassive.Wolvie(hero, false); break;
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
            case 12: die=ActivePassive.DraxOG(hero, false, hero, dmgtype); break;
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
                hero.passivefriend[0].remove(passivefriend[0], passivecount, "normal"); //remove war machine's heat signature detection
            }
            break;            
        }
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
    public void onAllyDeath (Character hero, Character deadfriend, Character killer)
    {
        if (killer!=null&&!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onEnemyDeath (Character hero, Character deadfoe, Character killer)
    {
        if (killer!=null&&hero.hash==killer.hash)
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
                hero.remove(hero, eff.hashcode, "normal"); 
            }
        }
    }
    @Override
    public void onAllySummon (Character hero, Summon newfriend)
    {
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onEnemySummon (Character hero, Summon newfoe)
    {
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public boolean onAllyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
        }
        return ok;
    }
    @Override
    public boolean onEnemyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        if (!(hero.binaries.contains("Stunned"))&&!(hero.binaries.contains("Banished")))
        {
        }
        return ok;
    }
    @Override
    public boolean onSelfControlled (Character hero, Character controller)
    {
        boolean ok=true;
        Character[] people=Battle.GetTeammates(hero, hero.team1);
        for (Character friend: people)
        {
            if (friend!=null)
            {
                ok=friend.onAllyControlled(friend, hero, controller);
            }
        }
        Character[] enemies=Battle.GetTeammates(hero, hero.team1);
        for (Character ant: enemies)
        {
            if (ant!=null)
            {
                ok=ant.onEnemyControlled(ant, hero, controller);
            }
        }
        return ok;
    }
}