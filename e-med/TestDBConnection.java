import java.sql.*;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("Database connected successfully!");
            
            // Test queries
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM customer");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                System.out.println("Total Customers: " + rs1.getInt(1));
            }
            
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM seller");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                System.out.println("Total Vendors: " + rs2.getInt(1));
            }
            
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM product");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                System.out.println("Total Products: " + rs3.getInt(1));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}