package myMarvelcardgamepack;
import java.util.Scanner;
import java.util.ArrayList;
public class Test 
{
   public static void main (String[] args)
   {
       boolean good=false; 
       int choice;
       String name;
       Scanner gather= new Scanner(System.in);
       Character[] team1= new Character[6]; Character[] team2= new Character[6];
       team1[0]=new Character(1);  
       team2[0]=new Character(2);
       team1[1]=new Character(1);
       team2[1]=new Character(2);
       team1[2]=new Character(1);       
       team2[2]=new Character(2);         
       team1[3]=null;
       //team2[3]=new Character(1);
       //team1[4]=new Character(2);
       //team2[4]=new Character(2);
       //team1[5]=new Character(1);
       //team2[5]=new Character(2);     
       team1[5]=new Character(2);
       team1[5].effects.add(new Bleed(100,100, 2));
       team2[2].effects.add(new Redwing()); 
       team2[1].effects.add(new ChanceDown(100, 3));
       //Scoreboard.main(team2, team1);
       System.out.println(System.getProperty("java.class.version")); //55 means 11, 56 means 12, etc
       
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