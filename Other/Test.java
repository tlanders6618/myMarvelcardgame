public class Test 
{
   public static Ability MakeAbDrax (int counter) 
    {
        String[] p= new String[5];
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
            case 3: AttackAb dice= new AttackAb("Slice and Dice", "single", "enemy", 80, 3); 
            return dice; 
            case 4: AttackAb Khonshu= new AttackAb ("Fist of Khonshu", "single", "enemy", 110, 4); String[] loss=StatFactory.SetParam("Stun", "100", "616", "616", "false");
            Khonshu.AddStatString (loss); 
            return Khonshu;
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
            if (i!=0)
            {
              Character bobby=team[i-1];
            }
            else if (i==0)
            {
               Character C12=team1[1]; Character C13=team1[2];
            }
            
         } //end if
      } //end loop
   } //end method
   
} //end class
