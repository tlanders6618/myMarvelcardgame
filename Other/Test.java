public class Test 
{
all buff abilities should have a cooldown
target filter doesn't account for 2 enemies being banished at same time
  
  //make four for wolvie; type is true, name is buff, debuff, heal, defence; amount is 10
  //fix random ricochet below
  
class Multichain extends AfterAbility
{
    int multis; //number of hits in attack
    int current=1; //current hit of attack
    public Multichain(int multiply)
    {
      multis=multiply;
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
  //after last hit become unusable
  
  
  public void UseMultichain (Character user, Ability ab) //similar to chain
    {
        int uses=multis-current; 
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();  
        int multi=multihit; int omulti=multihit;
        while (uses>0) 
        {
            int change=0;
            if (targets.size()<=0)
            {
                uses=-1;
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
            }
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely
                {
                    do 
                    {
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                        } 
                        damage+=change;
                        if (user.ignores.contains("Status effects"))
                        {
                            chump.HP-=damage;
                            if (chump.HP<=0)
                            {
                                chump.onLethalDamage(chump, user, "attack");
                            }
                        }
                        else if (lose==true)
                        {
                            chump.HP-=damage; //need to rewrite this to account for protect
                            chump.onAttacked(chump, user, 0);
                        }
                        else 
                        {
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        for (String[] array: tempstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array); 
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) 
                            {
                                otherapply.add(New);
                            }
                        }
                        for (String[] array: statstrings)
                        {  
                            StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                            if (array[4].equalsIgnoreCase("true"))
                            {
                                selfapply.add(New);
                            }
                            else if (!(user.binaries.contains("Missed"))&&array[4].equalsIgnoreCase("false")) //they cannot apply effects if the target evaded/they are blind
                            {
                                otherapply.add(New);
                            }
                        }
                        toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                        user.onAttack(user, chump); //activate relevant passives
                        if (aoe==false)
                        {
                            for (StatEff eff: user.effects) //undo empowerments
                            {
                                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                                {
                                    change=eff.UseEmpower(user, ab, damage, false);
                                }
                            }
                        }
                        if (selfapply.size()!=0)
                        {
                            selfapply.removeAll(selfapply); //ensures every status effect is unique, to avoid bugs
                        }
                        if (otherapply.size()!=0)
                        {
                            otherapply.removeAll(otherapply);
                        }
                        if (tempstrings.size()!=0) //these effects are only sometimes applied with attacks, hence the name temp; they're reset afterwards
                        {
                            tempstrings.removeAll(tempstrings);
                        }
                        if (user.binaries.contains("Missed"))
                        {
                            user.binaries.remove("Missed");
                        }
                        --multi;
                        damage=odamage; //reset damage 
                        dmgdealt=0;
                    }
                    while (multi>-1); //then repeat the attack for each multihit
                    multi=omulti; //reset the multihit counter for the next use
                }
                --uses;
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
