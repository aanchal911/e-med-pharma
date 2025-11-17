import java.sql.*;

public class VendorOrderCheck {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("🔍 Checking vendor order visibility...\n");
            
            // Check orders for each vendor
            String[] vendors = {"vendor01", "vendor02", "vendor03", "vendor04", "vendor05", "vendor06", "vendor07"};
            
            for (String vendorId : vendors) {
                System.out.println("🏪 Vendor: " + vendorId);
                
                // Get vendor name
                PreparedStatement vendorQuery = conn.prepareStatement("SELECT sname FROM seller WHERE sid = ?");
                vendorQuery.setString(1, vendorId);
                ResultSet vendorResult = vendorQuery.executeQuery();
                String vendorName = vendorResult.next() ? vendorResult.getString("sname") : "Unknown";
                System.out.println("   Name: " + vendorName);
                
                // Check orders for this vendor
                String orderQuery = "SELECT o.oid, o.uid, p.pname, o.quantity, o.price, o.orderdatetime, " +
                                   "COALESCE(o.status, 'Pending') as status " +
                                   "FROM orders o JOIN product p ON o.pid = p.pid " +
                                   "WHERE o.sid = ? ORDER BY o.orderdatetime DESC LIMIT 5";
                
                PreparedStatement orderPs = conn.prepareStatement(orderQuery);
                orderPs.setString(1, vendorId);
                ResultSet orderRs = orderPs.executeQuery();
                
                int orderCount = 0;
                while (orderRs.next()) {
                    orderCount++;
                    if (orderCount == 1) {
                        System.out.println("   📦 Recent Orders:");
                    }
                    System.out.println("     - Order #" + orderRs.getInt("oid") + 
                                     " | Customer: " + orderRs.getString("uid") + 
                                     " | Product: " + orderRs.getString("pname") + 
                                     " | Status: " + orderRs.getString("status") + 
                                     " | Date: " + orderRs.getString("orderdatetime"));
                }
                
                if (orderCount == 0) {
                    System.out.println("   ❌ No orders found for this vendor");
                } else {
                    System.out.println("   ✅ Total orders found: " + orderCount);
                }
                
                System.out.println();
            }
            
            // Check orders with NULL vendor
            System.out.println("🔍 Orders with NULL vendor:");
            PreparedStatement nullVendorQuery = conn.prepareStatement(
                "SELECT o.oid, o.uid, p.pname, o.quantity, o.price, o.orderdatetime " +
                "FROM orders o JOIN product p ON o.pid = p.pid " +
                "WHERE o.sid IS NULL ORDER BY o.orderdatetime DESC LIMIT 10"
            );
            ResultSet nullOrders = nullVendorQuery.executeQuery();
            
            int nullCount = 0;
            while (nullOrders.next()) {
                nullCount++;
                System.out.println("  - Order #" + nullOrders.getInt("oid") + 
                                 " | Customer: " + nullOrders.getString("uid") + 
                                 " | Product: " + nullOrders.getString("pname"));
            }
            
            if (nullCount == 0) {
                System.out.println("  ✅ No orders with NULL vendor found");
            } else {
                System.out.println("  ⚠️ Found " + nullCount + " orders with NULL vendor");
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}