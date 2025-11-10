import java.sql.*;

public class CheckAndCreateVendors {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Check existing vendors
            System.out.println("=== EXISTING VENDORS ===");
            String selectQuery = "SELECT seller_id, seller_name, password FROM seller";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            ResultSet rs = selectStmt.executeQuery();
            
            int vendorCount = 0;
            while (rs.next()) {
                vendorCount++;
                System.out.println("ID: " + rs.getString("seller_id") + 
                                 ", Name: " + rs.getString("seller_name") + 
                                 ", Password: " + rs.getString("password"));
            }
            
            System.out.println("\nTotal vendors found: " + vendorCount);
            
            if (vendorCount == 0) {
                System.out.println("\n=== CREATING DEFAULT VENDORS ===");
                
                // Create vendor01
                String insertQuery = "INSERT INTO seller (seller_id, seller_name, password) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                
                insertStmt.setString(1, "vendor01");
                insertStmt.setString(2, "Apollo Pharmacy");
                insertStmt.setString(3, "vendor123");
                insertStmt.executeUpdate();
                System.out.println("Created vendor01 - Apollo Pharmacy");
                
                // Create vendor02
                insertStmt.setString(1, "vendor02");
                insertStmt.setString(2, "MedPlus Store");
                insertStmt.setString(3, "vendor456");
                insertStmt.executeUpdate();
                System.out.println("Created vendor02 - MedPlus Store");
                
                System.out.println("\nDefault vendors created successfully!");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}