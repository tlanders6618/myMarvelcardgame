package myMarvelcardgamepack;

        
/**
* Designer: Timothy Landers
* Date: 17/7/22
* Filename: Character
* Purpose: Sets a character's base stats and abilities.
*/
import java.util.ArrayList; 
import java.util.Scanner;
public class Character 
{
    //base stats
    String Cname="J. Jonah Jameson";    
    int size=1;
    int HP=0; //health
    int maxHP;
    int index;
    int turn=0; //++ at start of turn; which turn they are on
    int DR=0; //damage reduction from hits
    int RDR=0; //resistance damage reduction
    int ADR=0; //damage reduction from all sources
    int BuDR, BlDR, PoDR, ShDR=0; //dot damage reduction
    int DV=0; //damage vulnerability
    int BD=0; //bonus damage on attacks
    int CC=0; //crit chance
    double critdmg=1.5; //default is crits do +50% dmg
    int SHLD=0; //shield
    int Cchance=0; //extra status chance
    int CritDR=0; //crit resistance
    int CritVul=0; //vulnerable
    double lifesteal=0; //drain
    int Torder; //character's place in the turn order
    boolean team1; //which team they're on, for determining who's an ally and who's an enemy
    Ability[] abilities = new Ability[5];
    boolean dead=false; 
    boolean targetable=true;
    int accuracy=100; //for blind
    int dmgtaken=0;
    int hash; //identifier code
    int passivecount; //counter for passives like Nitro's and Fury Jr's
    Ability[] activeability= new Ability[1]; //last used ability     
    Character[] passivefriend= new Character[1]; //for lads like venom
    ArrayList<StatEff> effects= new ArrayList<StatEff>(); //holds status effects
    ArrayList<String> immunities= new ArrayList<String>(); 
    ArrayList<String> binaries=new ArrayList<String>(); //overlapping conditions like stunned and invisible
    ArrayList<String> ignores=new ArrayList<String>(); //ignore when attacking
    ArrayList<SpecialAbility> helpers=new ArrayList<SpecialAbility>();
    public Character ()
    {
    }
    public Character (int Pindex)
    {
        index=Pindex; //character's semi-unique identifier number                
        //set identifiers
        HP=InHP (index, HP);
        maxHP=HP;
        Cname=SetName(index);
        hash=Card_HashCode.RandomCode();                
        //set abilities
        activeability[0]=null;
        abilities=Ability.AssignAb(index);     
    }
    public boolean CheckFor (Character hero, String eff)
    {
        boolean h=false;
        for (StatEff effect: hero.effects)
        {
            if (eff.equalsIgnoreCase(effect.getimmunityname()))
            {
                h=true;
                break;
            }
        }
        return h;
    }
    public void onTurn (Character hero)
    {
        boolean go=true;
        switch (hero.index)
        {
            case 5: StaticPassive.WM(hero, hero.turn); break;
            case 6: ActivePassive.CaptainA(hero); break;
            case 9: ActivePassive.StarLord(hero); go=false; break;
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
                    friend.onAllyTurn(hero, false);
                }
            }
            team=Damage_Stuff.TeamFlip(team); //reverse team affiliation to get enemies
            Character[] foes=Battle.GetTeammates(hero, team);
            for (Character foe: foes)
            {
                if (foe!=null&&foe.CheckFor(foe, "Banish")==false)
                {
                    foe.onEnemyTurn(hero, false);
                }
            }
            if (hero.team1==true) //potentially notify dead friends like Phoenix that it's time to revive
            {
                for (Character deadlad: Battle.team1dead)
                {
                    deadlad.onAllyTurn (hero, false);
                }
            }
            else
            {
                for (Character deadlad: Battle.team2dead)
                {
                    deadlad.onAllyTurn (hero, false);
                }
            }
        }
    }
    public Ability ChooseAb (Character hero)
    {
        if (hero.team1==true)
        {
            System.out.println ("\nPlayer 1, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        } 
        boolean skip=true; //allows heroes with no available abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (hero.abilities[i]!=null)
            {
                System.out.println (a+": "+hero.abilities[i].GetAbName(hero, hero.abilities[i])); 
                if (hero.abilities[i] instanceof AttackAb&&hero.abilities[i].CheckUse(hero, hero.abilities[i])==true)
                {
                    skip=false; //this prevents heroes from bypassing provoke, taunt, etc for balancing reasons; if they can attack the target, they must 
                }
            }
        }
        if (skip==true)
        {
            System.out.println ("0: Skip turn");
        }
        boolean good=false; int choice;
        do
        {
            choice=Damage_Stuff.GetInput(); 
            --choice; //to get the index number since the number entered was the ability number
            good=false;
            if (choice==-1&&skip==true)
            {
                good=true;
            }
            if (choice<5&&choice>=0&&!(hero.abilities[choice]==null)&&hero.abilities[choice].CheckUse(hero, hero.abilities[choice])==true) //needs to be a usable ability
            {
                good=true;
            }
        }
        while (good==false);
        if (choice==-1)
        {
            return null;
        }
        else
        {
            hero.activeability[0]=hero.abilities[choice];
            return hero.abilities[choice];
        }
    }
    public void onAllyTurn (Character ally, boolean summoned) //true if teammate is a summon
    {
    }
    public void onEnemyTurn (Character enemy, boolean summoned)
    {
    }
    public void onFightStart(Character hero)
    {
        switch (hero.index)
        {
            case 7: StaticPassive.Falcon(hero); break;
            case 8: StaticPassive.Bucky(hero); break;
        }
    }
    public void onAttack (Character hero)
    {
        //triggered after a hero attacks
    }
    public void onAttacked(Character attacked, Character attacker, int dmg)
    {
        if (attacked.dead==false&&!(attacked.binaries.contains("Stunned"))&&!(attacker.ignores.contains("Counter")))
        {
            for (StatEff eff: attacked.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Counter"))
                {
                    eff.UseCounter(attacked, attacker); //counter attack must trigger the took damage method for the attacker, with boolean true
                    attacked.remove(attacked, eff.getcode());
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
    public void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg)
    {
        switch (ally.index)
        {
            case 1: ActivePassive.MoonKnight(ally, hurtfriend, attacker); break;
        }
    }
    public void Healed (Character healed, int regener) //character is being healed
    {
        boolean nowound=true;
        if (healed.CheckFor(healed, "Wound")==true||healed.immunities.contains("Heal"))
        {
            nowound=false;
        }
        if (nowound==true&&healed.HP<healed.maxHP)
        {
            regener=healed.GetHealAmount(healed, regener);
            healed.HP+=regener;
            System.out.println("\n"+healed.Cname+" was healed for "+regener+" health!");
            if (healed.HP>healed.maxHP)
            {
                healed.HP=healed.maxHP;
            }
        }
    }
    public int GetHealAmount (Character hero, int amount)
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Poison"))
            {
                amount-=10;
            }
        }
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Recovery"))
            {
                int inc=eff.getpower();
                amount+=inc;
            }
        }
        if (amount<0)
        {
            amount=0;
        }
        return amount;
    }
    public void PassiveHealed (Character healed, int regener) //passive healing, unaffected by Recovery
    {
        boolean nowound=true; 
        if (healed.CheckFor(healed, "Wound")==true)
        {
            nowound=false;
        }
        if (nowound==true&&healed.HP<healed.maxHP)
        {
            for (StatEff eff: healed.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Poison"))
                {
                    regener-=10;
                }
            }
            healed.HP+=regener;
            System.out.println ("\n"+healed.Cname+" regained "+regener+" health!");
            if (healed.HP>healed.maxHP)
            {
                healed.HP=healed.maxHP;
            }
        }
    }
    public void CheckDrain (Character hero, int amount)
    {
        //this is now under the attack and attack ig defence functions instead of each attackab and basicab
        if (hero.lifesteal<=0||hero.CheckFor(hero, "Wound")==true)
        {
            //can't heal
        }
        else
        {
            if (hero.lifesteal==50)
            {
                double dub=amount/2;
                amount=5*(int)Math.ceil(dub/5.0); //drain half, rounded up
            }
            amount=hero.GetHealAmount(hero, amount);
            hero.HP+=amount;
            System.out.println("\n"+hero.Cname+" healed themself for "+amount+" health!");
            if (hero.HP>hero.maxHP)
            {
                hero.HP=hero.maxHP;
            }
        }
    }
    public void Shielded (Character hero, int amount) //hero is gaining sheidl
    {
        if ((hero.CheckFor(hero, "Shatter")==true)||hero.immunities.contains("Defence")||hero.SHLD>=amount)
        {
            //nothing happens; shield does not stack
        }
        else 
        {
            hero.SHLD=amount;
            System.out.println("\n"+hero.Cname+" received "+amount+" shield!");
        }
    }
    public void add (Character hero, StatEff eff) //adding a stateff
    {
        if (!(hero.immunities.contains(eff.getimmunityname()))&&!(hero.immunities.contains(eff.getefftype())))
        {
            hero.effects.add(eff);
            eff.onApply(hero);
            switch (eff.getefftype())
            {
                case "Buffs": hero.onBuffed(hero, eff, true); break;
                case "Heal": hero.onHealEffed(hero, eff, true); break;
                case "Debuffs": hero.onDebuffed(hero, eff, true); break;
                case "Defence": hero.onDefEffed(hero, eff, true); break;
                case "Other": hero.onOtherEffed(hero, eff, true); break;
                default: System.out.println ("Couldn't activate on-apply passive effects due to spelling error");
            }
            if (!(eff.getimmunityname().equalsIgnoreCase("Protect"))) //testing taunt/protect interaction; no point in announcing it being added if it's instantly removed
            {
                System.out.println ("\n"+hero.Cname+" gained "+eff.geteffname());
            }
        }
    }
    public void remove (Character hero, int removalcode) //removes status effects
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.getcode()==removalcode)
            {
                hero.effects.remove(eff);
                eff.Nullified(hero);
                //System.out.println ("\n"+eff.geteffname()+" successfully removed."); //test only
                switch (eff.getefftype())
                {
                    case "Buffs": hero.onBuffed(hero, eff, false); break;
                    case "Heal": hero.onHealEffed(hero, eff, false); break;
                    case "Debuffs": hero.onDebuffed(hero, eff, false); break;
                    case "Defence": hero.onDefEffed(hero, eff, false); break;
                    case "Other": hero.onOtherEffed(hero, eff, false); break;
                    default: System.out.println ("Couldn't activate on-apply passive effects due to spelling error");
                }
                break;
            }
        }
    }
    public void removeB (Character hero, String name)
    {
        for (String binary: hero.binaries)
        {
            if (binary.equalsIgnoreCase(name))
            {
                hero.binaries.remove(name);
                //System.out.println (binary+" status successfully removed."); //test only
                break;
            }
        }
    }
    public Character onTargeted (Character attacker, Character target)
    {
        Character ntarg=target;
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
        //switch goes down here, after target sorting
        if (ntarg!=null&&ntarg.CheckFor(ntarg, "Banish")==false)
        {
            return ntarg;
        }
        else
        {
            return target;
        }
    }
    public Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe)
    {
        Character ntarget=target;
        //do spidey, thing, jean switch here        
        return ntarget;
    }
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe)
    {
        //for normal damage from attack skills
        if (target.team1==true&&aoe==false)
        {
            for (Character friend: Battle.team1)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, dealer, target, dmg, aoe);
                }
            }
        }
        else if (target.team1==false&&aoe==false)
        {
            for (Character friend: Battle.team2)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, dealer, target, dmg, aoe);
                }
            }
        }
        if (aoe==false&&!(dealer.ignores.contains("Protect")))
        {
            target=target.onTargeted(dealer, target);
        }
        //check for blind
        boolean evade=false; boolean nomiss=true;
        if (dealer.CheckFor(dealer,"Blind")==true)
        {
            if (!(dealer.ignores.contains("Blind")))
            {
                nomiss=Card_CoinFlip.Flip(dealer.accuracy); //false if the attack missed
            }
        }
        if (nomiss==false)
        {
            System.out.println ("\n"+dealer.Cname+"'s attack missed.");
            dmg=0;
        }
        //check evasion
        if (!(dealer.ignores.contains("Evade"))&&(target.CheckFor(target, "Shatter")==false)&&!(target.binaries.contains("Stunned"))&&nomiss==true)
        { //conditions that would prevent evade from triggering
            for (StatEff effect: target.effects)
            {
                if (effect.getimmunityname().equalsIgnoreCase("Evade"))
                {
                    if (effect.getefftype().equalsIgnoreCase("Defence")&&!(dealer.ignores.contains("Defence"))) //evade is a defence effect
                    {
                        target.remove(target, effect.getcode());
                        evade=true;
                        dmg=0;
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    }
                    else if (effect.getefftype().equalsIgnoreCase("Other")) //evade Effects cannot be stopped
                    {
                        target.remove(target, effect.getcode());
                        evade=true;
                        dmg=0;
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    }
                }
                else if (effect.getimmunityname().equalsIgnoreCase("Evasion"))
                {
                    evade=Card_CoinFlip.Flip((50+target.Cchance));
                    if (evade==true)
                    {
                        dmg=0;
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    } //else nothing
                }
            }
        }
        //check guard
        if (evade==false&&nomiss==true)
        {
            if (dmg>0&&!(dealer.ignores.contains("Guard"))&&!(target.binaries.contains("Stunned"))&&!(dealer.ignores.contains("Defence")))
            { //conditions that would prevent guard from triggering
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Guard"))
                    {
                        int odmg=dmg;
                        dmg=eff.UseGuard(dmg);
                        System.out.println ("\n"+target.Cname+"'s Guard reduced" +dealer.Cname+"'s attack damage by "+(odmg-dmg));
                        if (dmg<=0)
                        {
                            dmg=0;
                            break; //no point in wasting guards on nothing
                        }
                    }
                }
            }
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
            for (SpecialAbility h: target.helpers) //for special things like redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
            if (dealer.ignores.contains("Shield")||dealer.ignores.contains("Defence"))
            {
                dmg=target.TakeDamageIgShield(target, dealer, dmg, aoe);
            }
            else
            {
                dmg=target.TakeDamage(target, dealer, dmg, aoe);
            }
            dealer.activeability[0].ReturnDamage(dmg); //tells the ability how much dmg the attack did
            if (target.dead==false)
            {
                target.onAttacked(target, dealer, dmg);
            }
            else
            {
                Character[] friends=Battle.GetTeammates(target, target.team1);
                for (Character friend: friends)
                {
                    if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                    {
                        friend.onAllyAttacked(friend, target, dealer, dmg);
                    }
                }
            }
            if (dealer.dead==false)
            {
                dealer.CheckDrain(dealer, dmg);
            }
        }
        return target;
    }
    public Character AttackIgDefence (Character dealer, Character target, int dmg, boolean aoe)
    {
        boolean noevade=true; 
        if (aoe==false)
        {
            target.onTargeted(dealer, target);
        }
        //evade and guard Effects are technically not counted as defence 
        if (!(dealer.ignores.contains("Evade"))&&(target.CheckFor(target, "Shatter")==false)&&!(target.binaries.contains("Stunned")))
        {
            for (StatEff effect: target.effects)
            {
                if (effect.getimmunityname().equalsIgnoreCase("Evade")&&effect.getefftype().equalsIgnoreCase("Other"))
                {
                    target.remove(target, effect.getcode());
                    noevade=false;
                    dmg=0;
                    System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                    target.onAttacked(target, dealer, dmg);
                    break;
                }
            }
        }
        if (noevade==true)
        {
            if (!(dealer.ignores.contains("Guard"))&&!(target.binaries.contains("Stunned")))
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Guard")&&eff.getefftype().equalsIgnoreCase("Other"))
                    {
                        int odmg=dmg;
                        dmg=eff.UseGuard(dmg);
                        System.out.println ("\n"+target.Cname+"'s Guard reduced" +dealer.Cname+"'s attack damage by "+(odmg-dmg));
                        if (dmg<=0)
                        {
                            break; //no point in wasting guards on nothing
                        }
                    }
                }
            }
            dmg=Damage_Stuff.DamageDecrease(true, target, dmg);
            dmg=target.TakeDamageIgShield(target, dealer, dmg, aoe);
            dealer.activeability[0].ReturnDamage(dmg); //tells the ability how much dmg the attack did
            if (target.dead==false)
            {
                target.onAttacked(target, dealer, dmg);
            }
            else
            {
                Character[] friends=Battle.GetTeammates(target, target.team1);
                for (Character friend: friends)
                {
                    if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                    {
                        friend.onAllyAttacked(friend, target, dealer, dmg);
                    }
                }
            }
            if (dealer.dead==false)
            {
                dealer.CheckDrain(dealer, dmg);
            }
        }        
        return target;
    }
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe)
    {
        //modified version of attack method since debuff skills do no damage
        if (target.team1==true)
        {
            for (Character friend: Battle.team1)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, dealer, target, 0, aoe);
                }
            }
        }
        else
        {
            for (Character friend: Battle.team2)
            {
                if (friend!=null&&friend.CheckFor(friend, "Banish")==false)
                {
                    target=friend.onAllyTargeted(friend, dealer, target, 0, aoe);
                }
            }
        }
        if (aoe==false&&!(dealer.ignores.contains("Protect")))
        {
            target=target.onTargeted(dealer, target);
        }
        //check for blind
        boolean evade=false; boolean nomiss=true;
        if ((dealer.CheckFor(dealer,"Blind")==true))
        {
            if (!(dealer.ignores.contains("Blind")))
            {
                nomiss=Card_CoinFlip.Flip(dealer.accuracy); //false if the attack missed
            }
        }
        if (nomiss==false)
        {
            System.out.println (dealer.Cname+"'s ability missed.");
        }
        //check evasion
        if (!(dealer.ignores.contains("Evade"))&&(target.CheckFor(target, "Shatter")==false)&&!(target.binaries.contains("Stunned"))&&nomiss==true)
        { //conditions that would prevent evade from triggering
            for (StatEff effect: target.effects)
            {
                if (effect.getimmunityname().equalsIgnoreCase("Evade"))
                {
                    if (effect.getefftype().equalsIgnoreCase("Defence")&&!(dealer.ignores.contains("Defence"))) //evade is a defence effect
                    {
                        target.remove(target, effect.getcode());
                        evade=true;
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    }
                    else if (effect.getefftype().equalsIgnoreCase("Other")) //evade Effects cannot be stopped
                    {
                        target.remove(target, effect.getcode());
                        evade=true;
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    }
                }
                else if (effect.getimmunityname().equalsIgnoreCase("Evasion"))
                {
                    evade=Card_CoinFlip.Flip((50+target.Cchance));
                    if (evade==true)
                    {
                        System.out.println ("\n"+target.Cname+" Evaded "+dealer.Cname+"'s attack!");
                        break;
                    } //else nothing
                }
            }
        }
        target.onAttacked(target, dealer, 0);
        return target;
    }
    public int TakeDamage (Character target, Character dealer, int dmg, boolean aoe) //true for aoe, false for single target
    {
        //SWITCH STATEMENT FOR LUKE CAGE GOES HERE
        //this is the default method for taking damage
        //this checks if shield is strong enough to prevent health damage from an enemy attack
        if (SHLD>=dmg&&dmg>0) 
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
    public int TakeDamage (Character target, int dmg, boolean dot) //true for dot and false for all other types of dmg
    {
        //and here
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
    public int TakeDamageIgShield (Character target, Character dealer, int dmg, boolean aoe)
    {
        //HERE TOO
        target.HP-=dmg; 
        target.TookDamage(target, dealer, dmg); 
        return dmg;
    }
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
    public boolean TakeRicochetDamage (Character target, int dmg)
    {
        boolean evade=false;
        if (target.CheckFor(target, "Stun")==false&&(target.CheckFor(target, "Shatter")==false)) //things that would allow the damage to ignore evade
        {
            for (StatEff effect: target.effects) 
            {
                if (effect.getimmunityname().equalsIgnoreCase("Evade"))
                {
                    target.remove(target, effect.getcode()); //evade is consumed and no damage is dealt
                    evade=true;
                    System.out.println ("\n"+target.Cname+" Evaded "+dmg+" Ricochet damage");
                    break;
                }
                else if (effect.getimmunityname().equalsIgnoreCase("Evasion"))
                {
                    evade=Card_CoinFlip.Flip((50+target.Cchance));
                    if (evade==true)
                    {
                        System.out.println ("\n"+target.Cname+" Evaded "+dmg+" Ricochet damage");
                        break;
                    } //else nothing
                }
            }
        }
        if (evade==false)
        {
            if (target.CheckFor(target, "Stun")==false)
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Guard"))
                    {
                        int odmg=dmg;
                        dmg=eff.UseGuard(dmg);
                        System.out.println ("\n"+target.Cname+"'s Guard reduced Ricochet damage by "+(odmg-dmg));
                        if (dmg<=0)
                        {
                            break; //no point in wasting guards on nothing
                        }
                    }
                }
            }
            dmg=(dmg+target.DV)-(target.ADR+target.RDR+target.DR);
            if (dmg<0)
            {
                dmg=0;
            }
            System.out.println ("\n"+target.Cname+" took "+dmg+" Ricochet damage"); 
            target.TakeDamage(target, dmg, false);
        }
        return evade;
    }
    public void DOTdmg (Character target, int dmg, String type) //target is the one taking the damage
    {
        int knull; //since the methods require return values, this will store them
        if (type.equalsIgnoreCase("bleed"))
        {
            dmg=(dmg-target.ADR-target.BlDR); //factoring in damage resistance
            System.out.println ("\n"+target.Cname+" took "+dmg+" Bleed damage"); 
            knull=target.TakeDamage(target, dmg, true);
        }
        else if (type.equalsIgnoreCase("burn"))
        {
            dmg=(dmg-target.ADR-target.BuDR);
            System.out.println ("\n"+target.Cname+" took "+dmg+" Burn damage"); 
            knull=target.TakeDamage(target, dmg, true);
        }
        else if (type.equalsIgnoreCase("poison"))
        {
            dmg=(dmg-target.ADR-target.PoDR);
            if(dmg>0) //poison damage ignores shields
            {
                HP=HP-dmg;
                System.out.println ("\n"+target.Cname+" took "+dmg+" Poison damage"); 
                target.TookDamage(target, true, dmg);
            }
        }
        else if (type.equalsIgnoreCase("shock"))
        {
            dmg=(dmg-target.ADR-target.ShDR);
            System.out.println ("\n"+target.Cname+" took "+dmg+" Shock damage"); 
            Ability.DoRicochetDmg(dmg, target, true);
            knull=target.TakeDamage(target, dmg, true);
        }
        else if (type.equalsIgnoreCase("Wither"))
        {
            dmg-=target.ADR;
            if (dmg>0)
            {
                target.HP-=dmg;
                System.out.println ("\n"+target.Cname+" took "+dmg+" Wither damage"); 
            }
            target.TookDamage(target, true, dmg);
        }
    }
    public String GetHP (Character hero)
    {
       return hero.HP+"/"+hero.maxHP;
    } 
    public String GetShield(Character hero)
    {
        return hero.SHLD+"/0";
    }
    public static int InHP (int index, int health)
    {
        //Initialises HP
        switch (index)
        {
            //case
            //health= 200; break;
                
            case 6: case 7: case 9: case 10:
            health= 220; break;
            
            case 1: case 2: case 3: case 5: case 4: case 8: case 11:
            health= 230;  break;
            
            case 12: case 13:
            health= 240; break;
                
            //Special carrots
            case 26: health=130; break;
        }    
        return health;
    }   
    public static String SetName (int index)
    {
        String name;
        switch (index)
        {
            case 1: name= "Moon Knight (Classic)"; break;
            case 2: name= "Gamora (Battle Armour)"; break;
            case 3: name= "Punisher (Classic)"; break;
            case 4: name= "Iron Man (Mark VII)"; break;
            case 5: name= "War Machine (James Rhodes)"; break; 
            case 6: name= "Captain America (Steve Rogers)"; break;
            case 7: name= "Falcon (Classic)"; break;
            case 8: name= "Winter Soldier (Classic)"; break;
            case 9: name= "Star-Lord (Modern)"; break;
            default: name= "ERROR. INDEX NUMBER NOT FOUND";
        }    
        return name;
    }
    public void onLethalDamage (Character hero, Character killer, String dmgtype)
    {
        hero.onDeath(hero, killer, dmgtype);
    }
    public void onLethalDamage (Character hero, boolean killer, String dmgtype)
    {
        hero.onDeath(hero, null, dmgtype);
    }
    public void onDeath (Character hero, Character killer, String dmgtype)
    {
        switch (hero.index)
        {
            case 5:     
            if (hero.passivefriend[0]!=null)
            {
                hero.passivefriend[0].remove(passivefriend[0], passivecount); //remove war machine's heat signature detection
            }
            break;            
        }
        hero.HP=0;
        hero.dead=true;
        hero.dmgtaken=0;
        ArrayList <StatEff> removeme= new ArrayList<StatEff>();
        removeme.addAll(hero.effects);       
        for (StatEff eff: removeme)
        {
            hero.remove(hero, eff.getcode()); 
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
        boolean foe=Damage_Stuff.TeamFlip(hero.team1); //to get their enemies
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
    public void onAllyDeath (Character bystander, Character deadfriend, Character killer)
    {
        if (killer!=null)
        {
        }
    }
    public void onEnemyDeath (Character bystander, Character deadfoe, Character killer)
    {
        if (killer!=null&&bystander.hash==killer.hash)
        {
            killer.onKill(killer, deadfoe);
        }
    }
    public void onKill (Character killer, Character victim)
    {
        //switch
    } 
    public void Transform (Character hero, int newindex, boolean greater) //new index is the index number of the character being transformed into
    {
        Ability[] newabilities = new Ability[5];
        newabilities=Ability.AssignAb(newindex);
        hero.abilities=newabilities;
        hero.Cname=SetName(newindex); 
        hero.index=newindex; 
        hero.dmgtaken=0;
        if (greater==true) //greater transformation is ocurring
        {
            hero.maxHP=InHP(newindex, HP);
            hero.HP=maxHP;
            ArrayList <StatEff> removeme= new ArrayList<StatEff>();
            removeme.addAll(hero.effects);        
            for (StatEff eff: removeme)
            {
                hero.remove(hero, eff.getcode()); 
            }
        }
    }
    public void onAllySummon (Character summoner, Summon newfriend)
    {
    }
    public void onEnemySummon (Character summoner, Summon newfoe)
    {
    }
    public void onBuffed(Character hero, StatEff buff, boolean add)
    {
        switch (hero.index)
        {
            case 2: ActivePassive.Gamora(hero, buff, add); break;
        } 
    }
    public void onDebuffed (Character hero, StatEff debuff, boolean add) //true if the stat is being added and false for removal
    {
    }
    public void onHealEffed (Character hero, StatEff heal, boolean add)
    {
    }
    public void onDefEffed (Character hero, StatEff def, boolean add)
    {
    }
    public void onOtherEffed (Character hero, StatEff otr, boolean add)
    {
    }
    public boolean onAllyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        return ok;
    }
    public boolean onEnemyControlled (Character hero, Character controlled, Character controller)
    {
        boolean ok=true;
        return ok;
    }
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
    public void BanishTick (Character hero)
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Banish"))
            {
                eff.UseBanish();
            }
        }
    }
    public int GetStatCount (Character hero, String name, String type)
    {
        int num=0;
        for (StatEff eff: hero.effects)
        {
            if (eff.geteffname().equals(name)&&eff.getefftype().equals(type))
            {
                ++num;
            }
        }
        return num;
    }
    public StatEff GetRandomStatEff (Character hero, String name, String type) //ensure size is appropriate before calling this and whatnot
    {
        int go=hero.GetStatCount (hero, name, type);
        if (go>0)
        {
            int rando=hero.effects.size();
            do
            {
                rando= (int)(Math.random()*hero.effects.size());
            }
            while (!(hero.effects.get(rando).getefftype().equalsIgnoreCase(type))||!(hero.effects.get(rando).geteffname().equalsIgnoreCase(name));
            return hero.effects.get(rando); //returns the effect in the array at the index of the given random index number
        }
        else
        {
            return null;
        }
    }
}
