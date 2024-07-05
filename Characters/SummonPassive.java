package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 24/11/22
 * Filename: SummonPassive
 * Purpose: Contains all of the passives for Summons.
 */
import java.util.ArrayList;
public class SummonPassive
{
    public static void Giganto (Character og, String duty)
    {
        if (duty.equals("spawn")) //onsummon
        {
            if (og.passivefriend[0].index==84) //summoned by namor
            {
                og.passivefriend[0].passivefriend[0]=og; //so namor's "trident of neptune" works properly
            }
        }
        else if (duty.equals("attack")) //onattack
        {
            String[] sonk={"Stun Effect", "500", "616", "1", "true"}; String[][] money=StatFactory.MakeParam(sonk, null); og.activeability.AddTempString(money); 
            String[] sank={"Target Effect", "500", "40", "1", "true"}; String[][] lmoney=StatFactory.MakeParam(sank, null); og.activeability.AddTempString(lmoney); 
        }
        else if (duty.equals("gain")&&og.passivecount==0) //add
        {
            og.ADR-=20; og.passivecount=1;
        }
        else if (duty.equals("lose")&&og.passivecount==1) //remove
        {
            og.ADR+=20; og.passivecount=0;
        }
    }
    public static void Decoy (Character decoy) //onsummon
    {
        decoy.add(new Taunt(500, 616, decoy)); decoy.binaries.add("Stunned");
    }
    public static int Daemon (Character matt, boolean start, Character attacker, int dmg)
    {
        if (start==true) //onsummon
        {
            boolean yes=CoinFlip.Flip(50); 
            if (yes==true)
            {
                Provoke g= new Provoke(500, 1, matt);
                Character jill=Ability.GetRandomHero(matt, attacker, false, false);
                StatEff.CheckApply(matt, jill, g);
            }
            else
            System.out.println(matt.Cname+"'s Provoke failed to apply due to chance.");
        }
        else //takedamage
        {
            if (attacker.CheckFor("Provoke", false)==true)
            {
                dmg-=10; 
            }
        }
        return dmg;
    }
    public static void LilDoomie (Character lil, Character ally) //allydeath
    {
        if (ally.summoned==false&&ally.index==28&&lil.dead==false) //only triggers if doom dies; it doesn't matter who summoned lil doomie
        {
            System.out.println("CONNECTION LOST. SELF TERMINATION IN 3...2...1..."); lil.onDeath(null, "self");
        }
    }
    public static void Drone (Character husk, StatEff effecter) //add
    {
        if (!(husk.binaries.contains("Stunned"))&&husk.passivefriend[0]!=null) 
        {
            Character target=husk.passivefriend[0]; //aka ultron
            String name=effecter.getimmunityname(); int dur=effecter.oduration; int pow=effecter.power;
            String[] morb={name, "500", Integer.toString(pow), Integer.toString(dur), "false"}; String[][] morbintime=StatFactory.MakeParam(morb, null);
            StatEff e=StatFactory.MakeStat(morbintime, target);  
            StatEff.CheckApply(husk, target, e);
        }
    }
    public static void Crushbot (Character bot)
    {
        if (!(bot.binaries.contains("Stunned"))) //onturn
        {
            System.out.println("\nSEARCHING...CRUSHBOT TARGET ACQUIRED");
            ArrayList<Character> low=Battle.ChooseTarget(bot, "enemy", "lowest");
            Character law=low.get(0);
            Target enron= new Target(100, 5, 1, bot);
            boolean yes=CoinFlip.Flip(100+bot.Cchance);
            if (yes==true)
            StatEff.CheckApply(bot, law, enron);
            else
            StatEff.applyfail(law, enron, "chance");
        }
    }
    public static void NickLMD (Summon lmd) //onsummon
    {
        if (lmd.passivefriend[0]!=null&&lmd.passivefriend[0].dead==false)
        {
            StatEff prot= new ProtectE (500, 616, lmd);
            prot.PrepareProtect(lmd, lmd.passivefriend[0]);
            StatEff.CheckApply(lmd, lmd, prot);
        }
    }
}