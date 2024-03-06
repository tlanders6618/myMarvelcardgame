package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character Char11= new Summon (28); //Char11.binaries.add("Stunned"); //Char11.add(Char11, new Taunt (0, 90)); 
       Character Char12= new Summon (27); Char11.add(Char12, new Tracer (100, 616)); Char12.binaries.add("Stunned"); //Char12.add(Char12, new Invisible(66, 66));
       Character Char13= new Summon (12); Char13.binaries.add("Stunned");
       Character Char21= new Hero (20); //Char21.binaries.add("Stunned"); 
       Character Char22= new Summon (7); Char22.binaries.add("Stunned"); 
       Character Char23= new Summon (12); Char23.binaries.add("Stunned"); 
       boolean Pwinner=false; 
       System.out.println("\nDonut forget to update card selection indexes");
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
        * make empowerments work with aoe for both regular and channelled abs; change chain method too
        * test shatter interact with protect
        * do draft mode soon
        * update github files
        * buff eddie
        * nerf x23 and parker
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