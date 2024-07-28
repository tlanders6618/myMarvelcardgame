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
        //[0][0] is name, [0][1] is proc chance, [0][2] is strength, [0][3] is duration, [0][4] is whether the eff is applied to self or not
        switch (param[0][0]) //param[1] is secondary stat for effs like counter and countdown to apply, e.g. a debuff or ricochet
        {
            case "Afflicted": return new Afflicted (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);  
            case "Aura": return new Aura(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q, param[1]); 
            case "Banish": return new Banish(Integer.valueOf(param[0][1]), Boolean.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); //boolean is true for linked/false for not
            case "Barrier": return new Barrier (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Bleed": return new Bleed (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Bleed Effect": return new BleedE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Blind": return new Blind (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);  
            case "Bulwark": return new Bulwark (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);  
            case "Burn": return new Burn (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);    
            case "Burn Effect": return new BurnE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Countdown": return new Countdown (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q, param[1]); 
            case "Counter": return new Counter (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q, param[1]); 
            case "Counter Effect": return new CounterE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q, param[1]); 
            case "Daze": return new Daze (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);  
            case "Daze Effect": return new DazeE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);  
            case "Debilitate": return new Debilitate (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Disarm": return new Disarm (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Disorient": return new Disorient (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Disrupt": return new Disrupt (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Drain": return new Drain (Integer.valueOf(param[0][1]), Boolean.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); //boolean is true for half/false for full
            case "Empower": return new Empower(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Evade": return new Evade (Integer.valueOf(param[0][1]), Q);
            case "Evade Effect": return new EvadeE (Integer.valueOf(param[0][1]), Q);
            case "Evasion": return new Evasion (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Fear": return new Fear(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Ferocity": return new Ferocity (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Focus": return new Focus (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Focus Effect": return new FocusE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Guard": return new Guard (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Guard Effect": return new GuardE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Intensify": return new Intensify (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Intensify Effect": return new IntensifyE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Invisible": return new Invisible (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Mighty Blows": return new MightyBlows (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Nauseated": return new Nauseated(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Neutralise": return new Neutralise (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Placebo (Buff)": return new PlaceboB(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Placebo (Debuff)": return new PlaceboD(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Poison": return new Poison (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Precision": return new Precision (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Protect": return new Protect (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Provoke": return new Provoke(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Recovery": return new Recovery (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Reflect": return new Reflect (Integer.valueOf(param[0][1]), Boolean.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); //boolean is true for half/false for full
            case "Reflect Effect": return new ReflectE (Integer.valueOf(param[0][1]), Boolean.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Regen": return new Regen (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Regen Effect": return new RegenE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Resistance": return new Resistance (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Resistance Effect": return new ResistanceE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Safeguard": return new Safeguard(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Shatter": return new Shatter (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Shatter Effect": return new ShatterE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Shock": return new Shock (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q);
            case "Snare": return new Snare (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Snare Effect": return new SnareE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Soaked": return new Soaked (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Speed": return new Speed(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q);
            case "Stun": return new Stun (Integer.valueOf(param[0][1]), Q);
            case "Stun Effect": return new StunE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Suppression": return new Suppression(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Target": return new Target (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); 
            case "Target Effect": return new TargetE (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); 
            case "Taunt": return new Taunt (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Terror": return new Terror(Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Tracer": return new Tracer (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Undermine": return new Undermine (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            case "Weakness": return new Weakness (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); 
            case "Wither": return new Wither (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][2]), Integer.valueOf(param[0][3]), Q); 
            case "Wound": return new Wound (Integer.valueOf(param[0][1]), Integer.valueOf(param[0][3]), Q); 
            default: System.out.println("Spelling error in statfactory; no matching stateff found."); return null;
        }
    }
}
class Tracker extends StatEff //displays a character's relevant personal statistics to help players make informed decisions; e.g. showing quicksilver's turns taken
{
    String oname; //original tracker name
    String name; //updated name, displaying stats
    int info; //value of stat being tracked
    public Tracker(String nname)
    {
        name=nname; oname=name; this.id=CardCode.RandomCode();
    }
    public Tracker (String nname, int dur) //for the weaver and kang
    {
        name=nname; oname=name; this.duration=dur; this.id=CardCode.RandomCode();
    }
    @Override
    public String geteffname()
    {
        return name;
    }
    @Override
    public void onApply(Character target)
    {
        switch (oname)
        {
            case "Damage Taken: ": info=target.dmgtaken; name=oname+info+"/180"; break;
            case "Energy: ": case "Control Points: ": case "Rage: ": case "Electrons: ": case "Momentum: ": case "Energy Reserve: ": case "Pain: ":
            info=target.passivecount; name=oname+info; 
            break;
            case "Sand Storm active: ": info=target.passivecount; name=oname+(target.passivecount+" turns"); break;
        }
    }
    @Override
    public String getimmunityname()
    {
        return oname;
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
                name=oname+info+"/180";
            }
            break;
            case "Control Points: ": case "Electrons: ": 
            info=hero.passivecount; name=(oname+info); 
            break;
            case "Momentum: ":
            if (hero.passivecount==5)
            name="Unstoppable!";
            else
            name=(oname+hero.passivecount);
            break;
        }
    }
    @Override
    public void Attacked(Character hero, Character attacker, int dmg) //can ignore both dmg and attacker; no trackers currently use it
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
                name=oname+info+"/180";
            }
            break;
            case "Energy: ": case "Control Points: ": case "Rage: ": case "Energy Reserve: ": case "Pain: ":
            info=hero.passivecount; name=(oname+info); 
            break;
        }
    }
}