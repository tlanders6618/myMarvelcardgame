package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 26/7/22
 * Filename: Summon
 * Purpose: To make summons.
 */
import java.util.ArrayList; 
public class Summon extends Character
{
    String Cname="Filson Wisk";    
    int size; //amount of character slots they take up
    int HP, maxHP=0; //health
    int turn=0; //++ at start of turn; which turn they are on
    int DR=0; //damage reduction
    int ADR=0; //damage reduction from all sources
    int BuDR, BlDR, PoDR, ShDR, CoDR=0; //dot damage reduction
    int DV=0; //damage vulnerability
    int BD=0; //bonus damage
    int CC=0; //crit chance
    double critdmg=1.5;
    int SHLD=0; //shield
    int index;
    int Torder; //character's place in the turn order
    boolean team1; //which team they're on, for determining who's an ally and who's an enemy
    boolean dead=false; 
    int accuracy=100; //for blind
    int dmgtaken=0;
    int hash;
    int passivecount;
    Character[] mysummoner= new Character[1];
    Ability[] activeability= new Ability[0];
    Ability[] abilities = new Ability[5];
    ArrayList<StatEff> effects= new ArrayList<StatEff>(); //holds status effects
    ArrayList<String> immunities= new ArrayList<String>(); 
    ArrayList<String> binaries=new ArrayList<String>(); //binaries: wounded, stunned, banished, untargetable, disrupted, disarmed, heal/debuff/buff/ability disabled, shattered, etc
    ArrayList<String> ignores=new ArrayList<String>();
    public Summon (int Sindex)
    {
        //Same as with characters
        index=Sindex;
        hash=Card_HashCode.RandomCode();
        Cname=SetNameSum(index);
        size=SetSizeSum(index);
        if (Sindex==13)
        {
            //clone health and abilities depend on who they're a clone of
        }
        else
        {
            HP=InHPSum (index, HP);
            abilities=Ability.AssignAbSum(index);
        }
        maxHP=HP;
    }
    public void onSummon (Summon lad)
    {
        //for triggering passives
        switch (lad.index)
        {
        }
        int ahcounter=0; 
        if (lad.team1==true) //this is so summons don't all have the same name (e.g. three enemies named "Thug")
        {
            for (Character hero: Battle.team1)
            {
                if (hero instanceof Summon)
                {
                    String name=Summon.SetNameSum(hero.index);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            for (Character hero: Battle.team1dead)
            {
                if (hero instanceof Summon)
                {
                    String name=Summon.SetNameSum(hero.index);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            if (ahcounter>0)
            {
                lad.Cname=Summon.AlterSumName(lad.index, ahcounter);
            }
        }
        else
        {//checks if any character has the same name as them
            for (Character hero: Battle.team2)
            {
                if (hero instanceof Summon)
                {
                    String name=Summon.SetNameSum(hero.index);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            for (Character hero: Battle.team2dead)
            {
                if (hero instanceof Summon)
                {
                    String name=Summon.SetNameSum(hero.index);
                    if (name.equals(lad.Cname))
                    {
                        ++ahcounter;
                    }
                }
            }
            if (ahcounter>0)
            {
                lad.Cname=Summon.AlterSumName(lad.index, ahcounter);
            }
        }
    }
    public static int InHPSum (int index, int health)
    {
        switch (index)
        {
            case 7: 
            health= 5; break;      
            
            case 1: case 10: case 11:
            health= 40;  break; 
            
            case 5: case 9:
            health= 50; break;
            
            case 2: case 3: case 6:
            health= 60; break;   
            
            case 8:
            health= 70; break;
            
            case 4: 
            health= 100; break;
            
            case 12:
            health= 200; break;
        }        
        return health;
    }
    public static String SetNameSum (int index)
    {
        String name;
        switch (index)
        {
            case 1: name="Nick Fury LMD (Summon)"; break;
            case 2: name="AIM Rocket Trooper (Summon)";break;
            case 3: name="AIM Crushbot (Summon)"; break;
            case 4: name="Ultron Drone (Summon)"; break;
            case 5: name="Doombot (Summon)"; break;
            case 6: name="Lesser Demon (Summon)"; break;
            case 7: name="Holographic Decoy (Summon)"; break;
            case 8: name="Ice Golem (Summon)"; break;
            case 9: name="HYDRA Trooper (Summon)"; break;
            case 10: name="Thug (Summon)"; break;
            case 11: name="Mirror Image (Summon)"; break;
            case 12: name="Giganto (Summon)"; break;
            default: name="Error with Summon index";
        }    
        return name;
    }
    public static String AlterSumName (int index, int counter)
    {
        String name;
        switch (index)
        {
            case 1: name="Nick Fury LMD "+counter+" (Summon)"; break;
            case 2: name="AIM Rocket Trooper "+counter+"(Summon)";break;
            case 3: name="AIM Crushbot "+counter+" (Summon)"; break;
            case 4: name="Ultron Drone "+counter+"(Summon)"; break;
            case 5: name="Doombot "+counter+" (Summon)"; break;
            case 6: name="Lesser Demon "+counter+" (Summon)"; break;
            case 7: name="Holographic Decoy "+counter+" (Summon)"; break;
            case 8: name="Ice Golem "+counter+" (Summon)"; break;
            case 9: name="HYDRA Trooper "+counter+" (Summon)"; break;
            case 10: name="Thug "+counter+" (Summon)"; break;
            case 11: name="Mirror Image "+counter+" (Summon)"; break;
            case 12: name="Giganto "+counter+" (Summon)"; break;
            default: name="Error altering Summon name";
        }    
        return name;
    }
    public static int SetSizeSum (int index)
    {
        switch (index)
        {
            case 12: case 13: case 14: case 15: case 16:
            index=2; break;
            default: index=1;
        }
        return index;
    }
    //All of the below are overriden to avoid confusion between summon indices and hero indicies
}