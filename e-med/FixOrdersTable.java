import java.sql.*;

public class FixOrdersTable {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL + "?useSSL=false&allowPublicKeyRetrieval=true", USERNAME, PASSWORD);
            
            System.out.println("=== FIXING ORDERS TABLE ===");
            
            // Add status column
            try {
                String addStatusColumn = "ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending'";
                conn.createStatement().execute(addStatusColumn);
                System.out.println("✓ Added status column");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("✓ Status column already exists");
                } else {
                    throw e;
                }
            }
            
            // Add order_date column if missing
            try {
                String addDateColumn = "ALTER TABLE orders ADD COLUMN order_date DATE DEFAULT (CURRENT_DATE)";
                conn.createStatement().execute(addDateColumn);
                System.out.println("✓ Added order_date column");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("✓ Order_date column already exists");
                } else {
                    throw e;
                }
            }
            
            // Update existing orders to have Pending status
            String updateExisting = "UPDATE orders SET status = 'Pending' WHERE status IS NULL";
            int updated = conn.createStatement().executeUpdate(updateExisting);
            System.out.println("✓ Updated " + updated + " existing orders to Pending status");
            
            // Update existing orders to have today's date
            String updateDate = "UPDATE orders SET order_date = CURRENT_DATE WHERE order_date IS NULL";
            int dateUpdated = conn.createStatement().executeUpdate(updateDate);
            System.out.println("✓ Updated " + dateUpdated + " existing orders with today's date");
            
            // Test the update now
            System.out.println("\n=== TESTING ORDER UPDATE ===");
            String testUpdate = "UPDATE orders SET status = 'Approved' WHERE oid = 1000";
            int result = conn.createStatement().executeUpdate(testUpdate);
            System.out.println("✓ Test update successful: " + result + " rows affected");
            
            // Show updated table structure
            System.out.println("\n=== UPDATED TABLE STRUCTURE ===");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "orders", null);
            
            while (columns.next()) {
                System.out.println("- " + columns.getString("COLUMN_NAME") + " (" + columns.getString("TYPE_NAME") + ")");
            }
            
            conn.close();
            System.out.println("\n✅ Orders table fixed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}