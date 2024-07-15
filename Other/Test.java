package myMarvelcardgamepack;
import javax.swing.*;
import java.util.*;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character Char11= new Hero (76); //Char11.binaries.add("Stunned"); 
       Character Char12= new Hero (13); //Char12.binaries.add("Stunned"); 
       Character Char13= new Hero (22); Char13.binaries.add("Stunned"); 
       Character Char21= new Hero (21); //Char21.binaries.add("Stunned"); 
       Character Char22= new Hero (6); //Char22.binaries.add("Stunned"); 
       Character Char23= new Hero (85); Char23.binaries.add("Stunned"); 
       boolean Pwinner=false; 
       
       //testing misc things unrelated to characters
       //String t="random 1"; System.out.println(true&&false); System.out.println(t.substring(0, t.length()-2)+" k "+t.substring(t.length()-1)); System.out.println("random k 1");
       
       //for adding stateffs
       //Char21.add(new Undermine(0, 3, Char11), false); //Char11.add(new Disrupt(500, 500, Char11)); //Char11.add(new Blind (0, 3, new Hero(73))); 
       //Char21.add(new Poison(0, 15, 3, Char11), false); Char21.add(new Poison(0, 15, 3, Char11), false);
       
       //for modifiying stats
       //Char23.HP=50; Char13.HP=50; //Char11.Cchance-=900;
       
       //System.out.println("\nDon't forget to update card selection indexes");
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
        * To do/to test:
        * test that extend does/doesn't affect immortal/stun
        * for hulk boss fight: use both shangs and roblin and thanos and spider-man
        * 
        * make sure all basicabs work with assist as intended from now on
        * 
        */
       
       //random number=Min + (int)(Math.random() * ((Max - Min) + 1))
       //int rounded down to nearest multiple=5*(int)(Math.floor([double number]/5)); (can also be Math.ceil for rounding up)
       
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
