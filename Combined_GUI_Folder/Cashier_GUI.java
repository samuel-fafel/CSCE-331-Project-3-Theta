import java.sql.*;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.lang.Math;
import java.text.SimpleDateFormat;

/** * Creates a GUI for cahsier workers of the Panda Express in the MSC.
  * It will include many other classes that will help with the front end and the back end
  * Many of the front end classes are void and just format
  * The rest of the function lay in main.
  *
  * @author Nicholas Nguyen
  * @author Namson Pham
  * @author Samuel Fafel
* **/
/**
  *This class contains everything needed for the cashier GUI interface, which will be used
  *for cashiers and food servers to input orders.
  *<p>
  *It houses all the methods and classes that will used, and
  *it also contains Cashier_GUI() itself that will be executed.
  *
  * @author Nicholas Nguyen
  * @author Samuel Fafel
  * @author Namson Pham
  */
public class Cashier_GUI extends JFrame {
  static JFrame f;
  private static String user;
  public static int TRANSACTION_ID;
  public static int ORDER_ID;
  
  static public int entrees_index = 3;
  static public int sides_index = 6;
  static public int meal_cap = 1;

  private static HashMap<Integer, String> current_item_list;

  static java.util.List<String> menu_meals = new ArrayList<>(Arrays.asList(
    "Bowl",
    "Plate",
    "Bigger_Plate",
    "Family_Meal",
    "Cub_Meal",
    "A_La_Carte_Entree_Sm",
    "A_La_Carte_Entree_Md",
    "A_La_Carte_Entree_Lg",
    "A_La_Carte_Side_Md",
    "A_La_Carte_Side_Lg"
  ));

  static java.util.List<String> menu_entrees = new ArrayList<>(Arrays.asList(
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
    "SEASONAL"
  ));

  static java.util.List<String> menu_sides = new ArrayList<>(Arrays.asList(
    "White_Rice",
    "Fried_Rice",
    "Brown_Rice",
    "Chow_Mein",
    "Super_Greens",
    "Egg_Rolls",
    "Spring_Rolls"
  ));

  static java.util.List<String> menu_drinks = new ArrayList<>(Arrays.asList(
    "Sm_Drink",
    "Md_Drink",
    "Lg_Drink",
    "Iced_Tea",
    "Bottled_Water",
    "Juice",
    "No_Drink"
  ));

  public static void get_latest_IDs() {
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

    // QUERY DATABASE FOR LASTEST TRANSACTION ID
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = "SELECT id,order_number FROM transactions ORDER BY id DESC LIMIT 1";  // create an SQL statement
      ResultSet result = stmt.executeQuery(query); // send statement to DBMS
      if(result.next()) { // Get responses from database
        TRANSACTION_ID = result.getInt("id");
        ORDER_ID = result.getInt("order_number");
      }
    } catch (Exception e){
      JOptionPane.showMessageDialog(null,"Error accessing Database for transaction numbers:\n" + e);
    }

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }

    return;
  }

  public static void set_user(String user_input) {
    user = user_input;
  }
  public static String get_user() {
    return user;
  }

  /**
  *Manipulates the physical feature of a Java buttons.
  *<p>
  *This method is constantly used in assigning buttons their look on the front end
  *and it conveniently keeps all the buttons in use with a uniform look.
  *
  * @param inputButton The button that will be assigned the different attributes
  */
  public static void buttonsettings(JButton inputButton) {
    inputButton.setFont(new Font("Verdana",Font.BOLD,12));
    inputButton.setPreferredSize(new Dimension(110,40));
    inputButton.setHorizontalAlignment(JButton.CENTER);
    inputButton.setVerticalAlignment(JButton.CENTER);
    inputButton.setBackground(Color.white);
    inputButton.setOpaque(true);
    inputButton.setBorder(BorderFactory.createLineBorder(Color.black));
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

  /**
  *Manipulates the physical feature of a Java labels but more specialized for the labeling of the sides.
  *<p>
  *This method is used in assigning label features to labels that are used in the sides column.
  *
  * @param inputLabel The label that will be assigned the different attributes
  */
  public static void sides_labelsettings(JLabel inputLabel) {
    inputLabel.setFont(new Font("Verdana",Font.BOLD,16));
    inputLabel.setPreferredSize(new Dimension(200,40));
    inputLabel.setHorizontalAlignment(JLabel.CENTER);
    inputLabel.setVerticalAlignment(JLabel.CENTER);
    inputLabel.setBackground(Color.red);
    inputLabel.setOpaque(true);
    inputLabel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  /**
  *Gives constraint dimensions to a layout constraint varaible.
  *<p>
  *This method is constantly used in ordering and placing different labels and buttons
  *on a grid with the passed on parameters.
  *
  * @param a The constraint varaible for which we will assign dimensions to
  * @param gridx The x-axis of where we want our item to be placed
  * @param gridy The y-axis of where we want our item to be placed
  * @param gridwidth The width of how many grid spaced you want the item to take up
  */
  public static GridBagConstraints constraints(int gridx, int gridy, int gridwidth, GridBagConstraints a) {
    GridBagConstraints c = a;
    c.gridx = gridx;
    c.gridy = gridy;
    c.gridwidth = gridwidth;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    return c;
  }

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
      JOptionPane.showMessageDialog(null, e.getClass().getName()+": "+e.getMessage());
      //System.exit(0);
    }

    // QUERY DATABASE FOR ITEMS
    HashMap< Integer, Vector<String> > transaction_info = new HashMap< Integer, Vector<String> >();
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = "SELECT * FROM " + menu + " ORDER BY id";  // create an SQL statement
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

  public static void add_transaction(HashMap<Integer, String> item_list, String payment_method, Double subtotal) {
    // CONNECT TO DATABASE
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

    // Get date info:
    java.util.Date now = new java.util.Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String current_time = sdf.format(now);
    String almost_date = "0" + month + "/" + day + "/" + year;
    String current_date = almost_date.substring(almost_date.length() - 10);

    // CREATE TRANSACTION
    double tax = Math.round(subtotal * 0.0825 * 100.0) / 100.0;
    double total = Math.round((subtotal + tax) * 100.0) / 100.0;
    current_item_list.put(0,  Integer.toString(TRANSACTION_ID));
    current_item_list.put(9,  current_date);
    current_item_list.put(10, get_user());
    current_item_list.put(11, payment_method);
    current_item_list.put(12, Double.toString(subtotal));
    current_item_list.put(13, Double.toString(tax));
    current_item_list.put(14, Double.toString(total));
    current_item_list.put(15, current_time);
    current_item_list.put(16, Integer.toString(ORDER_ID));

    // Assemble String
    String values_list = "";
    for (int key: item_list.keySet()) {
      if (key != 16) {
        values_list += "'" + item_list.get(key) + "', ";
      }
      else values_list += "'" + item_list.get(key) + "'";
    }

    System.out.println(values_list);

    // INSERT INTO DATABASE
    try{
      Statement stmt = conn.createStatement(); // create a statement object
      String query = "INSERT INTO transactions VALUES(" + values_list + ")";  // create an SQL statement
      stmt.executeQuery(query); // send statement to DBMS
    } catch (Exception e) {
      if (!e.toString().contains("No results were returned by the query.")) {
        JOptionPane.showMessageDialog(null,"Error accessing Database:\n" + e);
      }
    }

    //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }
  }

  /* ITEM PARSING
    // Parse order_items
      int meals_index = 2;
      int entrees_index = 3;
      int sides_index = 6;
      int drinks_index = 8;
    if (menu_meals.contains(item)) {
      current_item_list.put(meals_index, item);
    }
    else if (menu_entrees.contains(item) && entrees_index < sides_index) {
      current_item_list.put(entrees_index, item);
      entrees_index++;
    }
    else if (menu_sides.contains(item) && sides_index < drinks_index) {
      current_item_list.put(sides_index, item);
      sides_index++;
    }
    else if (menu_drinks.contains(item)) {
      current_item_list.put(drinks_index, item);
    }
  */

  public static void update_text(JTextArea textfield_items, JTextArea textfield_prices, Vector<Double> prices) {
    String item_list = "";

    //build item_list string
    for( Map.Entry<Integer,String> elem : current_item_list.entrySet()){
      if(elem.getKey() > 1 && elem.getKey() < 9){
        if(elem.getValue() != "none"){
          if(elem.getKey() > 2 && elem.getKey() < 6){
            if( (elem.getKey()-meal_cap) <= 2){
              item_list += "\t" + elem.getValue() + "\n";
            }
          }else if(elem.getKey() > 5 && elem.getKey() < 8){
            item_list += "\t" + elem.getValue() + "\n";
          }else{
            item_list += elem.getValue() + "\n";
          }
        }
      }
    }

    textfield_items.setText(item_list);
  }

  public static double update_total(JTextArea textfield, Vector<Double> prices) {
    double total = 0;
    for (int i = 0; i < prices.size(); i++) {
      total += prices.get(i);
    }
    textfield.setText(String.format("$%.2f", total));
    return total;
  }

  public static void adjust_text_area (JTextArea my_area) {
    my_area.setBackground(Color.LIGHT_GRAY);
  }

  public static void login_frame_settings(JFrame frame) {
    frame.setTitle("Login");
    frame.setSize(300, 200);
    frame.setLayout(new BorderLayout());
  }

  /**
  *The contructor that is what makes our unique Cashier GUI.
  *<p>
  *This will put all the methods together with some added functionality
  *to see and use the cashier GUI on the screen. Which will be able to input various orders
  *
  */
  public Cashier_GUI() {
    get_latest_IDs();
    
    // create a new frame
    JFrame login_frame = new JFrame();
    login_frame_settings(login_frame);
    JPanel login_panel = new JPanel(new GridLayout(3, 2));
    JLabel user_label, pin_label;
    JTextField user_field, pin_field;
    JButton login_button = new JButton("Login");

    login_frame.add(login_panel, BorderLayout.CENTER);

    // Username
    user_label = new JLabel("Username: ");
    user_field = new JTextField();
    pin_label = new JLabel("PIN: ");
    pin_field = new JTextField();
    login_panel.add(user_label);
    login_panel.add(user_field);
    login_panel.add(pin_label);
    login_panel.add(pin_field);

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
          login_frame.setVisible(false);
          set_user(user);
          JOptionPane.showMessageDialog(null, "Welcome " + get_user());
          f.setVisible(true);
        } else {
          JOptionPane.showMessageDialog(null, "Please enter a VALID username");
        }
        // JOptionPane.showMessageDialog(null,"Opened database successfully");
        } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        JOptionPane.showMessageDialog(null, e.getClass().getName()+": "+e.getMessage());
        //System.exit(0);
        }
      }
    });
    login_panel.add(login_button);
    login_frame.setVisible(true);

    f = new JFrame("DB GUI");

    //border initialization
    Border loweredbevel = BorderFactory.createLoweredBevelBorder();

    //creating contraints
    GridBagConstraints c = new GridBagConstraints();
    c.weighty = 1.0;

    // INITIALIZATION OF PANELS & LABELS
    //Pannel Initiliztion
    JPanel top_panel = new JPanel();                        // Interface Title
    JPanel meal_panel = new JPanel(new GridBagLayout());    // Meals
    JPanel entree_panel = new JPanel(new GridBagLayout());  // Entrees
    JPanel side_panel = new JPanel(new GridBagLayout());    // Sides
    JPanel drink_panel = new JPanel(new GridBagLayout());   // Drinks
    JPanel bottom_panel = new JPanel(new GridBagLayout());  // Close Panel
    JPanel view_panel = new JPanel(new GridBagLayout());    // View Current Order Panel
    JPanel payment_panel = new JPanel(new GridBagLayout()); // Decide Payment Method
    JPanel temp_panel = new JPanel(); // required for coherence

    //Label initialization
    JLabel top_label = new JLabel("Cashier Ordering Interface");
    JLabel meal_size_label = new JLabel("Regular Meals"); labelsettings(meal_size_label);
    JLabel alacarte_label = new JLabel("A-La-Carte"); labelsettings(alacarte_label);
    JLabel entree_label = new JLabel("Entrees"); labelsettings(entree_label);
    JLabel side_label = new JLabel("Sides"); sides_labelsettings(side_label);
    JLabel apps_label = new JLabel("Appetizers"); sides_labelsettings(apps_label);
    JLabel drink_label = new JLabel("Drinks"); sides_labelsettings(drink_label);
    JLabel order_items_label  = new JLabel("Order Items:"); labelsettings(order_items_label);
    JLabel order_prices_label = new JLabel("Item Prices:"); labelsettings(order_prices_label);
    JLabel order_totals_label = new JLabel("Order Subtotal:"); labelsettings(order_totals_label);
    JLabel payment_label = new JLabel("Payment Method:"); labelsettings(payment_label);

    // SET BASE FRAME SETTINGS FOR PANELS
    {
      // Top Panel Frame Settings
      top_panel.setBounds(20,20,1340,60);
      top_panel.setBackground(Color.gray);
      top_panel.setBorder(loweredbevel);
      top_label.setFont(new Font("Verdana",1,30));
      top_panel.add(top_label);

      // Meals/A-La-Carte Panel Frame Settings
      meal_panel.setBounds(20,100,360,480);
      meal_panel.setBackground(Color.white);
      meal_panel.setBorder(loweredbevel);

      // Entrees Panel Frame Settings
      entree_panel.setBounds(400,100,360,480);
      entree_panel.setBackground(Color.white);
      entree_panel.setBorder(loweredbevel);

      // Sides/Apps Panel Frame Settings
      side_panel.setBounds(780,100,300,480);
      side_panel.setBackground(Color.white);
      side_panel.setBorder(loweredbevel);

      // Drinks Panel Frame Settings
      drink_panel.setBounds(1100,100,260,480);
      drink_panel.setBackground(Color.white);
      drink_panel.setBorder(loweredbevel);

      // View Panel Frame Settings
      view_panel.setBounds(20,600,740,170);
      view_panel.setBackground(Color.white);
      view_panel.setBorder(loweredbevel);

      // Payment Panel Frame Settings
      payment_panel.setBounds(780,600,580,170);
      payment_panel.setBackground(Color.white);
      payment_panel.setBorder(loweredbevel);

      // Bottom Panel Frame Settings
      bottom_panel.setBounds(20,790,1340,50);
      bottom_panel.setBackground(Color.gray);
      bottom_panel.setBorder(loweredbevel);
      bottom_panel.setLayout(new GridLayout(1, 3, 450, 25));

      // Temp Panel (Coherency) Frame Settings
      temp_panel.setBackground(Color.lightGray);
      temp_panel.setBorder(loweredbevel);
    }

    // BUTTON INITIALIZATION
    // Initializing Meal / A-La-Carte Buttons
    Vector<JButton> meal_alacarte_buttons = new Vector<JButton>();
    JButton bowl_size = new JButton("Bowl"); meal_alacarte_buttons.add(bowl_size);
    JButton plate_size = new JButton("Plate");meal_alacarte_buttons.add(plate_size);
    JButton bigger_plate_size = new JButton("Bigger Plate"); meal_alacarte_buttons.add(bigger_plate_size);
    JButton family_size = new JButton("Family Meal");meal_alacarte_buttons.add(family_size);
    JButton cub_size = new JButton("Cub Meal");meal_alacarte_buttons.add(cub_size);
    JButton sm_entree = new JButton("Small Entree"); meal_alacarte_buttons.add(sm_entree);
    JButton md_entree = new JButton("Medium Entree"); meal_alacarte_buttons.add(md_entree);
    JButton lg_entree = new JButton("Large Entree"); meal_alacarte_buttons.add(lg_entree);
    JButton md_side = new JButton("Medium Side"); meal_alacarte_buttons.add(md_side);
    JButton lg_side = new JButton("Large Side"); meal_alacarte_buttons.add(lg_side);
    JButton no_meal = new JButton("None"); meal_alacarte_buttons.add(no_meal);
    JButton no_alacarte = new JButton("None"); meal_alacarte_buttons.add(no_alacarte);
    for (int i = 0; i < meal_alacarte_buttons.size(); i++) {
      buttonsettings(meal_alacarte_buttons.get(i));
    }

    // Initializing Entree Buttons
    Vector<JButton> entree_buttons = new Vector<JButton>();
    JButton orangeChicken = new JButton("<html><center>Orange<br>Chicken</center></html>"); entree_buttons.add(orangeChicken);
    JButton kungPaoChicken = new JButton("<html><center>Kung Pao<br>Chicken</center></html>"); entree_buttons.add(kungPaoChicken);
    JButton mushChicken = new JButton("<html><center>Mushroom<br>Chicken</center></html>"); entree_buttons.add(mushChicken);
    JButton beijingBeef = new JButton("<html><center>Beijing<br>Beef</center></html>"); entree_buttons.add(beijingBeef);
    JButton brocBeef = new JButton("<html><center>Brocolli<br>Beef</center></html>"); entree_buttons.add(brocBeef);
    JButton blackPepperSteak = new JButton("<html><center>Black Pepper<br>Angus Steak</center></html>"); entree_buttons.add(blackPepperSteak);
    JButton walnutShrimp = new JButton("<html><center>Honey Walnut<br>Shrimp</center></html>"); entree_buttons.add(walnutShrimp);
    JButton grillTeriChicken = new JButton("<html><center>Grilled Teriyaki<br>Chicken</center></html>"); entree_buttons.add(grillTeriChicken);
    JButton blkPepChicken = new JButton("<html><center>Black Pepper<br>Chicken</center></html>"); entree_buttons.add(blkPepChicken);
    JButton honeySesameChicken = new JButton("<html><center>Honey Sesame<br>Chicken Breast</center></html>"); entree_buttons.add(honeySesameChicken);
    JButton sweetFireChicken = new JButton("<html><center>Sweet Fire<br>Chicken Breast</center></html>"); entree_buttons.add(sweetFireChicken);
    JButton stringBeanChicken = new JButton("<html><center>String Bean<br>Chicken Breast</center></html>"); entree_buttons.add(stringBeanChicken);
    JButton seasonal = new JButton("<html><center>Seasonal<br>Entree</center></html>"); entree_buttons.add(seasonal);
    JButton no_entree = new JButton("None"); entree_buttons.add(no_entree);
    for (int i = 0; i < entree_buttons.size(); i++) {
      buttonsettings(entree_buttons.get(i));
    }

    // Initailizing Sides Buttons
    Vector<JButton> side_buttons = new Vector<JButton>();
    JButton white_rice = new JButton("White Rice"); side_buttons.add(white_rice);
    JButton fried_rice = new JButton("Fried Rice"); side_buttons.add(fried_rice);
    JButton brown_rice = new JButton("Brown Rice"); side_buttons.add(brown_rice);
    JButton chow_mein = new JButton("Chow Mein"); side_buttons.add(chow_mein);
    JButton super_greens = new JButton("Super Greens"); side_buttons.add(super_greens);
    JButton no_side = new JButton("None"); side_buttons.add(no_side);
    for (int i = 0; i < side_buttons.size(); i++) {
      buttonsettings(side_buttons.get(i));
    }

    // Initailizing Appetizer Buttons
    Vector<JButton> apps_buttons = new Vector<JButton>();
    JButton chicken_roll = new JButton("<html><center>Chicken<br>Egg Roll</center></html>"); apps_buttons.add(chicken_roll);
    JButton veggie_roll = new JButton("<html><center>Veggie<br>Spring Roll</center></html>"); apps_buttons.add(veggie_roll);
    JButton no_apps = new JButton("None"); apps_buttons.add(no_apps);
    for (int i = 0; i < apps_buttons.size(); i++) {
      buttonsettings(apps_buttons.get(i));
    }

    // Initailizing Drink Buttons
    Vector<JButton> drinks_buttons = new Vector<JButton>();
    JButton sm_fountain_drink = new JButton("Small Fountain Drink"); drinks_buttons.add(sm_fountain_drink);
    JButton md_fountain_drink = new JButton("Medium Fountain Drink"); drinks_buttons.add(md_fountain_drink);
    JButton lg_fountain_drink = new JButton("Large Fountain Drink"); drinks_buttons.add(lg_fountain_drink);
    JButton tea = new JButton("Brewed Ice Tea"); drinks_buttons.add(tea);
    JButton water_bottle = new JButton("Water Bottle"); drinks_buttons.add(water_bottle);
    JButton kid_juice = new JButton("Kid's Juice"); drinks_buttons.add(kid_juice);
    JButton no_drink = new JButton("None"); drinks_buttons.add(no_drink);
    for (int i = 0; i < drinks_buttons.size(); i++) {
      buttonsettings(drinks_buttons.get(i));
    }

    // Initializing View TextAreas
    JTextArea order_items = new JTextArea("",8,25);
    JTextArea order_prices = new JTextArea("",8,25);
    JTextArea order_totals = new JTextArea("",1,25);

    adjust_text_area(order_items);
    adjust_text_area(order_prices);
    adjust_text_area(order_totals);

    // Initializing Payment Method Buttons
    ButtonGroup payment_buttons = new ButtonGroup();
    JRadioButton radioButton1 = new JRadioButton("Dining Dollars");
    JRadioButton radioButton2 = new JRadioButton("Debit / Credit");

    // Initializing Bottom Buttons
    JButton close_button = new JButton("Close");
    JButton reset_order_button = new JButton("Reset Order");
    JButton place_order_button = new JButton("Place Order");

    // LABEL & BUTTON PLACEMENT
    {
      // Label/Button Placement for Meals
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        meal_panel.add(meal_size_label,c);

        c.insets = new Insets(1, 0, 10, 2);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        meal_panel.add(bowl_size, c);

        c.gridx = 1;
        c.gridy = 1;
        meal_panel.add(plate_size, c);

        c.gridx = 2;
        c.gridy = 1;
        meal_panel.add(bigger_plate_size, c);

        c.gridx = 0;
        c.gridy = 2;
        meal_panel.add(family_size, c);

        c.gridx = 1;
        c.gridy = 2;
        meal_panel.add(cub_size, c);

        c.gridx = 2;
        c.gridy = 2;
        meal_panel.add(no_meal, c);
      }

      // Label/Button Placement for A-La-Carte
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        meal_panel.add(alacarte_label,c);

        c.insets = new Insets(0, 0, 10, 2);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 7;
        meal_panel.add(sm_entree, c);

        c.gridx = 1;
        c.gridy = 7;
        meal_panel.add(md_entree, c);

        c.gridx = 2;
        c.gridy = 7;
        meal_panel.add(lg_entree, c);

        c.gridx = 0;
        c.gridy = 8;
        meal_panel.add(md_side, c);

        c.gridx = 1;
        c.gridy = 8;
        meal_panel.add(lg_side, c);

        c.gridx = 2;
        c.gridy = 8;
        meal_panel.add(no_alacarte, c);
      }

      // Label/Button Placement for Entree
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        entree_panel.add(entree_label,c);

        c.insets = new Insets(0, 0, 10, 2);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        entree_panel.add(orangeChicken, c);

        c.gridx = 1;
        c.gridy = 1;
        entree_panel.add(kungPaoChicken, c);

        c.gridx = 2;
        c.gridy = 1;
        entree_panel.add(mushChicken, c);

        c.gridx = 0;
        c.gridy = 2;
        entree_panel.add(beijingBeef, c);

        c.gridx = 1;
        c.gridy = 2;
        entree_panel.add(brocBeef, c);

        c.gridx = 2;
        c.gridy = 2;
        entree_panel.add(blackPepperSteak, c);

        c.gridx = 0;
        c.gridy = 3;
        entree_panel.add(walnutShrimp, c);

        c.gridx = 1;
        c.gridy = 3;
        entree_panel.add(grillTeriChicken, c);

        c.gridx = 2;
        c.gridy = 3;
        entree_panel.add(blkPepChicken, c);

        c.gridx = 0;
        c.gridy = 4;
        entree_panel.add(sweetFireChicken, c);

        c.gridx = 1;
        c.gridy = 4;
        entree_panel.add(honeySesameChicken, c);

        c.gridx = 2;
        c.gridy = 4;
        entree_panel.add(stringBeanChicken, c);

        c.gridx = 0;
        c.gridy = 5;
        entree_panel.add(seasonal, c);

        c.gridx = 1;
        c.gridy = 5;
        entree_panel.add(no_entree, c);
      }

      // Label/Button Placement for Side
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        side_panel.add(side_label,c);

        c.insets = new Insets(0, 0, 10, 2);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        side_panel.add(white_rice, c);

        c.gridx = 1;
        c.gridy = 1;
        side_panel.add(fried_rice, c);

        c.gridx = 0;
        c.gridy = 2;
        side_panel.add(brown_rice, c);

        c.gridx = 1;
        c.gridy = 2;
        side_panel.add(chow_mein, c);

        c.gridx = 0;
        c.gridy = 3;
        side_panel.add(super_greens, c);

        c.gridx = 1;
        c.gridy = 3;
        side_panel.add(no_side, c);
      }

      // Label/Button Placement for Apps
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        side_panel.add(apps_label,c);

        c.insets = new Insets(0, 0, 10, 2);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 7;
        side_panel.add(chicken_roll, c);

        c.gridx = 1;
        c.gridy = 7;
        side_panel.add(veggie_roll, c);

        c.gridx = 0;
        c.gridy = 8;
        side_panel.add(no_apps, c);
      }

      // Label/Button Placement for Drink
      {
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        drink_panel.add(drink_label, c);

        c.insets = new Insets(0, 0, 10, 2);
        c.gridx = 0;
        c.gridy = 1;
        drink_panel.add(sm_fountain_drink, c);

        c.gridx = 0;
        c.gridy = 2;
        drink_panel.add(md_fountain_drink, c);

        c.gridx = 0;
        c.gridy = 3;
        drink_panel.add(lg_fountain_drink, c);

        c.gridx = 0;
        c.gridy = 4;
        drink_panel.add(tea, c);

        c.gridx = 0;
        c.gridy = 5;
        drink_panel.add(water_bottle, c);

        c.gridx = 0;
        c.gridy = 6;
        drink_panel.add(kid_juice, c);

        c.gridx = 0;
        c.gridy = 7;
        drink_panel.add(no_drink, c);
      }

      // Label & JTextArea Placement for View Panel
      {
        c.insets = new Insets(5,5,5,5);
        view_panel.add(order_items_label, constraints(0,0,1, c));
        view_panel.add(order_prices_label, constraints(1,0,1, c));
        view_panel.add(order_totals_label, constraints(2,0,1, c));
        view_panel.add(order_items, constraints(0,1,1, c));
        view_panel.add(order_prices, constraints(1,1,1, c));
        view_panel.add(order_totals, constraints(2,1,1, c));
      }

      // Label & JRadio Placement for Payment Panel
      {
        c.insets = new Insets(5,5,5,5);
        payment_panel.add(payment_label, constraints(0,0,2, c));
        payment_buttons.add(radioButton1);
        payment_buttons.add(radioButton2);
        payment_panel.add(radioButton1, constraints(0,1,1, c));
        payment_panel.add(radioButton2, constraints(1,1,1, c));
        radioButton1.setSelected(true);
      }

      // Button Placement for Bottom Panel
      {
        bottom_panel.add(close_button);
        bottom_panel.add(reset_order_button);
        bottom_panel.add(place_order_button);
      }

    }

    //initialize current_item_list
    current_item_list = new HashMap<Integer, String>() {{
      put(0,  Integer.toString(TRANSACTION_ID));
      put(1,  "Sale");
      put(2,  "none"); //meal type
      put(3,  "none"); //entree 1
      put(4,  "none"); //entree 2
      put(5,  "none"); //entree 3
      put(6,  "none"); //side 1
      put(7,  "none"); //side 2
      put(8,  "none"); //drink
      put(9,  "N/A");
      put(10, get_user());
      put(11, "");
      put(12, "$0.00");
      put(13, "$0.00");
      put(14, "$0.00");
      put(15, "00:00");
      put(16, Integer.toString(ORDER_ID));
    }};

    //Vector<HashMap<Integer, String>> current_order = new Vector<HashMap<Integer, String>>();
    Vector<Double> current_price = new Vector<Double>();
    for(int i = 0; i < 15; i++){
      current_price.add(0.00);
    }

    // BUTTONS FUNCTIONALITY
    // Empower Meal/A-La-Carte Buttons
    HashMap< Integer, Vector<String> > menu_meals = menu_query("menu_meals"); // get meals data
    for (int b = 0; b < meal_alacarte_buttons.size(); b++) {
      Vector<String> meals = menu_meals.get(b+1);
      meal_alacarte_buttons.get(b).addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //update item listing and price
          current_item_list.put(2,meals.get(0));
          current_price.set(2,Double.parseDouble(meals.get(6)));

          //reset meal's contents
          //(avoids the database recieving a previously selected entree if the meal size is reduced)
          current_item_list.put(3,"none");
          current_item_list.put(4,"none");
          current_item_list.put(5,"none");
          current_item_list.put(6,"none");
          current_item_list.put(7,"none");

          //update indexing values and empty entree labels based on selection
          if(meals.get(0).contains("Bigger_Plate") || meals.get(0).contains("Family_Meal")){ 
            meal_cap = 3;
            current_item_list.put(3,"entree1");
            current_item_list.put(4,"entree2");
            current_item_list.put(5,"entree3");
            current_item_list.put(6,"side1");
            current_item_list.put(7,"side2");
          }else if(meals.get(0).contains("Plate")){ 
            meal_cap = 2;
            current_item_list.put(3,"entree1");
            current_item_list.put(4,"entree2");
            current_item_list.put(6,"side1");
            current_item_list.put(7,"side2");
          }else if(meals.get(0).contains("Bowl")){
            meal_cap=1;
            current_item_list.put(3,"entree1");
            current_item_list.put(6,"side1");
            current_item_list.put(7,"side2");
          }else if(meals.get(0).contains("A_La_Carte_Side")){
            meal_cap=0;
            current_item_list.put(6,"side1");
          }else{
            meal_cap=1;
          }
          entrees_index = 3;
          sides_index = 6;
          
          //display visual updates
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      });
    }

    // Empower Entree Buttons
    HashMap< Integer, Vector<String> > menu_entrees = menu_query("menu_entrees"); // get entree data
    for (int b = 0; b < entree_buttons.size(); b++) {
      Vector<String> entrees = menu_entrees.get(b+1);
      entree_buttons.get(b).addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(!current_item_list.get(2).contains("A_La_Carte_Side")){
            current_item_list.put(entrees_index,entrees.get(0));
            current_price.set(entrees_index,Double.parseDouble(entrees.get(6)));
            if(!current_item_list.get(2).contains("A_La_Carte_Entree")){
              entrees_index++;
              if(entrees_index > 2+meal_cap){entrees_index = 3;}
            }
          }
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      });
    }

    // Empower Sides Buttons
    HashMap< Integer, Vector<String> > menu_sides = menu_query("menu_sides"); // get sides data
    for (int b = 0; b < side_buttons.size(); b++) {
      Vector<String> sides = menu_sides.get(b+1);
      side_buttons.get(b).addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(!current_item_list.get(2).contains("A_La_Carte_Entree")){
            current_item_list.put(sides_index,sides.get(0));
            current_price.set(sides_index,Double.parseDouble(sides.get(6)));
            if(!current_item_list.get(2).contains("A_La_Carte_Side")){
              sides_index = ((sides_index + 1) % 2) + 6;
            }
          }
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      });
    }

    // Empower Appetizer Buttons
    apps_buttons.get(0).addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!current_item_list.get(2).contains("A_La_Carte_Side")){
          current_item_list.put(entrees_index,menu_sides.get(6).get(0));
          current_price.set(entrees_index,Double.parseDouble(menu_sides.get(6).get(6)));
          if(!current_item_list.get(2).contains("A_La_Carte_Entree")){
            entrees_index++;
            if(entrees_index > 2+meal_cap){entrees_index = 3;}
          }
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      }
    });
    apps_buttons.get(1).addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!current_item_list.get(2).contains("A_La_Carte_Side")){
          current_item_list.put(entrees_index,menu_sides.get(7).get(0));
          current_price.set(entrees_index,Double.parseDouble(menu_sides.get(7).get(6)));
          if(!current_item_list.get(2).contains("A_La_Carte_Entree")){
            entrees_index++;
            if(entrees_index > 2+meal_cap){entrees_index = 3;}
          }
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      }
    });

    // Empower Drink Buttons
    HashMap< Integer, Vector<String> > menu_drinks = menu_query("menu_drinks"); // get drinks data
    for (int b = 0; b < drinks_buttons.size(); b++) {
      Vector<String> drinks = menu_drinks.get(b+1);
      drinks_buttons.get(b).addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          current_item_list.put(8,drinks.get(0));
          current_price.set(8, Double.parseDouble(drinks.get(6)));
          update_text(order_items, order_prices, current_price);
          update_total(order_totals, current_price);
        }
      });
    }

    //Empower Bottom Panel Buttons
    close_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        f.dispose();
      }
    });
    reset_order_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for( Map.Entry<Integer,String> elem : current_item_list.entrySet()){
          if(elem.getKey() > 1 && elem.getKey() < 9){
            elem.setValue("none");
          }
        }
        for(Double val : current_price){
          val = 0.00;
        }
        update_text(order_items, order_prices, current_price);
        order_totals.setText(String.format("$0.00"));
      }
    });
    place_order_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        double subtotal = update_total(order_totals, current_price);
        JRadioButton payment_method = radioButton1;
        if (!radioButton1.isSelected()) payment_method = radioButton2;
        TRANSACTION_ID ++;
        ORDER_ID ++;
        add_transaction(current_item_list, payment_method.getText(), subtotal);
      }
    });

    // ADD EVERYTHING TO FRAME AND SET VISIBLE
    f.pack();
    f.add(top_panel);
    f.add(meal_panel);
    f.add(entree_panel);
    f.add(side_panel);
    f.add(drink_panel);
    f.add(view_panel);
    f.add(payment_panel);
    f.add(bottom_panel);
    f.add(temp_panel);
    f.setSize(1400, 900);
  }
}
