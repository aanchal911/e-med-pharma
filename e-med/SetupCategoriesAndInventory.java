import java.sql.*;
import java.util.Random;

public class SetupCategoriesAndInventory {
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Create categories table
            String createCategoriesTable = """
                CREATE TABLE IF NOT EXISTS categories (
                    category_id VARCHAR(10) PRIMARY KEY,
                    category_name VARCHAR(100) NOT NULL
                )""";
            conn.createStatement().execute(createCategoriesTable);
            
            // Insert categories
            String[][] categories = {
                {"AID", "First Aid"}, {"BAB", "Baby Care"}, {"BOD", "Body Care"},
                {"DEV", "Medical Devices"}, {"DIA", "Diabetes Care"}, {"DIP", "Diapers"},
                {"FOO", "Foot Care"}, {"HAR", "Hair Care"}, {"HER", "Herbal Products"},
                {"HYG", "Hygiene"}, {"INT", "Intimate Care"}, {"MED", "Medicines"},
                {"MEN", "Men's Care"}, {"MOB", "Mobility Aids"}, {"ORA", "Oral Care"},
                {"PAD", "Sanitary Pads"}, {"RES", "Respiratory"}, {"SKN", "Skin Care"},
                {"SUP", "Supplements"}, {"WOM", "Women's Care"}
            };
            
            String insertCategory = "INSERT IGNORE INTO categories VALUES (?, ?)";
            PreparedStatement catStmt = conn.prepareStatement(insertCategory);
            
            for (String[] cat : categories) {
                catStmt.setString(1, cat[0]);
                catStmt.setString(2, cat[1]);
                catStmt.executeUpdate();
            }
            
            // Add category column to product table if not exists
            try {
                conn.createStatement().execute("ALTER TABLE product ADD COLUMN category VARCHAR(10)");
            } catch (SQLException e) {
                // Column already exists
            }
            
            // Update product categories based on pid prefix
            conn.createStatement().execute("UPDATE product SET category = SUBSTRING(pid, 1, 3)");
            
            // Populate inventory table with random stock for all vendors and products
            String[] vendorIds = {"vendor01", "vendor02", "vendor03", "vendor04", "vendor05", "vendor06", "vendor07"};
            
            // Get all products
            ResultSet products = conn.createStatement().executeQuery("SELECT pid, pname FROM product");
            java.util.List<String[]> productList = new java.util.ArrayList<>();
            
            while (products.next()) {
                productList.add(new String[]{products.getString("pid"), products.getString("pname")});
            }
            
            // Insert into inventory table
            String insertInventory = "INSERT IGNORE INTO inventory (pid, pname, quantity, sid) VALUES (?, ?, ?, ?)";
            PreparedStatement invStmt = conn.prepareStatement(insertInventory);
            
            Random random = new Random();
            int totalInserts = 0;
            
            for (String vendorId : vendorIds) {
                for (String[] product : productList) {
                    // 85% chance of having stock, 15% out of stock
                    int stock = random.nextInt(100) < 85 ? random.nextInt(100) + 10 : 0;
                    
                    invStmt.setString(1, product[0]);
                    invStmt.setString(2, product[1]);
                    invStmt.setInt(3, stock);
                    invStmt.setString(4, vendorId);
                    
                    try {
                        invStmt.executeUpdate();
                        totalInserts++;
                    } catch (SQLException e) {
                        // Record already exists
                    }
                }
            }
            
            System.out.println("Setup completed!");
            System.out.println("Categories created: " + categories.length);
            System.out.println("Products updated with categories");
            System.out.println("Inventory records processed: " + totalInserts);
            System.out.println("Total products: " + productList.size());
            System.out.println("Total vendors: " + vendorIds.length);
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}