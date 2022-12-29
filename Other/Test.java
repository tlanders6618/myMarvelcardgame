package myMarvelcardgamepack;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.*;
public class Test 
{
   public static void main (String[] args)
   {
       //System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       Character[] team1=new Character[6]; Character[] team2=new Character[6];
       team1[0]=new Summon (1);
       team2[0]= new Hero (2);
       team1[1]=new Summon (1);
       team2[1]= new Hero (2);
       team1[2]=new Summon (1);
       team2[2]= new Hero (2);
       team1[3]=new Summon (3);
       team2[3]= new Hero (5);
       team1[4]=new Summon (3);
       team2[4]= new Hero (5);
       team1[5]=new Summon (3);
       team2[5]= new Hero (5);
       for (int i=0; i<22; i++)
       {
           //team1[0].effects.add(new Obsession());
           //team2[0].effects.add(new Obsession());
        }
       Scoreboard.main(team1, team2);
       /** 
        for (int i = 0; i < words.length; i++) 
       { //this equals to the row in our matrix.
         for (int j = 0; j < words[i].length; j++) 
         { //this equals to the column in each row.
            //System.out.print(words[i][j] + " ");
         }
         System.out.println("\n");
         //This prints the contents of a 2d array; save for later
       }*/
   }
}