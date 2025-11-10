import java.sql.*;

public class TestOrderUpdate {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL + "?useSSL=false&allowPublicKeyRetrieval=true", USERNAME, PASSWORD);
            
            System.out.println("=== CHECKING ORDERS TABLE ===");
            
            // Check table structure
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "orders", null);
            
            System.out.println("Orders table columns:");
            while (columns.next()) {
                System.out.println("- " + columns.getString("COLUMN_NAME") + " (" + columns.getString("TYPE_NAME") + ")");
            }
            
            // Check existing orders
            System.out.println("\n=== EXISTING ORDERS ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders LIMIT 5");
            ResultSetMetaData rsmd = rs.getMetaData();
            
            // Print headers
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();
            
            // Print data
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
            
            // Test update
            System.out.println("\n=== TESTING UPDATE ===");
            String updateQuery = "UPDATE orders SET status = ? WHERE oid = ?";
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, "Approved");
            ps.setInt(2, 1); // Assuming order ID 1 exists
            
            int result = ps.executeUpdate();
            System.out.println("Update result: " + result + " rows affected");
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}