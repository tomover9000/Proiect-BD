import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {
    
   JPanel panel;
   JLabel user_label, password_label;
   JTextField userName_text;
   JPasswordField password_text;
   JButton login, register;
   private DataBaseConnection DbConn;
   
   LoginPage() {
       
      this.DbConn = new DataBaseConnection("jdbc:sqlserver://localhost:1400;databaseName=PontajElectronic;", "admin", "password");
       
      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      
      // Password Label
      password_label = new JLabel();
      password_label.setText("Password :");
      password_text = new JPasswordField();
      
      // Submit
      login = new JButton("Login");
      register = new JButton("Register");
      panel = new JPanel(new GridLayout(3, 2, 10, 10));
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      panel.add(login);
      panel.add(register);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Adding the listeners to components..
      login.addActionListener(this);
      register.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Please Login Here !");
      setSize(350,250);
      setVisible(true);
      
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
       
      String userName = userName_text.getText();
      String password = password_text.getText();
      
      
      if(ae.getSource() == login) {
          if(this.checkLogin(userName, password)) {
              JOptionPane.showMessageDialog(null, "Logged in");
              new SelectTablePage(this.DbConn);
          } else {
              JOptionPane.showMessageDialog(null, "user/pass gresit");
          }
      }
      
      if(ae.getSource() == register) {
          new RegisterPage(this.DbConn);
      }
   }
   
   
   public boolean checkLogin(String user, String pass) {   
       // Facem cererea pentru user si parola
       try {
           Statement stmt = DbConn.getConnection().createStatement();
           String sql = "SELECT username, password FROM Angajat where username='" + user + "' and password='" + pass + "'";
           System.out.println("Checking user and pass..");
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()) {
               String currentUser = rs.getString("username").trim();
               String currentPassword = rs.getString("password").trim();
               if(currentUser.equalsIgnoreCase(user) && currentPassword.equalsIgnoreCase(pass)) {
                   System.out.println("Logged in");
                   return true;
               }
            }
       } catch(SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
   
}