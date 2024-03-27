package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character Char11= new Hero (34); //Char11.binaries.add("Stunned"); 
       Character Char12= new Hero (25); //Char12.binaries.add("Stunned"); 
       Character Char13= new Hero (23); Char13.binaries.add("Stunned");
       Character Char21= new Hero (21); //Char21.binaries.add("Stunned"); 
       Character Char22= new Summon (12); Char22.binaries.add("Stunned"); 
       Character Char23= new Summon (4); Char23.binaries.add("Stunned"); 
       boolean Pwinner=false;  
       Char11.passivecount=5; Char21.add(new Neutralise (100, 455)); Char21.add(new Disarm (100, 999)); //Char21.add(new Bulwark(100, 7)); //Char21.add(new Shatter(2, 3));
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
        * do draft mode for 4.1
        * randomly taking bleed dmg twice (captain a took thrice; flash took twice)
        * test that extend doesn't affect immortal/stun
        * final test: flash, deadpool, skull, juggs, bb, cm, modoc
        */
       //random number=Min + (int)(Math.random() * ((Max - Min) + 1))
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