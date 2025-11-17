import java.sql.*;

public class UpdateOrderStatus {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/drugdatabase";
        String username = "root";
        String password = "A@nchal911";
        
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // First, let's see current orders
            String checkOrders = "SELECT oid, uid, status FROM orders LIMIT 5";
            PreparedStatement ps1 = conn.prepareStatement(checkOrders);
            ResultSet rs = ps1.executeQuery();
            
            System.out.println("Current Orders Status:");
            while (rs.next()) {
                System.out.println("Order " + rs.getInt("oid") + " - Customer: " + rs.getString("uid") + " - Status: " + rs.getString("status"));
            }
            
            // Update some orders to different statuses for testing
            String updateApproved = "UPDATE orders SET status = 'Approved' WHERE oid IN (SELECT oid FROM (SELECT oid FROM orders LIMIT 2) AS temp)";
            PreparedStatement ps2 = conn.prepareStatement(updateApproved);
            int updated = ps2.executeUpdate();
            System.out.println("\nUpdated " + updated + " orders to 'Approved' status");
            
            // Update one to Dispatched
            String updateDispatched = "UPDATE orders SET status = 'Dispatched' WHERE oid = (SELECT oid FROM (SELECT oid FROM orders LIMIT 1 OFFSET 2) AS temp)";
            PreparedStatement ps3 = conn.prepareStatement(updateDispatched);
            ps3.executeUpdate();
            System.out.println("Updated 1 order to 'Dispatched' status");
            
            // Check updated status
            System.out.println("\nUpdated Orders Status:");
            PreparedStatement ps4 = conn.prepareStatement(checkOrders);
            ResultSet rs2 = ps4.executeQuery();
            while (rs2.next()) {
                System.out.println("Order " + rs2.getInt("oid") + " - Customer: " + rs2.getString("uid") + " - Status: " + rs2.getString("status"));
            }
            
            conn.close();
            System.out.println("\n✅ Order statuses updated for testing!");
            
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}