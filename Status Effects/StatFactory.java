package myMarvelcardgamepack;
/**
 * Designer: Timothy Landers
 * Date: 4/9/22
 * Filename: StatFactory
 * Purpose: To generate new instances of status effects.
 */
public class StatFactory
{
    //if I don't make a new instance of a status effect when I want to apply it, the program shares the same instance between everyone who has it, 
    //causing issues like negative duration; this prevents that
    public static String[] SetParam (String one, String two, String three, String four, String five)
    {
        String[] y= new String[5];
        y[0]=one;
        y[1]=two;
        y[2]=three;
        y[3]=four;
        y[4]=five;
        return y;
    }
    public static StatEff MakeStat (String[] param)
    {
        StatEff[] eff=new StatEff[1];
        switch (param[0])
        {
            case "Bleed": eff[0]=new Bleed (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Burn": eff[0]=new Burn (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;            
            case "Chance Down": eff[0]=new ChanceDown (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Chance Up": eff[0]=new ChanceUp (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Countdown": eff[0]=new Countdown (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Damage Up": eff[0]=new DamageUp (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Disrupt": eff[0]=new Disrupt (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Protect": eff[0]=new Protect (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Recovery": eff[0]= new Recovery (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Resistance": eff[0]=new Resistance (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Stun": eff[0]=new Stun (Integer.valueOf(param[1])); break;
            case "Target": eff[0]=new Target (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Taunt": eff[0]=new Taunt (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Weakness": eff[0]=new Weakness (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Wound": eff[0]=new Wound (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            default: System.out.println("Spelling error when making stateffs");
        }
        return eff[0];
    }
}
