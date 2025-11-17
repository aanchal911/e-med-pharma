import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt connection
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection successful!");
                System.out.println("Connected to: " + URL);
                System.out.println("Database: drugdatabase");
                
                // Test a simple query
                try {
                    var stmt = conn.createStatement();
                    var rs = stmt.executeQuery("SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = 'drugdatabase'");
                    if (rs.next()) {
                        System.out.println("Number of tables in database: " + rs.getInt("table_count"));
                    }
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("⚠️ Database exists but query failed: " + e.getMessage());
                }
                
                conn.close();
            } else {
                System.out.println("❌ Connection is null or closed");
            }
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL driver not found: " + e.getMessage());
            System.out.println("Make sure mysql-connector-j-9.4.0.jar is in classpath");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            System.out.println("Possible issues:");
            System.out.println("- MySQL server not running");
            System.out.println("- Database 'drugdatabase' doesn't exist");
            System.out.println("- Wrong username/password");
            System.out.println("- MySQL not accessible on localhost:3306");
        }
    }
}