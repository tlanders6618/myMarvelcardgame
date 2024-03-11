package myMarvelcardgamepack;

        
/**
* Designer: Timothy Landers
* Date: 1/12/22
* Filename: Character
* Purpose: Template for all characters.
*/
import java.util.ArrayList; 
import java.util.Scanner;
public abstract class Character 
{
    //base stats
    String Cname="J. Jonah Jameson";    
    int size=1;
    int HP=0; //health
    int maxHP;
    int index;
    int turn=0; //++ at start of turn; which turn they are on
    int DR=0; //damage reduction from hits from sources other than resistance (i.e. passives)
    int PRDR=0; //damage reduction from Resistance Effects
    int RDR=0; //damage reduction from Resistance
    int ADR=0; //damage reduction from all sources
    int BuDR, BlDR, PoDR, ShDR, WiDR=0; //dot damage reduction; burn, bleed, poison, shock, and wither
    int DV=0; //damage vulnerability
    int BD=0; //bonus damage on attacks
    int CC=0; //crit chance
    double critdmg=1.5; //default is crits do +50% dmg
    int SHLD=0; //shield
    int Cchance=0; //extra status chance
    int CritDR=0; //crit resistance
    int CritVul=0; //vulnerable
    double lifesteal=0; //drain
    boolean team1=false; //which team they're on, for determining who's an ally and who's an enemy
    Ability[] abilities = new Ability[5];
    Ability[][] transabs= new Ability[3][5]; //storing abilities of characters they transformed into
    boolean dead=false; 
    boolean summoned=false;
    boolean targetable=true;
    int accuracy=100; //for blind
    int dmgtaken=0;
    int hash; //identifier number
    int passivecount; //for keeping track of passive stuff
    Ability activeability=null; //last used ability     
    Character[] passivefriend= new Character[6]; //for lads like eddie brock venom
    ArrayList<StatEff> effects= new ArrayList<StatEff>(); //holds status effects
    ArrayList<String> immunities= new ArrayList<String>(); 
    ArrayList<String> binaries=new ArrayList<String>(); //overlapping conditions like stunned and invisible
    ArrayList<String> ignores=new ArrayList<String>(); //ignore when attacking
    ArrayList<SpecialAbility> helpers=new ArrayList<SpecialAbility>(); //performs unique misc. functions like redwing
    public Character ()
    {
    }
    public static boolean CheckFor (Character hero, String eff, boolean type)
    {
        if (type==false)
        {
            for (StatEff e: hero.effects)
            {
                if (eff.equalsIgnoreCase(e.getimmunityname()))
                {
                    return true;
                }
            }
        }
        else //check type
        {
            for (StatEff e: hero.effects)
            {
                if (eff.equalsIgnoreCase(e.getefftype()))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void onTurn (Character hero, boolean notbonus) //onturn passives present in both overridden subclasses; this just notifies allies and enemies the hero took their turn
    {
        boolean team=hero.team1;
        Character[] friends=Battle.GetTeammates(hero); 
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyTurn(hero, friend, hero.summoned);
            }
        }
        team=CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
        Character[] foes=Battle.GetTeam(team);
        for (Character foe: foes)
        {
            if (foe!=null&&!(foe.binaries.contains("Banished")))
            {
                foe.onEnemyTurn(hero, foe, hero.summoned);
            }
        }
        if (hero.team1==true) //potentially notify dead friends like Phoenix that it's time to revive
        {
            for (Character deadlad: Battle.team1dead)
            {
                deadlad.onAllyTurn (hero, deadlad, hero.summoned);
            }
        } 
        else
        {
            for (Character deadlad: Battle.team2dead)
            {
                deadlad.onAllyTurn (hero, deadlad, hero.summoned);
            }
        }
    }
    public abstract void onTurnEnd (Character hero, boolean notbonus);
    public Ability ChooseAb (Character hero) //called by turn in battle
    {
        if (hero.team1==true)
        {
            System.out.println ("\nPlayer 1, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        }
        else
        {
            System.out.println ("\nPlayer 2, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        } 
        boolean skip=Card_HashCode.CheckSkip(hero); //allows heroes with no usable abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (hero.abilities[i]!=null)
            {
                System.out.println (a+": "+hero.abilities[i].GetAbName(hero, hero.abilities[i]));  
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
            if (choice==-1&&skip==true) //skipped turn
            {
                good=true;
            }
            else if (choice<5&&choice>=0&&!(hero.abilities[choice]==null)&&hero.abilities[choice].CheckUse(hero, hero.abilities[choice])==false)
            {
                good=false;
                System.out.println("Selected ability is not currently usable. Pick another one.");
            }
            else if (choice<5&&choice>=0&&!(hero.abilities[choice]==null)&&hero.activeability!=null&&hero.activeability.unbound==true&&hero.abilities[choice].unbound==true)
            {
                good=false;
                System.out.println("Unbound abilities may only be used once per turn.");
            }
            else if (choice<5&&choice>=0&&!(hero.abilities[choice]==null)&&hero.abilities[choice].CheckUse(hero, hero.abilities[choice])==true) 
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
            hero.activeability=hero.abilities[choice];
            return hero.abilities[choice];
        }
    }
    public abstract void onAllyTurn (Character ally, Character hero, boolean summoned); //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    public abstract void onEnemyTurn (Character enemy, Character hero, boolean summoned);
    public abstract void onFightStart(Character hero);
    public abstract void onAttack (Character hero, Character victim);
    public abstract void onAttacked(Character attacked, Character attacker, int dmg);
    public abstract void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg);
    public abstract void HPChange (Character hero, int oldhp, int newhp);
    public static void Healed (Character healed, int regener, boolean passive) //passive refers to the "recover up to X missing health" type of healing abs
    {
        boolean nowound=true; int h=healed.HP;
        if (passive==false&&healed.immunities.contains("Heal")) //passive healing isn't considered to be a heal ability
        {
            System.out.println(healed.Cname+" could not be healed due to an immunity!");
            nowound=false;
        }
        else if (healed.binaries.contains("Wounded"))
        {
            System.out.println(healed.Cname+" could not be healed due to being Wounded!");
            nowound=false;
        }
        if (nowound==true&&healed.HP<healed.maxHP&&healed.dead==false)
        {
            regener=healed.GetHealAmount(healed, regener, passive);
            healed.HP+=regener;
            if (passive==false)
            System.out.println("\n"+healed.Cname+" was healed for "+regener+" health!");
            else if (passive==true)
            System.out.println ("\n"+healed.Cname+" regained "+regener+" health!");
            if (healed.HP>healed.maxHP)
            {
                healed.HP=healed.maxHP;
            }
            healed.HPChange(healed, h, healed.HP);
        }
    }
    public static int GetHealAmount (Character hero, int amount, boolean passive)
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Poison"))
            {
                amount-=10;
            }
        }
        if (passive==false) //passive healing is unaffected by recovery
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Recovery"))
                {
                    int inc=eff.power;
                    amount+=inc;
                }
            }
        }
        if (amount<0)
        {
            amount=0;
        }
        return amount;
    }
    public static void CheckDrain (Character hero, Character target, int amount)
    {
        //this is now under the attack methods instead of each attackab and basicab
        if (hero.lifesteal<=0||hero.immunities.contains("Drain")||hero.binaries.contains("Wounded"))
        {
            //can't heal
        }
        else
        {
            int h=hero.HP;
            if (hero.lifesteal==50)
            {
                double dub=amount/2;
                amount=5*(int)Math.ceil(dub/5.0); //drain half, rounded up
            }
            amount=hero.GetHealAmount(hero, amount, false);
            hero.HP+=amount;
            System.out.println("\n"+hero.Cname+" healed themself for "+amount+" health!");
            if (hero.HP>hero.maxHP)
            {
                hero.HP=hero.maxHP;
            }
            hero.HPChange(hero, h, hero.HP);
        }
    }
    public static void Shielded (Character hero, int amount) //hero is gaining shield
    {
        if (hero.binaries.contains("Shattered"))
        {
            System.out.println(hero.Cname+" could not be Shielded due to being Shattered.");
        }
        else if (hero.immunities.contains("Defence")||hero.immunities.contains("Shield"))
        {
            System.out.println(hero.Cname+" could not be Shielded due to an immunity.");
        }
        else if (hero.SHLD>=amount) //nothing happens; shield does not stack
        {
        }
        else 
        {
            hero.SHLD=amount;
            System.out.println("\n"+hero.Cname+" received "+amount+" shield!");
        }
    }
    public abstract void add (Character hero, StatEff eff); //adding a stateff    
    public abstract void remove (Character hero, int removalcode, String nullify); //removes status effects
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
    public abstract Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe); //for passives to change target hero
    public abstract Character Attack (Character dealer, Character target, int dmg, boolean aoe);
    public abstract Character AttackNoDamage (Character dealer, Character target, boolean aoe);
    public abstract void onCrit (Character hero, Character target); //called after successful crit; for passives
    public abstract int TakeDamage (Character target, Character dealer, int dmg, boolean aoe); //takedamage checks hero shield and calls tookdamage
    public abstract int TakeDamage (Character target, int dmg, boolean dot); 
    public abstract void TookDamage (Character hero, Character dealer, int dmg); //triggers relevant passives and checks if hero should be dead
    public abstract void TookDamage (Character hero, boolean dot, int dmg); 
    public void DOTdmg (Character target, int dmg, String type) //target is the one taking the damage
    {
        int knull; //since the take damage methods require return values, this will store them
        if (type.equalsIgnoreCase("bleed"))
        {
            dmg=(dmg-target.ADR-target.BlDR); //factoring in damage resistance
            if (dmg>0)
            {
                System.out.println ("\n"+target.Cname+" took "+dmg+" Bleed damage"); 
                knull=target.TakeDamage(target, dmg, true);
            }
        }
        else if (type.equalsIgnoreCase("burn"))
        {
            dmg=(dmg-target.ADR-target.BuDR);
            if (dmg>0)
            {
                System.out.println ("\n"+target.Cname+" took "+dmg+" Burn damage"); 
                knull=target.TakeDamage(target, dmg, true);
            }
        }
        else if (type.equalsIgnoreCase("poison"))
        {
            dmg=(dmg-target.ADR-target.PoDR);
            if (dmg>0&&!(target.immunities.contains("Lose"))) //poison damage ignores shields
            {
                HP-=dmg;
                System.out.println ("\n"+target.Cname+" took "+dmg+" Poison damage"); 
                target.TookDamage(target, true, dmg);
            }
        }
        else if (type.equalsIgnoreCase("shock"))
        {
            dmg=(dmg-target.ADR-target.ShDR);
            if (dmg>0)
            {
                System.out.println ("\n"+target.Cname+" took "+dmg+" Shock damage"); 
                knull=target.TakeDamage(target, dmg, true);
                Ability.DoRicochetDmg(dmg, target, true);
            }
        }
        else if (type.equalsIgnoreCase("Wither")&&!(target.immunities.contains("Lose")))
        {
            dmg-=(target.ADR+target.WiDR);
            if (dmg>0)
            {
                target.HP-=dmg;
                System.out.println ("\n"+target.Cname+" took "+dmg+" Wither damage");
                target.TookDamage(target, true, dmg);
            }
        }
    }
    public static String GetHP (Character hero)
    {
       return hero.HP+"/"+hero.maxHP;
    } 
    public static String GetShield(Character hero)
    {
        return hero.SHLD+"/0";
    }
    public int InHP (int index, boolean summoned) //Initialises HP
    {
        int health=0;
        if (summoned==false)
        {
            switch (index)
            {
                //case
                //health= 200; break;
            
                case 6: case 9: case 10: case 14: case 19: case 21:
                health= 220; break;
            
                case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 11: case 18: case 20: case 23: case 24:
                health= 230;  break;
            
                case 12: case 13: case 15: case 16: case 17: case 22:
                health= 240; break;
                    
                //Special carrots
                case 26: health=130; break;
            }    
            return health;
        }
        else
        {
            switch (index)
            {
                case 7: 
                health= 5; break;      
            
                case 1: case 10: case 11:
                health= 40;  break; 
            
                case 5: case 9:
                health= 50; break;
            
                case 2: case 3: case 6: case 27:
                health= 60; break;   
                
                case 8:
                health= 70; break;
                
                case 28:
                health= 80; break;
            
                case 4: 
                health= 100; break;
            
                case 12:
                health= 200; break;
            }        
            return health;
        }
    }
    public static String SetName (int index, boolean summoned)
    {
        String name;
        if (summoned==false) //hero names
        {
            switch (index)
            {
                case 1: name= "Moon Knight (Modern)"; break;
                case 2: name= "Gamora (Modern)"; break;
                case 3: name= "Punisher (Classic)"; break;
                case 4: name= "Iron Man (Mark VII)"; break;
                case 5: name= "War Machine (James Rhodes)"; break; 
                case 6: name= "Captain America (Steve Rogers)"; break;
                case 7: name= "Falcon (Classic)"; break;
                case 8: name= "Winter Soldier (Classic)"; break;
                case 9: name= "Star-Lord (Modern)"; break;
                case 10: name="Nick Fury (Classic)"; break;
                case 11: name="Nick Fury (Modern)"; break;
                case 12: name="Drax (Classic)"; break;
                case 13: name="Drax (Modern)"; break;
                case 14: name="X-23 (Modern)"; break;
                case 15: name="Wolverine (Classic)"; break;
                case 16: name="Venom (Eddie Brock)"; break;
                case 17: name="Venom (Mac Gargan)"; break;
                case 18: name="Spider-Man (Peter Parker)"; break;
                case 19: name="Spider-Man (Miles Morales)"; break;
                case 20: name="Spider-Man (Superior)"; break;
                case 21: name="Storm (Modern)"; break;
                case 22: name="Ms. Marvel (Kamala Khan)"; break;
                case 23: name="Captain Marvel (Carol Danvers)"; break;
                case 24: name="Binary (Carol Danvers)"; break;
                default: name= "ERROR. INDEX NUMBER NOT FOUND";
            }    
            return name;
        }
        else //summon names
        {
            switch (index)
            {
                case 1: name="Nick Fury LMD (Summon)"; break;
                case 2: name="AIM Rocket Trooper (Summon)";break;
                case 3: name="AIM Crushbot (Summon)"; break;
                case 4: name="Ultron Drone (Summon)"; break;
                case 5: name="Doombot (Summon)"; break;
                case 6: name="Lesser Demon (Summon)"; break;
                case 7: name="Holographic Decoy (Summon)"; break;
                case 8: name="Ice Golem (Summon)"; break;
                case 9: name="HYDRA Trooper (Summon)"; break;
                case 10: name="Thug (Summon)"; break;
                case 11: name="Mirror Image (Summon)"; break;
                case 12: name="Giganto (Summon)"; break;
                case 27: name="Spiderling (Summon)"; break;
                case 28: name="Arachnaught (Summon)"; break;
                default: name="Error with Summon index";
            }    
            return name;
        }
    }
    public abstract void onLethalDamage (Character hero, Character killer, String dmgtype);
    public abstract void onDeath (Character hero, Character killer, String dmgtype);
    public abstract void onAllyDeath (Character bystander, Character deadfriend, Character killer);
    public abstract void onEnemyDeath (Character bystander, Character deadfoe, Character killer);
    public abstract void onKill (Character killer, Character victim);
    public abstract void onRez (Character hero, Character healer);
    public abstract void Transform (Character hero, int newindex, boolean greater); //new index is the index number of the character being transformed into
    public abstract void onAllySummon (Character summoner, Summon newfriend);
    public abstract void onEnemySummon (Character summoner, Summon newfoe);
    public abstract boolean onAllyControlled (Character hero, Character controlled, Character controller);
    public abstract boolean onEnemyControlled (Character hero, Character controlled, Character controller);
    public abstract boolean onSelfControlled (Character hero, Character controller);
    public static void BanishTick (Character hero)
    {
        ArrayList <StatEff> ban= new ArrayList<StatEff>();
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Banish"))
            {
                ban.add(eff); 
            }
        }
        for (StatEff eff: ban)
        {
            eff.UseBanish();
        }
    }
    public static int GetStatCount (Character hero, String name, String type)
    {
        int num=0;
        for (StatEff eff: hero.effects)
        {
            if ((eff.getimmunityname().equalsIgnoreCase(name)||name.equalsIgnoreCase("any"))&&(eff.getefftype().equalsIgnoreCase(type)||type.equalsIgnoreCase("any")))
            {
                ++num;
            }
        }
        return num;
    }
    public static StatEff GetRandomStatEff (Character hero, String name, String type) //ensure size is appropriate before calling this and whatnot
    {
        int go=hero.GetStatCount (hero, name, type); //gets random stateffs of a specific type, e.g. debuffs
        if (go>0)
        {
            if (name.equalsIgnoreCase("any")&&!(type.equalsIgnoreCase("any")))
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getefftype().equalsIgnoreCase(type)));
                return hero.effects.get(rando); 
            }
            else if (type.equalsIgnoreCase("any")&&!(name.equalsIgnoreCase("any")))
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getimmunityname().equalsIgnoreCase(name)));
                return hero.effects.get(rando); //returns the effect in the array at the index of the given random index number
            }
            else if (type.equalsIgnoreCase("any")&&name.equalsIgnoreCase("any"))
            {
                int rando=hero.effects.size(); 
                rando= (int)(Math.random()*hero.effects.size());
                return hero.effects.get(rando); 
            }
            else
            {
                int rando=hero.effects.size();
                do
                {
                    rando= (int)(Math.random()*hero.effects.size());
                }
                while (!(hero.effects.get(rando).getefftype().equalsIgnoreCase(type))||!(hero.effects.get(rando).getimmunityname().equalsIgnoreCase(name)));
                return hero.effects.get(rando); 
            }
        }
        else
        {
            return null;
        }
    }
}