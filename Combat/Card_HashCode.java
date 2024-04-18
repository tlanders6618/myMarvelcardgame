package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 8/8/22
 * Filename: Card_HashCode
 * Purpose: Gives objects unique identifier numbers.
 */
public class Card_HashCode
{
    static int counter=1;
    public static int RandomCode()
    {
        int code=counter; 
        ++counter; //to ensure every number is different
        return code;
    }
    public static boolean CheckSkip (Character hero) //heroes can only skip their turn if they have no usable abilities
    {
        boolean usable=true; int use=0;
        for (Ability a: hero.abilities)
        {
            if (a!=null&&a.CheckUse(hero)==true)
            {
                if (a.friendly.equalsIgnoreCase("ally exclusive")||a.friendly.equalsIgnoreCase("both")) //requires an ally to target
                {
                    Character[] list=Battle.GetTeammates(hero);
                    for (Character c: list)
                    {
                        if (c!=null) //only considered usable if the hero still has allies they can use the ab on
                        {
                            ++use; break;
                        }
                    }
                }
                else //ability targets self or an enemy
                ++use;
            }
        }
        if (use>0)
        return false;
        else 
        return true;
    }
    public static void GetDesc (Character hero) //print desc of chosen ability of hero
    {
        System.out.print("\n");
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (hero.abilities[i]!=null)
            {
                System.out.println (a+": "+hero.abilities[i].GetAbName(hero));  
            }
        }
        int choice; //player chooses which ab they want desc of
        do
        {
            choice=Damage_Stuff.GetInput(); 
            --choice; //to get the index number since the number entered was the ability number
        }
        while (!(choice<5&&choice>=0&&hero.abilities[choice]!=null));
        hero.abilities[choice].PrintDesc(false);
        System.out.print("\n");
        //print this stuff again since chooseab's loop takes still input but doesn't show options again
        if (hero.team1==true)
        System.out.println ("\nPlayer 1, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        else
        System.out.println ("\nPlayer 2, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        boolean skip=Card_HashCode.CheckSkip(hero); //allows heroes with no usable abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (hero.abilities[i]!=null)
            {
                System.out.println (a+": "+hero.abilities[i].GetAbName(hero));  
            }
        }
        System.out.println("6: Check an ability's description");
        System.out.println("7: Check the description of this character's passive(s)");
        if (skip==true)
        {
            System.out.println ("0: Skip turn");
        }
    }
    public static void GetPDesc (Character hero) //print desc of passive of hero
    {
        System.out.print("\n");
        System.out.print(hero.pdesc);
        System.out.print("\n");
        //print this stuff again since chooseab's loop takes still input but doesn't show options again
        if (hero.team1==true)
        System.out.println ("\nPlayer 1, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        else
        System.out.println ("\nPlayer 2, choose an ability for "+hero.Cname+" to use. Type its number, not its name.");
        boolean skip=Card_HashCode.CheckSkip(hero); //allows heroes with no usable abilities to skip their turn
        for (int i=0; i<5; i++)
        {
            int a=i+1;
            if (hero.abilities[i]!=null)
            {
                System.out.println (a+": "+hero.abilities[i].GetAbName(hero));  
            }
        }
        System.out.println("6: Check an ability's description");
        System.out.println("7: Check the description of this character's passive(s)");
        if (skip==true)
        {
            System.out.println ("0: Skip turn");
        }
    }
    public static void RandomStat (Character hero, Character target, String kind) 
    {
        String[][] ps=null;
        switch (kind)
        {
            case "Ultron": int choice=1+(int)(Math.random() * ((3 - 1) + 1));
            if (choice==1) //bulwark
            {
                String[] porridge={"Bulwark", "500", "616", "2", "true"}; ps=StatFactory.MakeParam(porridge, null);
            }
            else if (choice==2) //placebo
            {
                String[] porridge={"Placebo (Buff)", "500", "616", "2", "true"}; ps=StatFactory.MakeParam(porridge, null);
            }
            else if (choice==3) //focus
            {
                String[] porridge={"Focus", "500", "616", "2", "true"}; ps=StatFactory.MakeParam(porridge, null);
            }
            break;
            case "disable debuffs": int choose=1+(int)(Math.random() * ((3 - 1) + 1));
            if (choose==1) 
            {
                String[] porridge={"Afflicted", "100", "616", "1", "false"}; ps=StatFactory.MakeParam(porridge, null);
            }
            else if (choose==2) 
            {
                String[] porridge={"Neutralise", "100", "616", "1", "false"}; ps=StatFactory.MakeParam(porridge, null);
            }
            else if (choose==3) 
            {
                String[] porridge={"Undermine", "100", "616", "1", "false"}; ps=StatFactory.MakeParam(porridge, null);
            }
            break;
        }
        if (hero.activeability!=null&&(Battle.team1[Battle.P1active]==hero||Battle.team2[Battle.P2active]==hero)) //to prevent bugs with assist
        hero.activeability.AddTempString(ps); 
        else
        {
            if (ps[0][4].equals("true")||hero==target) //don't forget to check application chance first since checkapply doesn't
            {
                StatEff e=StatFactory.MakeStat(ps, hero); StatEff.CheckApply(hero, hero, e); 
            }
            else 
            {
                StatEff e=StatFactory.MakeStat(ps, hero); StatEff.CheckApply(hero, target, e);
            }
        }
    }
}