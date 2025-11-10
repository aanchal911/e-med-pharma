import java.sql.*;

public class CheckTableStructure {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("=== SELLER TABLE STRUCTURE ===");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "seller", null);
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("Column: " + columnName + " - Type: " + dataType);
            }
            
            System.out.println("\n=== CURRENT SELLER DATA ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM seller");
            ResultSetMetaData rsmd = rs.getMetaData();
            
            // Print column headers
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();
            
            // Print data
            int count = 0;
            while (rs.next()) {
                count++;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
            
            System.out.println("\nTotal vendors: " + count);
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}