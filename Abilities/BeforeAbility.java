
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
    int num=616;
    public ActivateP ()
    {
    }
    public ActivateP (int c)
    {
        num=c;
    }
    @Override
    public int Use (Character user, Character target)
    {
        switch (user.index)
        {
            case 11: ActivePassive.FuryJr(user, "activate", false); break; //kill mode activation
            case 13: StaticPassive.Drax(user, null, "knife"); break; //twin blades activation
            /*case 23: ++user.passivecount; System.out.println(user.Cname+" gained 1 Energy."); //gain energy when using her abs
            for (StatEff e: user.effects) 
            {
                if (e instanceof Tracker)
                e.Attacked(user, null, 616);
            }
            break;*/
            case 39: StatEff delete=null; //electro's shocking touch 
            for (StatEff e: user.effects)
            {
                if (e.getimmunityname().equals("Intensify")&&e.getefftype().equals("Other"))
                {
                    String[] stat={"Shock", "500", Integer.toString(e.power+10), Integer.toString(e.duration), "false"}; String[][]haha=StatFactory.MakeParam(stat, null);
                    user.activeability.AddTempString(haha); delete=e;
                    break;
                }
            }
            if (delete!=null)
            user.remove(delete.id, "normal");
            break;
            case 40: ActivePassive.Sandy(user, "ult"); break; //activate sandstorm
        }
        return 0;
    }
}
class ApplyShatter extends BeforeAbility //shatter applies before attacking, and thus cannot simply be added to statstrings; this is used for mighty blows too for the same reason
{
    int chance; int duration; 
    boolean debuff; boolean effect; //whether to apply an eff or just do defence strip
    boolean mighty; //if granted by mighty blows, should not affect the hero's ability desc
    boolean applied=false; //whether shatter was successfully applied or not; for mr fantastic and peter parker so they can't evade aoe abs that apply shatter
    public ApplyShatter (int chancer, int dur, boolean deb, boolean other, boolean m)
    {
       chance=chancer; duration=dur; debuff=deb; String start; effect=other; mighty=m;
       if (this.chance>=500)
       start="Applies";
       else
       start=this.chance+"% chance to apply";
       if (mighty==false) //else leave desc blank
       {
           if (deb==true)
           {
               if (effect==true)
               this.desc=start+" a(n) Shatter Effect for "+duration+" turn(s). ";
               else
               this.desc=start+" a(n) Shatter for "+duration+" turn(s). ";
           }
           else
           this.desc=start+" Shatter. ";
        }
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
            String name;
            if (effect==false)
            name="Shatter";
            else
            name="Shatter Effect";
            String[] initial={name, nchance, "616", ndur, "false"};
            String[][] sjatter=StatFactory.MakeParam(initial, null);
            shatter=StatFactory.MakeStat(sjatter, user);
        }
        if (user.activeability!=null&&user.activeability.blind==false)
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
        if (ok==true&&(user.CheckFor("Neutralise", false)==true&&!(user.ignores.contains("Neutralise"))))
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
                System.out.println("\n"+target.Cname+" was Shattered!");
                applied=true;
                target.SHLD=0;
                ArrayList<StatEff>modexception= new ArrayList<StatEff>(target.effects);
                if (target.effects.size()>0)
                {
                    for (StatEff eff: modexception)
                    {
                        if (eff.getefftype().equalsIgnoreCase("Defence"))
                        {
                            target.remove(eff.id, "normal");
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
    @Override
    public void Use (Character user, Character victim, int ignore) //afterab
    {
        this.applied=false; //resets after use
    }
    @Override
    public boolean CheckApply ()
    {
       return applied;
    }
}
class BeforeNullify extends BeforeAbility //same as nullify but quick; only wm and brawn use this for now
{
    String effname; //name of buff to nullify
    String[] efftype; //since amadeus can nullify things other than buffs
    int chance; 
    int number;
    String type; //chosen, random, all
    boolean together; //true for together and false for separate
    public BeforeNullify (int echance, int num, String type, String ename, boolean self, boolean tog, String[] etype)
    {
        chance=echance; effname=ename; number=num;  together=tog; this.type=type; efftype=etype;
        String Chance;
        if (this.chance>=500)
        Chance="Nullifies ";
        else
        Chance=this.chance+"% chance to Nullify ";
        String Number;
        if (num<99)
        Number=Integer.toString(num)+" ";
        else
        Number="all ";
        String Type="";
        if (!(type.equals("all")))
        Type=type+" ";
        String Buff;
        if (type.equals("all"))
        Buff="buffs";
        else if (effname.equals("any"))
        Buff="buff(s)";
        else
        Buff=effname+" buff(s)";
        String Self;
        if (self==true)
        Self="self. ";
        else
        Self="the target. ";
        this.desc=Chance+Number+Type+Buff+" on "+Self;
    }
    @Override
    public int Use (Character user, Character target)
    {
        if (user.activeability!=null&&user.activeability.blind==false) //since this occurs before the attack method that normally checks misses, it must be checked here
        {
            Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
        }
        if (!(user.binaries.contains("Missed"))&&user.activeability!=null&&user.activeability.evade==false)
        {
            Damage_Stuff.CheckEvade(user, target); user.activeability.evade=true;
        }
        if (!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))&&!(user.binaries.contains("Missed")))
        {
            String[] neffname= new String [efftype.length]; 
            for (int i=0; i<efftype.length; i++) //this is really only for brawn; required for the overloaded CoinFlip.GetEffs 
            {
                neffname[i]=effname; 
            }
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, neffname, efftype); //the effs on the target that are eligible to be nullified
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
        else if (!(user.binaries.contains("Missed"))) //if fail is caused by a miss, print nothing
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
            System.out.println ("\nChoose "+todo+" buff(s) to Nullify from "+target.Cname+".");
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
                target.remove(ton.id, "nullify");   
                String name=ton.getimmunityname(); 
                if (hero.index==30&&!(name.equals("Protect"))&&!(name.equals("Taunt"))) //brawn's passive; can copy indefinite dur stateffs bc why not
                {
                    int dur=ton.oduration; int pow=ton.power;
                    String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                    StatEff e=StatFactory.MakeStat(string, hero);
                    StatEff.CheckApply(hero, hero, e);
                }
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
                target.remove(get.id, "nullify");
                String name=get.getimmunityname(); 
                if (hero.index==30&&!(name.equals("Protect"))&&!(name.equals("Taunt")))
                {
                    int dur=get.oduration; int pow=get.power;
                    String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                    StatEff e=StatFactory.MakeStat(string, hero);
                    StatEff.CheckApply(hero, hero, e);
                }
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
                    target.remove(eff.id, "nullify"); 
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
                    target.remove(eff.id, "nullify"); 
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class Boost extends BeforeAbility //for abs with increased stat chance
{
    int amount;
    public Boost (int a)
    {
        amount=a; this.desc="+"+a+"% status chance. ";
    }
    @Override
    public int Use (Character hero, Character target)
    {
        hero.Cchance+=amount;
        return 0;
    }
    @Override
    public int Use (Character hero, int ignore, Character target) //specialab to undo so statchance increase stays active during stateff gen and application
    {
        hero.Cchance-=amount;
        return ignore;
    }
}
class ChooseStat extends BeforeAbility //choose one effect to apply with the ability
{
   String[][] choice1; 
   String[][] choice2; 
   String[][] choice3; 
   int choicenum=0;
   public ChooseStat(String[][] one, String[][] two, String[][] three)
   {
      choice1=one; choice2=two; choice3=three;
      if (three!=null)
      ++choicenum; 
      if (two!=null)
      ++choicenum; 
      if (one!=null)
      ++choicenum; 
      if (choicenum==1)
      this.desc="Can apply "+choice1[0][0]+". ";
      else if (choicenum==2)
      this.desc="Can apply either "+choice1[0][0]+" or "+choice2[0][0]+". ";
      else if (choicenum==3)
      this.desc="Can apply either "+choice1[0][0]+", "+choice2[0][0]+", or "+choice3[0][0]+". ";
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
      while (choice<=0&&choice>choicenum);       
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
        String Self;
        if (self==true)
        Self="self";
        else
        Self="the target";
        String Name;
        if (name.equals("Buffs")||name.equals("Debuffs"))
        Name=name.substring(0, name.length()-1);
        else
        Name=name;
        if (unique==false)
        this.desc="Does +"+amount+" damage for each "+Name+" on "+Self+". ";
        else
        this.desc="Does +"+amount+" damage for each unique "+Name+" on "+Self+". ";
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
    String name; //of stateff to check for and remove
    boolean type; //check for eff type or name
    int amount; //how much extra dmg each stateff grants
    boolean self;
    boolean intense; //whether to add the value of removed buffs to the bonus damage before removing them
    boolean aoe; //if attack is aoe or not; if so, add empowerment
    public DamageCounterRemove(String nname, boolean etype, int namount, boolean self, boolean i, boolean aoe)
    {
        amount=namount; name=nname; type=etype; this.self=self; intense=i; this.aoe=aoe;
        String Self;
        if (self==true)
        Self="self";
        else
        Self="the target";
        String Name;
        if (name.equals("Buffs")||name.equals("Debuffs"))
        Name=name.substring(0, name.length()-1);
        else if (name.equals("Defence")||name.equals("Other")||name.equals("Heal"))
        Name=name+" effect";
        else
        Name=name;
        this.desc="Does +"+amount+" damage for each "+Name+" on "+Self+", and removes them all. ";
    }
    @Override
    public int Use(Character hero, Character target)
    { 
        int dmgincrease=0;
        if (target!=hero&&hero.activeability!=null&&hero.activeability.blind==false)
        {
            Damage_Stuff.CheckBlind(hero); hero.activeability.blind=true;
        }
        if (target!=hero&&hero.activeability!=null&&hero.activeability.evade==false&&!(hero.binaries.contains("Missed")))
        {
            Damage_Stuff.CheckEvade(hero, target); hero.activeability.evade=true;
        }
        if (!(hero.binaries.contains("Missed")))
        {
            if (self==true)
            dmgincrease=UseDamageCounterRemove(hero, hero, amount, name);
            else
            dmgincrease=UseDamageCounterRemove(hero, target, amount, name);
        }
        return dmgincrease;
    }
    public int UseDamageCounterRemove (Character hero, Character target, int amount, String name) 
    {
        int increase=0; //the amount of total extra damage the attack will do
        ArrayList<StatEff> concurrentmodificationexception= new ArrayList<StatEff>(); 
        if (type==true)
        { 
            for (StatEff eff: target.effects)
            {
                if (eff.getefftype().equalsIgnoreCase(name)) //type e.g. debuffs or buffs
                {
                    increase+=amount;
                    concurrentmodificationexception.add(eff);
                    if (intense==true)
                    increase+=eff.power;
                }
            }
        }
        else
        {
            for (StatEff eff: target.effects)
            {
                if (eff.getimmunityname().equalsIgnoreCase(name)) //specific name, e.g. bleed or intensify
                {
                    increase+=amount;
                    concurrentmodificationexception.add(eff);    
                    if (intense==true)
                    increase+=eff.power;
                }
            }
        }
        for (StatEff eff: concurrentmodificationexception)
        {
            target.remove(eff.id, "normal");
        }
        if (aoe==true&&increase>0&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to avoid applying empower after an assist
        {
            Empower e= new Empower(500, increase, 1, hero);
            hero.effects.add(e); //so the damage boost affects all targets hit by aoe attack instead of only the first; should be applied and consumed without player seeing it
        }
        return increase;
    }
}
class DamageCounterSimple extends BeforeAbility //just checks if the target has an eff, instead of how many of it they have; for miles morales, mac gargan, etc
{
    int amount; //of bonus dmg 
    String name; //of efftype/name to check for; e.g. debuffs or intensify
    String[] names=null;
    boolean type; //whether to check for efftype or effimmunityname
    boolean self; //whether to check self or enemy for the eff
    boolean has; //whether the damage boost is based on if the target has the stateff or doesn't have it
    int hp=616; //only grant extra dmg if target has certain amount of hp
    boolean above; //above or below the hp threshold
    public DamageCounterSimple (int namount, String nname, boolean etype, boolean self, boolean has) 
    {
        amount=namount; name=nname; type=etype; this.self=self; this.has=has;
        String Self;
        if (self==true)
        Self="self";
        else
        Self="the target";
        String Has;
        if (has==true)
        Has=" has any ";
        else
        Has=" doesn't have ";
        this.desc="Does +"+amount+" damage if "+Self+Has+name+". ";
    }
    public DamageCounterSimple (int namount, String[] nname, boolean etype, boolean self, boolean has) 
    {
        amount=namount; names=nname; type=etype; this.self=self; this.has=has;
        String Self;
        if (self==true)
        Self="self";
        else
        Self="the target";
        String Has;
        if (has==true)
        Has=" has ";
        else
        Has=" doesn't have ";
        String Name="";
        int i=names.length;
        for (int p=0; p<i; p++)
        {
            if (p!=(i-1))
            Name=Name+names[p]+" or ";
            else
            Name=Name+names[p]+"";
        }
        this.desc="Does +"+amount+" damage if "+Self+Has+Name+". ";
    }
    public DamageCounterSimple (int amount, int health, boolean self, boolean above)
    {
        this.amount=amount; hp=health; this.self=self; this.above=above;
        String Self;
        if (self==true)
        Self="self";
        else
        Self="the target";
        String Above;
        if (above==true)
        Above=" is above "+health+" HP";
        else
        Above=" is below "+health+" HP";
        this.desc="Does +"+amount+" damage if "+Self+Above+". ";
    }
    @Override
    public int Use (Character user, Character target)
    {
        if (self==true)
        {
            if (hp!=616)
            {
                if (above==true&&user.HP>hp)
                return amount;
                else if (above==false&&user.HP<hp)
                return amount;
            }
            else if (names==null)
            { 
                boolean got=user.CheckFor(name, type);
                if (got==true&&has==true)
                return amount;
                else if (got==false&&has==false)
                return amount;
            }
            else
            {
                boolean yes;
                for (int i=0; i<names.length; i++)
                {
                    yes=user.CheckFor(names[i], type); //target must have all of the required stateffs for the damage boost to apply
                    if (has==true&&yes==false) //if they don't have even one, the dmg boost is 0
                    return 0;
                    else if (has==false&&yes==true) //same if they're not supposed to have any and they have even one (e.g. Brawn's "Brawn")
                    return 0;
                }
                return amount;
            }
        }
        else
        {
            if (hp!=616)
            {
                if (above==true&&target.HP>hp)
                return amount;
                else if (above==false&&target.HP<hp)
                return amount;
            }
            else if (names==null)
            {
                boolean got=target.CheckFor(name, type);
                if (got==true&&has==true)
                return amount;
                else if (got==false&&has==false)
                return amount;
            }
            else
            {
                boolean yes;
                for (int i=0; i<names.length; i++)
                {
                    yes=target.CheckFor(names[i], type); 
                    if (has==true&&yes==false) 
                    return 0;
                    else if (has==false&&yes==true) 
                    return 0;
                }
                return amount;
            }
        }
        return 0;
    }
}
class DebuffMod extends BeforeAbility //for dynamically determining the debuffs an ab applies, e.g. MODOK
{
    int index, ab=0; //also may vary based on the ab the hero uses
    public DebuffMod (int ind)
    {
        index=ind;
    }
    public DebuffMod (int ind, int ability)
    {
        index=ind; ab=ability;
    }
    @Override
    public int Use (Character user, Character target)
    {
        switch (index) //no distinction between summon and hero indexes bc there's no overlap due to how few heroes use debuffmod
        {
            case 2: //gamora's second passive
            if (target.CheckFor("Protect", false)==true) //protected enemies have +1 bleed dur; make sure rogue and adaptoid don't copy her passive
            {
                if (ab==1)
                {
                    String[] light={"Bleed Effect", "50", "20", "2", "false"}; String[][] baby=StatFactory.MakeParam(light, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==2)
                {
                    String[] kira={"Bleed Effect", "50", "30", "2", "false"}; String[][] fin=StatFactory.MakeParam(kira, null); 
                    user.activeability.AddTempString(fin); 
                }
            }
            else //normal bleed dur
            {
                if (ab==1)
                {
                    String[] mello={"Bleed Effect", "50", "20", "1", "false"}; String[][] baby=StatFactory.MakeParam(mello, null); 
                    user.activeability.AddTempString(baby);
                }
                if (ab==2)
                {
                    String[] near={"Bleed Effect", "50", "30", "1", "false"}; String[][] fin=StatFactory.MakeParam(near, null); 
                    user.activeability.AddTempString(fin); 
                }
            }
            break;
            case 4: //ultron drone
            if (target.CheckFor("Buffs", true)==true) //copy
            {
                if (!(target.immunities.contains("Copy")&&!(target.immunities.contains("Other"))))
                {
                    if (user.activeability!=null&&user.activeability.blind==false)
                    {
                        Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
                    }
                    if (!(user.binaries.contains("Missed"))&&user.activeability!=null&&user.activeability.evade==false)
                    {
                        Damage_Stuff.CheckEvade(user, target); 
                        user.activeability.evade=true;
                    }
                    if (!(user.binaries.contains("Missed")))
                    {
                        boolean yes=CoinFlip.Flip(100+user.Cchance);
                        if (yes==true)
                        {
                            StatEff eff=null;
                            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, "any", "Buffs"); //the effs on the target eligible to be copied
                            ArrayList<StatEff> delete=new ArrayList<StatEff>();
                            for (StatEff e: effs)
                            {
                                if (e.duration>100) //no copying indefinite duration effs
                                delete.add(e);
                            }
                            effs.removeAll(delete);
                            int rando=(int) (Math.random()*(effs.size()-1));
                            eff= effs.get(rando);
                            String name=eff.getimmunityname(); int dur=eff.oduration; int pow=eff.power;
                            String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                            System.out.println(user.Cname+" Copied "+target.Cname+"'s "+eff.geteffname()+"!");
                            if (user.activeability!=null&&(Battle.team1[Battle.P1active]==user||Battle.team2[Battle.P2active]==user))
                            user.activeability.AddTempString(string);
                            else
                            {
                                StatEff e=StatFactory.MakeStat(string, user); StatEff.CheckApply(user, user, e);
                            }
                        }
                        else
                        System.out.println(user.Cname+"'s Copy failed to apply due to chance.");
                    }
                }
                else
                System.out.println(user.Cname+"'s Copy failed to apply due to an immunity.");
            }
            else //random buff gain
            CardCode.RandomStat(user, target, "Ultron");
            break;
            case 13: //modern drax
            StaticPassive.Drax(user, target, "battack"); //check which debuff to apply
            if (user.passivecount==-2) //double passive proc
            {
                String[] bloody= {"Bleed", "100", "65", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            else if (user.passivecount==-1) //regular passive proc
            {
                String[] bloody= {"Bleed", "100", "50", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            else //no passive
            {
                String[] bloody= {"Bleed", "100", "35", "2", "false"}; String[][] gore= StatFactory.MakeParam(bloody, null); user.activeability.AddTempString(gore);
            }
            break;
            case 19: //miles
            if (target.CheckFor("Shock", false)==true)
            {
                String[] horseson={"Undermine", "500", "616", "1", "false"}; String[][] albert=StatFactory.MakeParam(horseson, null); user.activeability.AddTempString(albert);
            }
            break;
            case 20: //superior
            if (target.CheckFor("Tracer", false)==true)
            {
                String []akaban={"Countdown", "100", "65", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            else
            {
                String []akaban={"Countdown", "100", "55", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            break;
            case 22: //KK's marvellous finish; embiggened bash is an afterab
            if (user.CheckFor("Evasion", false)==true) 
            {
                String[] kira={"Stun", "100", "616", "1", "false"}; String[][] fin=StatFactory.MakeParam(kira, null); user.activeability.AddTempString(fin); 
            }
            if (user.CheckFor("Mighty Blows", false)==true) 
            {
                String[] akaban={"Wound", "100", "616", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            break;
            case 25: //agent venom
            if (user.index==25&&user.passivecount>5) //shock in awe while in control
            {
                String[]akaban={"Undermine", "100", "616", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            break;
            case 28: //arachnaught
            if (target.summoned==true)
            {
                String []akaban={"Weakness", "100", "5", "2", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); 
                String[] temper={"Provoke", "100", "616", "2", "false"}; String[][] invasion=StatFactory.MakeParam(temper, null); 
                if (user.activeability!=null&&(Battle.team1[Battle.P1active]==user||Battle.team2[Battle.P2active]==user))
                {
                    user.activeability.AddTempString(niharu); user.activeability.AddTempString(invasion);
                }
                else
                {
                    StatEff e=StatFactory.MakeStat(niharu, user); StatEff.CheckApply(user, target, e);
                    StatEff ef=StatFactory.MakeStat(invasion, user); StatEff.CheckApply(user, target, ef);
                }
            }
            else
            {
                String []akaban={"Weakness", "100", "5", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); 
                String[] temper={"Provoke", "100", "616", "1", "false"}; String[][] invasion=StatFactory.MakeParam(temper, null); 
                if (user.activeability!=null&&(Battle.team1[Battle.P1active]==user||Battle.team2[Battle.P2active]==user))
                {
                    user.activeability.AddTempString(niharu); user.activeability.AddTempString(invasion);
                }
                else
                {
                    StatEff e=StatFactory.MakeStat(niharu, user); StatEff.CheckApply(user, target, e);
                    StatEff ef=StatFactory.MakeStat(invasion, user); StatEff.CheckApply(user, target, ef);
                }
            }
            break;
            case 32: //bb
            if (user.index==32&&ab==3)
            {
                int boost=user.passivecount*40; 
                user.passivecount=0; 
                return boost;
            }
            else if (user.index==32&&ab==4)
            {
                ApplyShatter n=null;
                int boost=user.passivecount*20; 
                if (user.passivecount>0)
                n= new ApplyShatter(100+(50*user.passivecount), user.passivecount, true, false, false);
                else
                n= new ApplyShatter(100, 0, false, false, false);
                if (user.activeability!=null&&user.activeability.blind==false)
                {
                    Damage_Stuff.CheckBlind(user); user.activeability.blind=true;
                }
                if (!(user.binaries.contains("Missed")))
                n.Use(user, target);
                user.passivecount=0; 
                return boost;
            }
            break;
            case 35: //juggernut
            if (user.index==35&&ab==1&&user.passivecount==5)
            return 20;
            else if (user.index==35&&ab==5&&user.passivecount==5)
            {
                System.out.println(user.Cname+" is no longer unstoppable."); user.passivecount=0; user.immunities.remove("Debuffs"); 
                return 50;
            }
            break;
            case 36: CardCode.RandomStat(user, target, "statdisable debuffs"); break; //vulture
            case 71: //vapor
            if (target.CheckFor("Defence", true)==true)
            {
                String[] akaban={"Poison", "100", "60", "1", "false"}; String[][] niharu=StatFactory.MakeParam(akaban, null); user.activeability.AddTempString(niharu);
            }
            break;
            case 77: //penance
            if (user.index==77)
            {
                switch (ab)
                {
                    case 1: user.passivecount-=3; break; //single target
                    //so the damage boost affects all targets hit by aoe attack instead of only the first; should be applied and consumed without player seeing it
                    case 2: int blooming=user.passivecount*15; user.passivecount=0; //aoe
                    if (blooming>0)
                    {
                        Empower e= new Empower(500, blooming, 1, user); user.effects.add(e); return blooming;
                    } 
                    break;
                    case 3: int hysop=ActivePassive.Penance(user, 60); break; //spike rake; passive doesn't trigger for self dmg so must trigger it manually
                }
            }
            break;
            case 79: //radioactive man
            if (target.CheckFor("Poison", false)==true)
            {
                String[] heybuddy={"Afflicted", "100", "616", "1", "false"}; String[][] azir=StatFactory.MakeParam(heybuddy, null); user.activeability.AddTempString(azir);
            }
            break;
            case 80: //scarecrow's feeding off fear; his fear doubling is in activatepassive since it wouldn't work here
            ArrayList<StatEff> modexception= new ArrayList<StatEff>(); boolean onceler=false; boolean apply=false;
            for (StatEff e: target.effects)
            {
                if (e.getimmunityname().equals("Terror")||e.getimmunityname().equals("Fear"))
                {
                    modexception.add(e);
                }
            }
            for (StatEff e: modexception)
            {
                if (onceler==false)
                System.out.println(target+"'s Fears expired.");
                target.remove(e.id, "silent"); 
                IntensifyE nero= new IntensifyE(500, 5, 616, user);
                RegenE nemo=new RegenE(500, 5, 616, user);
                //chance is calculated together, and only once, both to avoid wasting time and to avoid printing numerous failure messages
                //since stats are effs and the stat chance is so high, if they ever fail to apply once, it'd be due to something like leader or kang
                //and thus would fail to apply again, so no need to check more than once
                if (onceler==false&&CoinFlip.Flip(500+user.Cchance)==true&&StatEff.CheckFail(user, user, nero)==false&&StatEff.CheckFail(user, user, nemo)==false) 
                {
                    apply=true;
                    if (modexception.size()>1)
                    {
                        System.out.println ("\n"+user+" gained a(n) "+nero.geteffname()+" ("+modexception.size()+")"); 
                        System.out.println ("\n"+user+" gained a(n) "+nemo.geteffname()+" ("+modexception.size()+")");
                    }
                    else
                    {
                        System.out.println ("\n"+user+" gained a(n) "+nero.geteffname()); 
                        System.out.println ("\n"+user+" gained a(n) "+nemo.geteffname());
                    }
                }
                else if (onceler==false) //do not print 3+ failure messages; only do so the very first time
                {
                    apply=false;
                    StatEff.applyfail(user, nero, "chance"); StatEff.applyfail(user, nemo, "chance"); 
                }
                if (apply==true)
                {
                    user.add(nero, false); user.add(nemo, false); 
                }
                onceler=true;
            }
            break;
            case 84: //namor's trident of neptune; giganto must be on his team and alive and unstunned for the assist to work
            if (user.passivefriend.size()>0&&user.passivefriend.get(0).summoned==true&&user.passivefriend.get(0).index==12) //has successfully summoned giganto
            {
                if (user.passivefriend.get(0).team1==user.team1&&user.passivefriend.get(0).dead==false&&!(user.passivefriend.get(0).binaries.contains("Stunned"))) 
                //alive and usable, e.g. not dominated by supergiant 
                {
                    Assist number= new Assist(true, 1, 0, true, 0, null, 500, true);
                    boolean okie=CoinFlip.Flip(500+user.Cchance);
                    if (okie==true)
                    {
                        if (user.activeability!=null) //become elusive
                        {
                            user.activeability.blind=true; user.activeability.evade=true; user.activeability.elusive=true;
                        }
                        ArrayList<Character> friend= new ArrayList<Character>(); friend.add(user.passivefriend.get(0));
                        number.UseAssist(friend, target, user);
                    }
                    else
                    System.out.println(user.Cname+"'s Assist failed to apply due to chance");  
                }
                else //can't do assist, so do dmg
                {
                    if (user.activeability!=null) //if not assisting, attack isn't elusive
                    {
                        user.activeability.blind=false; user.activeability.evade=false; user.activeability.elusive=false;
                    }
                    return 45;
                }
            }
            else //can't do assist, so do dmg
            {
                if (user.activeability!=null)
                {
                    user.activeability.blind=false; user.activeability.evade=false; user.activeability.elusive=false;
                }
                return 45;
            }
            break;
            case 91: CardCode.RandomStat(user, target, "Goblin"); break; //green goblin
            case 100: //elixir
            if (user.index==100&&user==target)
            {
                user.passivecount=1; Tracker hacker= new Tracker(user+" will Resurrect after dying"); user.effects.add(hacker);
            }
            else
            {
                Rez rex= new Rez(500, 100, true); rex.Use(user, target, 0);
            }
            break;
            case 102: //gambit
            if (user.index==102)
            {
                String[] joke={"Countdown", "100", "25", "1", "false"}; 
                int decisive=1+(int)(Math.random() * ((4 - 1) + 1));
                if (decisive==1) 
                {
                    String[] ps={"Afflicted", "100", "616", "1", "false"}; String[][] add=StatFactory.MakeParam(joke, ps); user.activeability.AddTempString(add);
                }
                else if (decisive==2) 
                {
                    String[] ps={"Neutralise", "100", "616", "1", "false"}; String[][] add=StatFactory.MakeParam(joke, ps); user.activeability.AddTempString(add);
                }
                else if (decisive==3) 
                {
                    String[] ps={"Undermine", "100", "616", "1", "false"}; String[][] add=StatFactory.MakeParam(joke, ps); user.activeability.AddTempString(add);
                }
                else if (decisive==4) 
                {
                    String[] ps={"Disarm", "100", "616", "1", "false"}; String[][] add=StatFactory.MakeParam(joke, ps); user.activeability.AddTempString(add);
                }
            }
            break;
            case 104: //bishop
            if (user.index==104&&ab==1&&user.passivecount>=40)
            {
                user.passivecount-=40; System.out.println(user.Cname+" consumed 40 R.");
                for (StatEff e: user.effects) //update energy tracker
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Energy Reserve: "))
                    {
                        e.Attacked(user, null, 0);
                    }
                }
                return 40;
            }
            else if (user.index==104&&ab==2)
            {
                int tumeric=user.passivecount; user.passivecount=0; System.out.println(user.Cname+" consumed "+tumeric+" R.");
                for (StatEff e: user.effects) 
                {
                    if (e instanceof Tracker&&e.getimmunityname().equals("Energy Reserve: "))
                    {
                        e.Attacked(user, null, 0);
                    }
                }
                return tumeric;
            }
        }
        return 0;
    }
}
class Ignore extends BeforeAbility
{
   String condition; //condition needed to be met for ignore to apply
   String toig; //what the hero is ignoring
   int condnumber; //only for conditions that require it, usually a hp threshold
   boolean success=false;
   public Ignore (String toignore, String cond, int number)
   {
      toig=toignore; condition=cond; condnumber=number;
      if (!(cond.equals("always")))
      {
          String C="Error generating description for this ability's Ignore";
          if (cond.equals("enemy health below"))
          C="When enemy health is below "+number+",";
          else if (cond.equals("passive"))
          C="Conditionally";
          else if (cond.substring(0, 8).equals("self has"))
          C="When self has "+cond.substring(9)+",";
          else if (cond.substring(0, 9).equals("enemy has"))
          C="When the target has "+cond.substring(10)+",";
          if (toignore.equals("Missed"))
          this.desc=C+" ignore Miss. ";
          else if (toignore.equals("inescapable"))
          this.desc=C+" "+toignore+". ";
          else 
          this.desc=C+" ignore "+toignore+". ";
      }
      else //desc is the same but the "i" in ignore is capital since it's unconditional
      {
          if (toignore.equals("Missed"))
          this.desc="Ignores miss. ";
          else if (toignore.equals("inescapable"))
          this.desc="Inescapable. ";
          else 
          this.desc="Ignores "+toignore+". ";
      }
   }
   @Override
   public int Use (Character hero, Character target) //beforeab; also activated during target selection 
   {
      String hold=condition; //hold original condition so it's unaffected by substring change below
      if (condition.contains("enemy has"))
      condition=condition.substring(0, 9); //to activate enemy has case
      else if (condition.contains("self has"))
      condition=condition.substring(0, 8);
      switch (condition) //if conditions are met, add whatever needs to be ignored to the hero's ignore arraylist
      {
         case "always": 
         if (success==false) //to avoid triggering twice when called both times
         {
             Ignore.Execute(hero, toig, true); success=true; 
         }
         break;
         case "enemy health below": 
         if (success==false&&target!=null&&target.HP<=condnumber) 
         {
             Ignore.Execute(hero, toig, true); success=true;
         }
         break; 
         case "passive": 
         if (success==false&&hero.passivecount==condnumber) //if the hero's passive has been triggered; fury jr and juggs atm
         {
             Ignore.Execute (hero, toig, true); success=true;
         }
         break;
         case "enemy has": 
         if (success==false&&target!=null&&target.CheckFor(hold.substring(10), false)==true) //check second part of original condition to see what to checkfor
         {
             Ignore.Execute (hero, toig, true); success=true;
         }
         break;
         case "self has": 
         if (success==false&&hero.CheckFor(hold.substring(9), false)==true) 
         {
             Ignore.Execute (hero, toig, true); success=true;
         }
         break;
      }
      condition=hold; //restore condition to original
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
              case "inescapable": CoinFlip.AddInescapable (hero, true); break;
              case "Missed": hero.immunities.add("Missed"); break;
              case "Invisible": hero.ignores.add("Invisible"); break;
              case "Afflicted": hero.ignores.add("Afflicted"); break;
              case "Neutralise": hero.ignores.add("Neutralise"); break;
              case "Undermine": hero.ignores.add("Undermine"); break;
              case "Protect": hero.ignores.add("Protect"); break;
              case "Taunt": hero.ignores.add("Taunt"); break;
              case "DR": hero.ignores.add("DR"); break; //damage reduction
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
              case "inescapable": CoinFlip.AddInescapable (hero, false); break;
              case "Missed": hero.immunities.remove("Missed"); break;
              case "Invisible": hero.ignores.remove("Invisible"); break;
              case "Afflicted": hero.ignores.remove("Afflicted"); break;
              case "Neutralise": hero.ignores.remove("Neutralise"); break;
              case "Undermine": hero.ignores.remove("Undermine"); break;
              case "Protect": hero.ignores.remove("Protect"); break;
              case "Taunt": hero.ignores.remove("Taunt"); break;
              case "DR": hero.ignores.remove("DR"); break;
          }
      }
   }
}
class MissingDMG extends BeforeAbility
{
    int multiplier; int missing; boolean self;
    public MissingDMG (int mu, int mi, boolean se)
    {
        missing=mi; multiplier=mu; self=se;
        String Targ;
        if (se==true)
        Targ="self. ";
        else
        Targ="the target. ";
        this.desc="Deals +"+mu+" damage for every "+mi+" missing HP on "+Targ;
    }
    @Override
    public int Use (Character hero, Character target)
    {
        Character focus;
        if (self==false)
        focus=target;
        else
        focus=hero;
        int lost=focus.maxHP-focus.HP; //e.g. 250-100=150 hp lost
        if (lost<0)
        return 0;
        else
        {
            int tomult=lost/missing; //e.g. 150 hp lost divided by 10 is 15
            tomult*=multiplier; //e.g. has 150 missing HP and does +5 damage for every 10 missing health; 15*5=75 more dmg
            return tomult;
        }
    }
}
class SelfDMG extends BeforeAbility
{
    int amount; boolean loss;
    public SelfDMG (int amot, boolean loss)
    {
        amount=amot; this.loss=loss;
        if (loss==false)
        this.desc="Deals "+amount+" damage to self. ";
        else
        this.desc="Lose "+amount+" HP. ";
    }
    @Override
    public int Use (Character hero, Character ignored)
    {
        if (loss==false)
        {
            Damage_Stuff.ElusiveDmg(hero, hero, amount, "default");
        }
        else
        {
            hero.LoseHP(hero, amount, "self");
        }
        return 0;
    }
}