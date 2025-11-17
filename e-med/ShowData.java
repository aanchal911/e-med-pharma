import java.sql.*;

public class ShowData {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("=== CUSTOMER DATA ===");
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT c.uid, CONCAT(c.fname, ' ', c.lname) as name, c.email, c.phno, " +
                "COUNT(o.oid) as orders, COALESCE(SUM(o.price), 0) as spent " +
                "FROM customer c LEFT JOIN orders o ON c.uid = o.uid GROUP BY c.uid LIMIT 5");
            ResultSet rs1 = ps1.executeQuery();
            
            while (rs1.next()) {
                System.out.println("Customer: " + rs1.getString("uid") + " | " + 
                                 rs1.getString("name") + " | Orders: " + rs1.getInt("orders") + 
                                 " | Spent: Rs." + rs1.getInt("spent"));
            }
            
            System.out.println("\n=== VENDOR DATA ===");
            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT s.sid, s.sname, s.phno, COUNT(o.oid) as orders, COALESCE(SUM(o.price), 0) as revenue " +
                "FROM seller s LEFT JOIN orders o ON s.sid = o.sid GROUP BY s.sid, s.sname, s.phno LIMIT 5");
            ResultSet rs2 = ps2.executeQuery();
            
            while (rs2.next()) {
                System.out.println("Vendor: " + rs2.getString("sid") + " | " + 
                                 rs2.getString("sname") + " | Orders: " + rs2.getInt("orders") + 
                                 " | Revenue: Rs." + rs2.getInt("revenue"));
            }
            
            System.out.println("\n=== SYSTEM STATS ===");
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM customer");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) System.out.println("Total Customers: " + rs3.getInt(1));
            
            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*) FROM seller");
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) System.out.println("Total Vendors: " + rs4.getInt(1));
            
            PreparedStatement ps5 = conn.prepareStatement("SELECT COUNT(*) FROM product");
            ResultSet rs5 = ps5.executeQuery();
            if (rs5.next()) System.out.println("Total Products: " + rs5.getInt(1));
            
            PreparedStatement ps6 = conn.prepareStatement("SELECT COUNT(*), COALESCE(SUM(price), 0) FROM orders");
            ResultSet rs6 = ps6.executeQuery();
            if (rs6.next()) {
                System.out.println("Total Orders: " + rs6.getInt(1));
                System.out.println("Total Revenue: Rs." + rs6.getInt(2));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}