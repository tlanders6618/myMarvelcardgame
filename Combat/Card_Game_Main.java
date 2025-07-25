package myMarvelcardgamepack;
import java.util.ArrayList; 
import java.util.Scanner;
/**
 * Designer: Timothy Landers
 * Date: 17/7/22
 * Filename: Card_Game_Main
 * Purpose: Launches the card game.
 */
public class Card_Game_Main 
{
    public static Scanner trash;
    public static final int INVALID=616;
    public static final int TEAM_SIZE=6; 
    public static void main (String[] args)
    {
        //Variables    
        trash= new Scanner (System.in);
        int Ccounter=0; //for keeping track of the character/ban array index
        int name=616;
        String ban="nope";
        boolean unique=false;
        int[] bans= new int [13]; 
        int[] charnames= new int[6];
        bans[6]=24; bans[7]=62; bans[8]=122; bans[9]=170; bans[10]=184; //players cannot pick gauntlet thanos, binary, etc        
        //Selecting cards
        System.out.println ("Welcome. The current version is 4.6.3. The latest playable character is Supergiant (Classic). "); 
        System.out.println ("Currently, characters are playable from: 2.0, 2.1, 2.5, 2.6, 2.7, 2.8, 2.9, and 2.10.");
        System.out.println ("Remember, characters will take turns in the order they are picked.");
        System.out.println ("\nEnter draft mode (allows character banning and up to one character swap)? Type yes or no.");
        boolean issue=true; 
        while (issue==true) //as in there's an issue with what they typed
        {
            ban=trash.nextLine(); 
            ban=ban.toLowerCase().trim().replaceAll("\\s+"," "); 
            if (ban.equalsIgnoreCase("yes")||ban.equalsIgnoreCase("no"))
            {
                issue=false;
            }
        }
        //Optional ban phase
        if (ban.equalsIgnoreCase("yes"))
        {
            System.out.println ("Each player gets to ban 3 characters. Banned characters cannot be used by either player.");
            while (Ccounter<6)
            { 
                while (unique==false) //ensures there are no duplicate bans
                {
                    name=Card_Selection.Selection(Ccounter, "ban");
                    unique=Card_Selection.OnlyOne(name, bans); 
                    if (unique==false)
                    {
                        System.out.println (Character.SetName(name, false)+" has already been banned. Please select another character.");
                    }
                }
                if (name==95) //even though they're technically separate characters, they're treated as the same in terms of uniqueness
                bans[11]=96;
                else if (name==96) //this is to avoid there being 2 of the same hero on the battlefield if one transforms into the other mid-fight
                bans[11]=95;
                else if (name==100)
                bans[12]=101;
                else if (name==101)
                bans[12]=100;
                switch (Ccounter)
                {
                    case 0: bans[0]=name; break; 
                    case 1: bans[1]=name; break;
                    case 2: bans[2]=name; break;
                    case 3: bans[3]=name; break;
                    case 4: bans[4]=name; break;
                    case 5: bans[5]=name; break;
                } 
                System.out.println (Character.SetName(name, false)+" has been banned."); ++Ccounter; unique=false;
            }
            System.out.println ("\nPlayer 1's bans for this match: "+Character.SetName(bans[0], false)+", "+Character.SetName(bans[2], false)+", "+Character.SetName(bans[4], false));
            System.out.println ("Player 2's bans for this match: "+Character.SetName(bans[1], false)+", "+Character.SetName(bans[3], false)+", "+Character.SetName(bans[5], false));
        }
        //Creating teams
        unique=false; boolean notbanned=false; 
        System.out.println ("Now you will take turns choosing characters.");
        Ccounter=0;
        while (Ccounter<6)
        {
            name=Card_Selection.Selection(Ccounter, "select"); //player picks their character 
            unique=Card_Selection.OnlyOne(name, charnames); //check for duplicates
            notbanned=Card_Selection.OnlyOne(name, bans); //check for bans
            while (unique==false||notbanned==false) //ensures banned/duplicate characters cannot be used             
            {
                if (unique==false) //character is already chosen
                {
                    name=Card_Selection.Selection(616, "dupe");
                }
                if (notbanned==false) //character is banned
                {
                    name=Card_Selection.Selection(616, "banned");
                }
                unique=Card_Selection.OnlyOne(name, charnames);
                notbanned=Card_Selection.OnlyOne(name, bans);
            }
            if (name==95) 
            bans[11]=96;
            else if (name==96) 
            bans[11]=95;
            else if (name==100)
            bans[12]=101;
            else if (name==101)
            bans[12]=100;
            switch (Ccounter) //0 is name11, 1 is name21, 2 is name12, 3 is name22, 4 is name 13, 5 is name23
            { 
                case 0: charnames[0]=name; break; 
                case 1: charnames[1]=name; break;
                case 2: charnames[2]=name; break;
                case 3: charnames[3]=name; break;
                case 4: charnames[4]=name; break;
                case 5: charnames[5]=name; break;
            }  
            if (Ccounter%2==0)
            System.out.println ("Player 1 has picked "+Character.SetName(name, false)); 
            else
            System.out.println ("Player 2 has picked "+Character.SetName(name, false));
            ++Ccounter; 
        }
        //Optional team editing phase
        if (ban.equalsIgnoreCase("yes"))
        {
            System.out.println("\nPlayer 1's team: "+Character.SetName(charnames[0], false)+", "+Character.SetName(charnames[2], false)+", "+Character.SetName(charnames[4], false));
            System.out.println("Player 2's team: "+Character.SetName(charnames[1], false)+", "+Character.SetName(charnames[3], false)+", "+Character.SetName(charnames[5], false));
            for (int i=0; i<2; i++) //this is the reason why it's called draft mode instead of ban mode. Players can change one of their characters
            {
                unique=false; notbanned=false;
                boolean typo=true;
                if (i==0)
                System.out.println ("\nPlayer 1, do you want to change one of your characters? Type yes or no.");
                else 
                System.out.println ("\nPlayer 2, do you want to change one of your characters? Type yes or no.");
                boolean problem=true; String dr=null;
                while (problem==true) 
                {
                    dr=trash.nextLine(); 
                    dr=dr.trim().replaceAll("\\s+"," "); 
                    if (dr.equalsIgnoreCase("yes")||dr.equalsIgnoreCase("no"))
                    {
                        problem=false;
                    }
                }
                if (dr.equalsIgnoreCase("yes"))
                {
                    System.out.println ("Which character would you like to add to your team?");
                    while (unique==false||notbanned==false) //check for uniqueness again
                    {
                        if (i==0)
                        name=Card_Selection.Selection(0, "select");
                        else
                        name=Card_Selection.Selection(1, "select");
                        unique=Card_Selection.OnlyOne(name, charnames);
                        notbanned=Card_Selection.OnlyOne(name, bans);
                        if (unique==false)
                        {
                            System.out.println ("No duplicate characters allowed.");
                        }
                        if (notbanned==false)
                        {
                            System.out.println ("No banned characters allowed.");
                        }
                    }
                    if (i==0)
                    System.out.println ("Player 1 has picked "+Character.SetName(name, false));
                    else 
                    System.out.println ("Player 2 has picked "+Character.SetName(name, false));
                    System.out.println ("\nWhich character would you like them to replace? Type their number, not their name.");
                    if (i==0)
                    {
                        System.out.println("1. "+Character.SetName(charnames[0], false)+" 2. "+Character.SetName(charnames[2], false)+" 3. "+Character.SetName(charnames[4], false));
                    }
                    else
                    {
                        System.out.println("1. "+Character.SetName(charnames[1], false)+" 2. "+Character.SetName(charnames[3], false)+" 3. "+Character.SetName(charnames[5], false));
                    }
                    while (typo==true)  
                    {
                        int replace=Damage_Stuff.GetInput(); int gone=0; //index of character who was removed
                        if (i==0) //if player 1 is typing
                        {
                            if (replace==1)
                            {
                                typo=false; gone=charnames[0];   
                                System.out.println(Character.SetName(charnames[0], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[0]=name; 
                            }
                            else if (replace==2)
                            {
                                typo=false; gone=charnames[2];  
                                System.out.println(Character.SetName(charnames[2], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[2]=name; 
                            }
                            else if (replace==3)
                            {
                                typo=false; gone=charnames[4];  
                                System.out.println(Character.SetName(charnames[4], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[4]=name; 
                            } 
                        }
                        else //player 2 is typing
                        {
                            if (replace==1)
                            {
                                typo=false; gone=charnames[1];  
                                System.out.println(Character.SetName(charnames[1], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[1]=name; 
                            }
                            else if (replace==2)
                            {
                                typo=false; gone=charnames[3];  
                                System.out.println(Character.SetName(charnames[3], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[3]=name; 
                            }
                            else if (replace==3)
                            {
                                typo=false; gone=charnames[5];  
                                System.out.println(Character.SetName(charnames[5], false)+" has been replaced with "+Character.SetName(name, false)+".");
                                charnames[5]=name; 
                            } 
                        }
                        if (typo==true)
                        {
                            System.out.println ("Index number not found. Please try again.");
                        }
                        else if (gone==95||gone==96) //if emma is no longer on either player's team, she should be pickable again by the other player, in both of her forms
                        {
                            bans[11]=-616;
                        }
                        else if (gone==100||gone==101)
                        {
                            bans[12]=-616;
                        }
                    }
                }
            }
        }        
        //Creating character objects  
        Character Char11= new Hero (charnames[0]);
        Character Char12= new Hero (charnames[2]); 
        Character Char13= new Hero (charnames[4]); 
        Character Char21= new Hero (charnames[1]);
        Character Char22= new Hero (charnames[3]); 
        Character Char23= new Hero (charnames[5]); 
        //The game itself begins
        boolean Pwinner;
        Pwinner=Battle.main(Char11, Char12, Char13, Char21, Char22, Char23);
        //Result
        if (Pwinner==true)
        {
            System.out.println ("\nGame over. Player 1 wins!");
        }
        else if (Pwinner==false)
        {
            System.out.println ("\nGame over. Player 2 wins!");
        }        
        System.out.println("Thank you for playing!");
        trash.close();
    }
}