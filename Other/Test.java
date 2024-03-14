package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character Char11= new Hero (13); //Char11.binaries.add("Stunned"); 
       Character Char12= new Hero (21); Char12.binaries.add("Stunned"); 
       Character Char13= new Summon (12); Char13.binaries.add("Stunned");
       Character Char21= new Hero (11); //Char21.binaries.add("Stunned"); 
       Character Char22= new Summon (12); Char22.binaries.add("Stunned"); 
       Character Char23= new Hero (13); Char23.binaries.add("Stunned"); 
       boolean Pwinner=false; 
       //Char22.add(Char11, new Evade (100)); Char22.add(Char11, new Taunt (100)); //Char12.add(Char11, new Intensify(66, 5, 66)); //Char22.add(Char11, new Burn(100, 0, 2)); 
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
        * do draft mode soon
        * randomly taking bleed dmg twice (captain a took thrice; flash took twice)
        * rewrite modok passive to account for barrier  
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