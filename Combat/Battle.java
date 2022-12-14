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
    static ArrayList <Character> transformed= new ArrayList <Character>(); //keep a copy of the transformed characters so their cooldowns aren't reset
    static int p1teamsize=0, p2teamsize=0; //size of team
    static int p1heroes=3, p2heroes=3; //number of characters on the team
    static int P1active=0, P2active=0; //active character's array index number
    final static int maxteamsize=6;
    public static boolean main (Character Char11, Character Char12, Character Char13, Character Char21, Character Char22, Character Char23)
    {
        int tturns=1;  
        int round=0; //variable for keeping track of rounds for TTK sake
        boolean gamewinner=false;
        boolean Pwinner=true; //true for player 1, and false for player 2
        FightStart(Char11, Char12, Char13, Char21, Char22, Char23); //allows certain passives to take effect
        Scoreboard.main(team1, team2);
        int winner=91;
        while (gamewinner==false)
        {
            if (tturns%2!=0)
            {
                PlayerTurn(team1, true);
                Battle.CheckActive(true);
                Scoreboard.UpdateScore(team1, team2); 
                winner=CheckWin();
                if (winner==1)
                {
                    Pwinner=true; gamewinner=true;
                    break;
                }
                if (winner==2)
                {
                    Pwinner=false; gamewinner=true;
                    break;
                }
                //also remove banish on any character who is alone on their team                               
            }
            else if (tturns%2==0)
            {
                PlayerTurn(team2, false);
                Battle.CheckActive(false);
                Scoreboard.UpdateScore(team1, team2);
                winner=CheckWin();
                if (winner==1)
                {
                    Pwinner=true; gamewinner=true;
                    break;
                }
                if (winner==2)
                {
                    Pwinner=false; gamewinner=true;
                    break;
                }                
                //also remove banish on any character who is alone on their team                
            }
            ++tturns; 
        }
        /*
        System.out.println ("\nTotal turns taken: "+tturns); //for data gathering purposes; 28, 30
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
    public static void CheckActive(boolean team) //moves through the team array to determine whose turn it is
    {
        if (team==true)
        {
            ++P1active;
            if (P1active>=6) //avoiding index exceptions since 1995
            {
                P1active=0;
            }
        }
        else
        {
            ++P2active;
            if (P2active>=6)
            {
                P2active=0;
            }
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
        p1teamsize+=(Char11.size+Char12.size+Char13.size);
        p2teamsize+=(Char21.size+Char22.size+Char23.size);
        team1=SetTurnOrder(Char11, Char12, Char13);
        team2=SetTurnOrder(Char21, Char22, Char23);
        Char11.onFightStart(Char11); 
        Char21.onFightStart(Char21); 
        Char12.onFightStart(Char12); 
        Char22.onFightStart(Char22); 
        Char13.onFightStart(Char13); 
        Char23.onFightStart(Char23); 
    }
    public static Character[] SetTurnOrder (Character one, Character two, Character three)
    {
        Character[] team= new Character[6];
        team[0]=one; one.Torder=1;
        team[1]=two; two.Torder=2;
        team[2]=three; three.Torder=3;
        return team;
    }
    public static void PlayerTurn (Character[] champions, boolean affil)
    {
        boolean fine=false; Character hero=null;
        while (affil==true&&fine==false) //ensures hero is usable
        {
            if (champions[P1active]==null)
            {
                Battle.CheckActive(affil); 
            }
            else if (champions[P1active].binaries.contains("Banished"))
            {
                System.out.println ("\n"+champions[P1active].Cname+" skips their turn due to being Banished.");
                champions[P1active].BanishTick(champions[P1active]); //ensures banished heroes take no turns but still have their banish duration reduced 
                Battle.CheckActive(affil);
            }
            else if (champions[P1active]!=null)
            {
                hero=champions[P1active];
                fine=true;
            }
        }
        while (affil==false&&fine==false)
        {
            if (champions[P2active]==null)
            {  
                Battle.CheckActive(affil);
            }
            else if (champions[P2active].CheckFor(champions[P2active], "Banished")==true)
            {
                System.out.println ("\n"+champions[P2active].Cname+" skips their turn due to being Banished.");
                champions[P2active].BanishTick(champions[P2active]); 
                Battle.CheckActive(affil);
            }
            else if (champions[P2active]!=null)
            {
                hero=champions[P2active];
                fine=true;
            }
        }
        if (hero.activeability[0]!=null)
        {
            hero.activeability[0].ActivateChannelled();
        }
        ArrayList<StatEff>concurrent= new ArrayList<StatEff>();
        if (hero.effects.size()!=0)
        {
            concurrent.addAll(hero.effects);
        }
        for (StatEff eff: concurrent)
        {
            eff.onTurnStart(hero);
        }         
        if (hero.dead==false) //if they have not died from DoT damage
        {
            Ability activeAb=null;
            ArrayList<Character> targets= new ArrayList<Character>();
            ArrayList<StatEff> toapply= new ArrayList<StatEff>(); 
            ArrayList<StatEff> selfadd= new ArrayList<StatEff>(); //status effects for hero to apply to self
            if (!(hero.binaries.contains("Stunned")))
            {
                hero.onTurn(hero, true);
                Scoreboard.UpdateScore(team1, team2);
                activeAb=hero.ChooseAb(hero);
                if (activeAb!=null)
                {
                    targets=Battle.ChooseTarget(hero, activeAb.friendly, activeAb.target);        
                    selfadd=activeAb.UseAb(hero, activeAb, targets);
                }
            }
            else
            {
                System.out.println("\n"+hero.Cname+" skips their turn due to being Stunned.");
            }
            ArrayList<StatEff>modexception= new ArrayList<StatEff>();
            if (hero.effects.size()!=0)
            {
                modexception.addAll(hero.effects);
            }
            for (StatEff eff: modexception)
            {
                eff.onTurnEnd(hero);
            } 
            toapply.addAll(selfadd); //must be added after turn end so they don't prematurely tick
            if (toapply.size()>0&&hero.dead==false) 
            {
                for (StatEff eff: toapply)
                {
                    eff.CheckApply(hero, hero, eff);
                }
            }
            for (Character target: targets) 
            {
                ArrayList<SpecialAbility> exception= new ArrayList<SpecialAbility>();
                if (target!=null)
                {                    
                    for (SpecialAbility h: target.helpers) //for special things like redwing; happens after the attack to prevent status effect application 
                    {
                        exception.add(h); 
                    }
                    for (SpecialAbility h: exception)
                    {
                        h.Undo (target); 
                    }
                }
            }
            for (Ability ab: hero.abilities)
            {
                if (ab!=null)
                {
                    ab.CDReduction(1);
                }
            } 
            if (hero.dead==false)
            {
                hero.OnTurnEnd(hero, true);             
            }
            ArrayList<SpecialAbility> exceptional= new ArrayList<SpecialAbility>();
            for (SpecialAbility h: hero.helpers)
            {
                exceptional.add(h); 
            }
            for (SpecialAbility h: exceptional)
            {
                h.Undo (hero); 
            }
        }
    }
    public static void BonusTurn (Character hero)
    { 
        System.out.println ("\n"+hero.Cname+" takes a bonus turn");
        if (hero.activeability[0]!=null)
        {
            hero.activeability[0].ActivateChannelled();
        }
        ArrayList<StatEff>concurrent= new ArrayList<StatEff>();
        if (hero.effects.size()!=0)
        {
            concurrent.addAll(hero.effects);
        }
        for (StatEff eff: concurrent)
        {
            eff.onTurnStart(hero);
        }         
        if (hero.dead==false) //if they have not died from DoT damage
        {
            Ability activeAb=null; 
            ArrayList<Character> targets= new ArrayList<Character>();
            ArrayList<StatEff> toapply= new ArrayList<StatEff>(); //status effects for hero to apply to the target
            ArrayList<StatEff> selfadd= new ArrayList<StatEff>(); //status effects for hero to apply to self
            if (!(hero.binaries.contains("Stunned")))
            {
                hero.onTurn(hero, false);
                Scoreboard.UpdateScore(team1, team2);
                activeAb=hero.ChooseAb(hero);
                if (activeAb!=null)
                {
                    targets=Battle.ChooseTarget(hero, activeAb.friendly, activeAb.target);        
                    selfadd=activeAb.UseAb(hero, activeAb, targets);
                }
            }
            else
            {
                System.out.println("\n"+hero.Cname+" skips their turn due to being Stunned.");
            }
            ArrayList<StatEff>modexception= new ArrayList<StatEff>();
            if (hero.effects.size()!=0)
            {
                modexception.addAll(hero.effects);
            }
            for (StatEff eff: modexception)
            {
                eff.onTurnEnd(hero);
            } 
            toapply.addAll(selfadd); //must be added after turn end so they don't prematurely tick
            if (toapply.size()>0&&hero.dead==false) 
            {
                for (StatEff eff: toapply)
                {
                    eff.CheckApply(hero, hero, eff);
                }
            }
            for (Character target: targets) 
            {
                ArrayList<SpecialAbility> exception= new ArrayList<SpecialAbility>();
                if (target!=null)
                {                    
                    for (SpecialAbility h: target.helpers) //for special things like redwing; happens after the attack to prevent status effect application 
                    {
                        exception.add(h); 
                    }
                    for (SpecialAbility h: exception)
                    {
                        h.Undo (target); 
                    }
                }
            }
            for (Ability ab: hero.abilities)
            {
                if (ab!=null)
                {
                    ab.CDReduction(1);
                }
            } 
            if (hero.dead==false)
            {
                hero.OnTurnEnd(hero, true);             
            }
            ArrayList<SpecialAbility> exceptional= new ArrayList<SpecialAbility>();
            for (SpecialAbility h: hero.helpers)
            {
                exceptional.add(h); 
            }
            for (SpecialAbility h: exceptional)
            {
                h.Undo (hero); 
            }
        }
    }
    public static ArrayList ChooseTarget (Character hero, String friendly, String type) 
    {
        //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
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
                targets=Battle.GetTeammates(hero, Card_CoinFlip.TeamFlip(hero.team1));
            }
            else if (friendly.equalsIgnoreCase("ally inclusive"))
            {
                targets=Battle.GetTeammates(hero, hero.team1);
                for (int i=0; i<6; i++)
                {
                    if (targets[i]==null)
                    {
                        targets[i]=hero;
                        break;
                    }
                }
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                if (hero.team1==true&&p1heroes>1)
                {
                    targets=Battle.GetTeammates(hero, hero.team1);
                }
                else if (hero.team1==false&&p2heroes>1)
                {
                    targets=Battle.GetTeammates(hero, hero.team1);
                }
                else //there are no eligible targets so return nothing; ability is unsuable
                {
                    ArrayList<Character> knull= new ArrayList<Character>();
                    return knull;
                }
            }
        }
        else if (type.equalsIgnoreCase ("random target"))
        {
            if (friendly.equalsIgnoreCase("enemy"))
            {
                for (int i=0; i<6; i++)
                {
                    targets[i]=Ability.GetRandomEnemy(hero, false);
                }
            }
            else if (friendly.equalsIgnoreCase("ally exclusive"))
            {
                for (int i=0; i<6; i++)
                {
                    targets[i]=Ability.GetRandomEnemy(hero, true);
                }
            }
        }
        ArrayList toret=Card_CoinFlip.ToList(targets);
        if (type.equalsIgnoreCase("none")||type.equalsIgnoreCase("self"))
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
            boolean meat=Card_CoinFlip.TeamFlip(hero.team1);
            list=Battle.GetTeam(meat); 
        }
        else if (friendly.equalsIgnoreCase("ally inclusive"))
        {
            list=Battle.GetTeam(hero.team1);
        }
        else if (friendly.equalsIgnoreCase("ally exclusive"))
        {
            list=Battle.GetTeammates(hero, hero.team1);
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
    public static int GetTeamSize(boolean team)
    {
        if (team==true)
        {
            return p1teamsize;
        }
        else
        {
            return p2teamsize;
        }
    }
    public static int GetNumOfHeroes (boolean team)
    {
        if (team==true)
        {
            return p1heroes;
        }
        else
        {
            return p2heroes;
        }
    }
    public static Character[] GetTeammates (Character work, boolean team)
    {
        if (team==true) //team1
        {
            ArrayList <Character> friends= new ArrayList <Character>();
            for (Character text:team1) 
            {
                if (text!=null&&text.hash!=work.hash&&(text.CheckFor(text, "Banish")==false)&&text.dead==false)
                {
                    friends.add(text);
                }
            }
            Character[] foes= new Character[6];
            int i=0;
            for (Character text:friends) 
            {
                if (text!=null)
                {
                    foes[i]=text;
                    ++i;
                }
            }
            return foes;
        }
        else 
        {
            ArrayList <Character> friends= new ArrayList <Character>();
            for (Character text:team2) 
            {
                if (text!=null&&text.hash!=work.hash&&(text.CheckFor(text, "Banish")==false)&&text.dead==false) //excludes the caller, so they won't apply their ability to themself
                {
                    friends.add(text);
                }
            }
            Character[] foes= new Character[6];
            int i=0;
            for (Character text:friends) 
            {
                if (text!=null)
                {
                    foes[i]=text;
                    ++i;
                }
            }
            return foes;
        }
    }
    public static void SnareTime (Character snared) //losing speed and applying snare
    {
        //work in progress 
    }
    public static void SpeedTime (Character sped) //losing snare and applying speed
    {
        //work in progress
    }
    public static void AddDead (Character dead)
    {
        if (dead.team1==true)
        {            
            for (int i=0; i<6; i++)
            {
                if (team1[i]!=null&&team1[i].hash==dead.hash) 
                {
                    team1[i]=null;
                    break;
                }
            }
            for (Character de: team1)
            {
                if (de!=null&&de.Torder>dead.Torder)
                {
                    de.Torder-=1; //e.g. if the character in front of them was second in turn order and they were third, they'd now be second (3-1)
                }
            }
            team1dead.add(dead); team1=NullShift(team1); 
            p1teamsize-=dead.size;
            p1heroes--;
        }
        else
        {            
            for (int i=0; i<6; i++)
            {
                if (team2[i]!=null&&team2[i].hash==dead.hash)
                {
                    team2[i]=null;
                    break;
                }
            }
            for (Character de: team2)
            {
                if (de!=null&&de.Torder>dead.Torder)
                {
                    de.Torder-=1;
                }
            }
            team2dead.add(dead); team2=NullShift(team2);
            p2teamsize-=dead.size;
            p2heroes--;
        }
    }
    public static Character[] NullShift (Character[] team)
    {
       Character[] newteam= new Character[6]; int ac=0;
       for (int count=0; count<6; ++count) //fill the empty spaces in the array
       {
           if (team[count]!=null)
           { 
               newteam[ac]=team[count];
               ++ac;
           }
       }
       return newteam;
    }
    public static void SummonSomeone (Character summoner, Summon friend) //checks if there's space on the team for a summon
    {
        int size=friend.SetSizeSum(friend.index); 
        friend.size=size;
        if (summoner.team1==true) //they're on team 1
        {
            if (p1teamsize<6&&(p1teamsize+size)<=6) //they cannot have more than 6 characters per team, or the equivalent
            {
                friend.team1=true;
                friend.mysummoner=summoner;
                Battle.AddSummon(friend);
                Character[] friends=Battle.GetTeammates(summoner, friend.team1);
                for (Character prot: friends)
                {
                    if (prot!=null&&!(prot.binaries.contains("Banished")))
                    {
                        prot.onAllySummon(summoner, friend);
                    }
                }
                Character[] enemies=Battle.GetTeammates(summoner, Card_CoinFlip.TeamFlip(friend.team1));
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
                if (friend.index!=9) //to prevent spam with Baron Strucker's legion of HYDRA troopers
                {
                    System.out.println ("Player 1 max team size reached. Summon failed."); 
                }
            }
        }
        else ///they are on team 2
        {
            if (p2teamsize<6&& (p2teamsize+size)<=6) 
            {
                friend.team1=false;
                friend.mysummoner=summoner;
                Battle.AddSummon(friend);
                Character[] friends=Battle.GetTeammates(summoner, friend.team1);
                for (Character prot: friends)
                {
                    if (prot!=null&&prot.CheckFor(prot, "Banish")==false)
                    {
                        prot.onAllySummon(summoner, friend);
                    }
                }
                Character[] enemies=Battle.GetTeammates(summoner, Card_CoinFlip.TeamFlip(friend.team1));
                for (Character ant: enemies)
                {
                    if (ant!=null&&ant.CheckFor(ant, "Banish")==false)
                    {
                        ant.onEnemySummon(summoner, friend);
                    }
                }
            }
            else
            {
                if (friend.index!=9)
                {
                    System.out.println ("Player 2 max team size reached. Summon failed."); 
                }
            }
        }
    }
    public static void AddSummon (Summon friend) //add summon to the team and activate their on summon ability
    {
        if (friend.team1==true)
        {
            for (int i=0; i<6; i++)
            {
                if (team1[i]==null)
                {
                    team1[i]=friend;
                    friend.Torder=i+1;
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
                    friend.Torder=i+1;
                    break;
                }
            }
            p2teamsize+=friend.size;
            p2heroes++;
        }
        System.out.println(friend.mysummoner.Cname+" Summoned "+friend.Cname);
        friend.onSummon(friend);
    }
    public static int CheckWin ()
    {
        if (p1heroes==0) //all 6 characters on team 1 are dead
        {
            return 2; //player 2 wins
        }
        else if (p2heroes==0)
        {
            return 1;
        }
        else
        {
            return 0; //no winner yet
        }
    }
}