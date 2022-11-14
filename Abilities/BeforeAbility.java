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
class ActivateP extends BeforeAbility
{
    int name;
    public ActivateP (int index)
    {
        name=index;
    }
    @Override
    public int Use (Character user, Character target)
    {
        switch (name)
        {
            case 11: ActivePassive.FuryJr(user, true, false); user.helpers.add(new BonusTurnHelper()); break;
        }
        return 0;
    }
}
class BeforeNullify extends BeforeAbility
{
    String[] effname;
    int chance; 
    int duration;
    int number;
    String type;
    boolean together; //true for together and false for separate
    public BeforeNullify (String t, int nullifychance, int num, String[] mnameofbufftonullify, boolean tog)
    {
        chance=nullifychance; effname=mnameofbufftonullify; number=num;  together=tog; type=t;
    }
    @Override
    public int Use (Character user, Character target)
    {
        if (!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))&&!(user.binaries.contains("Missed"))) 
        {
            boolean nomiss=Damage_Stuff.CheckBlind(user); boolean evaded=Damage_Stuff.CheckEvade(user, target, nomiss);
            int h=effname.length; String[] efftype=new String[h];
            for (int i=0; i<h; i++)
            {
                efftype[i]="Buffs";
            }
            ArrayList <StatEff> effs=Card_CoinFlip.GetEffs(target, effname, efftype);  
            if (effs.size()>0&&nomiss==true&&evaded==false)
            {
                if (type.equals("chosen"))
                {
                    NullifyChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    NullifyRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    NullifyAll(user, target, effs);
                }
                else
                {
                    System.out.println("Nullify failed due to a spelling error.");
                }
            }
        }
        else
        {
            System.out.println (user.Cname+"'s Nullify failed to apply due to an immunity.");
        }
        return 0;
    }
    public void NullifyChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println (hero.Cname+", choose "+todo+" buff(s) to Nullify on "+target.Cname+".");
            System.out.println ("Enter the number next to it, not its name."); 
            for (int i=0; i<todo; i++)
            {
                boolean falg=false; int counter=0; int index=616; 
                for (StatEff eff: effs)
                {
                    int o=counter+1;  
                    System.out.println(o+": "+eff.geteffname());
                    ++counter;
                }            
                do
                {
                    index=Damage_Stuff.GetInput();
                    --index;
                    if (index>=0&&index<effs.size())
                    {
                        falg=true;
                    }
                }
                while (falg==false);
                StatEff ton= effs.get(index);
                target.remove(target, ton.getcode());     
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Nullified!");
                effs.remove(index);
            }
        }
    }
    public void NullifyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                todo=0;
            }
        }
        else
        {
            for (int i=0; i<effs.size(); i++)
            {
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0)
        {
            for (int i=0; i<todo; i++)
            {
                int rando=(int) (Math.random()*(effs.size()-1));
                StatEff get=effs.get(rando);
                target.remove(target, get.getcode());
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Nullified!");
                effs.remove(rando);
            }
        }
    }
    public void NullifyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    target.remove(target, eff.getcode()); 
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=Card_CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    target.remove(target, eff.getcode()); 
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class ChooseStat extends BeforeAbility //choose one effect to apply
{
   String[] choice1; String[] choice2; String[] choice3; int choicenum=0;
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
      System.out.println("Choose a status effect to apply. Type the number next to its name.");
      if (choice1!=null)
      {
         System.out.println("1. "+choice1[0]);
      }
      if (choice2!=null)
      {
         System.out.println("2. "+choice2[0]);
      }
      if (choice3!=null)
      {
         System.out.println("3. "+choice3[0]);
      }
      int choice=0;
      do 
      {
         choice=Damage_Stuff.GetInput();
      }
      while (choice>4&&choice<0&&choice>choicenum);       
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
      return 0;
   }
}
class DamageCounterBroad extends BeforeAbility //increase damage based on number of target buffs, debuffs, or def effs
{//needed for characters like rogue 
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
{ //needed for characters like carnage
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
class Ignore extends BeforeAbility
{
   String condition; String toadd; int condnumber; boolean success=false;
   public Ignore (String add, String cond, int number)
   {
      condition=cond; toadd=add; condnumber=number;
   }
   @Override
   public int Use (Character hero, Character target)
   {
      switch (condition) //if conditions are met, add whatever needs to be ignored to the hero's ignore arraylist
      {
         case "always": Ignore.Execute(hero, toadd); success=true; break;
         case "enemy health below": if (target.HP<=condnumber)
         {
            Ignore.Execute(hero, toadd); success=true;  
         } break; 
      }
      return 0;
   }
   @Override
   public void Use (Character hero, Character target, int noodle) //afterability to undo effects after attack
   {
      if (success==true)
      {
         success=false;
         Ignore.IgnoreUndo(hero, toadd);
      }
   }
   public static void Execute (Character hero, String todo)
   {
      switch (todo)
      {
         case "targeting effects": Card_CoinFlip.IgnoreTargeting (hero, true); break;
         case "defence": hero.ignores.add("Defence"); break;
         case "blind": hero.ignores.add("Blind"); break; 
         case "inescapable": Card_CoinFlip.AddInescapable (hero, true); break;
      }
   }
   public static void IgnoreUndo (Character hero, String toremove)
   {
      switch (toremove)
      {
         case "targeting effects": Card_CoinFlip.IgnoreTargeting (hero, false); break;
         case "defence": hero.ignores.remove("Defence"); break;
         case "blind": hero.ignores.remove("Blind"); break;
         case "inescapable": Card_CoinFlip.AddInescapable (hero, false); break;
      }
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