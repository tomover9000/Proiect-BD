import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.sql.*;

// Aici vom selecta tabelele de care ne ocupam

public class SelectTablePage implements ActionListener {
    
    JFrame frame;
    JButton angajati, manageri, evenimente;
    private DataBaseConnection DbConn;
    
    SelectTablePage(DataBaseConnection DbConn) {
        this.DbConn = DbConn;
        
        frame = new JFrame("Alegere Tabela");
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(3, 1, 20, 20));
        
        
        angajati = new JButton("Angajati");
        manageri = new JButton("Manageri");
        evenimente = new JButton("Evenimente");
        
        frame.add(angajati);
        frame.add(manageri);
        frame.add(evenimente);
        
        angajati.addActionListener(this);
        manageri.addActionListener(this);
        evenimente.addActionListener(this);
       
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == angajati) {
            new InfoAngajati(this.DbConn);
        }
        if(ae.getSource() == manageri) {
            new InfoManageri(this.DbConn);
        }
        if(ae.getSource() == evenimente) {
            new Events(this.DbConn);
        }
    }
}