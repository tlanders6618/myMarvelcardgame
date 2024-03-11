package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 4/9/22
 * Filename: StatFactory
 * Purpose: To generate new status effects; also contains scoreboard tracker effs.
 */
public class StatFactory
{
    //if I don't make a new instance of a status effect when I want to apply it, the program shares the same instance between everyone who has it, 
    //causing issues like negative duration; this prevents that by making a new stateff object eah ab use
    public static String[][] MakeParam (String[] eff, String[] eff2) //turns two String[]s into one String[][] so it can become a StatEff later
    {
        String[][] arr= new String[2][6];
        for (int i=0; i<eff.length; i++) //row 0 is for eff stats (e.g. counter) and row 1 is for stats of secondary eff (e.g. shock applied on counter)
        {
            arr[0][i]=eff[i];
        }
        if (eff2!=null)
        {
            for (int i=0; i<eff2.length; i++) 
            {
                arr[1][i]=eff2[i];
            }
        }
        else
        arr[1]=null;
        return arr;
    }
    public static StatEff MakeStat (String[][] param, Character Q)
    {
        StatEff eff=null;
        //[0][0] is name, [0][1] is proc chance, [0][2] is strength, [0][3] is duration, [0][4] is whether the eff is applied to self or not
        switch (param[0][0]) //param[1] is secondary stat for effs like counter and countdown to apply, e.g. a debuff or ricochet
        {
            case "Bleed": eff=new Bleed (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Burn": eff=new Burn (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;    
            case "Countdown": eff=new Countdown (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), param[1]); break;
            case "Counter": eff=new Counter (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), param[1]); break; 
            case "Daze": eff=new Daze (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;  
            case "Debilitate": eff= new Debilitate (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Disarm": eff=new Disarm (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Disrupt": eff=new Disrupt (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Drain": eff=new Drain (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Evade": eff= new Evade (Integer.valueOf(param[0][1])); break;
            case "EvadeE": eff= new EvadeE (Integer.valueOf(param[0][1])); break;
            case "Evasion": eff= new Evasion (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Focus": eff=new Focus (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "FocusE": eff=new FocusE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Intensify": eff=new Intensify (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "IntensifyE": eff=new IntensifyE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Invisible": eff= new Invisible (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Mighty Blows": eff= new MightyBlows (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Neutralise": eff= new Neutralise (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Poison": eff=new Poison (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Precision": eff=new Precision (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Protect": eff=new Protect (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Provoke": eff =new Provoke(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); break;
            case "Recovery": eff= new Recovery (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Regen": eff=new Regen (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Resistance": eff=new Resistance (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "ResistanceE": eff=new ResistanceE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Shatter": eff= new Shatter (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Shock": eff= new Shock (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Snare": eff= new Snare (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Speed": eff=new Speed(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Stun": eff=new Stun (Integer.valueOf(param[0][1])); break;
            case "Target": eff=new Target (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Taunt": eff=new Taunt (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Terror": eff=new Terror(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); break;
            case "Tracer": eff= new Tracer (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Undermine": eff= new Undermine (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            case "Weakness": eff=new Weakness (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3])); break;
            case "Wound": eff=new Wound (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3])); break;
            default: System.out.println("Spelling error in statfactory; no matching stateff found.");
        }
        return eff;
    }
}
class Tracker extends StatEff //displays a character's relevant personal statistics to help players make informed decisions; e.g. showing quicksilver's turns taken
{
    String oname; //original tracker name
    String name; //updated name, displaying stats
    int info; //value of stat being tracked
    public Tracker(String nname)
    {
        name=nname; oname=name;
    }
    public Tracker (String nname, int dur) //for the weaver and kang
    {
        name=nname; oname=name; this.duration=dur;
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public void Nullified(Character target)
    {
    }
    @Override
    public void onApply(Character target)
    {
        switch (oname)
        {
            case "Damage Taken: ": info=target.dmgtaken; name+=info; break;
            case "Energy: ": info=target.passivecount; name+=info; break;
        }
    }
    @Override
    public String getimmunityname()
    {
        return "None";
    }
    @Override
    public String getefftype()
    {
        return "Secret";
    }
    @Override
    public void onTurnEnd(Character hero)
    {
        switch (oname)
        {
            case "Damage Taken: ": info=hero.dmgtaken;
            if (info>=180)
            {
                name="Berserker Frenzy active";
                oname=name;
            }
            else
            {
                name=(oname+info);
            }
            break;
        }
    }
    @Override
    public void Attacked(Character hero, Character attacker, int dmg)
    {
        switch (oname)
        {
            case "Damage Taken: ": info=hero.dmgtaken;
            if (info>=180)
            {
                name="Berserker Frenzy active";
                oname=name;
            }
            else
            {
                name=(oname+info);
            }
            break;
            case "Energy: ": info=hero.passivecount; name=(oname+info); break;
        }
    }
}