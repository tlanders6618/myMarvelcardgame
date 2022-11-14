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
    public static String[] SetParam (String one, String two, String three, String four, String five, String six)
    {
        String[] y= new String[6];
        y[0]=one;
        y[1]=two;
        y[2]=three;
        y[3]=four;
        y[4]=five;
        y[5]=six;
        return y;
    }
    public static StatEff MakeStat (String[] param)
    {
        StatEff eff=null;
        switch (param[0])
        {
            case "Bleed": eff=new Bleed (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Burn": eff=new Burn (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;            
            case "Chance Down": eff=new ChanceDown (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Chance Up": eff=new ChanceUp (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Countdown": eff=new Countdown (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3]), param[5]); break;
            case "Damage Up": eff=new DamageUp (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Disrupt": eff=new Disrupt (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Invisible": eff= new Invisible (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Protect": eff=new Protect (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Recovery": eff= new Recovery (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Resistance": eff=new Resistance (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            //case "Speed": eff=new Speed(Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Stun": eff=new Stun (Integer.valueOf(param[1])); break;
            case "Target": eff=new Target (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Taunt": eff=new Taunt (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            case "Weakness": eff=new Weakness (Integer.valueOf(param[1]), Integer.valueOf(param[2]), Integer.valueOf(param[3])); break;
            case "Wound": eff=new Wound (Integer.valueOf(param[1]), Integer.valueOf(param[2])); break;
            default: System.out.println("Spelling error when making stateffs");
        }
        return eff;
    }
}