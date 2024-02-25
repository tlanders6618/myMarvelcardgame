package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       //classic drax is 12, modern drax is 13, x23 is 14, wolvie is 15
       Character Char11= new Hero (10); //Char11.add(Char11, new Intensify (0, 1500, 2)); //Char11.binaries.add("Stunned");
       Character Char12= new Summon (7); Char12.binaries.add("Stunned");
       Character Char13= new Summon (7); Char13.binaries.add("Stunned");
       Character Char21= new Hero (8); Char21.add(Char21, new Intensify (0, 90, 2)); //Char21.binaries.add("Stunned"); 
       Character Char22= new Hero (10); Char22.binaries.add("Stunned");
       Character Char23= new Summon (7); Char23.binaries.add("Stunned");
       
       boolean Pwinner=false; 
       Pwinner=Battle.main(Char11, Char12, Char13, Char21, Char22, Char23);
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
        * make empowerments work with aoe for both regular and channelled abs
        * test shatter interact with protect
        * test snare same way as speed
        * test drax and drax
        */
       
       /** 
        for (int i = 0; i < n.length; i++) 
       { //this is equal to the row 
         for (int j = 0; j < n[i].length; j++) 
         { //this is equal to the column in each row.
            System.out.print(n[i][j] + " ");
         }
         System.out.println("\n");
         //This prints the contents of a 2d array; save for later
       }//*/
   }
}