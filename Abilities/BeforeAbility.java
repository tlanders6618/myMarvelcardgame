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
class ActivateP extends BeforeAbility //ability activates a hero's passive
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
            case 13: StaticPassive.Drax(user, null, true); break;
            case 23: ++user.passivecount; ++user.passivecount; System.out.println(user.Cname+" gained 2 Energy.");
            for (StatEff e: user.effects) //update displayed energy count
            {
                if (e instanceof Tracker)
                e.Attacked(user, null, 616);
            }
            break;
            case 24: ++user.passivecount; System.out.println(user.Cname+" gained 1 Energy."); //since binary doesn't use it; gives marvel 1 energy instead of 2 
            for (StatEff e: user.effects) 
            {
                if (e instanceof Tracker)
                e.Attacked(user, null, 616);
            }
            break;
        }
        return 0;
    }
}
class ApplyShatter extends BeforeAbility //shatter applies before attacking, and thus cannot simply be added to statstrings; this is used for mighty blows too for the same reason
{
    int chance; int duration; boolean debuff;
    public ApplyShatter (int chancer, int dur, boolean deb)
    {
       chance=chancer; duration=dur; debuff=deb;
    }
    @Override
    public int Use (Character user, Character target)
    {
        boolean ok=true;
        StatEff shatter=null;
        if (debuff==true)
        {
            String nchance=String.valueOf(chance);
            String ndur=String.valueOf(duration);
            String[] initial={"Shatter", nchance, "616", ndur, "false"};
            String[][] sjatter=StatFactory.MakeParam(initial, null);
            shatter=StatFactory.MakeStat(sjatter, user);
        }
        if (user.activeability.blind=false)
        {
            Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
        }
        if (user.binaries.contains("Missed")||target.team1==user.team1) //to avoid shattering an ally due to mighty blows adding this to all the user's abs
        {
            ok=false;
        }
        if (ok==true&&(target.immunities.contains("Debuffs")||target.immunities.contains("Shatter")))
        {
            ok=false; String start=null;
            if (debuff==true)
            start=user.Cname+"'s "+shatter.geteffname();
            else
            start=user.Cname+"'s Shatter";
            System.out.println(start+" failed to apply due to an immunity.");
        }
        if (ok==true&&(user.CheckFor(user, "Neutralise", false)==true&&!(user.ignores.contains("Neutralise"))))
        {
            ok=false; String start=null;
            if (debuff==true)
            start=user.Cname+"'s "+shatter.geteffname();
            else
            start=user.Cname+"'s Shatter";
            System.out.println(start+" could not be applied due to a conflicting status effect.");
        }
        if (ok==true)
        {
            boolean yes=CoinFlip.Flip(chance+user.Cchance); 
            if (yes==true)
            {
                if (debuff==false)
                System.out.println(target.Cname+" was Shattered!");
                target.SHLD=0;
                ArrayList<StatEff>modexception= new ArrayList<StatEff>();
                if (target.effects.size()>0)
                {
                    modexception.addAll(target.effects);
                    for (StatEff eff: modexception)
                    {
                        if (eff.getefftype().equalsIgnoreCase("Defence"))
                        {
                            target.remove(target, eff.hashcode, "normal");
                        }
                    }
                }
                if (debuff==true)
                {
                    StatEff.CheckApply(user, target, shatter);
                }
            }
            else 
            {
                String start;
                if (debuff==true)
                start=user.Cname+"'s "+shatter.geteffname();
                else
                start=user.Cname+"'s Shatter";
                System.out.println(start+" failed to apply due to chance.");
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
        Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
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
class DamageCounter extends BeforeAbility //increase damage of attack based on number of target's stateffs of certain type; for characters like rogue 
{
    int amount; //of bonus dmg per eff
    boolean unique; //whether to count duplicate effs or not
    String name; //of efftype/name to check for; e.g. debuffs or intensify
    boolean type; //whether to check for efftype or effimmunityname
    boolean self; //whether to check self or enemy for the effs
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
            else //check if immunityname matches name
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
            else //check if immunityname matches name
            {
                for (StatEff eff: target.effects)
                {
                    if (eff.getimmunityname().equalsIgnoreCase(name))
                    {
                        dupe=eff.getefftype();
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
class DamageCounterRemove extends BeforeAbility //increase damage based on number of target stat effs, and then remove all; for characters like carnage
{ 
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
        Damage_Stuff.CheckBlind(hero); hero.activeability.blind=true;
        if (!(hero.binaries.contains("Missed")))
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
class DamageCounterSimple extends BeforeAbility //just checks if the target has an eff, instead of how many of it they have; for miles morales, mac gargan, etc
{
    int amount; //of bonus dmg 
    String name; //of efftype/name to check for; e.g. debuffs or intensify
    boolean type; //whether to check for efftype or effimmunityname
    boolean self; //whether to check self or enemy for the eff
    public DamageCounterSimple (int namount, String nname, boolean etype, boolean self) 
    {
        amount=namount; name=nname; type=etype; this.self=self;
    }
    @Override
    public int Use (Character user, Character target)
    {
        int send=0;
        if (self==true)
        {
            boolean got=user.CheckFor(user, name, type);
            if (got==true)
            {
                send=amount;
            }
        }
        else
        {
            boolean got=target.CheckFor(target, name, type);
            if (got==true)
            {
                send=amount;
            }
        }
        return send;
    }
}
class DebuffMod extends BeforeAbility //for altering the debuffs an ab applies, e.g. Superior Spidey and MODOK
{
    int index=0; //exactly kind of debuff modification is needed depends based on the hero 
    int ab=0; //also may vary based on the ab the hero uses
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
            if (user.index==2&&target.CheckFor(target, "Protect", false)==true) //protected enemies have +1 bleed dur; make sure rogue and adaptoid don't copy her passive
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
            case 13: //modern drax
            if (user.index==13)
            StaticPassive.Drax(user, target, false); //check with debuff to apply
            if (user.passivecount==-2) //double passive proc
            {
                String[] bloody= {"Bleed", "100", "65", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            else if (user.passivecount==-1) //passive proc
            {
                String[] bloody= {"Bleed", "100", "50", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            else //no passive
            {
                String[] bloody= {"Bleed", "100", "35", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            break;
            case 19: //miles
            if (target.CheckFor(target, "Shock", false)==true)
            {
                String[] horseson={"Undermine", "500", "616", "1", "false"}; String[][] albert=StatFactory.MakeParam(horseson, null); user.activeability.AddTempString(albert);
            }
            break;
            case 20: //superior
            if (target.CheckFor(target, "Tracer", false)==true)
            {
                String []akaban={"Countdown", "100", "80", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            else
            {
                String []akaban={"Countdown", "100", "70", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            break;
            case 22: //KK
            if (user.CheckFor(user, "Evasion", false)==true) 
            {
                if (ab==2)
                {
                    String[] light={"Provoke", "100", "616", "1", "false"}; String[][] baby=StatFactory.MakeParam(light, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==5)
                {
                    String[] kira={"Disarm", "100", "616", "1", "false"}; String[][] fin=StatFactory.MakeParam(kira, null); 
                    user.activeability.AddTempString(fin); 
                }
            }
            if (user.CheckFor(user, "Mighty Blows", false)==true) 
            {
                if (ab==2)
                {
                    String[] mello={"Terror", "100", "616", "1", "false"}; String[][] baby=StatFactory.MakeParam(mello, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==5) //shatter is applied before attacking
                {
                    if (user.activeability.blind==false)
                    {
                        Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
                    }
                    if (!(user.binaries.contains("Missed")))
                    {
                        Shatter s= new Shatter (100, 1); boolean yes=CoinFlip.Flip(100+user.Cchance);
                        if (yes==true)
                        StatEff.CheckApply(user, target, s);
                        else
                        StatEff.applyfail(user, s, "chance");
                    }
                }
            }
            break;
            case 28: //arachnaught
            if (target.summoned==true)
            {
                String []akaban={"Weakness", "100", "5", "2", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
                String[] temper={"Provoke", "100", "616", "2", "false"}; String[][] invasion=StatFactory.MakeParam(temper, null); user.activeability.AddTempString(invasion);
            }
            else
            {
                String []akaban={"Weakness", "100", "5", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
                String[] temper={"Provoke", "100", "616", "1", "false"}; String[][] invasion=StatFactory.MakeParam(temper, null); user.activeability.AddTempString(invasion);
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
   public int Use (Character hero, Character target) //beforeab
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
   public int Use (Character hero, int noodle, Character target) //specialability to undo effects after attack; not an afterab to ensure bonus stays active when applying stateffs
   {
      if (success==true)
      {
         success=false;
         Ignore.Execute(hero, toig, false);
      }
      return 0;
   }
   public static void Execute (Character hero, String todo, boolean add) //add or remove thing to be ignored
   {
      if (add==true)
      {
          switch (todo)
          {
              case "targeting effects": CoinFlip.IgnoreTargeting (hero, true); break;
              case "Defence": hero.ignores.add("Defence"); break;
              case "Blind": hero.ignores.add("Blind"); break; 
              case "Evade": hero.ignores.add("Evade"); break;
              case "Counter": hero.ignores.add("Counter"); break;
              case "Inescapable": CoinFlip.AddInescapable (hero, true); break;
              case "Missed": hero.immunities.add("Missed"); break;
              case "Afflicted": hero.ignores.add("Afflicted"); break;
              case "Neutralise": hero.ignores.add("Neutralise"); break;
              case "Undermine": hero.ignores.add("Undermine"); break;
          }
      }
      else
      {
          switch (todo)
          {
              case "targeting effects": CoinFlip.IgnoreTargeting (hero, false); break;
              case "Defence": hero.ignores.remove("Defence"); break;
              case "Blind": hero.ignores.remove("Blind"); break;
              case "Evade": hero.ignores.remove("Evade"); break;
              case "Counter": hero.ignores.remove("Counter"); break;
              case "Inescapable": CoinFlip.AddInescapable (hero, false); break;
              case "Missed": hero.immunities.remove("Missed"); break;
              case "Afflicted": hero.ignores.remove("Afflicted"); break;
              case "Neutralise": hero.ignores.remove("Neutralise"); break;
              case "Undermine": hero.ignores.remove("Undermine"); break;
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