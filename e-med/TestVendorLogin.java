import java.sql.*;

public class TestVendorLogin {
    public static void main(String[] args) {
        System.out.println("🧪 Testing Vendor Login and Order Visibility\n");
        
        // Test vendor credentials from AuthenticationFrame
        String[][] testVendors = {
            {"vendor01", "vendor123", "Apollo Pharmacy"},
            {"vendor02", "vendor456", "MedPlus Store"},
            {"vendor03", "health123", "HealthKart Pharmacy"},
            {"vendor04", "guard456", "Guardian Pharmacy"},
            {"vendor05", "net789", "Netmeds Store"},
            {"vendor06", "onemg321", "1mg Pharmacy"},
            {"vendor07", "easy654", "Pharmeasy Store"}
        };
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            for (String[] vendor : testVendors) {
                String vendorId = vendor[0];
                String password = vendor[1];
                String expectedName = vendor[2];
                
                System.out.println("🏪 Testing Vendor: " + vendorId + " (" + expectedName + ")");
                
                // Test database authentication
                String authQuery = "SELECT sid, sname, pass FROM seller WHERE sid = ?";
                PreparedStatement authPs = conn.prepareStatement(authQuery);
                authPs.setString(1, vendorId);
                ResultSet authRs = authPs.executeQuery();
                
                if (authRs.next()) {
                    String dbPassword = authRs.getString("pass");
                    String dbName = authRs.getString("sname");
                    boolean passwordMatch = password.equals(dbPassword);
                    
                    System.out.println("   ✅ Found in database: " + dbName);
                    System.out.println("   🔑 Password match: " + passwordMatch);
                    
                    if (!passwordMatch) {
                        System.out.println("   ⚠️ Expected: " + password + ", Got: " + dbPassword);
                    }
                } else {
                    System.out.println("   ❌ NOT found in database - will use fallback authentication");
                }
                
                // Check orders for this vendor
                String orderQuery = "SELECT COUNT(*) as order_count FROM orders WHERE sid = ?";
                PreparedStatement orderPs = conn.prepareStatement(orderQuery);
                orderPs.setString(1, vendorId);
                ResultSet orderRs = orderPs.executeQuery();
                
                if (orderRs.next()) {
                    int orderCount = orderRs.getInt("order_count");
                    System.out.println("   📦 Orders assigned: " + orderCount);
                    
                    if (orderCount > 0) {
                        // Show recent orders
                        String recentQuery = "SELECT o.oid, o.uid, p.pname, COALESCE(o.status, 'Pending') as status " +
                                           "FROM orders o JOIN product p ON o.pid = p.pid " +
                                           "WHERE o.sid = ? ORDER BY o.orderdatetime DESC LIMIT 3";
                        PreparedStatement recentPs = conn.prepareStatement(recentQuery);
                        recentPs.setString(1, vendorId);
                        ResultSet recentRs = recentPs.executeQuery();
                        
                        System.out.println("   📋 Recent Orders:");
                        while (recentRs.next()) {
                            System.out.println("     - Order #" + recentRs.getInt("oid") + 
                                             " | " + recentRs.getString("pname") + 
                                             " | Status: " + recentRs.getString("status"));
                        }
                    }
                }
                
                System.out.println();
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🎯 SOLUTION:");
        System.out.println("1. Vendors should login with credentials: vendor01/vendor123, vendor02/vendor456, etc.");
        System.out.println("2. Orders are being assigned to these vendor IDs correctly");
        System.out.println("3. If vendor dashboard shows no orders, check the currentVendor variable");
        System.out.println("4. The vendor dashboard query: SELECT * FROM orders WHERE sid = 'vendorXX'");
    }
}