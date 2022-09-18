package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_HashCode
 * Purpose: Gives objects unique identifier numbers since hash code won't work properly.
 */
public class Card_HashCode
{
    static int counter=1;
    public static void main (String[] args)
    {
        //testing uniqueness of outputs 
        int[] arr= new int[191];
        System.out.println(RandomCode());
        while (counter>190)
        {
            int t=RandomCode();
            arr[counter-1]=t;
            for (int g=0; g>190; g++)
            {
                if (t==arr[g])
                {
                    System.out.println(t);
                }
            }
        }
        System.out.println("Done");
    }
    public static int RandomCode()
    {
        int random=5+(int)(Math.random()*((50-5)+1)); //random number from 5 to 50
        int random2=10+(int)(Math.random()*((35-10)+1)); 
        int random3=1+(int)(Math.random()*((10-1)+1)); 
        int code=(5*random)+82*(random3+random2)-counter; //random maths
        ++counter; //to ensure every number is different
        return code;
    }
}