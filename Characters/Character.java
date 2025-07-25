package myMarvelcardgamepack;
import java.util.ArrayList; 
import java.util.Scanner;        
/**
* Designer: Timothy Landers
* Date: 1/12/22
* Filename: Character
* Purpose: Template for all characters, both heroes and summons.
*/
public abstract class Character 
{
    //base stats
    private String Cname="J. Jonah Jameson";    
    private int size=1;
    private int HP=0; //health
    private int maxHP;
    private int BHP; //temporary bonus health from barrier
    private int index;
    private int turn=0; //++ at start of turn; which turn they are on
    private int DR=0; //damage reduction from hits from sources other than resistance (i.e. passives)
    private int PRDR=0; //damage reduction from Resistance Effects
    private int RDR=0; //damage reduction from Resistance
    private int ADR=0; //damage reduction from all sources
    private int BuDR, BlDR, PoDR, ShDR, WiDR=0; //dot damage reduction; burn, bleed, poison, shock, and wither
    private int DV=0; //damage vulnerability
    private int BD=0; //bonus damage on attacks from status effects
    private int PBD=0; //bonus damage on attacks from passives
    private int CC=0; //crit chance
    private int nCC=0; //negative crit chance
    private double critdmg=1.5; //default is crits do +50% dmg
    private int SHLD=0; //shield
    private int Cchance=0; //extra status chance
    private int CritDR=0; //crit resistance, mainly from bulwark
    private int CritVul=0; //vulnerable; not negative critdr because it only applies if target can crit
    private int lifesteal=0; //drain
    private boolean team1=false; //which team they're on, for determining who's an ally and who's an enemy
    private Ability[] abilities = new Ability[5];
    private Ability[][] transabs= new Ability[3][5]; //storing abilities of characters they transformed into
    private boolean dead=false; 
    private final boolean summoned=false;
    private boolean targetable=true;
    private int accuracy=100; //for blind
    private int dmgtaken=0;
    private int id; //identifier number
    private int passivecount; //for keeping track of passive stuff
    private Ability activeability=null; //last used ability     
    private ArrayList<Character> passivefriend= new ArrayList<Character>(); //for lads like eddie brock venom
    private ArrayList<StatEff> effects= new ArrayList<StatEff>(); //holds status effects
    private ArrayList<String> immunities= new ArrayList<String>(); 
    private ArrayList<String> binaries=new ArrayList<String>(); //overlapping conditions like stunned and invisible
    private ArrayList<String> ignores=new ArrayList<String>(); //ignore when attacking
    private ArrayList<SpecialAbility> helpers=new ArrayList<SpecialAbility>(); //performs unique misc. functions like redwing
    public Character (int index)
    {
        this.index=index;
        this.id=CardCode.RandomCode();
    }
    @Override
    public String toString()
    {
        return this.Cname;
    }
    public abstract void AddImmune (boolean b);
    public abstract void add (StatEff eff, boolean loud); //adding a stateff, with or without notification message    
    public abstract void remove (int removalcode, String nullify); //removes status effects
    public abstract void StatFailed (Character h, StatEff e, String c); //notification when any hero fails to apply a stateff; for leader, gorr, magneto, etc
    public abstract void onEnemyGain (Character enemy, StatEff e); //for when an enemy gains a stateff
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
    public void onTurnStart () //ticks down stateffs that tick on turnstart, aka dot and regen, so the various types can be printed in a combined message
    {
        ArrayList<StatEff> concurrentmodificationexception4electricboogalooboogaloo2= new ArrayList<StatEff>();
        concurrentmodificationexception4electricboogalooboogaloo2.addAll(this.effects);
        boolean bleed=false, burn=false, shock=false, poison=false, wither=false, regen=false, wound=false, immune=false;
        int BlT=0, BuT=0, ShT=0, PoT=0, WiT=0, ReT=0; //total dmg/healing values of the effs
        for (StatEff e: concurrentmodificationexception4electricboogalooboogaloo2) //gathers aggregate strength of the effs
        {
            if (this.effects.contains(e)) //if something like wolvie's frenzy removes the eff from him in the middle of the loop, skip the removed eff 
            {
                int dmg=e.power; //this method condenses dot dmg to reduce clutter; instead of printing "took 5 burn dmg" 5 times, now print "took 25 burn dmg" one time
                String name=e.getimmunityname();
                if (name.equals("Bleed")) 
                {
                    dmg-=this.BlDR; dmg-=this.ADR; BlT+=dmg; //System.out.println("Took "+dmg+" bleed dmg");
                    if (e.power>0) //due to angel; don't want to print taking 0 damage from an eff with 0 strength bc it's redundant
                    bleed=true;
                }
                else if (name.equals("Burn"))
                {
                    dmg-=this.BuDR; dmg-=this.ADR; BuT+=dmg; //System.out.println("Took "+dmg+" burn dmg");
                    if (e.power>0) 
                    burn=true;
                }
                else if (name.equals("Shock"))
                {
                    dmg-=this.ShDR; dmg-=this.ADR; ShT+=dmg; //System.out.println("Took "+dmg+" shock dmg");
                    if (e.power>0) 
                    shock=true; 
                }
                else if (name.equals("Poison"))
                {
                    if (!(this.immunities.contains("Lose")))
                    {
                        dmg-=this.PoDR; PoT+=dmg;
                        if (e.power>0) 
                        poison=true; 
                    }
                    else
                    {
                        poison=true; immune=true;
                    }
                }
                else if (name.equals("Wither"))
                {
                    if (!(this.immunities.contains("Lose")))
                    {
                        dmg-=this.WiDR; WiT+=dmg; 
                        if (e.power>0) 
                        wither=true; 
                    }
                    else
                    {
                        wither=true; immune=true;
                    }
                }
                else if (name.equals("Regen"))
                {
                    if (this.HP<this.maxHP) //do not print healing message if hero is already full hp
                    {
                        regen=true; int p=CoinFlip.GetStatCount(this, "Poison", "any"); p*=10; dmg-=p; 
                        if (e.getefftype().equals("Heal")) //to get accurate number of healing to be printed; recovery only affects heal effs, not regen Effects
                        {
                            for (StatEff read: this.effects)
                            {
                                if (read.getimmunityname().equals("Recovery"))
                                {
                                    dmg+=read.power;
                                }
                            }
                        }
                        if (dmg<0)
                        dmg=0;
                        if (dmg+this.HP>this.maxHP)
                        dmg=this.maxHP-this.HP;
                        ReT+=dmg; 
                    }
                }
                e.onTurnStart(this); //calls onturnstart on the eff to ensure dmg is done in the order the effs were applied, for fairness, instead of as a big lump sum later on
                if (this.dead==true) //can no longer be healed/take dot dmg, so end loop 
                break;
            }
        }
        if (BlT<0) //taking negative dmg is not possible, so the printed message must reflect that 
        BlT=0;
        if (BuT<0)
        BuT=0;
        if (ShT<0)
        ShT=0;
        if (PoT<0)
        PoT=0;
        if (WiT<0)
        WiT=0;
        if (ReT<0)
        ReT=0;
        if (bleed==true) //if they had the eff, print how much total dmg/healing they took from it, even if it's 0, because it isn't done by the effs themselves anymore
        System.out.println("\n"+this+" took "+BlT+" Bleed damage."); 
        if (burn==true)
        System.out.println("\n"+this+" took "+BuT+" Burn damage."); 
        if (shock==true)
        System.out.println("\n"+this+" took "+ShT+" Shock damage."); 
        if (poison==true)
        {
            if (immune==true)
            System.out.println("\n"+this+" lost no health from Poison due to an immunity."); 
            else
            System.out.println("\n"+this+" lost "+PoT+" health from Poison."); 
        }
        if (wither==true)
        {
            if (immune==true)
            System.out.println("\n"+this+" lost no health from Wither due to an immunity."); 
            else
            System.out.println("\n"+this+" lost "+WiT+" health from Wither."); 
        }
        if (regen==true)
        {
            if (this.CheckFor("Wound", false)==true)
            System.out.println("\n"+this+" could not be healed due to being Wounded.");
            else
            System.out.println("\n"+this+" was healed for "+ReT+" health!"); 
        }
    }
    public void DOTdmg (int dmg, String type) //take dot dmg
    {
        if (this.dead==false)
        {
            int knull; //since the take damage methods require return values, this will store them
            if (type.equalsIgnoreCase("bleed"))
            {
                dmg=(dmg-this.ADR-this.BlDR); //factoring in damage resistance
                /*if (dmg<=0)
                dmg=0;
                System.out.println ("\n"+this.Cname+" took "+dmg+" Bleed damage"); */
                if (dmg>0)
                {
                    knull=this.TakeDamage(dmg, true);
                }
            }
            else if (type.equalsIgnoreCase("burn"))
            {
                dmg=(dmg-this.ADR-this.BuDR);
                /*if (dmg<=0)
                dmg=0;
                System.out.println ("\n"+this.Cname+" took "+dmg+" Burn damage"); */
                if (dmg>0)
                {
                    knull=this.TakeDamage(dmg, true);
                }
            }
            else if (type.equalsIgnoreCase("poison"))
            {
                dmg=(dmg-this.PoDR);
                if (dmg<=0)
                dmg=0;
                this.LoseHP(null, dmg, "Poison", false);
            }
            else if (type.equalsIgnoreCase("shock"))
            {
                dmg=(dmg-this.ADR-this.ShDR);
                /*if (dmg<=0)
                dmg=0;
                System.out.println ("\n"+this.Cname+" took "+dmg+" Shock damage"); */
                if (dmg>0)
                {
                    knull=this.TakeDamage(dmg, true);
                    Ability.DoRicochetDmg(dmg, this, this, true, null);
                }
            }
            else if (type.equalsIgnoreCase("Wither"))
            {
                dmg-=this.WiDR;
                if (dmg<=0)
                dmg=0;
                this.LoseHP(null, dmg, "Wither", false);
            }
        }
    }
    public void onTurn (boolean ignoreme) //onturn passives present in both overridden subclasses; this just notifies allies and enemies the hero took their turn
    {
        ++this.turn; //all of below happens regardless of whether hero is stunned or not
        boolean team=this.team1;
        Character[] friends=Battle.GetTeammates(this); 
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyTurn(this, this.summoned);
            }
        }
        team=CoinFlip.TeamFlip(team); //reverse team affiliation to get enemies
        Character[] foes=Battle.GetTeam(team);
        for (Character foe: foes)
        {
            if (foe!=null&&!(foe.binaries.contains("Banished")))
            {
                foe.onEnemyTurn(this, this.summoned);
            }
        }
        if (this.team1==true) //notify dead friends like Phoenix that it's potentially time to revive
        {
            ArrayList<Character> concurrentmodificationexception4electricboogalooboogaloo2= new ArrayList<Character>(Battle.team1dead); 
            for (Character deadlad: concurrentmodificationexception4electricboogalooboogaloo2)
            {
                deadlad.onAllyTurn (this, this.summoned);
            }
        } 
        else
        {
            ArrayList<Character> concurrentmodificationexception4electricboogalooboogaloo2= new ArrayList<Character>(Battle.team2dead); 
            for (Character deadlad: concurrentmodificationexception4electricboogalooboogaloo2)
            {
                deadlad.onAllyTurn (this, this.summoned);
            }
        }
    }
    public void onTurnEnd (boolean notbonus) //generic turnend function; turnend passives overrride this; tick down stateffs and reduce cds
    {
        ArrayList<StatEff> exception= new ArrayList<StatEff>(); exception.addAll(this.effects);
        for (StatEff e: exception)
        {
            if (this.dead==false)
            e.onTurnEnd(this);
        }
        if (this.dead==false) //ab cds reduced at end of turn; if hero dies from dot before turnend, they don't get cd reduction
        {
            for (Ability ab: this.abilities)
            {
                if (ab!=null)
                ab.CDReduction(1);
            } 
        }
    }
    public Ability ChooseAb () //called by battle.turn
    {
        if (this.team1==true)
        System.out.println ("\nPlayer 1, choose an ability for "+this+" to use. Type its number, not its name.");
        else
        System.out.println ("\nPlayer 2, choose an ability for "+this+" to use. Type its number, not its name.");
        boolean skip=CardCode.CheckSkip(this); //allows heroes with no usable abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (this.abilities[i]!=null)
            {
                System.out.println (a+": "+this.abilities[i].GetAbName(this));  
            }
        }
        System.out.println("6: Check an ability's description");
        System.out.println("7: Check the description of this character's passive(s)");
        if (skip==true)
        {
            System.out.println ("0: Skip turn");
        }
        boolean good=false; int choice;
        do
        {
            choice=Damage_Stuff.GetInput(); 
            --choice; //to get the index number since the number entered was the ability number
            if (choice==-1&&skip==true) //skipped turn
            {
                good=true;
            }
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.abilities[choice].CheckUse(this)==false)
            {
                System.out.println("Selected ability is not currently usable. Pick another one.");
            }
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.activeability!=null&&this.activeability.unbound==true&&this.abilities[choice].unbound==true)
            {
                System.out.println("Unbound abilities may only be used once per turn.");
            }
            else if (choice<5&&choice>=0&&!(this.abilities[choice]==null)&&this.abilities[choice].CheckUse(this)==true) 
            {
                good=true;
            }
            else if (choice==5)
            {
                CardCode.GetDesc(this);
            }
            else if (choice==6)
            {
                CardCode.GetPDesc(this);
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
    public abstract void AllyHPChange (Character ally, int oldhp, int newhp);
    public abstract void EnemyHPChange (Character enemy, int oldhp, int newhp);
    public void Healed (int regener, boolean passive, boolean regen) //passive refers to the "recover up to X missing health" type of healing abs
    {
        boolean nowound=true; int h=this.HP;
        if (passive==false&&this.immunities.contains("Heal")) //passive healing isn't considered to be a heal ability
        {
            if (regen==false) //don't print anything for regen; that's already handled by hero.onTurnStart
            System.out.println("\n"+this.Cname+" could not be healed due to an immunity!");
            nowound=false;
        }
        else if (passive==false&&this.binaries.contains("Wounded"))
        {
            if (regen==false)
            System.out.println("\n"+this.Cname+" could not be healed due to being Wounded!");
            nowound=false;
        }
        else if (this.binaries.contains("No Heal")) //can't recover any health by any means, only for corvus as of 4.6.3
        {
            nowound=false;
        }
        if (nowound==true&&this.dead==false) //able to be healed
        {
            if (this.HP<this.maxHP) //is healed
            {
                regener=this.GetHealAmount(regener, passive);
                if (regener+this.HP>this.maxHP) //can't have more health than the maximum amount
                regener=this.maxHP-this.HP;
                this.HP+=regener;
                if (passive==false&&regen==false)
                System.out.println("\n"+this.Cname+" was healed for "+regener+" health!");
                else if (passive==true&&regen==false)
                System.out.println ("\n"+this.Cname+" regained "+regener+" health!");
                this.HPChange(h, this.HP);
            }
            else if (passive==false&&regen==false) //cannot heal due to being at full hp
            System.out.println("\n"+this.Cname+" was healed for 0 health!");
        }
    }
    public int GetHealAmount (int amount, boolean passive)
    {
        for (StatEff eff: this.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Poison"))
            {
                amount-=10;
            }
        }
        if (passive==false) //passive healing is unaffected by recovery
        {
            for (StatEff eff: this.effects)
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
    public void CheckDrain (Character target, int amount) //this is now under the attack method instead of each attackab and basicab
    {
        if (this.lifesteal<=0||target.immunities.contains("Drained"))
        {
            //can't heal
        }
        else if (this.binaries.contains("Wounded"))
        {
            System.out.println("\n"+this.Cname+" could not be healed due to being Wounded!");
        }
        else
        {
            if (this.lifesteal==50)
            {
                double dub=amount/2;
                amount=5*(int)Math.ceil(dub/5.0); //drain half, so only heal for half of damage dealt, rounded up
            }
            amount=this.GetHealAmount(amount, false);
            this.Healed(amount, false, false);
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
    public abstract Character onTargeted (Character attacker, int dmg, boolean aoe);
    public abstract void onAllyTurn (Character ally, boolean summoned); 
    public abstract void onEnemyTurn (Character enemy, boolean summoned);
    public abstract void onFightStart();
    public abstract void onAllyAttacked(Character hurtfriend, Character attacker, int dmg);
    public abstract Character onAllyTargeted (Character dealer, Character target, int dmg, boolean aoe); //for passives that change the target hero, like thing's
    public abstract void BeforeAttack (Character target, boolean t); //t is whether it was called before or after checking for protect
    public Character Attack (Character dealer, Character target, int dmg, boolean aoe) //for attack skills
    {
        BeforeAttack(target, true);
        target=target.onTargeted(dealer, dmg, aoe); 
        BeforeAttack(target, false);
        if (!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))) //only check if dealer isn't immune to miss and hasn't missed already; can't miss twice 
        { 
            if (dealer.activeability.blind==false)
            {
                Damage_Stuff.CheckBlind(dealer);
                dealer.activeability.blind=true;
            }
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); 
                dealer.activeability.evade=true;
            }
        }
        if ((!(dealer.binaries.contains("Missed")))&&target.dead==false) //check that they're still alive, so mulitihit attacks don't trigger takedamage on a dead target
        {
            dmg=Damage_Stuff.DamageFormula(dealer, target, dmg);
            if (target.CheckFor("Guard", false)==true) //no need to waste time calling it otherwise
            dmg=Damage_Stuff.CheckGuard(dealer, target, dmg);      
            for (SpecialAbility h: target.helpers) //for redwing 
            {
                dmg=h.Use (target, dmg, dealer); 
            }
            dmg=target.TakeDamage(dealer, dmg, aoe);
        }
        else
        dmg=0;
        dealer.activeability.ReturnDamage(dmg); //tells the ability how much dmg the attack did
        dealer.onAttack(target); //activate relevant passives after attacking
        if (target.dead==false&&(target.team1!=dealer.team1)) //characters like annihilus shouldn't trigger counter, reflect, etc on allies
        {
            target.onAttacked(dealer, dmg, aoe);
        }
        Character[] friends=Battle.GetTeammates(target);
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyAttacked(target, dealer, dmg);
            }
        }
        if (dealer.dead==false)
        {
            dealer.CheckDrain(target, dmg);
        }
        return target;
    }
    public Character AttackNoDamage (Character dealer, Character target, boolean aoe) //modified version of attack method since debuff skills cannot do damage
    {
        BeforeAttack(target, true);
        target=target.onTargeted(dealer, 0, aoe);
        BeforeAttack(target, false);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            if (dealer.activeability.blind==false)
            {
                Damage_Stuff.CheckBlind(dealer);
                dealer.activeability.blind=true;
            }
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); 
                dealer.activeability.evade=true;
            }
        }
        dealer.onAttack(target);
        if (target.team1!=dealer.team1) //characters like annihilus shouldn't trigger counter, reflect, etc on allies
        target.onAttacked(dealer, 0, aoe);
        Character[] friends=Battle.GetTeammates(target);
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyAttacked(target, dealer, 0);
            }
        }
        return target;
    }
    public Character AttackNoDamage (Character dealer, Character target, int lossy, boolean aoe, boolean max) //for (max) health loss attacks
    {
        BeforeAttack(target, true);
        target=target.onTargeted(dealer, 0, aoe);
        BeforeAttack(target, false);
        if ((!(dealer.binaries.contains("Missed"))&&!(dealer.immunities.contains("Missed"))))
        {
            if (dealer.activeability.blind==false)
            {
                Damage_Stuff.CheckBlind(dealer);
                dealer.activeability.blind=true;
            }
            if (dealer.activeability.evade==false)
            {
                Damage_Stuff.CheckEvade(dealer, target); 
                dealer.activeability.evade=true;
            }
        }
        if (!(dealer.binaries.contains("Missed")))
        {
            if (max==true)
            target.LoseMaxHP (dealer, lossy);
            else
            target.LoseHP (dealer, lossy, "knull", false);
        }
        dealer.onAttack(target);
        if (target.dead==false&&(target.team1!=dealer.team1)) //characters like annihilus shouldn't trigger counter, reflect, etc on allies)
        target.onAttacked(dealer, 0, aoe);
        Character[] friends=Battle.GetTeammates(target);
        for (Character friend: friends)
        {
            if (friend!=null&&!(friend.binaries.contains("Banished")))
            {
                friend.onAllyAttacked(target, dealer, 0);
            }
        }
        return target;
    }
    public abstract void onCrit (Character target); //called after successful crit; for passives
    public abstract void onAttack (Character personIjustattacked);
    public abstract void onAttacked(Character attacker, int dmg, boolean aoe);
    public abstract void onEvade (Character attacker);
    public abstract void onAllyEvade (Character ally, Character attacker);
    public abstract void onEnemyEvade (Character enemy, Character attacker);
    public abstract int TakeDamage (Character dealer, int dmg, boolean aoe); //takedamage checks hero shield and calls tookdamage
    public abstract int TakeDamage (int dmg, boolean dot); 
    public abstract void TookDamage (Character dealer, int dmg); //triggers relevant passives and checks if hero should be dead
    public abstract void TookDamage (boolean dot, int dmg); 
    public abstract void onLethalDamage (Character killer, String dmgtype); //triggers relevant passives/immortality and calls ondeath
    public abstract void onDeath (Character killer, String dmgtype);
    public abstract void onAllyDeath (Character deadfriend, Character killer);
    public abstract void onEnemyDeath (Character deadfoe, Character killer);
    public abstract void onKill (Character victim);
    public abstract void onRez (Character healer); 
    public abstract void onAllyRez (Character ally, Character healer); //to reapply passive bonuses to allies
    public void LoseMaxHP (Character attacker, int lossy)
    {
        if (!(this.immunities.contains("Reduce")))
        {
            System.out.println("\n"+this.Cname+"'s max health was reduced by "+lossy+"!");
            this.maxHP-=lossy;
            if (this.index==105) //mr.immortal is currently (4.5) the only hero with a passive relating to losing max hp, so this is an if instead of a switch
            ActivePassive.Immortal(this, "hp");
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
    public void LoseHP (Character attacker, int lossy, String dot, boolean sacrifice) //modified version of tookdamage
    {
        if (sacrifice==true||!(this.immunities.contains("Lose")))
        {
            if (dot.equals("Poison")||dot.equals("Wither")) 
            {
                //moved print statement from here to the dotdmg method
            }
            else
            {
                if (sacrifice==false)
                System.out.println("\n"+this.Cname+" lost "+lossy+" health!");
                else
                System.out.println("\n"+this.Cname+" sacrificed "+lossy+" health!");
            }
            int h=this.HP;
            Damage_Stuff.CheckBarrier(this, null, lossy); //attacker has to be null or barrier won't have its value reduced by the health loss; see checkbarrier for why
            if (this.HP<=0)
            this.HP=0;
            if (this.HP==0&&!(this.binaries.contains("Immortal")))
            {
                if ((dot.equals("Poison")||dot.equals("Wither")))
                {
                    this.onLethalDamage(null, "DOT");
                }
                else if (dot.equalsIgnoreCase("self"))
                {
                    this.onLethalDamage(null, "Lose");
                }
                else
                {
                    this.onLethalDamage(attacker, "Lose");
                }
            }
            if (this.dead==false)
            {
                this.HPChange(h, this.HP);
            }
        }
        else
        {
            System.out.println(attacker+"'s health loss failed due to "+this+"'s immunity.");
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
                //220
                case 6: case 9: case 10: case 14: case 19: case 21: case 29: case 33: case 36: case 37: case 68: case 69: case 71: case 73: case 80: case 91: case 93: 
                case 95: case 96: case 102: case 103:
                return 220;
                //230
                case 1: case 2: case 3: case 4: case 5: case 7: case 8: case 11: case 18: case 20: case 23: case 24: case 25: case 34: case 39: case 40: case 64: case 72: 
                case 74: case 75: case 79: case 81: case 82: case 84: case 86: case 88: case 89: case 90: case 92: case 94: case 97: case 98: case 100: case 101:
                return 230;
                //240
                case 12: case 13: case 15: case 16: case 17: case 22: case 27: case 28: case 30: case 32: case 35: case 38: case 41: case 61: case 62: case 66: case 70: case 78:
                case 83: case 85: case 87: case 99: case 104:
                return 240;
                //special carrots
                case 26: return 130; //modork
                case 31: return 250; //hulk
                case 76: case 105: return 100; //speedball and mr immortal
                case 63: case 65: case 67: case 77: return 200; //corvus, supergiant, maw, penance
            }    
            return 616;
        }
        else
        {
            switch (index)
            {
                case 7: return 5;    
            
                case 1: case 10: case 11: return 40;
            
                case 5: case 9: return 50;
            
                case 2: case 3: case 6: case 27: return 60;
                
                case 8: return 70;
                
                case 28: return 80;
                
                case 4: return 100;
                
                case 14: case 15: case 16: return 120;
            
                case 12: return 200;
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
                //2.0: Original
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
                case 14: return "X-23 (Classic)"; 
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
                //2.1: Sinister Six
                case 36: return "Vulture (Classic)";
                case 37: return "Mysterio (Classic)";
                case 38: return "Doctor Octopus (Classic)";
                case 39: return "Electro (Max Dillon)";
                case 40: return "Sandman (Classic)";
                case 41: return "Rhino (Classic)";
                //2.5: Thanos Arrives
                case 61: return "Thanos (Classic)";
                case 62: return "Thanos (Infinity Gauntlet)";
                case 63: return "Corvus Glaive (Classic)";
                case 64: return "Proxima Midnight (Classic)";
                case 65: return "Supergiant (Classic)";
                case 66: return "Black Dwarf (Classic)";
                case 67: return "Ebony Maw (Classic)";
                //2.6: U-Foes
                case 68: return "Vector (Classic)";
                case 69: return "X-Ray (Classic)";
                case 70: return "Ironclad (Classic)";
                case 71: return "Vapor (Classic)";
                //2.7: Thunderbolts
                case 72: return "Baron Zemo (Helmut Zemo)";
                case 73: return "Melissa Gold (Screaming Mimi)";
                case 74: return "Melissa Gold (Songbird)";
                case 75: return "Moonstone (Karla Sofen)";
                case 76: return "Speedball (Classic)";
                case 77: return "Speedball (Penance)";
                case 78: return "Red Hulk (Classic)";
                case 79: return "Radioactive Man (Classic)";
                case 80: return "Scarecrow (Modern)";
                //2.8: Defenders
                case 81: return "Daredevil (Matt Murdock)";
                case 82: return "Iron Fist (Danny Rand)";
                case 83: return "Luke Cage (Modern)";
                case 84: return "Namor (Modern)";
                case 85: return "Silver Surfer (Classic)";
                //2.9: Fearsome Foes of Spider-Man
                case 86: return "Kraven the Hunter (Classic)";
                case 87: return "Lizard (Classic)";
                case 88: return "Scorpion (Modern)";
                case 89: return "Hydro-Man (Classic)";
                case 90: return "Carnage (Classic)";
                case 91: return "Green Goblin (Classic)";
                case 92: return "Green Goblin (Red Goblin)";
                case 93: return "Hobgoblin (Roderick Kingsley)";
                case 94: return "Hobgoblin (Phil Urich)";
                //2.10: Marvellous Mutants
                case 95: return "Emma Frost (Classic)";
                case 96: return "Emma Frost (Diamond Form)";
                case 97: return "Angel (Modern)";
                case 98: return "Angel (Archangel)";
                case 99: return "Colossus (Classic)";
                case 100: return "Elixir (Golden Skin)";
                case 101: return "Elixir (Black Skin)";
                case 102: return "Gambit (Classic)";
                case 103: return "Nightcrawler (Modern)";
                case 104: return "Bishop (Classic)";
                case 105: return "Mr. Immortal (Classic)";
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
                case 14: return "Bruin Franchisee (Summon)";
                case 15: return "Ringer Franchisee (Summon)";
                case 16: return "Squid Franchisee (Summon)";
                case 27: return "Spiderling (Summon)"; 
                case 28: return "Arachnaught (Summon)"; 
            }    
            return "Error with Summon index";
        }
    }
    public abstract void Transform (int newindex, boolean greater); //new index is the index number of the character being transformed into
    public abstract void onAllySummon (Character summoner, Summon newfriend);
    public abstract void onEnemySummon (Character summoner, Summon newfoe);
    public abstract void onAllyControlled (Character ally, Character controller);
    public abstract void onSelfControlled (Character controller);
    public void BanishTick ()
    {
        for (StatEff eff: new ArrayList<StatEff>(this.effects))
        {
            if (eff.getimmunityname().equals("Banish"))
            {
                eff.UseBanish(this); break; //banish does not stack since it would be redundant, not to mention impossible to apply more than once
            }
        }
    }
    public static String MakeDesc (int index, boolean summoned) //descriptions of every character's passives
    {
        if (summoned==false) 
        {
            switch (index)
            {
                //2.0: Original
                case 1: //moon knight 
                return "When an ally Protected by Moon Knight is attacked, counter for 55 damage.";
                case 2: //gamora
                return "With Intensify, ignore Protect and Counter, become immune to Steal, and gain +50% status chance. Bleeds applied to Protected enemies have +1 duration."; 
                case 4: //iron man
                return "When an Intensify is Stolen or Nullified from self, gain an Empowerment with equal value.";
                case 5: //war machine
                return "On War Machine's first turn, apply a Target: 5 Effect to an enemy, preventing Invisible."; 
                case 6: //captain america
                return "Gain Shield: 20 on fight start and on turn."; 
                case 7: //falcon
                return "On fight start, apply Redwing, granting 50% damage reduction and debuff immunity once when taking 120+ damage from an attack. "+
                "Apply a small Mend to heroes who consume Redwing."; 
                case 8: //bucky
                return "Attacks ignore Defence."; 
                case 9: //star lord
                return "Every other turn, apply Confidence: 15 to self and allies."; 
                case 10: //fury sr
                return "Summons Nick Fury LMD (Summon) when falling below 90 HP for the first time."; 
                case 12: //drax og
                return "On fight start, apply Obsession to an enemy. Apply +1 on attack (max 3). "+
                "Gain immunity to buffs and Persuaded, ignore targeting effects, and always takes status effect damage on turn end."; 
                case 13: //drax modern
                return "Do +15 damage and apply debuffs with +15 strength when attacking enemies with 75% HP or more."; 
                case 14: //x23
                return "Gain +50% crit chance against enemies below 90 HP. On crit, 50% chance to gain Regen: 15 for 1 turn."; 
                case 15: //wolverine
                return "Gain Regen: 15 for 1 turn when attacked. "+
                "After taking 180 damage, clear status effects on self and enter Berserker Frenzy, granting +15 damage reduction but making attacks Random Target."; 
                case 16: //venom og
                return "On fight start, choose an ally to gain Resistance; when they're attacked, counter for 40 damage. Ignore Evade but take +5 damage while Burning."; 
                case 17: //venom gargan
                return "On kill, gain Focus for 1 turn. Ignore Evade but take +5 damage while Burning."; 
                case 18: //spidey
                return "Evade all enemy AoE attacks. On turn, gain Evade. While he has Evade, Spider-Man becomes the target when an ally with less HP than him is attacked."; 
                case 20: //superior spidey
                return "Attacks against enemies with Tracers are Inescapable."; 
                case 23: //captain marvel 
                return "Gain immunity to Poison and take no damage from Shock or Burn. On turn, when taking 80+ damage, and when attacking, gain 1 E. "+
                "At 5 E, lose all status effects and Transform into Binary.";
                case 24: //binary
                return "Gain immunity to status effects. On any turn, lose 1 E to do 10 Elusive damage to all enemies."; 
                case 25: //agent venom
                return "While above 5 C, gain +50% status chance; while below, apply Bleed: 15 for 1 turn and do +10 damage to self and the target when attacking. "+
                "\nIgnore Evade but take +5 damage while Burning. Lose 3 C when attacked."; 
                case 26: //modok
                return "Gain Shield: 100 on fight start; while active, gain debuff immunity and take -100 damage from attacks that do 100+ damage. "+
                "\nAttacks ignore targeting effects and apply a 1 turn Shatter or disable debuff based on the target's abilities.";
                case 27: //ultron
                return "Gain immunity to Bleed, Poison, Copy, Snare, Control, and Steal; take no Wither damage."; 
                case 28: //doom
                return "Gain immunity to Control, Shock, Burn, Persuaded, and Freeze; once per turn, apply Shock: 20 for 1 turn when attacked."; 
                case 30: //brawn
                return "Gain immunity to Poison and Control. Brawn can Nullify Heal and Defence effects, and gains copies of effects he Nullified. ";
                case 31: //hulk
                return "Gain immunity to Poison, Control, Terror, and Persuaded. Take less and deal more damage for every 40 missing HP.";
                case 32: //black bolt
                return "Every other turn, gain 1 E; when using an ability, lose all E to do +20 damage for each. Gain immunity to Control.";
                case 33: //deadpool
                return "On turn, gain 30 HP. Do +15 damage against Summons and reduce all cooldowns by 1 turn on kill.";
                case 35: //juggernaut
                return "Gain immunity to Stun and Copy. Gain +15 damage reduction and Control immunity while above 100 HP. "+ 
                "Gain 1 M on turn and attack (max 5); while at 5, become debuff immune.";
                //2.1: Sinister Six
                case 36: //vulture
                return "50% chance to apply Wound for 1 turn when attacking enemies below 120 HP.";
                case 39: //electro
                return "Convert all Shocks on self to Intensify Effects with equal value.";
                case 40: //sandman
                return "Gain immunity to Bleed, Disarm, Snare, and Shock, and ignore Counters; when receiving 2 Burns, convert them into a Stun Effect for 1 turn.";
                case 41: //rhino
                return "On fight start, gain Resistance. Take -10 Bleed damage. Gain immunity to max HP reduction, Suppression, Vulnerable, and Terror.";
                //2.5: Thanos Arrives
                case 61: //thanos
                return "Gain immunity to Banish, Control, Wither, Persuaded, HP loss, and max HP reduction. Gain +10 damage reduction and +5 for each G. "+
                "Gain 1 G on turn and enemy death. At 6, Transform into Thanos (Infinity Gauntlet). On death, enemies gain Confidence: 45.";
                case 62: //gauntlet thanos
                return "Gain immunity to status effects, crits, HP loss, and max HP reduction. All abilities are Elusive and inescapable. On Transform, take a turn.";
                case 63: //corvus
                return "Gain +100% crit chance. On lethal damage, gain -100% crit chance, gain Guard, and become immune to status effects and death until the Guard is lost.";
                case 64: //proxima
                return "Ignore Evade.";
                case 65: //supergiant
                return "Dominated enemies become allies with reduced HP. If Supergiant dies or they die from anything other than Feast, they lose Dominate and regain their HP.\n"+
                "On Supergiant's first turn, apply Terror to an enemy for 1 turn(s).";
                case 67: //maw
                return "Enemy Whispers are removed when they are attacked, but 3 Whispers become Persuaded, disabling abilities and granting Ebony Maw Evasion.";
                //2.6: U-Foes
                case 68: //vector
                return "Gain immunity to Shock, Bleed, Burn, and Freeze. When using an ability on self or an ally, 100% chance to Purify. "+
                "When using an ability on an enemy, 100% chance to Nullify.";
                case 69: //x-ray
                return "Gain immunity to Bleed, Poison, and Heal. Take no damage from Wither.";
                case 70: //ironclad
                return "Gain immunity to Bleed, Burn, and Freeze, but take +10 damage from Poison. When an ally takes 100+ damage, gain Taunt for 1 turn. "+ 
                "If attacked while Taunting, counter for 0 damage, affected by status effects on self.";
                case 71: //vapor
                return "Gain immunity to Bleed and Poison. Ignore Evade.";
                //2.7: Thunderbolts
                case 72: //zemo
                return "Gain immunity to Steal and Disarm, and ignore Guard. On turn, remove Guard from self. When using Deadly Lunge for the first time, gain Precision.";
                case 73: //screaming mimi
                return "Debuffs applied are twice as effective (values are 100% instead of 50%)";
                case 74: //songbird
                return "Mend can critically hit, increasing the healing done based on critical damage.";
                case 75: //moonstone
                return "Gain immunity to Steal and ignore Provoke and Terror. "+
                "When failing to gain a duplicate buff, Extend the original by 1 turn and Amplify it by the duplicate's strength.";
                case 76: //speedball
                return "Gain immunity to damage, Control, Snare, max HP reduction, health loss, and Heal. On fight start, gain Reflect. "+
                "On turn, and when attacked or Stunned, sacrifice 15 health.";
                case 77: //penance
                return "Gain immunity to Control and Heal. Take half damage from attacks, and gain 1 Pain for every 20 damage taken before damage reduction.";
                case 78: //red hulk
                return "Take -10 damage for each Burn on self. Every 40 damage taken, gain Burn: 0 for 3 turns; at 5+, lose 30 HP on turn until there are less than 5."; 
                case 80: //scarecrow
                return "Gain immunity to Terror. Enemies immune to Terror cannot gain Fear.";
                //2.8: Defenders
                case 81: //daredevil
                return "Ignore Blind and Invisible. Ignore the Counter activation limit.";
                case 83: //luke cage
                return "Gain immunity to Bleed, Shock, and Burn. Take -25 damage from attacks that do less than 50 damage.";
                case 85: //silver surfer
                return "Gain immunity to status effects. Channelled abilities cannot be interrupted.";
                //2.9: Fearsome Foes of Spider-Man
                case 86: //kraven
                return "Attacks against Disoriented enemies ignore Invisible, Evade, and Blind.";
                case 89: //hydro man
                return "Gain immunity to Bleed, Burn, Snare, and Soaked, but take +10 damage from Shock.";
                case 90: //carnage
                return "When an enemy gains Bleed, gain Intensify: 5 with equal duration. On kill, gain Focus for 1 turn. Ignore Evade but take +10 damage while Burning."; 
                case 91: //goblin
                return "Pumpkin Bombs can apply Weakness: 20, Poison: 15, or Target: 10, for 2 turns.";
                case 92: //red goblin
                return "Gain immunity to Burn and Snare. On attack, gain Intensify: 5. With 3, ignore Evade; with 5, gain +50% status chance. Lose all Intensify on enemy death.";
                case 93: //hobby kingsley
                return "On Franchisee kill, gain 100 HP, gain Focus for 1 turn, and reset all cooldowns.";
                case 94: //hobby phil
                return "When falling below 130 HP for the first time, gain Focus for 1 turn and reset the cooldown of a chosen ability.";
                //2.10: Marvellous Mutants
                case 96: //diamond frost
                return "Gain +15 damage reduction and immunity to Bleed, Poison, Burn, Shock, Stun, Control, and Heal; take no Wither damage. On Transform, gain Taunt for 1 turn.";
                case 97: //angel
                return "100% chance to gain Bleed: 0 for 2 turns when taking damage. On turn, gain 15 HP per Bleed on self.";
                case 98: //archangel
                return "On attack, flat 50% chance to apply Bleed: 15 for 2 turns and flat 50% chance to apply Poison: 15 for 2 turns. "+
                "\nIf the target has 3 Bleed, apply Weakness: 30 for 1 turn and if they have 3 Poison, apply Wound for 1 turn.";
                case 99: //colossus
                return "Gain immunity to Bleed, Burn, and Freeze; with Taunt, incoming attacks have -50% crit chance.";
                case 102: //gambit
                return "Countdowns apply a random disable debuff for 1 turn on expiry.";
                case 103: //nightcrawler
                return "On Evade, counter for 25 damage and apply Bleed: 15 for 1 turn.";
                case 104: //bishop
                return "Store all damage taken as R. On turn, gain Taunt. When taking damage while Taunting, remove the Taunt and gain Regen: 30 for 1 turn.";
                case 105: //mr immortal
                return "On death, apply Confidence: 30 and Focus for 1 turn to all allies. 2 ally turns after dying, Resurrect with full HP. Max HP cannot fall below 5.";
                //default
                default: return "This character doesn't have any passive abilities.";
            }    
        }
        else //summon 
        {
            switch (index)
            {
                case 1: //fury lmd
                return "Gain immunity to Bleed, Poison, and Copy; take no Wither damage. On Summon, Protect Nick Fury (Classic)."; 
                case 28: //arachnaught
                return "Gain immunity to Bleed, Poison, and Copy; take no Wither damage.";
                case 3: //crushbot
                return "Gain immunity to Bleed, Poison, and Copy; take no Wither damage. On turn, apply Target: 5 to the lowest HP enemy.";
                case 4: //ultron drone
                return "Gain immunity to Bleed, Poison, and Copy; take no Wither damage. When gaining a buff, grant Ultron a copy."; 
                case 5: //doombot
                return "Gain immunity to Bleed, Poison, Persuaded, Control, and Copy; take no Wither damage. Die when Dr. Doom dies."; 
                case 6: //lesser demon
                return "Gain immunity to Persuaded. 50% chance to Provoke a random enemy when Summoned; take -10 damage from Provoked enemies."; 
                case 7: //holographic decoy
                return "Gain Taunt on Summon. Holographic Decoy is permanently Stunned and immune to status effects."; 
                case 8: //ice golem
                return "On Summon and every other turn, Protect an ally for 1 turn. Gain immunity to Bleed, Poison, and Shock; take no Wither damage but take +15 damage from Burn."; 
                case 9: //hydra trooper
                return "Do +5 damage for each other HYDRA Trooper."; 
                case 11: //loki's illusions
                return "On Summon, gain Focus and Protect the summoner. Gain immunity to non-Other effects."; 
                case 12: //giganto
                return "Gain immunity to Persuaded and +20 damage reduction. Lose this damage reduction and gain Stun and Target: 40 for 1 turn after attacking. "+
                "Giganto is counted as 2 characters for the purpose of the team size limit."; 
                case 14: //bruin
                return "Bruin is counted as 2 characters for the purpose of the team size limit.";
                case 15: //ringer
                return "Ringer is counted as 2 characters for the purpose of the team size limit.";
                case 16: //squid
                return "Squid is counted as 2 characters for the purpose of the team size limit.";
                //default
                default: return "This character doesn't have any passive abilities.";
            }    
        }
    }
}