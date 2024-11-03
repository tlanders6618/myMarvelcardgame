
package myMarvelcardgamepack;

/**
 * Designer: Timothy Landers
 * Date: 17/7/22
 * Filename: Ability
 * Purpose: Creates characters' abilities.
 */
import java.util.ArrayList;
import java.util.Iterator;
public abstract class Ability
{
    boolean channelled=false; boolean finished=false; boolean interrupt=false; boolean usable=true; 
    boolean restricted=false; int restriction=0; //restricted is whether ab has unique requirements for checkuse, e.g. hp is below 100
    boolean singleuse=false; boolean used=false; //used is only for keeeping track of single use
    boolean unbound=false, control=false, elusive=false; 
    boolean ignore=false; //for ignoring disable debuffs but not suppression (nothing ignores suppression)
    boolean together=false; //whether status effects are applied separately or together
    boolean done=false; //no more targets left to attack; mainly for aoe abs
    String oname; String desc=null;
    String target; String friendly; 
    //friendly means enemy, both, either, self, ally inc, ally exc
    //target is single, self, multitarg, random, or aoe 
    boolean aoe=false; boolean attack=false;
    boolean blind=false, evade=false; //blind and evade are usually checked after using beforeabs but not in all cases; keeps track of if they were checked to avoid checking twice
    int multiuse=0; int cd=0; int dcd=0; //all abilities are initially displayed with 0 cooldown (dcd) and switch to their listed cooldowns after use 
    ArrayList <Character> ctargets; //for channelled abs
    ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); //purify, mend, etc
    ArrayList<StatEff> otherapply= new ArrayList<StatEff>(); //applied to target(s)
    ArrayList<StatEff> selfapply= new ArrayList<StatEff>(); //applied to self after turn end
    //statstrings are strings that hold stateff creation information; ensures that each stateff is its own unique object
    ArrayList<String[][]> statstrings= new ArrayList<String[][]>(); //always applied
    ArrayList<String[][]> tempstrings= new ArrayList<String[][]>(); //only sometimes applied, from passive or empower or "choose one effect" abs
    public Ability ()
    {
        /* statstrings/tempstatstrings are added when the ab is created under ab list
         * when using an ab, first multiuse, channelled, and aoe are checked for
         * then the target(s) is selected from the target array and empowerments are applied
         * beforeabs are applied and the user attacks/uses the ab
         * blind and evade are checked for under the attack method
         * the damage formula is also calculated under attack, and after, afterabs are applied
         * statstrings are gathered and sent away to be turned into stateffs and applied, and tempstatstrings are erased
         * miss and empowerments are removed
         * multihit and aoe cause the above to repeat until their ends
         * interrupt is removed, and the ab enters its cd
         * */
    }    
    public abstract boolean CheckUse (Character user);
    public void PrintDesc (boolean ignore) 
    {
        //friendly means enemy, both, either, self, ally inc, ally exc
        if (this.friendly.equals("enemy")) 
        System.out.print("Affects enemies. ");
        else if (this.friendly.equals("ally exclusive"))
        System.out.print("Affects allies. ");
        else if (this.friendly.equals("self"))
        System.out.print("Affects self. ");
        else if (this.friendly.equals("ally inclusive"))
        System.out.print("Affects allies and/or self. ");
        else 
        System.out.print("Affects allies and/or enemies. ");
        //target is single, self, multitarg, random, or aoe 
        if (this.target.equals("self")||this.target.equals("rez"))
        {
            //stop here and do nothing; also to avoid index exception below
        }
        else if (this.target.equals("single")) 
        System.out.print("Single target. ");
        else if (this.target.equals("multitarget"))
        System.out.print("Multitarget. ");
        else if (this.target.equalsIgnoreCase("aoe"))
        System.out.print("AoE. ");
        else if (this.target.equals("lowest"))
        System.out.print("Targets the character with the lowest HP. ");
        else if (this.target.equals("missing"))
        System.out.print("Targets the character with the most missing HP. ");
        else if (this.target.substring(0, 6).equals("random")) //should be no problem with index exceptions as long as random target is checked for last
        {
            System.out.print("Random target. "); 
            if (this.target.substring(7).equals("Bleed"))
            System.out.print("Prioritises enemies with "+this.target.substring(7)+". ");
            else if (Integer.valueOf(this.target.substring(this.target.length()-1))==2) //random 2
            System.out.print("Repeat this attack. ");
            else if (Integer.valueOf(this.target.substring(this.target.length()-1))==3) //random 3
            System.out.print("Repeat this attack twice. ");
        }
        if (this.cd>1)
        System.out.print(this.cd+" turn cooldown. ");
        if (this.elusive==true)
        System.out.print("Elusive. ");
        if (this.channelled==true)
        System.out.print("Channelled. ");
        if (this.singleuse==true)
        System.out.print("Single use. ");
        if (this.multiuse>0)
        System.out.print("Multiuse: "+this.multiuse+". ");
        if (this.control==true)
        System.out.print("Control. ");
        if (this.unbound==true)
        System.out.print("Unbound. ");
        for (String[][] e: this.statstrings)
        {
            if (!(e[0][0].equals("Empower"))) //empower descriptions must be done manually due to their wide range of effects; they don't fit the regular format
            {
                String Chance;
                if (e[0][4].equals("true")||e[0][4].equals("true aoe"))
                {
                    if (Integer.valueOf(e[0][1])>=500)
                    Chance="Gain ";
                    else
                    Chance=e[0][1]+"% chance to gain ";
                }
                else
                {
                    if (Integer.valueOf(e[0][1])>=500)
                    Chance="Applies ";
                    else
                    Chance=e[0][1]+"% chance to apply ";
                }
                String rest="";
                if (e[0][2].equalsIgnoreCase("true")||e[0][2].equalsIgnoreCase("false")) //for banish, drain, and reflect, which have words instead of a strength value
                {
                    if (e[0][0].equals("Banish"))
                    {
                        if (e[0][2].equalsIgnoreCase("true"))
                        rest=rest+" (Linked)";
                    }
                    else //drain and reflect
                    {
                        if (e[0][2].equalsIgnoreCase("true"))
                        rest=rest+": Half";
                        else
                        rest=rest+": Full";
                    }
                }
                else if (Integer.valueOf(e[0][2])<616) //only print strength if it has one
                rest=rest+": "+e[0][2];
                if (Integer.valueOf(e[0][3])<616) //same for duration
                {
                    rest=rest+" for "+e[0][3]+" turn(s)";
                }
                if (!(e[0][0].equals("Aura")))
                System.out.print(Chance+"a(n) "+e[0][0]+rest+". ");
                else //if aura, print what debuff it applies too
                System.out.print(Chance+"a(n) "+e[0][0]+": "+e[1][0]+rest+". ");
            }
        }
        for (SpecialAbility a: this.special)
        {
            if (a.desc!=null)
            System.out.print(a.desc);
        }
        if (this.desc!=null) //for abilities with unusual/overly specific effects
        System.out.print(desc);
    }
    public boolean GetLose() //for assists; only attackabs use this
    {
        return false;
    }
    public boolean GetMax() //for assists; only attackabs use this
    {
        return false;
    }
    public String GetAbName (Character hero)
    {
        boolean useable=this.CheckUse(hero);
        if (useable==true&&dcd==0)
        {
            return this.oname;
        }
        else if (useable==false&&dcd>0)
        {
            return this.oname+", usable in "+this.dcd+" turn(s)";
        }
        else
        {
            return this.oname+ " (disabled)";
        }
    } 
    public boolean CheckControl (Character user, Character target) //jean grey and moon knight use their passives and then ability rendered useless if target is immune
    {
        target.onSelfControlled(user);
        Character[] friends=Battle.GetTeammates(target); 
        for (Character c: friends)
        {
            if (c!=null)
            {
                c.onAllyControlled(target, user);
            }
        }
        if (!(target.immunities.contains("Control")))
        return true;
        else
        {
            System.out.println(user.Cname+"'s "+user.activeability.GetAbName(user)+" had no effect due to "+target+"'s immunity to Control.");
            return false;
        }
    }
    public abstract void CheckIgnore(Character user, boolean add); //if ability ignores a disable debuff, add it to hero binaries so stateff.checkapply doesn't cause failure
    public void UseStatStrings (Character user, Character target, boolean temp) //turn statstrings into stateffs
    {
        ArrayList<String[][]> strings=null;
        if (temp==false)
        strings=this.statstrings;
        else
        strings=this.tempstrings;
        for (String[][] array: strings)
        {  
            StatEff New=StatFactory.MakeStat(array, user); //this is how selfapply and other apply are populated
            if (array[0][4].equalsIgnoreCase("true"))
            {
                selfapply.add(New);
            }
            else if (!(user.binaries.contains("Missed"))&&array[0][4].equalsIgnoreCase("false")) //they cannot apply effects if blind or the target evaded
            {
                otherapply.add(New);
            }
            else if (!(array[0][4].equalsIgnoreCase("true aoe")))
            {
                if (target!=null&&user.id==target.id)
                {
                    selfapply.add(New);
                }
                else if (!(user.binaries.contains("Missed")))
                {
                    otherapply.add(New);
                }
            }
        }
    }
    public ArrayList<StatEff> UseAb (Character user, ArrayList<Character> targets) //only applies for the non-attack abs since they all work the same
    {
        boolean typo=true; int uses=1; 
        ArrayList<StatEff> toadd= new ArrayList<StatEff>(); //stateffs to be added to self right after turn ends
        if (multiuse>0)
        {
            System.out.println ("How many extra times would you like to use this ability?");
            do
            {
                uses=Damage_Stuff.GetInput();
                if (uses>=0&&uses<=multiuse)
                {
                    typo=false;
                }
                ++uses;
            }
            while (typo==true);
        }
        if (targets.size()<=0)
        {
            System.out.println(this.oname+" could not be used due to a lack of eligible targets."); 
            return null;
        }
        if (this.channelled==true)
        {
            this.SetChannelled(user, targets);
        }
        else
        {
            System.out.println (user+" used "+this.oname);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            Iterator<Character> iterator=targets.iterator();
            while (iterator.hasNext()==true) //use ab on each target
            {
                Character chump=iterator.next();
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) //if null, skip entirely
                {
                    if (this.ignore==true)
                    this.CheckIgnore(user, true);
                    int change=0; //does nothing, but beforeabs and empowers return ints so this stores them
                    for (StatEff eff: user.effects) //get empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, this, true);
                        }
                    }
                    if (elusive==true)
                    {
                        blind=true; evade=true; //no need to check blind or evade for elusive abs since they aren't attacks
                    }
                    for (SpecialAbility ob: special)
                    {
                        change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                    } 
                    if (this.attack==true&&this.elusive==false)
                    {
                        if (chump.team1!=user.team1) //hitting an enemy
                        {
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    this.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                    this.UseStatStrings(user, chump, false);
                    //then see what can be applied and what fails; only successful selfapply effs are added to toadd and returned to be applied after
                    toadd.addAll(Ability.ApplyStats(user, chump, together, selfapply, otherapply)); 
                    if (aoe==false)
                    {
                        for (StatEff eff: user.effects) //undo empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                int irrelevant=eff.UseEmpower(user, this, false);
                            }
                        }
                    }
                    this.ResetAb(user); //clears selfapply, resets blind/evade checks, and removes tempstrings and missed
                    if (iterator.hasNext()==false)
                    this.done=true; //done using ab because no more targets for it to affect
                    for (SpecialAbility ob: special) //specialabs only used after everything is reset
                    {
                        ob.Use(user, 616, chump); 
                    }
                    if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                    {
                        toadd.addAll(selfapply);
                        selfapply.removeAll(selfapply); 
                    }
                }
                --uses;
            }
            if (aoe==true) 
            {
                for (String[][] array: statstrings) //statstrings are checked once for each of the ab's targets, which would cause effs meant for self to be applied multiple times
                {  
                    if (array[0][4].equalsIgnoreCase("true aoe")) //this allows them to only be applied once per ab use
                    {
                        StatEff New=StatFactory.MakeStat(array, user); 
                        toadd.add(New);
                    }
                }
                for (StatEff eff: user.effects) //undo empowerments
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        int irrelevant=eff.UseEmpower(user, this, false);
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
            dcd+=cd;
        }
        this.done=false; //reset for next use
        return toadd; 
    }
    public void ResetAb(Character user) //called at the end of every ability use; resets stuff for next use/target
    {
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
        this.blind=false; this.evade=false;
        if (this.ignore==true)
        this.CheckIgnore(user, false);
    }
    public void CDReduction(int amount)
    {
        this.dcd-=amount;
        if (this.dcd<=0)
        {
            this.dcd=0; //no negative cooldowns
        }
    }
    public void SetChannelled (Character hero, ArrayList<Character> targets)
    {
        this.ctargets=targets;
        this.finished=false; //makes ab interruptable; exists to prevent printing interrupt message if hero dies after channelled activates (since there's nothing to interrupt)
        hero.effects.add(new Tracker ("Channelling "+this.oname)); //so it's impossible to forget someone is channelling
    }
    public void InterruptChannelled (Character hero) //same for all non abs
    {
        if (interrupt==false&&finished==false&&(hero.dead==true||(hero.dead==false&&!(hero.immunities.contains("Interrupt"))))) 
        {   //death must always interrupt, to avoid channels activating on resurrect; can't interrupt if ab is in middle of being used though
            this.interrupt=true;
            if (hero.dead==false)
            System.out.println(hero.Cname+"'s Channelling was interrupted!");
            StatEff remove= null;
            for (StatEff e: hero.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+this.oname))
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
        if (channelled==true&&interrupt==true) 
        {
            this.interrupt=false; //reset it so the ab is not permanently unusable
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            finished=true; //so if they die in the middle of using a channelled ab, it won't print "channel was interrupted" on death
            System.out.println ("\n"+oname+"'s channelling finished.");
            System.out.println (user.Cname+" used "+oname+"!");
            StatEff remove= null;
            for (StatEff e: user.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+ab.oname))
                {
                    remove=e; break;
                }
            }
            if (remove!=null)
            user.effects.remove(remove);  
            if (ctargets.size()<=0)
            {
                System.out.println(oname+" could not be used due to a lack of eligible targets."); //but can still apply stateffs to self
                this.UseStatStrings(user, null, true);
                this.UseStatStrings(user, null, false);
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
            Iterator<Character> iterator=ctargets.iterator();
            while (iterator.hasNext()==true) //use ab on each target
            {
                Character chump=iterator.next();
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) 
                {
                    int change=0; 
                    if (this.ignore==true)
                    this.CheckIgnore(user, true);
                    for (StatEff eff: user.effects) //get empowerments
                    {
                        if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                        {
                            change=eff.UseEmpower(user, ab, true);
                        }
                    }
                    if (elusive==true)
                    {
                        blind=true; evade=true; //no need to check blind or evade for elusive abs since they aren't attacks
                    }
                    for (SpecialAbility ob: special)
                    {
                        change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                    } 
                    if (this.attack==true)
                    {
                        if (chump.team1!=user.team1) //hitting an enemy
                        {
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                    }
                    for (SpecialAbility ob: special)
                    {
                        ob.Use(user, chump, 0); //apply unique ability functions after attacking; this only activates after abs
                    } 
                    this.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                    this.UseStatStrings(user, chump, false);
                    toadd.addAll(Ability.ApplyStats(user, chump, together, selfapply, otherapply));
                    if (aoe==false)
                    {
                        for (StatEff eff: user.effects) //undo empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                int irrelevant=eff.UseEmpower(user, ab, false);
                            }
                        }
                    }
                    this.ResetAb(user); //clears selfapply, resets blind/evade checks, and removes tempstrings and missed
                    if (iterator.hasNext()==false)
                    this.done=true; //done using ab because no more targets for it to affect
                    for (SpecialAbility ob: special) //specialabs only used after everything is reset
                    {
                        ob.Use(user, 616, chump); 
                    }
                    if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                    {
                        toadd.addAll(selfapply);
                        selfapply.removeAll(selfapply); 
                    }
                }
            }
            if (aoe==true)
            {
                for (String[][] array: statstrings) //statstrings are checked once for each of the ab's targets, which would cause effs meant for self to be applied multiple times
                {  
                    if (array[0][4].equalsIgnoreCase("true aoe")) //this allows them to only be applied once per ab use
                    {
                        StatEff New=StatFactory.MakeStat(array, user); 
                        toadd.add(New);
                    }
                }
                for (StatEff eff: user.effects) //undo empowerments
                {
                    if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                    {
                        int irrelevant=eff.UseEmpower(user, ab, false);
                    }
                }
            }
            ArrayList<StatEff> errands= new ArrayList<StatEff>(); errands.addAll(user.effects);
            for (StatEff eff: errands) //even though empowers won't activate again once used, they only expire onturnend
            {
                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                eff.onTurnEnd(user); //removes used up empowerments from scoreboard after channelled ab use, to avoid confusion/the appearance of a bug
            }
        }
        //don't go on cooldown bc useab already took care of it
        this.done=false;
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
    public int GetBaseDmg () //for chain, and some hero passives
    {
        return 0;
    }
    public int GetMultihit(boolean original) //for chain
    {
        return 0;
    }
    public void UseMultihit () //for multichain
    {
    }
    public void ReturnDamage(int dmg) //tells ab how much dmg it did for ricochet and whatnot to work properly
    {
    }
    public int GetDmgDealt () //for chain, and some hero passives
    {
        return 0;
    }
    public static Ability[] AssignAb (int index)
    {
        Ability[] abilities = new Ability[5];
        for (int i=0; i<5; i++)
        {
            Ability ab=Ability_List_Player.GetAb(index, i, false);
            abilities[i]=ab;
        }
        return abilities;
    }
    public static Ability[] AssignAbSum (int index)
    {
        Ability[] abilities = new Ability[5];
        for (int i=0; i<5; i++)
        {
            Ability ab=Ability_List_Summon.GetAb(index, i, false);
            abilities[i]=ab;
        }
        return abilities;
    }
    public static void DoRicochetDmg (int dmg, Character user, Character targ, boolean shock, String[][] e) //this both calculates and deals Ricochet damage to a random enemy
    {
        double d=dmg/2; //dmg dealt divided by 2
        dmg=5*(int)(Math.floor(d/5)); //ricochet damage; rounded down
        Character villain=Ability.GetRandomHero(user, targ, shock, true); //random enemy, or teammate if the Ricochet is from a Shock
        if (villain!=null)
        {
            if (shock==true) //shock did the dmg; no dealer
            Damage_Stuff.ElusiveDmg(null, villain, dmg, "ricochet");
            else //ability did the dmg; dealer is the ab's user
            Damage_Stuff.ElusiveDmg(user, villain, dmg, "ricochet");
            if (e!=null)
            {
                if (e[0][4].equals("false")&&villain.dead==false)
                {
                    StatEff effect=StatFactory.MakeStat(e, user);
                    StatEff.CheckApply(user, villain, effect);
                }
                else
                {
                    StatEff effect=StatFactory.MakeStat(e, user);
                    StatEff.CheckApply(user, user, effect);
                }
            }
        }
    }
    public static Character GetRandomHero(Character hero, Character targ, boolean shock, boolean ricochet) //hero is the character calling the method
    {
        //Determine team of the caller
        boolean team=hero.team1;
        if (ricochet==true&&shock==false) //called by ricochet from an attack; to avoid ricocheting the wrong target if performing an assist on an ally
        team=targ.team1;
        else if (shock==false) //need to get one of the hero's enemies; but if shock is true, leave team as is and get the hero's team for a random teammate
        team=CoinFlip.TeamFlip(team);
        boolean solo;
        if (team==true)
        solo=Battle.p1solo;
        else        
        solo=Battle.p2solo;
        //Then move on to getting the enemy
        if ((ricochet==true&&solo==false)||ricochet==false) //there must be more than one person on the target's team, or else there's no one to hurt with the ricochet
        {
            //Determine the caller's enemies or teammates
            Character[] enemies= new Character[6];
            if (shock==false) //get a random enemy
            enemies=Battle.GetTeam(team);
            else //get a random teammate
            enemies=Battle.GetTeammates(hero);
            ArrayList<Character>nenemies= CoinFlip.ToList(enemies);
            ArrayList<Character> time= new ArrayList<Character>();
            time.addAll(nenemies);
            for (Character c: time)
            {
                if (c.binaries.contains("Banished")) //cannot be hurt by ricochet dmg if banished; theyre completely out of play
                nenemies.remove(c);
            }
            boolean flag=true;
            Character villain=null; 
            //Randomly choose an enemy
            if (nenemies.size()>0)
            {
                while (flag==true)
                {
                    int rando = (int)(Math.random()*nenemies.size());
                    villain=nenemies.get(rando);
                    if (villain!=null&&villain!=hero&&villain!=targ) //cannot do ricochet dmg to self if caused by shock, or to enemy who was hit by attack that caused ricochet
                    {
                        return villain;
                    }
                }
            }
        }
        return null;
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
            int chance=0; boolean other=false;
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
                other=true; //hero has effs in otherapp
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
            else if (succeed==false)
            {
                if (hero.dead==false)
                {
                    for (StatEff eff: selfapp)
                    {
                        StatEff.applyfail(hero, eff, "chance"); //(Test) The application chance was "+chance);
                    }
                }
                if (other==true&&target.dead==false)
                {
                    for (StatEff eff: otherapp)
                    {
                        StatEff.applyfail(target, eff, "chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
            else if (succeed==true)
            {
                if (selfapp.size()!=0&&hero.dead==false)
                {
                    for (StatEff eff: selfapp)
                    {   //since these stateffs are applied after turn end (when disable debuffs tick down), the check needs to be here to account for them before they expire
                        if (StatEff.CheckFail(hero, hero, eff)==false) 
                        {
                            toadd.add(eff);
                        } //else fail due to conflict, but checkapply already prints the failure message
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
        else //same as above, but each chance is calculated separately
        { 
            if (selfapp.size()!=0&&hero.dead==false)
            { 
                for (StatEff eff: selfapp) 
                {
                    int chance=eff.chance;
                    chance+=hero.Cchance;
                    boolean succeed=CoinFlip.Flip(chance);
                    if (succeed==true) 
                    {   
                        if (StatEff.CheckFail(hero, hero, eff)==false) 
                        {
                            toadd.add(eff);
                        } //else fail due to conflict, but checkapply already prints the failure message
                    }
                    else 
                    {
                        StatEff.applyfail(hero, eff, "chance"); //(Test) The application chance was "+chance);
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
                        StatEff.CheckApply(hero, target, eff); ///System.out.println ("(Test) The application chance was "+chance);
                    }
                    else
                    {
                        StatEff.applyfail(target, eff, "chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
        }
        return toadd;
    }
}