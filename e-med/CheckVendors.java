import java.sql.*;

public class CheckVendors {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("=== VENDORS IN DATABASE ===");
            PreparedStatement ps = conn.prepareStatement("SELECT sid, sname, pass FROM seller");
            ResultSet rs = ps.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Vendor ID: " + rs.getString("sid"));
                System.out.println("Name: " + rs.getString("sname"));
                System.out.println("Password: " + rs.getString("pass"));
                System.out.println("---");
            }
            
            if (count == 0) {
                System.out.println("No vendors found in database!");
                System.out.println("Creating sample vendor02...");
                
                PreparedStatement insertPs = conn.prepareStatement(
                    "INSERT INTO seller (sid, sname, pass, address, phno) VALUES (?, ?, ?, ?, ?)");
                insertPs.setString(1, "vendor02");
                insertPs.setString(2, "Apollo Pharmacy");
                insertPs.setString(3, "vendor123");
                insertPs.setString(4, "123 Medical Street, Ahmedabad");
                insertPs.setLong(5, 9876543210L);
                
                int result = insertPs.executeUpdate();
                if (result > 0) {
                    System.out.println("✓ vendor02 created successfully!");
                    System.out.println("Login: vendor02 / vendor123");
                }
            } else {
                System.out.println("Total vendors found: " + count);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}