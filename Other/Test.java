public class Test 
{
all buff abilities should have a cooldown
target filter doesn't account for 2 enemies being banished at same time
  
    }    
   public static Ability MakeAbX23 (int counter) 
     {
        switch (counter)
        {
            case 0: BasicAb karate= new BasicAb ("Karate Kick", "single", "enemy", 45);
            return karate; 
            case 1: BasicAb slash= new BasicAb ("Precision Slash", "single", "enemy", 35); String[] sun=StatFactory.SetParam("Precision", "100", "616", "2", "true");
            slash.AddStatString(sun);
            return slash; //make precision and counter and check shatter for accuracy
            case 2: return null;
            case 3: AttackAb out= new AttackAb("Bleed Them Out", "single", "enemy", 60, 3); String[] bled=StatFactory.SetParam("Bleed", "100", "20", "2", "false");
            out.AddStatString(bled);
            return out; 
            case 4: AttackAb trip= new AttackAb ("Triple Slash", "single", "enemy", 30, 3); trip.multi=2; trip.special.add(new Multichain(trip.multi) );
            return trip;
        }
        return null;
    }  
   public static Ability MakeAbWolvie (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb x= new BasicAb("X-Slash", "single", "enemy", 35); String[] julie=StatFactory.SetParam("Bleed", "50", "15", "1", "false");
            return x; 
            case 1: BasicAb primal= new BasicAb ("Primal Punch", "single", "enemy", 45); 
            return primal; 
            case 2: return null; 
            case 3: return null; 
            case 4: AttackAb best= new AttackAb ("Best There Is", "single", "enemy", 100, 5); best.special.add(new DamageCounterRemove (all but other on self));
            return best;
        }
        return null;
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
