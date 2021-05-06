import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class Events implements ActionListener {
   
   JFrame frame;
   JTable table;
   DefaultTableModel model;
   JLabel date_label;
   JTextField date_text;
   JPanel bottomPanel;
   JButton early, late, allEvents;
   private DataBaseConnection DbConn;
   private int selectedRow = 0;
   final static boolean shouldFill = true;
   
   Events(DataBaseConnection DbConn) {
       this.DbConn = DbConn;
   
       frame = new JFrame("Events");
       frame.setLayout(new BorderLayout());
       //frameGBC.insets = new Insets(5, 10, 5, 10);
       
       //tabel
       table = new JTable();
       table.setPreferredScrollableViewportSize(new Dimension(600, 300));
       table.setFillsViewportHeight(true);
       frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
       
       bottomPanel = new JPanel();
       early = new JButton("Early People");
       late = new JButton ("Late Workers");
       allEvents = new JButton("Show All Events");
       date_label = new JLabel();
       date_label.setText("Data (YYYY/MM/DD)");
       date_text = new JTextField();
       date_text.setText("2020/01/12");
       bottomPanel.add(date_label);
       bottomPanel.add(date_text);
       bottomPanel.add(early);
       bottomPanel.add(late);
       bottomPanel.add(allEvents);
       frame.getContentPane().add(bottomPanel, BorderLayout.PAGE_END);
       early.addActionListener(this);
       late.addActionListener(this);
       allEvents.addActionListener(this);
       
       frame.setSize(300, frame.getHeight());
       frame.pack();
       frame.setVisible(true);
       this.updateTable(date_text.getText().trim());
       
       
   }
   
   public void updateTable(String date) {
       model = new DefaultTableModel(new String[]{"Ora", "NumePoarta", "Iesire", "Nume", "Prenume"}, 0);
       try {
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT e.Time, p.NumePoarta, e.Exited, a.Nume, a.Prenume FROM Events e JOIN Angajat a ON e.AngajatID = a.AngajatID JOIN Poarta p ON e.PoartaID = p.PoartaID WHERE e.Date = '" + date + "'";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String e = rs.getString("Time").substring(0,8);
               String f = rs.getString("NumePoarta");
               String g = rs.getString("Exited");
               String h = rs.getString("Nume");
               String i = rs.getString("Prenume");
               model.addRow(new Object[]{e, f, g, h, i});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }
       table.setModel(model);
   }
   
   public void showEarlyPeople(String date) {
       DefaultTableModel model1 = new DefaultTableModel(new String[]{"Nume", "Prenume"}, 0);
       
       try {  
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT a.Nume, a.Prenume FROM Angajat a WHERE a.AngajatID IN (SELECT e.AngajatID FROM Events e WHERE e.Date = '" + date + "' AND e.Time < '08:00' AND e.PoartaID = 1)";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String d = rs.getString("Nume");
               String e = rs.getString("Prenume");
               model1.addRow(new Object[]{d, e});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }  
       this.table.setModel(model1);
   }
   
   public void showLateWorkers(String date) {
       DefaultTableModel model1 = new DefaultTableModel(new String[]{"Nume", "Prenume"}, 0);
       try {  
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT a.Nume, a.Prenume FROM Angajat a WHERE a.AngajatID IN (SELECT e.AngajatID FROM Events e WHERE e.Date = '" + date + "' AND e.Time > '18:00' AND e.PoartaID = 1)";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String d = rs.getString("Nume");
               String e = rs.getString("Prenume");
               model1.addRow(new Object[]{d, e});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }  
       this.table.setModel(model1);
   }
    
   @Override
   public void actionPerformed(ActionEvent ae) {
       
       // verificam ce buton a fost apasat
       if(ae.getSource() == early) {
           showEarlyPeople(date_text.getText().trim());
       }
       
       if(ae.getSource() == allEvents) {
           updateTable(date_text.getText().trim());
       }
       
       if(ae.getSource() == late) {
           showLateWorkers(date_text.getText().trim());
       }
       
       
   }
}