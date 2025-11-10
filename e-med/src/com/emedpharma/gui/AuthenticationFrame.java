package com.emedpharma.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AuthenticationFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextField loginUserId, signupUserId, signupName, signupEmail, signupPhone;
    private JPasswordField loginPassword, signupPassword, confirmPassword;
    private JComboBox<String> loginUserType, signupUserType, signupCity;
    private JTextArea signupAddress;
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
        signupAddress = new JTextArea(2, 20);
        
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
        JLabel testLabel = new JLabel("<html><center><b>Test Credentials:</b><br>" +
            "Customer: aanchal01/pass123<br>" +
            "Vendors: vendor01/vendor123, vendor02/vendor456<br>" +
            "vendor03/health123, vendor04/guard456<br>" +
            "vendor05/net789, vendor06/onemg321, vendor07/easy654</center></html>");
        testLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        testLabel.setForeground(new Color(100, 100, 100));
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
        signupAddress.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        signupAddress.setRows(2);
        JScrollPane addressScroll = new JScrollPane(signupAddress);
        addressScroll.setPreferredSize(new Dimension(200, 50));
        panel.add(addressScroll, gbc);
        
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
    
    private void performSignup() {
        String userType = (String) signupUserType.getSelectedItem();
        String userId = signupUserId.getText().trim();
        String name = signupName.getText().trim();
        String email = signupEmail.getText().trim();
        String phone = signupPhone.getText().trim();
        String city = (String) signupCity.getSelectedItem();
        String streetAddress = signupAddress.getText().trim();
        String fullAddress = streetAddress + ", " + city + ", Gujarat, India";
        String password = new String(signupPassword.getPassword());
        String confirmPass = new String(confirmPassword.getPassword());
        
        if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || streetAddress.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            showStatus("Please fill all required fields", Color.RED);
            return;
        }
        
        if (!password.equals(confirmPass)) {
            showStatus("Passwords do not match!", Color.RED);
            return;
        }
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return createUser(userId, name, email, phone, fullAddress, password, userType);
            }
            
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
    
    private boolean authenticateUser(String userId, String password, String userType) {
        String tableName = "Customer".equals(userType) ? "customer" : 
                          "Vendor".equals(userType) ? "seller" : "admin";
        String idColumn = "Customer".equals(userType) ? "uid" : 
                         "Vendor".equals(userType) ? "sid" : "aid";
        
        String query = "SELECT " + idColumn + ", pass FROM " + tableName + " WHERE " + idColumn + " = ?";
        
        try {
            // Try MySQL 8.0+ driver first
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // Fallback to older driver
                Class.forName("com.mysql.jdbc.Driver");
            }
            
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("Database connected successfully");
            System.out.println("Executing query: " + query + " with userId: " + userId);
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("pass");
                System.out.println("Found user in database. Password match: " + password.equals(dbPassword));
                conn.close();
                return password.equals(dbPassword);
            } else {
                System.out.println("User not found in database");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to hardcoded credentials for testing
            System.out.println("=== USING FALLBACK AUTHENTICATION ===");
            System.out.println("Checking userType: '" + userType + "'");
            System.out.println("Checking userId: '" + userId + "'");
            System.out.println("Checking password: '" + password + "'");
            
            if ("Customer".equals(userType)) {
                System.out.println("Customer type selected, checking credentials...");
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
                if (("vendor01".equals(userId) && "vendor123".equals(password)) ||
                    ("vendor02".equals(userId) && "vendor456".equals(password)) ||
                    ("vendor03".equals(userId) && "health123".equals(password)) ||
                    ("vendor04".equals(userId) && "guard456".equals(password)) ||
                    ("vendor05".equals(userId) && "net789".equals(password)) ||
                    ("vendor06".equals(userId) && "onemg321".equals(password)) ||
                    ("vendor07".equals(userId) && "easy654".equals(password))) {
                    System.out.println("✓ Vendor credentials match for: " + userId);
                    return true;
                } else {
                    System.out.println("✗ Vendor credentials don't match");
                    System.out.println("Available vendors: vendor01-07 with respective passwords");
                }
            } else {
                System.out.println("Unknown user type: " + userType);
            }
        }
        System.out.println("=== AUTHENTICATION FAILED ===");
        return false;
    }
    
    private boolean createUser(String userId, String name, String email, String phone, 
                              String address, String password, String userType) {
        String tableName = "Customer".equals(userType) ? "customer" : "seller";
        String query;
        
        if ("Customer".equals(userType)) {
            query = "INSERT INTO customer (uid, name, email, phone, address, pass) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            query = "INSERT INTO seller (sid, name, email, phone, address, pass) VALUES (?, ?, ?, ?, ?, ?)";
        }
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase", "root", "A@nchal911");
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, password);
            
            int result = ps.executeUpdate();
            conn.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void openDashboard(String userType, String userId) {
        switch (userType) {
            case "Customer":
                new SmartCustomerDashboard(userId).setVisible(true);
                break;
            case "Vendor":
                new VendorDashboard(userId).setVisible(true);
                break;
            case "Admin":
                JOptionPane.showMessageDialog(this, "Admin Dashboard coming soon!");
                break;
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }
}