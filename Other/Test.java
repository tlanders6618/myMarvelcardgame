public class Test 
{
   public static Ability MakeAbDrax (int counter) 
    {
        switch (counter)
        {
            case 0: BasicAb headbutt =new BasicAb ("Headbutt", "single", "enemy", 45); 
            cdart.AddStatString(despair); 
            return headbutt; 
            case 1: AttackAb twins= new AttackAb("Twins Blades", "single", "enemy", 80, 3); 
            String[] despair=StatFactory.SetParam("Bleed", "100", "5", "2", "false");
            String[] knock=StatFactory.SetParam("Bleed", "100", "5", "2", "false"); 
            twins.AddStatString(despair); twins.AddStatString(knock); twins.together=false;
            String[] life= StatFactory.SetParam("Drain", "500", "616", "2", "true", "Half"); twins.AddStatString(life);
            return twins; 
            case 2: DefAb fierce= new DefAb ("Fierce Protector", "single", "Ally exclusive", 3); String[] gloom=StatFactory.SetParam ("Protect", "500", "1", "616", "true");
            String[] one=StatFactory.SetParam("Counter", chance, "35", "2", "true"); 
            String[] two=StatFactory.SetParam("Counter", chance, "35", "2", "true"); 
            fierce.AddStatString(gloom); fierce.AddStatString(one); fierce.AddStatString(two);
            return fierce; 
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "single", "enemy", 80, 3); dice.special.add (new Ricochet (500));
            return dice; 
            case 4: AttackAb destroy= new AttackAb ("The Destroyer", "single", "enemy", 120, 4);
            //append shatter method for 1 turn here
            return destroy;
        }
        return null;
    }    
   public static Ability MakeAbX23 (int counter) 
     {
        switch (counter)
        {
            case 0: 
            return headbutt; 
            case 1: 
            return twins; 
            case 2: 
            return fierce; 
            case 3: 
            return dice; 
            case 4: 
            return destroy;
        }
        return null;
    }  
   public static Ability MakeAbWolvie (int counter) 
    {
        switch (counter)
        {
            case 0: 
            return headbutt; 
            case 1: 
            return twins; 
            case 2: 
            return fierce; 
            case 3: 
            return dice; 
            case 4: 
            return destroy;
        }
        return null;
    }  
   
   ApplyShatter extends Before Ab 
   {
      int chance; int duration;
      ApplyShatter (int chancer, int dur)
      {
         chance=chancer; duration=dur;
      }
      @Override
      Use
      {
         if (!(hero.binaries.contains("Missed")))
         {
            coinflip with chance+Cchance;
            if it applies
            {
               String[] shatter=StatFactory.SetParam("Shatter", chance.toString() wont work, "616", durtostring, "false");
               Make stateff and check apply the normal way
            }
               else nothing
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
