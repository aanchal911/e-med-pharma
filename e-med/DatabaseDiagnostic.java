import java.sql.*;

public class DatabaseDiagnostic {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("✅ Database connection successful!");
            
            // Check tables
            System.out.println("\n📋 Database Tables:");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables("drugdatabase", null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
            }
            
            // Check orders table structure
            System.out.println("\n📊 Orders Table Structure:");
            ResultSet columns = metaData.getColumns("drugdatabase", null, "orders", "%");
            while (columns.next()) {
                System.out.println("  - " + columns.getString("COLUMN_NAME") + " (" + columns.getString("TYPE_NAME") + ")");
            }
            
            // Check if status column exists
            PreparedStatement statusCheck = conn.prepareStatement("SHOW COLUMNS FROM orders LIKE 'status'");
            ResultSet statusResult = statusCheck.executeQuery();
            boolean hasStatusColumn = statusResult.next();
            System.out.println("\n🔍 Status column exists: " + hasStatusColumn);
            
            if (!hasStatusColumn) {
                System.out.println("⚠️ Adding status column to orders table...");
                PreparedStatement addStatus = conn.prepareStatement("ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending'");
                addStatus.executeUpdate();
                System.out.println("✅ Status column added successfully!");
            }
            
            // Check vendors
            System.out.println("\n🏪 Available Vendors:");
            PreparedStatement vendorQuery = conn.prepareStatement("SELECT sid, sname FROM seller");
            ResultSet vendors = vendorQuery.executeQuery();
            while (vendors.next()) {
                System.out.println("  - " + vendors.getString("sid") + ": " + vendors.getString("sname"));
            }
            
            // Check orders by vendor
            System.out.println("\n📦 Orders by Vendor:");
            PreparedStatement ordersByVendor = conn.prepareStatement(
                "SELECT o.sid, s.sname, COUNT(*) as order_count " +
                "FROM orders o LEFT JOIN seller s ON o.sid = s.sid " +
                "GROUP BY o.sid, s.sname ORDER BY order_count DESC"
            );
            ResultSet orderCounts = ordersByVendor.executeQuery();
            while (orderCounts.next()) {
                String vendorId = orderCounts.getString("sid");
                String vendorName = orderCounts.getString("sname");
                int count = orderCounts.getInt("order_count");
                System.out.println("  - " + (vendorId != null ? vendorId : "NULL") + 
                                 " (" + (vendorName != null ? vendorName : "Unknown") + "): " + count + " orders");
            }
            
            // Check recent orders
            System.out.println("\n📋 Recent Orders:");
            PreparedStatement recentOrders = conn.prepareStatement(
                "SELECT o.oid, o.uid, o.sid, p.pname, COALESCE(o.status, 'Pending') as status, o.orderdatetime " +
                "FROM orders o LEFT JOIN product p ON o.pid = p.pid " +
                "ORDER BY o.orderdatetime DESC LIMIT 10"
            );
            ResultSet recent = recentOrders.executeQuery();
            while (recent.next()) {
                System.out.println("  - Order #" + recent.getInt("oid") + 
                                 " | Customer: " + recent.getString("uid") + 
                                 " | Vendor: " + recent.getString("sid") + 
                                 " | Product: " + recent.getString("pname") + 
                                 " | Status: " + recent.getString("status"));
            }
            
            // Check inventory
            System.out.println("\n📦 Vendor Inventory:");
            PreparedStatement inventoryQuery = conn.prepareStatement(
                "SELECT i.sid, s.sname, COUNT(*) as product_count " +
                "FROM inventory i LEFT JOIN seller s ON i.sid = s.sid " +
                "GROUP BY i.sid, s.sname ORDER BY product_count DESC"
            );
            ResultSet inventory = inventoryQuery.executeQuery();
            while (inventory.next()) {
                String vendorId = inventory.getString("sid");
                String vendorName = inventory.getString("sname");
                int productCount = inventory.getInt("product_count");
                System.out.println("  - " + (vendorId != null ? vendorId : "NULL") + 
                                 " (" + (vendorName != null ? vendorName : "Unknown") + "): " + productCount + " products");
            }
            
            conn.close();
            System.out.println("\n✅ Database diagnostic completed!");
            
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}