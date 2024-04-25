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
    //passivefriend[0] is the summoner
    public Summon (int Sindex)
    {
        //Same as with characters
        //Need to manually assign team affiliation when creating a summon; not done in constructor
        index=Sindex;
        summoned=true;
        hash=Card_HashCode.RandomCode();
        size=SetSizeSum(index);
        this.pdesc=Character.MakeDesc(Sindex, true);
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
    public void onSummon ()
    {
        switch (this.index) //for triggering on summon passives
        {
            case 1: SummonPassive.NickLMD(this); break;
            case 3: SummonPassive.Crushbot(this, true); break;
            case 4: SummonPassive.Drone(this, true, null); break;
            case 5: SummonPassive.LilDoomie(this, true, null); break;
            case 6: int ignore=SummonPassive.Daemon(this, true, null, 0); break;
            case 7: SummonPassive.Decoy(this); break;
            case 12: SummonPassive.Giganto(this, "spawn"); break;
        }
        CheckSumDupes(this);
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
            case 2: name="AIM Rocket Trooper "+counter+" (Summon)";break;
            case 3: name="AIM Crushbot "+counter+" (Summon)"; break;
            case 4: name="Ultron Drone "+counter+" (Summon)"; break;
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
            return 2;
            default: return 1;
        }
    }
    //Below are overridden to avoid conflict between summmon and hero indexes
    @Override
    public void add (StatEff eff) //adding a stateff
    {
        this.effects.add(eff); 
        String type=eff.getefftype(); String name=eff.getimmunityname();
        switch (this.index)
        {
            case 4: 
            if (type.equals("Buffs"))
            SummonPassive.Drone(this, false, eff); 
            break;
            case 12:
            if (name.equals("Stun"))
            SummonPassive.Giganto(this, "gain");
            break;
        }
        for (StatEff e: this.effects) //for stateffs that react to other stateffs
        {
            e.Attacked(eff);
        }
        if (!(eff.getefftype().equals("Secret"))&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))) 
        //due to taunt/protect interaction; no point in announcing it being added if it's instantly removed
        {
            System.out.println ("\n"+this.Cname+" gained a(n) "+eff.geteffname());
        }
        eff.onApply(this);
    }
    @Override
    public void remove (int removalcode, String how) //removes status effects
    {
        for (StatEff eff: this.effects)
        {
            if (eff.hashcode==removalcode)
            {
                String name=eff.getimmunityname(); String type=eff.getefftype();
                eff.Nullified(this);
                //redundant to print the message if something like nullify already does, so it's only printed if nullify is false
                if (how.equals("normal")&&!(eff.getimmunityname().equals("Protect")||eff.getimmunityname().equals("Evade")))                 
                {
                    System.out.println (this.Cname+"'s "+eff.getimmunityname()+" expired."); 
                }      
                this.effects.remove(eff);
                switch (this.index)
                {
                    case 12: 
                    if (name.equals("Stun"))
                    {
                        SummonPassive.Giganto(this, "lose"); 
                        break; //out of switch
                    }
                }  
                break; //end the for each loop
            }
        }
    }
    @Override
    public void onTurn (boolean notbonus)
    {
        switch (this.index)
        {
            case 3: SummonPassive.Crushbot(this, false); break;
            case 7: 
            if (this.team1==true) 
            {
                if (Battle.p1solo==true) //to prevent infinite turn skipping if a decoy is the only one left alive on its team
                this.binaries.remove("Stunned");
                else //to prevent infinite turn skipping if there is more than one hero on the team, but they're all decoys
                {
                    Character[] team=Battle.GetTeammates(this); boolean solo=true;
                    for (Character c: team)
                    {
                        if (c!=null&&(c.summoned==false||c.index!=7))
                        {
                            solo=false; break;
                        }
                    }
                    if (solo==true)
                    this.binaries.remove("Stunned");
                 }
            }
            else
            {
                if (Battle.p2solo==true) 
                this.binaries.remove("Stunned");
                else 
                {
                    Character[] team=Battle.GetTeammates(this); boolean solo=true;
                    for (Character c: team)
                    {
                        if (c!=null&&(c.summoned==false||c.index!=7))
                        {
                            solo=false; break;
                        }
                    }
                    if (solo==true)
                    this.binaries.remove("Stunned");
                }
            }
            break;
        }
        super.onTurn(notbonus);
    }
    @Override
    public void onTurnEnd (boolean notbonus)
    {
    }
    @Override
    public void onAllyTurn (Character ally, boolean summoned) //ally is the one triggering call and this is one reacting; true if teammate is a summon
    {
    }
    @Override
    public void onEnemyTurn (Character enemy, boolean summoned)
    {
    }
    @Override
    public void onFightStart()
    {
    }
    @Override
    public void BeforeAttack (Character dealer, Character victim, boolean target)
    {
    }
    @Override
    public void onAttack (Character victim)
    {
        switch (this.index)
        {
            case 12: SummonPassive.Giganto(this, "attack"); break;
        }
    }
    @Override
    public void onCrit (Character target)
    {
    }
    @Override
    public void onAttacked(Character attacker, int dmg)
    {
        if (this.dead==false)
        {
            ArrayList<StatEff> concurrentmodificationexception3electricboogalooboogaloo= new ArrayList<StatEff>();
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
        } 
    }
    @Override
    public void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg)
    {
    }
    @Override
    public void HPChange (int oldhp, int newhp)
    {
    }
    @Override
    public Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe)
    {
        Character ntarget=target;      
        return ntarget;
    }
    @Override
    public int TakeDamage (Character target, Character dealer, int dmg, boolean aoe) //this is the default method for taking damage
    {
        switch (target.index)
        {
            case 6: dmg=SummonPassive.Daemon(target, false, dealer, dmg); break;
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
    public int TakeDamage (Character target, int dmg, boolean dot) //this checks if shield is strong enough to prevent health damage from an enemy attack
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
            //hero.HPChange(hero, h, hero.HP); //commented out for now since no summons use this method; saves time of checking it
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
            hero.onLethalDamage(dealer, "attack");
        }
        else
        {
            //check passive
        }
    }
    @Override
    public void onLethalDamage (Character killer, String dmgtype)
    {
        if (!(this.binaries.contains("Immortal")))
        this.onDeath(killer, dmgtype);
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
        boolean foe=CoinFlip.TeamFlip(this.team1); //to get their enemies
        Character[] enemies=Battle.GetTeam(foe);
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
    public void onAllyDeath (Character bystander, Character deadfriend, Character killer)
    {
        switch (bystander.index)
        {
            case 5: SummonPassive.LilDoomie(bystander, false, deadfriend); break;
        }
    }
    @Override
    public void onEnemyDeath (Character bystander, Character deadfoe, Character killer)
    {
        if (killer!=null&&bystander.hash==killer.hash)
        {
            killer.onKill(deadfoe);
        }
    }
    @Override
    public void onKill (Character victim)
    {
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
            String New=SetName(newindex, true); 
            if (greater==false)
            System.out.println(old+" Transformed into "+New+"!");
            else
            System.out.println(old+" Transformed (Greater) into "+New+"!");
            this.Cname=New; 
            if (this.transabs[0][0]==null) //transforming for the first time, not counting legion since he's special
            {
                this.transabs[0]=this.abilities;
                Ability[] newabilities=Ability.AssignAbSum(newindex);
                this.abilities=newabilities;
            }
            else //already transformed earlier and now transforming back
            {
                Ability[] temp=this.transabs[0];
                this.transabs[0]=this.abilities;
                this.abilities=temp;
            }
            if (greater==true) //greater transformation is ocurring
            {
                this.maxHP=InHP(newindex, true); 
                this.HP=maxHP;
                this.SHLD=0;
                ArrayList <StatEff> removeme= new ArrayList<StatEff>();
                removeme.addAll(this.effects);        
                for (StatEff eff: removeme)
                {
                    if (!(eff instanceof Tracker))
                    this.remove(eff.hashcode, "normal"); 
                }
                if (this.index==28) //if statement since there are only 2 summons with transform right now
                {
                    CoinFlip.RobotImmunities(this, false); //if arachnaught is transforming into someone else, it loses its immunities
                }
                else if (newindex==28)
                {
                    CoinFlip.RobotImmunities(this, true); //when transforming into an arachnaught, gain its immunities
                }
            }
            this.index=newindex; 
            this.pdesc=Character.MakeDesc(newindex, true);
            CheckSumDupes(this);
        }
        else
        System.out.println(this.Cname+"'s Transformation failed due to an immunity.");
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
    public void onAllyControlled (Character ally, Character controller) //ally is one being controlled
    {
    }
    @Override
    public void onSelfControlled (Character controller)
    {
    }
}