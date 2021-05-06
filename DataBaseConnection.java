import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    
    // url, user si parola
    // tinem minte userul si parola
    // acest tip de obiect va fi folosit pentru a mentine conexiunea la baza de date
    private String dbURL;
    private String user;
    private String pass;
    private Connection conn = null;
    
    DataBaseConnection(String dbURL, String user, String pass) {
 
        try {
            
            this.dbURL = dbURL;
            this.user = user;
            this.pass = pass;
            conn = DriverManager.getConnection(dbURL, user, pass);
            
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
 
        } catch(SQLException ex) {
            ex.printStackTrace();
        
        }
    }
    
    public Connection getConnection() {
        return this.conn;
    }
}