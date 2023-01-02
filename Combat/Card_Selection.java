package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 20/6/22
 * Filename: Card_Selection
 * Purpose: Pre-fight character selection, as well as target and ability selection.
 */
import java.util.Scanner;
import java.util.ArrayList;
public class Card_Selection
{
    public static int Selection(int counter)
    {
        int Cname=0;      
        if (counter<6)
        {
            if (counter==0 || counter==2 || counter==4)
            {
                System.out.println ("\nPlayer 1, choose a character. Type the character's index number, shown on the character list.");                       
            }
            else
            {
                System.out.println ("\nPlayer 2, choose a character. Type the character's index number, shown on the character list.");  
            }
            boolean good;
            do
            {
                Cname=Damage_Stuff.GetInput(); 
                good=false;
                if (Cname==616||Cname<=0||(Cname==11||Cname>12)) //update with each version
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
        else 
        {
            System.out.println ("Could not select a character due to counter error.");         
            return 616;           
        }    
    }  
    public static int Ban (int counter)
    {
        int ban;
        boolean typo=true;
        if (counter<6)
        {
            if (counter==0 || counter==2 || counter==4)
            {
                System.out.println ("\nPlayer 1, choose a character to ban. Type the character's index number, shown on the character list.");                        
            }
            else
            {
                System.out.println ("\nPlayer 2, choose a character to ban. Type the character's index number, shown on the character list.");     
            }
            do
            {
                ban=Damage_Stuff.GetInput();
                if (ban==616||ban<=0||ban>10)
                {
                    System.out.println("Index number not found.");
                }
                else
                {
                    typo=false;
                }
            }
            while (typo==true);
            return ban;
        }
        else 
        {
            System.out.println ("Could not select a ban due to counter error.");          
            return 616;            
        }    
    }
    public static int Retry(boolean banned)
    {
        int rename=0;
        boolean typo=true;
        if (banned==true) //tried to pick banned character
        {
            System.out.println ("\nThe selected character has been banned. Banned characters cannot be used. Please select another character.");
        }
        do
        {
            rename=Damage_Stuff.GetInput();
            if (rename==616||rename<=0||(rename==11||rename>12))
            {
                System.out.println("Index number not found.");
            }
            else
            {
                typo=false;
            }
        }
        while (typo==true);
        return rename;            
    }
    public static boolean OnlyOne (int chosen, int[] others)
    { 
        //This ensures players cannot choose duplicate characters. Chosen is the name the player entered; the other names are already chosen characters
        boolean unique=true;  
        for (int i: others)
        {
            if (i!=0&&chosen==i) //the default value for empty int array slots is 0; no point in checking those 
            {
                unique=false;
                break;
            }
        }
        return unique;
    }
    public static int ChooseTargetFriend (Character[] list)
    {
        int targ=616; boolean typo=true;
        for (int i=0; i<6; i++)
        {
            if (list[i]!=null&&list[i].binaries.contains("Banished"))
            {
                list[i]=null;
            }
        } 
        int available=0;
        for (int i=0; i<6; i++)
        {
            if (list[i]!=null&&!(list[i].binaries.contains("Banished")))
            {
                ++available;
            }
        }
        if (available>0) //the hero must have at least one person they can target to avoid an infinite loop
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
        return targ;
    }
    public static Character ChooseTargetFoe (Character hero, Character[] list)
    {
        int targ=56; boolean typo=true;
        ArrayList<Character> team=Card_CoinFlip.ToList(list);
        ArrayList<Character> remove=new ArrayList<Character>();
        for (Character c: team)
        {
            if (c.CheckFor(c, "Banish")==true)
            {
                remove.add(c);
            }            
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        //switch statement for kraven here
        ArrayList <Integer> afraid= new ArrayList <Integer>();
        if (hero.CheckFor(hero, "Terror")==true&&!(hero.ignores.contains("Terror")))
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Terror"))
                {
                    int h=eff.UseTerrorProvoke();
                    Integer a=h; afraid.add(a); //getting and storing the hash of the character who applied terror
                }
            } 
            for (Character c: team)
            {
                for (Integer ig: afraid)
                {
                    int h=(int) ig;
                    if (h==c.hash)
                    {
                        remove.add(c); //the hero cannot target the one who terrified them
                    }
                }
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        ArrayList <Integer> nafraid= new ArrayList <Integer>();
        if (hero.CheckFor(hero, "Provoke")==true&&!(hero.ignores.contains("Provoke")))
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Provoke"))
                {
                    int h=eff.UseTerrorProvoke();
                    Integer a=h; nafraid.add(a);
                }
            } 
        }
        ArrayList <Integer> priority= new ArrayList<Integer>();
        if (!(hero.ignores.contains("Taunt"))&&!(hero.ignores.contains("Defence")))
        {
            for (Character target: team)
            {
                if (target.CheckFor(target, "Taunt")==true)
                {
                    int h=target.hash;
                    Integer a=h; priority.add(a);
                }
            } 
        }
        if (priority.size()==0) //no taunt
        {
            if (nafraid.size()>0) //provoke is only used is no one is taunting
            {
                for (Character c: team)
                {
                    for (Integer ig: nafraid)
                    {
                        int h= (int) ig;
                        if (h!=c.hash)
                        {
                            remove.add(c); 
                        }
                    }
                }
            }
        }
        else //someone is taunting
        {
            for (Character c: team)
            {
                for (Integer ig: priority)
                {
                    int h= (int) ig;
                    if (h!=c.hash)
                    {
                        remove.add(c); //non taunters cannot be attacked
                    }
                }
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        if (Battle.GetNumOfHeroes(Card_CoinFlip.TeamFlip(hero.team1))>1) //the untargetable heroes are only untargetable if they are not alone on their team
        {
            for (Character c: team)
            {
                if (c.targetable==false)
                {
                    remove.add(c);
                }
                else if (!(hero.ignores.contains("Invisible")))
                {
                    if (c.binaries.contains("Invisible"))
                    {
                        remove.add(c);
                    }
                }
            }
        }
        if (remove.size()>0)
        {
            team.removeAll(remove);
            remove.removeAll(remove);
        }
        int i=team.size();
        Character[] nlist= new Character[i];
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
            if (targ==616||targ<0||targ>i)
            {
                //try again
            }
            else
            {
                typo=false;
            }
        }
        while (typo==true);
        Character target=nlist[targ];
        return target;
    }
}