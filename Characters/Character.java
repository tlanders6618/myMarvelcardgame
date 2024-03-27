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
    int BHP; //temporary bonus health from barrier
    int index;
    int turn=0; //++ at start of turn; which turn they are on
    int DR=0; //damage reduction from hits from sources other than resistance (i.e. passives)
    int PRDR=0; //damage reduction from Resistance Effects
    int RDR=0; //damage reduction from Resistance
    int ADR=0; //damage reduction from all sources
    int BuDR, BlDR, PoDR, ShDR, WiDR=0; //dot damage reduction; burn, bleed, poison, shock, and wither
    int DV=0; //damage vulnerability
    int BD=0; //bonus damage on attacks from status effects
    int PBD=0; //bonus damage on attacks from passives
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
    public abstract void add (StatEff eff); //adding a stateff    
    public abstract void remove (int removalcode, String nullify); //removes status effects
    public boolean CheckFor (String eff, boolean type)
    {
        if (type==false)
        {
            for (StatEff e: this.effects)
            {
                if (eff.equalsIgnoreCase(e.getimmunityname()))
                {
                    return true;
                }
            }
        }
        else //check type
        {
            for (StatEff e: this.effects)
            {
                if (eff.equalsIgnoreCase(e.getefftype()))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void onTurn (boolean ignoreme) //onturn passives present in both overridden subclasses; this just notifies allies and enemies the hero took their turn
    {
        ++this.turn;
        boolean team=this.team1;
        Character[] friends=Battle.GetTeammates(this); 
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyTurn(this, friend, this.summoned);
            }
        }
        team=CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
        Character[] foes=Battle.GetTeam(team);
        for (Character foe: foes)
        {
            if (foe!=null&&!(foe.binaries.contains("Banished")))
            {
                foe.onEnemyTurn(this, foe, this.summoned);
            }
        }
        if (this.team1==true) //potentially notify dead friends like Phoenix that it's time to revive
        {
            for (Character deadlad: Battle.team1dead)
            {
                deadlad.onAllyTurn (this, deadlad, this.summoned);
            }
        } 
        else
        {
            for (Character deadlad: Battle.team2dead)
            {
                deadlad.onAllyTurn (this, deadlad, this.summoned);
            }
        }
    }
    public abstract void onTurnEnd (Character hero, boolean notbonus);
    public Ability ChooseAb () //called by turn in battle
    {
        if (this.team1==true)
        System.out.println ("\nPlayer 1, choose an ability for "+this.Cname+" to use. Type its number, not its name.");
        else
        System.out.println ("\nPlayer 2, choose an ability for "+this.Cname+" to use. Type its number, not its name.");
        boolean skip=Card_HashCode.CheckSkip(this); //allows heroes with no usable abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (this.abilities[i]!=null)
            {
                System.out.println (a+": "+this.abilities[i].GetAbName(this));  
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
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.abilities[choice].CheckUse(this)==false)
            {
                good=false;
                System.out.println("Selected ability is not currently usable. Pick another one.");
            }
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.activeability!=null&&this.activeability.unbound==true&&this.abilities[choice].unbound==true)
            {
                good=false;
                System.out.println("Unbound abilities may only be used once per turn.");
            }
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.abilities[choice].CheckUse(this)==true) 
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
            this.activeability=this.abilities[choice];
            return this.abilities[choice];
        }
    }
    public abstract void HPChange (int oldhp, int newhp);
    public void Healed (int regener, boolean passive) //passive refers to the "recover up to X missing health" type of healing abs
    {
        boolean nowound=true; int h=this.HP;
        if (passive==false&&this.immunities.contains("Heal")) //passive healing isn't considered to be a heal ability
        {
            System.out.println(this.Cname+" could not be healed due to an immunity!");
            nowound=false;
        }
        else if (passive==false&&this.binaries.contains("Wounded"))
        {
            System.out.println(this.Cname+" could not be healed due to being Wounded!");
            nowound=false;
        }
        if (nowound==true&&this.HP<this.maxHP&&this.dead==false)
        {
            regener=this.GetHealAmount(this, regener, passive);
            if (regener+this.HP>this.maxHP) //can't have more health than the maximum amount
            regener=this.maxHP-this.HP;
            this.HP+=regener;
            if (passive==false)
            System.out.println("\n"+this.Cname+" was healed for "+regener+" health!");
            else if (passive==true)
            System.out.println ("\n"+this.Cname+" regained "+regener+" health!");
            this.HPChange(h, this.HP);
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
        //this is now under the attack method instead of each attackab and basicab
        if (hero.lifesteal<=0||target.immunities.contains("Drained")||hero.binaries.contains("Wounded"))
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
            if (hero.HP<hero.maxHP)
            hero.HP+=amount;
            System.out.println("\n"+hero.Cname+" healed themself for "+amount+" health!");
            if (hero.HP>hero.maxHP)
            {
                hero.HP=hero.maxHP;
            }
            hero.HPChange(h, hero.HP);
        }
    }
    public void Shielded (int amount) //hero is gaining shield
    {
        if (this.binaries.contains("Shattered"))
        {
            System.out.println(this.Cname+" could not be Shielded due to being Shattered.");
        }
        else if (this.immunities.contains("Defence")||this.immunities.contains("Shield"))
        {
            System.out.println(this.Cname+" could not be Shielded due to an immunity.");
        }
        else if (this.SHLD>=amount) //nothing happens; shield does not stack
        {
        }
        else 
        {
            this.SHLD=amount;
            System.out.println("\n"+this.Cname+" received "+amount+" shield!");
        }
    }
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
    public abstract void onAllyTurn (Character ally, Character hero, boolean summoned); //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    public abstract void onEnemyTurn (Character enemy, Character hero, boolean summoned);
    public abstract void onFightStart();
    public abstract void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg);
    public abstract Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe); //for passives to change target hero
    public abstract void BeforeAttack (Character dealer, Character target, boolean t); //whether to call before or after checking for protect
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe) //for attack skills
    {
        BeforeAttack(dealer, target, true);
        target=target.onTargeted(dealer, target, dmg, aoe); 
        BeforeAttack(dealer, target, false);
        if (!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))) //only check if dealer isn't immune to miss and hasn't missed already; can't miss twice 
        { 
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); //blind is checked when activating the ab; evade is checked here once the target has been selected
                dealer.activeability.evade=true;
            }
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
        dealer.onAttack(target); //activate relevant passives after attacking
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
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe)
    {
        //modified version of attack method since debuff skills cannot do damage
        BeforeAttack(dealer, target, true);
        target=target.onTargeted(dealer, target, 0, aoe);
        BeforeAttack(dealer, target, false);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); 
                dealer.activeability.evade=true;
            }
        }
        dealer.onAttack(target);
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
    public Character AttackNoDamage (Character dealer, Character target, int lossy, boolean aoe, boolean max) //for (max) health loss attacks
    {
        BeforeAttack(dealer, target, true);
        target=target.onTargeted(dealer, target, 0, aoe);
        BeforeAttack(dealer, target, false);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); //blind is checked when activating the ab; evade is checked here once the target has been selected
                dealer.activeability.evade=true;
            }
        }
        if (!(dealer.binaries.contains("Missed")))
        {
            if (max==true)
            target.LoseMaxHP (dealer, lossy);
            else
            target.LoseHP (dealer, lossy, "knull");
        }
        dealer.onAttack(target);
        if (target.dead==false)
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
    public abstract void onCrit (Character target); //called after successful crit; for passives
    public abstract void onAttack (Character victim);
    public abstract void onAttacked(Character attacked, Character attacker, int dmg);
    public abstract int TakeDamage (Character target, Character dealer, int dmg, boolean aoe); //takedamage checks hero shield and calls tookdamage
    public abstract int TakeDamage (Character target, int dmg, boolean dot); 
    public abstract void TookDamage (Character hero, Character dealer, int dmg); //triggers relevant passives and checks if hero should be dead
    public abstract void TookDamage (Character hero, boolean dot, int dmg); 
    public abstract void onLethalDamage (Character killer, String dmgtype);
    public abstract void onDeath (Character killer, String dmgtype);
    public abstract void onAllyDeath (Character bystander, Character deadfriend, Character killer);
    public abstract void onEnemyDeath (Character bystander, Character deadfoe, Character killer);
    public abstract void onKill (Character victim);
    public abstract void onRez (Character healer); //needs to call hpchange
    public void LoseMaxHP (Character attacker, int lossy)
    {
        if (!(this.immunities.contains("Reduce")))
        {
            System.out.println(this.Cname+"'s max health was reduced by "+lossy+"!");
            this.maxHP-=lossy;
            if (this.maxHP<=0) //ignores immortality and the like; instant and permanent death
            {
                this.maxHP=0;
                this.onDeath(attacker, "Reduce");
            }
            if (this.dead==false)
            {
                this.HPChange(this.HP, this.HP); //for passives based on missing health, which are calculated using the difference between current and max health
            }
        }
        else
        System.out.println(attacker.Cname+"'s max health reduction failed due to"+this.Cname+"'s immunity.");
    }
    public void LoseHP (Character attacker, int lossy, String dot) //modified version of tookdamage
    {
        if (!(this.immunities.contains("Lose")))
        {
            if (dot.equals("Poison"))
            System.out.println(this.Cname+" lost "+lossy+" health from Poison!");
            else if (dot.equals("Wither"))
            System.out.println(this.Cname+" lost "+lossy+" health from Wither!");
            else
            System.out.println(this.Cname+" lost "+lossy+" health!");
            int h=this.HP;
            Damage_Stuff.CheckBarrier(this, attacker, lossy);
            if (this.HP<=0)
            this.HP=0;
            if (this.HP==0&&(dot.equals("Poison")||dot.equals("Wither"))&&!(this.binaries.contains("Immortal")))
            {
                this.onLethalDamage(null, "DOT");
            }
            else if (this.HP==0&&dot.equals("Self")&&!(this.binaries.contains("Immortal")))
            {
                this.onLethalDamage(null, "Lose");
            }
            else if (this.HP==0&&!(this.binaries.contains("Immortal")))
            {
                this.onLethalDamage(attacker, "Lose");
            }
            if (this.dead==false)
            {
                this.HPChange(h, this.HP);
            }
        }
        else
        {
            if (dot.equals("Poison"))
            System.out.println(this.Cname+" lost no health from Poison due to an immunity!");
            else if (dot.equals("Wither"))
            System.out.println(this.Cname+" lost no health from Wither due to an immunity!");
            else
            System.out.println(attacker.Cname+"'s health loss failed due to"+this.Cname+"'s immunity.");
        }
    }
    public void DOTdmg (int dmg, String type)
    {
        int knull; //since the take damage methods require return values, this will store them
        if (type.equalsIgnoreCase("bleed"))
        {
            dmg=(dmg-this.ADR-this.BlDR); //factoring in damage resistance
            if (dmg<=0)
            dmg=0;
            System.out.println ("\n"+this.Cname+" took "+dmg+" Bleed damage"); 
            if (dmg>0)
            {
                knull=this.TakeDamage(this, dmg, true);
            }
        }
        else if (type.equalsIgnoreCase("burn"))
        {
            dmg=(dmg-this.ADR-this.BuDR);
            if (dmg<=0)
            dmg=0;
            System.out.println ("\n"+this.Cname+" took "+dmg+" Burn damage"); 
            if (dmg>0)
            {
                knull=this.TakeDamage(this, dmg, true);
            }
        }
        else if (type.equalsIgnoreCase("poison"))
        {
            dmg=(dmg-this.PoDR);
            if (dmg<=0)
            dmg=0;
            this.LoseHP(null, dmg, "Poison");
        }
        else if (type.equalsIgnoreCase("shock"))
        {
            dmg=(dmg-this.ADR-this.ShDR);
            if (dmg<=0)
            dmg=0;
            System.out.println ("\n"+this.Cname+" took "+dmg+" Shock damage"); 
            if (dmg>0)
            {
                knull=this.TakeDamage(this, dmg, true);
                Ability.DoRicochetDmg(dmg, this, true, null);
            }
        }
        else if (type.equalsIgnoreCase("Wither"))
        {
            dmg-=this.WiDR;
            if (dmg<=0)
            dmg=0;
            this.LoseHP(null, dmg, "Wither");
        }
    }
    public static String GetHP (Character hero)
    {
       if (hero.BHP>0)
       return hero.BHP+"+"+hero.HP+"/"+hero.maxHP;
       else
       return hero.HP+"/"+hero.maxHP;
    } 
    public static String GetShield(Character hero)
    {
        return hero.SHLD+"/0";
    }
    public int InHP (int index, boolean summoned) //Initialises HP
    {
        if (summoned==false)
        {
            switch (index)
            {
                //case
                //return 200;
            
                case 6: case 9: case 10: case 14: case 19: case 21: case 29: case 33:
                return 220;
            
                case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 11: case 18: case 20: case 23: case 24: case 25: case 34:
                return 230;
            
                case 12: case 13: case 15: case 16: case 17: case 22: case 27: case 28: case 30: case 32: case 35:
                return 240;
                
                //Special carrots
                case 26: return 130;
                case 31: return 250;
            }    
            return 616;
        }
        else
        {
            switch (index)
            {
                case 7: 
                return 5;    
            
                case 1: case 10: case 11:
                return 40;
            
                case 5: case 9:
                return 50;
            
                case 2: case 3: case 6: case 27:
                return 60;
                
                case 8:
                return 70;
                
                case 4: case 28:
                return 80;
            
                case 12:
                return 200;
            }   
            return 616;
        }
    }
    public static String SetName (int index, boolean summoned)
    {
        if (summoned==false) //hero names
        {
            switch (index)
            {
                case 1: return "Moon Knight (Modern)"; 
                case 2: return "Gamora (Modern)"; 
                case 3: return "Punisher (Classic)"; 
                case 4: return "Iron Man (Mark VII)"; 
                case 5: return "War Machine (James Rhodes)"; 
                case 6: return "Captain America (Steve Rogers)"; 
                case 7: return "Falcon (Classic)"; 
                case 8: return "Winter Soldier (Classic)"; 
                case 9: return "Star-Lord (Modern)"; 
                case 10: return "Nick Fury (Classic)"; 
                case 11: return "Nick Fury (Modern)"; 
                case 12: return "Drax (Classic)"; 
                case 13: return "Drax (Modern)"; 
                case 14: return "X-23 (Modern)"; 
                case 15: return "Wolverine (Classic)"; 
                case 16: return "Venom (Eddie Brock)"; 
                case 17: return "Venom (Mac Gargan)"; 
                case 18: return "Spider-Man (Peter Parker)"; 
                case 19: return "Spider-Man (Miles Morales)"; 
                case 20: return "Spider-Man (Superior)"; 
                case 21: return "Storm (Modern)"; 
                case 22: return "Ms. Marvel (Kamala Khan)"; 
                case 23: return "Captain Marvel (Carol Danvers)"; 
                case 24: return "Binary (Carol Danvers)"; 
                case 25: return "Venom (Flash Thompson)"; 
                case 26: return "MODOK (Classic)"; 
                case 27: return "Ultron (Classic)"; 
                case 28: return "Dr. Doom (Classic)";
                case 29: return "Dr. Strange (Classic)";
                case 30: return "Amadeus Cho (Brawn)";
                case 31: return "Hulk (Classic)";
                case 32: return "Black Bolt (Classic)";
                case 33: return "Deadpool (Classic)";
                case 34: return "Red Skull (Classic)";
                case 35: return "Juggernaut (Classic)";
            }    
            return "ERROR. INDEX NUMBER NOT FOUND";
        }
        else //summon names
        {
            switch (index)
            {
                case 1: return "Nick Fury LMD (Summon)"; 
                case 2: return "AIM Rocket Trooper (Summon)";
                case 3: return "AIM Crushbot (Summon)"; 
                case 4: return "Ultron Drone (Summon)"; 
                case 5: return "Doombot (Summon)"; 
                case 6: return "Lesser Demon (Summon)"; 
                case 7: return "Holographic Decoy (Summon)"; 
                case 8: return "Ice Golem (Summon)"; 
                case 9: return "HYDRA Trooper (Summon)"; 
                case 10: return "Thug (Summon)"; 
                case 11: return "Mirror Image (Summon)"; 
                case 12: return "Giganto (Summon)"; 
                case 27: return "Spiderling (Summon)"; 
                case 28: return "Arachnaught (Summon)"; 
            }    
            return "Error with Summon index";
        }
    }
    public abstract void Transform (int newindex, boolean greater); //new index is the index number of the character being transformed into
    public abstract void onAllySummon (Character summoner, Summon newfriend);
    public abstract void onEnemySummon (Character summoner, Summon newfoe);
    public abstract boolean onAllyControlled (Character hero, Character controlled, Character controller);
    public abstract boolean onSelfControlled (Character hero, Character controller);
    public void BanishTick ()
    {
        ArrayList <StatEff> ban= new ArrayList<StatEff>();
        for (StatEff eff: this.effects)
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
}