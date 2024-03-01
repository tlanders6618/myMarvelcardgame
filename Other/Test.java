package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character Char11= new Hero (16); //Char11.add(Char11, new Taunt (0, 90)); //Char11.binaries.add("Stunned");
       Character Char12= new Hero (1); Char12.binaries.add("Stunned");
       Character Char13= new Summon (7); Char13.binaries.add("Stunned");
       Character Char21= new Hero (3); //Char21.binaries.add("Stunned");  //Char21.add(Char21, new Countdown (100, 250, 1, null)); 
       Character Char22= new Summon (12); Char22.binaries.add("Stunned"); 
       Character Char23= new Summon (7); Char23.binaries.add("Stunned");
       boolean Pwinner=false; 
       Pwinner=Battle.main(Char11, Char12, Char13, Char21, Char22, Char23);
       System.out.println("Donut forget to update card selection indexes");
       if (Pwinner==true)
       {
           System.out.println ("\nPlayer 1 wins!");
       }
       else if (Pwinner==false)
       {
           System.out.println ("\nPlayer 2 wins!");
       }        
       Card_Game_Main.trash.close();
       /*
        * To do/to test today:
        * make empowerments work with aoe for both regular and channelled abs; change chain method too
        * test shatter interact with protect
        */
       
       /** 
        for (int r = 0; i < arr.length; r++) 
       { //this is equal to the row 
         for (int c = 0; c < arr[r].length; c++) 
         { //this is equal to the column in each row.
            System.out.print(arr[r][c] + " ");
         }
         System.out.println("\n");
         //This prints the contents of a 2d array; save for later
       }//*/
   }
}