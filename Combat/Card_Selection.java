package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 20/6/22
 * Filename: Card_Selection
 * Purpose: Pre-fight character selection and banning, as well as target selection.
 */
import java.util.Scanner;
import java.util.ArrayList;
public class Card_Selection
{
    public static int Selection(int counter, boolean ban) //choosing heroes
    {
        int Cname=0;      
        if (counter<6) //each player chooses 3 heroes
        {
            if (counter==0 || counter==2 || counter==4)
            {
                if (ban==false)
                System.out.println ("\nPlayer 1, choose a character. Type the character's index number, shown on the character list."); 
                else
                System.out.println ("\nPlayer 1, choose a character to ban. Type the character's index number, shown on the character list."); 
            }
            else
            {
                if (ban==false)
                System.out.println ("\nPlayer 2, choose a character. Type the character's index number, shown on the character list.");  
                else
                System.out.println ("\nPlayer 2, choose a character to ban. Type the character's index number, shown on the character list."); 
            }
            boolean good;
            do
            {
                Cname=Damage_Stuff.GetInput(); 
                good=false;
                if (Cname==616||Cname<=0||Cname>88||(Cname>41&&Cname<81)) //updated as more characters are released in each version
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
            if (ban==false)
            System.out.println ("Could not select a character due to counter error."); 
            else
            System.out.println ("Could not select a ban due to counter error.");  
            return 616;           
        }    
    }  
    public static int Retry(boolean banned) //if player inputs duplicate or banned hero
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
            if (rename==616||rename<=0||rename>88||(rename>41&&rename<81))
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
    public static boolean OnlyOne (int chosen, int[] others) //Ensures players cannot choose duplicate heroes. 
    { 
        //Chosen is the name the player entered; the other names are previously selected heroes
        for (int i: others)
        {
            if (i!=0&&chosen==i) //the default value for empty int array slots is 0; no point in checking those 
            {
                return false;
            }
        }
        return true;
    }
    public static int ChooseTargetFriend (Character[] list) //for targeting allies
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
        if (available>0) //the hero must have at least one person they can target, to avoid an infinite loop
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
    public static Character ChooseTargetFoe (Character hero, Character[] list) //targeting an enemy
    {
        int targ=56; boolean typo=true;
        ArrayList<Character> team=CoinFlip.ToList(list);
        ArrayList<Character> safe=new ArrayList<Character>();
        ArrayList<Character> remove=new ArrayList<Character>();
        if (hero.index==20) //since superior spidey only conditionally ignores targeting effects 
        {
            for (Character c: list)
            {
                if (c.CheckFor("Tracer", false)==true&&c.targetable==true&&!(c.binaries.contains("Banished"))) //inescapable
                safe.add(c);
            }
        }
        if (hero.activeability!=null) //war machine's passive triggers before he uses any abilities
        {
            for (SpecialAbility s: hero.activeability.special) //has to be called here or else the ignore is applied after target filtering
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
        if (team.size()>1&&((hero.team1==true&&Battle.p2solo!=true)||(hero.team1==false&&Battle.p1solo!=true))) 
        {
            for (Character c: team)
            {
                if (hero.index==86&&c.binaries.contains("Invisible")&&c.CheckFor("Snare", false)==true) //kraven passive
                {
                    //don't remove them, but don't add them to safe either; kraven treats snared invisible heroes like normal, meaning he can't target them through provoke/taunt
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
            if (hero.CheckFor("Terror", false)==true&&!(hero.ignores.contains("Terror")))
            {
                ArrayList <Integer> afraid= new ArrayList <Integer>(); //terror only works if the one the hero is terrified of isn't alone
                for (StatEff eff: hero.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Terror"))
                    {
                        int h=eff.UseTerrorProvoke();
                        if (h!=0)
                        {
                            Integer a=h; afraid.add(a); //getting and storing the hash of the character who applied terror
                        }
                    }
                } 
                for (Character c: team)
                {
                    for (Integer ig: afraid)
                    {
                        int h=(int) ig;
                        if (h==c.hash) //the enemy the hero is terrified of is only untargetable as long as there are other valid targets
                        {
                            remove.add(c); //the hero cannot target the one who terrified them 
                            break;
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
        ArrayList <Integer> nafraid= new ArrayList <Integer>();
        if (hero.CheckFor("Provoke", false)==true&&!(hero.ignores.contains("Provoke")))
        {
            for (StatEff eff: hero.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase("Provoke"))
                {
                    int h=eff.UseTerrorProvoke();
                    if (h!=0)
                    {
                        Integer a=h; nafraid.add(a);
                    }
                }
            } 
        }
        ArrayList <Integer> priority= new ArrayList<Integer>(); //taunt takes priority over provoke
        if (!(hero.ignores.contains("Taunt")))
        {
            for (Character target: team)
            {
                for (StatEff e: target.effects)
                {
                    if (e.getimmunityname().equals("Taunt")&&e.getefftype().equals("Defence")&&!(hero.ignores.contains("Defence")))
                    {
                        int h=target.hash;
                        Integer a=h; priority.add(a);
                    }
                    else if (e.getimmunityname().equals("Taunt")&&e.getefftype().equals("Other"))
                    {
                        int h=target.hash;
                        Integer a=h; priority.add(a);
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
                    for (Integer ig: nafraid)
                    {
                        int h= (int) ig;
                        if (h!=c.hash)
                        {
                            remove.add(c); //can only target the one who applied provoke
                        }
                    }
                }
            }
        }
        else //someone is taunting
        {
            for (Character c: team)
            {
                boolean taunt=false;
                for (Integer ig: priority)
                {
                    int h= (int) ig;
                    if (h==c.hash)
                    {
                        taunt=true; //non taunters cannot be attacked
                    }
                }
                if (taunt==false)
                remove.add(c);
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
        Character[] nlist= new Character[i]; //put all valid targets into new array
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
        Character target=nlist[targ];
        return target;
    }
}