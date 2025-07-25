package myMarvelcardgamepack;
    
import java.util.Scanner;
import java.util.ArrayList;
/**
* @author Timothy Landers
* <p>Date of creation: 20/6/22
* <p>Purpose: Contains methods for pre-fight character selection and banning, as well as target selection.
*/
public class Card_Selection
{
    /**
    * Prompts the player to choose the index number of a hero, and prints the purpose of the selection. Used for character selection/reselection.
    * <p>Also ensures index corresponds to a playable character, but does not check for bans/duplicates.
    * @param counter Should be either 616 (if player's previous input was invalid) or from 0-5 inclusive, to track how many heroes have been chosen. 
    * @param goal The purpose of the method call (e.g. previous input was a duplicate).
    * @return The index number chosen.
    * @see Card_Game_Main 
    */
    public static int Selection(int counter, String goal) 
    {
        int Cname=0;      
        boolean good=false;
        boolean check=false;
        if (counter>-1&&counter<6)
        check=true;
        if (check==true)
        {
            if (counter==0||counter==2||counter==4) //each player chooses 3 heroes; even numbers are for player 1
            {
                if (goal.equals("select"))
                System.out.println ("\nPlayer 1, choose a character. Type the character's index number, shown on the character list."); 
                else
                System.out.println ("\nPlayer 1, choose a character to ban. Type the character's index number, shown on the character list."); 
            }
            else
            {
                if (goal.equals("select"))
                System.out.println ("\nPlayer 2, choose a character. Type the character's index number, shown on the character list.");  
                else
                System.out.println ("\nPlayer 2, choose a character to ban. Type the character's index number, shown on the character list."); 
            }
        }
        else
        {
            if (goal.equals("banned")) 
            {
                System.out.println ("The selected character has been banned. Banned characters cannot be used. Please select another character.");
            } 
            else if (goal.equals("dupe"))
            {
                System.out.println ("No duplicate characters allowed.");
            }
            else //just in case
            {
                System.out.println("Spelling error in Selection's argument. Fix it.");
            }
        }
        do
        {
            Cname=Damage_Stuff.GetInput(); 
            if (Cname==616||Cname<=0||Cname>105||(Cname>41&&Cname<68)&&!(Cname>=61&&Cname<=65)) //updated as more characters are released in each version
            {
                System.out.println("Index number not found.");
            }
            else
            {
                good=true;
            }
        }
        while (good==false);
        return Cname;
    }  
    /**
     * Checks if the given index number is contained in the given list of indexes.
     * @param given The index number a player selected.
     * @param others The list of banned or currently chosen characters' index numbers.
     * @return False if given is a duplicate/banned index number. True if given is not (given is the only one).
     * @see Card_Game_Main
     */
    public static boolean OnlyOne (int given, int[] others) 
    { 
        for (int i: others)
        {
            if (i!=0&&given==i) //0 is the default value for empty int array slots, so skip checking those 
            return false;
        }
        return true;
    }
    /**
     * Handles target selection for heroes targeting individual teammates (AoE abilities don't have or need a special method). 
     * <p>The hero is not a parameter because targeting effects don't prevent them from targeting allies, and thus don't need to be checked.
     * @param list The hero's teammates. 
     * @return The teammate being targeted. If the hero has no valid targets, returns null.
     * @see Battle
     * @see StaticPassive
     */
    public static Character ChooseTargetFriend (Character[] list) 
    {
        int targ=616; boolean typo=true;
        boolean available=false;
        for (int i=0; i<6; i++)
        {
            if (list[i]!=null)
            {
                if (list[i].binaries.contains("Banished"))
                {
                    list[i]=null; //banished heroes cannot be targeted
                }
                else 
                available=true; 
            }
        } 
        if (available==true) //the hero must have at least one person they can target
        {
            for (int i=0; i<6; i++)
            {
                if (list[i]!=null)
                {
                    int n=i+1;
                    System.out.println (n+". "+list[i].Cname);
                }
            } 
            do
            {
                targ=Damage_Stuff.GetInput();
                --targ;
                if (targ==616||targ<0||targ>5||list[targ]==null)
                {
                    //try again
                }
                else
                {
                    typo=false;
                }
            }
            while (typo==true);             
        }
        if (targ!=616)
        return list[targ];
        else
        return null;
    }
    /**
     * Handles target selection for heroes targeting their enemies; checks for targeting effects. Used for both AoE and single target selection.
     * @param hero The hero.
     * @param list The hero's enemies. 
     * @return A Character array of enemies being targeted; if a single target ability was used, the array will only contain a single enemy.
     * @see Battle
     */
    public static Character[] ChooseTargetFoe (Character hero, Character[] list) //targeting an enemy
    {
        int targ=56; boolean typo=true;
        ArrayList<Character> team=CoinFlip.ToList(list); //targetable enemies
        ArrayList<Character> safe=new ArrayList<Character>();
        ArrayList<Character> remove=new ArrayList<Character>();        
        if (hero.activeability!=null) //war machine's passive triggers before he uses any abilities, so null check is needed to avoid nullexception
        {
            for (SpecialAbility s: hero.activeability.getSpecial()) //has to be called here or else the ignore is applied after target filtering
            { 
                if (s instanceof Ignore)
                {
                    int ignoreme=s.Use(hero, null);
                }
            }
        }
        for (Character c: team) //can't target banished enemies
        {
            if (c.binaries.contains("Banished"))
            {
                remove.add(c);
            }            
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        if (team.size()>1&&!(hero.ignores.contains("Untargetable"))&&((hero.team1==true&&Battle.p2solo!=true)||(hero.team1==false&&Battle.p1solo!=true)))
        { 
            for (Character c: team) //untargetable enemies are not untargetable if they are alone
            {
                if (c.targetable==false)
                {
                    remove.add(c);
                }
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        if (hero.index==20) //since superior spidey only conditionally ignores targeting effects 
        {
            for (Character c: team)
            {
                if (c.CheckFor("Tracer", false)==true) //inescapable
                safe.add(c);
            }
        }
        if (team.size()>1&&((hero.team1==true&&Battle.p2solo!=true)||(hero.team1==false&&Battle.p1solo!=true))) 
        {
            for (Character c: team)
            {
                if (hero.index==86&&c.binaries.contains("Invisible")&&c.CheckFor("Disorient", false)==true) //kraven passive
                {
                    //don't remove them, but don't add them to safe either; kraven treats snared invisible heroes like normal, so he still can't target them through provoke/taunt
                }
                else if (!(hero.ignores.contains("Invisible"))&&c.binaries.contains("Invisible"))
                {
                    remove.add(c);
                }
            }
        }
        if (remove.size()>0&&remove.size()!=team.size()) //invisible has no effect if everyone on a team is invisible
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        if (team.size()>1&&((hero.team1==true&&Battle.p2solo!=true)||(hero.team1==false&&Battle.p1solo!=true))) 
        {
            if (hero.CheckFor("Terror", false)==true&&!(hero.ignores.contains("Terror"))) //terror only works if the one the hero is terrified of isn't alone
            { 
                ArrayList <Character> afraid= new ArrayList <Character>(); 
                for (StatEff eff: hero.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Terror"))
                    {
                        Character temp=eff.UseTerrorProvoke();
                        if (temp!=null)
                        afraid.add(temp); //getting and storing the character who applied terror
                    }
                } 
                for (Character c: team)
                {
                    for (Character boy: afraid)
                    {
                        if (boy==c&&team.size()-remove.size()>1) //the enemy the hero is terrified of is only untargetable as long as there are other valid targets
                        {
                            remove.add(c); //the hero cannot target the one who terrified them 
                        }
                    }
                }
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        ArrayList <Character> nafraid= new ArrayList <Character>(); 
        if (hero.CheckFor("Provoke", false)==true&&!(hero.ignores.contains("Provoke")))
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Provoke"))
                {
                    Character temp=eff.UseTerrorProvoke();
                    if (temp!=null)
                    nafraid.add(temp);
                }
            } 
        } 
        ArrayList<Character> pfriends= new ArrayList<Character>();
        for (Character c: list)
        {
            if (c!=null&&c.team1==hero.team1) //for both/either multitargets; invisible and untargetable don't matter for teammates, but Provoke does
            {
                for (Character aggro: nafraid) //if provoked by ally, can only target that ally; if only provoked by enemies, can target any allies
                {
                    if (c==aggro)
                    pfriends.add(c);
                }
            }
        }
        if (pfriends.size()>0) //provoked by at least one ally
        safe.addAll(pfriends);
        else //provoked by no allies, so can target any of them
        {
            for (Character c: list)
            {
                if (c!=null&&c.team1==hero.team1) 
                safe.add(c);
            }
        }
        ArrayList <Character> priority= new ArrayList<Character>(); //taunt takes priority over provoke
        if (!(hero.ignores.contains("Taunt")))
        {
            for (Character target: team)
            {
                if (target.team1!=hero.team1) //taunt has no effect on allies
                {
                    for (StatEff e: target.effects)
                    {
                        if (e.getimmunityname().equals("Taunt")&&e.getefftype().equals("Defence")&&!(hero.ignores.contains("Defence")))
                        {
                            priority.add(target); break; //stop searching target's effects
                        }
                        else if (e.getimmunityname().equals("Taunt")&&e.getefftype().equals("Other"))
                        {
                            priority.add(target); break;
                        }
                    }
                }
            } 
        }
        if (priority.size()==0) //no taunt
        {
            if (nafraid.size()>0) //provoke is only taken into account if no one is taunting
            {
                for (Character c: team)
                {
                    if (!(nafraid.contains(c)))
                    {
                        remove.add(c); //can only target the one who applied provoke; remove everyone without it
                    }
                }
                if (team.size()-remove.size()<nafraid.size()) //if provoked by a teammate then add them here, since team normally only contains enemies
                {
                    if (hero.team1==true)
                    {
                        for (Character c: Battle.team1)
                        {
                            if (nafraid.contains(c))
                            team.add(c); 
                        }
                    }
                    else
                    {
                        for (Character c: Battle.team2)
                        {
                            if (nafraid.contains(c))
                            team.add(c); 
                        }
                    }
                }
            }
        }
        else //someone is taunting
        {
            for (Character c: team)
            {
                if (!(priority.contains(c)))
                remove.add(c); //non taunters cannot be targeted
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        for (Character c: safe)
        {
            if (!(team.contains(c)))
            team.add(c); //characters who the hero can target due to their conditional inescapable or whatever; simpler to add them back at end than rewrite the normal process
        }
        int i=team.size();
        Character[] nlist= new Character[i+1]; //put all valid targets into new array
        for (int h=0; h<i; h++)
        {
            nlist[h]=team.get(h);
        }
        for (int h=0; h<i; h++) //finally choose a target
        {
            int num=h+1;
            System.out.println (num+". "+nlist[h].Cname);
        } 
        do
        {
            targ=Damage_Stuff.GetInput();
            --targ;
            if (targ==616||targ<0||targ>=i)
            {
                //try again
            }
            else
            {
                typo=false;
            }
        }
        while (typo==true);
        nlist[i]=nlist[targ]; //player's chosen target is at end of array
        return nlist; //returns filtered list that can be used by multitarget
    }
}