import java.sql.*;
import java.util.Scanner;

public class AddNewVendor {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("=== ADD NEW VENDOR ===");
            
            System.out.print("Enter Vendor ID: ");
            String vendorId = scanner.nextLine();
            
            System.out.print("Enter Vendor Name: ");
            String vendorName = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            
            System.out.print("Enter Phone Number: ");
            long phoneNumber = scanner.nextLong();
            
            String insertQuery = "INSERT INTO seller (sid, sname, pass, address, phno) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            
            stmt.setString(1, vendorId);
            stmt.setString(2, vendorName);
            stmt.setString(3, password);
            stmt.setString(4, address);
            stmt.setLong(5, phoneNumber);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("\nVendor added successfully!");
                System.out.println("ID: " + vendorId);
                System.out.println("Name: " + vendorName);
                System.out.println("Password: " + password);
            } else {
                System.out.println("Failed to add vendor.");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        
        scanner.close();
    }
}