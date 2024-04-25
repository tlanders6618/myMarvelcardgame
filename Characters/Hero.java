
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
        this.pdesc=Character.MakeDesc(Pindex, false);
        //set identifiers
        HP=InHP (index, false);
        maxHP=HP;
        Cname=SetName(index, false);
        hash=Card_HashCode.RandomCode();                
        //set abilities
        abilities=Ability.AssignAb(index);     
    }
    @Override
    public void onTurn (boolean notbonus) 
    {
        switch (this.index) //first turn is turn 0
        {
            case 5: StaticPassive.WM(this); break;
            case 6: ActivePassive.CaptainA(this); break;
            case 9: ActivePassive.StarLord(this); break; 
            case 11: ActivePassive.FuryJr (this, "onturn", false); break;
            case 18: ActivePassive.Spidey(this, null, false, false); break;
            case 23: ActivePassive.CM(this, true, 0); break;
            case 24: ActivePassive.Binary(this); break;
            case 28: StaticPassive.DOOM(this, "turn", this); break;
            case 32: StaticPassive.BB(this, false); break;
            case 33: StaticPassive.Deadpool(this, "turn", null); break;
            case 35: ActivePassive.Cain(this, "turn", 616); break;
            case 40: ActivePassive.Sandy(this, "turn"); break;
        }
        super.onTurn(notbonus);
    }
    @Override
    public void onTurnEnd (boolean notbonus)
    {
        switch (this.index)
        {
            case 12: ActivePassive.DraxOG(this, false, null, null); break;
        }
    }
    @Override
    public void onAllyTurn (Character ally, boolean summoned) //ally is the one triggering call and this is one reacting
    {
        if (!(this.binaries.contains("Banished"))) //remember that this triggers for dead heroes too
        {
            switch (index)
            {
                case 11: ActivePassive.FuryJr(this, "allyturn", summoned); break;
                case 24: ActivePassive.Binary(this); break;
                case 40: ActivePassive.Sandy(this, "turn"); break;
            }
        }
    }
    @Override
    public void onEnemyTurn (Character enemy, boolean summoned) //enemy is the one triggering call and this is one reacting
    {
        if (!(this.binaries.contains("Banished")))
        {
            switch (index)
            {
                case 24: ActivePassive.Binary(this); break;
            }
        }
    }
    @Override
    public void onFightStart()
    {
        switch (this.index)
        {
            case 6: ActivePassive.CaptainA(this); break;
            case 7: StaticPassive.Falcon(this); break;
            case 8: StaticPassive.Bucky(this); break;
            case 12: StaticPassive.DraxOG(this); break;
            case 15: StaticPassive.WolvieTracker(this); break;
            case 16: StaticPassive.OGVenom (this); StaticPassive.Symbiote (this, 0, true); break;
            case 17: StaticPassive.Symbiote (this, 0, true); break;
            case 23: StaticPassive.CM(this, true); break;
            case 25: this.passivecount=10; this.Cchance+=50; Tracker t=new Tracker("Control Points: "); this.effects.add(t); t.onApply(this); 
            StaticPassive.Symbiote (this, 0, true); break;
            case 26: int ignore=StaticPassive.MODOC(this, null, "start", 0); break;
            case 27: StaticPassive.Ultron(this); break;
            case 28: StaticPassive.DOOM(this, "start", this); break;
            case 30: StaticPassive.Brawn(this); break;
            case 31: StaticPassive.Hulk(this, true); break;
            case 32: StaticPassive.BB(this, true); break;
            case 35: ActivePassive.Cain(this, "start", 616); break;
            case 40: StaticPassive.Sandy(this); break;
            case 41: StaticPassive.Rhino(this); break;
            case 81: StaticPassive.DD(this, null, true); break;
            case 83: int care=StaticPassive.LukeCage(this, 0, true); break;
            case 85: StaticPassive.Surfer(this); break;
        }
    }
    @Override
    public void add (StatEff eff) //adding a stateff
    {
        for (StatEff e: this.effects) //for stateffs that react to other stateffs, e.g. fortify
        {
            e.Attacked(eff);
        }
        if (!(eff.getefftype().equals("Secret"))&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))) 
        //due to taunt/protect interaction; no point in announcing it being added if it's instantly removed
        {
            System.out.println ("\n"+this.Cname+" gained a(n) "+eff.geteffname());
        }
        switch (this.index) //modify effect before gaining it
        {
            case 39: 
            if (eff.getimmunityname().equals("Shock"))
            eff=StaticPassive.InstaConversion(this, eff, "Intensify Effect", eff.power, eff.duration); 
            break;
        }
        this.effects.add(eff);  
        eff.onApply(this);
        switch (this.index) //after gaining effect
        {
            case 2: 
            if (eff.getimmunityname().equals("Intensify")&&eff.getefftype().equals("Buffs"))
            ActivePassive.Gamora(this, eff, true); 
            break;
            case 16: case 17: case 25:
            if (eff.getimmunityname().equals("Burn"))
            StaticPassive.Symbiote(this, 5, false);
            break;
            case 40:
            if (eff.getimmunityname().equals("Burn"))
            ActivePassive.Sandy(this, "burn");
            break;
        }
    }
    @Override
    public void remove (int removalcode, String how) //removes status effects; how is how it was removed, meaning purify or nullify or steal or normal (i.e. expiry)
    {
        for (StatEff eff: this.effects)
        {
            if (eff.hashcode==removalcode)
            {
                String name=eff.getimmunityname(); String type=eff.getefftype();
                eff.Nullified(this);
                //redundant to print the message if something like nullify already does, so it's only printed if the stateff wasn't nullified or stolen or purified
                if (how.equals("normal"))           
                {
                    System.out.println (this.Cname+"'s "+eff.getimmunityname()+" expired."); 
                }               
                this.effects.remove(eff);
                switch (this.index)
                {
                    case 2: 
                    if (name.equals("Intensify")&&type.equals("Buffs"))
                    {
                        ActivePassive.Gamora(this, eff, false); 
                        break;
                    }
                    case 4:
                    if (name.equals("Intensify")&&type.equals("Buffs")&&(how.equals("nullify")||how.equals("steal")))
                    {
                        ActivePassive.IM(this, eff); 
                        break;
                    }
                    case 16: case 17: case 25:
                    if (name.equals("Burn"))
                    StaticPassive.Symbiote(this, -5, false);
                    break;
                }    
                break; //end the for each loop
            }
        }
    }
    @Override
    public void BeforeAttack (Character dealer, Character victim, boolean target)
    {
        if (target==true)
        {
            switch (dealer.index) //for passives that let the dealer ignore protect or miss or etc against certain targets; called before checking for protect 
            {
                case 20: ActivePassive.Superior(dealer, victim, true); break;
            }
        }
        else //once target is determined after checking for protect and passives like spidey's, check if passive activates against the target
        {
            switch (dealer.index)
            {
                case 13: StaticPassive.Drax(dealer, victim, false); break;
                case 14: ActivePassive.X23(dealer, victim, false, true); break;
                case 25: StaticPassive.Flash(dealer); break;
                case 26: int ignore=StaticPassive.MODOC(dealer, victim, "attack", 616); break;
                case 33: StaticPassive.Deadpool(dealer, "attack", victim); break;
                case 36: StaticPassive.Vulture(dealer, victim); break;
                case 86: StaticPassive.Kraven(this, victim, true); break;
            }
        }
    }
    @Override
    public Character onTargeted (Character attacker, Character target, int dmg, boolean aoe)
    {
        Character ntarg=target;
        if (aoe==false&&target.CheckFor("Protect", false)==true&&!(attacker.ignores.contains("Protect"))) //check for protect
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
    public void onAttack (Character victim) //triggered after a hero finishes attacking
    {
        switch (this.index)
        {
            case 12: ActivePassive.DraxOG(this, true, victim, null); break;
            case 13: StaticPassive.Drax (this, null, false); break;
            case 14: ActivePassive.X23(this, victim, false, false); break;
            case 20: ActivePassive.Superior(this, victim, false); break;
            case 23: StaticPassive.CM(this, false); break;
            case 33: StaticPassive.Deadpool(this, "attack", victim); break;
            case 35: ActivePassive.Cain(this, "attack", 616); break;
            case 86: StaticPassive.Kraven(this, victim, false); break;
        }
    }
    @Override
    public void onCrit (Character target) //called by damagestuff crit calc
    {
        switch (this.index)
        {
            case 14: ActivePassive.X23(this, target, true, false); break;
        }
    }
    @Override
    public void onAttacked(Character attacker, int dmg)
    {
        switch (this.index) //for passives that trigger even if the hero died from the attack
        {
            case 25: ActivePassive.Flash(this, -3); break;
            case 28: StaticPassive.DOOM(this, "attacked", attacker); break;
        }
        if (this.dead==false)
        {
            ArrayList<StatEff> concurrentmodificationexception3electricboogalooboogaloo= new ArrayList<StatEff>(); //counter is removed after use
            concurrentmodificationexception3electricboogalooboogaloo.addAll(this.effects);
            boolean counter=false; //only activate counter once per attack
            for (StatEff eff: concurrentmodificationexception3electricboogalooboogaloo)
            {
                if (eff.getimmunityname().equals("Counter")&&counter==false)
                {
                    eff.Attacked(this, attacker, dmg); counter=true;
                }               
                else if (!(eff.getimmunityname().equals("Counter")))
                {
                    eff.Attacked(this, attacker, dmg);
                }
            }
            switch (this.index)
            {
                case 15: ActivePassive.Wolvie(this, true); break;
                case 81: StaticPassive.DD(this, attacker, false); break;
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
    public Character onAllyTargeted (Character hero, Character dealer, Character ally, int dmg, boolean aoe)
    {
        if (!(hero.binaries.contains("Banished"))&&aoe==false)
        {
            switch (hero.index)
            {
                case 18: 
                if (!(hero.binaries.contains("Stunned"))&&ally.HP<hero.HP&&hero.CheckFor("Evade", false)==true)
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
            case 26: dmg=StaticPassive.MODOC(target, dealer, "attacked", dmg); break;
            case 83: dmg=StaticPassive.LukeCage(target, dmg, false); break;
        }
        int odmg=dmg;
        if (dealer.ignores.contains("Shield")||dealer.ignores.contains("Defence"))
        {
        } 
        else if (SHLD>=dmg) 
        {
            target.SHLD-=dmg; 
            dmg=0;
        }
        else if (SHLD<dmg) //shield broken; can't absorb all the damage
        {
            int s=target.SHLD;
            dmg-=s;
            target.SHLD=0;
        }
        Damage_Stuff.CheckBarrier(target, dealer, dmg);
        target.TookDamage(target, dealer, odmg);
        return odmg;
    }
    @Override
    public int TakeDamage (Character target, int dmg, boolean dot) //same as above but for taking sourceless damage
    {
        int odmg=dmg;
        if (SHLD>=dmg) 
        {
            target.SHLD-=dmg; 
            dmg=0;
        }
        else if (SHLD<dmg) 
        {
            int s=target.SHLD;
            dmg-=s;
            target.SHLD=0;
        }
        Damage_Stuff.CheckBarrier(target, null, dmg);
        target.TookDamage(target, dot, dmg);
        return odmg;
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
            hero.onLethalDamage(null, "DOT");
        }
        else if (hero.HP<=0&&dot==false&&!(hero.binaries.contains("Immortal")))
        {
            hero.onLethalDamage(null, "other");
        }
        if (hero.dead==false)
        {
            hero.HPChange(h, hero.HP);
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
            hero.onLethalDamage(dealer, "attack");
        }
        else
        {
            hero.HPChange(h, hero.HP);
            switch (hero.index)
            {
                case 15: ActivePassive.Wolvie(hero, false); break;
                case 23: ActivePassive.CM(hero, false, dmg); break;
            }
        }
    }
    @Override
    public void HPChange (int oldhp, int newhp)
    {
        switch (this.index)
        {
            case 10: StaticPassive.FurySr(this, newhp); break;
            case 31: StaticPassive.Hulk(this, false); break;
            case 35: ActivePassive.Cain(this, "change", oldhp); break;
        }
    }
    @Override
    public void onLethalDamage (Character killer, String dmgtype)
    {
        boolean die=true;
        switch (this.index)
        {
            case 12: die=ActivePassive.DraxOG(this, false, this, dmgtype); break;
        }
        if (die==true)
        {
            this.onDeath(killer, dmgtype);
        }
    }
    @Override
    public void onDeath (Character killer, String dmgtype)
    {
        if (killer!=null)
        {
            System.out.println(killer.Cname+" killed "+this.Cname);
        }
        else
        {
            System.out.println(this.Cname+" has died");
        }
        switch (this.index)
        {
            case 5: case 16:
            if (this.passivefriend[0]!=null&&this.passivefriend[0].dead==false)
            {
                this.passivefriend[0].remove(passivecount, "normal"); //remove heat signature detection's target/lethal protector's resistance
                if (this.index==5)
                this.passivefriend[0].immunities.remove("Invisible"); //undo heat signature's effects
            }
            break;     
        }
        if (this.activeability!=null&&this.activeability.channelled==true)
        {
            this.activeability.InterruptChannelled(this, this.activeability);
        }
        this.HP=0;
        this.dead=true;
        this.dmgtaken=0;
        this.turn=0;
        ArrayList <StatEff> removeme= new ArrayList<StatEff>();
        removeme.addAll(this.effects);  
        for (StatEff eff: removeme)
        {
            if (!(eff instanceof Tracker))
            {
                this.remove(eff.hashcode, "silent"); 
            }
        }
        Character[] people=Battle.GetTeammates(this);
        for (Character friend: people)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyDeath(friend, this, killer);
            }
        }
        Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(this.team1));
        for (Character ant: enemies)
        {
            if (ant!=null&&!(ant.binaries.contains("Banished")))
            {
                ant.onEnemyDeath(ant, this, killer);
            }
        }
        Battle.AddDead(this);
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
            killer.onKill(deadfoe);
        }
    }
    @Override
    public void onKill (Character victim)
    {
        switch (this.index)
        {
            case 17: ActivePassive.Venom(this); break;
            case 33: StaticPassive.Deadpool(this, "kill", victim); break;
        }
    } 
    @Override 
    public void onRez (Character healer)
    {
    }
    @Override
    public void Transform (int newindex, boolean greater) //new index is the index number of the character being transformed into
    {
        if (!(this.immunities.contains("Other")))
        {
            String old=this.Cname; 
            String New=SetName(newindex, false); 
            if (greater==false)
            System.out.println(old+" Transformed into "+New+"!");
            else
            System.out.println(old+" Transformed (Greater) into "+New+"!");
            this.Cname=New; 
            if ((this.index<113||this.index>115)&&this.transabs[0][0]==null) //transforming for the first time, not counting legion since he's special
            {
                this.transabs[0]=this.abilities;
                Ability[] newabilities=Ability.AssignAb(newindex);
                this.abilities=newabilities;
            }
            else if (this.index<113||this.index>115&&this.transabs[0][0]!=null) //already transformed earlier and now transforming back
            {
                Ability[] temp=this.transabs[0];
                this.transabs[0]=this.abilities;
                this.abilities=temp;
            }
            else //hero is legion
            {
                if (this.index==113)
                {
                    this.transabs[0]=this.abilities;
                }
                else if (this.index==114)
                {
                    this.transabs[1]=this.abilities;
                }
                else if (this.index==115)
                {
                    this.transabs[2]=this.abilities;
                }
                if (newindex==113)
                {
                    this.abilities=this.transabs[0];
                }
                else if (newindex==114)
                {
                    this.abilities=this.transabs[1];
                }
                else if (newindex==115)
                {
                    this.abilities=this.transabs[2];
                }
            }
            if (greater==true) //greater transformation is ocurring
            {
                this.maxHP=InHP(newindex, false); 
                this.HP=maxHP;
                this.SHLD=0;
                ArrayList <StatEff> removeme= new ArrayList<StatEff>();
                removeme.addAll(this.effects);        
                for (StatEff eff: removeme)
                {
                    if (!(eff instanceof Tracker))
                    this.remove(eff.hashcode, "normal"); 
                }
            }
            switch (this.index) //for getting rid of immunities when undoing transformation
            {
                case 24: StaticPassive.Binary(this, false); break;
            }
            this.index=newindex; 
            this.pdesc=Character.MakeDesc(newindex, false);
            switch (this.index) //for gaining immunities when transforming
            {
                case 24: StaticPassive.Binary(this, true); break;
            }
        }
        else
        System.out.println(this.Cname+"'s Transformation failed due to an immunity.");
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
    public void onAllyControlled (Character ally, Character controller) //ally is one being controlled
    {
    }
    @Override
    public void onSelfControlled (Character controller)
    {
    }
}