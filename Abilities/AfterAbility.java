package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 19/8/22
 * Filename: AfterAbility
 * Purpose: To perform non status effect related functions on abilities.
 */
import java.util.ArrayList;
public abstract class AfterAbility extends SpecialAbility //applied after a hero attacks
{
    public AfterAbility()
    {
    }
}
class Activate extends AfterAbility //forcibly ticks all stateffs of a given type on the target down to 0, optionally doing extra dmg for each
{
    boolean type; //determines whether to get name or type
    String name; //of eff to activate
    int dmg; //dmg to be dealt for each eff activated
    public Activate (boolean t, String n, int d)
    {
        type=t; name=n; dmg=d;
    }
    @Override 
    public void Use (Character user, Character target, int ignoreme)
    {
        ArrayList<StatEff> toact= new ArrayList<StatEff>();
        if (type==true) 
        {
            toact=CoinFlip.GetEffs(target, "any", name);
        }
        else
        {
            toact=CoinFlip.GetEffs(target, name, "any");
        }
        int todeal=0;
        if (toact.size()>0&&!(user.binaries.contains("Missed"))) //then reduce their duration to 0
        {
            for (StatEff eff: toact) 
            {
                do 
                { 
                    eff.onTurnEnd(target); 
                }
                while (eff.duration>0);
                todeal+=dmg; //doing extra dmg for each eff target had
            }
            if (todeal>0&&target.dead==false)
            {
                if (user.ignores.contains("Defence"))
                {
                    todeal-=target.ADR; todeal-=target.DR; todeal-=target.PRDR;
                    todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                    target.TakeDamage(target, user, todeal, false);  
                }
                else
                {
                    todeal-=target.ADR; todeal-=target.DR; todeal-=target.RDR; todeal-=target.PRDR;
                    todeal=Damage_Stuff.CheckGuard(user, target, todeal);
                    target.TakeDamage(target, user, todeal, false);    
                }    
            }
        }
    }
}  
class ActivatePassive extends AfterAbility //ability activates a hero's passive or does something otherwise too difficult/specific for another afterab
{
    int num=0;
    public ActivatePassive ()
    {
    }
    public ActivatePassive (int n)
    {
        num=n;
    }
    @Override
    public void Use (Character user, Character target, int ignore)
    {
        switch (user.index)
        {
            case 17: //macdonald eating 
            if (target.dead==true)
            {
                boolean yes=CoinFlip.Flip(500+user.Cchance);
                user.activeability.dcd-=2;
                Regen drugs= new Regen (500, 45, 2);
                if (yes==true)
                StatEff.CheckApply(user, user, drugs);
                else
                StatEff.applyfail(user, drugs, "chance");
            }
            break;
            case 25: ActivePassive.Flash(user, num); break;
        }
    }
}
class Assist extends AfterAbility 
{
    boolean friendly; //call ally or enemy to assist
    int num; //of heroes performing assist
    int bonus; //extra damage
    boolean random; //random or chosen target to call for assist
    int Cchance; //extra statchance
    String[][] apply=null;
    int chance; //to apply
    boolean together;
    boolean skull; //to trigger red skull's damagecounter ability bc this is the easiest way to do it
    public Assist (boolean f, int n, int b, boolean r, int c, boolean t)
    {
        friendly=f; num=n; bonus=b; random=r; chance=c; together=t;
    }
    public Assist (boolean f, int n, int b, boolean r, int s, int c, boolean t) //hero performing assist gains status chance; mysterio
    {
        friendly=f; num=n; bonus=b; random=r; Cchance=s; chance=c; together=t;
    }
    public Assist (boolean f, int n, int b, boolean r, String[][] s, int c, boolean t) //hero doing assist gains stateff after attacking; loki
    {
        friendly=f; num=n; bonus=b; random=r; apply=s; chance=c; together=t;
    }
    @Override
    public void Use (Character user, Character chump, int ignore) //target of the assist is same as target of the ability; if random, then ab will need to be random target
    {
        Character[] targets=null;
        if (friendly==true)
        targets=Battle.GetTeammates(user);
        else
        targets=Battle.GetTeam(CoinFlip.TeamFlip(user.team1));
        boolean valid=false;
        ArrayList<Character> champs=new ArrayList<Character>();
        ArrayList<Ability> basics=new ArrayList<Ability>();
        int done=0; //number of heroes who actually have basic attacks and thus are able to perform an assist
        for (Character a: targets)
        {
            if (a!=null&&!(a.binaries.contains("Banished")))
            {
                for (Ability f: a.abilities)
                {
                    if (f instanceof BasicAb)
                    {
                        ++done; champs.add(a); basics.add(f);
                        break;
                    }
                }
            }
            if (done>=num)
            break;
        }
        int hold=done;
        boolean print=true; //whether to print failure message for lack of eligible targets or not; don't need to print 2 reasons for failure
        if (hold>0)
        {
            for (int i=0; i<hold; i++)
            {
                boolean yes=CoinFlip.Flip(chance+user.Cchance);
                if (yes==true&&together==true)
                {
                    break; //done looping; all assists work
                }
                else if (yes==false&&together==true)
                {
                    System.out.println(user.Cname+"'s Assist failed to apply due to chance"); done=0; print=false; break; //done looping; all assists fail
                }
                else if (yes==false&&together==false)
                {
                    System.out.println(user.Cname+"'s Assist failed to apply due to chance"); --done; print=false; //keep looping; this assist fails but check others
                }
                //else if yes==true&&together==false, keep looping; this assist works but check others
            }
        }
        if (done>0) 
        { 
            ArrayList<Character> nchamp=new ArrayList<Character>(); //characters who will assist, chosen from those who are able to
            ArrayList<Ability> nbasic=new ArrayList<Ability>();
            if (random==true)
            {
                for (int i=0; i<done; i++)
                {
                    int index=0+(int)(Math.random() * (((champs.size()-1) - 0) + 1)); //random number between 0 and champs.size-1, the highest index of a hero in champs
                    if (champs.size()>0) //basics and champs should have same size
                    {
                        nchamp.add(champs.get(index)); nbasic.add(basics.get(index)); //two arraylists are designed so the hero and their basic will have the same index in both
                        champs.remove(index); basics.remove(index); //can't have same hero be called twice by one assist
                    }
                }
            }
            else //choose who performs the assist from the available targets in champs
            {
                System.out.println ("Choose a character to perform the Assist. Type their number, not their name."); 
                for (int hocley=0; hocley<done; hocley++) //can choose as many characters as are available, since the available number can't be higher than the assist's num value
                {
                    int choice=616;
                    for (int i=0; i<champs.size(); i++)
                    {
                        System.out.println ((i+1)+": "+champs.get(i).Cname);  
                    }
                    do
                    {
                        choice=Damage_Stuff.GetInput(); 
                        if (!(choice<=done&&choice>0)) 
                        choice=616;
                    }
                    while (choice==616);
                    nchamp.add(champs.get(choice));
                }
            }
            for (int i=0; i<nchamp.size(); i++)
            { 
                Character dealer=nchamp.get(i); Ability ab=nbasic.get(i); 
                boolean lose=ab.GetLose(); boolean max=ab.GetMax(); int multihit=ab.GetMultihit(true);
                ArrayList<StatEff> selfapply=new ArrayList<StatEff>(); ArrayList<StatEff> otherapply=new ArrayList<StatEff>(); ArrayList<StatEff> toadd=new ArrayList<StatEff>();
                do 
                {
                    int change=0; int damage=ab.GetBaseDmg();
                    damage+=bonus; dealer.Cchance+=Cchance;
                    if (skull==true) //his storm assault
                    {
                        DamageCounter d= new DamageCounter("Debuffs", true, 5, false, true);
                        int add=d.Use(dealer, chump);
                        damage+=add;
                    }
                    for (SpecialAbility ob: ab.special)
                    {
                        change=ob.Use(dealer, chump); //before abs still apply
                        damage+=change;
                    } 
                    if (lose==true) 
                    chump.LoseHP (dealer, damage, "knull");
                    else if (max==true)
                    chump.LoseMaxHP (dealer, damage);
                    else //assists are elusive
                    {
                        damage-=chump.ADR;
                        if (damage<0)
                        damage=0;
                        chump.TakeDamage(chump, dealer, damage, ab.aoe);
                    }
                    for (SpecialAbility ob: ab.special)
                    {
                        ob.Use(dealer, chump, damage); //afterabs
                    } 
                    for (String[][] array: ab.tempstrings)
                    {  
                        StatEff New=StatFactory.MakeStat(array, dealer); 
                        if (array[0][4].equalsIgnoreCase("true"))
                        selfapply.add(New);
                        else if (array[0][4].equalsIgnoreCase("false")) 
                        otherapply.add(New);
                        else
                        {
                            if (dealer.hash==chump.hash)
                            selfapply.add(New);
                            else
                            otherapply.add(New);
                        }
                    }
                    for (String[][] array: ab.statstrings)
                    {  
                        StatEff New=StatFactory.MakeStat(array, dealer); 
                        if (array[0][4].equalsIgnoreCase("true"))
                        selfapply.add(New);
                        else if (array[0][4].equalsIgnoreCase("false")) 
                        otherapply.add(New);
                        else
                        {
                            if (dealer.hash==chump.hash)
                            selfapply.add(New);
                            else
                            otherapply.add(New);
                        }
                    }
                    toadd=Ability.ApplyStats(dealer, chump, together, selfapply, otherapply);
                    if (toadd.size()>0) //apply stateffs to self
                    {
                        for (StatEff eff: toadd)
                        {
                            eff.CheckApply(dealer, dealer, eff);
                        }
                    }
                    if (ab.tempstrings.size()!=0) 
                    ab.tempstrings.removeAll(ab.tempstrings);
                    multihit--;
                    for (SpecialAbility ob: ab.special)
                    {
                        ob.Use(dealer, 616, chump); //for now this only activates chain
                    }
                }
                while (multihit>-1); //then repeat the attack for each multihit
                if (apply!=null)
                {
                    StatEff bunny=StatFactory.MakeStat(apply, user); //for loki
                    StatEff.CheckApply(user, dealer, bunny);
                }
                //then loop to next hero doing assist
            }
        }
        else if (print==true)
        System.out.println(user.Cname+"'s Assist failed to apply due to a lack of eligible targets.");
    }
}
class CopySteal extends AfterAbility //the only difference is that steal removes the buff; otherwise they're identical so they share a method
{
    String[] effname; //name of eff(s) to extend since mandarin can extend more than just one type
    String[] efftype; //usually only buffs, except for hulkling
    int chance; 
    int number; //of effs to copy
    String type; //chosen or random
    boolean together; 
    boolean steal=false;
    public CopySteal (int echance, int num, String type, String[] ename, String[] etype, boolean tog, boolean steal)
    {
        chance=echance; together=tog; number=num; effname=ename; efftype=etype; this.type=type; this.steal=steal;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (user.team1!=target.team1&&user.binaries.contains("Missed")) //need to check for miss before affecting an enemy
        valid=false;
        if (steal==false&&target.immunities.contains("Copy"))
        valid=false;
        else if (steal==true&&target.immunities.contains("Steal"))
        valid=false;
        if (valid==true&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, effname, efftype, "Protect", false); //the effs on the target eligible to be copied; protect is excluded
            ArrayList<StatEff> delete=new ArrayList<StatEff>();
            for (StatEff e: effs)
            {
                if (e.duration>100) //no copying indefinite duration effs
                delete.add(e);
            }
            effs.removeAll(delete);
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    CopyChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    CopyRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    CopyAll(user, target, effs);
                }
                else
                {
                    if (steal==false)
                    System.out.println("Copy failed due to a spelling error.");
                    else
                    System.out.println("Steal failed due to a spelling error.");
                }
            }
        }
        else if (!(user.binaries.contains("Missed"))) //if fail is caused by a miss, print nothing
        {
            if (steal==false)
            System.out.println (user.Cname+"'s Copy failed to apply due to an immunity.");
            else
            System.out.println (user.Cname+"'s Steal failed to apply due to an immunity.");
        }
    }
    public void CopyChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                if (steal==false)
                System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                else
                System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
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
                    if (steal==false)
                    System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                    else
                    System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            if (steal==false)
            System.out.println ("Choose "+todo+" effect(s) to Copy from "+target.Cname+".");
            else
            System.out.println ("Choose "+todo+" effect(s) to Steal from "+target.Cname+".");
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
                effs.remove(ton); //can't copy the same stateff twice in one use
                String name=ton.getimmunityname(); int dur=ton.oduration; int pow=ton.power;
                if (steal==false)
                System.out.println(hero.Cname+" Copied "+target.Cname+"'s "+ton.geteffname()+"!");
                else
                {
                    System.out.println(hero.Cname+" Stole "+target.Cname+"'s "+ton.geteffname()+"!");
                    target.remove(ton.hashcode, "steal");
                }
                String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                if (hero.activeability!=null&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to prevent bugs with assist
                hero.activeability.AddTempString(string);
                else
                {
                    StatEff e=StatFactory.MakeStat(string, hero); StatEff.CheckApply(hero, hero, e);
                }
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void CopyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                if (steal==false)
                System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                else
                System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
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
                    if (steal==false)
                    System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                    else
                    System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0)
        {
            for (int i=0; i<todo; i++)
            {
                int rando=(int) (Math.random()*(effs.size()-1));
                StatEff ton= effs.get(rando);
                effs.remove(ton); //can't copy the same stateff twice in one use
                String name=ton.getimmunityname(); int dur=ton.oduration; int pow=ton.power;
                if (steal==false)
                System.out.println(hero.Cname+" Copied "+target.Cname+"'s "+ton.geteffname()+"!");
                else
                {
                    System.out.println(hero.Cname+" Stole "+target.Cname+"'s "+ton.geteffname()+"!");
                    target.remove(ton.hashcode, "steal");
                }
                String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                if (hero.activeability!=null&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to prevent bugs with assist
                hero.activeability.AddTempString(string);
                else
                {
                    StatEff e=StatFactory.MakeStat(string, hero); StatEff.CheckApply(hero, hero, e);
                }
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void CopyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    String name=eff.getimmunityname(); int dur=eff.oduration; int pow=eff.power;
                    if (steal==false)
                    System.out.println(hero.Cname+" Copied "+target.Cname+"'s "+eff.geteffname()+"!");
                    else
                    {
                        System.out.println(hero.Cname+" Stole "+target.Cname+"'s "+eff.geteffname()+"!");
                        target.remove(eff.hashcode, "steal");
                    }
                    String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                    if (hero.activeability!=null&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to prevent bugs with assist
                    hero.activeability.AddTempString(string);
                    else
                    {
                        StatEff e=StatFactory.MakeStat(string, hero); StatEff.CheckApply(hero, hero, e);
                    }
                }
            }
            else
            {
                if (steal==false)
                System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                else
                System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    String name=eff.getimmunityname(); int dur=eff.oduration; int pow=eff.power;
                    if (steal==false)
                    System.out.println(hero.Cname+" Copied "+target.Cname+"'s "+eff.geteffname()+"!");
                    else
                    {
                        System.out.println(hero.Cname+" Stole "+target.Cname+"'s "+eff.geteffname()+"!");
                        target.remove(eff.hashcode, "steal");
                    }
                    String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "true"}; String[][] string=StatFactory.MakeParam(morb, null);
                    if (hero.activeability!=null&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to prevent bugs with assist
                    hero.activeability.AddTempString(string);
                    else
                    {
                        StatEff e=StatFactory.MakeStat(string, hero); StatEff.CheckApply(hero, hero, e);
                    }
                }
                else
                {
                    if (steal==false)
                    System.out.println(hero.Cname+"'s Copy failed to apply due to chance.");
                    else
                    System.out.println(hero.Cname+"'s Steal failed to apply due to chance.");
                }
            }
        }
    }
}
class Confidence extends AfterAbility
{
    int amount; int chance; 
    public Confidence(int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (caller.CheckFor("Afflicted", false)==false) //confidence is a heal ability
        {
            boolean success=CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&target.dead==false)
            {
                target.Healed(amount, false);
                target.Shielded(amount);
            }
        }
        else
        System.out.println (caller.Cname+"'s Confidence failed to apply due to a conflicting status effect.");
    }
}
class Extend extends AfterAbility
{
    String[] effname; //name of eff(s) to extend since mandarin can extend more than just one type
    String[] efftype; //including nondmging debuffs
    int chance; 
    int number; //of effs to extend
    int turns; //number of turns to extend eff by 
    String type; //whether all effs are extended or just some; chosen or random
    boolean together; //true for together and false for separate
    boolean self;
    boolean immortalstun=false; //whether immortality or stun can be extended or not
    public Extend (int echance, int num, String type, String[] ename, String[] etype, int turns, boolean self, boolean tog, boolean i)
    {
        chance=echance; together=tog; number=num; effname=ename; efftype=etype; this.type=type; this.self=self; this.turns=turns; immortalstun=i;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before affecting an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Extend"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=null;
            if (efftype[0].equals("nondamaging"))
            effs=CoinFlip.GetEffsND(target, immortalstun);
            else if (immortalstun==false) //exclude immortality
            effs=CoinFlip.GetEffs(target, effname, efftype, "Immortality", false); 
            else
            effs=CoinFlip.GetEffs(target, effname, efftype); //the effs on the target eligible to be extended
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    ExtendChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    ExtendRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    ExtendAll(user, target, effs);
                }
                else
                {
                    System.out.println("Extend failed due to a spelling error.");
                }
            }
        }
        else if (!(user.binaries.contains("Missed"))) //if fail is caused by a miss, print nothing
        {
            System.out.println (user.Cname+"'s Extend failed to apply due to an immunity.");
        }
    }
    public void ExtendChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
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
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" effect(s) to Extend on "+target.Cname+".");
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
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Extended by "+turns+" turn(s)!");
                ton.Extended(turns, target);   
                effs.remove(ton); //can't extend the same stateff twice with one usage of Extend
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void ExtendRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
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
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
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
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Extended by "+turns+" turn(s)!");
                get.Extended(turns, target);      
                effs.remove(get);
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void ExtendAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance);
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+turns+" turn(s)!");
                    eff.Extended(turns, target);
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Extended by "+turns+" turn(s)!");
                    eff.Extended(turns, target);                    
                }
                else
                {
                    System.out.println(hero.Cname+"'s Extend failed to apply due to chance.");
                }
            }
        }
    }
}
class Mend extends AfterAbility
{
    int amount; int chance; 
    public Mend (int cchance, int aamount)
    {
        amount=aamount;
        chance=cchance;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (caller.CheckFor("Afflicted", false)==false) 
        {
            boolean success=CoinFlip.Flip(chance+caller.Cchance);
            if (success==true&&target.dead==false)
            {
                target.Healed(amount, false);
            }
        }
        else
        System.out.println (caller.Cname+"'s Mend failed to apply due to a conflicting status effect.");
    }
}
class MendPassive extends AfterAbility //restoring missing health; for deadpool and dr. doom
{
    int amount; 
    public MendPassive (int aamount)
    {
        amount=aamount;
    }
    @Override 
    public void Use(Character caller, Character target, int ignore) 
    {
        if (target.dead==false)
        target.Healed(amount, true);
    }
}
class Nullify extends AfterAbility
{
    String effname;
    int chance; 
    int number; //of buffs to nullify
    String type; //whether all buffs are nullified or only a few; chosen or andom
    String efftype="Buffs";
    boolean together; //true for together and false for separate
    boolean self;
    public Nullify (int echance, int num, String type, String ename, boolean self, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; this.type=type; this.self=self;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before nullifying an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Nullify"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, effname, efftype); //the buffs on the target eligible to be nullified
            if (effs.size()>0)
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
                target.remove(ton.hashcode, "nullify");                     
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
                target.remove(get.hashcode, "nullify");                
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
                    target.remove(eff.hashcode, "nullify"); 
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
                    target.remove(eff.hashcode, "nullify");                     
                }
                else
                {
                    System.out.println(hero.Cname+"'s Nullify failed to apply due to chance.");
                }
            }
        }
    }
}
class Purify extends AfterAbility
{
    String effname; //name of debuff(s) to be purified
    String efftype="Debuffs";
    int chance; 
    int number;
    String type; //chosen, random, or all
    boolean together; //true for together and false for separate
    boolean self;
    public Purify (int echance, int num, String t, String ename, boolean self, boolean tog)
    {
        chance=echance; together=tog; number=num; effname=ename; type=t; this.self=self;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean valid=true;
        if (self==true)
        target=user;
        if (user.team1!=target.team1&&(user.binaries.contains("Missed"))) //need to check for miss before purifying an enemy
        valid=false;
        if (valid==true&&!(target.immunities.contains("Purify"))&&!(target.immunities.contains("Other"))) 
        {
            ArrayList <StatEff> effs=CoinFlip.GetEffs(target, effname, efftype); //only gather the target's debuffs
            if (effs.size()>0)
            {
                if (type.equals("chosen"))
                {
                    PurifyChosen(user, target, effs);
                }
                else if (type.equals("random"))
                {
                    PurifyRandom(user, target, effs);
                }
                else if (type.equals("all"))
                {
                    PurifyAll(user, target, effs);
                }
                else
                {
                    System.out.println("Purify failed due to a spelling error.");
                }
            }
        }
        else
        {
            System.out.println (user.Cname+"'s Purify failed to apply due to an immunity.");
        }
    }
    public void PurifyChosen (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
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
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                    --todo;
                }
            }
        }
        if (todo>0) 
        {
            System.out.println ("Choose "+todo+" debuff(s) to Purify from "+target.Cname+"."); 
            System.out.println ("Enter the number next to it, not its name."); 
            for (int i=0; i<todo; i++)
            {
                boolean falg=false; int counter=0; int index=616; 
                for (StatEff eff: effs) //list the debuffs
                {
                    int o=counter+1;  
                    System.out.println(o+": "+eff.geteffname());
                    ++counter;
                }            
                do
                {
                    index=Damage_Stuff.GetInput();
                    --index;
                    if (index>=0&&index<effs.size()) //valid number typed in
                    {
                        falg=true;
                    }
                }
                while (falg==false);
                StatEff ton= effs.get(index);                  
                System.out.println(target.Cname+"'s "+ton.geteffname()+" was Purified!");
                target.remove(ton.hashcode, "purify");   
                effs.remove(index);
                if (effs.size()<=0)
                {
                    break; //end loop
                }
            }
        }
    }
    public void PurifyRandom (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false; int todo=number;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==false)
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
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
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
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
                System.out.println(target.Cname+"'s "+get.geteffname()+" was Purified!");
                target.remove(get.hashcode, "purify");
                effs.remove(rando);
                if (effs.size()<=0)
                {
                    break;
                }
            }
        }
    }
    public void PurifyAll (Character hero, Character target, ArrayList<StatEff> effs)
    {
        boolean succeed=false;
        if (together==true)
        {
            succeed=CoinFlip.Flip(chance+hero.Cchance); 
            if (succeed==true)
            {
                for (StatEff eff: effs)
                {
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Purified!");
                    target.remove(eff.hashcode, "purify"); 
                }
            }
            else
            {
                System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
            }
        }
        else
        {
            for (StatEff eff: effs)
            {
                succeed=CoinFlip.Flip(chance+hero.Cchance); 
                if (succeed==true)
                {                    
                    System.out.println(target.Cname+"'s "+eff.geteffname()+" was Purified!");
                    target.remove(eff.hashcode, "purify"); 
                }
                else
                {
                    System.out.println(hero.Cname+"'s Purify failed to apply due to chance.");
                }
            }
        }
    }
}
class ReduceCD extends AfterAbility
{
    boolean chosen; int amount;
    public ReduceCD (boolean c, int a)
    {
        chosen=c; amount=a;
    }
    @Override
    public void Use (Character user, Character target, int ignoreme)
    {
        boolean go=false;
        for (Ability a: target.abilities)
        {
            if (a!=null&&a.singleuse==false&&a.dcd>0)
            {
                go=true; break; //must have at least one ab on cd or there's nothing to reduce
            }
        }
        if (go==true&&chosen==false) //reduce cd of all abs
        {
            for (Ability a: target.abilities)
            {
                if (a!=null&&a.singleuse==false&&a.dcd>0)
                {
                    if (amount>0)
                    System.out.println(a.GetAbName(target)+" had its cooldown reduced by "+amount+" turn(s).");
                    else 
                    System.out.println(a.GetAbName(target)+" had its cooldown increased by "+Math.abs(amount)+" turn(s).");
                    a.CDReduction(amount);
                }
            }
        }
        else if (go==true)
        {
            Ability victim=null; int choice=-1; boolean good=false;
            System.out.println ("\nChoose one of "+target.Cname+"'s abilities. Type its number, not its name.");
            for (int i=0; i<5; i++)
            {
                int a=i+1;
                if (target.abilities[i]!=null&&target.abilities[i].singleuse==false&&target.abilities[i].dcd>0)
                {
                    System.out.println (a+": "+target.abilities[i].GetAbName(target));  
                }
            }
            do
            {
                choice=Damage_Stuff.GetInput(); 
                --choice; //to get the index number since the number entered was the ability number
                if (choice<5&&choice>=0)
                {
                    good=true;
                    victim=target.abilities[choice];
                }
            }
            while (good==false);
            if (amount>0)
            System.out.println(victim.GetAbName(target)+" had its cooldown reduced by "+amount+" turn(s).");
            else 
            System.out.println(victim.GetAbName(target)+" had its cooldown increased by "+Math.abs(amount)+" turn(s).");
            victim.CDReduction(amount);
        }
        else
        System.out.println("All of "+target.Cname+"'s abilities are already off cooldown!");
    }
}
class Ricochet extends AfterAbility //do ricochet damage
{
    int chance; String[][] string=null;
    public Ricochet(int cchance)
    {
        chance=cchance; 
    }
    public Ricochet (int chance, String[][] e) //apply stateff to enemy dmged by ricochet
    {
        this.chance=chance; string=e;
    }
    @Override
    public void Use (Character user, Character target, int dmg) //user is the one doing the damage 
    { 
        if ((!(user.binaries.contains("Missed"))||user.immunities.contains("Missed")))
        {
            boolean success=CoinFlip.Flip(chance+user.Cchance); 
            if (success==true&&dmg>5) //sends over ab's dmg dealt for ricochet calculation
            Ability.DoRicochetDmg (dmg, target, true, string); 
            else if (dmg>5) //don't print failure message if failure was due to low dmg
            System.out.println(user.Cname+"'s Ricochet failed to apply due to chance.");
        }
    }
}
class Suicide extends AfterAbility //the thing minions use in suicide attacks
{
    public Suicide ()
    {
    }
    @Override
    public void Use (Character hero, Character target, int ignore)
    {
        if (hero.dead==false) //from reflect or something; no need to die again
        hero.onDeath(null, "knull");
    }
}
class Summoning extends AfterAbility
{
    int sindex=616; int in2=0; int in3=0;
    public Summoning (int ind)
    {
        sindex=ind;
    }
    public Summoning (int ind, int c)
    {
        sindex=ind; in2=c;
    }
    public Summoning (int ind, int c, int t)
    {
        sindex=ind; in2=c; in3=t;
    }
    @Override
    public void Use (Character hero, Character target, int ignore)
    {
        int i=sindex; //by default, abs only summon one Summon
        if (in2!=0) //but if there is more than one option, player chooses and change the i value
        {
            if (hero.team1==true)
            System.out.println ("\nPlayer 1, choose a character for "+hero.Cname+" to Summon. Type their number, not their name.");
            else
            System.out.println ("\nPlayer 2, choose a character for "+hero.Cname+" to Summon. Type their number, not their name.");
            System.out.println ("1. "+Character.SetName(sindex, true));
            System.out.println ("2. "+Character.SetName(in2, true));
            if (in3!=0)
            System.out.println ("3. "+Character.SetName(in3, true));
            int choice=0; boolean good=false;
            do
            {
                choice=Damage_Stuff.GetInput(); 
                if (choice==1)
                {
                    choice=sindex; good=true;
                }
                else if (choice==2)
                {
                    choice=in2; good=true;
                }
                else if (in3!=0&&choice==3)
                {
                    choice=in3; good=true;
                }
            }
            while (good==false);
            i=choice;
        }
        Summon friend= new Summon(i);
        friend.team1=hero.team1; //add if statement to change this when needed in the future
        Battle.SummonSomeone(hero, friend);
    }
}
class Transformation extends AfterAbility
{
    int index;
    boolean great;
    public Transformation (int n, boolean g)
    {
        index=n; great=g;
    }
    @Override
    public void Use (Character hero, Character ignored, int ignore)
    {
        hero.Transform(index, great);
    }
}
class Update extends AfterAbility //adds tracker to hero to make it clear that their otherwise silent otherab took effect; for drax modern, mephisto, the weaver, unstoppable colossus
{
    int index=0;
    public Update (int ind)
    {
        index=ind;
    }
    @Override
    public void Use (Character hero, Character ignored, int ignore)
    {
        switch (index)
        {
            case 13: boolean ok=true;
            for (StatEff e: hero.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Twin Blades active"))
                {
                    ok=false; break;
                }
            }
            if (ok==true) //although unlikely to happen, still pointless to let this show twice since its effect doesn't stack
            hero.effects.add(new Tracker ("Twin Blades active")); 
            break;
        }
    }
}