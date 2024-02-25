
package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 17/7/22
 * Filename: Ability
 * Purpose: Creates characters' abilities.
 */
import java.util.ArrayList;
public abstract class Ability
{
    boolean channelled=false; boolean interrupt=false; boolean usable=true; 
    boolean singleuse=false; boolean used=false; //used is only for keeeping track of single use
    boolean unbound=false;
    boolean together=true; //whether status effects are applied separately or together
    String oname; 
    String target; String friendly; 
    //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
    //target is single, self, multitarg, random, or aoe 
    boolean aoe=false; boolean attack=false;
    int multiuse=0; int cd=0; int dcd=0; //all abilities are initially displayed with 0 cooldown (dcd) and switch to their listed cooldowns after use 
    ArrayList <Character> ctargets; //for channelled abs
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); 
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>();
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>();
    ArrayList<String[][]> statstrings= new ArrayList<String[][]>();
    ArrayList<String[][]> tempstrings= new ArrayList<String[][]>();
    public Ability ()
    {
        /* statstrings/tempstatstrings are added when the ab is created under ab list
         * when using an ab, first multiuse, channelled, and aoe are checked for
         * then the target is selected from the target array and empowerments are applied
         * blind and its miss chance are checked for, and then beforeabs are applied
         * the user attacks/uses the ab, and afterabs are applied
         * statstrings are gathered and sent away to be turned into stateffs and applied, and tempstatstrings are erased
         * miss and empowerments are removed
         * multihit and aoe cause the above to repeat until their ends
         * interrupt is removed, and the ab enters its cd
         * */
    }    
    public abstract boolean CheckUse (Character user, Ability ab);
    public String GetAbName (Character hero, Ability ab)
    {
        boolean useable=CheckUse(hero, ab);
        if (useable==true&&dcd==0)
        {
            return this.oname;
        }
        else if (useable==false&&dcd>0)
        {
            return this.oname+", usable again in "+this.dcd+" turn(s)";
        }
        else
        {
            return this.oname+ " (disabled)";
        }
    } 
    public ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets) //only applies for the non-attack abs since they all work the same
    {
        boolean typo=true; int uses=1; 
        System.out.println (user.Cname+" used "+oname);
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (user.binaries.contains("Missed")) //to prevent a miss if the hero's assist/counterattack was evaded after they last attacked (miss is otherwise cleared after attacking)
        user.binaries.remove("Missed");
        if (multiuse>0)
        {
            System.out.println ("How many extra times would you like to use this ability?");
            do
            {
                uses=Damage_Stuff.GetInput();
                ++uses;
                if (uses>0&&uses<=multiuse)
                {
                    typo=false;
                }
            }
            while (typo==true);
        }
        if (channelled==true)
        {
            ab.SetChannelled(user, ab, targets);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            if (targets.size()<=0)
            {
                uses=-1;
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
            }
            for (Character chump: targets) //use the ability on its target
            {
                if (chump!=null) //if null, skip entirely
                {
                    int change=0; 
                    for (StatEff eff: user.effects) //get empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, true);
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                    } 
                    for (String[][] array: tempstrings)
                    {  
                        StatEff New=StatFactory.MakeStat(array); 
                        if (array[0][4].equalsIgnoreCase("true"))
                        {
                            selfapply.add(New);
                        }
                        else if ((!(user.binaries.contains("Missed"))||user.immunities.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                        {
                            otherapply.add(New);
                        }
                        else
                        {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
                        }
                    }
                    for (String[][] array: statstrings)
                    {  
                       StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                       if (array[0][4].equalsIgnoreCase("true"))
                       {
                           selfapply.add(New);
                       }
                       else if (array[0][4].equalsIgnoreCase("false"))
                       {
                           otherapply.add(New);
                       }
                       else
                        {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
                        }
                    }
                    if (this.attack==true)
                    {
                        if (chump.team1!=user.team1) //hitting an enemy
                        {
                            Damage_Stuff.CheckBlind(user);
                            user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    for (StatEff eff: user.effects) //undo empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, false);
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
                }
                --uses;
            }
        }
        if (singleuse==true)
        {
            used=true;
        }
        else
        {
            dcd=cd;
        }
        return toadd;
    }
    public void CDReduction(int amount)
    {
        if (singleuse==true)
        {
            //it doesn't have a cooldown
        }
        else if (dcd<=0)
        {
            //no negative cooldowns
        }
        else 
        {
            dcd-=amount;
            if (dcd<0)
            {
                dcd=0; //no negative cooldowns
            }
        } 
    }
    public void SetChannelled (Character hero, Ability ab, ArrayList<Character> targets)
    {
        ctargets=targets;
        hero.effects.add(new Tracker ("Channelling "+ab.oname)); //so you don't forget someone is channelling
    }
    public void InterruptChannelled (Character hero, Ability ab)
    {
        if (interrupt==false)
        {
            interrupt=true;
            System.out.println(hero.Cname+"'s Channelling was interrupted!");
            StatEff remove= null;
            for (StatEff e: hero.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+ab.oname))
                {
                    remove=e; break;
                }
            }
            if (remove!=null)
            hero.effects.remove(remove);  
        }
    }
    public ArrayList<StatEff> ActivateChannelled(Character user, Ability ab)
    {
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (user.binaries.contains("Missed")) //to prevent a miss if the hero's assist/counterattack was evaded after they last attacked (miss is otherwise cleared after attacking)
        user.binaries.remove("Missed");
        if (channelled==true&&interrupt==true) 
        {
            interrupt=false; //reset it so the ab is not permanently unusable
            if (singleuse==true)
            {
                used=true;
            }
            else
            {
                dcd=cd;
            }
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            System.out.println ("\n"+oname+"'s channelling finished.");
            System.out.println (user.Cname+" used "+oname+"!"); 
            if (ctargets.size()<=0)
            {
                System.out.println(ab.oname+" could not be used due to a lack of eligible targets.");
                for (String[][] array: tempstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array); 
                    if (!(array[0][4].equalsIgnoreCase("false")))
                    {
                        selfapply.add(New);
                    }
                }
                for (String[][] array: statstrings)
                {  
                    StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                    if (!(array[0][4].equalsIgnoreCase("false")))
                    {
                        selfapply.add(New);
                    }
                }
                ArrayList<StatEff> n= new ArrayList<StatEff>(); //empty array since there's no other targets to apply stateffs to 
                toadd=Ability.ApplyStats(user, null, together, selfapply, n);
                if (selfapply.size()!=0)
                {
                    selfapply.removeAll(selfapply); 
                }
                if (tempstrings.size()!=0) 
                {
                    tempstrings.removeAll(tempstrings);
                }
                return toadd;
            }
            for (Character chump: ctargets) //use the ability on its target
            {
                if (chump!=null) 
                {
                    int change=0; 
                    for (StatEff eff: user.effects) //get empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, true);
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                    } 
                    if (this.attack==true)
                    {
                        if (chump.team1!=user.team1) //hitting an enemy
                        {
                            Damage_Stuff.CheckBlind(user);
                            user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    for (String[][] array: tempstrings)
                    {  
                        StatEff New=StatFactory.MakeStat(array); 
                        if (array[0][4].equalsIgnoreCase("true"))
                        {
                            selfapply.add(New);
                        }
                        else if ((!(user.binaries.contains("Missed"))||user.immunities.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) 
                        {
                            otherapply.add(New);
                        }
                        else
                        {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
                        }
                    }
                    for (String[][] array: statstrings)
                    {  
                       StatEff New=StatFactory.MakeStat(array); //this is how selfapply and other apply are populated
                       if (array[0][4].equalsIgnoreCase("true"))
                       {
                           selfapply.add(New);
                       }
                       else if (array[0][4].equalsIgnoreCase("false"))
                       {
                           otherapply.add(New);
                       }
                       else //neither true nor false; capabable of affecting either self or an ally
                        {
                            if (user.hash==chump.hash)
                            {
                                selfapply.add(New);
                            }
                            else
                            {
                                otherapply.add(New);
                            }
                        }
                    }
                    toadd=Ability.ApplyStats(user, chump, together, selfapply, otherapply);
                    for (StatEff eff: user.effects) //undo empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, false);
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
                }
            }
        }
        if (singleuse==true)
        {
            used=true;
        }
        else
        {
            dcd=cd;
        }
        return toadd;
    }
    public void AddStatString(String[][] f)
    {
        statstrings.add(f);
    }
    public void AddTempString(String[][] f)
    {
        tempstrings.add(f);
    }
    public int GetBaseDmg () //reckon this is for jean, thing, etc
    {
        return 0;
    }
    public void ReturnDamage(int dmg) //tells ab how much dmg it did for ricochet and whatnot to work properly
    {
    }
    public static Ability[] AssignAb (int index)
    {
        Ability[] abilities = new Ability[5];
        int counter=0;
        Ability ab1=Ability_List_Player.GetAb(index, counter, false);
        ++counter;
        Ability ab2=Ability_List_Player.GetAb(index, counter, false);
        ++counter;
        Ability ab3=Ability_List_Player.GetAb(index, counter, false);
        ++counter;
        Ability ab4=Ability_List_Player.GetAb(index, counter, false);
        ++counter;
        Ability ab5=Ability_List_Player.GetAb(index, counter, false);
        abilities[0]=ab1;
        abilities[1]=ab2;
        abilities[2]=ab3;
        abilities[3]=ab4;
        abilities[4]=ab5;
        return abilities;
    }
    public static Ability[] AssignAbSum (int index)
    {
        Ability[] abilities = new Ability[5];
        int counter=0;
        Ability ab1=Ability_List_Summon.GetAb(index, counter, false);
        ++counter;
        Ability ab2=Ability_List_Summon.GetAb(index, counter, false);
        ++counter;
        Ability ab3=Ability_List_Summon.GetAb(index, counter, false);
        ++counter;
        Ability ab4=Ability_List_Summon.GetAb(index, counter, false);
        ++counter;
        Ability ab5=Ability_List_Summon.GetAb(index, counter, false);
        abilities[0]=ab1;
        abilities[1]=ab2;
        abilities[2]=ab3;
        abilities[3]=ab4;
        abilities[4]=ab5;
        return abilities;
    }
    public static void DoRicochetDmg (int dmg, Character user, boolean shock)
    {
        //this both calculates and deals Ricochet damage to a random enemy
        double d=dmg/2; //dmg dealt divided by 2
        dmg=5*(int)(Math.floor(d/5)); //ricochet damage; rounded down
        Character villain=Ability.GetRandomHero(user, shock); //random enemy, or teammate if the Ricochet is from a Shock
        if (villain!=null)
        {
            dmg-=villain.ADR;
            if (dmg>0)
            {
                System.out.println ("\n"+villain.Cname+" took "+dmg+" Ricochet damage"); 
                villain.TakeDamage(villain, dmg, false); //random enemy takes the damage
            }
        }
    }
    public static Character GetRandomHero(Character hero, boolean shock) //hero is the character calling the method
    {
        //Determine team of the caller
        boolean team=hero.team1; 
        if (shock==false) //get the opponent's team; but if shock is true, leave team as is and get the character's team for a random teammate
        {
            team=CoinFlip.TeamFlip(team);
        }
        boolean solo;
        if (team==true)
        solo=Battle.p1solo;
        else        
        solo=Battle.p2solo;
        if (solo==false) //there must be more than one person on the target's team, or else there's no one to hurt with the ricochet
        {
            //Determine the caller's enemies or teammates
            Character[] enemies= new Character[6];
            enemies=Battle.GetTeammates(hero, team);
            boolean flag=true;
            Character villain=null; 
            //Randomly choose an enemy
            while (flag==true)
            {
                int rando = (int)(Math.random()*enemies.length);
                villain=enemies[rando];
                if (villain!=null&&!(villain.binaries.contains("Banished")))
                {
                    flag=false;
                }
            }
            return villain;
        }
        else
        {
            return null;
        }
    }
    public static ArrayList<StatEff> ApplyStats (Character hero, Character target, boolean together, ArrayList<StatEff> selfapp, ArrayList<StatEff> otherapp)
    {
        ArrayList<StatEff> toadd= new ArrayList<StatEff>(); //this is only for status effects to be applied to self; returned so they can be applied after the hero's turn ends
        for (StatEff eff: selfapp)
        {
            if (eff.getimmunityname().equalsIgnoreCase("Protect"))
            {
                eff.PrepareProtect(hero, target); //determines who to apply the protect to since it can't be done with a constructor
            }
        }
        if (together==true) //the application chance is rolled once for all the effects
        {
            int chance=0;
            if (selfapp.size()!=0)
            {
                for (StatEff eff: selfapp)
                {
                    chance=eff.chance;
                    chance+=hero.Cchance;
                    break; //they all have to have the same chance if they're applied together
                }
            }
            else if (!(hero.binaries.contains("Missed"))&&otherapp.size()!=0) //if the attack applies no status effects (if both arrays are empty), nothing happens
            {
                for (StatEff eff: otherapp)
                {
                    chance=eff.chance;
                    chance+=hero.Cchance;
                    break; 
                }
            }
            boolean succeed=CoinFlip.Flip(chance); 
            if (selfapp.size()==0&&otherapp.size()==0) //do not print anything since there are no effects to apply
            { 
            }
            else if (succeed==false&&hero.binaries.contains("Missed")&&otherapp.size()>0) //if otherapp's stateffs can't be applied due to a miss, no need to print anything
            {
            }
            else if (succeed==false&&target.dead==false)
            {
                System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
            }
            else if (succeed==true)
            {
                if (selfapp.size()!=0&&hero.dead==false)
                {
                    for (StatEff eff: selfapp)
                    {   //since these stateffs are applied after turn end when disables tick down/expire, the check needs to be here to account for them before expiry
                        if (hero.immunities.contains(eff.getefftype())||hero.immunities.contains(eff.getimmunityname()))
                        {
                            StatEff.applyfail(hero, eff, "immune");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Neutralise")==true)
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Undermine")==true)
                        { 
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Afflicted")==true)
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else
                        {
                            boolean apple=eff.CheckStacking(hero, eff, eff.stackable); 
                            if (apple==true)
                            {
                                toadd.add(eff);
                            }
                            else
                            {
                                StatEff.applyfail(hero, eff, "dupe");
                            }
                        }
                    }
                }
                if (!(hero.binaries.contains("Missed"))&&otherapp.size()!=0&&target!=null&&target.dead==false)
                {
                    for (StatEff eff: otherapp)
                    {
                        eff.CheckApply(hero, target, eff);
                    }
                }
            }
        }
        else
        { 
            if (selfapp.size()!=0&&hero.dead==false)
            { 
                for (StatEff eff: selfapp) //each chance is calculated separately
                {
                    int chance=eff.chance;
                    chance+=hero.Cchance;
                    boolean succeed=CoinFlip.Flip(chance);
                    if (succeed==true) 
                    {   
                        if (hero.immunities.contains(eff.getefftype())||hero.immunities.contains(eff.getimmunityname()))
                        {
                            StatEff.applyfail(hero, eff, "immune");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Neutralise")==true)
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Undermine")==true)
                        { 
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Defence")&&(target.binaries.contains("Shattered")))
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Afflicted")==true)
                        {
                            StatEff.applyfail(hero, eff, "conflict");
                        }
                        else
                        {
                            boolean apple=eff.CheckStacking(hero, eff, eff.stackable); 
                            if (apple==true)
                            {
                                toadd.add(eff);
                            }
                            else
                            {
                                StatEff.applyfail(hero, eff, "dupe");
                            }
                        }
                    }
                    else 
                    {
                        System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
            if (!(hero.binaries.contains("Missed"))&&otherapp.size()!=0&&target!=null&&target.dead==false)
            {
                for (StatEff eff: otherapp)
                {                    
                    int chance=eff.chance; 
                    chance+=hero.Cchance; 
                    boolean succeed=CoinFlip.Flip(chance);
                    if (succeed==true)
                    {
                        eff.CheckApply(hero, target, eff); ///System.out.println ("(Test) The application chance was "+chance);
                    }
                    else
                    {
                        System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
        }
        return toadd;
    }
}