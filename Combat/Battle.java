
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
        Scoreboard.main(team1, team2);
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
            Scoreboard.UpdateScore(team1, team2); 
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
                        champions[P1active].remove(e.hashcode, "normal");
                    }
                    hero=champions[P1active];
                    fine=true;
                }
                else
                {
                    System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Banished.");
                    champions[P1active].BanishTick(); //ensures banished heroes take no turns but still have their banish duration reduced 
                    Battle.CheckActive(team);
                }
            }
            else if (champions[P1active].binaries.contains("Stunned")) //turn skip, but stateffs still tick
            {
                System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Stunned.");
                champions[P1active].onTurn(true);
                ArrayList<StatEff> modificationexception= new ArrayList<StatEff>(); modificationexception.addAll(champions[P1active].effects);
                for (StatEff e: modificationexception)
                {
                    e.onTurnStart(champions[P1active]);
                }
                if (champions[P1active]!=null) //since dying makes their spot in the team array null, a null check and an alive check are the same thing
                {
                    for (StatEff e: modificationexception)
                    {
                        e.onTurnEnd(champions[P1active]);
                    }
                }
                if (champions[P1active]!=null)
                {
                    champions[P1active].onTurnEnd(true);
                    for (Ability ab: champions[P1active].abilities)
                    {
                        if (ab!=null)
                        {
                            ab.CDReduction(1);
                        }
                    } 
                }
                Battle.CheckActive(team);
            }
            else if (champions[P1active]!=null) //can proceed to their turn
            {
                hero=champions[P1active];
                fine=true;
            }
            if (p2heroes==0) //for abilities like sandman's sandstorm that can potentially kill enemies in-between turns
            {
                Scoreboard.UpdateScore(team1, team2);
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
                        champions[P2active].remove(e.hashcode, "normal");
                    }
                    hero=champions[P2active];
                    fine=true;
                }
                else
                {
                    System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Banished.");
                    champions[P1active].BanishTick(); //ensures banished heroes take no turns but still have their banish duration reduced 
                    Battle.CheckActive(team);
                }
            }
            else if (champions[P2active].binaries.contains("Stunned"))
            {
                System.out.println ("\n"+champions[P2active].Cname+" skips their turn due to being Stunned.");
                champions[P2active].onTurn(true);
                ArrayList<StatEff> modificationexception= new ArrayList<StatEff>(); modificationexception.addAll(champions[P2active].effects);
                for (StatEff e: modificationexception)
                {
                    e.onTurnStart(champions[P2active]);
                }
                if (champions[P2active]!=null)
                {
                    for (StatEff e: modificationexception)
                    {
                        e.onTurnEnd(champions[P2active]);
                    }
                }
                if (champions[P2active]!=null)
                {
                    champions[P2active].onTurnEnd(true);
                    for (Ability ab: champions[P2active].abilities)
                    {
                        if (ab!=null)
                        {
                            ab.CDReduction(1);
                        }
                    } 
                }
                Battle.CheckActive(team);
            }
            else if (champions[P2active]!=null)
            {
                hero=champions[P2active];
                fine=true;
            }
            if (p1heroes==0) //for abilities like sandman's sandstorm that can potentially kill enemies in-between turns
            {
                Scoreboard.UpdateScore(team1, team2);
                return true;
            }
        }
        return Battle.Turn(hero, bonusturn);
    }
    public static boolean Turn (Character hero, boolean bonus) //what happens on a character's turn; returns true if game ends before a hero finishes their turn 
    {
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
                Scoreboard.UpdateScore(team1, team2); //show that all enemies are dead
                return true; //end the game because the enemy team died from the channelled ability; no point in taking a turn now
            }
            else if (hero.team1==false&&p1heroes==0)
            {
                Scoreboard.UpdateScore(team1, team2);
                return true;
            }
        }
        ArrayList<StatEff>concurrent= new ArrayList<StatEff>(); //to avoid concurrent modification exception
        if (hero.effects.size()!=0) //DoT effs tick if they have any
        {
            concurrent.addAll(hero.effects);
            for (StatEff eff: concurrent)
            {
                eff.onTurnStart(hero); 
            }  
        }   
        Ability activeAb=null;
        ArrayList<Character> targets= new ArrayList<Character>(); //chars to hit with ab
        ArrayList<StatEff> selfadd= new ArrayList<StatEff>(); //status effects for hero to apply to self
        Scoreboard.UpdateScore(team1, team2);
        if (hero.dead==false) //if they have not died from DoT damage
        {
            if (bonus==false) //makes distinction between bonus turns and normal turns to avoid quicksilver taking infinite turns
            hero.onTurn(true);
            else
            hero.onTurn(false);
            Scoreboard.UpdateScore(team1, team2);
            if (hero.team1==true&&p2heroes==0)
            return true; //end the game because the enemy team died from a passive or ability, like sandman's sandstorm
            else if (hero.team1==false&&p1heroes==0)
            return true; 
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
                    selfadd2=activeAb.UseAb(hero, activeAb, targets);
                    if (selfadd2!=null) //abs only return null if they can't be used due to a lack of targets; if null, restart the loop and choose something usable this time
                    {
                        selfadd.addAll(selfadd2);
                        if (activeAb.unbound==true) //update scoreboard to immediately see effect of used unbound ab, and keep looping and use another ab
                        Scoreboard.UpdateScore(team1, team2); 
                        else //end the loop and end turn; chooseab already prevents infinite spamming of unbound abs so there are no infinite turns 
                        flag=true;
                    }
                }
            }
            ArrayList<StatEff>modexception= new ArrayList<StatEff>(); //to avoid concurrent modification exception
            if (hero.effects.size()!=0) //if they have any non-DoT stateffs, they tick down
            {
                modexception.addAll(hero.effects);
                for (StatEff eff: modexception)
                {
                    eff.onTurnEnd(hero);
                } 
            }
            for (Ability ab: hero.abilities) //after turn end, all the hero's cds tick down
            {
                if (ab!=null)
                {
                    ab.CDReduction(1);
                }
            } 
            if (hero.dead==false) //finally, hero ends their turn
            {
                if (bonus==false)
                hero.onTurnEnd(true);             
                else
                hero.onTurnEnd(false);   
                if (selfadd.size()>0) //if the ab buffs the hero, the effs must be added after their turn end so they don't prematurely tick down
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
            ArrayList<SpecialAbility> exceptions= new ArrayList<SpecialAbility>(); //then remove hero's used helpers too, or trigger a bonus turn
            for (SpecialAbility h: hero.helpers)
            {
                exceptions.add(h); 
            }
            for (SpecialAbility h: exceptions)
            {
                h.Undo (hero); 
            }
        }
        return false;
    }
    public static ArrayList ChooseTarget (Character hero, String friendly, String type) 
    {
        //friendly means (single) ally, enemy, both, either, self, ally inc(lusive), ally exc(lusive)
        //type is single, multi, random, or aoe, fixed
        Character[] targets= new Character[6];
        if (type.equalsIgnoreCase("Single")||type.equalsIgnoreCase("Multitarget"))
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
        else if (type.equalsIgnoreCase ("random"))
        {
            if (friendly.equalsIgnoreCase("enemy"))
            {
                targets[0]=Ability.GetRandomHero(hero, hero, false, false);
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                targets[0]=Ability.GetRandomHero(hero, hero, true, false);
            }
        }
        else if (type.equals("lowest"))
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
            int hp=6666; Character low=null;
            for (Character i: foes)
            {
                if (i!=null)
                {
                    int n=i.HP; 
                    if (n<hp)
                    {
                        hp=n; low=i;
                    }
                }
            }
            targets[0]=low;
        }
        ArrayList toret=CoinFlip.ToList(targets);
        if (type.equalsIgnoreCase("self"))
        {
            ArrayList<Character> me= new ArrayList<Character>();
            me.add(hero);
            return me;
        }
        else
        {
            return toret;
        }
    }
    public static Character[] TargetFilter(Character hero, String friendly, String type)
    {
        //used to let player choose target, after accounting for targeting effects
        Character[] targets= new Character[6];
        Character[] list= new Character[6]; //in future will need method to switch list into size 12 in case of friendly being both or either
        if (friendly.equalsIgnoreCase("enemy"))
        {
            boolean meat=CoinFlip.TeamFlip(hero.team1);
            list=Battle.GetTeam(meat); 
        }
        else if (friendly.equalsIgnoreCase("ally inclusive"))
        {
            list=Battle.GetTeam(hero.team1);
        }
        else if (friendly.equalsIgnoreCase("ally exclusive"))
        {
            list=Battle.GetTeammates(hero);
        }
        System.out.println ("\nChoose a target. Type the number that appears before their name.");
        if (!(friendly.equalsIgnoreCase("enemy"))) //choosing a teammate
        {
            int select=Card_Selection.ChooseTargetFriend(list);
            if (select!=616)
            {
                targets[0]=list[select];
            }
        }
        else //choose an enemy after checking targeting effects
        {
            Character select=Card_Selection.ChooseTargetFoe(hero, list);
            targets[0]=select;
        }
        if (type.equalsIgnoreCase("Multitarget"))
        {
            System.out.println ("Choose a second target. Type the number that appears before their name.");
            switch (friendly)
            {
                case "Both exclusive": break;
                case "Both inclusive": break;
                case "Ally inclusive": break;
                case "Ally exclusive": break;
                case "Enemy": break;
            }
            //targets[1]= the second target
        }
        return targets;
    }
    public static Character[] GetTeam(boolean team)
    {
        //this makes a copy to avoid sending over the team itself, as that would cause problems
        Character[] targs= new Character[6]; int ind=0;
        if (team==true)
        {
            for (int i=0; i<6; i++)
            {
                if (team1[i]!=null)
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
                if (team2[i]!=null)
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
                if (hero!=null&&hero.hash!=self.hash&&!(hero.binaries.contains("Banished"))&&hero.dead==false)
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
                if (hero!=null&&hero.hash!=self.hash&&!(hero.binaries.contains("Banished"))&&hero.dead==false) 
                {
                    foes[i]=hero;
                    ++i;
                }
            }
            return foes;
        }
    }
    public static void Snared (Character snared) //losing speed and applying snare
    {
        int index=0, oindex=0; 
        if (snared.team1==true&&p1solo==false) //speed and snare are useless if there's only one hero
        {
            for (int i=0; i<6; i++) //need to find out hero's position in their team
            {
                if (team1[i]==snared)
                {
                    index=i; oindex=i; break;
                }
            }
            if (index!=(p1heroes-1)) //the hero isn't already last in their turn order; move them down one in the new turn order
            {
                Character[] buck=new Character[6];
                team1[index]=null;
                index+=1;   
                buck[index]=snared;
                ArrayList<Character> jimbom= CoinFlip.ToList(team1); //convert the team to a list and remove the nulls
                ArrayList<Character> killers=new ArrayList<Character>();
                for (Character c: jimbom)
                {
                    if (c==null)
                    killers.add(c);
                }
                jimbom.removeAll(killers);
                for (int e=0; e<6; e++) //snared's teammates fill in the blank spots in the new array in the same order they take turns, leaving snared's place intact
                {
                    if (buck[e]==null&&jimbom.size()>0)
                    {
                        buck[e]=jimbom.get(0); jimbom.remove(0);
                    }
                }
                team1=buck; 
                team1=NullShift(team1);
                if (team2[P1active]==snared) 
                Battle.CheckActive(true);
            }
            else //the hero already goes last
            {
                Character[] buck=new Character[6];
                buck[0]=snared; //hero already goes last, so the snare now makes them go first
                team1[index]=null;
                for (int e=1; e<6; e++)
                {
                    buck[e]=team1[e-1];
                }
                team1=buck; 
                do //this bypasses the usual playerturn method to skip the snared hero so the next one in the turn order goes instead, to avoid the snared one taking 2 turns
                {
                    Battle.CheckActive(true); 
                } 
                while(team2[P1active]==null); //skip the null slots until the next hero in turn order is located
                if (team2[P1active]==snared) 
                Battle.CheckActive(true);
            }
        }
        else if (snared.team1==false&&p2solo==false) 
        {
            for (int i=0; i<6; i++) 
            {
                if (team2[i]==snared)
                {
                    index=i; oindex=i; break;
                }
            }
            if (index!=(p2heroes-1)) 
            {
                Character[] buck=new Character[6];
                team2[index]=null;
                index+=1;   
                buck[index]=snared;
                ArrayList<Character> jimbom= CoinFlip.ToList(team2); //convert the team to a list and remove the nulls
                ArrayList<Character> killers=new ArrayList<Character>();
                for (Character c: jimbom)
                {
                    if (c==null)
                    killers.add(c);
                }
                jimbom.removeAll(killers);
                for (int e=0; e<6; e++) //snared's teammates fill in the blank spots in the new array in the same order they take turns, leaving snared's place intact
                {
                    if (buck[e]==null&&jimbom.size()>0)
                    {
                        buck[e]=jimbom.get(0); jimbom.remove(0);
                    }
                }
                team2=buck; 
                team2=NullShift(team2);
                if (team2[P2active]==snared) 
                Battle.CheckActive(false);
            }
            else //the hero already goes last
            {
                Character[] buck=new Character[6];
                buck[0]=snared; //hero already goes last, so the snare now makes them go first
                team2[index]=null;
                for (int e=1; e<6; e++)
                {
                    buck[e]=team2[e-1];
                }
                team2=buck; 
                do //this bypasses the usual playerturn method to skip the snared hero so the next one in the turn order goes instead, to avoid the snared one taking 2 turns
                {
                    Battle.CheckActive(false); 
                } 
                while(team2[P2active]==null); //skip the null slots until the next hero in turn order is located
                if (team2[P2active]==snared) 
                Battle.CheckActive(false);
            }
        }
    }
    public static void Speeded (Character sped) //losing snare and applying speed
    {
        int index=0, oindex=0; 
        if (sped.team1==true&&p1solo==false)
        {
            for (int i=0; i<6; i++) //need to find out hero's position in their team
            {
                if (team1[i]==sped)
                {
                    index=i; oindex=i; break;
                }
            }
            if (index!=0) //then move them up one in the new turn order
            {
                index-=1;
            }
            else //the hero already goes first
            {
                index=p1heroes-1; //the number of characters is how many turns they have to wait until they take their next one; with speed, it's one less
            }
            //swapping places with the next person in the turn order caused a ton of bugs, so this now works differently
            Character[] buck=new Character[6];
            buck[index]=sped; //the sped up character's place is predetermined and placed in a new array
            team1[oindex]=null; 
            ArrayList<Character> jimbom= CoinFlip.ToList(team1); //convert the team to a list and remove the nulls
            ArrayList<Character> killers=new ArrayList<Character>();
            for (Character c: jimbom)
            {
                if (c==null)
                killers.add(c);
            }
            jimbom.removeAll(killers);
            for (int e=0; e<6; e++) //sped's teammates fill in the blank spots in the new array in the same order they take turns, leaving sped's place intact
            {
                if (buck[e]==null&&jimbom.size()>0)
                {
                    buck[e]=jimbom.get(0); jimbom.remove(0);
                }
            }
            team1=buck; 
            team1=NullShift(team1);
        }
        else if (sped.team1==false&&p2solo==false) //speed and snare are useless if there's only one hero
        {
            for (int i=0; i<6; i++) //need to find out hero's position in their team
            {
                if (team2[i]==sped)
                {
                    index=i; oindex=i; break;
                }
            }
            if (index!=0) //then move them up one in the new turn order
            {
                index-=1;
            }
            else //the hero already goes first
            {
                index=p2heroes-1; //the number of characters is how many turns they have to wait until they take their next one; with speed, it's one less
            }
            //swapping places with the next person in the turn order caused a ton of bugs, so this now works differently
            Character[] buck=new Character[6];
            buck[index]=sped; //the sped up character's place is predetermined and placed in a new array
            team2[oindex]=null; 
            ArrayList<Character> jimbom= CoinFlip.ToList(team2); //convert the team to a list and remove the nulls
            ArrayList<Character> killers=new ArrayList<Character>();
            for (Character c: jimbom)
            {
                if (c==null)
                killers.add(c);
            }
            jimbom.removeAll(killers);
            for (int e=0; e<6; e++) //sped's teammates fill in the blank spots in the new array in the same order they take turns, leaving sped's place intact
            {
                if (buck[e]==null&&jimbom.size()>0)
                {
                    buck[e]=jimbom.get(0); jimbom.remove(0);
                }
            }
            team2=buck; 
            team2=NullShift(team2);
        }
    }
    public static void AddDead (Character dead) //altering team arrays to remove dead characters
    {
        if (dead.team1==true)
        {            
            team1dead.add(dead); 
            for (int i=0; i<6; i++)
            {
                if (team1[i]!=null&&team1[i]==dead) //make their spot in their team array null
                {
                    team1[i]=null;
                    break;
                }
            }
            team1=NullShift(team1); 
            p1teamsize-=dead.size;
            p1heroes--;
        }
        else
        {         
            team2dead.add(dead); 
            for (int i=0; i<6; i++)
            {
                if (team2[i]!=null&&team2[i]==dead)
                {
                    team2[i]=null;
                    break;
                }
            }
            team2=NullShift(team2);
            p2teamsize-=dead.size;
            p2heroes--;
        }
    }
    public static Character[] NullShift (Character[] team) //after character dies, move others up in team array to put the nulls at the bottom/end
    {
       Character[] newteam= new Character[6]; 
       int index=0;
       for (int count=0; count<6; ++count) 
       {
           if (team[count]!=null)
           { 
               newteam[index]=team[count];
               ++index;
           }
       }
       return newteam;
    }
    public static void SummonSomeone (Character summoner, Summon friend) //checks if there's space on the team for a summon
    {
        int size=friend.size; 
        if (friend.team1==true) //should have been manually assigned when making the summon
        {
            if (p1teamsize<6&&(p1teamsize+size)<=6) //they cannot have more than 6 characters per team, or the equivalent
            {
                friend.passivefriend[0]=summoner;
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
                System.out.println ("Player 1 max team size reached. Summon failed."); 
            }
        }
        else ///they are on team 2
        {
            if (p2teamsize<6&& (p2teamsize+size)<=6) 
            {
                friend.passivefriend[0]=summoner;
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
                System.out.println ("Player 2 max team size reached. Summon failed."); 
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
        System.out.println(friend.passivefriend[0].Cname+" Summoned "+friend.Cname);
        friend.onSummon();
    }
    public static int CheckWin (int turn) //turn is tturns%2
    {
        //if both team's heroes are dead (e.g. a lone ultron drone kills the last enemy), the winner is whoever's turn it is 
        if (turn!=0) //player 1's turn; always odd numbers, starting with turn 1
        {
            if (p2heroes==0)
            { 
                return 1; //player 1 is the winner
            }
        }
        else if (turn==0) //player 2' turn; always an even number, starting with turn 2
        {
            if (p1heroes==0) 
            { 
                return 2; //player 2 wins
            }
        }
        else
        {
            if (p1heroes==0) 
            return 2; //player 2 wins
            else if (p2heroes==0)
            return 1;
        }
        return 0; //no winner yet
    }
}