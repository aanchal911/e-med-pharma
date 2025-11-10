import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("Testing MySQL Database Connection...");
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("✓ MySQL Driver loaded successfully");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            System.out.println("✓ Database connection successful");
            
            // Test customer table
            System.out.println("\n--- Testing Customer Table ---");
            PreparedStatement ps1 = conn.prepareStatement("SELECT uid, name, email FROM customer LIMIT 3");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                System.out.println("Customer: " + rs1.getString("uid") + " - " + rs1.getString("name"));
            }
            
            // Test seller table
            System.out.println("\n--- Testing Seller Table ---");
            PreparedStatement ps2 = conn.prepareStatement("SELECT sid, sname FROM seller LIMIT 3");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                System.out.println("Seller: " + rs2.getString("sid") + " - " + rs2.getString("sname"));
            }
            
            // Test product table
            System.out.println("\n--- Testing Product Table ---");
            PreparedStatement ps3 = conn.prepareStatement("SELECT pid, pname, manufacturer, price FROM product LIMIT 5");
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                System.out.println("Product: " + rs3.getString("pname") + " by " + rs3.getString("manufacturer") + " - Rs." + rs3.getDouble("price"));
            }
            
            // Test inventory table
            System.out.println("\n--- Testing Inventory Table ---");
            PreparedStatement ps4 = conn.prepareStatement("SELECT i.pid, i.pname, i.sid, i.quantity FROM inventory i LIMIT 5");
            ResultSet rs4 = ps4.executeQuery();
            while (rs4.next()) {
                System.out.println("Inventory: " + rs4.getString("pname") + " - Vendor: " + rs4.getString("sid") + " - Stock: " + rs4.getInt("quantity"));
            }
            
            // Test orders table
            System.out.println("\n--- Testing Orders Table ---");
            PreparedStatement ps5 = conn.prepareStatement("SELECT oid, uid, pid, quantity, price FROM orders LIMIT 3");
            ResultSet rs5 = ps5.executeQuery();
            while (rs5.next()) {
                System.out.println("Order: " + rs5.getInt("oid") + " - Customer: " + rs5.getString("uid") + " - Product: " + rs5.getString("pid"));
            }
            
            conn.close();
            System.out.println("\n✓ All database tests completed successfully!");
            System.out.println("✓ MySQL database is working properly for e-MEDpharma");
            
        } catch (Exception e) {
            System.out.println("✗ Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}