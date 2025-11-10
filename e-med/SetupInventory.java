import java.sql.*;
import java.util.Random;

public class SetupInventory {
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
            
            // Add category column to product table
            try {
                conn.createStatement().execute("ALTER TABLE product ADD COLUMN category VARCHAR(10)");
            } catch (SQLException e) {
                // Column already exists
            }
            
            // Update product categories based on pid prefix
            conn.createStatement().execute("UPDATE product SET category = SUBSTRING(pid, 1, 3)");
            
            // Create vendor_inventory table
            String createInventoryTable = """
                CREATE TABLE IF NOT EXISTS vendor_inventory (
                    vendor_id VARCHAR(20),
                    product_id VARCHAR(20),
                    stock_quantity INT DEFAULT 0,
                    PRIMARY KEY (vendor_id, product_id),
                    FOREIGN KEY (vendor_id) REFERENCES seller(sid),
                    FOREIGN KEY (product_id) REFERENCES product(pid)
                )""";
            conn.createStatement().execute(createInventoryTable);
            
            // Get all products
            ResultSet products = conn.createStatement().executeQuery("SELECT pid FROM product");
            
            String[] vendorIds = {"vendor01", "vendor02", "vendor03", "vendor04", "vendor05", "vendor06", "vendor07"};
            java.util.List<String> productList = new java.util.ArrayList<>();
            
            while (products.next()) {
                productList.add(products.getString("pid"));
            }
            String[] productIds = productList.toArray(new String[0]);
            
            // Insert inventory for each vendor-product combination
            String insertInventory = "INSERT IGNORE INTO vendor_inventory (vendor_id, product_id, stock_quantity) VALUES (?, ?, ?)";
            PreparedStatement invStmt = conn.prepareStatement(insertInventory);
            
            Random random = new Random();
            int totalInserts = 0;
            
            for (String vendorId : vendorIds) {
                for (String productId : productIds) {
                    // 85% chance of having stock, 15% out of stock
                    int stock = random.nextInt(100) < 85 ? random.nextInt(50) + 1 : 0;
                    
                    invStmt.setString(1, vendorId);
                    invStmt.setString(2, productId);
                    invStmt.setInt(3, stock);
                    invStmt.executeUpdate();
                    totalInserts++;
                }
            }
            
            System.out.println("Setup completed!");
            System.out.println("Categories created: " + categories.length);
            System.out.println("Inventory records created: " + totalInserts);
            System.out.println("Total products: " + productIds.length);
            System.out.println("Total vendors: " + vendorIds.length);
            System.out.println("Products updated with categories");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}