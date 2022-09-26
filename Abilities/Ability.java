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
    public Ability ()
    {
    }    
    public abstract ArrayList<StatEff> UseAb (Character user, Ability ab, Character[] targets);
    public abstract void CDReduction();
    public abstract boolean CheckUse (Character user, Ability ab);
    public abstract String GetAbName(Character b, Ability h);
    public abstract int GetAbDamage(Character b, Ability h);
    public abstract String GetFriendly(); //ally inc, ally exc, ally, enemy, both ally exc and enemy, both ally inc and enemy, neither, either
    public abstract String GetTargetType(); //multitarget, random, single, aoe, self
    public abstract boolean GetChannelled();
    public abstract void InterruptChannelled(); 
    public abstract void ActivateChannelled();
    public abstract void SetChannelled(Character ad, Ability a, Character[] f);
    public abstract boolean GetTogether();
    public abstract void AddStatString (String[] p);
    public abstract void AddTempString (String[] p);
    /**public void ChangeTarget (Character t)
    {
    }*/
    public int ReturnDamage (int g) //reckon this is for jean, thing, etc
    {
        return 616;
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
        villain.TakeRicochetDamage(villain, dmg); //random enemy takes the damage
    }
    public static Character GetRandomEnemy(Character hero, boolean shock) //hero is the character calling the method
    {
        //Determine team of the caller
        boolean team=hero.team1; 
        if (shock==false) //get the opponent's team, otherwise leave team as is and get the character's team for a random teammate
        {
            if (team==true) //hero is on team1
            {
                team=false; //set it to be opposite in order to get their opponent's team
            }
            else if (team==false) //hero is on team2
            {
                team=true;
            }
        }
        //Determine the caller's enemies or teammates
        Character[] enemies= new Character[6];
        enemies=Battle.GetTeammates(hero, team);
        boolean flag=true;
        Character villain=enemies[0];
        //Randomly choose an enemy
        while (flag==true)
        {
            int rando = (int)(Math.random()*enemies.length);
            villain=enemies[rando];
            if (villain!=null&&!(villain.CheckFor(villain, "Banish")&&villain.dead==false))
            {
                flag=false;
            }
        }
        return villain;
    }
    public static ArrayList<StatEff> ApplyStats (Character hero, Character target, boolean together, ArrayList<StatEff> selfapp, ArrayList<StatEff> otherapp)
    {
        ArrayList<StatEff> toadd= new ArrayList<StatEff>(); //this is only for selfapp 
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
                    chance=eff.getchance();
                    chance+=hero.Cchance;
                    break; //they all have to have the same chance if they're applied together
                }
            }
            else if (otherapp.size()!=0) //if the attack applies no status effects (if both arrays are empty), nothing happens
            {
                for (StatEff eff: otherapp)
                {
                    chance=eff.getchance();
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
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Buff Disable")==true)
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
                        if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Buff Disable")==true)
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
                    int chance=eff.getchance();
                    chance+=hero.Cchance;
                    boolean succeed=Card_CoinFlip.Flip(chance);
                    if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Buff Disable")==true)
                    {
                        succeed=false;
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                    {
                        succeed=false;
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                    {
                        succeed=false;
                    }
                    if (succeed==true)
                    {
                        toadd.add(eff); 
                    }
                    else
                    {
                        System.out.println (hero.Cname+"'s effect(s) failed to apply due to chance"); //(Test) The application chance was "+chance);
                    }
                }
            }
            if (otherapp.size()!=0)
            {
                for (StatEff eff: otherapp)
                {                    
                    int chance=eff.getchance(); 
                    chance+=hero.Cchance; 
                    boolean succeed=Card_CoinFlip.Flip(chance);
                    if (eff.getefftype().equalsIgnoreCase("Buffs")&&hero.CheckFor(hero, "Buff Disable")==true)
                    {
                        succeed=false;
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Debuffs")&&hero.CheckFor(hero, "Debuff Disable")==true)
                    {
                        succeed=false;
                    }
                    else if (eff.getefftype().equalsIgnoreCase("Heal")&&hero.CheckFor(hero, "Heal Disable")==true)
                    {
                        succeed=false;
                    }
                    if (succeed==true&&target.dead==false)
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