package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: AfterAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
public abstract class AfterAbility extends SpecialAbility //applied after a hero attacks
{
    public AfterAbility()
    {
    }
}
class AddIgnore extends AfterAbility
{
    String condition; String ignorethis; int threshold; //health threshold, needed for lads like Corvus
    public AddIgnore (String igthis, String condie, int thresh)
    {
        condition=condie; ignorethis=igthis; threshold=thresh;
    }
    @Override
    public int Use (Character user, Character target)
    {
        boolean okay=CheckIgCondition(condition, target, threshold);
        if (okay==true)
        {
            user.ignores.add (ignorethis);
        }
        return 0;
    }
    @Override 
    public void Use (Character user, Character target, int ignoreme)
    {
        user.ignores.remove (ignorethis);
    }
    public static boolean CheckIgCondition (String condition, Character target, int hp)
    { 
        switch (condition) 
        {
            case "none": return true; //will add more as needed
            default: return false;
        }
    }
}
class Confidence extends AfterAbility
{
    int amount; int chance; Ricochet r= new Ricochet(1);
    public Confidence(int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character mj, int ignore) 
    {
        if (caller.CheckFor(caller, "Heal Disable")==false) //confidence is a heal ability
        {
            boolean success=Card_CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&mj.dead==false&&!(mj.binaries.contains("Banished")))
            {
                mj.Healed(mj, amount);
                mj.Shielded(mj, amount);
            }
        }
    }
}
class Extend extends AfterAbility
{
    String buffname[]; String[] bufftype;
    String number; 
    int chance; int duration;
    boolean choice; //player chooses buff or not; true for yes and false for random buffs
    boolean together; //true for together and false for separate
    public Extend (int extchance, String numbertoremove, boolean randomorchosen, String[] mnameofbufftoext, String[] typeofbuff boolean tog, int dur)
    {
        chance=extchance; buffname=mnameofbufftoext; number=numbertoremove; choice=randomorchosen; together=tog; bufftype=typeofbuff; duration=dur;
    }
    @Override
    public int Use (Character user, Character target)
    {
        for (int i=0; i<buffname.size(); i++)
        {
            //this calculates how many effects will be extended
            boolean succeed=true;  
            ArrayList<StatEff> toext= new ArrayList <StatEff>();
            int available=0; //how many buffs hero can extend after checking chance
            if (choice==false&&!(target.immunities.contains("Extend"))&&!(target.immunities.contains("Other"))) //ability has random extends
            {
                if (together==true) //the application chance is rolled once for all the effects
                {
                    succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                    if (succeed==false)
                    {
                        System.out.println (user.Cname+"'s Extend failed to apply due to chance."); //(Test) The application chance was "+chance);
                    }
                    else if (!(number.equalsIgnoreCase("all")))
                    {
                        available=Integer.valueOf(number);
                    }
                }
            }
            else //chance calculated separately
            {
                for (StatEff eff: target.effects)
                {
                    if (!(buffname.equalsIgnoreCase("all"))) //extend specific buffs on the target, e.g. Counter
                    {
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&eff.getimmunityname().equalsIgnoreCase(buffname[i]))
                        {
                            succeed=Card_CoinFlip.Flip(chance+user.Cchance);
                            if (succeed==true&&bufftype[i].equalsIgnoreCase(eff.getefftype()))
                            {
                                removal.add(eff); //stores which effects will be removed
                                ++available;
                            }
                        }    
                    }
                    else //extend all buffs on the target, regardless of type
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
            //this extends them all
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
                            System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended!");
                        }
                    } 
             }
        }
            return 0;   
}
class Ricochet extends AfterAbility
{
    int chance;
    public Ricochet(int cchance)
    {
        chance=cchance;
    }
    @Override
    public void Use (Character user, Character ignorethis, int dmg) //user is the one doing the damage
    { 
        boolean success=Card_CoinFlip.Flip(chance+user.Cchance);
        if (success==true&&dmg>5)
        {
            Ability.DoRicochetDmg (dmg, user, false);            
        }
    }
}
