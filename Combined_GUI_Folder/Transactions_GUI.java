import java.sql.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout.*;
import javax.swing.BorderFactory.*;
import javax.swing.border.*;
import javax.swing.text.BadLocationException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.HashMap;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transactions_GUI extends JFrame {
  static java.util.List<String> menu_combined = new ArrayList<>(Arrays.asList(
    "Bowl",
    "Plate",
    "Bigger_Plate",
    "Family_Meal",
    "Cub_Meal",
    "A_La_Carte_Entree_Sm",
    "A_La_Carte_Entree_Md",
    "A_La_Carte_Entree_Lg",
    "A_La_Carte_Side_Md",
    "A_La_Carte_Side_Lg",
    "Orange_Chicken",
    "Kung_Pao_Chicken",
    "Mushroom_Chicken",
    "Beijing_Beef",
    "Broccoli_Beef",
    "Black_Pepper_Angus_Steak",
    "Honey_Walnut_Shrimp",
    "Grilled_Teriyaki_Chicken",
    "Black_Pepper_Chicken",
    "Honey_Sesame_Chicken",
    "Sweetfire_Chicken",
    "String_Bean_Chicken",
    "SEASONAL",
    "White_Rice",
    "Brown_Rice",
    "Fried_Rice",
    "Chow_Mein",
    "Super_Greens",
    "Egg_Rolls",
    "Spring_Rolls",
    "Sm_Drink",
    "Md_Drink",
    "Lg_Drink",
    "Iced_Tea",
    "Bottled_Water",
    "Juice",
    "No_Drink",
    "none"
  ));
  static java.util.List<String> ingredients = new ArrayList<>(Arrays.asList(
    "Orange_Sauce",
    "Kung_Pao_Sauce",
    "Mushrooms",
    "Broccoli",
    "Black_Pepper",
    "Honey_Walnut_Mix",
    "Teriyaki_Sauce",
    "Sweetfire_Sauce",
    "Honey_Sesame_Mix",
    "White_Rice",
    "Noodles",
    "Vegetables",
    "Vegetable_Mix",
    "Egg_Roll_Casing",
    "Soda_Syrup",
    "Beef",
    "Chicken",
    "Steak",
    "Shrimp",
    "String_Beans",
    "Brown_Rice",
    "Tea",
    "NULL"
  ));

  /**
  *Gives constraint dimensions to a layout constraint varaible.
  *<p>
  *This method is constantly used in ordering and placing different labels and buttons
  *on a grid with the passed on parameters.
  *
  * @param gridx The x-axis of where we want our item to be placed
  * @param gridy The y-axis of where we want our item to be placed
  * @param gridwidth The width of how many grid spaced you want the item to take up
  * @return c The constraint variable that is configured
  */
  public static GridBagConstraints constraints(int gridx, int gridy, int gridwidth) {
    GridBagConstraints c = new GridBagConstraints();
    if (gridwidth == 99) {
      c.insets = new Insets(0,0,5,0);
    }
    c.gridx = gridx;
    c.gridy = gridy;
    c.gridwidth = gridwidth;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    c.weighty = 1.0;
    return c;
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
    my_panel.setBorder(border);
    my_panel.setBounds(bound1x,bound1y,bound2x,bound2y);
  }

  public static void adjust_text_area(JTextArea my_area) {
    my_area.setBorder(new BevelBorder(BevelBorder.LOWERED));
    my_area.setBackground(Color.LIGHT_GRAY);
  }
  /**
  *Manipulates the physical feature of a Java labels.
  *<p>
  *This method is constantly used in assigning labels their look on the front end
  *and it conveniently keeps all the labels in use with a uniform look.
  *
  * @param inputLabel The label that will be assigned the different attributes
  */
  public static void labelsettings(JLabel inputLabel) {
    inputLabel.setFont(new Font("Verdana",Font.BOLD,16));
    inputLabel.setPreferredSize(new Dimension(330,40));
    inputLabel.setHorizontalAlignment(JLabel.CENTER);
    inputLabel.setVerticalAlignment(JLabel.CENTER);
    inputLabel.setBackground(Color.red);
    inputLabel.setOpaque(true);
    inputLabel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  // GET THE LATEST TRANSACTION ID
  public static int get_latest_transaction() {
    //Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // QUERY DATABASE FOR LASTEST TRANSACTION ID
    int output = 200000;
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = "SELECT id FROM transactions ORDER BY id DESC LIMIT 1";  // create an SQL statement
      ResultSet result = stmt.executeQuery(query); // send statement to DBMS
      while (result.next()) { // Get responses from database
        output = Integer.parseInt(result.getString(1));
      }
    } catch (Exception e){
      JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
    }

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }

    return output;
  }

  /* Samuel Fafel
    menu_query takes in a string for the menu requested
    and returns a Hashmap (dictionary) containing each row
    of data in the requested menu SQL table.
    Key is the row of the table
    Value is a Vector containing the entries in that row.
    Indexes of that Vector correspond to columns in the table.
  */
  // Ask for all the entries in a given MENU (only menus)
  public static HashMap< Integer, Vector<String> > menu_query(String menu) {
    //Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // QUERY DATABASE FOR TRANSACTIONS
    HashMap< Integer, Vector<String> > transaction_info = new HashMap< Integer, Vector<String> >();
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = "SELECT * FROM " + menu;  // create an SQL statement
      ResultSet result = stmt.executeQuery(query); // send statement to DBMS
      int id = 1;
      while (result.next()) { // Get responses from database
      Vector<String> response = new Vector<String>();
        for (int i = 1; i <= 7; i++) { // 7 Columns of Data
          String output = result.getString(i);
          response.add(output);
        }
        transaction_info.put(id, response);
        id += 1;
      }
    } catch (Exception e){
      JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
    }

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }

    return transaction_info;
  }

  /* Samuel Fafel
    button_query takes in an id for the transaction requested
    and returns a Vector containing each entry in the transaction
    Indexes of the Vector correspond to columns in the table.
  */
  // BUTTON QUERY
  public static Vector<String> button_query(int id) {
    //Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // QUERY DATABASE FOR TRANSACTIONS
    Vector<String> transaction_info = new Vector<String> ();
    try{
        Statement stmt = conn.createStatement(); // create a statement object
        String query = "SELECT * FROM transactions WHERE id='" + id + "'";  // create an SQL statement
        ResultSet result = stmt.executeQuery(query); // send statement to DBMS
        while (result.next()) { // Get responses from database
        Vector<String> response = new Vector<String>();
          for (int i = 1; i <= 16; i++) { // 16 Columns of Data
            String output = result.getString(i);
            transaction_info.add(output);
          }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
    }
    return transaction_info;
  }

  /* Samuel Fafel
    generic_query takes in a string for the SQL query and the open
    Connection to the server. It returns a Hashmap similar to menu_query
    except that the Hashmap contains data for transactions rather than menus
    Key is the row of the table
    Value is a Vector containing the entries in that row.
    Indexes of that Vector correspond to columns in the table.
  */
  // GENERIC TRANSACTION QUERY
  public static HashMap<Integer, Vector<String> > generic_query(String sql_query, Connection conn) {
    // QUERY DATABASE FOR TRANSACTIONS
    HashMap< Integer, Vector<String> > transaction_info = new HashMap< Integer, Vector<String> >();
    try{
        Statement stmt = conn.createStatement(); // create a statement object
        String query = sql_query;  // create an SQL statement
        ResultSet result = stmt.executeQuery(query); // send statement to DBMS
        while (result.next()) { // Get responses from database
        Vector<String> response = new Vector<String>();
          for (int i = 1; i <= 15; i++) { // 15 Columns of Data
            String output = result.getString(i);
            response.add(output);
          }
          transaction_info.put(Integer.parseInt(response.get(0)), response);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
    }
    return transaction_info;
  }

  /* Samuel Fafel
    run_command takes in a string for the SQL query and returns void
    it submits the command to the server, and the server is in charge of processing it.
  */
  // SUBMIT ANY COMMAND TO DATABASE
  public static void run_command(String sql_query) {
    //Build the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // run command
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = sql_query;  // create an SQL statement
      ResultSet result = stmt.executeQuery(query); // send statement to DBMS
    } catch (Exception e){
      if (!e.toString().contains("No results were returned by the query.")) {
        JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
      }
    }
  }

  /***
   * Generates a string to be the content of an X report.
   * Goes through all the transactions of the current day to display and update running totals of sold items and sales.
   * If a z report exists for the current day, only later transactions are considered
   * @return report_string
   */
  public static String generateXReport(){
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

    // Get & format current date
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
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
    String report_string = "X Report for " + current_date + "\n";
    // Sales totals
    float total = 0;
    // Temp string for item names
    String item = "";
    // String for holding the z report time bound
    String z_time = "";

    // Try database operations
    try{
        //extract menu totals
        Statement stmt = conn.createStatement(); // create a second statement object
        ResultSet DB_menu = stmt.executeQuery("SELECT name, running_total FROM menu_full");
        HashMap<String,Integer> totals_map = new HashMap<String,Integer>();
        while(DB_menu.next()){
          totals_map.put(DB_menu.getString("name"), DB_menu.getInt("running_total") );
        }

        //check for z report checkpoint times from current date
        ResultSet z_result = stmt.executeQuery("SELECT * FROM z_reports WHERE date='" + current_date + "' ORDER BY time DESC LIMIT 1");
        if(z_result.next()){
          z_time = z_result.getString("time");
        }else{
          z_time = "00:00:00";
        }
        report_string += "since " + z_time + "\n";

        // Get responses from database
        String query = "SELECT * FROM transactions WHERE date='" + current_date + "' AND time>='" + z_time + "'";  // create an SQL query
        ResultSet result = stmt.executeQuery(query); // send statement to DBMS
        while (result.next()) {
          //for (meal_size, entrees 1-3, sides 1-2, drink), add 1 to the item's running total if it appears
          for(int i = 3; i <=9; i++){
            item = result.getString(i);
            try{
              //if transaction time > z report time
              totals_map.put(item,totals_map.get(item) + 1);
            }catch (Exception e){
              //System.out.println(e.toString());
            }
          }

          // sum subtotals
          total += result.getFloat("subtotal");
        }

        // add every entry to the report & update database
        for(Map.Entry<String, Integer> duo : totals_map.entrySet() ){
          report_string += duo.getKey() + ": " + duo.getValue() + "\n";
          run_command("UPDATE menu_full SET running_total=" + duo.getValue() + " WHERE name='" + duo.getKey() + "'");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error accessing Database for X Report:\n" + e);
    }

    // add total sales
    report_string += "Total Sales: $" + Float.toString(total) + "\n";

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }

    return report_string;
  }

  /* Samuel Fafel
    generate_sales_report takes in strings for the start/end date/times
  */
  // GENERATE SALES REPORT
  public static void generate_sales_report(String start_date, String start_time, String end_date, String end_time) {
    //Build the Connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // Gather Transactional Data
    LocalDate startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate currentDate = startDate;
    HashMap< Integer, Vector<String> > all_transactions = new HashMap< Integer, Vector<String> >();
    while (!currentDate.isAfter(endDate)) {
      String currentDate_formatted = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      String request = "SELECT * FROM transactions WHERE date='" + currentDate_formatted + "'";
      if (currentDate_formatted.equalsIgnoreCase(end_date)) {
        request += " AND time<'" + end_time + "'";
      }
      if (currentDate_formatted.equalsIgnoreCase(start_date)) {
        request += " AND time>'" + start_time + "'";
      }
      HashMap< Integer, Vector<String> > day_transactions = generic_query(request, conn);
      all_transactions.putAll(day_transactions); // add today's sales to all_transactions
      currentDate = currentDate.plusDays(1);
    }

    // Count how many sales of each item and put into hashmap
    HashMap<String, Integer> sales_by_item = new HashMap<String, Integer>();
    for (String menu_item : menu_combined) {
      sales_by_item.put(menu_item, 0); // Initialize all at zero
    }
    for (Vector<String> transaction : all_transactions.values()) {
      for (int i = 2; i <= 8; i++) {
        String item = transaction.get(i);
        int increment = sales_by_item.get(item) + 1;
        sales_by_item.put(item, increment); // Increment Counts
      }
    }

    // Create a New Frame for the Report
    JFrame sales_report_frame = new JFrame();
    JPanel sales_report_panel = new JPanel(new GridBagLayout());
    JLabel sales_report_label = new JLabel("Sales Report for Dates: " + start_date + " - " + end_date);
    sales_report_frame.setTitle("Sales Report");
    adjust_panel(sales_report_panel, Color.white, BorderFactory.createLoweredBevelBorder(), 100,100,100,100);
    sales_report_label.setFont(new Font("Verdana",1,15));
    sales_report_panel.add(sales_report_label, constraints(0, 0, 20));

    // Print out sales for each item
    for (int i = 0; i < menu_combined.size(); i++) {
      String key = menu_combined.get(i);
      Integer value = sales_by_item.get(key);
      if (key != "none") {
        sales_report_panel.add(new JLabel(Integer.toString(value)), constraints(0, i+1, 1));
        sales_report_panel.add(new JLabel(" | " + key), constraints(1, i+1, 1));
      }
    }

    sales_report_frame.setSize(650, 700);
    sales_report_frame.add(sales_report_panel, BorderLayout.CENTER);
    sales_report_frame.setVisible(true);

    // Close the Connection
    try {
      conn.close();
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }
  }

  public static void generate_excess_report(String start_date, String start_time, String end_date, String end_time) {
    System.out.println("Generating Excess Report");
    // Build the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
    // Gather Transactional Data
    LocalDate startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate currentDate = startDate;
    HashMap< Integer, Vector<String> > all_transactions = new HashMap< Integer, Vector<String> >();
    while (!currentDate.isAfter(endDate)) {
      String currentDate_formatted = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      String request = "SELECT * FROM transactions WHERE date='" + currentDate_formatted + "'";
      if (currentDate_formatted.equalsIgnoreCase(end_date)) {
        request += " AND time<'" + end_time + "'";
      }
      if (currentDate_formatted.equalsIgnoreCase(start_date)) {
        request += " AND time>'" + start_time + "'";
      }
      HashMap< Integer, Vector<String> > day_transactions = generic_query(request, conn);
      all_transactions.putAll(day_transactions); // add today's sales to all_transactions
      currentDate = currentDate.plusDays(1);
    }
    // Getting current stock amount
    HashMap<String, Integer> stock_amount = new HashMap<String, Integer>();
    try {
      Statement stock_req = conn.createStatement();
      ResultSet resultSet = stock_req.executeQuery("SELECT name, stock FROM perishable");

      while (resultSet.next()) {
        String name = resultSet.getString("name");
        int stock = resultSet.getInt("stock");
        double stock_limit = stock * 0.1;
        stock_amount.put(name, (int)stock_limit);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // Count how many ingredients were used and put into hashmap
    HashMap<String, Integer> sales_by_ing = new HashMap<String, Integer>();
    System.out.println("Allocating Ingredients HashMap");
    for (String ing : ingredients) {
      sales_by_ing.put(ing, 0); // Initializing all at zero
    }
    HashMap<Integer, Vector<String>> menu_combined;
    menu_combined = menu_query("menu_full");

    System.out.println("Entering transactions");
    for (Vector<String> transaction : all_transactions.values()) {
      for (int i = 3; i <= 8; i++) {
        boolean found = false;
        String item = transaction.get(i);
        for (Vector<String> entry : menu_combined.values()) {
          if (item != "NULL" && item != "none" && found == false) {
            String perishable_1 = "", perishable_2 = "";
            if (item.equalsIgnoreCase(entry.get(0))) {;
              perishable_1 = entry.get(1);
              perishable_2 = entry.get(2);
              if (sales_by_ing.containsKey(perishable_1)) {
                int increment = sales_by_ing.get(perishable_1) + 1;
                sales_by_ing.put(perishable_1, increment);
              }
              if (sales_by_ing.containsKey(perishable_2)) {
                int increment = sales_by_ing.get(perishable_2) + 1;
                sales_by_ing.put(perishable_2, increment);
              }
              found = true;
            }
          }
        }
      }
    }
    Vector<String> items = new Vector<String>();
    Vector<String> percentages = new Vector<String>();
    for (Map.Entry<String, Integer> entry : sales_by_ing.entrySet()) {
      String key = entry.getKey();
      Integer value = entry.getValue();
      if (stock_amount.containsKey(key)) {
        if (sales_by_ing.get(key) < stock_amount.get(key)) {
          items.add(key);
          double percent = sales_by_ing.get(key)/ (stock_amount.get(key)/0.1) * 100;
          String format_percent = String.format("%.2f", percent);
          percentages.add(format_percent);
        }
      }
    }

    // Create a New Frame for the Report
    JFrame excess_report_frame = new JFrame();
    JPanel excess_report_panel = new JPanel(new GridBagLayout());
    JLabel excess_report_label = new JLabel("Excess Report for Dates: " + start_date + " - " + end_date);
    excess_report_frame.setTitle("Excess Report");
    adjust_panel(excess_report_panel, Color.white, BorderFactory.createLoweredBevelBorder(), 100,100,100,100);
    excess_report_label.setFont(new Font("Verdana",1,15));
    excess_report_panel.add(excess_report_label, constraints(0, 0, 20));

    // Print out excess for each item
    for (int i = 0; i < items.size(); i++) {
      excess_report_panel.add(new JLabel(items.get(i)), constraints(0, i+1, 1));
      excess_report_panel.add(new JLabel(" | " + percentages.get(i) + "% Sold"), constraints (1, i+1, 1));
    }

    excess_report_frame.setSize(650, 700);
    excess_report_frame.add(excess_report_panel, BorderLayout.CENTER);
    excess_report_frame.setVisible(true);

    // Close the Connection
    try {
      conn.close();
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }
  }

  static JFrame f;
  public Transactions_GUI() throws BadLocationException {
    //Build the Connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // create a new frame
    f = new JFrame("DB GUI");

    //Pannel Initiliztion
    JPanel top_panel = new JPanel(new GridBagLayout());
    JPanel list_panel = new JPanel(new GridBagLayout());
    JPanel info_panel = new JPanel(new GridBagLayout());
    JPanel bottom_panel = new JPanel(new GridBagLayout());
    JPanel temp_panel = new JPanel(new GridBagLayout());

    Border blackline;
    Border raisedbevel;
    Border loweredbevel;
    Border empty;

    blackline = BorderFactory.createLineBorder(Color.black);
    raisedbevel = BorderFactory.createRaisedBevelBorder();
    loweredbevel = BorderFactory.createLoweredBevelBorder();
    empty = BorderFactory.createEmptyBorder();

    // Make X Report Button & add action listener
    JButton x_button = new JButton("X Report");
    x_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,generateXReport());
      }
    });

    // Make Close Button & add action listener
    JButton close_button = new JButton("Close");
    for (int i = 0; i < 1; i++) {
      Connection temp_conn = conn;
      close_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //closing the connection
          try {
            temp_conn.close();
          } catch(Exception a) {
            JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
          }
          f.dispose();
        }
      });
    }

    // TOP PANEL
    adjust_panel(top_panel, Color.gray, loweredbevel, 20,20,1340,60);
    f.add(top_panel); // add panel to frame
    JLabel top_label = new JLabel("Manage Receipts Interface");
    top_label.setFont(new Font("Verdana",1,30));
    top_panel.add(top_label); // add label to panel

    // LIST PANEL
    adjust_panel(list_panel, Color.white, loweredbevel, 20,100,600,650);
    f.add(list_panel);
    JLabel list_label = new JLabel("Reports");
    labelsettings(list_label);
    list_panel.add(list_label, constraints(0,0,99));

    // SALES REPORT BUTTON
    list_panel.add(new JLabel("Start Date/Time:"), constraints(0,1,1));
    list_panel.add(new JLabel("End Date/Time:"), constraints(1,1,1));
    JTextArea sales_report_startdate = new JTextArea("MM/DD/YYYY", 1,12);
    JTextArea sales_report_enddate = new JTextArea("MM/DD/YYYY", 1,12);
    JTextArea sales_report_starttime = new JTextArea("HH:MM:SS", 1,12);
    JTextArea sales_report_endtime = new JTextArea("HH:MM:SS", 1,12);

    adjust_text_area(sales_report_startdate);
    adjust_text_area(sales_report_enddate);
    adjust_text_area(sales_report_starttime);
    adjust_text_area(sales_report_endtime);

    JButton sales_report_generate = new JButton("Generate Sales Report");
    list_panel.add(sales_report_startdate, constraints(0,2,1));
    list_panel.add(sales_report_enddate, constraints(1,2,1));
    list_panel.add(sales_report_starttime, constraints(0,3,1));
    list_panel.add(sales_report_endtime, constraints(1,3,1));
    list_panel.add(sales_report_generate, constraints(2,2,1));
    sales_report_generate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generate_sales_report(sales_report_startdate.getText(), sales_report_starttime.getText(), sales_report_enddate.getText(), sales_report_endtime.getText());
      }
    });

    // EXCESS REPORT BUTTON
    list_panel.add(new JLabel("Start Date/Time:"), constraints(0,4,1));
    list_panel.add(new JLabel("End Date/Time:"), constraints(1,4,1));
    JTextArea excess_report_startdate = new JTextArea("MM/DD/YYYY", 1,12);
    JTextArea excess_report_enddate = new JTextArea("MM/DD/YYYY", 1,12);
    JTextArea excess_report_starttime = new JTextArea("HH:MM:SS", 1,12);
    JTextArea excess_report_endtime = new JTextArea("HH:MM:SS", 1,12);

    adjust_text_area(excess_report_startdate);
    adjust_text_area(excess_report_enddate);
    adjust_text_area(excess_report_starttime);
    adjust_text_area(excess_report_endtime);

    JButton excess_report_generate = new JButton("Excess Sales Report");
    list_panel.add(excess_report_startdate, constraints(0,5,1));
    list_panel.add(excess_report_enddate, constraints(1,5,1));
    list_panel.add(excess_report_starttime, constraints(0,6,1));
    list_panel.add(excess_report_endtime, constraints(1,6,1));
    list_panel.add(excess_report_generate, constraints(2,5,1));
    excess_report_generate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generate_excess_report(excess_report_startdate.getText(), excess_report_starttime.getText(), excess_report_enddate.getText(), excess_report_endtime.getText());
      }
    });

    for (int i = 4; i < 30; i++) {
      list_panel.add(new JLabel(" "), constraints(0,i,1));
    }

    // RESTOCK REPORT BUTTON
    JButton restock_report_generate = new JButton("Restock Report");
    list_panel.add(restock_report_generate, constraints(2, 8, 1));
    restock_report_generate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try{
          Restock_Report restock_report = new Restock_Report();
        } catch(Exception a) {
          JOptionPane.showMessageDialog(null,"Error opening Restock Report: " + a);
        }
      }
    });

    // INFO PANEL
    adjust_panel(info_panel, Color.white, loweredbevel, 640,100,720,650);
    f.add(info_panel);
    JLabel info_label = new JLabel("Receipt Info");
    labelsettings(info_label);
    info_label.setFont(new Font("Verdana",1,20));
    info_panel.add(info_label, constraints(0, 0, 20));
    String[] fieldnames = new String[] {"ID", "Order Type", "Meal Size", "Entree 1", "Entree 2", "Entree 3",
    "Side 1", "Side 2", "Drink", "Date", "Conducted By", "Payment Method  ", "Subtotal", "Tax", "Total", "Time"};
    Vector<JTextArea> textfields = new Vector<JTextArea>();
    for (int i = 0; i < fieldnames.length; i++) {
      JTextArea temp = new JTextArea(fieldnames[i], 1, 25);
      adjust_text_area(temp);
      textfields.add(temp);
      info_panel.add(new JLabel(fieldnames[i]), constraints(0, i+1, 1));
      info_panel.add(textfields.get(i), constraints(1, i+1, 1));
    }

    // MANUAL TRANSACTION LOOKUP
    list_panel.add(new JLabel("Manual Transaction Lookup:"), constraints(0,33,1));
    JTextArea manual_lookup = new JTextArea("Enter Transaction ID", 1,12);
    adjust_text_area(manual_lookup);
    JButton request_transaction = new JButton("Display Transaction");
    list_panel.add(manual_lookup, constraints(0,34,1));
    list_panel.add(request_transaction, constraints(1,34,1));
    request_transaction.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int id = Integer.parseInt(manual_lookup.getText());
        Vector<String> update_info = button_query(id);
        for (int i = 0; i < textfields.size(); i++) {
          textfields.get(i).setText(update_info.get(i));
        }
      }
    });

    // LATEST TRANSACTION BUTTON
    JButton latest_button = new JButton("View Latest Transaction");
    latest_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int id = get_latest_transaction();
        Vector<String> update_info = button_query(id);
        for (int i = 0; i < textfields.size(); i++) {
          textfields.get(i).setText(update_info.get(i));
        }
      }
    });
    list_panel.add(latest_button, constraints(0,35,2));

    // BOTTOM PANEL
    adjust_panel(bottom_panel, Color.gray, loweredbevel, 20,770,1340,70);
    f.add(bottom_panel);
    bottom_panel.add(close_button, constraints(0,0,1));
    bottom_panel.add(x_button);
    JTextArea query_box = new JTextArea("", 1,75);
    adjust_text_area(query_box);
    JButton query_button = new JButton("submit query");
    query_button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          run_command(query_box.getText());
          query_box.setText("");
        }
      });
    bottom_panel.add(query_button, constraints(0,1,1));
    bottom_panel.add(query_box, constraints(1,1,1));

    // BORDER PANEL (Required for coherence)
    temp_panel.setBackground(Color.lightGray);
    temp_panel.setBorder(loweredbevel);
    f.add(temp_panel);

    // set the size of frame
    f.setSize(1400, 900);
    f.setVisible(true);
  }
}
