package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: BeforeAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
import java.util.ArrayList; 
public abstract class BeforeAbility extends SpecialAbility //used before a hero attacks, modifying damage
{
   public BeforeAbility()
   {
   }
}
class BeforeNullify extends BeforeAbility
{
    String buffname; 
    String number; 
    int chance; 
    boolean choice; //player chooses buff or not; true for yes and false for random buffs
    boolean together; //true for together and false for separate
    public BeforeNullify (int nullifychance, String numbertoremove, boolean randomorchosen, String mnameofbufftonullify, boolean tog)
    {
        chance=nullifychance; buffname=mnameofbufftonullify; number=numbertoremove; choice=randomorchosen; together=tog;
    }
    @Override
    public int Use (Character user, Character target)
    {
        boolean succeed=true;  
        ArrayList<StatEff> removal= new ArrayList <StatEff>();
        int available=0; //how many buffs hero can nullify after checking chance
        if (choice==false&&!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))) //ability has random nullifies
        {
            if (together==true) //the application chance is rolled once for all the effects
            {
                succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                if (succeed==false)
                {
                    System.out.println (user.Cname+"'s Nullify failed to apply due to chance."); //(Test) The application chance was "+chance);
                }
                else if (!(number.equalsIgnoreCase("all")))
                {
                    available=Integer.valueOf(number);
                }
            }
            else //chance calculated separately
            {
                for (StatEff eff: target.effects)
                {
                    if (!(buffname.equalsIgnoreCase("all"))) //nullify specific buffs on the target, e.g. Counter
                    {
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&eff.geteffname().equalsIgnoreCase(buffname))
                        {
                            succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                            if (succeed==true)
                            {
                                removal.add(eff); //stores which effects will be removed
                                ++available;
                            }
                        }
                    }
                    else //nullify all buffs on the target, regardless of type
                    {
                        succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                        if (succeed==true)
                        {
                            removal.add(eff); //stores which effects will be removed
                            ++available;
                        }
                    }
                }
            }
        }
        else if (choice==true) //choose an effect to remove
        {
            if (together==true) //the application chance is rolled once for all the effects
            {
                succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                if (succeed==false)
                {
                    System.out.println (user.Cname+"'s Nullify failed to apply due to chance."); //(Test) The application chance was "+chance);
                }
                else if (!(number.equalsIgnoreCase("all")))
                {
                    available=Integer.valueOf(number);
                }
            }
            else //chance calculated separately
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getefftype().equalsIgnoreCase("Buffs")) //the player can choose from all of the target's buffs
                    {
                        succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                        if (succeed==true)
                        {
                            ++available;
                        }
                    }
                }
            }
        }
        if (!(number.equalsIgnoreCase("all"))&&succeed==true) //remove certain number
        {
            if (choice==false) //remove randomly
            {
                while (available>0)
                {
                    if (target.effects!=null)
                    {
                        StatEff removve=target.GetRandomStatEff(target, "Buffs"); 
                        target.remove (target, removve.getcode());
                        System.out.println(target.Cname+"'s "+removve.geteffname()+" was Nullified!");
                        --available;
                    }
                }
            }
            else //choose buffs to remove
            {
                if (user.team1==true)
                {
                    System.out.println ("Player 1, choose a buff to Nullify from "+target.Cname+".");
                }
                else
                {
                    System.out.println ("Player 2, choose a buff to Nullify from "+target.Cname+".");
                }
                while (available>0)
                {
                    int counter=0; int removeme=616;                    
                    if (target.effects.size()!=0)
                    {
                        for (StatEff eff: target.effects)
                        {
                            if (eff.getefftype().equalsIgnoreCase("Buffs"))
                            {
                                int o= counter+1;
                                System.out.println(o+": "+eff.geteffname());
                                ++counter;
                            }                            
                        }         
                        System.out.println ("Enter the number next to it, not its name."); boolean falg=false;
                        do
                        {
                            removeme=Damage_Stuff.GetInput();
                            --removeme;
                            if (removeme>=0&&removeme<target.effects.size())
                            {
                                falg=true;
                            }
                        }
                        while (falg==false);
                        StatEff gone= target.effects.get(removeme);
                        target.remove(target, gone.getcode());
                        System.out.println(target.Cname+"'s "+gone.geteffname()+" was Nullified!");
                    }
                    --available;
                }
            }
        }
        else if (!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))) //remove all
        {
            if (succeed==true)
            {
                ArrayList<StatEff>concurrent= new ArrayList<StatEff>();
                if (target.effects.size()!=0)
                {
                    concurrent.addAll(target.effects);
                }
                for (StatEff eff: concurrent)
                {
                    if (eff.getefftype().equalsIgnoreCase("Buffs"))
                    {
                        target.remove(target, eff.getcode());
                        System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                    }
                } 
            }
        }
        return 0;
    }
}
class ChooseStat extends BeforeAbility //choose one effect to apply
{
   String[] choice1; String[] choice2; String[] choice3; int choicenum;
   public ChooseStat(String[] one, String[] two, String[] three)
   {
      choice1=one; choice2=two; choice3=three;
      if (one!=null)
      {
         ++choicenum;
      }
      if (two!=null)
      {
         ++choicenum;
      }
      if (three!=null)
      {
         ++choicenum;
      }
   }
   @Override
   public int Use(Character hero, Character target)
   {
      System.out.println("Choose a status effect to apply. Type the number next to its name");
      int choice=0;
      do 
      {
         choice=DamageStuff.GetInput();
      }
      while (choice<4&&choice>0&&CHOICENUMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM);       
      if (choice==1)
      {
         hero.activeability[0].AddTempString(choice1);
      }
      else if (choice==2)
      {
         hero.activeability[0].AddTempString(choice2);
      }
      else if (choice==3)
      {
         hero.activeability[0].AddTempString(choice3);
      }      
   }
}
class DamageCounterBroad extends BeforeAbility //increase damage based on number of target buffs, debuffs, or def effs
{//needed for characters like rogue and ronan
    int amount; String unique; String name;
    public DamageCounterBroad (String nunique, String nname, int namount)
    {
        amount=namount; unique=nunique; name=nname;
    }
    @Override
    public int Use(Character zebra, Character target)
    {
        int dmgincrease=UseDamageCounterBroad(target, amount, unique, name);
        return dmgincrease;
    }
    public int UseDamageCounterBroad (Character target, int amount, String unique, String name) 
    {
        int increase=0; //the amount of extra damage the attack will do
        if (unique.equalsIgnoreCase("false"))
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getefftype().equalsIgnoreCase(name)) //buffs, debuffs, heal, or defence
                {
                    increase+=amount;
                }
            }
        }
        else if (unique.equalsIgnoreCase("true")) //for characters like ultron; only unique buffs count towards the bonus
        {
            ArrayList<String> dupes= new ArrayList<String>();
            String dupe;
            for (StatEff eff: target.effects)
            {
                if (eff.getefftype().equalsIgnoreCase(name))
                {
                    dupe=eff.getimmunityname();
                    if (!(dupes.contains(dupe))) //it's unique; otherwise nothing happens
                    {
                        increase+=amount;
                        dupes.add(dupe);
                    }
                }
            }
        }
        return increase;
    }
}
class DamageCounterRemove extends BeforeAbility //increase damage based on number of target stat effs, and then remove all
{ //needed for characters like moonstone or carnage
    int amount; String name;
    public DamageCounterRemove(String nname, int namount)
    {
        amount=namount; name=nname;
    }
    @Override
    public int Use(Character zebra, Character target)
    {
        int dmgincrease=UseDamageCounterRemove(target, amount, name);
        return dmgincrease;
    }
    public int UseDamageCounterRemove (Character target, int amount, String name) 
    {
        int increase=0; //the amount of extra damage the attack will do for each
        for (StatEff eff: target.effects)
        {
            if (eff.getimmunityname().equalsIgnoreCase(name)) //specific name, e.g. bleed or pain
            {
                increase+=amount;
                target.remove(target, eff.getcode());
            }
        }
        return increase;
    }
}
class SelfDMG extends BeforeAbility
{
    int amount;
    public SelfDMG (int amot)
    {
        amount=amot;
    }
    @Override
    public int Use (Character hero, Character ignored)
    {
        amount-=hero.ADR;
        if (hero.SHLD>=amount) 
        {
            hero.SHLD-=amount; 
        }
        else if (hero.SHLD<amount) //shield broken; can't absorb all the damage
        {
            hero.HP=(hero.HP+hero.SHLD)-amount; 
            hero.SHLD=0;
        }
        System.out.println (hero.Cname+" took "+amount+" damage");
        if (hero.HP<=0)
        {
            hero.HP=0;
            hero.onLethalDamage(hero, null, "other");
        }
        return 0;
    }
}
