package myMarvelcardgamepack;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * @author Timothy Landers
 * <p> Date of creation: 17/7/22
 * <p> Purpose: Implementation for the characters' abilities and the methods needed to use them.
 */
public abstract class Ability
{
    private boolean channelled=false, finished=false, interrupt=false; //finished/interrupted channelling
    private int restriction=616; //value for above restriction, e.g. 100
    private boolean singleuse=false, used=false; //whether ability's single use has been used up or not, separate from cooldowns
    private boolean unbound=false, control=false, elusive=false; 
    private boolean ignore=false; //for ignoring disable debuffs but not suppression (nothing ignores suppression)
    private boolean together=false; //whether status effects are applied separately or together
    private boolean done=false; //no more targets left to attack; mainly for aoe abs
    private String name, desc=null; //ab's name, and optional additional description (for abs too specific for printdesc)
    private String target; //target is single, self, multitarg, random, or aoe 
    private String friendly; //friendly means enemy, both, either, self, ally inc, ally exc
    private boolean aoe=false, attack=false; //whether ability is considered aoe/an attack or not
    private boolean blind=false, evade=false; //blind and evade are usually checked after using beforeabs but not always; keeps track of if they were checked to avoid checking twice
    private int multiuse=0, cd, dcd=0; //cd is the ab's default cd, and dcd (displayed cd) is how many turns are actually left until usable again 
    private ArrayList <Character> ctargets; //for holding channelled abs' targets
    private ArrayList<SpecialAbility> special= new ArrayList<SpecialAbility>(); //purify, mend, etc
    private ArrayList<StatEff> otherapply= new ArrayList<StatEff>(); //stateffs applied to target(s)
    private ArrayList<StatEff> selfapply= new ArrayList<StatEff>(); //stateffs applied to self after turn end
    //statstrings are strings that hold stateff creation information; ensures that each stateff is its own unique object
    private ArrayList<String[][]> statstrings= new ArrayList<String[][]>(); //always applied
    private ArrayList<String[][]> tempstrings= new ArrayList<String[][]>(); //inconsistently applied, e.g. from passive or empower or "choose one effect" abs
    //all for attack abs only
    private int damage=0; //dmg after empowerment, damagecounter, etc
    private int odamage=0; //base dmg
    private int dmgdealt=0; //final/actual dmg dealt; mainly for ricochet; determined by the hero.attack method where damagecalc is done
    private int multihit=0; //how many hits are currently left in a multihit attack
    private int omulti=0; //how much multihit an attack has; the number of + signs
    private boolean max=false; //whether the target directly loses max health instead of taking damage
    private boolean lose=false; //whether the target directly loses health instead of taking damage
    /**
    * Constructor for all abilities. 
    * @param title The name of the ability.
    * @param targ How many targets the ability can have.
    * @param friend Who the ability targets.
    * @param dmg Damage dealt by the ability, if any
    * @param cd Cooldown of the ability, if any
    * @param mult Multihit of the ability, if any
    * @param traits Which traits the ability has, if any
    * @see Trait
    */
    public Ability (String title, String targ, String friend, int dmg, int cd, int mult, Trait[] traits)
    {
        this.name=title; this.target=targ; this.friendly=friend; this.cd=cd;
        if (friend.equalsIgnoreCase("enemy")||friend.equalsIgnoreCase("both")||friend.equalsIgnoreCase("either")) //anything other than ally or self
        {
            this.attack=true;
        }
        if (targ.equalsIgnoreCase("aoe"))
        {
            this.aoe=true;
        }
        this.damage=dmg; this.odamage=dmg;
        this.multihit=mult; this.omulti=mult;
        if (traits!=null)
        this.setAttributes(traits);
        /* the process of using an ab is as follows:
         * statstrings/tempstatstrings are added when the ab is created under ab list
         * when using an ab, first multiuse, channelled, and aoe are checked for
         * then the target(s) is selected from the target array and empowerments are applied
         * beforeabs are applied and the user attacks/uses the ab
         * blind and evade are checked for under the attack method
         * the damage formula is also calculated under attack, and then afterabs are applied
         * statstrings are converted into stateffs to be placed into self/otherapply
         * the stateffs in self/otherapply are checked against immunities and existing stateffs (e.g. safeguard), then applied or not
         * tempstatstrings are erased, along with self/otherapply
         * miss and empowerments are removed
         * multihit and aoe cause the above to repeat until their ends
         * interrupt is removed, and the ab enters its cd
         * */
    }
    private void setAttributes (Trait[] traits) //together, desc, etc
    {
        for (Trait trait: traits) //should be set at creation and never changed
        {
            switch (trait)
            {
                case CHANNELLED: this.channelled=true; break;
                case SINGLEUSE: this.singleuse=true; break;
                case UNBOUND: this.unbound=true; break;
                case TOGETHER: this.together=true; break; //separate is the default due to being slightly more common, so together must be manually set
                case IGNORE: this.ignore=true; break;
                case CONTROL: this.control=true; break;
                case ELUSIVE: this.elusive=true; break;
                case LOSE: this.lose=true; break;
                case MAX: this.max=true; break;
                case MULTIUSE1: this.multiuse=1; break;
                case MULTIUSE2: this.multiuse=2; break;
                case MULTIUSE3: this.multiuse=3; break;
                case SEALED1: this.dcd=1; break;
                case RESTRICTPENANCE: this.restriction=77; break;
                case RESTRICTRULK: this.restriction=78; break;
                default: System.out.println("Error with ability construction. Invalid trait: "+trait);
            }
        }
    }
    /**
    * Does NOT set an ability's description. If an ability does something unique such that printDesc can't auto generate a description for it (e.g. Elixir's Life Transfer),
    * setDesc manually appends that information to the ability's description. 
    * @param d The additional information to add
    * @see printDesc
    */
    public void setDesc (String d)
    {
        if (this.desc==null) //can only be set once
        this.desc=d;
    }
    /**
    * Returns how much damage an ability is supposed to do (i.e. the number in its description), used for damage calculations.
    * @return The ability's base damage
    */
    public int getBaseDmg ()
    {
        return odamage;
    }
    /**
    * Returns how much multihit an ability is supposed to have, used for Chain and when attacking.
    * @param original Whether to retrieve the original/base multihit (the number in the ability description) or current multihit (hits left in the current attack)
    * @return The ability's multihit
    * @see Chain
    */
    public int getMultihit(boolean original) 
    {
        if (original==true)
        return omulti;
        else
        return multihit;
    }
    /**
    * Decrements an ability's multihit after completing an attack.
    */
    public void useMultihit () //also for chain
    {
        --multihit;
    }
    /**
    * Lets the ability know how much damage it ultimately dealt, after damage calculations (returns the damage to the ability).
    * @param d The damage dealt
    */
    public void returnDamage (int d)
    {
        dmgdealt=d;
    }
    /**
    * Returns how much damage the ability ultimately dealt, after damage calculations; used for ricochet and some passives.
    * @return The damage dealt
    */
    public int getDmgDealt () 
    {
        return dmgdealt;
    }
    /**
    * Returns whether the ability causes health loss instead of doing damage, used when attacking or assisting.
    * @return True if the ability causes health loss
    */
    public boolean getLose() 
    {
        return this.lose;
    }
    /**
    * Returns whether the ability causes max health reduction instead of doing damage, used when attacking or assisting.
    * @return True if the ability causes max health reduction
    */
    public boolean getMax() 
    {
        return this.max;
    }
    /**
    * Returns either the ability's base cooldown (the number written in its description) or its current/displayed cooldown.
    * @param displayed Whether to return the displayed cooldown or not
    * @return The ability's cooldown
    */
    public int getCD (boolean displayed)
    {
        if (displayed==true)
        return this.dcd;
        else
        return this.cd;
    }
    /**
    * Returns whether the ability is an attack or not. Generally speaking, anything that targets an enemy is an attack, and everything else is not.
    * @return True if the ability is considered an attack
    */
    public boolean isAttack()
    {
        return this.attack;
    }
    /**
    * Returns whether the ability's status effects are applied together or separately.
    * @return True if status effects are applied together
    */
    public boolean isTogether ()
    {
        return this.together;
    }
    /**
    * Returns whether an ability is considered AoE or not.
    * @return True if the ability is AoE
    */
    public boolean isAoE ()
    {
        return this.aoe;
    }
    /**
    * Lets an ability know that it's already checked for Blind on its user. This prevents Blind's miss chance from being calculated more than once per attack.
    */
    public void checkedBlind()
    {
        this.blind=true;
    }
    /**
    * Lets an ability know that it's already checked for Evade on its target. This prevents Evade from being consumed more than once per attack.
    */
    public void checkedEvade ()
    {
        this.evade=true;
    }
    /**
    * Lets an ability know that it's done attacking, mainly to notify AoE abilities that they're out of targets.
    * @param i True if the ability is done 
    */
    public void setDone (boolean i)
    {
        this.done=i;
    }
    /**
    * Returns whether an ability is done attacking or not.
    * @return True if the ability is done
    */
    public boolean isDone ()
    {
        return this.done;
    }
    /**
    * Adds a special ability to an ability.
    * @param i The ability being added
    * @see SpecialAbility
    */
    public void addSpecial (SpecialAbility i)
    {
        this.special.add(i);
    }
    /**
    * Returns all of an ability's special abilities.
    * @return The ability's special abilities 
    * @see SpecialAbility
    */
    public ArrayList<SpecialAbility> getSpecial ()
    {
        return this.special;
    }
    /**
    * Adds a string representation of a status effect (statstring) to be applied by an ability.
    * @param s The statstring being added
    * @see StatFactory
    */
    public void addStatString (String[][] s)
    {
        this.statstrings.add(s);
    }
    /*public void setStatStrings (boolean normal, String[][] i)
    {
        if (normal==true)
        this.statstrings.add(i);
        else
        this.tempstrings.add(i);
    }*/
    /**
    * Returns string representations of which status effects an ability is supposed to apply.
    * @param base True to return the ability's base status effects (the ones in its description). False to return its temporary status effects (e.g. from Empower).
    * @return The ability's statstrings 
    */
    public ArrayList<String[][]> getStatStrings (boolean base)
    {
        if (base==true)
        return this.statstrings;
        else
        return this.tempstrings;
    }
    /**
    * Once a status effect has been created, the ability checks it against the user's chance, the target's immunities, etc to see if it can be applied.
    * This lets the ability know whether the created stateff is to be applied to the ability's user or the ability's target. 
    * All of an ability's created stateffs must be registered this way.
    * @param self True if the stateff should be applied to the ability's user
    * @param s The stateff 
    */
    public void setApply (boolean self, StatEff s)
    {
        if (self==true)
        this.selfapply.add(s);
        else
        this.otherapply.add(s);
    }
    /*
    * Returns all of an ability's special abilities.
    * @return 
    *
    public ArrayList<StatEff> getApply (boolean self)
    {
        if (self==true)
        return this.selfapply;
        else
        return this.otherapply;
    }*/
    /*
     * After the ability's status effects have applied 
     */
    public void clearApply (boolean self)
    {
        if (self==true)
        this.selfapply.removeAll(this.selfapply);
        else
        this.otherapply.removeAll(this.otherapply);
    }*/
    public void setMultiuse(int i)
    {
        this.multiuse=i;
    }
    public int getMultiuse ()
    {
        return this.multiuse;
    }
    public void setSingleUse (boolean init, boolean arg)
    {
        if (init==true)
        this.singleuse=arg;
        else
        this.used=arg;
    }
    public boolean getSingleUse (boolean used)
    {
        if (used==true)
        return this.used;
        else
        return this.singleuse;
    }
    public void setChannelled (boolean i)
    {
        this.channelled=i;
    }
    public boolean isChannelled ()
    {
        return this.channelled;
    }
    public void setIgnore(boolean i)
    {
        this.ignore=i;
    }
    public boolean getIgnore ()
    {
        return this.ignore;
    }
    public void setElusive(boolean i)
    {
        this.elusive=i;
    }
    public boolean isElusive ()
    {
        return this.elusive;
    }
    public boolean isControl ()
    {
        return this.control;
    }
    public int getRestriction ()
    {
        return this.restriction;
    }
    public String getAbName (Character hero, boolean original)
    {
        if (original==true)
        {
            return this.name;
        }
        else //printing ability names on turn; player needs to see useability
        {
            boolean useable=this.checkUse(hero);
            if (useable==false&&dcd>0)
            {
                return this.name+", usable in "+this.dcd+" turn(s)";
            }
            else if (useable==false)
            {
                return this.name+ " (disabled)";
            }
            else
            {
                return this.name;
            }
        }
    } 
    public boolean checkUse (Character user)
    {
        if (user.CheckFor("Suppression", false)==true||user.CheckFor("Persuaded", false)==true)
        {
            return false;
        }
        else if (this.singleuse==true&&this.used==true) 
        {
            return false;
        }
        else if (this.dcd>0) //on cooldown/sealed
        {
            return false;
        }
        else
        return true;
    }
    /**
    * 
    * @param ignore
    * @see
    */
    public void printDesc () 
    {
        if (this instanceof AttackAb)
        {
            this.PrintDescAttack();
        } 
        //friendly means enemy, both, either, self, ally inc, ally exc
        switch (this.friendly)
        {
            case "enemy": System.out.print("Affects enemies. "); break;
            case "ally exclusive": System.out.print("Affects allies. "); break;
            case "ally inclusive": System.out.print("Affects allies and/or self. "); break;
            case "self": System.out.print("Affects self. "); break;
            case "both": System.out.print("Affects allies and enemies. "); break;
            case "either": System.out.print("Affects allies and/or enemies. "); break;
            default: System.out.print("Error: typo in the ability's friendly attribute.");
        }
        //target is single, self, multitarg, random, or aoe 
        switch (this.target)
        {
            case "self": case "rez": break; //stop here and do nothing; also to avoid index exception below
            case "single": System.out.print("Single target. "); break;
            case "multitarget": System.out.print("Multitarget. "); break;
            case "AoE": System.out.print("AoE. "); break;
            case "lowest": System.out.print("Targets the character with the lowest HP. "); break;
            case "missing": System.out.print("Targets the character with the most missing HP. "); break;
            default:
            if (this.target.substring(0, 6).equals("random")) //should be no problem with index exceptions since random target is checked for last
            {
                System.out.print("Random target. "); 
                if (this.target.substring(7).equals("Bleed"))
                System.out.print("Prioritises enemies with "+this.target.substring(7)+". ");
                else if (Integer.valueOf(this.target.substring(this.target.length()-1))==2) //random 2
                System.out.print("Repeat this attack. ");
                else if (Integer.valueOf(this.target.substring(this.target.length()-1))==3) //random 3
                System.out.print("Repeat this attack twice. ");
            }
            else
            System.out.print("Error: typo in the ability's target attribute. ");
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
    private void PrintDescAttack ()
    {
        if (this instanceof BasicAb) 
        System.out.print("Basic attack. "); 
        else
        System.out.print("Attack ability. "); 
        if (this.lose==true)
        System.out.print("Causes "+damage+" HP loss. ");
        else if (this.max==true)
        System.out.print("Reduces max HP by "+damage+". ");
        else
        System.out.print("Does "+damage+" damage. ");
        if (this.multihit>0)
        System.out.print("Multihit: "+this.omulti+". ");
        if (this.getIgnore()==true)
        System.out.print("Ignores Disarm. ");
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
            System.out.println(user.Cname+"'s "+user.activeability.getAbName(null, true)+" had no effect due to "+target+"'s immunity to Control.");
            return false;
        }
    }
    public abstract void CheckIgnore(Character user, boolean add); //if ability ignores a disable debuff, add it to hero binaries so stateff.checkapply doesn't cause failure
    public void UseStatStrings (Character user, Character target, boolean temp) //turn statstrings into stateffs to be added to self/otherapply
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
    public void CDReduction (int amount)
    {
        this.dcd-=amount;
        if (this.dcd<=0)
        {
            this.dcd=0; //no negative cooldowns
        }
    }
    public ArrayList<StatEff> UseAb (Character user, ArrayList<Character> targets) //only applies for the non-attack abs since they all work the same
    {
        if (this instanceof AttackAb)
        {
            return this.UseAbAttack(user, targets);
        } //otherwise proceed with below method
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
            System.out.println(this.name+" could not be used due to a lack of eligible targets."); 
            return null;
        }
        if (this.channelled==true)
        {
            this.PrepChannelled(user, targets);
        }
        else
        {
            System.out.println (user+" used "+this.name);
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
    private ArrayList<StatEff> UseAbAttack (Character user, ArrayList<Character> targets) //doesn't call check ignore since disarm doesn't require it
    { 
        boolean typo=true; int uses=1;   
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
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
        if (this.channelled==true)
        {
            this.PrepChannelled(user, targets);
        }
        else
        {
            System.out.println (user+" used "+this.name);
        }
        while (uses>0&&channelled==false) //repeat the attack for each multiuse; channelled abilities will do nothing now and activate later
        {
            if (targets.size()<=0)
            {
                System.out.println(this.name+" could not be used due to a lack of eligible targets.");
                return null;
            }
            Iterator<Character> iterator=targets.iterator();
            while (iterator.hasNext()==true)
            {
                Character chump=iterator.next();
                boolean okay=true;
                if (chump!=null&&this.control==true)
                okay=CheckControl(user, chump);
                if (chump!=null&&okay==true) //if null, skip entirely
                {
                    do 
                    {
                        int change=0;
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, this, true);
                                damage+=change;
                                if (damage<0)
                                damage=0;
                            }
                        }
                        if (elusive==true)
                        {
                            blind=true; evade=true; //no need to check blind or evade for elusive abs since they aren't attacks
                        }
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                            damage+=change;
                            if (damage<0)
                            damage=0;
                        } 
                        if (elusive==true&&(this.odamage>0||this.damage>0)) //only try to do damage if attack was meant to do damage; abs that call assists shouldn't print
                        {
                            Damage_Stuff.ElusiveDmg(user, chump, damage, "default");
                        }
                        else if (lose==true) //modified version of attacknodamage method
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, false); //ondamage since loss and reduce are unaffected by dmg modification
                        }
                        else if (max==true)
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, true);
                        }
                        else 
                        {
                            if (this.odamage>0||this.damage>0) //only call attack method if attack was meant to do dmg
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                            else
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
                        } 
                        this.UseStatStrings(user, chump, true); //turn ab's statstrings into stateffs
                        this.UseStatStrings(user, chump, false);
                        //then see what can be applied and what fails; only successful selfapply effs are added to toadd and returned to be applied after ab usage
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
                        this.UseMultihit();
                        if (iterator.hasNext()==false)
                        this.done=true;
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, dmgdealt, chump); 
                        }
                        if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                        {
                            toadd.addAll(selfapply);
                            selfapply.removeAll(selfapply); 
                        }
                        this.dmgdealt=0;
                        damage=odamage; //reset damage 
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
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
        this.done=false;
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
    public void PrepChannelled (Character hero, ArrayList<Character> targets)
    {
        this.ctargets=targets;
        this.finished=false; //makes ab interruptable; exists to prevent printing interrupt message if hero dies after channelled activates (since there's nothing to interrupt)
        hero.effects.add(new Tracker ("Channelling "+this.name)); //so it's impossible to forget someone is channelling
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
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+this.name))
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
        if (this instanceof AttackAb)
        {
            return this.ActivateChannelledAttack(user, ab);
        } //else proceed with below
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();
        if (channelled==true&&interrupt==true) 
        {
            this.interrupt=false; //reset it so the ab is not permanently unusable
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            finished=true; //so if they die in the middle of using a channelled ab, it won't print "channel was interrupted" on death
            System.out.println ("\n"+name+"'s channelling finished.");
            System.out.println (user.Cname+" used "+this.name+"!");
            StatEff remove= null;
            for (StatEff e: user.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+ab.name))
                {
                    remove=e; break;
                }
            }
            if (remove!=null)
            user.effects.remove(remove);  
            if (ctargets.size()<=0)
            {
                System.out.println(ab.name+" could not be used due to a lack of eligible targets."); //but can still apply stateffs to self
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
    private ArrayList<StatEff> ActivateChannelledAttack(Character user, Ability ab)
    {
        ArrayList<StatEff> toadd= new ArrayList<StatEff>();   
        if (channelled==true&&interrupt==true) 
        {
            interrupt=false; //reset it so the ab is not permanently unusable
            if (singleuse==true)
            {
                used=true;
            }
            else
            {
                dcd+=cd;
            }
            return null;
        }
        else if (channelled==true&&interrupt==false)
        {
            finished=true; //so if they die in the middle of using a channelled ab, it won't print "channel was interrupted" on death
            System.out.println (this.name+"'s channelling finished.");
            System.out.println (user.Cname+" used "+this.name+"!");
            StatEff remove= null;
            for (StatEff e: user.effects)
            {
                if (e instanceof Tracker&&e.geteffname().equals("Channelling "+ab.name))
                {
                    remove=e; break;
                }
            }
            if (remove!=null)
            user.effects.remove(remove);  
            int omulti=multihit;
            int change=0;
            if (ctargets.size()<1)
            {
                System.out.println(name+" could not be used due to a lack of eligible targets."); //but can still apply stateffs to self
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
                if (chump!=null&&okay==true) //if null, skip entirely
                {
                    do 
                    {
                        for (StatEff eff: user.effects) //get empowerments
                        {
                            if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                            {
                                change=eff.UseEmpower(user, ab, true);
                                damage+=change;
                                if (damage<0)
                                damage=0;
                            }
                        }
                        if (elusive==true)
                        {
                            blind=true; evade=true; //no need to check blind or evade for elusive abs since they aren't attacks
                        }
                        for (SpecialAbility ob: special)
                        {
                            change=ob.Use(user, chump); //apply unique ability functions before attacking; this only affects before abs
                            damage+=change;
                            if (damage<0)
                            damage=0;
                        } 
                        if (elusive==true&&(this.odamage>0||this.damage>0)) //only print damage if attack was meant to do damage; abs that call assists shouldn't print
                        {
                            Damage_Stuff.ElusiveDmg(user, chump, damage, "default");
                        }
                        else if (lose==true) //modified version of attacknodamage method
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, false); //ondamage since loss and reduce are unaffected by dmg modification
                        }
                        else if (max==true)
                        {
                            chump=user.AttackNoDamage(user, chump, odamage, aoe, true);
                        }
                        else 
                        {
                            if (this.odamage>0||this.damage>0)
                            chump=user.Attack(user, chump, damage, aoe); //damage formula is calculated here
                            else
                            chump=user.AttackNoDamage(user, chump, aoe); //let chump know he's been attacked
                        }
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, chump, dmgdealt); //apply unique ability functions after attacking; this only activates after abs
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
                        this.UseMultihit();
                        if (iterator.hasNext()==false)
                        this.done=true; //done using ab because no more targets for it to affect
                        for (SpecialAbility ob: special)
                        {
                            ob.Use(user, dmgdealt, chump); 
                        }
                        if (selfapply.size()!=0) //here to enable functionality of the specialab Use
                        {
                            toadd.addAll(selfapply);
                            selfapply.removeAll(selfapply); 
                        }
                        this.dmgdealt=0;
                        damage=odamage; //reset damage 
                    }
                    while (multihit>-1); //then repeat the attack for each multihit
                    multihit=omulti; //reset the multihit counter for the next use
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
            for (StatEff eff: errands) //even though empowers won't activate again once used, they only expire onturnend and channelled abs activate on turnstart
            {
                if (eff.getimmunityname().equalsIgnoreCase("Empower"))
                eff.onTurnEnd(user); //removes used up empowerments from scoreboard after channelled ab use, to avoid confusion/the appearance of a bug
            }
        } 
        //don't go on cooldown bc useab already took care of it
        this.done=false; //reset for next use
        return toadd;
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
                    chance=eff.getChance();
                    chance+=hero.Cchance;
                    break; //they all have to have the same chance if they're applied together
                }
            }
            else if (!(hero.binaries.contains("Missed"))&&otherapp.size()!=0) //if the attack applies no status effects (if both arrays are empty), nothing happens
            {
                other=true; //hero has effs in otherapp
                for (StatEff eff: otherapp)
                {
                    chance=eff.getChance();
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
                    int chance=eff.getChance();
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
                    int chance=eff.getChance();
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