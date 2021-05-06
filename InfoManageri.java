 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;
import javax.swing.border.EmptyBorder;

// Aici putem modifica sau sterge manageri

public class InfoManageri implements ActionListener {
   
   JFrame frame;
   JPanel panelButoane, panelTextbox;
   JTable table;
   JButton update, delete, sumaOre, manDep;
   JLabel nume_label, prenume_label, departament_label;
   JTextField nume_text, prenume_text, departament_text;
   DefaultTableModel model;
   private DataBaseConnection DbConn;
   private int selectedRow = 0;
   final static boolean shouldFill = true;
   
   InfoManageri(DataBaseConnection DbConn) {
       this.DbConn = DbConn;
       frame = new JFrame("Manageri/Departamente");
       frame.setLayout(new GridBagLayout());
       GridBagConstraints frameGBC = new GridBagConstraints();
       
       //tabel
       table = new JTable();
       table.setPreferredScrollableViewportSize(new Dimension(600, 300));
       table.setFillsViewportHeight(true);
       model = new DefaultTableModel(new String[]{"ManagerID", "NumeDepartament", "Nume", "Prenume"}, 0);
       table.setModel(model);
       
       table.addMouseListener(new java.awt.event.MouseAdapter() {
           @Override
           public void mouseClicked(java.awt.event.MouseEvent evt) {
               
               //populam casutele de text cu ce e selectat din tabel
               selectedRow = table.rowAtPoint(evt.getPoint());
               
               departament_text.setText((String)model.getValueAt(selectedRow, 1));
               nume_text.setText((String)model.getValueAt(selectedRow, 2));
               prenume_text.setText((String)model.getValueAt(selectedRow, 3));

           }
        });
       frameGBC.gridx = 0;
       frameGBC.gridy = 0;
       frameGBC.fill = GridBagConstraints.HORIZONTAL;
       frameGBC.weightx = 0;
       frame.getContentPane().add(new JScrollPane(table), frameGBC);
  
       //textbox si labels
       panelTextbox = new JPanel(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       
       //setting the textboxes and labels
       nume_label = new JLabel();
       nume_label.setText("Nume: ");
       nume_text = new JTextField(10);
       prenume_label = new JLabel();
       prenume_label.setText("Prenume: ");
       prenume_text = new JTextField(10);
       departament_label = new JLabel();
       departament_label.setText("Departament: ");
       departament_text = new JTextField(10);
  
       frameGBC.gridx = 1;
       frameGBC.gridy = 0;
       frameGBC.ipadx = 5;
       c.gridx = 0;
       c.gridy = 0;
       panelTextbox.add(nume_label, c);
       c.gridx = 1;
       c.gridy = 0;
       panelTextbox.add(nume_text, c);
       c.gridx = 0;
       c.gridy = 1;
       panelTextbox.add(prenume_label, c);
       c.gridx = 1;
       c.gridy = 1;
       panelTextbox.add(prenume_text, c);
       c.gridx = 0;
       c.gridy = 2;
       panelTextbox.add(departament_label, c);
       c.gridx = 1;
       panelTextbox.add(departament_text, c);
       frame.getContentPane().add(panelTextbox, frameGBC);
       
       //butoane
       update = new JButton("Update");
       delete = new JButton("Delete");
       sumaOre = new JButton("SumaOre");
       manDep = new JButton("Mangeri/Departamente");
       panelButoane = new JPanel();
       panelButoane.add(update);
       panelButoane.add(delete);
       panelButoane.add(sumaOre);
       panelButoane.add(manDep);
       
       frameGBC.gridx = 1;
       frameGBC.gridy = 1;
       frame.getContentPane().add(panelButoane, frameGBC);
       update.addActionListener(this);
       delete.addActionListener(this);
       sumaOre.addActionListener(this);
       manDep.addActionListener(this);
       
       frame.setSize(300, frame.getHeight());
       frame.pack();
       frame.setVisible(true);
       this.updateTable();
       
   }
   
   public void updateTable() {
       try {
           model = new DefaultTableModel(new String[]{"ManagerID", "NumeDepartament", "Nume", "Prenume"}, 0);
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT m.ManagerID, d.NumeDepartament, m.Nume, m.Prenume FROM Manager m JOIN Departament d ON d.ManagerID = m.ManagerID";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String d = rs.getString("ManagerID");
               String e = rs.getString("NumeDepartament");
               String f = rs.getString("Nume");
               String g = rs.getString("Prenume");
               model.addRow(new Object[]{d, e, f, g});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }
       table.setModel(model);
   }
    
   @Override
   public void actionPerformed(ActionEvent ae) {
       
       String nume = nume_text.getText().trim();
       String prenume = prenume_text.getText().trim();
       String departament = departament_text.getText().trim();
       
       if(ae.getSource() == update) {
           try {
               // update tabela
               String managerID = (String)model.getValueAt(selectedRow, 0);
               Statement stmt = DbConn.getConnection().createStatement();
               String sql = "UPDATE Manager SET Nume = '" + nume + "', Prenume = '" + prenume + "' " +
                            "WHERE ManagerID = " + managerID;
               System.out.println(sql);
               stmt.executeUpdate(sql);
            } catch(SQLException e) {
                e.printStackTrace();
            }
            this.updateTable();
       }
       
       if(ae.getSource() == delete) {
            try {
               // stergere angajati
               String managerID = (String)model.getValueAt(selectedRow, 0);
               Statement stmt = DbConn.getConnection().createStatement();
               String sql = "DELETE FROM Manager WHERE ManagerID = " + managerID;
               System.out.println(sql);
               stmt.executeUpdate(sql);
            } catch(SQLException e) {
                e.printStackTrace();
            }      
            this.updateTable();
            // decrementam randul pentru ca un rand a disparut
            selectedRow -= 1;
       }
       
       if(ae.getSource() == sumaOre) {
           this.sumaOrelor();
       }
       
       if(ae.getSource() == manDep) {
           this.updateTable();
       }
       
   }
   
   public void sumaOrelor() {
       DefaultTableModel model1 = new DefaultTableModel(new String[]{"NumeDepartament", "OreLucrate"}, 0);
       
       // we make the request
       try {  
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT d.NumeDepartament, SUM(o.OreLucrate) as OreLucrate FROM Departament d JOIN Angajat a ON d.DepartamentID = a.DepartamentID JOIN OreAngajat o ON a.AngajatID = o.AngajatID WHERE a.AngajatID IN (SELECT AngajatID FROM OreAngajat WHERE Penalizare = 0) GROUP BY d.NumeDepartament";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String d = rs.getString("NumeDepartament");
               String e = rs.getString("OreLucrate");
               model1.addRow(new Object[]{d, e});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }  
       this.table.setModel(model1);
   }
}