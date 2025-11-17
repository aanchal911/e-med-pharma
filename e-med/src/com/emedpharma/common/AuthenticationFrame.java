package com.emedpharma.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AuthenticationFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextField loginUserId, signupUserId, signupName, signupEmail, signupPhone, signupAddress;
    private JPasswordField loginPassword, signupPassword, confirmPassword;
    private JComboBox<String> loginUserType, signupUserType, signupCity;
    private JLabel statusLabel;
    
    private MainApplication parentApp;
    
    public AuthenticationFrame() {
        this(null);
    }
    
    public AuthenticationFrame(MainApplication parent) {
        this.parentApp = parent;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("e-MEDpharma - Login / Sign Up");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Handle window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentApp != null) {
                    parentApp.setVisible(true);
                }
                dispose();
            }
        });
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        tabbedPane = new JTabbedPane();
        
        // Login components
        loginUserId = new JTextField(20);
        loginPassword = new JPasswordField(20);
        loginUserType = new JComboBox<>(new String[]{"Customer", "Vendor", "Admin"});
        
        // Signup components
        signupUserId = new JTextField(20);
        signupName = new JTextField(20);
        signupEmail = new JTextField(20);
        signupPhone = new JTextField(20);
        signupPassword = new JPasswordField(20);
        confirmPassword = new JPasswordField(20);
        signupUserType = new JComboBox<>(new String[]{"Customer", "Vendor"});
        signupCity = new JComboBox<>(new String[]{
            "Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", 
            "Jamnagar", "Junagadh", "Gandhinagar", "Anand", "Navsari",
            "Morbi", "Mehsana", "Bharuch", "Vapi", "Veraval",
            "Porbandar", "Godhra", "Botad", "Amreli", "Palanpur"
        });
        signupAddress = new JTextField(20);
        
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(27, 94, 32), 0, getHeight(), new Color(46, 125, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("e-MEDpharma Authentication");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        // Login Panel
        JPanel loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);
        
        // Signup Panel
        JPanel signupPanel = createSignupPanel();
        tabbedPane.addTab("Sign Up", signupPanel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        
        // User Type
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        loginUserType.setPreferredSize(new Dimension(200, 35));
        panel.add(loginUserType, gbc);
        
        // User ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(loginUserId);
        panel.add(loginUserId, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(loginPassword);
        panel.add(loginPassword, gbc);
        
        // Login Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(25, 10, 15, 10);
        JButton loginBtn = createStyledButton("Login", new Color(46, 125, 50));
        panel.add(loginBtn, gbc);
        
        // Test credentials
        gbc.gridy = 4; gbc.insets = new Insets(10, 10, 10, 10);
        JLabel testLabel = new JLabel("<html><center><b>Login Credentials:</b><br><br>" +
            "<b>Customer:</b> aanchal01 / pass123<br><br>" +
            "<b>Pharmacy Partners:</b><br>" +
            "Apollo: vendor01 / vendor123<br>" +
            "MedPlus: vendor02 / vendor456<br><br>" +
            "<b>ADMIN ACCESS:</b><br>" +
            "<font color='red'>System Admin: admin01 / admin123</font><br>" +
            "<font color='red'>Support Admin: admin02 / support123</font></center></html>");
        testLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        testLabel.setForeground(new Color(80, 80, 80));
        testLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        testLabel.setBackground(new Color(248, 250, 252));
        testLabel.setOpaque(true);
        panel.add(testLabel, gbc);
        
        return panel;
    }
    
    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Account Type
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        signupUserType.setPreferredSize(new Dimension(200, 35));
        panel.add(signupUserType, gbc);
        
        // User ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupUserId);
        panel.add(signupUserId, gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupName);
        panel.add(signupName, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupEmail);
        panel.add(signupEmail, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupPhone);
        panel.add(signupPhone, gbc);
        
        // City
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("City (Gujarat):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        signupCity.setPreferredSize(new Dimension(200, 35));
        panel.add(signupCity, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Street Address:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupAddress);
        panel.add(signupAddress, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(signupPassword);
        panel.add(signupPassword, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        styleTextField(confirmPassword);
        panel.add(confirmPassword, gbc);
        
        // Sign Up Button
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(20, 10, 15, 10);
        JButton signupBtn = createStyledButton("Create Account", new Color(27, 94, 32));
        panel.add(signupBtn, gbc);
        
        return panel;
    }
    
    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Login button
        JButton loginBtn = (JButton) ((JPanel) tabbedPane.getComponentAt(0)).getComponent(6);
        loginBtn.addActionListener(e -> performLogin());
        
        // Signup button
        JButton signupBtn = (JButton) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(18);
        signupBtn.addActionListener(e -> performSignup());
        
        // Enter key listeners
        loginPassword.addActionListener(e -> performLogin());
        confirmPassword.addActionListener(e -> performSignup());
    }
    
    private void performLogin() {
        String userType = (String) loginUserType.getSelectedItem();
        String userId = loginUserId.getText().trim();
        String password = new String(loginPassword.getPassword());
        
        if (userId.isEmpty() || password.isEmpty()) {
            showStatus("Please fill all fields", Color.RED);
            return;
        }
        
        showStatus("Authenticating...", Color.BLUE);
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("UserType: '" + userType + "'");
        System.out.println("UserId: '" + userId + "'");
        System.out.println("Password: '" + password + "'");
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authenticateUser(userId, password, userType);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        showStatus("Login successful!", new Color(0, 150, 0));
                        
                        Timer timer = new Timer(1000, e -> {
                            openDashboard(userType, userId);
                            dispose();
                            if (parentApp != null) {
                                parentApp.dispose(); // Close main app when login successful
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        showStatus("Invalid credentials! Check console for details.", Color.RED);
                    }
                } catch (Exception e) {
                    showStatus("Login failed! " + e.getMessage(), Color.RED);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    /**
     * ENCAPSULATION: This private method handles user registration
     * Data is encapsulated within this method and validated before processing
     * Demonstrates data hiding - internal logic is hidden from external classes
     */
    private void performSignup() {
        // Extract user input data from GUI components (Encapsulation of form data)
        String userType = (String) signupUserType.getSelectedItem();
        String userId = signupUserId.getText().trim();
        String name = signupName.getText().trim();
        String email = signupEmail.getText().trim();
        String phone = signupPhone.getText().trim();
        String city = (String) signupCity.getSelectedItem();
        String streetAddress = signupAddress.getText().trim();
        String fullAddress = streetAddress + ", " + city + ", Gujarat, India";
        // Convert char array to String for security (password handling best practice)
        String password = new String(signupPassword.getPassword());
        String confirmPass = new String(confirmPassword.getPassword());
        
        // Input validation - ensures data integrity before processing
        if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || streetAddress.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            showStatus("Please fill all required fields", Color.RED);
            return; // Early return pattern for validation
        }
        
        // Password confirmation validation - security measure
        if (!password.equals(confirmPass)) {
            showStatus("Passwords do not match!", Color.RED);
            return;
        }
        
        // POLYMORPHISM: SwingWorker is used for background processing
        // This prevents GUI freezing during database operations
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            // METHOD OVERRIDING: Overriding abstract method from SwingWorker class
            @Override
            protected Boolean doInBackground() throws Exception {
                // Delegate user creation to separate method (Single Responsibility Principle)
                return createUser(userId, name, email, phone, fullAddress, password, userType);
            }
            
            // METHOD OVERRIDING: Overriding done() method to handle UI updates
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        showStatus("Account created successfully!", new Color(0, 150, 0));
                        
                        Timer timer = new Timer(1500, e -> {
                            openDashboard(userType, userId);
                            dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        showStatus("Failed to create account!", Color.RED);
                    }
                } catch (Exception e) {
                    showStatus("Signup failed!", Color.RED);
                }
            }
        };
        worker.execute();
    }
    
    /**
     * POLYMORPHISM & ABSTRACTION: This method handles authentication for different user types
     * Uses conditional logic to abstract database table differences
     * Demonstrates polymorphic behavior - same method works for different user types
     */
    private boolean authenticateUser(String userId, String password, String userType) {
        // Handle admin authentication separately
        if ("Admin".equals(userType)) {
            return authenticateAdmin(userId, password);
        }
        
        // POLYMORPHISM: Different table names based on user type
        // This allows same method to work with Customer and Vendor
        String tableName = "Customer".equals(userType) ? "customer" : "seller";
        // Different column names for different user types (Database abstraction)
        String idColumn = "Customer".equals(userType) ? "uid" : "sid";
        
        // Dynamic SQL query construction based on user type (Polymorphism in action)
        String query = "SELECT " + idColumn + ", pass FROM " + tableName + " WHERE " + idColumn + " = ?";
        
        try {
            // EXCEPTION HANDLING: Try-catch for robust database connectivity
            // Try MySQL 8.0+ driver first (newer version)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // Fallback to older driver for compatibility
                Class.forName("com.mysql.jdbc.Driver");
            }
            
            // Database connection establishment
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            // Debug logging for development
            System.out.println("Database connected successfully");
            System.out.println("Executing query: " + query + " with userId: " + userId);
            
            // SECURITY: Using PreparedStatement to prevent SQL injection
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId); // Parameter binding for security
            ResultSet rs = ps.executeQuery();
            
            // Check if user exists in database
            if (rs.next()) {
                String dbPassword = rs.getString("pass");
                System.out.println("Found user in database. Password match: " + password.equals(dbPassword));
                conn.close(); // Resource cleanup
                return password.equals(dbPassword); // Password verification
            } else {
                System.out.println("User not found in database");
            }
            conn.close(); // Always close database connections
        } catch (Exception e) {
            // EXCEPTION HANDLING: Graceful error handling for database failures
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            
            // FALLBACK MECHANISM: Hardcoded credentials for testing when DB is unavailable
            // This demonstrates defensive programming - system still works without database
            System.out.println("=== USING FALLBACK AUTHENTICATION ===");
            System.out.println("Checking userType: '" + userType + "'");
            System.out.println("Checking userId: '" + userId + "'");
            System.out.println("Checking password: '" + password + "'");
            
            // POLYMORPHISM: Same authentication logic handles different user types
            if ("Customer".equals(userType)) {
                System.out.println("Customer type selected, checking credentials...");
                // Hardcoded customer credentials for testing
                if ("aanchal01".equals(userId) && "pass123".equals(password)) {
                    System.out.println("✓ aanchal01 credentials match!");
                    return true;
                } else if ("shagun02".equals(userId) && "pass456".equals(password)) {
                    System.out.println("✓ shagun02 credentials match!");
                    return true;
                } else if ("dhara03".equals(userId) && "pass789".equals(password)) {
                    System.out.println("✓ dhara03 credentials match!");
                    return true;
                } else {
                    System.out.println("✗ No customer credentials match");
                    System.out.println("Expected: aanchal01/pass123, shagun02/pass456, or dhara03/pass789");
                }
            } else if ("Vendor".equals(userType)) {
                System.out.println("Vendor type selected, checking credentials...");
                // Multiple pharmacy partner credentials - demonstrates scalability
                if (("vendor01".equals(userId) && "vendor123".equals(password)) ||
                    ("vendor02".equals(userId) && "vendor456".equals(password)) ||
                    ("vendor03".equals(userId) && "health123".equals(password)) ||
                    ("vendor04".equals(userId) && "guard456".equals(password)) ||
                    ("vendor05".equals(userId) && "net789".equals(password)) ||
                    ("vendor06".equals(userId) && "onemg321".equals(password)) ||
                    ("vendor07".equals(userId) && "easy654".equals(password))) {
                    System.out.println("✓ Pharmacy partner credentials match for: " + getPharmacyName(userId));
                    return true;
                } else {
                    System.out.println("✗ Pharmacy credentials don't match");
                    System.out.println("Available pharmacies: Apollo, MedPlus, HealthGuard, Guardian, Netmeds, 1mg, EasyMedico");
                }
            }
        }
        System.out.println("=== AUTHENTICATION FAILED ===");
        return false;
    }
    
    private boolean authenticateAdmin(String userId, String password) {
        try {
            // Try database authentication first
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT aid, pass FROM admin WHERE aid = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("pass");
                conn.close();
                if (password.equals(dbPassword)) {
                    System.out.println("✓ Admin authentication successful from database");
                    return true;
                }
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Database error for admin auth: " + e.getMessage());
        }
        
        // Fallback to hardcoded credentials
        if (("admin".equals(userId) && "admin123".equals(password)) ||
            ("admin01".equals(userId) && "admin123".equals(password)) ||
            ("admin02".equals(userId) && "support123".equals(password))) {
            System.out.println("✓ Admin authentication successful (fallback)");
            return true;
        }
        
        System.out.println("✗ Admin authentication failed");
        return false;
    }
    
    /**
     * POLYMORPHISM: Creates different types of users (Customer/Vendor) using same method
     * ENCAPSULATION: Database operations are encapsulated within this method
     * ABSTRACTION: Hides complex database insertion logic from calling code
     */
    private boolean createUser(String userId, String name, String email, String phone, 
                              String address, String password, String userType) {
        // POLYMORPHISM: Different table names based on user type
        String tableName = "Customer".equals(userType) ? "customer" : "seller";
        String query;
        
        // Dynamic query construction based on user type (Polymorphic behavior)
        if ("Customer".equals(userType)) {
            // Customer table has 'uid' as primary key
            query = "INSERT INTO customer (uid, name, email, phone, address, pass) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            // Seller table has 'sid' as primary key
            query = "INSERT INTO seller (sid, name, email, phone, address, pass) VALUES (?, ?, ?, ?, ?, ?)";
        }
        
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase", "root", "A@nchal911");
            
            // SECURITY: Use PreparedStatement to prevent SQL injection
            PreparedStatement ps = conn.prepareStatement(query);
            // Bind parameters to prevent SQL injection attacks
            ps.setString(1, userId);    // User ID (uid/sid)
            ps.setString(2, name);      // Full name
            ps.setString(3, email);     // Email address
            ps.setString(4, phone);     // Phone number
            ps.setString(5, address);   // Full address
            ps.setString(6, password);  // Password (should be hashed in production)
            
            // Execute the insert query
            int result = ps.executeUpdate();
            conn.close(); // Always close database connections
            return result > 0; // Return true if insertion was successful
        } catch (Exception e) {
            // EXCEPTION HANDLING: Log error and return false on failure
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * POLYMORPHISM: Opens different dashboard types based on user type
     * FACTORY PATTERN: Creates appropriate dashboard object based on user type
     * ABSTRACTION: Hides dashboard creation complexity from authentication logic
     */
    private void openDashboard(String userType, String userId) {
        // POLYMORPHISM: Same method call creates different dashboard types
        switch (userType) {
            case "Customer":
                // Create and show Customer dashboard (inheritance from JFrame)
                new com.emedpharma.customer.SmartCustomerDashboard(userId).setVisible(true);
                break;
            case "Vendor":
                // Create and show Vendor dashboard with pharmacy name (inheritance from JFrame)
                new com.emedpharma.vendor.VendorDashboard(userId, getPharmacyName(userId)).setVisible(true);
                break;
            case "Admin":
                // Create and show Admin dashboard
                new com.emedpharma.admin.AdminDashboard(userId).setVisible(true);
                break;
        }
    }
    
    /**
     * ENCAPSULATION: Maps vendor IDs to actual pharmacy names
     * ABSTRACTION: Hides the mapping logic from other methods
     */
    private String getPharmacyName(String vendorId) {
        switch (vendorId) {
            case "vendor01": return "Apollo Pharmacy";
            case "vendor02": return "MedPlus Store";
            case "vendor03": return "HealthKart Pharmacy";
            case "vendor04": return "Guardian Pharmacy";
            case "vendor05": return "Netmeds Store";
            case "vendor06": return "1mg Pharmacy";
            case "vendor07": return "Pharmeasy Store";
            default: return "Unknown Pharmacy";
        }
    }
    
    /**
     * ENCAPSULATION: Encapsulates status display logic
     * ABSTRACTION: Provides simple interface to show status messages
     * Hides the complexity of GUI component manipulation
     */
    private void showStatus(String message, Color color) {
        // Update status label text
        statusLabel.setText(message);
        // Set text color (red for errors, green for success)
        statusLabel.setForeground(color);
        // Set font style for better visibility
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }
}