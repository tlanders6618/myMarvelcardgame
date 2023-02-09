public class Test 
{
all buff abilities should have a cooldown
target filter doesn't account for 2 enemies being banished at same time
  
  //make four for wolvie; type is true, name is buff, debuff, heal, defence; amount is 10
  //fix random ricochet below
  //test x23 against mysterio dummies outside of battle
  
class Multichain extends AfterAbility
{
    int multis; //number of hits in attack
    int current=1; //current hit of attack
    AttackAb ability;
    public Multichain(int multiply, AttackAb ability)
    {
      multis=multiply; this.ability=ability;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (target.dead==true&&current<multi) //doesn't activate on final hit because there's no more dmg to deal
        {
           UseMultichain(caller);
           current=1; //reset counter because attack was just finished on another target(s) 
        }
      else
      {
        if (current<multis)
        ++current;
        else 
        current=1; //reset counter because attack is over
      }
    }
}
  //override attack method so ronin can check if target has bleed
  //have ability.applystats check if targ is dead before applying for effiency
  //after last hit become unusable so it can only trigger once
  //merge onattack method with attack method
  
  public void UseMultichain (Character user) //similar to chain; precondition of ab.aoe being false
    {
        int uses=multis-current; int odamage=ability.damage; int damage=odamage;
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();  
        while (uses>0) 
        {
            int change=0; 
            Character chump=Ability.GetRandomEnemy (user, false);
            if (chump!=null)
            {
                            for (StatEff eff: user.effects) //undo empowerments from previous hit 
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ability, damage, false);
                                }
                            }
                        }
                 for (StatEff eff: user.effects) //get empowerments for the rest of the attack
                {
                   if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                   {
                       change=eff.UseEmpower(user, ability, damage, true);
                   }
                }
                        for (SpecialAbility ob: ability.special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                        } 
                        damage+=change;
                        chump=user.Attack(user, chump, damage, false); //damage formula is calculated here
                        for (SpecialAbility ob: ability.special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        for (String[] array: ability.tempstrings) LEFT OFF HERE true 
                        {  
                            StatEff New=StatFactory.MakeStat(array); 
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                ability.selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) 
                            {
                                ability.otherapply.add(New);
                            }
                        }
                        for (String[] array: ability.statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                ability.selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                ability.otherapply.add(New);
                            }
                        }
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                        for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ab, damage, false);
                                }
                            }
                        if (user.binaries.contains("Missed"))
                        {
                            user.binaries.remove("Missed");
                        }
                        damage=odamage; //reset damage 
                        ability.dmgdealt=0;
                }
                --uses;
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
