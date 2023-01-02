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
    int DR=0; //damage reduction from hits from passives
    int RDR=0; //resistance damage reduction
    int ADR=0; //damage reduction from all sources
    int BuDR, BlDR, PoDR, ShDR=0; //dot damage reduction
    int DV=0; //damage vulnerability
    int BD=0; //bonus damage on attacks
    int PBD=0; //bonus damage from passives
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
    boolean summoned=false;
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
    public static boolean CheckFor (Character hero, String eff)
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
    public abstract void onTurn (Character hero, boolean notbonus);
    public abstract void OnTurnEnd (Character hero, boolean notbonus);
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
        boolean skip=true; //allows heroes with no usable abilities to skip their turn
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
    public abstract void onAllyTurn (Character ally, Character hero, boolean summoned); //ally is the one triggering call and hero is one reacting; true if teammate is a summon
    public abstract void onEnemyTurn (Character enemy, Character hero, boolean summoned);
    public abstract void onFightStart(Character hero);
    public abstract void onAttack (Character hero, Character victim);
    public abstract void onAttacked(Character attacked, Character attacker, int dmg);
    public abstract void onAllyAttacked(Character ally, Character hurtfriend, Character attacker, int dmg);
    public abstract void HPChange (Character hero, int oldhp, int newhp);
    public static void Healed (Character healed, int regener) 
    {
        boolean nowound=true; int h=healed.HP;
        if (healed.binaries.contains("Wounded")||healed.immunities.contains("Heal"))
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
            healed.HPChange(healed, h, healed.HP);
        }
    }
    public static int GetHealAmount (Character hero, int amount)
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
                int inc=eff.power;
                amount+=inc;
            }
        }
        if (amount<0)
        {
            amount=0;
        }
        return amount;
    }
    public static void PassiveHealed (Character healed, int regener) //passive healing, unaffected by Recovery
    {
        boolean nowound=true; int h=healed.HP;
        if (healed.binaries.contains("Wounded"))
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
            healed.HPChange(healed, h, healed.HP);
        }
    }
    public static void CheckDrain (Character hero, int amount)
    {
        //this is now under the attack methods instead of each attackab and basicab
        if (hero.lifesteal<=0||hero.binaries.contains("Wounded"))
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
            amount=hero.GetHealAmount(hero, amount);
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
        if (hero.binaries.contains("Shattered")||hero.immunities.contains("Defence")||hero.SHLD>=amount)
        {
            //nothing happens; shield does not stack
        }
        else 
        {
            hero.SHLD=amount;
            System.out.println("\n"+hero.Cname+" received "+amount+" shield!");
        }
    }
    public static void add (Character hero, StatEff eff) //adding a stateff
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
            if (!(eff.getimmunityname().equalsIgnoreCase("Protect"))) //due to taunt/protect interaction; no point in announcing it being added if it's instantly removed
            {
                System.out.println ("\n"+hero.Cname+" gained a(n) "+eff.geteffname());
            }
        }
    }
    public static void remove (Character hero, int removalcode, boolean nullify) //removes status effects
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.hashcode==removalcode)
            {
                hero.effects.remove(eff);
                eff.Nullified(hero);
                if (nullify==false&&!(eff.getimmunityname().equalsIgnoreCase("Protect"))) //redundant to print the message if something like nullify already does
                {
                    System.out.println ("\n"+hero.Cname+"'s "+eff.getimmunityname()+" expired."); 
                }                
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
    public abstract Character onAllyTargeted (Character hero, Character dealer, Character target, int dmg, boolean aoe);
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe)
    {
        //for normal damage from attack skills
        target=target.onTargeted(dealer, target, dmg, aoe);
        if (!(dealer.binaries.contains("Missed")))
        {
            Damage_Stuff.CheckBlind(dealer);
            Damage_Stuff.CheckEvade(dealer, target);
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
        }
        if (dealer.binaries.contains("Missed"))
        {
            dmg=0;
        }
        if (!(dealer.binaries.contains("Missed")))
        {
            dmg=Damage_Stuff.CheckGuard(dealer, target, dmg);      
            for (SpecialAbility h: target.helpers) //for special things like redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
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
                if (friend!=null&&!(friend.binaries.contains("Banished")))
                {
                    friend.onAllyAttacked(friend, target, dealer, dmg);
                }
            }
        }
        if (dealer.dead==false)
        {
            dealer.CheckDrain(dealer, dmg);
        }
        return target;
    }
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe)
    {
        //modified version of attack method since debuff skills do no damage
        target=target.onTargeted(dealer, target, 0, aoe);
        if (!(dealer.binaries.contains("Missed")))
        {
            Damage_Stuff.CheckBlind(dealer);
            Damage_Stuff.CheckEvade(dealer, target);
        }
        target.onAttacked(target, dealer, 0);
        return target;
    }
    public abstract int TakeDamage (Character target, Character dealer, int dmg, boolean aoe); 
    public abstract int TakeDamage (Character target, int dmg, boolean dot); //true for dot and false for all other types of dmg
    public abstract void TookDamage (Character hero, boolean dot, int dmg); //true for dot and false for all other types
    public abstract void TookDamage (Character hero, Character dealer, int dmg);
    public boolean TakeRicochetDamage (Character target, int dmg)
    {
        boolean evade=false;
        if (target.CheckFor(target, "Stun")==false&&(target.CheckFor(target, "Shatter")==false)) //things that would allow the damage to ignore evade
        {
            for (StatEff effect: target.effects) 
            {
                if (effect.getimmunityname().equalsIgnoreCase("Evade"))
                {
                    target.remove(target, effect.hashcode, false); //evade is consumed and no damage is dealt
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
            if (dmg>0) //poison damage ignores shields
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
                Ability.DoRicochetDmg(dmg, target, true);
                knull=target.TakeDamage(target, dmg, true);
            }
        }
        else if (type.equalsIgnoreCase("Wither"))
        {
            dmg-=target.ADR;
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
    public int InHP (int index, boolean summoned)
    {
        //Initialises HP
        int health=0;
        if (summoned==false)
        {
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
            
                case 2: case 3: case 6:
                health= 60; break;   
            
                case 8:
                health= 70; break;
            
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
        if (summoned==false)
        {
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
                case 10: name="Nick Fury (Classic)"; break;
                case 11: name="Nick Fury (Modern)"; break;
                case 12: name="Drax (Classic)"; break;
                case 13: name="Drax (Modern)"; break;
                default: name= "ERROR. INDEX NUMBER NOT FOUND";
            }    
            return name;
        }
        else
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
                default: name="Error with Summon index";
            }    
            return name;
        }
    }
    public abstract void onLethalDamage (Character hero, Character killer, String dmgtype);
    public abstract void onLethalDamage (Character hero, boolean killer, String dmgtype);
    public abstract void onDeath (Character hero, Character killer, String dmgtype);
    public abstract void onAllyDeath (Character bystander, Character deadfriend, Character killer);
    public abstract void onEnemyDeath (Character bystander, Character deadfoe, Character killer);
    public abstract void onKill (Character killer, Character victim);
    public abstract void Transform (Character hero, int newindex, boolean greater); //new index is the index number of the character being transformed into
    public abstract void onAllySummon (Character summoner, Summon newfriend);
    public abstract void onEnemySummon (Character summoner, Summon newfoe);
    public abstract void onBuffed(Character hero, StatEff buff, boolean add);
    public abstract void onDebuffed (Character hero, StatEff debuff, boolean add); //true if the stat is being added and false for removal
    public abstract void onHealEffed (Character hero, StatEff heal, boolean add);
    public abstract void onDefEffed (Character hero, StatEff def, boolean add);
    public abstract void onOtherEffed (Character hero, StatEff otr, boolean add);
    public abstract boolean onAllyControlled (Character hero, Character controlled, Character controller);
    public abstract boolean onEnemyControlled (Character hero, Character controlled, Character controller);
    public abstract boolean onSelfControlled (Character hero, Character controller);
    public static void BanishTick (Character hero)
    {
        for (StatEff eff: hero.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Banish"))
            {
                eff.UseBanish();
            }
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
        int go=hero.GetStatCount (hero, name, type);
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