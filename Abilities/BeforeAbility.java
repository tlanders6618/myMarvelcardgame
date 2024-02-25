package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: BeforeAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
import java.util.ArrayList; 
public abstract class BeforeAbility extends SpecialAbility //used before a hero attacks, usually modifying damage
{
   public BeforeAbility()
   {
   }
}
class ActivateP extends BeforeAbility //activate passive
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
            case 11: ActivePassive.FuryJr(user, false, false, false, true); break;
        }
        return 0;
    }
}
class ApplyShatter extends BeforeAbility //shatter applies before attacking, as stated in the glossary
{
    int chance; int duration; boolean debuff;
    public ApplyShatter (int chancer, int dur, boolean deb)
    {
       chance=chancer; duration=dur; debuff=deb;
    }
    @Override
    public int Use (Character user, Character target)
    {
        if (user.CheckFor(user, "Neutralise")==false&&(!(user.binaries.contains("Missed"))||user.immunities.contains("Missed")))
        {
            boolean yes=CoinFlip.Flip(chance+user.Cchance); 
            if (debuff==true&&yes==true)
            {
                String nchance=String.valueOf(chance);
                String ndur=String.valueOf(duration);
                String[] initial={"Shatter", nchance, "616", ndur, "false"};
                String[][] sjatter=StatFactory.MakeParam(initial, null);
                StatEff shatter=StatFactory.MakeStat(sjatter);
                StatEff.CheckApply(user, target, shatter);
            }
            else if (debuff==false&&yes==true)
            {
                target.SHLD=0;
                ArrayList<StatEff>modexception= new ArrayList<StatEff>();
                if (target.effects.size()>0)
                {
                    modexception.addAll(target.effects);
                }
                for (StatEff eff: modexception)
                {
                    if (eff.getefftype().equalsIgnoreCase("Defence"))
                    {
                        target.remove(target, eff.hashcode, "normal");
                    }
                }
            }
        }
        return 0;
    }
}
class BeforeNullify extends BeforeAbility //same as nullify but before doing dmg
{
    String effname; //name of buff to nullify
    String[] efftype; //since amadeus can nullify things other than buffs
    int chance; 
    int number;
    String type;
    boolean together; //true for together and false for separate
    public BeforeNullify (int echance, int num, String type, String ename, boolean self, boolean tog, String[] etype)
    {
        chance=echance; effname=ename; number=num;  together=tog; this.type=type; efftype=etype;
    }
    @Override
    public int Use (Character user, Character target)
    {
        if (!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))&&(!(user.binaries.contains("Missed"))||user.immunities.contains("Missed"))) 
        {
            String[] neffname= new String [efftype.length]; 
            for (int i=0; i<efftype.length; i++) //this is really only for brawn; required for the overloaded CoinFlip.GetEffs 
            {
                neffname[i]=effname; 
            }
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, neffname, efftype); //the effs on the target that are eligible to be nullified
            Damage_Stuff.CheckEvade(user, target); //since this occurs before the attack method that normally checks evade, it must be checked here
            if (effs.size()>0&&!(user.binaries.contains("Missed")))
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
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==false)
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" buff(s) to Nullify from "+target.Cname+".");
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
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Nullified!");
                target.remove(target, ton.hashcode, "nullify");   
                effs.remove(index);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void NullifyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
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
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
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
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Nullified!");
                target.remove(target, get.hashcode, "nullify");
                effs.remove(rando);
                if (effs.size()<=0)
                {
                    i+=616;
                }
            }
        }
    }
    public void NullifyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                    target.remove(target, eff.hashcode, "nullify"); 
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
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {                    
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Nullified!");
                    target.remove(target, eff.hashcode, "nullify"); 
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class ChooseStat extends BeforeAbility //choose one effect to apply with the ability
{
   String[][] choice1; String[][] choice2; String[][] choice3; int choicenum=0;
   public ChooseStat(String[][] one, String[][] two, String[][] three)
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
         System.out.println("1. "+choice1[0][0]);
      }
      if (choice2!=null)
      {
         System.out.println("2. "+choice2[0][0]);
      }
      if (choice3!=null)
      {
         System.out.println("3. "+choice3[0][0]);
      }
      int choice=0;
      do 
      {
         choice=Damage_Stuff.GetInput();
      }
      while (choice>4&&choice<0&&choice>choicenum);       
      if (choice==1)
      {
         hero.activeability.AddTempString(choice1);
      }
      else if (choice==2)
      {
         hero.activeability.AddTempString(choice2);
      }
      else if (choice==3)
      {
         hero.activeability.AddTempString(choice3);
      }      
      return 0;
   }
}
class DamageCounter extends BeforeAbility //increase damage of attack based on number of target's stateffs of certain type; needed for characters like rogue 
{
    int amount; boolean unique; String name; boolean type; boolean self;
    public DamageCounter (String nname, boolean etype, int namount, boolean self, boolean unique) 
    {
        amount=namount; this.unique=unique; name=nname; type=etype; this.self=self;
    }
    @Override
    public int Use(Character hero, Character target)
    {
        int dmgincrease=0;
        if (!(hero.binaries.contains("Missed"))||hero.immunities.contains("Missed")) //either haven't missed yet, or are immune to it
        {
            if (self==true)
            dmgincrease=UseDamageCounter(hero, amount, unique, name);
            else
            dmgincrease=UseDamageCounter(target, amount, unique, name);
        }
        return dmgincrease;
    }
    public int UseDamageCounter (Character target, int amount, boolean unique, String name) 
    {
        int increase=0; //the amount of extra damage the attack will do
        if (unique==false)
        {
            if (type==true) //getefftype
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getefftype().equalsIgnoreCase(name)) //buffs, debuffs, heal, or defence
                    {
                        increase+=amount;
                    }
                }
            }
            else //check name
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase(name)) //buffs, debuffs, heal, or defence
                    {
                        increase+=amount;
                    }
                }
            }
        }
        else if (unique==true) //for characters like ultron; only unique buffs count towards the bonus
        {
            ArrayList<String> dupes= new ArrayList<String>();
            String dupe;
            if (type==true)
            {
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
            else
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase(name))
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
        }
        return increase;
    }
}
class DamageCounterRemove extends BeforeAbility //increase damage based on number of target stat effs, and then remove all
{ //needed for characters like carnage
    int amount; //how much extra dmg each stateff grants
    String name; //of stateff to check for and remove
    boolean type; //check for eff type or name
    boolean self;
    public DamageCounterRemove(String nname, boolean etype, int namount, boolean self)
    {
        amount=namount; name=nname; type=etype; this.self=self;
    }
    @Override
    public int Use(Character hero, Character target)
    {
        int dmgincrease=0;
        if (!(hero.binaries.contains("Missed"))||hero.immunities.contains("Missed"))
        {
            if (self==true)
            dmgincrease=UseDamageCounterRemove(hero, amount, name);
            else
            dmgincrease=UseDamageCounterRemove(target, amount, name);
        }
        return dmgincrease;
    }
    public int UseDamageCounterRemove (Character target, int amount, String name) 
    {
        int increase=0; //the amount of total extra damage the attack will do
        ArrayList<StatEff> concurrentmodificationexception= new ArrayList<StatEff>(); 
        if (type==true)
        { 
            for (StatEff eff: target.effects)
            {
                if (eff.getefftype().equalsIgnoreCase(name)) //specific name, e.g. bleed or pain
                {
                    increase+=amount;
                    concurrentmodificationexception.add(eff);
                }
            }
        }
        else
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase(name)) //specific name, e.g. bleed or pain
                {
                    increase+=amount;
                    concurrentmodificationexception.add(eff);                    
                }
            }
        }
        for (StatEff eff: concurrentmodificationexception)
        {
            target.remove(target, eff.hashcode, "normal");
        }
        return increase;
    }
}
class DebuffMod extends BeforeAbility //for altering the debuffs an ab applies, e.g. Superior Spidey and MODOK
{
    int index=0; //exactly kind of debuff modification is needed depends based on the hero 
    int ab=0; //also varies based on the ab the hero uses
    public DebuffMod (int windex, int ability)
    {
        index=windex; ab=ability;
    }
    @Override
    public int Use (Character user, Character target)
    {
        switch (index)
        {
            case 2: //gamora's second passive
            if (target.CheckFor(target, "Protect")==true) //protected enemies have +1 bleed dur
            {
                if (ab==1)
                {
                    String[] light={"Bleed", "50", "20", "2", "false"}; String[][] baby=StatFactory.MakeParam(light, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==2)
                {
                    String[] kira={"Bleed", "50", "30", "2", "false"}; String[][] fin=StatFactory.MakeParam(kira, null); 
                    user.activeability.AddTempString(fin); 
                }
            }
            else //normal bleed dur
            {
                if (ab==1)
                {
                    String[] mello={"Bleed", "50", "20", "1", "false"}; String[][] baby=StatFactory.MakeParam(mello, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==2)
                {
                    String[] near={"Bleed", "50", "30", "1", "false"}; String[][] fin=StatFactory.MakeParam(near, null); 
                    user.activeability.AddTempString(fin); 
                }
            }
            break;
        }
        return 0;
    }
}
class Ignore extends BeforeAbility
{
   String condition; //whether the benefits are given every time the attack is activated, or only sometimes
   String toig; //what the hero is ignoring
   int condnumber; //only for conditions that require it, usually a hp threshold
   boolean success=false;
   public Ignore (String toignore, String cond, int number)
   {
      toig=toignore; condition=cond; condnumber=number;
   }
   @Override
   public int Use (Character hero, Character target)
   {
      switch (condition) //if conditions are met, add whatever needs to be ignored to the hero's ignore arraylist
      {
         case "always": Ignore.Execute(hero, toig, true); success=true; break;
         case "enemy health below": 
         if (target.HP<=condnumber) 
         {
             Ignore.Execute(hero, toig, true); success=true;
         }
         break; 
         case "passive": 
         if (hero.passivecount==1) //if the hero's passive has been triggered
         {
             Ignore.Execute (hero, toig, true); success=true;
         }
         break;
      }
      return 0;
   }
   @Override
   public void Use (Character hero, Character target, int noodle) //afterability to undo effects after attack
   {
      if (success==true)
      {
         success=false;
         Ignore.Execute(hero, toig, false);
      }
   }
   public static void Execute (Character hero, String todo, boolean add) //add or remove thing to be ignored
   {
      if (add==true)
      {
          switch (todo)
          {
              case "targeting effects": CoinFlip.IgnoreTargeting (hero, true); break;
              case "defence": hero.ignores.add("Defence"); break;
              case "blind": hero.ignores.add("Blind"); break; 
              case "inescapable": CoinFlip.AddInescapable (hero, true); break;
              case "Missed": hero.immunities.add("Missed"); break;
          }
      }
      else
      {
          switch (todo)
          {
              case "targeting effects": CoinFlip.IgnoreTargeting (hero, false); break;
              case "defence": hero.ignores.remove("Defence"); break;
              case "blind": hero.ignores.remove("Blind"); break;
              case "inescapable": CoinFlip.AddInescapable (hero, false); break;
              case "Missed": hero.immunities.remove("Missed"); break;
          }
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
class Summoning extends BeforeAbility
{
    int index;
    public Summoning (int inl)
    {
        index=inl;
    }
    @Override
    public int Use (Character hero, Character ignored)
    {
        Summon sum= new Summon (index);
        Battle.SummonSomeone(hero, sum);
        return 0;
    }
}