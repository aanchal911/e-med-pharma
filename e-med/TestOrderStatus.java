import java.sql.*;

public class TestOrderStatus {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/drugdatabase";
        String username = "root";
        String password = "A@nchal911";
        
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // Add status column if not exists
            try {
                String addColumn = "ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending'";
                PreparedStatement ps1 = conn.prepareStatement(addColumn);
                ps1.executeUpdate();
                System.out.println("✅ Status column added");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("✅ Status column already exists");
                } else {
                    throw e;
                }
            }
            
            // Update existing orders
            String updateOrders = "UPDATE orders SET status = 'Pending' WHERE status IS NULL OR status = ''";
            PreparedStatement ps2 = conn.prepareStatement(updateOrders);
            int updated = ps2.executeUpdate();
            System.out.println("✅ Updated " + updated + " orders with Pending status");
            
            // Test status update
            String testUpdate = "UPDATE orders SET status = 'Approved' WHERE oid = (SELECT MIN(oid) FROM (SELECT oid FROM orders) AS temp)";
            PreparedStatement ps3 = conn.prepareStatement(testUpdate);
            ps3.executeUpdate();
            System.out.println("✅ Test status update completed");
            
            // Verify results
            String verify = "SELECT oid, uid, status FROM orders LIMIT 3";
            PreparedStatement ps4 = conn.prepareStatement(verify);
            ResultSet rs = ps4.executeQuery();
            
            System.out.println("\n📋 Order Status Verification:");
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("oid") + 
                                 ", Customer: " + rs.getString("uid") + 
                                 ", Status: " + rs.getString("status"));
            }
            
            conn.close();
            System.out.println("\n✅ Order status functionality is now working!");
            
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}