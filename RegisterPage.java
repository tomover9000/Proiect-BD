import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;


public class RegisterPage implements ActionListener {
    
   JFrame frame;
   JLabel user_label, password_label, nume_label, prenume_label, dataNasterii_label, message;
   JTextField userName_text, nume_text, prenume_text, dataNasterii_text;
   JPasswordField password_text;
   JButton register;
   private DataBaseConnection DbConn;
   
   
   RegisterPage(DataBaseConnection DbConn) {
       
      this.DbConn = DbConn;
      
      this.frame = new JFrame("Register");
      
      frame.setSize(900, 600);
      
      // Asta e doar sa impinga registerul in dreapta in layout
      message = new JLabel();
       
      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      
      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      
      // Nume Label
      nume_label = new JLabel();
      nume_label.setText("Nume :");
      nume_text = new JTextField();
      
      // Prenume Label
      prenume_label = new JLabel();
      prenume_label.setText("Prenume :");
      prenume_text = new JTextField();
      
      // Data Nasterii text
      dataNasterii_label = new JLabel();
      dataNasterii_label.setText("Data Nasterii (YYYY-MM-DD) :");
      dataNasterii_text = new JTextField();
      
      
      // Submit
      register = new JButton("Register");
      frame.setLayout(new GridLayout(6, 2, 10, 10));
      frame.add(user_label);
      frame.add(userName_text);
      frame.add(password_label);
      frame.add(password_text);
      frame.add(nume_label);
      frame.add(nume_text);
      frame.add(prenume_label);
      frame.add(prenume_text);
      frame.add(dataNasterii_label);
      frame.add(dataNasterii_text);
      frame.add(message);
      frame.add(register);
      
      // Adding the listeners to components..
      register.addActionListener(this);
      //add(frame, BorderLayout.CENTER);
      frame.setTitle("Please Register Here !");
      frame.setVisible(true);
      
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
       
      String userName = userName_text.getText();
      String password = password_text.getText();
      String nume = nume_text.getText();
      String prenume = prenume_text.getText();
      String dataNasterii = dataNasterii_text.getText();
      
      if(this.addUser(userName, password, nume, prenume, dataNasterii)) {
          JOptionPane.showMessageDialog(null, "Registered");
      } else {
          JOptionPane.showMessageDialog(null, "User already exists");
      }
      
   }
   
   public boolean addUser(String user, String pass, String nume, String prenume, String dataNasterii) {
       
       // check if user already exists
       try {
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT username, password FROM Angajat where username='" + user + "'";
           ResultSet rs = stmt.executeQuery(sql);
           if(rs.next() != false) {
               return false;
           }
        } catch(SQLException ex) {
           ex.printStackTrace();
        }
        
       try {
           // adaugare angajat nou
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "INSERT INTO Angajat(username,password,Nume,Prenume,DataNasterii) VALUES('" + user + "','" + pass + "','" + nume + "','" + prenume + "','" + dataNasterii + "')";
           System.out.println("Adding new user..");
           stmt.executeUpdate(sql);
           return true;
        } catch(SQLException e) {
           e.printStackTrace();
        }
        return false;
   }
   
    
}