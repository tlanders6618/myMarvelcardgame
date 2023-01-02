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
    boolean channelled=false; boolean singleuse=false; boolean used=false; //only if ability is single use
    boolean together=true; //whether status effects are applied separately or together
    String name; String oname; String target; String friendly; 
    //friendly means ally, enemy, both, either, neither, self, ally inc, ally exc
    //target is single, self, multitarg, random, or aoe 
    int multiuse=0; int cd=0; int dcd=0; //all abilities are initially displayed with 0 cooldown and switch to their listed cooldowns after use 
    public Ability ()
    {
    }    
    public abstract ArrayList<StatEff> UseAb (Character user, Ability ab, ArrayList<Character> targets);
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
    public abstract boolean CheckUse (Character user, Ability ab);
    public abstract String GetAbName(Character b, Ability h);
    public abstract void InterruptChannelled(); 
    public abstract void ActivateChannelled();
    public abstract void SetChannelled(Character ad, Ability a, ArrayList<Character> f);
    public abstract void AddStatString (String[] p);
    public abstract void AddTempString (String[] p);
    public int ReturnBaseDmg () //reckon this is for jean, thing, etc
    {
        return 0;
    }
    public void ReturnDamage(int dmg) //for ricochet and whatnot to work properly
    {
    }
    public static Ability[] AssignAb (int index)
    {
        Ability[] abilities = new Ability[5];
        int counter=0;
        Ability ab1=Ability_List_Player.GetAb(index, counter);
        ++counter;
        Ability ab2=Ability_List_Player.GetAb(index, counter);
        ++counter;
        Ability ab3=Ability_List_Player.GetAb(index, counter);
        ++counter;
        Ability ab4=Ability_List_Player.GetAb(index, counter);
        ++counter;
        Ability ab5=Ability_List_Player.GetAb(index, counter);
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
        Ability ab1=Ability_List_Summon.GetAb(index, counter);
        ++counter;
        Ability ab2=Ability_List_Summon.GetAb(index, counter);
        ++counter;
        Ability ab3=Ability_List_Summon.GetAb(index, counter);
        ++counter;
        Ability ab4=Ability_List_Summon.GetAb(index, counter);
        ++counter;
        Ability ab5=Ability_List_Summon.GetAb(index, counter);
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
        double d=dmg/2;
        dmg=5*(int)(Math.floor(d/5)); //ricochet damage; rounded down
        Character villain=Ability.GetRandomEnemy(user, shock); //random enemy, or teammate if the Ricochet is from a Shock
        if (villain!=null)
        {
            villain.TakeRicochetDamage(villain, dmg); //random enemy takes the damage
        }
    }
    public static Character GetRandomEnemy(Character hero, boolean shock) //hero is the character calling the method
    {
        //Determine team of the caller
        boolean team=hero.team1; 
        if (shock==false) //get the opponent's team; if shock is true, leave team as is and get the character's team for a random teammate
        {
            team=Card_CoinFlip.TeamFlip(team);
        }
        int size=Battle.GetTeamSize(team);
        if (size>1) //there must be more than one person on the team
        {
            //Determine the caller's enemies or teammates
            Character[] enemies= new Character[6];
            enemies=Battle.GetTeammates(hero, team);
            boolean flag=true;
            Character villain=null; Character ban;
            if (team==hero.team1) //so the ricochet can't harm the hero who took the initial hit
            {
                ban=hero;
            }
            else
            {
                ban=null;
            }
            //Randomly choose an enemy
            while (flag==true)
            {
                int rando = (int)(Math.random()*enemies.length);
                villain=enemies[rando];
                if (villain!=null&&!(villain.equals(ban))&&villain.CheckFor(villain, "Banish")==false&&villain.dead==false)
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
            else if (otherapp.size()!=0) //if the attack applies no status effects (if both arrays are empty), nothing happens
            {
                for (StatEff eff: otherapp)
                {
                    chance=eff.chance;
                    chance+=hero.Cchance;
                    break; 
                }
            }
            boolean succeed=Card_CoinFlip.Flip(chance); 
            if (selfapp.size()==0&&otherapp.size()==0)
            { //do not print anything since there are no effects to apply
            }
            else if (succeed==false)
            {
                System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
            }
            else if (succeed==true)
            {
                if (selfapp.size()!=0)
                {
                    for (StatEff eff: selfapp)
                    {
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&(hero.CheckFor(hero, "Buff Disable")==true||hero.CheckFor(hero, "Disrupt")==true))
                        { 
                            StatEff.applyfail(hero, eff);
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                        {
                            StatEff.applyfail(hero, eff);
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                        {
                            StatEff.applyfail(hero, eff);
                        }
                        else
                        {
                            toadd.add(eff);
                        }
                    }
                }
                if (otherapp.size()!=0)
                {
                    for (StatEff eff: otherapp)
                    {
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&(hero.CheckFor(hero, "Buff Disable")==true||hero.CheckFor(hero, "Disrupt")==true))
                        {
                            StatEff.applyfail(hero, eff);
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                        {
                            StatEff.applyfail(hero, eff);
                        }
                        else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                        {
                            StatEff.applyfail(hero, eff);
                        }
                        else if (target.dead==false)
                        {
                            eff.CheckApply(hero, target, eff);
                        }
                    }
                }
            }
        }
        else
        { 
            if (selfapp.size()!=0)
            { 
                for (StatEff eff: selfapp) //each chance is calculated separately
                {
                    int chance=eff.chance;
                    chance+=hero.Cchance;
                    boolean succeed=Card_CoinFlip.Flip(chance);
                    boolean print=true;
                    if (eff.getefftype().equalsIgnoreCase("Buffs")&&(hero.CheckFor(hero, "Buff Disable")==true||hero.CheckFor(hero, "Disrupt")==true))
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    if (succeed==true)
                    {
                        toadd.add(eff); 
                    }
                    else if (print==true)
                    {
                        System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
            if (otherapp.size()!=0)
            {
                for (StatEff eff: otherapp)
                {                    
                    int chance=eff.chance; 
                    chance+=hero.Cchance; 
                    boolean succeed=Card_CoinFlip.Flip(chance);
                    boolean print=true;
                    if (eff.getefftype().equalsIgnoreCase("Buffs")&&(hero.CheckFor(hero, "Buff Disable")==true||hero.CheckFor(hero, "Disrupt")==true))
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                    {
                        succeed=false; print=false;
                        StatEff.applyfail(hero, eff);
                    }
                    if (succeed==true&&target.dead==false)
                    {
                        eff.CheckApply(hero, target, eff); ///System.out.println ("(Test) The application chance was "+chance);
                    }
                    else if (print==true)
                    {
                        System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
        }
        return toadd;
    }
}