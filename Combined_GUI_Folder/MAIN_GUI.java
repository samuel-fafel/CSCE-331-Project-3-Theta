import java.sql.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.text.SimpleDateFormat;



/* INSTRUCTIONS
  Compile:
    javac -source 11 -target 11 *.java
  REGENERATE combined_gui.jar FOR AjaxSwing:
    jar cfm combined_gui.jar META-INF/MANIFEST.MF postgresql-42.2.8.jar *.class

  Run Manually Windows:
    java -cp ".;postgresql-42.2.8.jar" MAIN_GUI
  Run Manually Mac/Linux:
    java -cp ".:postgresql-42.2.8.jar" MAIN_GUI
*/

/**
  *This class contains everything needed for the main GUI which will be used to navigate to other interfaces.
  *<p>
  *It houses all the methods and classes that will used in MAIN_GUI(),
  *it also contains MAIN_GUI() itself that is the acutal interface that will be executed,
  *and it holds main that will execute MAIN_GUI()
  *
  * @author Nicholas Nguyen
  * @author Samuel Fafel
  * @author Namson Pham
  */

public class MAIN_GUI extends JFrame {

  private static String user;
  private static Boolean manager_auth;

  public static void set_user(String user_input) {
    user = user_input;
  }
  public static String get_user() {
    return user;
  }

  public static void set_auth(String role) {
    if (role.equalsIgnoreCase("Manager")) {
      manager_auth = true;
    } else {
      manager_auth = false;
    }
  }
  public static Boolean get_auth() {
    return manager_auth;
  }

  /**
  *Gives constraint dimensions to a layout constraint varaible.
  *<p>
  *This method is constantly used in ordering and placing different labels and buttons
  *on a grid with the passed on parameters.
  *
  * @param c The constraint varaible for which we will assign dimensions to
  * @param gridx The x-axis of where we want our item to be placed
  * @param gridy The y-axis of where we want our item to be placed
  * @param gridwidth The width of how many grid spaced you want the item to take up
  */
  public static GridBagConstraints constraints(GridBagConstraints c,int gridx, int gridy, int gridwidth) {
    c.gridx = gridx;
    c.gridy = gridy;
    c.gridwidth = gridwidth;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    return c;
  }

  /**
  *Manipulates the physical feature of a Java button.
  *<p>
  *This method is constantly used in assigning buttons their look on the front end
  *and it convenient keeps all the buttons in use with a uniform look.
  *
  * @param inputButton The button that will be assigned the different attributes
  */
  public static void buttonsettings(JButton inputButton) {
    inputButton.setFont(new Font("Verdana",Font.BOLD,24));
    inputButton.setPreferredSize(new Dimension(400,100));
    inputButton.setHorizontalAlignment(JButton.CENTER);
    inputButton.setVerticalAlignment(JButton.CENTER);
    inputButton.setBackground(Color.white);
    inputButton.setOpaque(true);
    inputButton.setBorder(BorderFactory.createRaisedBevelBorder());
  }

  /**
  *Manipulates the physical feature of a Java panal.
  *<p>
  *This method is constantly used in assigning panel their look on the front end
  *and it allows different size manipulation of the different panels.
  *
  * @param my_panel The panel that will be manipulated
  * @param color Desired color of the panel
  * @param border The border style of the panel
  * @param bound1x The x dimension of where the panel will be placed on the frame
  * @param bound1y The y dimension of where the panel will be placed on the frame
  * @param bound2x The x dimension of the panel size
  * @param bound2y The y dimension of the panel size
  */
  public static void adjust_panel(JPanel my_panel, Color color, Border border, int bound1x, int bound1y, int bound2x, int bound2y) {
    my_panel.setBackground(color);
    //my_panel.setBorder(border);
    my_panel.setBounds(bound1x,bound1y,bound2x,bound2y);
  }

  public static void login_frame_settings(JFrame frame) {
    frame.setTitle("Login");
    frame.setSize(600, 600);
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
  }

  public class RoundedCornerPanel extends JPanel {
    private int cornerRadius;

    public RoundedCornerPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(Color.BLACK); // change the color of the border
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        g2.dispose();
    }
}

  public static void run_command(String sql_query) {
    //Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      //System.exit(0);
    } //JOptionPane.showMessageDialog(null,"Opened database successfully");

    // run command
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = sql_query;  // create an SQL statement
      stmt.executeQuery(query); // send statement to DBMS
    } catch (Exception e){
      if (!e.toString().contains("No results were returned by the query.")) {
        JOptionPane.showMessageDialog(null,"error when trying " + sql_query + "\n" + e);
      }
    }
  }

  /***
   * Generates a string to be the content of a Z report.
   * Goes through all the transactions of the current day to update running totals, records them, then zeroes them out.
   * @return report_string
   * @author Haden Johnson
   */
  public static String generateZReport(){
    // Connect to Database
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      //System.exit(0);
    }

    // Get current date & time
    java.util.Date now = new java.util.Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String current_time = sdf.format(now);
    String current_date = "";
    if(month < 10){
       current_date += "0";
    }
    current_date += month + "/";
    if(day < 10){
      current_date += "0";
   }
    current_date += day + "/" + year;

    // Initialize variables
    // String to return
    String report_string = "<html>Z Report for " + current_date + "<br>";
    // Sales totals
    float total = 0;
    // Temp string for item names
    String item = "";

    // Try database operations
    try{
        //extract menu totals
        Statement stmt = conn.createStatement(); // create a statement object
        ResultSet DB_menu = stmt.executeQuery("SELECT name, running_total FROM menu_full");
        HashMap<String,Integer> totals_map = new HashMap<String,Integer>();
        while(DB_menu.next()){
          totals_map.put(DB_menu.getString("name"), DB_menu.getInt("running_total") );
        }

        // Get transactions from database
        String query = "SELECT * FROM transactions WHERE date='" + current_date + "'";  // create an SQL query
        ResultSet result = stmt.executeQuery(query); // send statement to DBMS
        while (result.next()) {
          //update running totals
          //for (meal_size, entrees 1-3, sides 1-2, drink), add 1 to the item's running total if it appears
          for(int i = 3; i <=9; i++){
            item = result.getString(i);
            try{
              totals_map.put(item,totals_map.get(item) + 1);
            }catch (Exception e){
              //Happens when entry IS NULL
              //System.out.println(e.toString());
            }
          }

          // sum subtotals
          total += result.getFloat("subtotal");
        }

        // add every entry to the report
        for(Map.Entry<String, Integer> duo : totals_map.entrySet() ){
          report_string += duo.getKey() + ": " + duo.getValue() + "<br>";
        }

    } catch (Exception e) {
        JLabel error = new JLabel("<html>Error accessing Database for Z Report:<br>" + e + "</html>");
        error.setFont(new Font("Verdana",Font.PLAIN,16));
        JOptionPane.showMessageDialog(null, error);
    }

    report_string += "Total Sales: $" + Float.toString(total) + "<br></html>";

    //update z report log & zero running totals
    run_command( "INSERT INTO z_reports(date,time) VALUES('" + current_date + "','" + current_time + "')" );
    run_command( "UPDATE menu_full SET running_total=0" );

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }

    return report_string;
  }

  static JFrame f;
  /**
  *The contructor that is what makes our unique main GUI.
  *<p>
  *This will put all the methods together with some added functionality
  *to see the main GUI on the screen. Which will be able to branch to the
  *other GUIs.
  *
  */
  public MAIN_GUI() {
    //Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      JOptionPane.showMessageDialog(null, e.getClass().getName()+": "+e.getMessage());
      //System.exit(0);
    }

    // create a new frame
    f = new JFrame("Employee Home & Login");
    GridBagConstraints c = new GridBagConstraints();

    //Pannel Initiliztion
    RoundedCornerPanel top_panel = new RoundedCornerPanel(20);
    top_panel.setLayout(new GridBagLayout());
    RoundedCornerPanel middle_panel = new RoundedCornerPanel(20);
    middle_panel.setLayout(new GridBagLayout());
    JPanel temp_panel = new JPanel(new GridBagLayout());

    Border blackline;
    Border raisedbevel;
    Border loweredbevel;
    Border empty;

    JLabel top_label = new JLabel("Employee Home & Login");

    blackline = BorderFactory.createLineBorder(Color.black);
    raisedbevel = BorderFactory.createRaisedBevelBorder();
    loweredbevel = BorderFactory.createLoweredBevelBorder();
    empty = BorderFactory.createEmptyBorder();

    // Make Close Button & add action listener
    JButton Z_button = new JButton("Generate Z Report");
    JButton cashier_button = new JButton("Cashier Interface");
    JButton product_button = new JButton("Product Manipulation");
    JButton transactions_button = new JButton("Transactions & Reports");
    JButton login_main_button = new JButton("Login");
    for (int i = 0; i < 1; i++) {
      Connection temp_conn = conn;
      Z_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JLabel z_report_text = new JLabel(generateZReport());
          z_report_text.setFont(new Font("Verdana",Font.PLAIN,16));
          JOptionPane.showMessageDialog(null, z_report_text);
        }
      });
    // Login Button
      login_main_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // create a new frame
          JFrame login_frame = new JFrame();
          login_frame_settings(login_frame);
          JPanel login_panel = new JPanel(new GridLayout(3, 2));
          JLabel user_label, pin_label;
          JTextField user_field, pin_field;
          JButton login_button = new JButton("Login");

          // Username
          user_label = new JLabel("Username: ");
          user_field = new JTextField("", 15);
          pin_label = new JLabel("PIN: ");
          pin_field = new JTextField("", 15);

          // Set font for all components
          Font font_BOLD = new Font("Verdana", Font.BOLD, 24);
          Font font_PLAIN = new Font("Verdana", Font.PLAIN ,24);
          user_label.setFont(font_BOLD);
          user_field.setFont(font_PLAIN);
          pin_label.setFont(font_BOLD);
          pin_field.setFont(font_PLAIN);

          // Center text both vertically and horizontally
          user_label.setHorizontalAlignment(SwingConstants.RIGHT);
          pin_label.setHorizontalAlignment(SwingConstants.RIGHT);

          // Add all to panel
          login_panel.add(user_label, constraints(c,0,0,1));
          login_panel.add(user_field, constraints(c,1,0,1));
          login_panel.add(pin_label, constraints(c,0,1,1));
          login_panel.add(pin_field, constraints(c,1,1,1));
          login_button.setFont(new Font("Verdana",Font.BOLD,24));
          login_panel.add(login_button, constraints(c,0,2,2));

          login_frame.add(login_panel, BorderLayout.CENTER);
          login_frame.getRootPane().setDefaultButton(login_button);
          login_frame.pack();
          login_frame.setVisible(true);

          login_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              String user = user_field.getText();
              String pin = pin_field.getText();
              int pin_value = Integer.parseInt(pin);
              Connection conn = null;
              try {
              Class.forName("org.postgresql.Driver");
              conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
                "csce315331_theta_master","3NHS");
              String sql = "SELECT * FROM employees WHERE name = ? and pin = ?";
              PreparedStatement statement = conn.prepareStatement(sql);
              statement.setString(1, user);
              statement.setInt(2, pin_value);
              ResultSet rs = statement.executeQuery();
              if (rs.next()) {
                String role = rs.getString("position");
                role = role.replaceAll("\\s", "");
                login_frame.setVisible(false);
                set_user(user);
                set_auth(role);
                System.out.println(role);
                System.out.println(get_auth());
                JLabel welcome = new JLabel("Welcome " + get_user() + "!");
                welcome.setFont(new Font("Verdana",Font.BOLD,24));
                JOptionPane.showMessageDialog(null, welcome);
                f.setVisible(true);
              } else {
                JLabel unwelcome = new JLabel("Login Attempt Failed");
                unwelcome.setFont(new Font("Verdana",Font.BOLD,24));
                JOptionPane.showMessageDialog(null, unwelcome);
              }
              } catch (Exception e) {
              e.printStackTrace();
              System.err.println(e.getClass().getName()+": "+e.getMessage());
              JOptionPane.showMessageDialog(null, e.getClass().getName()+": "+e.getMessage());
              //System.exit(0);
              }
            }
        });
        login_frame.setVisible(true);
        }

      });
      cashier_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (get_user() != null) {
            try {
              System.out.println(get_user());
              Cashier_GUI s = new Cashier_GUI(get_user());
            } catch(Exception a) {
              JOptionPane.showMessageDialog(null,"Error opening Cashier GUI: " + a);
            }
          } else {
            JOptionPane.showMessageDialog(null, "Please login first!");
          }
        }
      });
      product_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (get_auth()) {
            try {
              Product_GUI s = new Product_GUI();
            } catch(Exception a) {
              JOptionPane.showMessageDialog(null,"Error opening Product GUI: " + a);
            }
          } else {
            JOptionPane.showMessageDialog(null, "You do not have authorization access to view!");
          }
        }
      });
      transactions_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (get_auth()) {
            try {
              Transactions_GUI s = new Transactions_GUI();
            } catch(Exception a) {
              JOptionPane.showMessageDialog(null,"Error opening Transactions GUI: " + a);
            }
          } else {
            JOptionPane.showMessageDialog(null, "You dod not have authorization access to view!");
          }
        }
      });
    }
    Color lightRed = new Color(252, 217, 217);

    adjust_panel(top_panel,lightRed,loweredbevel,20,20,1340,100);

    top_label.setFont(new Font("Verdana",1,30));
    top_panel.add(top_label);
    buttonsettings(Z_button);
    buttonsettings(cashier_button);
    buttonsettings(product_button);
    buttonsettings(transactions_button);
    buttonsettings(login_main_button);

    adjust_panel(middle_panel,lightRed,loweredbevel,20,140,1340,700);
    constraints(c,0,2,3);
    c.insets =  new Insets(25, 0, 0, 0);
    middle_panel.add(Z_button,c);

    constraints(c, 0, 1, 3);
    //transactions_button.setPreferredSize(new Dimension(20,20));
    c.insets =  new Insets(0, 0, 25, 0);
    middle_panel.add(login_main_button, c);

    constraints(c,0,0,1);
    c.insets =  new Insets(0, 0, 225, 25);
    cashier_button.setPreferredSize(new Dimension(400,200));
    middle_panel.add(cashier_button,c);

    constraints(c,1,0,1);
    c.insets =  new Insets(0, 25, 225, 25);
    product_button.setPreferredSize(new Dimension(400,200));
    middle_panel.add(product_button,c);

    constraints(c,2,0,1);
    c.insets =  new Insets(0, 25, 225, 0);
    transactions_button.setPreferredSize(new Dimension(400,200));
    middle_panel.add(transactions_button,c);



    //f.add(middle_panel);
    //Color customColor = new Color(204, 255, 204);
    f.getRootPane().setDefaultButton(login_main_button);
    f.getContentPane().setBackground(Color.red);
    // top_panel.setOpaque(false);
    // middle_panel.setOpaque(false);
    temp_panel.setOpaque(false);
    f.setSize(1400,900);
    f.add(top_panel);
    f.add(middle_panel);
    f.add(temp_panel);
    f.setLocationRelativeTo(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);




  }

  /**
  *This is where we execute the files.
  *<p>
  *In this case we are executing the main GUI that will be base where we will be able to branch to the other GUIs.
  *
  * @see MAIN_GUI()
  */
  public static void main(String[] args) {
    MAIN_GUI s = new MAIN_GUI();
  }

}
