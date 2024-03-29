package myMarvelcardgamepack;

 
/**
 * Designer: Timothy Landers
 * Date: 17/7/22
 * Filename: Card_Game_Main
 * Purpose: Launches the card game.
 */
import java.util.ArrayList; 
import java.util.Scanner;
public class Card_Game_Main 
{
    static Scanner trash;
    public static void main (String[] args)
    {
        //Variables    
        trash= new Scanner (System.in);
        int Ccounter=0; //for keeping track of the character/ban array index
        int name=616;
        String ban="nope";
        String bane, bane1, bane2, bane3, bane4, bane5, bane6="F";
        boolean unique=false;
        int[] bans= new int [11]; 
        int[] charnames= new int[6];
        bans[6]=62; bans[7]=24; bans[8]=122; bans[9]=170; bans[10]=184; //players cannot pick gauntlet thanos, binary, etc        
        //Selecting cards
        System.out.println ("Welcome. The current version is 4.0.19. The latest playable character is Dr. Strange (Classic). "); 
        System.out.println ("Please check the character overview list for the full list of playable characters.");
        System.out.println ("Remember, characters will take turns in the order they are picked.");
        //System.out.println ("\nEnter draft mode (allows character banning and up to one character swap)? Type yes or no.");
        boolean issue=true; //no draft mode until there are at least ~16 heroes
        while (issue==true) //as in there's an issue with what they typed
        {
            ban="no"; //until draft mode is reenabled
            //ban=scan.nextLine(); 
            ban=ban.trim().replaceAll("\\s+"," "); 
            if (ban.equalsIgnoreCase("yes")||ban.equalsIgnoreCase("no"))
            {
                issue=false;
            }
        }
        //Optional ban phase
        if (ban.equalsIgnoreCase("yes"))
        {
            System.out.println ("Each player gets to ban 3 characters. Banned characters cannot be used by either player.");
            for (int i=0; i<6; i++)
            { 
                while (unique==false) //ensures there are no duplicate bans
                {
                    name=Card_Selection.Selection(i, true);
                    unique=Card_Selection.OnlyOne(name, bans); 
                    if (unique==false)
                    {
                        System.out.println ("No duplicate bans allowed.");
                    }
                }
                switch (Ccounter)
                {
                    case 0: bans[0]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break; 
                    case 1: bans[1]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break;
                    case 2: bans[2]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break;
                    case 3: bans[3]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break;
                    case 4: bans[4]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break;
                    case 5: bans[5]=name; bane=Character.SetName(name, false); System.out.println (bane+" has been banned."); break;
                } 
            }
            bane1=Character.SetName(bans[0], false);
            bane2=Character.SetName(bans[1], false);
            bane3=Character.SetName(bans[2], false);
            bane4=Character.SetName(bans[3], false);
            bane5=Character.SetName(bans[4], false);
            bane6=Character.SetName(bans[5], false);
            System.out.println ("Banned characters for this match: "+bane1+", "+bane2+", "+bane3);
            System.out.println ("Banned characters for this match: "+bane4+", "+bane5+", "+bane6);
        }
        //Creating teams
        unique=false; boolean uncle; //uncle also represents hero uniqueness
        boolean banned; 
        System.out.println ("Now you will take turns choosing characters.");
        while (Ccounter<6)
        {
            name=Card_Selection.Selection(Ccounter, false); //player picks their character 
            unique=Card_Selection.OnlyOne(name, charnames); //check for duplicates
            uncle=Card_Selection.OnlyOne(name, bans); //check for bans
            while (unique==false||uncle==false) //ensures banned/duplicate characters cannot be used             
            {
                if (unique==false)
                {
                    banned=false;
                    System.out.println ("No duplicate characters allowed.");
                    name=Card_Selection.Retry(banned);
                }
                if (uncle==false) 
                {
                    banned=true;
                    name=Card_Selection.Retry(banned);
                }
                unique=Card_Selection.OnlyOne(name, charnames);
                uncle=Card_Selection.OnlyOne(name, bans);
            }
            switch (Ccounter)
            { 
                case 0: charnames[0]=name; bane=Character.SetName(name, false); System.out.println ("Player 1 has picked "+bane); ++Ccounter; break; 
                case 1: charnames[1]=name; bane=Character.SetName(name, false); System.out.println ("Player 2 has picked "+bane); ++Ccounter; break;
                case 2: charnames[2]=name; bane=Character.SetName(name, false); System.out.println ("Player 1 has picked "+bane); ++Ccounter; break;
                case 3: charnames[3]=name; bane=Character.SetName(name, false); System.out.println ("Player 2 has picked "+bane); ++Ccounter; break;
                case 4: charnames[4]=name; bane=Character.SetName(name, false); System.out.println ("Player 1 has picked "+bane); ++Ccounter; break;
                case 5: charnames[5]=name; bane=Character.SetName(name, false); System.out.println ("Player 2 has picked "+bane); ++Ccounter; break;
            }  
            //0 is name11, 1 is name21, 2 is name12, 3 is name22, 4 is name 13, 5 is name23
        }
        //Optional team editing phase
        if (ban.equalsIgnoreCase("yes"))
        {
            for (int i=0; i<2; i++) //this is the reason why it's called draft mode instead of ban mode. Players can change one of their characters
            {
                unique=false; uncle=false;
                boolean typo=true;
                if (i==0)
                {
                    System.out.println ("Player 1, do you want to change one of your characters? Type yes or no.");
                }
                else if (i==1)
                {
                    System.out.println ("Player 2, do you want to change one of your characters? Type yes or no.");
                }
                bane=trash.nextLine(); 
                if (bane.equalsIgnoreCase("yes"))
                {
                    System.out.println ("Which character would you like to add to your team?");
                    while (unique==false||uncle==false) //check for uniqueness again
                    {
                        name=trash.nextInt();
                        unique=Card_Selection.OnlyOne(name, charnames);
                        uncle=Card_Selection.OnlyOne(name, bans);
                        if (unique==false)
                        {
                            System.out.println ("No duplicate characters allowed.");
                        }
                        if (uncle==false)
                        {
                            System.out.println ("No banned characters allowed.");
                        }
                    }
                    bane=Character.SetName(name, false);
                    if (i==0)
                    {
                        System.out.println ("Player 1 has picked "+bane);
                    }
                    else 
                    {
                        System.out.println ("Player 2 has picked "+bane);
                    }
                    System.out.println ("Which character would you like them to replace?");
                    while (typo==true) 
                    {
                        int replace;
                        replace=Damage_Stuff.GetInput(); 
                        if (i==0) //if player 1 is typing
                        {
                            if (replace==charnames[0])
                            {
                                typo=false;
                                charnames[0]=name;
                            }
                            else if(replace==charnames[2])
                            {
                                typo=false;
                                charnames[2]=name; 
                            }
                            else if(replace==charnames[4])
                            {
                                typo=false;
                                charnames[3]=name;
                            } 
                        }
                        else if (i==1) //player 2 is typing
                        {
                            if (replace==charnames[1])
                            {
                                typo=false;
                                charnames[0]=name;
                            }
                            else if(replace==charnames[3])
                            {
                                typo=false;
                                charnames[2]=name; 
                            }
                            else if(replace==charnames[5])
                            {
                                typo=false;
                                charnames[3]=name;
                            } 
                        }
                        if (typo==true)
                        {
                            System.out.println ("Index number not found. Please try again.");
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