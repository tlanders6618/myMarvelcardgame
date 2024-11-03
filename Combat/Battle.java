
package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 25/7/22
 * Filename: Battle
 * Purpose: For the characters to fight.
 */
import java.util.ArrayList;
public class Battle
{
    static Character[] team1= new Character[6];
    static Character[] team2= new Character[6];
    static ArrayList <Character> team1dead= new ArrayList <Character>();
    static ArrayList <Character> team2dead= new ArrayList <Character>();
    static int p1teamsize=0, p2teamsize=0; //size of team 
    static int p1heroes=3, p2heroes=3; //number of characters on the team
    static boolean p1solo=false, p2solo=false;
    static int P1active=0, P2active=0; //active character's array index number
    final static int maxteamsize=6;
    static int tturns=1; //keep track of which player's turn it is 
    public static boolean main (Character Char11, Character Char12, Character Char13, Character Char21, Character Char22, Character Char23)
    {
        int round=0; //variable for keeping track of rounds for TTK sake
        boolean gamewinner=false; //game has been won
        boolean Pwinner=false; //true for player 1 wins, and false for player 2 wins
        FightStart(Char11, Char12, Char13, Char21, Char22, Char23); //allows certain passives to take effect
        Scoreboard2.main(team1, team2);
        int winner=616;
        while (gamewinner==false)
        {
            if (tturns%2!=0) //odd number means player 1's turn
            {
                gamewinner=PlayerTurn(team1, true, false);
                Battle.CheckActive(true);               
            }
            else if (tturns%2==0)
            {
                gamewinner=PlayerTurn(team2, false, false);
                Battle.CheckActive(false);        
                ++round;
            }
            winner=CheckWin(tturns%2); 
            ++tturns;
            Scoreboard2.UpdateScore(team1, team2); 
            if (winner==1)
            {
                Pwinner=true; gamewinner=true;
            }
            if (winner==2)
            {
                Pwinner=false; gamewinner=true;
            } 
        }
        /*
        System.out.println ("\nTotal turns taken: "+tturns); //for data gathering purposes; 28, 30
        System.out.println ("\nRounds taken: "+round);
        for (Character h: team1)
        {
            if (h!=null)
            {
                System.out.println(h.Cname+": "+h.turn+" turns taken"); //4-7 for survivors; 0-2 for dead
            }
        }
        for (Character h: team2)
        {
            if (h!=null)
            {
                System.out.println(h.Cname+": "+h.turn+" turns taken"); //all dps comp: 3, 4
            }
        }
        //*/
        return Pwinner;
    }
    public static void CheckActive(boolean team) //determines which character's turn it should be
    {
        if (team==true)
        {
            ++P1active;
            if (P1active>=6) //avoiding index exceptions since 1995
            {
                P1active=0;
            }
            if (p1heroes==1)
            p1solo=true;
            else
            p1solo=false;
        }
        else
        {
            ++P2active;
            if (P2active>=6)
            {
                P2active=0;
            }
            if (p2heroes==1)
            p2solo=true;
            else
            p2solo=false;
        }
    }
    public static void FightStart(Character Char11, Character Char12, Character Char13, Character Char21, Character Char22, Character Char23)
    { 
        System.out.println ("\nThe battle has begun.");
        Char11.team1=true; //determines which team the characters are on so they know who's an ally and who's an enemy
        Char12.team1=true;
        Char13.team1=true;
        Char21.team1=false;
        Char22.team1=false;
        Char23.team1=false;
        p1teamsize+=(Char11.size+Char12.size+Char13.size); //for extra big lads like Giganto who take up 2 spaces
        p2teamsize+=(Char21.size+Char22.size+Char23.size);
        team1=SetTurnOrder(Char11, Char12, Char13);
        team2=SetTurnOrder(Char21, Char22, Char23);
        Char11.onFightStart(); 
        Char21.onFightStart(); 
        Char12.onFightStart(); 
        Char22.onFightStart(); 
        Char13.onFightStart(); 
        Char23.onFightStart(); 
        //test
        //Battle.SummonSomeone(Char21, new Summon(7)); Battle.SummonSomeone(Char21, new Summon(7)); Battle.SummonSomeone(Char21, new Summon(7));  
    }
    public static Character[] SetTurnOrder (Character one, Character two, Character three) 
    {
        Character[] team= new Character[6];
        team[0]=one; 
        team[1]=two; 
        team[2]=three;
        return team;
    }
    public static boolean PlayerTurn (Character[] champions, boolean team, boolean bonusturn) //passes on boolean from battle.turn to end the game early if needed
    {
        boolean fine=false; Character hero=null;
        while (team==true&&fine==false) //ensures hero is usable
        {
            if (champions[P1active]==null) //change the P1 active value until it's a valid character
            {
                Battle.CheckActive(team); 
            }
            else if (champions[P1active].binaries.contains("Banished")) //turn skip or remove banish if alone
            {
                if (p1solo==true)
                {
                    ArrayList<StatEff> concurrent= new ArrayList<StatEff>();
                    for (StatEff e: champions[P1active].effects)
                    {
                        if (e.getimmunityname().equals("Banish"))
                        {
                            concurrent.add(e);
                        }
                    }
                    for (StatEff e: concurrent)
                    {
                        champions[P1active].remove(e.id, "normal");
                    }
                    hero=champions[P1active];
                    fine=true;
                }
                else
                {
                    System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Banished.");
                    champions[P1active].BanishTick(); //ensures banished heroes take no turns and don't have stateffs or cds reduced but still have their banish duration reduced 
                    Battle.CheckActive(team);
                }
            }
            else if (champions[P1active].binaries.contains("Stunned")) //turn skip, but stateffs and cds still tick
            {
                System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Stunned.");
                champions[P1active].onTurnStart(); //tick down all regen and dot effs 
                if (champions[P1active]!=null) //since dying makes their spot in the team array null, a null check and an alive check are the same thing
                champions[P1active].onTurn(true); //activate some passives
                if (champions[P1active]!=null)
                champions[P1active].onTurnEnd(true); //tick down remaining stateffs and reduce cds
                Battle.CheckActive(team);
            }
            else if (champions[P1active]!=null) //can proceed to their turn
            {
                hero=champions[P1active];
                fine=true;
            }
            if (p1heroes==0||p2heroes==0) //for abilities like sandman's sandstorm that can potentially kill enemies and end the game in-between turns
            {
                Scoreboard2.UpdateScore(team1, team2);
                return true;
            }
        }
        while (team==false&&fine==false) //same thing, but for player 2's team
        {
            if (champions[P2active]==null)
            {  
                Battle.CheckActive(team);
            }
            else if (champions[P2active].binaries.contains("Banished"))
            {
                if (p2solo==true)
                {
                    ArrayList<StatEff> concurrent= new ArrayList<StatEff>();
                    for (StatEff e: champions[P2active].effects)
                    {
                        if (e.getimmunityname().equals("Banish"))
                        {
                            concurrent.add(e);
                        }
                    }
                    for (StatEff e: concurrent)
                    {
                        champions[P2active].remove(e.id, "normal");
                    }
                    hero=champions[P2active];
                    fine=true;
                }
                else
                {
                    System.out.println ("\n"+champions[P2active].Cname+" skips their turn due to being Banished.");
                    champions[P2active].BanishTick(); //ensures banished heroes take no turns but still have their banish duration reduced 
                    Battle.CheckActive(team);
                }
            }
            else if (champions[P2active].binaries.contains("Stunned"))
            {
                System.out.println ("\n"+champions[P2active].Cname+" skips their turn due to being Stunned.");
                champions[P2active].onTurnStart(); //tick down all regen and dot effs 
                if (champions[P2active]!=null) //since dying makes their spot in the team array null, a null check and an alive check are the same thing
                champions[P2active].onTurn(true); //activate some passives
                if (champions[P2active]!=null)
                champions[P2active].onTurnEnd(true); //tick down remaining stateffs and reduce cds
                Battle.CheckActive(team);
            }
            else if (champions[P2active]!=null)
            {
                hero=champions[P2active];
                fine=true;
            }
            if (p1heroes==0||p2heroes==0) //for abilities like sandman's sandstorm that can potentially kill enemies and end the game in-between turns
            {
                Scoreboard2.UpdateScore(team1, team2);
                return true;
            }
        }
        return Battle.Turn(hero, bonusturn);
    }
    public static boolean Turn (Character hero, boolean bonus) //what happens on a character's turn; returns true if game ends before a hero finishes their turn 
    {
        if (bonus==true)
        System.out.println(hero+" took a bonus turn!");
        if (hero.activeability!=null&&hero.activeability.channelled==true) //activeability set later/on previous turn, when chooseab is called
        {
            ArrayList<StatEff> toadd= new ArrayList<StatEff>();
            toadd=hero.activeability.ActivateChannelled(hero, hero.activeability); 
            if (toadd!=null) //channelled abs return null if ab could not be used due to targets dying during channel
            {
                for (Character target: hero.activeability.ctargets) 
                {
                    if (target!=null)
                    {
                        ArrayList<SpecialAbility> exception= new ArrayList<SpecialAbility>(); 
                        for (SpecialAbility h: target.helpers) //for helpers like redwing; removed after an attack so it can prevent status effect application during it
                        {
                            exception.add(h); 
                        }
                        for (SpecialAbility h: exception)
                        {
                            h.Undo (target); 
                        }
                    }
                }
            }
            if (toadd!=null&&hero.dead==false) //if they didn't die from any reflect or counterattack
            {
                if (toadd.size()>0) //apply stateffs to self
                {
                    for (StatEff eff: toadd)
                    {
                        eff.CheckApply(hero, hero, eff);
                    }
                }
            }
            if (hero.team1==true&&p2heroes==0)
            {
                Scoreboard2.UpdateScore(team1, team2); //show that all enemies are dead
                return true; //end the game because the enemy team died from the channelled ability; no point in taking a turn now
            }
            else if (hero.team1==false&&p1heroes==0)
            {
                Scoreboard2.UpdateScore(team1, team2);
                return true;
            }
        }
        if (!(hero.binaries.contains("Banished"))) //if channelled ab applies linked banish or hero is banished by passive after attacking with channelled ab, do not take turn
        {
            Ability activeAb=null;
            ArrayList<Character> targets= new ArrayList<Character>(); //chars to hit with ab
            ArrayList<StatEff> selfadd= new ArrayList<StatEff>(); //status effects for hero to apply to self
            hero.onTurnStart(); //tick down all regen and dot effs 
            Scoreboard2.UpdateScore(team1, team2); //show effects of dot dmg/regen
            if (hero.dead==false) //if they have not died from DoT damage
            {
                if (bonus==false) //makes distinction between bonus turns and normal turns primarily to avoid quicksilver taking infinite turns
                hero.onTurn(true);
                else
                hero.onTurn(false);
                Scoreboard2.UpdateScore(team1, team2);
                if (hero.team1==true&&p2heroes==0)
                return true; //end the game because the enemy team died from a passive or channelled ability, e.g. sandman's sandstorm
                else if (hero.team1==false&&p1heroes==0)
                return true; 
            }
            if (hero.dead==false) //if they have not died from onturn
            {
                boolean flag=false;
                ArrayList<StatEff> selfadd2= new ArrayList<StatEff>(); //to save unbound ab's stateffs to be applied at turn end
                while (flag==false) 
                {
                    activeAb=hero.ChooseAb(); //hero chooses an ab to use, which then becomes their activeability
                    if (activeAb==null) //hero chose to skip turn; end instantly since there's no ab to use
                    break;
                    else  
                    {
                        targets=Battle.ChooseTarget(hero, activeAb.friendly, activeAb.target); //choose targets and use the ab
                        selfadd2=activeAb.UseAb(hero, targets); //stateffs to apply to self
                        if (selfadd2!=null) //abs only return null if they can't be used due to a lack of targets; if null, restart the loop and choose something usable this time
                        {
                            if (activeAb.unbound==true) //update scoreboard to immediately see effect of used unbound ab, and keep looping and use another ab
                            {
                                if (selfadd2.size()>0&&hero.dead==false) //also, unbound buffs apply to self immediately, instead of after turn end
                                { 
                                    for (StatEff eff: selfadd2)
                                    {
                                        eff.CheckApply(hero, hero, eff);
                                    }
                                }
                                Scoreboard2.UpdateScore(team1, team2); 
                            }
                            else //end the loop and end turn; chooseab already prevents infinite spamming of unbound abs so there are no infinite turns 
                            {
                                flag=true; selfadd.addAll(selfadd2); //if not unbound, apply stateffs to self after turn end
                            }
                        }
                    }
                }
                if (hero.dead==false) //finally, hero ends their turn and applies stateffs from their abilities to self
                {
                    if (bonus==false)
                    hero.onTurnEnd(true);
                    else
                    hero.onTurnEnd(false);
                    if (selfadd.size()>0&&hero.dead==false) //if the ab buffs the hero, the effs must be added after their turn end so they don't prematurely tick down
                    {
                        for (StatEff eff: selfadd)
                        {
                            eff.CheckApply(hero, hero, eff); 
                        }
                    }
                }
                for (Character target: targets) 
                {
                    if (target!=null)
                    {
                        ArrayList<SpecialAbility> exception= new ArrayList<SpecialAbility>(); 
                        for (SpecialAbility h: target.helpers) //for helpers like redwing; removed after an attack so it can prevent status effect application during it
                        {
                            exception.add(h); 
                        }
                        for (SpecialAbility h: exception)
                        {
                            h.Undo (target); 
                        }
                    }
                }
                if (hero.team1==true&&p2heroes==0)
                return true; //do not trigger bonus turn if all enemies are dead, or else game won't be able to end
                else if (hero.team1==false&&p1heroes==0)
                return true; 
                ArrayList<SpecialAbility> exceptions= new ArrayList<SpecialAbility>(); //remove hero's used helpers/trigger a bonus turn
                for (SpecialAbility h: hero.helpers)
                {
                    exceptions.add(h); 
                }
                for (SpecialAbility h: exceptions)
                {
                    h.Undo (hero); 
                }
            }
        }
        else
        System.out.println ("\n"+hero+" skips their turn due to being Banished.");
        return false;
    }
    public static void BanishTurn (Character hero, Character enemy) //taking turn during linked banish
    {
        //channelled abilities are ignored and stay channelling until the banish ends since they activate on the hero's next turn, and they can't take turns while banished
        Ability activeAb=null;
        ArrayList<StatEff> selfadd= new ArrayList<StatEff>(); //status effects for hero to apply to self
        //status effects do not tick down while banished
        //on turn passives skipped because banished heroes cannot take turns
        ArrayList<StatEff> selfadd2=null; //to save unbound ab's stateffs to be applied at turn end
        while (true) 
        {
            activeAb=hero.ChooseAb(); //hero chooses an ab to use, which then becomes their activeability
            if (activeAb==null) //hero chose to skip turn; end instantly since there's no ab to use
            break;
            else  //only two ab options while banished: attack enemy or buff self
            {
                System.out.println ("\nChoose a target. Type the number that appears before their name."); 
                ArrayList<Character> param=new ArrayList<Character>(); 
                if ((activeAb.friendly.equals("enemy")&&!(activeAb.target.equals("AoE")))||(activeAb.friendly.equals("ally inclusive")&&!(activeAb.target.equals("AoE")))) 
                {
                    Character only=null;
                    if (activeAb.friendly.equals("enemy"))
                    only=enemy;
                    else 
                    only=hero;
                    System.out.println ("1. "+only); //providing the illusion of choice because not having it feels weird
                    do
                    {
                        int answer=Damage_Stuff.GetInput();
                        if (answer!=1)
                        {
                            
                        }
                        else
                        {
                            param.add(only); break;
                        }
                    }
                    while (true);     
                }
                else if (activeAb.friendly.equals("self")||(activeAb.friendly.equals("ally inclusive")&&activeAb.target.equals("AoE")))
                {
                    param.add(hero);
                }
                else if (activeAb.friendly.equals("enemy")&&activeAb.target.equals("AoE"))
                {
                    param.add(enemy);
                }
                else if (activeAb.friendly.equals("either")||activeAb.friendly.equals("both"))
                {
                    System.out.println("Will implement this function later since no currently playable characters even have abilities like this (4.4)."); break;
                }
                if (param.size()>0) //useab if it's valid
                {
                    selfadd2=activeAb.UseAb(hero, param); 
                }
                if (selfadd2!=null) //else selfadd2 stays null bc ab is invalid; hero must either choose something valid or skip turn to continue
                {
                    if (activeAb.unbound==true) 
                    {
                        if (selfadd2.size()>0&&hero.dead==false) 
                        { 
                            for (StatEff eff: selfadd2)
                            {
                                eff.CheckApply(hero, hero, eff);
                            }
                        }
                        Scoreboard2.UpdateScore(team1, team2); 
                    }
                    else //end the loop and end turn; chooseab already prevents infinite spamming of unbound abs so there are no infinite turns 
                    {
                        selfadd.addAll(selfadd2); //if not unbound, apply stateffs to self after turn end
                        break;
                    }
                }
            }
        }
        if (hero.dead==false) //can still die from counter, passives, etc; all of those still work normally
        {
            //no turn end since no turn was taken
            if (selfadd.size()>0&&hero.dead==false) 
            {
                for (StatEff eff: selfadd)
                {
                    eff.CheckApply(hero, hero, eff); //can still gain stateffs, they just do not tick down
                }
                Scoreboard2.UpdateScore(team1, team2); 
            }
        }
        ArrayList<SpecialAbility> exception= new ArrayList<SpecialAbility>(enemy.helpers); 
        for (SpecialAbility h: exception) //for helpers like redwing, if target had it
        {
            h.Undo (enemy); 
        }
        ArrayList<SpecialAbility> exceptions= new ArrayList<SpecialAbility>(hero.helpers); //then remove hero's used helpers too, if any
        for (SpecialAbility h: exceptions)
        {
            h.Undo (hero); 
        }
    }
    public static ArrayList ChooseTarget (Character hero, String friendly, String type) 
    {
        //friendly means ally, enemy, both inc/exc, either inc/exc, self, ally inc/exc
        //type is single, multi, random, or aoe, fixed
        Character[] targets= new Character[12];
        if (type.equalsIgnoreCase("self"))
        {
            ArrayList<Character> me= new ArrayList<Character>();
            me.add(hero);
            return me;
        }
        else if (type.equalsIgnoreCase("Single")||type.equalsIgnoreCase("Multitarget")||type.equalsIgnoreCase("rez"))
        {
            targets=Battle.TargetFilter(hero, friendly, type); //standard selection
        }
        else if (type.equalsIgnoreCase("AoE"))
        {
            if (friendly.equalsIgnoreCase("enemy"))
            {
                targets=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
            }
            else if (friendly.equalsIgnoreCase("ally inclusive"))
            {
                targets=Battle.GetTeam(hero.team1);
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                if (hero.team1==true&&p1solo==false)
                {
                    targets=Battle.GetTeammates(hero);
                }
                else if (hero.team1==false&&p2solo==false)
                {
                    targets=Battle.GetTeammates(hero);
                }
                else //there are no eligible targets so return nothing; ability is unsuable
                {
                    ArrayList<Character> knull= new ArrayList<Character>();
                    return knull;
                }
            }
        }
        else if (type.equals("lowest")||type.equals("missing"))
        {
            Character[] foes=null;
            if (friendly.equalsIgnoreCase("enemy"))
            {
                foes=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                foes=Battle.GetTeammates(hero);
            }
            else if (friendly.equalsIgnoreCase("ally inclusive"))
            {
                foes=Battle.GetTeam(hero.team1);
            }            
            int hp=6666; Character low=foes[0];
            for (Character c: foes)
            {
                if (c!=null)
                {
                    if (type.equals("lowest"))
                    {
                        int n=c.HP; 
                        if (n<hp)
                        {
                            hp=n; low=c;
                        }
                    }
                    else if (type.equals("missing"))
                    {
                        int n=c.maxHP-c.HP; 
                        if (n>0&&n<hp) //possible for maxhp to be below hp so ignore those cases
                        {
                            hp=n; low=c;
                        }
                    }
                }
            }
            targets[0]=low;
        }
        else if (type.substring(0, 6).equalsIgnoreCase ("random")) //substring shouldn't cause any index exceptions since it's checked last, after ones like aoe
        {
            if (type.substring(7).equals("Bleed")) //prioritises enemies with X; for heroes like phil urich
            {
                if (friendly.equalsIgnoreCase("enemy")) 
                {
                    boolean bleed=false;
                    if (hero.team1==true) //check if any enemies even have bleed so it can be prioritised
                    {
                        for (Character c: team2) 
                        {
                            if (c!=null&&c.CheckFor(type.substring(7), false)==true)
                            {
                                bleed=true; break;
                            }
                        }
                    }
                    else
                    {
                        for (Character c: team1)
                        {
                            if (c!=null&&c.CheckFor(type.substring(7), false)==true)
                            {
                                bleed=true; break;
                            }
                        }
                    }
                    if (bleed==true) //if someone has bleed, keep getting random enemies until one with bleed is selected to be target
                    {
                        Character t=null;
                        do
                        {
                            t=Ability.GetRandomHero(hero, hero, false, false);
                        }
                        while (t.CheckFor(type.substring(7), false)==false);
                        targets[0]=t;
                    }
                    else //if no one has bleed, then do normal random target
                    targets[0]=Ability.GetRandomHero(hero, hero, false, false);
                }
            }
            else //random 1, random 2, etc
            {
                int val=Integer.valueOf(type.substring(7));
                if (friendly.equalsIgnoreCase("enemy")) 
                {
                    for (int i=0; i<val; i++) //get as many random enemies as needed; for heroes like green goblin with multihit attacks where each hit has a different target
                    {
                        targets[i]=Ability.GetRandomHero(hero, hero, false, false);
                    }
                }
                else if (friendly.equalsIgnoreCase("ally exclusive"))
                {
                    for (int i=0; i<val; i++)
                    {
                        targets[i]=Ability.GetRandomHero(hero, hero, true, false);
                    }
                }
            }
        }
        ArrayList toret=CoinFlip.ToList(targets);
        return toret;
    }
    public static Character[] TargetFilter(Character hero, String friendly, String type) //used to let player choose target after accounting for targeting effects
    {
        Character[] list= new Character[12]; //contains all possible targets
        Character[] targets= new Character[6]; //the one that's returned; contains actual targets
        if (type.equalsIgnoreCase("rez")) //gets targets from the dead
        {
            ArrayList<Character> temp= new ArrayList<Character>(); 
            if (friendly.equalsIgnoreCase("ally exclusive")||friendly.equalsIgnoreCase("ally inclusive")) //as of 4.2, only allies and/or self can be rezzed by an ability
            {
                if (hero.team1==true)
                temp.addAll(Battle.team1dead);
                else //if (hero.team1==false)
                temp.addAll(Battle.team2dead);
                if (friendly.equalsIgnoreCase("ally inclusive"))
                temp.add(hero);
                int in=0; 
                for (Character c: temp) //convert array to list for target selection
                {
                    list[in]=c; in++; 
                    if (in>11) //list has max size of 12; there shouldn't ever be that many dead, but break just in case to avoid index exception
                    break;
                }
            }
        }
        else //normal target selection from living
        {
            if (friendly.equalsIgnoreCase("enemy"))
            {
                list=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
            }
            else if (friendly.equalsIgnoreCase("ally inclusive"))
            {
                list=Battle.GetTeam(hero.team1);
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                list=Battle.GetTeammates(hero);
            }
            else if (friendly.equalsIgnoreCase("both inclusive")||friendly.equalsIgnoreCase("either inclusive"))
            {
                Character[] pol=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
                Character[] pot=Battle.GetTeam(hero.team1);
                for (int i=0; i<6; i++)
                {
                    list[i]=pol[i];
                }
                for (int i=0; i<6; i++)
                {
                    list[i+6]=pot[i];
                }
            }
            else if (friendly.equalsIgnoreCase("both exclusive")||friendly.equalsIgnoreCase("either exclusive"))
            {
                Character[] pol=Battle.GetTeam(CoinFlip.TeamFlip(hero.team1));
                Character[] pot=Battle.GetTeammates(hero);
                for (int i=0; i<6; i++)
                {
                    list[i]=pol[i];
                }
                for (int i=0; i<6; i++)
                {
                    list[i+6]=pot[i];
                }
            }
        }
        System.out.println ("\nChoose a target. Type the number that appears before their name."); 
        if (friendly.equalsIgnoreCase("ally inclusive")||friendly.equalsIgnoreCase("ally exclusive")||type.equalsIgnoreCase("rez")) //choosing a teammate, or dead person
        {
            targets[0]=Card_Selection.ChooseTargetFriend(list); //doesn't account for targeting effects
        }
        else //choose an enemy after checking targeting effects, or choose a teammate if multitarget both/either; foe works either way
        {
            list=Card_Selection.ChooseTargetFoe(hero, list); 
            targets[0]=list[list.length-1];
        }
        if (type.equalsIgnoreCase("Multitarget"))
        {
            for (int i=0; i<list.length; i++)
            {
                if (list[i]==targets[0]) //character will be in list twice since targets[0] is stored in the last spot of the list
                {
                    list[i]=null; //cannot choose same target twice with Multitarget
                }
            }
            boolean valid=false;
            for (Character c: list)
            {
                if (c!=null) //there is at least one valid target remaining
                {
                    valid=true; break;
                }
            }
            if (valid==true)
            {
                switch (friendly)
                {
                    case "both inclusive": case "both exclusive": //both means one target from each team; one friendly and one enemy
                    if (targets[0].team1==hero.team1) //first target was an ally
                    {
                        System.out.println ("Choose a second target. Type the number that appears before their name.");
                        for (int i=0; i<list.length; i++)
                        {
                            if (list[i].team1==hero.team1) //second target must be an enemy, so remove allies from target list
                            list[i]=null;
                        }
                    }
                    else if (targets[0].team1!=hero.team1&&((hero.team1==true&&p1solo==false)||(hero.team1==false&&p2solo==false))) //first target was an enemy
                    {
                        System.out.println ("Choose a second target. Type the number that appears before their name."); //only print message if hero isnt alone on their team
                        for (int i=0; i<list.length; i++)
                        {
                            if (list[i].team1!=hero.team1) //second target must be an ally, so remove enemies from target list
                            list[i]=null;
                        }
                    }
                    list=Card_Selection.ChooseTargetFoe(hero, list); targets[1]=list[list.length-1];
                    break;
                    case "ally inclusive": case "ally exclusive": 
                    System.out.println ("Choose a second target. Type the number that appears before their name."); 
                    targets[1]=Card_Selection.ChooseTargetFriend(list); 
                    break;
                    case "enemy": case "either inclusive": case "either exclusive": 
                    System.out.println ("Choose a second target. Type the number that appears before their name."); 
                    list=Card_Selection.ChooseTargetFoe(hero, list); targets[1]=list[list.length-1];
                    break;
                }
            }
        }
        return targets;
    }
    public static Character[] GetTeam(boolean team) //returns all valid character on a team
    {
        //makes a copy to avoid aliasing problems
        Character[] targs= new Character[6]; int ind=0;
        if (team==true)
        {
            for (int i=0; i<6; i++)
            {
                if (team1[i]!=null&&!(team1[i].binaries.contains("Banished"))&&team1[i].dead==false)
                {
                    targs[ind]=team1[i];
                    ++ind;
                }
            }
        }
        else
        {
            for (int i=0; i<6; i++)
            {
                if (team2[i]!=null&&!(team2[i].binaries.contains("Banished"))&&team2[i].dead==false)
                {
                    targs[ind]=team2[i];
                    ++ind;
                }
            }
        }
        return targs;
    }
    public static Character[] GetTeammates (Character self) //returns all valid characters on a team, excluding the caller
    {
        if (self.team1==true) //team1
        {
            Character[] friends= new Character[6];
            int i=0;
            for (Character hero:team1) 
            {
                if (hero!=null&&hero.id!=self.id&&!(hero.binaries.contains("Banished"))&&hero.dead==false)
                {
                    friends[i]=hero;
                    ++i;
                }
            }
            return friends;
        }
        else //team2
        {
            Character[] foes= new Character[6];
            int i=0;
            for (Character hero:team2) 
            {
                if (hero!=null&&hero.id!=self.id&&!(hero.binaries.contains("Banished"))&&hero.dead==false) 
                {
                    foes[i]=hero;
                    ++i;
                }
            }
            return foes;
        }
    }
    public static void AddDead (Character dead) //altering team arrays to remove dead characters
    {
        Character[] newteam=new Character[6];
        int ind=0;
        if (dead.team1==true)
        {            
            team1dead.add(dead); 
            for (int i=0; i<6; i++)
            {
                if (team1[i]!=null&&team1[i]!=dead) //make new array with dead hero missing, instead of doing team[i]=null, to avoid bugs
                {
                    newteam[ind]=team1[i];
                    ++ind;
                }
            }
            team1=newteam; 
            p1teamsize-=dead.size;
            p1heroes--;
        }
        else
        {         
            team2dead.add(dead); 
            for (int i=0; i<6; i++)
            {
                if (team2[i]!=null&&team2[i]!=dead)
                {
                    newteam[ind]=team2[i];
                    ++ind;
                }
            }
            team2=newteam;
            p2teamsize-=dead.size;
            p2heroes--;
        }
    }
    public static void SummonSomeone (Character summoner, Summon friend) //checks if there's space on the team for a summon
    {
        int size=friend.size; 
        if (friend.team1==true) //should have been manually assigned when making the summon
        {
            if (p1teamsize+size<=6) //they cannot have more than 6 characters per team, or the equivalent
            {
                friend.passivefriend.add(0,summoner); //add, not set, since arraylist starts empty and technically has no index 0 yet
                Battle.AddSummon(friend);
                Character[] friends=Battle.GetTeammates(summoner); //after they're summoned, allies and enemies apply relevant passives
                for (Character prot: friends)
                {
                    if (prot!=null&&!(prot.binaries.contains("Banished")))
                    {
                        prot.onAllySummon(summoner, friend);
                    }
                }
                Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(friend.team1));
                for (Character ant: enemies)
                {
                    if (ant!=null&&!(ant.binaries.contains("Banished")))
                    {
                        ant.onEnemySummon(summoner, friend);
                    }
                }
            }
            else //if they are out of space, print error message
            {
                System.out.println ("Not enough space on Player 1's team for "+friend+". Summon failed."); 
            }
        }
        else ///they are on team 2
        {
            if (p2teamsize+size<=6)
            {
                friend.passivefriend.add(0,summoner); //add, not set, since arraylist starts empty and technically has no index 0 yet
                Battle.AddSummon(friend);
                Character[] friends=Battle.GetTeammates(summoner);
                for (Character prot: friends)
                {
                    if (prot!=null&&!(prot.binaries.contains("Banished")))
                    {
                        prot.onAllySummon(summoner, friend);
                    }
                }
                Character[] enemies=Battle.GetTeam(CoinFlip.TeamFlip(friend.team1));
                for (Character ant: enemies)
                {
                    if (ant!=null&&!(ant.binaries.contains("Banished")))
                    {
                        ant.onEnemySummon(summoner, friend);
                    }
                }
            }
            else
            {
                System.out.println ("Not enough space on Player 2's team for "+friend+". Summon failed."); 
            }
        }
    }
    public static void AddSummon (Summon friend) //add summon to the team array and activate their on summon ability
    {
        if (friend.team1==true)
        {
            for (int i=0; i<6; i++)
            {
                if (team1[i]==null) //add them to the first empty spot at the end of the turn order
                {
                    team1[i]=friend;
                    break;
                }
            }
            p1teamsize+=friend.size;
            p1heroes++;
        }
        else
        {
            for (int i=0; i<6; i++)
            {
                if (team2[i]==null)
                {
                    team2[i]=friend;
                    break;
                }
            }
            p2teamsize+=friend.size;
            p2heroes++;
        }
        System.out.println(friend.passivefriend.get(0)+" Summoned "+friend);
        friend.onSummon();
    }
    public static int CheckWin (int turn) //turn is tturns%2
    {
        //if both team's heroes are dead (e.g. a lone doombot suicide kills the last enemy), the winner is whoever's turn it is 
        if (turn!=0) //player 1's turn; always odd numbers, starting with turn 1
        {
            if (p2heroes==0)
            { 
                return 1; //player 1 is the winner
            }
        }
        else //if (turn==0) //player 2' turn; always an even number, starting with turn 2
        {
            if (p1heroes==0) 
            { 
                return 2; //player 2 wins
            }
        }
        if (p1heroes==0) 
        return 2; //player 2 wins
        else if (p2heroes==0)
        return 1;  
        else
        return 0; //no winner yet
    }
}