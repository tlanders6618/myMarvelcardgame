package myMarvelcardgamepack;


/**
 * Designer: Timothy Landers
 * Date: 11/7/24
 * Filename: Scoreboard2
 * Purpose: To display the states of both teams in a way that adheres to GUI convention (the old scoreboard from 2022 didn't have invokelater, which led to bugs).
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
public class Scoreboard2 extends JSplitPane
{
    //battle calls scoreboard.main(team1, team2) to make gui and updatescore to refresh it
    static final long serialVersionUID = 1L;
    static JFrame frame; //holds everything 
    static JPanel TopPanel; 
    static JPanel BottomPanel;
    static JTable table11; //for displaying info about player 1's first 3 characters    
    static JTable table12; //for player 1's second set of 3 characters
    static JTable table21; 
    static JTable table22;
    static JPanel bholder; static JPanel tholder; //where the tables are held
    static JScrollPane Scroll11; static JScrollPane Scroll12; 
    static JScrollPane Scroll21; static JScrollPane Scroll22; 
    
    public static void main(Character[] team1, Character[] team2) //called by battle to make gui; now utilises the official recommended creation format to avoid problems
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                Scoreboard2.Create(team1, team2);
            }
        }
        );
    }
    public static void Create (Character[] team1, Character[] team2) //make gui frame and add content (scoreboard) to it
    {
        frame = new JFrame("Battle Progress"); 
        frame.add(new Scoreboard2(team1, team2));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
        frame.setMinimumSize(new Dimension(1050,350)); // width then height
        frame.pack();
        frame.setVisible(true);
    }
    public Scoreboard2 (Character[] team1, Character[] team2) 
    {
        //scoreboard2 holds the two panels, which hold the tables; necessary for 4 tables to be shown at once (each table holds 3 heroes)
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        TopPanel = new JPanel(new BorderLayout()); 
        tholder = new JPanel(new GridLayout(1,2)); //holds tables so they can be automatically resized
        TopPanel.add(tholder, BorderLayout.CENTER);
        BottomPanel = new JPanel(new BorderLayout());   
        bholder = new JPanel(new GridLayout(1,2)); //holds tables so they can be automatically resized
        BottomPanel.add(bholder, BorderLayout.CENTER);
        //Get table data
        String row11[][]; String column11[]; //for player 1's heroes
        String row12[][]; String column12[]; 
        String row21[][]; String column21[]; //for player 2's heroes
        String row22[][]; String column22[];
        row11=GetRows(team1, true); column11=GetColumns(team1, true);        
        row21=GetRows(team2, true); column21=GetColumns(team2, true);
        //Make tables
        table11=new JTable(row11,column11);  
        table21=new JTable(row21,column21); 
        table11.getColumnModel().getColumn(0).setMinWidth(90); table11.getColumnModel().getColumn(0).setMaxWidth(90);
        table21.getColumnModel().getColumn(0).setMinWidth(90); table21.getColumnModel().getColumn(0).setMaxWidth(90);
        //Make tables uneditable
        table11.setEnabled(false);
        table21.setEnabled(false);
        table11.getTableHeader().setReorderingAllowed(false); 
        table21.getTableHeader().setReorderingAllowed(false);
        //check for a second set of tables, if needed, for summons
        if (Battle.p1heroes>3)
        {
            row12=GetRows(team1, false);  
            column12=GetColumns(team1, false);
            table12=new JTable(row12, column12);
            table12.getTableHeader().setReorderingAllowed(false);
            table12.getColumnModel().getColumn(0).setMinWidth(90); table12.getColumnModel().getColumn(0).setMaxWidth(90);
            table12.setEnabled(false);
            JScrollPane Scroll12 = new JScrollPane(table12); 
            bholder.add(Scroll12);
            Scroll12.setPreferredSize(new Dimension(600,100));
        }
        if (Battle.p2heroes>3)
        {
            row22=GetRows(team2, false); column22=GetColumns(team2, false);
            table22= new JTable(row22, column22); 
            table22.getTableHeader().setReorderingAllowed(false);
            table22.getColumnModel().getColumn(0).setMinWidth(90); table22.getColumnModel().getColumn(0).setMaxWidth(90);
            table22.setEnabled(false);
            JScrollPane Scroll22 = new JScrollPane(table22); 
            bholder.add(Scroll22);
            Scroll22.setPreferredSize(new Dimension(600,100)); 
        }
        JScrollPane Scroll11 = new JScrollPane(table11); 
        JScrollPane Scroll21 = new JScrollPane(table21);  
        tholder.add(Scroll11);
        tholder.add(Scroll21);
        this.setLeftComponent(TopPanel);
        this.setRightComponent(BottomPanel);        
        Scroll11.setPreferredSize(new Dimension(600,100));      
        Scroll21.setPreferredSize(new Dimension(600,100));         
        this.setDividerSize(0); this.setEnabled(false); //makes the divider invisible and unusable, respectively
        this.setDividerLocation(200);
    }
    public static void UpdateScore (Character[] team1, Character[] team2)
    {
        if (frame!=null&&table11!=null) 
        //needs to be here to avoid exception when update is called before the creation thread is invoked (update is called ~1-3 times before thread finishes) 
        {
            frame.setVisible(false); 
            //get new information
            String[][] newrow11=GetRows(team1, true);
            String[][] newrow21=GetRows(team2, true);         
            String newcolumn11[]=GetColumns(team1, true);     
            String newcolumn21[]=GetColumns(team2, true); 
            //update table to show it
            table11.setModel(new DefaultTableModel (newrow11, newcolumn11));
            table21.setModel(new DefaultTableModel (newrow21, newcolumn21)); 
            //reset first column width
            table11.getColumnModel().getColumn(0).setMinWidth(90); table11.getColumnModel().getColumn(0).setMaxWidth(90);
            table21.getColumnModel().getColumn(0).setMinWidth(90); table21.getColumnModel().getColumn(0).setMaxWidth(90);
            
            //team 1
            if (Battle.p1heroes>3)
            {
                String[][] newrow12=GetRows(team1, false);
                String[] newcolumn12=GetColumns(team1, false);
                if (table12==null) //make it since it wasn't made at the start of the fight
                {
                    table12=new JTable(newrow12, newcolumn12);
                    table12.getTableHeader().setReorderingAllowed(false);
                    table12.setEnabled(false);
                    JScrollPane Scroll12 = new JScrollPane(table12);
                    bholder.add(Scroll12);
                    Scroll12.setPreferredSize(new Dimension(600,100));               
                }
                else
                {
                    table12.setModel(new DefaultTableModel (newrow12, newcolumn12)); //update the table
                }
                table12.getColumnModel().getColumn(0).setMinWidth(90); table12.getColumnModel().getColumn(0).setMaxWidth(90);
            }
            else if (table12!=null) //get rid of the table because the extra heroes have died; if null, it doesn't exist yet so it doesn't need to be wiped
            {
                table12.setModel(new DefaultTableModel());
            }
            
            //team 2
            if (Battle.p2heroes>3)
            {
                String[][] newrow22=GetRows(team2, false);
                String[] newcolumn22=GetColumns(team2, false);
                if (table22==null)
                {
                    table22=new JTable (newrow22, newcolumn22);                
                    table22.getTableHeader().setReorderingAllowed(false);
                    table22.setEnabled(false); 
                    JScrollPane Scroll22 = new JScrollPane(table22); 
                    bholder.add(Scroll22);  
                    Scroll22.setPreferredSize(new Dimension(600,100));
                }
                else
                {
                    table22.setModel(new DefaultTableModel (newrow22, newcolumn22));
                }
                table22.getColumnModel().getColumn(0).setMinWidth(90); table22.getColumnModel().getColumn(0).setMaxWidth(90);
            }
            else if (table22!=null)
            {
                table22.setModel(new DefaultTableModel());
            }
            frame.setVisible(true); 
        }
    }
    public static String[][] GetRows (Character[] champs, boolean halftocheck) //check first 3 team members or last 3, since it's 3 per table
    {
        //first row is team, second is health, third for shield, everything else is stateff
        //format is row then column
        int max=1; //minimum of one so the status effects label has space to be displayed
        ArrayList <String> manywords= new ArrayList<String>(); int length=champs.length;
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null) //since the stateff display has been condensed (see bottom of this method), the row count is no longer equivalent to the biggest effects.size
                {
                    ArrayList<String> unique= new ArrayList<String>(); //to avoid creating too many extra rows, now rows are counted based on unique stateffs
                    for (StatEff e: champs[i].effects) //this is bc ones with the same name are now combined to become (#) effname, and only take up a single row now
                    {
                        if (!(unique.contains(e.geteffname())))
                        {
                            unique.add(e.geteffname());
                        }
                    }
                    if (unique.size()>max)
                    max=unique.size(); //no more rows than there are unique status effects
                }
            } 
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null) 
                {
                    ArrayList<String> unique= new ArrayList<String>();
                    for (StatEff e: champs[i].effects) 
                    {
                        if (!(unique.contains(e.geteffname())))
                        {
                            unique.add(e.geteffname());
                        }
                    }
                    if (unique.size()>max)
                    max=unique.size(); 
                }
            } 
        }
        max+=3; //to make space for the team and shield and health labels
        String[][] labels=new String[max][length+1]; //+1 since there's an extra column listing what each row is for
        int counter=1; 
        labels[0][0]="Team"; labels[1][0]="Health"; labels[2][0]="Shield"; labels[3][0]="Status effects";
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null&&champs[i].team1==true)
                {
                    labels[0][counter]="Team 1"; 
                }
                else if (champs[i]!=null&&champs[i].team1==false)
                {
                    labels[0][counter]="Team 2";
                }
                ++counter; 
            }
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null&&champs[i].team1==true)
                {
                    labels[0][counter]="Team 1"; 
                }
                else if (champs[i]!=null&&champs[i].team1==false)
                {
                    labels[0][counter]="Team 2";
                }
                ++counter; 
            }
        }
        counter=1;
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null)
                {
                    labels[1][counter]=champs[i].GetHP(champs[i]);
                    ++counter;
                }
            }
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null)
                {
                    labels[1][counter]=champs[i].GetHP(champs[i]);
                    ++counter;
                }
            }
        }
        counter=1; 
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null)
                {
                    labels[2][counter]=champs[i].GetShield(champs[i]);
                    ++counter;
                }
            }
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null)
                {
                    labels[2][counter]=champs[i].GetShield(champs[i]);
                    ++counter;
                }
            }
        }
        counter=1; int statcount=3;
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null)
                {
                    ArrayList<String> completed= new ArrayList<String>();
                    for (StatEff eff: champs[i].effects) 
                    {
                        String name=eff.geteffname(); int num=1; //scoreboard will no longer simply copy all of a hero's stateffs to be displayed solely bc of Roblin
                        for (StatEff eff2: champs[i].effects) //condenses stateffs so instead of listing three diferent Bleed: 5's, (3) Bleed: 5 will be shown instead
                        {
                            if (eff2!=eff&&eff2.geteffname().equals(name)) //this will improve readability and reduce clutter
                            {
                                ++num; //tracks how many identical stateffs there are, not counting the original eff
                            }
                        }
                        if (!(completed.contains(name))) //if (3) Bleed: 5 is already displayed, don't show anymore Bleed: 5's because they're already accounted for
                        {
                            completed.add(name); //add the original effname so no duplicates are displayed, as noted above
                            if (num>1) //turn Bleed: 5 into (3) Bleed: 5
                            name="("+num+") "+name;
                            labels[statcount][counter]=name; //display the (3) Bleed: 5
                            ++statcount;
                        }
                    }
                }
                statcount=3; ++counter;
            }
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null)
                {
                    ArrayList<String> completed= new ArrayList<String>();
                    for (StatEff eff: champs[i].effects) 
                    {
                        String name=eff.geteffname(); int num=1; 
                        for (StatEff eff2: champs[i].effects) 
                        {
                            if (eff2!=eff&&eff2.geteffname().equals(name)) 
                            {
                                ++num; 
                            }
                        }
                        if (!(completed.contains(name))) 
                        {
                            completed.add(name); 
                            if (num>1)
                            name="("+num+") "+name;
                            labels[statcount][counter]=name; 
                            ++statcount;
                        }
                    }
                }
                statcount=3; ++counter;
            }
        }
        return labels;
    }
    public static String[] GetColumns (Character[] champs, boolean halftocheck)
    {
        //column titles are just the heroes' names
        ArrayList <String> words= new ArrayList<String>(); int index=1; //the first row is the description
        words.add("Name");
        if (halftocheck==true)
        {
            for (int i=0; i<3; i++)
            {
                if (champs[i]!=null)
                {
                    String name=champs[i].Cname;
                    words.add(name);
                    ++index;
                }            
            }
        }
        else
        {
            for (int i=3; i<6; i++)
            {
                if (champs[i]!=null)
                {
                    String name=champs[i].Cname;
                    words.add(name);
                    ++index;
                }            
            }
        }
        String[] Titles= new String[index]; int counter=0;
        for (String str: words) //convert arraylist to array
        {
            Titles[counter]=str;
            ++counter;
        }
        return Titles;
    }    
}
