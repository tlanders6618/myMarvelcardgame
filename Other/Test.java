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
            case 2: DefAb fierce= new DefAb ("Fierce Protector", "single", "ally exclusive", 3); String[] gloom=StatFactory.SetParam ("Protect", "500", "1", "616", "true");
            String[] one=StatFactory.SetParam("Counter", chance, "35", "2", "true"); 
            String[] two=StatFactory.SetParam("Counter", chance, "35", "2", "true"); 
            fierce.AddStatString(gloom); fierce.AddStatString(one); fierce.AddStatString(two);
            return fierce; 
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "single", "enemy", 80, 3); dice.special.add (new Ricochet (500));
            return dice; 
            case 4: AttackAb destroy= new AttackAb ("The Destroyer", "single", "enemy", 120, 4);
            //append shatter method for 1 turn here
            //og drax obsession passive should be (500+cchance)
            return destroy;
        }
        return null;
    }    
   public static Ability MakeAbX23 (int counter) 
     {
        switch (counter)
        {
            case 0: BasicAb karate= new BasicAb ("Karate Kick", "single", "enemy", 45);
            return karate; 
            case 1: BasicAb slash= new BasicAb ("Precision Slash", "single", "enemy", 35); String[] sun=StatFactory.SetParam("Precision", "100", "616", "2", "true");
            slash.AddStatString(sun);
            return slash; //rename the ab to drop the plural; also make precision and counter and check shatter for accuracy
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
            case 2: return null; //make regen
            case 3: return null; //make tracker eff for berserker also override the on death stateff removal to not remove it
            case 4: AttackAb best= new AttackAb ("Best There Is", "single", "enemy", 100, 5); best.special.add(new DamageCounterRemove (all but other on self));
            return best;
        }
        return null;
    }  
   
   //from now on, check blind before attacking but not during attacking; move it out of attack method so damage counter isn't wasted
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
