import java.sql.*;

public class CheckInventoryTable {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("=== CHECKING EXISTING TABLES ===");
            
            // Check if vendor_inventory table exists
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "vendor_inventory", null);
            
            if (tables.next()) {
                System.out.println("vendor_inventory table exists");
                
                // Show structure
                ResultSet columns = metaData.getColumns(null, null, "vendor_inventory", null);
                while (columns.next()) {
                    System.out.println("Column: " + columns.getString("COLUMN_NAME") + 
                                     " - Type: " + columns.getString("TYPE_NAME"));
                }
                
                // Show data
                System.out.println("\n=== SAMPLE DATA ===");
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM vendor_inventory LIMIT 5");
                ResultSetMetaData rsmd = rs.getMetaData();
                
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t");
                }
                System.out.println();
                
                while (rs.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } else {
                System.out.println("vendor_inventory table does not exist");
            }
            
            // Check inventory table
            tables = metaData.getTables(null, null, "inventory", null);
            if (tables.next()) {
                System.out.println("\ninventory table exists");
                
                ResultSet columns = metaData.getColumns(null, null, "inventory", null);
                while (columns.next()) {
                    System.out.println("Column: " + columns.getString("COLUMN_NAME") + 
                                     " - Type: " + columns.getString("TYPE_NAME"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}