import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout.*;
import javax.swing.BorderFactory.*;
import javax.swing.border.*;

import java.util.*;

public class Restock_Report{
    Vector<String> results = new Vector<>();

    public Restock_Report() throws BadLocationException{
      //Add visual elements
      JFrame f = new JFrame("Restock Report");
      //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      //Building the connection
      Connection conn = null;
      try {
          Class.forName("org.postgresql.Driver");
          conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_theta",
          "csce315331_theta_master", "3NHS");
      } catch (Exception a) {
          a.printStackTrace();
          System.err.println(a.getClass().getName()+": "+a.getMessage());
          System.exit(0);
          }
      try{
          //Gather info for perishable item
          //create a statement object
          Statement stmt1 = conn.createStatement();
          //create an SQL statement
          String sqlStatement1 = "SELECT name FROM perishable WHERE stock < reorder;";
          //send statement to DBMS
          ResultSet result1 = stmt1.executeQuery(sqlStatement1);
          while (result1.next()) {
            results.add(result1.getString("name"));
          }
        } catch (Exception b){
          JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }

      //closing the connection
      try {
        conn.close();
      } catch(Exception c) {
        JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
      }

      //Set text pane with styles
      JTextPane text_pane = new JTextPane();
      SimpleAttributeSet att_set = new SimpleAttributeSet();

      text_pane.setCharacterAttributes(att_set, true);
      text_pane.setFont(new Font("Verdana", Font.PLAIN, 16));

      Document doc = text_pane.getStyledDocument();
      for (int i = 0; i < results.size(); i++){
        doc.insertString(doc.getLength(), results.elementAt(i)+"\n", att_set);
      }

      //Need scroll pane to display text pane
      JScrollPane scrollPane = new JScrollPane(text_pane);
      f.getContentPane().add(scrollPane, BorderLayout.CENTER);
      
      Color lightRed = new Color(252, 217, 217);
      text_pane.setBackground(lightRed);
      scrollPane.setBackground(lightRed);
      f.setSize(400, 300);
      f.setVisible(true);
    }

    /*//Main function for testing and debugging
    public static void main(String args[]) throws BadLocationException{
      Restock_Report report = new Restock_Report();
    }*/
}