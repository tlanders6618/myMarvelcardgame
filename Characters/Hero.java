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
        switch (hero.index) //first turn is turn 0
        {
            case 5: StaticPassive.WM(hero); break;
            case 6: ActivePassive.CaptainA(hero); break;
            case 9: ActivePassive.StarLord(hero); break; 
            case 11: ActivePassive.FuryJr (hero, true, false, false, false); break;
            case 18: ActivePassive.Spidey(hero, null, false, false); break;
            case 23: ActivePassive.CM(hero, true, 0); break;
            case 24: ActivePassive.Binary(hero); break;
        }
        ++hero.turn;
        super.onTurn(hero, notbonus);
    }
    @Override
    public void onTurnEnd (Character hero, boolean notbonus)
    {
        switch (hero.index)
        {
            case 12: ActivePassive.DraxOG(hero, false, null, null); break;
        }
    }
    @Override
    public void onAllyTurn (Character ally, Character hero, boolean summoned) //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    {
        if (!(hero.binaries.contains("Banished"))) //remember that this triggers for dead heroes too
        {
            switch (index)
            {
                case 11: ActivePassive.FuryJr(hero, false, true, summoned, false); break;
                case 24: ActivePassive.Binary(hero); break;
            }
        }
    }
    @Override
    public void onEnemyTurn (Character enemy, Character hero, boolean summoned)
    {
        if (!(hero.binaries.contains("Banished")))
        {
            switch (index)
            {
                case 24: ActivePassive.Binary(hero); break;
            }
        }
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
            case 16: StaticPassive.OGVenom (hero); StaticPassive.Symbiote (hero, 0, true); break;
            case 17: StaticPassive.Symbiote (hero, 0, true); break;
            case 23: StaticPassive.CM(hero); break;
            case 25: hero.passivecount=10; hero.Cchance+=50; Tracker t=new Tracker("Control Points: "); hero.effects.add(t); t.onApply(hero); 
            StaticPassive.Symbiote (hero, 0, true); break;
            case 26: int ignore=StaticPassive.MODOC(hero, null, false, true, 0); break;
        }
    }
    @Override
    public void add (Character hero, StatEff eff) //adding a stateff
    {
        hero.effects.add(eff);        
        String type=eff.getefftype(); String name=eff.getimmunityname();
        switch (hero.index)
        {
            case 2: 
            if (name.equals("Intensify")&&type.equals("Buffs"))
            ActivePassive.Gamora(hero, eff, true); 
            break;
            case 16: case 17: case 25:
            if (name.equals("Burn"))
            StaticPassive.Symbiote(hero, 5, false);
            break;
        }
        for (StatEff e: hero.effects) //for stateffs that react to other stateffs like fortify
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
                    if (name.equals("Intensify")&&type.equals("Buffs"))
                    {
                        ActivePassive.Gamora(hero, eff, false); 
                        break;
                    }
                    case 4:
                    if (name.equals("Intensify")&&type.equals("Buffs")&&(how.equals("nullify")||how.equals("steal")))
                    {
                        ActivePassive.IM(hero, eff); 
                        break;
                    }
                    case 16: case 17: case 25:
                    if (name.equals("Burn"))
                    StaticPassive.Symbiote(hero, -5, false);
                    break;
                }     
                hero.effects.remove(eff);
                break; //end the for each loop
            }
        }
    }
    @Override
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe) //for attack skills
    {
        switch (dealer.index) //for passives that let the dealer ignore protect or miss or etc against certain targets 
        {
            case 20: ActivePassive.Superior(dealer, target, true); break;
        }
        target=target.onTargeted(dealer, target, dmg, aoe); 
        switch (dealer.index)
        {
            case 13: StaticPassive.Drax(dealer, target, false); break;
            case 14: ActivePassive.X23(dealer, target, false, true); break;
        }
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed")))) //only check if dealer isn't immune to miss and hasn't missed already; can't miss twice 
        { 
            Damage_Stuff.CheckEvade(dealer, target); //blind is checked when activating the ab; evade is checked here once the target has been selected
        }
        if ((!(dealer.binaries.contains("Missed")))&&target.dead==false) //check that they're still alive for mulitihit attacks
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
        dealer.onAttack(dealer, target); //activate relevant passives after attacking
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
        //modified version of attack method since debuff skills cannot do damage
        switch (dealer.index) //for passives that let the dealer ignore protect or miss or etc against certain targets 
        {
            case 20: ActivePassive.Superior(dealer, target, true); break;
        }
        target=target.onTargeted(dealer, target, 0, aoe);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            Damage_Stuff.CheckEvade(dealer, target);
        }
        dealer.onAttack(dealer, target);
        target.onAttacked(target, dealer, 0);
        Character[] friends=Battle.GetTeammates(target);
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyAttacked(friend, target, dealer, 0);
            }
        }
        return target;
    }
    @Override
    public Character onTargeted (Character attacker, Character target, int dmg, boolean aoe)
    {
        Character ntarg=target;
        if (aoe==false&&target.CheckFor(target, "Protect", false)==true&&!(attacker.ignores.contains("Protect"))) //check for protect
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&!(eff.getProtector().equals(target))) //protect has no effect if the target isn't the one being protected
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
        switch (target.index) //for passives like howard the duck's that apply when attacked but take place before damage is dealt; mainly for avoidance
        {
            //no need to trigger some of these passives if protect was activated since protect changes the attack's target
            case 18: ActivePassive.Spidey(target, attacker, true, aoe); break;
        }
        if (ntarg!=target&&!(ntarg.binaries.contains("Banished"))) //if the character is protected, no one else needs to do anything bc they're safe now
        {
            return ntarg;
        }
        if (ntarg==target) //only need to notify allies if the character is still vulnerable
        {
            Character[] friends=Battle.GetTeammates(target);
            for (Character friend: friends) //this is where spidey, thing, etc do their thing
            {
                if (friend!=null&&!(friend.binaries.contains("Banished")))
                {
                    ntarg=friend.onAllyTargeted(friend, attacker, target, dmg, aoe);
                    if (ntarg!=target)
                    return ntarg;
                }
            }
        } 
        return ntarg;
    }
    @Override
    public void onAttack (Character hero, Character victim) //triggered after a hero finishes attacking
    {
        switch (hero.index)
        {
            case 12: ActivePassive.DraxOG(hero, true, victim, null); break;
            case 13: StaticPassive.Drax (hero, null, false); break;
            case 14: ActivePassive.X23(hero, victim, false, false); break;
            case 20: ActivePassive.Superior(hero, victim, false); break;
            case 23: ActivePassive.CM(hero, false, 0); break; //after attacking, see if she can go binary; to avoid bugs with her transforming mid attack
        }
    }
    @Override
    public void onCrit (Character hero, Character target) //called by damagestuff crit calc
    {
        switch (hero.index)
        {
            case 14: ActivePassive.X23(hero, target, true, false); break;
        }
    }
    @Override
    public void onAttacked(Character attacked, Character attacker, int dmg)
    {
        switch (attacked.index) //for passives that trigger even if the hero died from the attack
        {
            case 25: ActivePassive.Flash(attacked, -3); break;
        }
        if (attacked.dead==false)
        {
            for (StatEff eff: attacked.effects)
            {
                eff.Attacked(attacked, attacker, dmg);
            }
            switch (attacked.index)
            {
                case 15: ActivePassive.Wolvie(attacked, true); break;
            }
        } 
    }
    @Override
    public void onAllyAttacked(Character hero, Character hurtfriend, Character attacker, int dmg) 
    {
        if (!(hero.binaries.contains("Banished")))
        {
            switch (hero.index)
            {
                case 1: ActivePassive.MoonKnight(hero, hurtfriend, attacker); break;
                case 16: ActivePassive.OGVenom (hero, hurtfriend, attacker); break;
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
    public Character onAllyTargeted (Character hero, Character dealer, Character ally, int dmg, boolean aoe)
    {
        if (!(hero.binaries.contains("Banished"))&&aoe==false)
        {
            switch (hero.index)
            {
                case 18: 
                if (!(hero.binaries.contains("Stunned"))&&ally.HP<ally.maxHP&&hero.CheckFor(hero, "Evade", false)==true)
                {
                    System.out.println("With great power, there must also come great responsibility."); 
                    System.out.println(hero.Cname+" took the attack for "+ally.Cname+"!");
                    return hero;
                }
                break;
            }
        }
        return ally;
    }
    @Override
    public int TakeDamage (Character target, Character dealer, int dmg, boolean aoe) //this checks if shield is strong enough to prevent health damage from an enemy attack
    {
        switch (target.index)
        {
            case 26: dmg=StaticPassive.MODOC(target, dealer, true, false, dmg); break;
        }
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
    public int TakeDamage (Character target, int dmg, boolean dot) //same as above but for taking sourceless damage
    {
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
            hero.onLethalDamage(hero, null, "DOT");
        }
        else if (hero.HP<=0&&dot==false&&!(hero.binaries.contains("Immortal")))
        {
            hero.onLethalDamage(hero, null, "other");
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
            switch (hero.index)
            {
                case 15: ActivePassive.Wolvie(hero, false); break;
                case 23: ActivePassive.CM(hero, false, dmg); break;
            }
        }
    }
    @Override
    public void onLethalDamage (Character hero, Character killer, String dmgtype)
    {
        boolean die=true;
        switch (hero.index)
        {
            case 12: die=ActivePassive.DraxOG(hero, false, hero, dmgtype); break;
        }
        if (die==true)
        {
            hero.onDeath(hero, killer, dmgtype);
        }
    }
    @Override
    public void onDeath (Character hero, Character killer, String dmgtype)
    {
        switch (hero.index)
        {
            case 5: case 16:    
            if (hero.passivefriend[0]!=null&&hero.passivefriend[0].dead==false)
            {
                hero.passivefriend[0].remove(passivefriend[0], passivecount, "normal"); //remove heat signature detection/lethal protector's resistance
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
        Character[] people=Battle.GetTeammates(hero);
        for (Character friend: people)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyDeath(friend, hero, killer);
            }
        }
        Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
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
        switch (killer.index)
        {
            case 17: ActivePassive.Venom(killer); break;
        }
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
            String New=SetName(newindex, false); 
            if (greater==false)
            System.out.println(old+" Transformed into "+New+"!");
            else
            System.out.println(old+" Transformed (Greater) into "+New+"!");
            hero.Cname=New; 
            if ((hero.index<113||hero.index>115)&&hero.transabs[0][0]==null) //transforming for the first time, not counting legion since he's special
            {
                hero.transabs[0]=hero.abilities;
                Ability[] newabilities=Ability.AssignAb(newindex);
                hero.abilities=newabilities;
            }
            else if (hero.index<113||hero.index>115&&hero.transabs[0][0]!=null) //already transformed earlier and now transforming back
            {
                Ability[] temp=hero.transabs[0];
                hero.transabs[0]=hero.abilities;
                hero.abilities=temp;
            }
            else //hero is legion
            {
                if (hero.index==113)
                {
                    hero.transabs[0]=hero.abilities;
                }
                else if (hero.index==114)
                {
                    hero.transabs[1]=hero.abilities;
                }
                else if (hero.index==115)
                {
                    hero.transabs[2]=hero.abilities;
                }
                if (newindex==113)
                {
                    hero.abilities=hero.transabs[0];
                }
                else if (newindex==114)
                {
                    hero.abilities=hero.transabs[1];
                }
                else if (newindex==115)
                {
                    hero.abilities=hero.transabs[2];
                }
            }
            if (greater==true) //greater transformation is ocurring
            {
                hero.maxHP=InHP(newindex, false); 
                hero.HP=maxHP;
                hero.SHLD=0;
                ArrayList <StatEff> removeme= new ArrayList<StatEff>();
                removeme.addAll(hero.effects);        
                for (StatEff eff: removeme)
                {
                    if (!(eff instanceof Tracker))
                    hero.remove(hero, eff.hashcode, "normal"); 
                }
            }
            switch (hero.index) //for getting rid of immunities when undoing transformation
            {
                case 24: StaticPassive.Binary(hero, false); break;
            }
            hero.index=newindex; 
            switch (hero.index) //for gaining immunities when transforming
            {
                case 24: StaticPassive.Binary(hero, true); break;
            }
        }
        else
        System.out.println(hero.Cname+"'s Transformation failed due to an immunity.");
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
        Character[] people=Battle.GetTeammates(hero);
        for (Character friend: people)
        {
            if (friend!=null)
            {
                ok=friend.onAllyControlled(friend, hero, controller);
            }
        }
        Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
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