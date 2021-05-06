 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;
import javax.swing.border.EmptyBorder;

// Aici ne ocupam de angajati (insert, update, delete)

public class InfoAngajati implements ActionListener {
   
   JFrame frame;
   JPanel panelButoane, panelTextbox;
   JTable table;
   JButton insert, update, delete;
   JLabel  password_label, nume_label, prenume_label, dataNasterii_label, departament_label;
   JTextField nume_text, prenume_text, dataNasterii_text, departament_text;
   JCheckBox isManagerCheck;
   DefaultTableModel model;
   private DataBaseConnection DbConn;
   private int selectedRow = 0;
   
   final static boolean shouldFill = true;
   
   InfoAngajati(DataBaseConnection DbConn) {
       this.DbConn = DbConn;
   
       frame = new JFrame("Angajati");
       frame.setLayout(new GridBagLayout());
       GridBagConstraints frameGBC = new GridBagConstraints();
       
       //tabel
       table = new JTable();
       table.setPreferredScrollableViewportSize(new Dimension(900, 600));
       table.setFillsViewportHeight(true);
       model = new DefaultTableModel(new String[]{"AngajatID", "NumeDepartament", "Nume", "Prenume", "Data Nasterii"}, 0);
       table.setModel(model);
       
       table.addMouseListener(new java.awt.event.MouseAdapter() {
           @Override
           public void mouseClicked(java.awt.event.MouseEvent evt) {
               
               //populam casutele de text cu ce e selectat din tabel
               selectedRow = table.rowAtPoint(evt.getPoint());
               
               departament_text.setText((String)model.getValueAt(selectedRow, 1));
               nume_text.setText((String)model.getValueAt(selectedRow, 2));
               prenume_text.setText((String)model.getValueAt(selectedRow, 3));
               dataNasterii_text.setText((String)model.getValueAt(selectedRow, 4));
               
               isManagerCheck.setSelected(isManager((String)model.getValueAt(selectedRow, 0)));
               
               
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
       dataNasterii_label = new JLabel();
       dataNasterii_label.setText("Data Nasterii (YYYY-MM-DD):");
       dataNasterii_text = new JTextField(10);
       departament_label = new JLabel();
       departament_label.setText("Departament: ");
       departament_text = new JTextField(10);
       isManagerCheck = new JCheckBox("este Manager");
  
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
       panelTextbox.add(dataNasterii_label, c);
       c.gridx = 1;
       c.gridy = 2;
       panelTextbox.add(dataNasterii_text, c);
       c.gridx = 0;
       c.gridy = 3;
       panelTextbox.add(departament_label, c);
       c.gridx = 1;
       panelTextbox.add(departament_text, c);
       c.gridy = 4;
       panelTextbox.add(isManagerCheck, c);
       
       frame.getContentPane().add(panelTextbox, frameGBC);
       
       //butoane
       insert = new JButton("Insert");
       update = new JButton("Update");
       delete = new JButton("Delete");
       
       panelButoane = new JPanel();
       panelButoane.add(insert);
       panelButoane.add(update);
       panelButoane.add(delete);
       frameGBC.gridx = 1;
       frameGBC.gridy = 1;
       frame.getContentPane().add(panelButoane, frameGBC);
       update.addActionListener(this);
       delete.addActionListener(this);
       insert.addActionListener(this);
       
       
       frame.setSize(300, frame.getHeight());
       frame.pack();
       frame.setVisible(true);
       this.updateTable();
       
   }
   
   public void updateTable() {
       try {
           model = new DefaultTableModel(new String[]{"AngajatID", "NumeDepartament", "Nume", "Prenume", "Data Nasterii"}, 0);
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT a.AngajatId, d.NumeDepartament, a.Nume, a.Prenume, a.DataNasterii FROM Angajat a JOIN Departament d ON a.DepartamentID = d.DepartamentID";
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()) {
               String d = rs.getString("AngajatID");
               String e = rs.getString("NumeDepartament");
               String f = rs.getString("Nume");
               String g = rs.getString("Prenume");
               String h = rs.getString("DataNasterii");
               model.addRow(new Object[]{d, e, f, g, h});
           }
       } catch(SQLException e) {
           e.printStackTrace();
       }
       table.setModel(model);
   }
    
   @Override
   public void actionPerformed(ActionEvent ae) {
       
       String angajatID = (String)model.getValueAt(selectedRow, 0);
       String nume = nume_text.getText().trim();
       String prenume = prenume_text.getText().trim();
       String dataNasterii = dataNasterii_text.getText().trim();
       String departament = departament_text.getText().trim();
       
       if(ae.getSource() == update) {
           try {
               // update tabela
               Statement stmt = DbConn.getConnection().createStatement();
               String sql = "UPDATE Angajat SET Nume = '" + nume + "', Prenume = '" + prenume + "', DataNasterii = '" + dataNasterii + "' " +
                            "WHERE AngajatID = " + angajatID;
               System.out.println(sql);
               stmt.executeUpdate(sql);
               // update managers if necessary
               if(isManagerCheck.isSelected() == true && isManager(angajatID) == false) {
                   // if checkbox is selected si angajatul nu este manager atunci il facem manager
                   String addManager = "INSERT INTO Manager(ManagerID,Nume,Prenume) VALUES("+ angajatID + ",'" + nume + "','" + prenume + "')";
                   System.out.println(addManager);
                   stmt.executeUpdate(addManager);
               } else if(isManagerCheck.isSelected() == false && isManager(angajatID) == true) {
                   // if checkbox is not selected si angajatul este manager atunci il scoatem de la manageri
                   String removeManager = "DELETE FROM Manager WHERE ManagerID = " + angajatID;
                   System.out.println(removeManager);
                   stmt.executeUpdate(removeManager);
               }
            } catch(SQLException e) {
                e.printStackTrace();
            }       
       }
       
       if(ae.getSource() == delete) {
            try {
               // stergere angajati
               Statement stmt = DbConn.getConnection().createStatement();
               String sql = "DELETE FROM Angajat WHERE AngajatID = " + angajatID;
               System.out.println(sql);
               stmt.executeUpdate(sql);
            } catch(SQLException e) {
                e.printStackTrace();
            }        
       }
       
       if(ae.getSource() == insert) {
            try {
                // adaugare angajat nou
                Statement stmt = DbConn.getConnection().createStatement();
                String sql1 = "SELECT DepartamentID FROM Departament WHERE NumeDepartament = '" + departament + "'";
                System.out.println(sql1);
                ResultSet rs = stmt.executeQuery(sql1);
                rs.next();
                String departamentID = rs.getString("DepartamentID");
                String sql = "INSERT INTO Angajat(DepartamentID,Nume,Prenume,DataNasterii) VALUES("+ departamentID + ",'" + nume + "','" + prenume + "','" + dataNasterii + "')";
                System.out.println(sql);
                stmt.executeUpdate(sql);
            } catch(SQLException e) {
                e.printStackTrace();
            }       
       }
       this.updateTable();
   }
   
   public boolean isManager(String AngajatID) {
       try {
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT ManagerID FROM Manager WHERE ManagerID = '" + AngajatID + "'";
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           
           return rs.next();
           
       } catch(SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
}