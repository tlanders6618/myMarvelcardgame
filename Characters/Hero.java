
package myMarvelcardgamepack;

        
/**
* Designer: Timothy Landers
* Date: 1/12/22
* Filename: Hero
* Purpose: Creates heroes and allows them to interact (fight) with each other.
*/
import java.util.ArrayList; 
import java.util.Scanner;
public class Hero extends Character
{
    public Hero (int Pindex)
    {
        super(Pindex); //character's semi-unique identifier number     
        this.pdesc=Character.MakeDesc(Pindex, false);
        //set identifiers
        this.HP=InHP (index, false);
        this.maxHP=HP;
        this.Cname=SetName(index, false);                
        //set abilities and passives
        this.AddImmune(true);
        this.abilities=Ability.AssignAb(index);     
    }
    @Override
    public void onFightStart()
    {
        switch (this.index)
        {
            case 6: ActivePassive.CaptainA(this); break;
            case 7: StaticPassive.Falcon(this); break;
            case 12: StaticPassive.DraxOG(this); break;
            case 16: StaticPassive.OGVenom (this); break;
            case 25: ActivePassive.Flash(this, 10, false, true); break;
            case 26: int ignore=StaticPassive.MODOC(this, null, "start", 0); break;
            case 41: StaticPassive.Rhino(this); break;
            case 76: ActivePassive.Speedball(this, true); break;
        }
    }
    @Override
    public void add (StatEff eff, boolean print) //adding a stateff
    {
        ArrayList<StatEff> conmod=new ArrayList<StatEff>(this.effects);
        for (StatEff e: conmod) //for stateffs that react to other stateffs
        {
            e.Attacked(this, eff);
        }
        if (print==true&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))&&!(eff.getimmunityname().equalsIgnoreCase("Banish"))) 
        //due to taunt/protect interaction as well as banish; no point in announcing it being added if it's instantly removed
        {
            System.out.println ("\n"+this+" gained a(n) "+eff);
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
        if (this.effects.contains(eff)) //if it wasn't instantly removed; primarily for banish
        {
            String type=eff.getefftype(); String name=eff.getimmunityname();
            switch (this.index) //after gaining effect
            {
                case 2: 
                if (name.equals("Intensify")&&eff.getefftype().equals("Buffs"))
                ActivePassive.Gamora(this, eff, true); 
                break;
                case 16: case 17: case 25: //venoms
                if (name.equals("Burn"))
                StaticPassive.Burn(this, 5);
                break;
                case 40:
                if (name.equals("Burn"))
                ActivePassive.Sandy(this, "burn");
                break;
                case 76:
                if (name.equals("Stun"))
                ActivePassive.Speedball(this, false);
                break;
                case 78: 
                if (name.equals("Burn"))
                {
                    StaticPassive.Burn(this, -10);
                    if (type.equals("Other")&&eff.power==0)
                    ActivePassive.Rulk(this, "add", 616);
                }
                break;
                case 90: //carnage
                if (name.equals("Burn"))
                StaticPassive.Burn(this, 10);
                break;
                case 92:
                if (name.equals("Intensify")&&type.equals("Other")&&eff.power==5)
                ActivePassive.Roblin(this, this, "gain");
                break;
                case 99:
                if (name.equals("Taunt"))
                ActivePassive.Colossus(this, true);
                break;
            }
            Character[] foes=Battle.GetTeam(CoinFlip.TeamFlip(this.team1));
            for (Character c: foes)
            {
                if (c!=null)
                {
                    c.onEnemyGain(this, eff);
                }
            }
        }
    }
    @Override
    public void remove (int removalcode, String how) //removes status effects; how is how it was removed, meaning purify or nullify or steal or normal (i.e. expiry)
    { 
        for (StatEff eff: this.effects)
        {
            if (eff.id==removalcode)
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
                    case 16: case 17: case 25: //venoms
                    if (name.equals("Burn"))
                    StaticPassive.Burn(this, -5);
                    break;
                    case 78: 
                    if (name.equals("Burn"))
                    {
                        StaticPassive.Burn(this, 10);
                        if (type.equals("Other")&&eff.power==0)
                        ActivePassive.Rulk(this, "remove", 616);
                    }
                    break;
                    case 90: //cletus kasady
                    if (name.equals("Burn"))
                    StaticPassive.Burn(this, -10);
                    break;
                    case 99:
                    if (name.equals("Taunt"))
                    ActivePassive.Colossus(this, false);
                    break;
                }    
                break; //end the for each loop
            }
        }
    }
    @Override
    public void StatFailed (Character target, StatEff e, String cause) //any hero's eff failed to apply; target is hero the stateff was supposed to be applied to
    {
        switch (this.index)
        {
            case 75: 
            if (target==this)
            ActivePassive.Moonstone(this, e); 
            break;
        }
    }
    @Override
    public void onEnemyGain (Character foe, StatEff e) //enemy gained a stateff
    {
        if (!(this.binaries.contains("Banished"))&&!(foe.binaries.contains("Banished")))
        {
            switch (this.index)
            {
                case 90: 
                if (e.getimmunityname().equals("Bleed"))
                ActivePassive.Carnage(this, e.oduration); 
                break;
            }
        }
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
            case 32: StaticPassive.BB(this); break;
            case 33: StaticPassive.Deadpool(this, "heal", null); break;
            case 35: ActivePassive.Cain(this, "turn", 616); break;
            case 40: ActivePassive.Sandy(this, "turn"); break;
            case 72: StaticPassive.Zemo(this, true); break;
            case 74: ActivePassive.Songbird(this); break;
            case 78: ActivePassive.Rulk(this, "turn", 0); break;
            case 97: ActivePassive.Angel(this, true, 0); break;
            case 104: ActivePassive.Bishop(this, 0, "turn"); break;
        }
        super.onTurn(notbonus); //call on allyturn and onenemyturn
    }
    @Override
    public void onTurnEnd (boolean notbonus)
    {
        super.onTurnEnd(notbonus); //tick stateffs and cds
        switch (this.index)
        {
            case 12: ActivePassive.DraxOG(this, false, null, null); break;
            case 13: StaticPassive.Drax(this, null, "turnend"); break;
            case 76: ActivePassive.Speedball(this, false); break;
            case 77: ActivePassive.Penance(this, -616); break;
        }
    }
    @Override
    public void onAllyTurn (Character ally, boolean summoned) //ally is the one triggering call and this is one reacting
    {
        if (!(this.binaries.contains("Banished"))&&!(ally.binaries.contains("Banished"))) //remember that this triggers for dead heroes too
        {
            switch (index)
            {
                case 11: ActivePassive.FuryJr(this, "allyturn", summoned); break;
                case 24: ActivePassive.Binary(this); break;
                case 40: ActivePassive.Sandy(this, "turn"); break;
                case 105: ActivePassive.Immortal(this, "ally"); break;
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
    public void BeforeAttack (Character victim, boolean target)
    {
        if (target==true)
        {
            switch (this.index) //for passives that let the dealer ignore protect or miss or etc against certain targets; called before checking for protect 
            {
                case 20: ActivePassive.Superior(this, victim, true); break;
            }
            if (this.CheckFor("Aura", false)==true&&this.activeability.aoe==false) //aura tempstring applied regardless of target
            {
                for (StatEff e: this.effects)
                {
                    if (e.getimmunityname().equals("Aura"))
                    {
                        e.Attacked(this, this, 616);
                    }
                }
            }
        }
        else //once target is determined after checking for protect and passives like spidey's, check if passive activates against the target
        {
            switch (this.index)
            {
                case 13: StaticPassive.Drax(this, victim, "battack"); break;
                case 14: ActivePassive.X23(this, victim, false, true); break;
                case 25: ActivePassive.Flash(this, 0, true, false); break;
                case 26: int ignore=StaticPassive.MODOC(this, victim, "attack", 616); break;
                case 33: StaticPassive.Deadpool(this, "attack", victim); break;
                case 36: StaticPassive.Vulture(this, victim); break;
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
                if (eff.getimmunityname().equalsIgnoreCase("Protect")&&!(eff.getProtector()==target)) //target must have protected, not be protecting someone else
                {
                    Character bigman=eff.getProtector(); 
                    if (!(target.binaries.contains("Banished"))&&!(bigman.binaries.contains("Stunned"))&&!(bigman.binaries.contains("Banished"))) //doesn't work while banished
                    {
                        if (eff.getefftype().equalsIgnoreCase("Defence")&&!(attacker.ignores.contains("Defence"))) 
                        {
                            System.out.println(bigman+" protected "+target+"!");
                            return bigman; //protector becomes target instead
                        } 
                        else if (eff.getefftype().equalsIgnoreCase("Other")) 
                        {
                            System.out.println(bigman+" protected "+target+"!");
                            return bigman; //if the character is protected, end method bc they're safe now
                        }            
                    }
                }
            }
        }
        else //only need to notify allies and activate their passives if the character is still vulnerable
        {
            Character[] friends=Battle.GetTeammates(target);
            for (Character friend: friends) //this is where spidey, thing, etc do their thing
            {
                if (friend!=null&&!(friend.binaries.contains("Banished")))
                {
                    ntarg=friend.onAllyTargeted(attacker, target, dmg, aoe);
                    if (ntarg!=target)
                    return ntarg; //only the first target switching passive should take effect
                }
            }
            switch (target.index) //for passives like howard the duck's that apply when attacked but take place before damage is dealt; mainly for avoidance
            {
                //no need to trigger these passives if protect/another passive was activated, since protect changes the attack's target
                //but if no one else is going to be the target, then the original target triggers their ontargeted passive
                case 18: ActivePassive.Spidey(target, attacker, true, aoe); break;
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
            case 13: StaticPassive.Drax (this, null, "onattack"); break;
            case 14: ActivePassive.X23(this, victim, false, false); break;
            case 20: ActivePassive.Superior(this, victim, false); break;
            case 23: ActivePassive.CM(this, false, -616); break;
            case 33: StaticPassive.Deadpool(this, "attack", victim); break;
            case 35: ActivePassive.Cain(this, "attack", 616); break;
            case 72: StaticPassive.Zemo(this, false); break;
            case 86: StaticPassive.Kraven(this, victim, false); break;
            case 92: ActivePassive.Roblin(this, victim, "attack"); break;
            case 98: ActivePassive.AA(this, victim); break;
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
    public void onAttacked(Character attacker, int dmg, boolean aoe)
    {
        switch (this.index) //for passives that trigger even if the hero died from the attack
        {
            case 25: ActivePassive.Flash(this, -3, false, false); break;
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
                    if (!(eff.getimmunityname().equals("Aura"))||(eff.getimmunityname().equals("Aura")&&aoe==false))
                    eff.Attacked(this, attacker, dmg);
                }
            }
            switch (this.index)
            {
                case 15: ActivePassive.Wolvie(this, true); break;
                case 70: ActivePassive.Ironclad(this, attacker, 616, true); break;
                case 76: ActivePassive.Speedball(this, false); break;
                case 81: StaticPassive.DD(this, attacker); break;
                case 104: ActivePassive.Bishop(this, dmg, "attacked"); break;
            }
        } 
    }
    @Override
    public void onAllyAttacked(Character ally, Character attacker, int dmg) 
    {
        if (!(this.binaries.contains("Banished"))&&!(ally.binaries.contains("Banished")))
        {
            switch (this.index)
            {
                case 1: ActivePassive.MoonKnight(this, ally, attacker); break;
                case 16: ActivePassive.OGVenom (this, ally, attacker); break;
                case 70: ActivePassive.Ironclad(this, ally, dmg, false); break;
            }
        }
    }
    @Override
    public Character onAllyTargeted (Character dealer, Character ally, int dmg, boolean aoe)
    {
        if (!(this.binaries.contains("Banished"))&&!(ally.binaries.contains("Banished"))&&aoe==false) //aoe abilities target everyone, so they're considered as targeting no one
        {
            switch (this.index)
            {
                case 18: 
                if (!(this.binaries.contains("Stunned"))&&ally.HP<this.HP&&this.CheckFor("Evade", false)==true)
                {
                    System.out.println("With great power, there must also come great responsibility."); 
                    System.out.println(this.Cname+" took the attack for "+ally.Cname+"!");
                    return this;
                }
                break;
            }
        }
        return ally;
    }
    @Override
    public int TakeDamage (Character dealer, int dmg, boolean aoe) //this checks if shield is strong enough to prevent health damage from an enemy attack
    {
        switch (this.index)
        {
            case 26: dmg=StaticPassive.MODOC(this, dealer, "attacked", dmg); break;
            case 77: dmg=ActivePassive.Penance(this, dmg); break;
            case 83: dmg=StaticPassive.LukeCage(this, dmg); break;
        }
        int odmg=dmg;
        if (dealer.ignores.contains("Shield")||dealer.ignores.contains("Defence"))
        {
            //do nothing and skip to checkbarrier
        } 
        else if (SHLD>=dmg) 
        {
            this.SHLD-=dmg; 
            dmg=0;
        }
        else if (SHLD<dmg) //shield broken; can't absorb all the damage
        {
            int s=this.SHLD;
            dmg-=s;
            this.SHLD=0;
        }
        Damage_Stuff.CheckBarrier(this, dealer, dmg);
        this.TookDamage(dealer, odmg);
        return odmg;
    }
    @Override
    public int TakeDamage (int dmg, boolean dot) //same as above but for taking sourceless damage
    {
        int odmg=dmg;
        if (SHLD>=dmg) 
        {
            this.SHLD-=dmg; 
            dmg=0;
        }
        else if (SHLD<dmg) 
        {
            int s=this.SHLD;
            dmg-=s;
            this.SHLD=0;
        }
        Damage_Stuff.CheckBarrier(this, null, dmg);
        this.TookDamage(dot, dmg);
        return odmg;
    }
    @Override
    public void TookDamage (boolean dot, int dmg) //check if hero should be dead and either do hpchange or kill them; for sourceless dmg
    { 
        this.dmgtaken+=dmg;
        int h=this.HP; h+=dmg; //for tracking hp changes for passives
        if (this.HP<=0)
        this.HP=0;
        if (this.HP<=0&&dot==true&&!(this.binaries.contains("Immortal")))
        {
            this.onLethalDamage(null, "DOT");
        }
        else if (this.HP<=0&&dot==false&&!(this.binaries.contains("Immortal")))
        {
            this.onLethalDamage(null, "other");
        }
        if (this.dead==false)
        {
            this.HPChange(h, this.HP);
            switch (this.index)
            {
                case 15: ActivePassive.Wolvie(this, false); break;
                case 78: ActivePassive.Rulk(this, "hurt", dmg); break;
                case 97: ActivePassive.Angel(this, false, dmg); break;
                case 104: ActivePassive.Bishop(this, dmg, "damaged"); break;
            }
        }
    }
    @Override
    public void TookDamage (Character dealer, int dmg) //for taking damage from a hero
    {
        if (this.immunities.contains("Damage")) //dmg number is unchanged, but print 0 because they didn't take any dmg
        System.out.println ("\n"+dealer.Cname+" did 0 damage to "+this.Cname);
        else
        System.out.println ("\n"+dealer.Cname+" did "+dmg+" damage to "+this.Cname);
        this.dmgtaken+=dmg; 
        int h=this.HP; h+=dmg;
        if (this.HP<=0)
        this.HP=0;
        if (this.HP<=0&&!(this.binaries.contains("Immortal")))
        {
            this.onLethalDamage(dealer, "attack");
        }
        else
        {
            this.HPChange(h, this.HP);
            switch (this.index)
            {
                case 15: ActivePassive.Wolvie(this, false); break;
                case 23: ActivePassive.CM(this, false, dmg); break;
                case 78: ActivePassive.Rulk(this, "hurt", dmg); break;
                case 97: ActivePassive.Angel(this, false, dmg); break;
                case 104: ActivePassive.Bishop(this, dmg, "damaged"); break;
            }
        }
    }
    @Override
    public void HPChange (int oldhp, int newhp)
    {
        switch (this.index)
        {
            case 10: StaticPassive.FurySr(this); break;
            case 31: ActivePassive.Hulk(this); break;
            case 35: ActivePassive.Cain(this, "change", oldhp); break;
            case 94: StaticPassive.Phil(this); break;
        }
        if (!(this.binaries.contains("Banished")))
        {
            Character[] people=Battle.GetTeammates(this);
            for (Character friend: people)
            {
                if (friend!=null)
                {
                    friend.AllyHPChange(this, oldhp, newhp);
                }
            }
        }
    }
    @Override
    public void AllyHPChange (Character ally, int oldhp, int newhp)
    {
        if (!(this.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void EnemyHPChange (Character enemy, int oldhp, int newhp)
    {
        if (!(this.binaries.contains("Banished"))||!(enemy.binaries.contains("Banished")))
        {
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
        if (die==true&&!(this.binaries.contains("Immortal")))
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
        else //if (!(dmgtype.equalsIgnoreCase("DOT"))) 
        {
            System.out.println(this.Cname+" has died");
        }
        if (this.activeability!=null&&this.activeability.channelled==true)
        {
            this.activeability.InterruptChannelled(this, this.activeability); //already checks if channelling was finished
        }
        ArrayList <StatEff> removeme= new ArrayList<StatEff>();
        removeme.addAll(this.effects);  
        for (StatEff eff: removeme)
        {
            if (!(eff instanceof Tracker))
            {
                this.remove(eff.id, "silent"); 
            }
        }
        this.HP=0;
        this.dead=true;
        this.dmgtaken=0;
        this.turn=0;
        Character[] people=Battle.GetTeammates(this);
        for (Character friend: people)
        {
            if (friend!=null)
            {
                friend.onAllyDeath(this, killer);
            }
        }
        Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(this.team1));
        for (Character ant: enemies)
        {
            if (ant!=null)
            {
                ant.onEnemyDeath(this, killer);
            }
        }
        Battle.AddDead(this);
        switch (this.index) //only trigger ondeath passives once stateffs and variables have been fully removed/properly changed
        {
            case 5: case 16:
            if (this.passivefriend.get(0)!=null&&this.passivefriend.get(0).dead==false)
            {
                this.passivefriend.get(0).remove(passivecount, "normal"); //remove heat signature detection's target/lethal protector's resistance
                if (this.index==5)
                this.passivefriend.get(0).immunities.remove("Invisible"); //undo heat signature's effects
                else if (this.index==16) 
                {
                    StatEff r=null; //remove the lethal protector tracker
                    for (StatEff e: this.passivefriend.get(0).effects)
                    {
                        if (e instanceof Tracker&&e.geteffname().equals("Watched by Venom (Eddie Brock)"))
                        {
                            r=e; break;
                        }
                    }
                    this.passivefriend.get(0).remove(r.id, "silent");
                }
            }
            break;
            case 100: StaticPassive.Elixir(this); break;
            case 105: ActivePassive.Immortal(this, "death"); break;
        }
    }
    @Override
    public void onAllyDeath (Character deadfriend, Character killer)
    {
        if (!(this.binaries.contains("Banished"))&&!(deadfriend.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onEnemyDeath (Character deadfoe, Character killer)
    {
        if (!(this.binaries.contains("Banished"))||!(deadfoe.binaries.contains("Banished")))
        {
            switch (this.index)
            {
                case 92: ActivePassive.Roblin(this, deadfoe, "death"); break;
                case 93: ActivePassive.OGHobby(this, killer); break;
            }
        }
        if (killer!=null&&this.id==killer.id)
        this.onKill(deadfoe);
    }
    @Override
    public void onKill (Character victim) //no need to do banish check for obvious reasons
    {
        switch (this.index)
        {
            case 17: case 90: ActivePassive.Venom(this); break; //macdonald and college have the same passive for some reason, so this is efficient albeit mildly confusing
            case 33: StaticPassive.Deadpool(this, "kill", victim); break;
        }
    } 
    @Override 
    public void onRez (Character healer) //healer is one who rezzed them
    {
        switch (this.index)
        {
            case 16: 
            if (this.passivefriend.get(0).dead==false) //readd tracker that was removed on venom's death since his passive will continue to apply
            this.passivefriend.get(0).add(new Tracker ("Watched by Venom (Eddie Brock)"), false);
            break;
        }
        Character[] friends=Battle.GetTeammates(this);
        for (Character c: friends)
        {
            if (c!=null&&!(c.binaries.contains("Banished")))
            {
                c.onAllyRez(this, healer);
            }
        }
    }
    @Override
    public void onAllyRez (Character ally, Character healer)
    {
        if (!(this.binaries.contains("Banished")))
        {
            switch (this.index)
            {
            }
        }
    }
    @Override
    public void onEvade (Character attacker) //for nightcrawler, sabretooth, stature, etc
    {
        switch (this.index)
        {
            case 103: ActivePassive.Crawler(this, attacker); break;
        }
        Character[] friends=Battle.GetTeammates(this);
        for (Character c: friends)
        {
            if (c!=null)
            {
                c.onAllyEvade(this, attacker);
            }
        }
        Character[] foes=Battle.GetTeam(CoinFlip.TeamFlip(this.team1));
        for (Character c: foes)
        {
            if (c!=null)
            {
                c.onEnemyEvade(this, attacker);
            }
        }
    }
    @Override
    public void onAllyEvade (Character ally, Character attacker)
    {
        if (!(this.binaries.contains("Banished"))&&!(ally.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onEnemyEvade (Character enemy, Character attacker)
    {
        if (!(this.binaries.contains("Banished"))||!(enemy.binaries.contains("Banished")))
        {
        }
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
                    this.remove(eff.id, "normal"); 
                }
            }
            this.AddImmune(false); //for getting rid of immunities when leaving a transformed form; e.g. a robot transforming would lose robot immunities
            this.index=newindex; 
            this.passivecount=0;
            this.pdesc=Character.MakeDesc(newindex, false);
            this.AddImmune(true); //for gaining immunities when transforming into someone; e.g. transforming into a robot would grant robot immunities
            switch (this.index) //for triggering passives that specifically occur when transforming, like infinity gauntlet thanos's and diamond frost's
            {
                case 24: StaticPassive.Binary(this); break;
                case 96: StaticPassive.Frost(this); break;
            }
        }
        else
        System.out.println(this.Cname+"'s Transformation failed due to an immunity.");
    }
    @Override
    public void onAllySummon (Character hero, Summon newfriend)
    {
        if (!(hero.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onEnemySummon (Character hero, Summon newfoe)
    {
        if (!(hero.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onAllyControlled (Character ally, Character controller) //ally is one being controlled
    {
        if (!(this.binaries.contains("Banished")))
        {
        }
    }
    @Override
    public void onSelfControlled (Character controller)
    {
    }
    @Override
    public void AddImmune (boolean add) //called on character creation/transformation; avoids problem of phoenix's passive conflicting with dormammu's, etc
    {
        if (add==true) //immunities are now gained before fight start; also applies tracker since they're as equally intrinsic to heroes as immunities are
        {
            switch (this.index) 
            {
                //2.0: Original
                case 8: //bucky
                this.ignores.add("Defence"); break; 
                case 12: //drax classic
                this.immunities.add("Buffs"); this.immunities.add("Persuaded"); CoinFlip.IgnoreTargeting(this, true); break; 
                case 15: //wolverine
                Tracker frenzy= new Tracker("Damage Taken: "); this.effects.add(frenzy); frenzy.onApply(this); break;
                case 16: case 17: case 90: //venom brock, venom mac, and carnage
                this.ignores.add("Evade"); break; 
                case 23: //captain marvel
                Tracker NRG= new Tracker("Energy: "); this.effects.add(NRG); NRG.onApply(this); this.immunities.add("Poison"); this.BuDR+=999; this.ShDR+=999; break; 
                case 24: //binary
                Tracker kotal= new Tracker("Energy: "); this.effects.add(kotal); kotal.onApply(this); CoinFlip.StatImmune(this, true); break; 
                case 25: //agent venom
                this.ignores.add("Evade"); Tracker t=new Tracker("Control Points: "); this.effects.add(t); t.onApply(this); break; 
                case 26: //modok
                CoinFlip.IgnoreTargeting(this, true); break; 
                case 27: //ultron
                CoinFlip.RobotImmunities(this, true); this.immunities.add("Snare"); this.immunities.add("Steal"); this.immunities.add("Control"); break; 
                case 28: //dr doom
                Tracker er=new Tracker("Titanium Battlesuit armed"); this.effects.add(er); this.immunities.add("Persuaded"); this.immunities.add("Control");
                this.immunities.add("Burn"); this.immunities.add("Freeze"); this.immunities.add("Shock"); break;
                case 30: //brawn
                this.immunities.add("Poison"); this.immunities.add("Control"); break; 
                case 31: //hulk
                Tracker rage= new Tracker ("Rage: "); this.effects.add(rage); rage.onApply(this); this.immunities.add("Terror"); this.immunities.add("Poison"); 
                this.immunities.add("Control"); this.immunities.add("Persuaded"); break;
                case 32: //black bolt
                this.immunities.add("Control"); Tracker elect= new Tracker ("Electrons: "); this.effects.add(elect); elect.onApply(this); break; 
                case 35: //juggernaut
                Tracker wrath= new Tracker("Momentum: "); this.effects.add(wrath); wrath.onApply(this); 
                this.immunities.add("Copy"); this.immunities.add("Snare"); this.immunities.add("Stun");
                if (this.HP>100)
                {
                    this.ADR+=15; this.immunities.add("Control"); Tracker salt= new Tracker("Cyttorak's Blessing active"); this.effects.add(salt);
                }
                else
                {
                    Tracker chip= new Tracker("Cyttorak's Blessing lost"); this.effects.add(chip);
                }
                break;
                //2.1: Sinister Six
                case 40: //sandman
                this.immunities.add("Bleed"); this.immunities.add("Shock"); this.immunities.add("Snare"); this.immunities.add("Disarm"); this.ignores.add("Counter"); break;
                case 41: //rhino
                this.immunities.add("Vulnerable"); this.immunities.add("Suppression"); this.immunities.add("Reduce"); this.immunities.add("Terror"); this.BlDR+=15; break; 
                //2.6: U-Foes
                case 68: //vector
                this.immunities.add("Bleed"); this.immunities.add("Shock"); this.immunities.add("Freeze"); this.immunities.add("Burn"); break;
                case 69: //x-ray
                this.immunities.add("Bleed"); this.immunities.add("Poison"); this.immunities.add("Heal"); this.WiDR+=999; break;
                case 70: //ironclad
                this.immunities.add("Bleed"); this.immunities.add("Burn"); this.immunities.add("Freeze"); break;
                case 71: //vapor
                this.immunities.add("Bleed"); this.immunities.add("Poison"); this.ignores.add("Evade"); break;
                //2.7: Thunderbolts
                case 72: //zemo
                this.immunities.add("Disarm"); this.immunities.add("Steal"); this.ignores.add("Guard"); break;
                case 75: //moonstone
                this.immunities.add("Steal"); this.ignores.add("Provoke"); this.ignores.add("Terror"); break;
                case 76: //speedball
                this.immunities.add("Control"); this.immunities.add("Heal"); this.immunities.add("Snare"); this.immunities.add("Reduce"); this.immunities.add("Lose"); 
                CoinFlip.DMGImmune(this, true); break;
                case 77: //penance
                Tracker hot= new Tracker ("Pain: "); this.effects.add(hot); hot.onApply(this); this.immunities.add("Control"); this.immunities.add("Heal"); break;
                case 80: //scarecrow
                this.immunities.add("Terror"); break;
                //2.8: Defenders
                case 81: //daredevil
                this.ignores.add("Blind"); this.ignores.add("Invisible"); break;
                case 83: //luke cage
                this.immunities.add("Burn"); this.immunities.add("Bleed"); this.immunities.add("Shock"); break;
                case 85: //silver surfer
                CoinFlip.StatImmune(this, true); this.immunities.add("Interrupt"); break;
                //2.9: Fearsome Foes of Spider-Man
                case 89: //hydroman
                this.immunities.add("Bleed"); this.immunities.add("Burn"); this.immunities.add("Soaked"); this.ShDR-=10; break;
                case 92: //roblin
                this.immunities.add("Burn"); this.immunities.add("Snare"); break;
                //2.10: Marvellous Mutants
                case 96: //diamond frost
                this.ADR+=15; this.WiDR+=999; this.immunities.add("Heal"); this.immunities.add("Stun"); this.immunities.add("Control"); this.immunities.add("Bleed"); 
                this.immunities.add("Burn"); this.immunities.add("Poison"); this.immunities.add("Shock"); break;
                case 99: //colossus
                this.immunities.add("Bleed"); this.immunities.add("Burn"); this.immunities.add("Freeze"); break;
                case 104: //bishop
                Tracker pip= new Tracker("Energy Reserve: "); this.effects.add(pip); pip.onApply(this); break;
            }    
        }
        else //remove immunities when transforming 
        {
            switch (this.index) 
            {
                //2.0: Original
                case 8: //bucky
                this.ignores.remove("Defence"); break; 
                case 12: //drax classic
                this.immunities.remove("Buffs"); this.immunities.remove("Persuaded"); CoinFlip.IgnoreTargeting(this, false); break; 
                case 15: //wolverine
                StatEff thor=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Damage Taken: "))
                    {
                        thor=e; break;
                    }
                }
                this.remove(thor.id, "silent");
                break;
                case 16: case 17: case 90: //venom brock, venom mac, and carnage
                this.ignores.remove("Evade"); break; 
                case 23: //captain marvel
                StatEff tor=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Energy: "))
                    {
                        tor=e; break;
                    }
                }
                this.remove(tor.id, "silent"); this.immunities.remove("Poison"); this.BuDR-=999; this.ShDR-=999; 
                break; 
                case 24: //binary
                StatEff khan=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Energy: "))
                    {
                        khan=e; break;
                    }
                }
                this.remove(khan.id, "silent"); CoinFlip.StatImmune(this, false); 
                break; 
                case 25: //agent venom
                StatEff ret=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Control Points: "))
                    {
                        ret=e; break;
                    }
                }
                this.remove(ret.id, "silent"); this.ignores.remove("Evade"); 
                break; 
                case 26: //modok
                CoinFlip.IgnoreTargeting(this, false); break; 
                case 27: //ultron
                CoinFlip.RobotImmunities(this, false); this.immunities.remove("Snare"); this.immunities.remove("Steal"); this.immunities.remove("Control"); break; 
                case 28: //dr doom
                StatEff tree=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.geteffname().equals("Titanium Battlesuit armed"))
                    {
                        tree=e; break;
                    }
                }
                this.remove(tree.id, "silent"); this.immunities.remove("Persuaded"); this.immunities.remove("Control"); this.immunities.remove("Burn"); 
                this.immunities.remove("Freeze"); this.immunities.remove("Shock"); 
                break;
                case 30: //brawn
                this.immunities.remove("Poison"); this.immunities.remove("Control"); break; 
                case 31: //hulk
                StatEff tef=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Rage: "))
                    {
                        tef=e; break;
                    }
                }
                this.remove(tef.id, "silent"); this.immunities.remove("Terror"); this.immunities.remove("Poison"); 
                this.immunities.remove("Control"); this.immunities.remove("Persuaded"); 
                break;
                case 32: //black bolt
                StatEff fer=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Electrons: "))
                    {
                        fer=e; break;
                    }
                }
                this.remove(fer.id, "silent"); this.immunities.remove("Control"); 
                break; 
                case 35: //juggernaut
                StatEff very=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Momentum: "))
                    {
                        very=e; break;
                    }
                }
                this.remove(very.id, "silent"); this.immunities.remove("Snare"); this.immunities.remove("Stun"); this.immunities.remove("Copy");
                if (this.HP>100)
                {
                    this.ADR-=15; this.immunities.remove("Control"); 
                    StatEff fed=null;
                    for (StatEff e: this.effects)
                    {
                        if (e instanceof Tracker&&e.geteffname().equals("Cyttorak's Blessing active"))
                        {
                            fed=e; break;
                        }
                    }
                    this.remove(fed.id, "silent");
                }
                else
                {
                    StatEff derf=null;
                    for (StatEff e: this.effects)
                    {
                        if (e instanceof Tracker&&e.geteffname().equals("Cyttorak's Blessing lost"))
                        {
                            derf=e; break;
                        }
                    }
                    this.remove(derf.id, "silent");
                }
                break;
                //2.1: Sinister Six
                case 40: //sandman
                this.immunities.remove("Bleed"); this.immunities.remove("Shock"); this.immunities.remove("Snare"); 
                this.immunities.remove("Disarm"); this.ignores.remove("Counter"); break;
                case 41: //rhino
                this.immunities.remove("Vulnerable"); this.immunities.remove("Suppression"); this.immunities.remove("Reduce"); this.immunities.remove("Terror"); this.BlDR-=15; break; 
                //2.6: U-Foes
                case 68: //vector
                this.immunities.remove("Bleed"); this.immunities.remove("Shock"); this.immunities.remove("Freeze"); this.immunities.remove("Burn"); break;
                case 69: //x-ray
                this.immunities.remove("Bleed"); this.immunities.remove("Poison"); this.immunities.remove("Heal"); this.WiDR-=999; break;
                case 70: //ironclad
                this.immunities.remove("Bleed"); this.immunities.remove("Burn"); this.immunities.remove("Freeze"); break;
                case 71: //vapor
                this.immunities.remove("Bleed"); this.immunities.remove("Poison"); this.ignores.remove("Evade"); break;
                //2.7: Thunderbolts
                case 72: //zemo
                this.immunities.remove("Disarm"); this.immunities.remove("Steal"); this.ignores.remove("Guard"); break;
                case 75: //moonstone
                this.immunities.remove("Steal"); this.ignores.remove("Provoke"); this.ignores.remove("Terror"); break;
                case 76: //speedball
                this.immunities.remove("Control"); this.immunities.remove("Heal"); this.immunities.remove("Snare"); this.immunities.remove("Reduce"); this.immunities.remove("Lose");
                CoinFlip.DMGImmune(this, false); break;
                case 77: //penance
                StatEff noggin=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Pain: "))
                    {
                        noggin=e; break;
                    }
                }
                this.remove(noggin.id, "silent"); this.immunities.remove("Control"); this.immunities.remove("Heal");  
                break;
                case 80: //scarecrow
                this.immunities.remove("Terror"); break;
                //2.8: Defenders
                case 81: //daredevil
                this.ignores.remove("Blind"); this.ignores.remove("Invisible"); break;
                case 83: //luke cage
                this.immunities.remove("Burn"); this.immunities.remove("Bleed"); this.immunities.remove("Shock"); break;
                case 85: //silver surfer
                CoinFlip.StatImmune(this, false); this.immunities.remove("Interrupt"); break;
                //2.9: Fearsome Foes of Spider-Man
                case 89: //hydroman
                this.immunities.remove("Bleed"); this.immunities.remove("Burn"); this.immunities.remove("Soaked"); this.ShDR+=10; break;
                case 92: //roblin
                this.immunities.remove("Burn"); this.immunities.remove("Snare"); break;
                //2.10: Marvellous Mutants
                case 96: //diamond frost
                this.ADR-=15; this.WiDR-=999; this.immunities.remove("Heal"); this.immunities.remove("Stun"); this.immunities.remove("Control"); this.immunities.remove("Bleed"); 
                this.immunities.remove("Burn"); this.immunities.remove("Poison"); this.immunities.remove("Shock"); break;
                case 99: //colossus
                this.immunities.remove("Bleed"); this.immunities.remove("Burn"); this.immunities.remove("Freeze"); break;
                case 104: //bishop
                StatEff cant=null;
                for (StatEff e: this.effects)
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Energy Reserve: "))
                    {
                        cant=e; break;
                    }
                }
                this.remove(cant.id, "silent"); 
                break; 
            }       
        }
    }
}