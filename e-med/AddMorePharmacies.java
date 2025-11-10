import java.sql.*;

public class AddMorePharmacies {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO seller (sid, sname, pass, address, phno) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            
            // Add more pharmacies
            String[][] pharmacies = {
                {"vendor03", "HealthKart Pharmacy", "health123", "303 Wellness Street, Bangalore", "7345678901"},
                {"vendor04", "Guardian Pharmacy", "guard456", "404 Care Avenue, Chennai", "6456789012"},
                {"vendor05", "Netmeds Store", "net789", "505 Digital Plaza, Hyderabad", "5567890123"},
                {"vendor06", "1mg Pharmacy", "onemg321", "606 Quick Lane, Pune", "4678901234"},
                {"vendor07", "Pharmeasy Store", "easy654", "707 Express Road, Kolkata", "3789012345"}
            };
            
            System.out.println("Adding new pharmacies...");
            
            for (String[] pharmacy : pharmacies) {
                stmt.setString(1, pharmacy[0]);
                stmt.setString(2, pharmacy[1]);
                stmt.setString(3, pharmacy[2]);
                stmt.setString(4, pharmacy[3]);
                stmt.setLong(5, Long.parseLong(pharmacy[4]));
                
                try {
                    stmt.executeUpdate();
                    System.out.println("Added: " + pharmacy[0] + " - " + pharmacy[1]);
                } catch (SQLException e) {
                    System.out.println("Skipped " + pharmacy[0] + " (already exists)");
                }
            }
            
            // Show all vendors
            System.out.println("\n=== ALL VENDORS ===");
            ResultSet rs = conn.createStatement().executeQuery("SELECT sid, sname FROM seller");
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println(count + ". " + rs.getString("sid") + " - " + rs.getString("sname"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}