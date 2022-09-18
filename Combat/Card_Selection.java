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
                if (Cname==616||Cname<=0||Cname>7) //update with each version
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
                if (ban==616||ban<=0||ban>7)
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
            if (rename==616||rename<=0||rename>7)
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
        int targ=56; boolean typo=true;
        for (int i=0; i<6; i++)
        {
            if (list[i]!=null&&(list[i].CheckFor(list[i], "Banish")==true))
            {
                list[i]=null;
            }
        } 
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
        return targ;
    }
    public static Character ChooseTargetFoe (Character hero, Character[] list)
    {
        int targ=56; boolean typo=true;
        ArrayList <Integer> afraid= new ArrayList <Integer>();
        for (int i=0; i<6; i++)
        {
            if (list[i]!=null&&list[i].CheckFor(list[i], "Banish")==true)
            {
                list[i]=null;
            }
            if (list[i]!=null&&list[i].targetable==false)
            {
                list[i]=null;
            }
            if (!(hero.ignores.contains("Invisible")))
            {
                if (list[i]!=null&&list[i].CheckFor(list[i], "Invisible")==true)
                {
                    list[i]=null;
                }
            }
        } 
        //switch statement for kraven
        if (!(hero.immunities.contains("Terror"))&&!(hero.ignores.contains("Terror")))
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Terror"))
                {
                    int h=eff.UseTerrorProvoke();
                    Integer a=h; afraid.add(a); //getting and storing the hash of the character who applied terror
                }
            } 
            for (int i=0; i<6; i++)
            {
                for (Integer ig: afraid)
                {
                    int h= (int) ig;
                    if (h==list[i].hash)
                    {
                        list[i]=null; //the hero cannot target the one who terrified them
                    }
                }
            }
        }
        ArrayList <Integer> nafraid= new ArrayList <Integer>();
        if (!(hero.immunities.contains("Provoke"))&&!(hero.ignores.contains("Provoke")))
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
            for (Character target: list)
            {
                if (target!=null&&target.CheckFor(target, "Taunt")==true)
                {
                    int h=target.hash;
                    Integer a=h; priority.add(a);
                }
            } 
        }
        if (priority.size()==0) 
        {
            if (nafraid.size()>0) //provoke is only used is no one is taunting
            {
                for (int i=0; i<6; i++)
                {
                    for (Integer ig: nafraid)
                    {
                        int h= (int) ig;
                        if (h!=list[i].hash)
                        {
                            list[i]=null; 
                        }
                    }
                }
            }
        }
        else //someone is taunting
        {
            for (int i=0; i<6; i++)
            {
                for (Integer ig: priority)
                {
                    int h= (int) ig;
                    if (list[i]!=null&&h!=list[i].hash)
                    {
                        list[i]=null; //non taunters cannot be attacked
                    }
                }
            }
        }
        for (int i=0; i<6; i++) //finally choose a target
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
        Character target=list[targ];
        return target;
    }
}