import java.sql.*;

public class VerifyOrderFlow {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "A@nchal911";
    
    public static void main(String[] args) {
        System.out.println("🔍 Order Flow Verification");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
            // Test placing an order
            int orderId = placeTestOrder(conn);
            
            // Verify vendor can see it
            verifyVendorOrder(conn, orderId);
            
            // Test approval
            testApproval(conn, orderId);
            
            conn.close();
            System.out.println("✅ Order flow working correctly!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    private static int placeTestOrder(Connection conn) throws SQLException {
        String query = "INSERT INTO orders (pid, sid, uid, quantity, price, status) VALUES ('MED001', 'vendor01', 'aanchal01', 1, 25, 'Pending')";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int orderId = rs.getInt(1);
        System.out.println("✅ Test order placed: " + orderId);
        return orderId;
    }
    
    private static void verifyVendorOrder(Connection conn, int orderId) throws SQLException {
        String query = "SELECT * FROM orders WHERE oid = ? AND sid = 'vendor01'";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            System.out.println("✅ Vendor can see order: " + orderId);
        } else {
            throw new SQLException("Vendor cannot see order");
        }
    }
    
    private static void testApproval(Connection conn, int orderId) throws SQLException {
        String query = "UPDATE orders SET status = 'Approved' WHERE oid = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, orderId);
        ps.executeUpdate();
        System.out.println("✅ Order approved successfully");
    }
}