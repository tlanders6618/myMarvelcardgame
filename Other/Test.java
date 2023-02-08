public class Test 
{
all buff abilities should have a cooldown
target filter doesn't account for 2 enemies being banished at same time
  
  //make four for wolvie; type is true, name is buff, debuff, heal, defence; amount is 10
  //fix random ricochet below
  
  class Multichain extends AfterAbility
{
    int multi; //number of hits in attack
    int current=1; //current hit of attack
    public Multichain(int multiply)
    {
      multi=multiply;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (target.dead==true&&current<multi) //doesn't activate on final hit because there's no more dmg to deal
        {
            //new target
          ++multi;
        }
      else
      {
        if (current<multi)
        ++multi;
        else 
        current=1; //reset counter
      }
    }
}
  
   Speed (Character fast)
   {
      //check fast's team
      for (int i, etc)
      {
         if (team1[i]==fast)
         {
            //all code is in here
            make sure to check this code works both when speed is being applied on fast's turn and when it's being applied off their turn
            if (i!=0) //the character with speed is not in the first position of the array
            {
              Character C11=team[i-1]; //one in front of fast
              Character c13= //the one going right after fast
            }
            else if (i==0) //the one being snared is already going first but isn't necessarily taking their turn now
            {
               Character C12=team1[1]; Character C13=team1[2]; //team1[0]=fast already
            }
            
         } //end if
      } //end for loop
   } //end method
   
} //end test class
