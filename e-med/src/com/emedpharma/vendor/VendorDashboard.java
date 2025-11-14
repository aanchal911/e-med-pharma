package com.emedpharma.vendor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VendorDashboard extends JFrame {
    private static final Color PRIMARY_GREEN = new Color(27, 94, 32);
    private static final Color ACCENT_GREEN = new Color(46, 125, 50);
    private static final Color LIGHT_GREEN = new Color(200, 230, 201);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_TEXT = new Color(33, 37, 41);
    
    private String currentVendor;
    private JPanel mainContentPanel;
    private JTextField searchField;
    private List<Medicine> inventory;
    private List<Order> pendingOrders;
    private java.util.Stack<String> navigationHistory;
    
    public VendorDashboard(String vendorId) {
        this.currentVendor = vendorId;
        this.inventory = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
        this.navigationHistory = new java.util.Stack<>();
        
        initializeComponents();
        setupLayout();
        loadVendorData();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("e-MEDpharma - Vendor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        searchField = new JTextField(30);
        mainContentPanel = new JPanel(new BorderLayout());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GRAY);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Main Content Panel with scroll support
        mainContentPanel.setBackground(LIGHT_GRAY);
        showDashboardHome();
        
        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(mainScrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_GREEN, 0, getHeight(), ACCENT_GREEN);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Left - Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("e-MEDpharma Vendor");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        
        // Center - Search
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setOpaque(false);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        JButton searchBtn = new JButton("Search Inventory");
        searchBtn.setPreferredSize(new Dimension(120, 35));
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setForeground(PRIMARY_GREEN);
        searchBtn.setBorder(BorderFactory.createEmptyBorder());
        searchBtn.setFocusPainted(false);
        
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        // Right - Vendor actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentVendor);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        JButton ordersBtn = new JButton("Orders");
        ordersBtn.setBackground(WARNING_COLOR);
        ordersBtn.setForeground(Color.WHITE);
        ordersBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ordersBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        ordersBtn.setFocusPainted(false);
        
        JButton reportsBtn = new JButton("Reports");
        reportsBtn.setBackground(new Color(102, 126, 234));
        reportsBtn.setForeground(Color.WHITE);
        reportsBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportsBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        reportsBtn.setFocusPainted(false);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(DANGER_COLOR);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setFocusPainted(false);
        
        actionsPanel.add(welcomeLabel);
        actionsPanel.add(Box.createHorizontalStrut(20));
        actionsPanel.add(ordersBtn);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(reportsBtn);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(logoutBtn);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        headerPanel.add(actionsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Navigation Menu
        String[][] menuItems = {
            {"[H]", "Dashboard", "home"},
            {"[I]", "Inventory Management", "inventory"},
            {"[O]", "Order Management", "orders"},
            {"[+]", "Add New Medicine", "add_medicine"},
            {"[S]", "Sales Analytics", "analytics"},
            {"[P]", "Profile Settings", "profile"},
            {"[N]", "Notifications", "notifications"}
        };
        
        for (String[] item : menuItems) {
            JButton menuBtn = createMenuButton(item[0], item[1], item[2]);
            sidebarPanel.add(menuBtn);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }
        
        // Business Status Card
        sidebarPanel.add(Box.createVerticalStrut(30));
        JPanel statusCard = createBusinessStatusCard();
        sidebarPanel.add(statusCard);
        
        return sidebarPanel;
    }
    
    private JButton createMenuButton(String icon, String text, String action) {
        JButton button = new JButton(icon + "  " + text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(240, 45));
        button.setBackground(LIGHT_GRAY);
        button.setForeground(DARK_TEXT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_GREEN);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_GRAY);
                button.setForeground(DARK_TEXT);
            }
        });
        
        // Action listener
        button.addActionListener(e -> handleMenuAction(action));
        
        return button;
    }
    
    private JPanel createBusinessStatusCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(240, 248, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREEN, 2),
            new EmptyBorder(15, 15, 15, 15)));
        card.setMaximumSize(new Dimension(240, 150));
        
        JLabel titleLabel = new JLabel("Business Status");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel inventoryLabel = new JLabel("Inventory Items: " + inventory.size());
        inventoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ordersLabel = new JLabel("Pending Orders: " + pendingOrders.size());
        ordersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ordersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(inventoryLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(ordersLabel);
        
        return card;
    }
    
    private void handleMenuAction(String action) {
        navigationHistory.push(action);
        
        switch (action) {
            case "home":
                showDashboardHome();
                break;
            case "inventory":
                showInventoryManagement();
                break;
            case "orders":
                showOrderManagement();
                break;
            case "add_medicine":
                showAddMedicine();
                break;
            case "analytics":
                showSalesAnalytics();
                break;
            case "profile":
                showProfileSettings();
                break;
            case "notifications":
                showNotifications();
                break;
        }
    }
    
    private void showDashboardHome() {
        navigationHistory.clear();
        navigationHistory.push("home");
        mainContentPanel.removeAll();
        
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(LIGHT_GRAY);
        homePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Welcome Section
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(30, 30, 30, 30)));
        
        JLabel welcomeTitle = new JLabel("Good " + getTimeOfDay() + ", " + currentVendor + "!");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeTitle.setForeground(DARK_TEXT);
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel welcomeSubtitle = new JLabel("Manage your pharmacy business efficiently");
        welcomeSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeSubtitle.setForeground(new Color(100, 100, 100));
        welcomeSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeSubtitle);
        
        // Quick Stats
        JPanel statsPanel = createQuickStatsPanel();
        
        // Recent Activity
        JPanel activityPanel = createRecentActivityPanel();
        
        homePanel.add(welcomePanel, BorderLayout.NORTH);
        homePanel.add(statsPanel, BorderLayout.CENTER);
        homePanel.add(activityPanel, BorderLayout.SOUTH);
        
        mainContentPanel.add(homePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createQuickStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(LIGHT_GRAY);
        statsPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        // Total Sales
        JPanel salesCard = createStatCard("[Rs]", "Rs." + getTotalSales(), "Total Sales", SUCCESS_COLOR);
        
        // Inventory Count
        JPanel inventoryCard = createStatCard("[I]", String.valueOf(inventory.size()), "Inventory Items", ACCENT_GREEN);
        
        // Pending Orders
        JPanel ordersCard = createStatCard("[O]", String.valueOf(pendingOrders.size()), "Pending Orders", WARNING_COLOR);
        
        // Low Stock Items
        JPanel stockCard = createStatCard("[!]", String.valueOf(getLowStockCount()), "Low Stock Alert", DANGER_COLOR);
        
        statsPanel.add(salesCard);
        statsPanel.add(inventoryCard);
        statsPanel.add(ordersCard);
        statsPanel.add(stockCard);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String icon, String value, String label, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(25, 20, 25, 20)));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(accentColor);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(new Color(100, 100, 100));
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(labelLabel);
        
        return card;
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DARK_TEXT);
        
        JPanel activitiesList = new JPanel();
        activitiesList.setLayout(new BoxLayout(activitiesList, BoxLayout.Y_AXIS));
        activitiesList.setBackground(Color.WHITE);
        
        String[] activities = {
            "New order received - Order #ORD001",
            "Paracetamol stock updated - 500 units added",
            "Customer inquiry about Crocin availability",
            "Monthly sales report generated"
        };
        
        for (String activity : activities) {
            JLabel activityLabel = new JLabel("• " + activity);
            activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            activityLabel.setForeground(DARK_TEXT);
            activitiesList.add(activityLabel);
            activitiesList.add(Box.createVerticalStrut(8));
        }
        
        activityPanel.add(titleLabel, BorderLayout.NORTH);
        activityPanel.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        activityPanel.add(activitiesList, BorderLayout.CENTER);
        
        return activityPanel;
    }
    
    private void showInventoryManagement() {
        mainContentPanel.removeAll();
        
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(LIGHT_GRAY);
        inventoryPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Inventory Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        // Add Medicine Button
        JButton addMedicineBtn = new JButton("Add New Medicine");
        addMedicineBtn.setBackground(ACCENT_GREEN);
        addMedicineBtn.setForeground(Color.WHITE);
        addMedicineBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addMedicineBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addMedicineBtn.setFocusPainted(false);
        addMedicineBtn.addActionListener(e -> showAddMedicine());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GRAY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addMedicineBtn, BorderLayout.EAST);
        
        // Load real inventory from database
        JPanel inventoryGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        inventoryGrid.setBackground(LIGHT_GRAY);
        
        loadVendorInventory(inventoryGrid);
        
        JScrollPane scrollPane = new JScrollPane(inventoryGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        inventoryPanel.add(headerPanel, BorderLayout.NORTH);
        inventoryPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(inventoryPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void loadVendorInventory(JPanel inventoryGrid) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT p.pid, p.pname, p.manufacturer, p.price, i.quantity " +
                          "FROM product p JOIN inventory i ON p.pid = i.pid " +
                          "WHERE i.sid = ?";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentVendor);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                JPanel productCard = createInventoryCard(
                    rs.getString("pid"),
                    rs.getString("pname"),
                    rs.getString("manufacturer"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                );
                inventoryGrid.add(productCard);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            // Add sample inventory cards if database fails
            addSampleInventoryCards(inventoryGrid);
        }
    }
    
    private void addSampleInventoryCards(JPanel inventoryGrid) {
        String[][] sampleProducts = {
            {"MED001", "Paracetamol 500mg", "Generic", "25.0", "250"},
            {"MED002", "Crocin Advance", "GSK", "35.0", "180"},
            {"MED003", "Dolo 650", "Micro Labs", "30.0", "120"},
            {"MED004", "Aspirin 75mg", "Bayer", "45.0", "90"}
        };
        
        for (String[] product : sampleProducts) {
            JPanel productCard = createInventoryCard(
                product[0], product[1], product[2], 
                Double.parseDouble(product[3]), Integer.parseInt(product[4])
            );
            inventoryGrid.add(productCard);
        }
    }
    
    private JPanel createInventoryCard(String pid, String name, String manufacturer, double price, int stock) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(DARK_TEXT);
        
        JLabel manufacturerLabel = new JLabel("By " + manufacturer);
        manufacturerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        manufacturerLabel.setForeground(new Color(100, 100, 100));
        
        JLabel priceLabel = new JLabel("Rs." + price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(ACCENT_GREEN);
        
        JLabel stockLabel = new JLabel("Stock: " + stock + " units");
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockLabel.setForeground(stock > 50 ? SUCCESS_COLOR : stock > 10 ? WARNING_COLOR : DANGER_COLOR);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(manufacturerLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stockLabel);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateStockBtn = new JButton("Update Stock");
        updateStockBtn.setBackground(new Color(102, 126, 234));
        updateStockBtn.setForeground(Color.WHITE);
        updateStockBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        updateStockBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        updateStockBtn.setFocusPainted(false);
        updateStockBtn.addActionListener(e -> showUpdateStockDialog(pid, name, stock));
        
        JButton updatePriceBtn = new JButton("Update Price");
        updatePriceBtn.setBackground(WARNING_COLOR);
        updatePriceBtn.setForeground(Color.WHITE);
        updatePriceBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        updatePriceBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        updatePriceBtn.setFocusPainted(false);
        updatePriceBtn.addActionListener(e -> showUpdatePriceDialog(pid, name, price));
        
        buttonPanel.add(updateStockBtn);
        buttonPanel.add(updatePriceBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void showUpdateStockDialog(String pid, String productName, int currentStock) {
        JDialog stockDialog = new JDialog(this, "Update Stock - " + productName, true);
        stockDialog.setSize(400, 250);
        stockDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Update Stock for: " + productName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        formPanel.add(new JLabel("Current Stock:"));
        formPanel.add(new JLabel(String.valueOf(currentStock) + " units"));
        
        formPanel.add(new JLabel("New Stock Quantity:"));
        JTextField newStockField = new JTextField(String.valueOf(currentStock));
        styleTextField(newStockField);
        formPanel.add(newStockField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateBtn = new JButton("Update Stock");
        updateBtn.setBackground(ACCENT_GREEN);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(e -> {
            try {
                int newStock = Integer.parseInt(newStockField.getText());
                updateProductStock(pid, newStock);
                JOptionPane.showMessageDialog(stockDialog, "Stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                stockDialog.dispose();
                showInventoryManagement(); // Refresh
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(stockDialog, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> stockDialog.dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        stockDialog.add(mainPanel);
        stockDialog.setVisible(true);
    }
    
    private void showUpdatePriceDialog(String pid, String productName, double currentPrice) {
        JDialog priceDialog = new JDialog(this, "Update Price - " + productName, true);
        priceDialog.setSize(400, 300);
        priceDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Update Price for: " + productName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        formPanel.add(new JLabel("Current Price:"));
        formPanel.add(new JLabel("Rs." + currentPrice));
        
        formPanel.add(new JLabel("New Price (Rs.):"));
        JTextField newPriceField = new JTextField(String.valueOf(currentPrice));
        styleTextField(newPriceField);
        formPanel.add(newPriceField);
        
        formPanel.add(new JLabel("Discount (%):"));
        JTextField discountField = new JTextField("0");
        styleTextField(discountField);
        formPanel.add(discountField);
        
        JLabel finalPriceLabel = new JLabel("Final Price: Rs." + currentPrice);
        finalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        finalPriceLabel.setForeground(ACCENT_GREEN);
        formPanel.add(new JLabel("Final Price:"));
        formPanel.add(finalPriceLabel);
        
        // Update final price when discount changes
        discountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    double price = Double.parseDouble(newPriceField.getText());
                    double discount = Double.parseDouble(discountField.getText());
                    double finalPrice = price - (price * discount / 100);
                    finalPriceLabel.setText("Rs." + String.format("%.2f", finalPrice));
                } catch (NumberFormatException e) {
                    finalPriceLabel.setText("Rs." + currentPrice);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateBtn = new JButton("Update Price");
        updateBtn.setBackground(ACCENT_GREEN);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(e -> {
            try {
                double newPrice = Double.parseDouble(newPriceField.getText());
                double discount = Double.parseDouble(discountField.getText());
                double finalPrice = newPrice - (newPrice * discount / 100);
                
                updateProductPrice(pid, finalPrice);
                JOptionPane.showMessageDialog(priceDialog, 
                    "Price updated successfully!\nNew Price: Rs." + String.format("%.2f", finalPrice), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                priceDialog.dispose();
                showInventoryManagement(); // Refresh
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(priceDialog, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> priceDialog.dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        priceDialog.add(mainPanel);
        priceDialog.setVisible(true);
    }
    
    private void updateProductStock(String pid, int newStock) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "UPDATE inventory SET quantity = ? WHERE pid = ? AND sid = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newStock);
            ps.setString(2, pid);
            ps.setString(3, currentVendor);
            
            ps.executeUpdate();
            conn.close();
            
            System.out.println("Stock updated for product: " + pid + " to " + newStock);
            
        } catch (Exception e) {
            System.out.println("Error updating stock: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateProductPrice(String pid, double newPrice) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "UPDATE product SET price = ? WHERE pid = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDouble(1, newPrice);
            ps.setString(2, pid);
            
            ps.executeUpdate();
            conn.close();
            
            System.out.println("Price updated for product: " + pid + " to Rs." + newPrice);
            
        } catch (Exception e) {
            System.out.println("Error updating price: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showOrderManagement() {
        mainContentPanel.removeAll();
        
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBackground(LIGHT_GRAY);
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order Management - Approve/Reject Orders");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        // Load real orders from database
        JPanel ordersGrid = new JPanel();
        ordersGrid.setLayout(new BoxLayout(ordersGrid, BoxLayout.Y_AXIS));
        ordersGrid.setBackground(LIGHT_GRAY);
        
        loadVendorOrders(ordersGrid);
        
        JScrollPane scrollPane = new JScrollPane(ordersGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        ordersPanel.add(titleLabel, BorderLayout.NORTH);
        ordersPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(ordersPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void loadVendorOrders(JPanel ordersGrid) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT o.oid, o.uid, p.pname, o.quantity, o.price, o.orderdatetime " +
                          "FROM orders o JOIN product p ON o.pid = p.pid " +
                          "WHERE o.sid = ? ORDER BY o.orderdatetime DESC";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentVendor);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                JPanel orderCard = createOrderCard(
                    rs.getInt("oid"),
                    rs.getString("uid"),
                    rs.getString("pname"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    "Pending", // Default status since orders table doesn't have status column
                    rs.getString("orderdatetime")
                );
                ordersGrid.add(orderCard);
                ordersGrid.add(Box.createVerticalStrut(10));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
            // Add sample orders if database fails
            addSampleOrderCards(ordersGrid);
        }
    }
    
    private void addSampleOrderCards(JPanel ordersGrid) {
        Object[][] sampleOrders = {
            {1, "aanchal01", "Paracetamol 500mg", 2, 50.0, "Pending", "2024-01-22"},
            {2, "shagun02", "Crocin Advance", 1, 35.0, "Pending", "2024-01-22"},
            {3, "dhara03", "Dolo 650", 1, 30.0, "Approved", "2024-01-21"}
        };
        
        for (Object[] order : sampleOrders) {
            JPanel orderCard = createOrderCard(
                (Integer) order[0], (String) order[1], (String) order[2], 
                (Integer) order[3], (Double) order[4], (String) order[5], (String) order[6]
            );
            ordersGrid.add(orderCard);
            ordersGrid.add(Box.createVerticalStrut(10));
        }
    }
    
    private JPanel createOrderCard(int orderId, String customerId, String productName, 
                                  int quantity, double price, String status, String date) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Order Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel orderLabel = new JLabel("Order #" + orderId + " - " + customerId);
        orderLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        orderLabel.setForeground(DARK_TEXT);
        
        JLabel productLabel = new JLabel(productName + " (Qty: " + quantity + ")");
        productLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productLabel.setForeground(new Color(100, 100, 100));
        
        JLabel priceLabel = new JLabel("Total: Rs." + price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(ACCENT_GREEN);
        
        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        Color statusColor = status.equals("Pending") ? WARNING_COLOR : 
                           status.equals("Approved") ? SUCCESS_COLOR : DANGER_COLOR;
        statusLabel.setForeground(statusColor);
        
        infoPanel.add(orderLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(productLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(statusLabel);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        if ("Pending".equals(status)) {
            JButton approveBtn = new JButton("Approve");
            approveBtn.setBackground(SUCCESS_COLOR);
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            approveBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            approveBtn.setFocusPainted(false);
            approveBtn.addActionListener(e -> updateOrderStatus(orderId, "Approved"));
            
            JButton rejectBtn = new JButton("Reject");
            rejectBtn.setBackground(DANGER_COLOR);
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rejectBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            rejectBtn.setFocusPainted(false);
            rejectBtn.addActionListener(e -> updateOrderStatus(orderId, "Rejected"));
            
            buttonPanel.add(approveBtn);
            buttonPanel.add(rejectBtn);
        } else {
            JLabel processedLabel = new JLabel("Order " + status);
            processedLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            processedLabel.setForeground(statusColor);
            buttonPanel.add(processedLabel);
        }
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void updateOrderStatus(int orderId, String newStatus) {
        // Since the current orders table doesn't have a status column,
        // we'll simulate the approval/rejection process
        
        JOptionPane.showMessageDialog(this, 
            "Order #" + orderId + " has been " + newStatus.toLowerCase() + "!\n\n" +
            "Note: To fully implement this feature, add a 'status' column to the orders table.", 
            "Order " + newStatus, JOptionPane.INFORMATION_MESSAGE);
        
        // For demonstration, we'll refresh the view
        showOrderManagement();
        
        // TODO: Add status column to orders table and implement real update:
        // ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending';
        // Then use: UPDATE orders SET status = ? WHERE oid = ?
    }
    
    private void showAddMedicine() {
        JFrame addMedicineFrame = new JFrame("Add New Medicine");
        addMedicineFrame.setSize(500, 600);
        addMedicineFrame.setLocationRelativeTo(this);
        addMedicineFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Add New Medicine");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Medicine Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Medicine Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        styleTextField(nameField);
        formPanel.add(nameField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Pain Relief", "Fever", "Antibiotics", "Vitamins", "Heart Health", "Diabetes", "Other"
        });
        categoryCombo.setPreferredSize(new Dimension(200, 35));
        formPanel.add(categoryCombo, gbc);
        
        // Stock Quantity
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        JTextField stockField = new JTextField(20);
        styleTextField(stockField);
        formPanel.add(stockField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Price (Rs.):"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(20);
        styleTextField(priceField);
        formPanel.add(priceField, gbc);
        
        // Expiry Date
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Expiry Date:"), gbc);
        gbc.gridx = 1;
        JTextField expiryField = new JTextField(20);
        expiryField.setText("YYYY-MM-DD");
        styleTextField(expiryField);
        formPanel.add(expiryField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(200, 80));
        formPanel.add(descScroll, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addBtn = new JButton("Add Medicine");
        addBtn.setBackground(ACCENT_GREEN);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> {
            // Add medicine logic here
            JOptionPane.showMessageDialog(addMedicineFrame, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            addMedicineFrame.dispose();
            showInventoryManagement(); // Refresh inventory
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> addMedicineFrame.dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelBtn);
        
        formPanel.add(buttonPanel, gbc);
        
        addMedicineFrame.add(headerPanel, BorderLayout.NORTH);
        addMedicineFrame.add(formPanel, BorderLayout.CENTER);
        addMedicineFrame.setVisible(true);
    }
    
    private void showSalesAnalytics() {
        mainContentPanel.removeAll();
        
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBackground(LIGHT_GRAY);
        analyticsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Sales Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        JLabel comingSoonLabel = new JLabel("Advanced analytics dashboard coming soon...");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        analyticsPanel.add(titleLabel, BorderLayout.NORTH);
        analyticsPanel.add(comingSoonLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(analyticsPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showProfileSettings() {
        mainContentPanel.removeAll();
        
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(LIGHT_GRAY);
        profilePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Profile Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        JLabel comingSoonLabel = new JLabel("Profile management coming soon...");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        profilePanel.add(titleLabel, BorderLayout.NORTH);
        profilePanel.add(comingSoonLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(profilePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showNotifications() {
        mainContentPanel.removeAll();
        
        JPanel notificationsPanel = new JPanel(new BorderLayout());
        notificationsPanel.setBackground(LIGHT_GRAY);
        notificationsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        JLabel comingSoonLabel = new JLabel("Notification center coming soon...");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        notificationsPanel.add(titleLabel, BorderLayout.NORTH);
        notificationsPanel.add(comingSoonLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(notificationsPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
    
    private void loadVendorData() {
        // Load real data from database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            // Load inventory
            String inventoryQuery = "SELECT p.pid, p.pname, 'Medicine' as category, i.quantity, p.price, p.exp " +
                                   "FROM product p JOIN inventory i ON p.pid = i.pid WHERE i.sid = ?";
            PreparedStatement ps1 = conn.prepareStatement(inventoryQuery);
            ps1.setString(1, currentVendor);
            ResultSet rs1 = ps1.executeQuery();
            
            while (rs1.next()) {
                inventory.add(new Medicine(
                    rs1.getString("pid"),
                    rs1.getString("pname"),
                    rs1.getString("category"),
                    rs1.getInt("quantity"),
                    rs1.getDouble("price"),
                    rs1.getString("exp")
                ));
            }
            
            // Load orders for this vendor
            String ordersQuery = "SELECT oid, uid, pid, quantity, price FROM orders WHERE sid = ?";
            PreparedStatement ps2 = conn.prepareStatement(ordersQuery);
            ps2.setString(1, currentVendor);
            ResultSet rs2 = ps2.executeQuery();
            
            while (rs2.next()) {
                pendingOrders.add(new Order(
                    "ORD" + rs2.getInt("oid"),
                    rs2.getString("uid"),
                    rs2.getString("pid"),
                    rs2.getDouble("price"),
                    "Pending" // Default status
                ));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading vendor data: " + e.getMessage());
            // Load mock data as fallback
            inventory.add(new Medicine("MED001", "Paracetamol 500mg", "Pain Relief", 250, 25.0, "2025-12-31"));
            inventory.add(new Medicine("MED002", "Crocin Advance", "Fever", 180, 35.0, "2025-10-15"));
            inventory.add(new Medicine("MED003", "Dolo 650", "Pain Relief", 120, 30.0, "2025-11-20"));
            
            pendingOrders.add(new Order("ORD001", "aanchal01", "Paracetamol, Crocin", 60.0, "Pending"));
            pendingOrders.add(new Order("ORD002", "shagun02", "Dolo 650", 30.0, "Processing"));
        }
    }
    
    private void setupEventHandlers() {
        // Header button functionality
        JPanel headerPanel = (JPanel) getContentPane().getComponent(0);
        JPanel actionsPanel = (JPanel) headerPanel.getComponent(2);
        
        // Orders button
        JButton ordersBtn = (JButton) actionsPanel.getComponent(2);
        ordersBtn.addActionListener(e -> showOrderManagement());
        
        // Reports button
        JButton reportsBtn = (JButton) actionsPanel.getComponent(4);
        reportsBtn.addActionListener(e -> showSalesAnalytics());
        
        // Logout button
        JButton logoutBtn = (JButton) actionsPanel.getComponent(6);
        logoutBtn.addActionListener(e -> performLogout());
    }
    
    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new com.emedpharma.common.MainApplication();
        }
    }
    
    // Helper methods
    private String getTimeOfDay() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12) return "Morning";
        else if (hour < 17) return "Afternoon";
        else return "Evening";
    }
    
    private String getTotalSales() {
        return "15,450"; // Mock data
    }
    
    private int getLowStockCount() {
        return 3; // Mock data
    }
    
    // Inner classes
    private static class Medicine {
        private String id, name, category, expiryDate;
        private int stock;
        private double price;
        
        public Medicine(String id, String name, String category, int stock, double price, String expiryDate) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.stock = stock;
            this.price = price;
            this.expiryDate = expiryDate;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getStock() { return stock; }
        public double getPrice() { return price; }
        public String getExpiryDate() { return expiryDate; }
    }
    
    private static class Order {
        private String orderId, customerId, items, status;
        private double amount;
        
        public Order(String orderId, String customerId, String items, double amount, String status) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.items = items;
            this.amount = amount;
            this.status = status;
        }
        
        // Getters
        public String getOrderId() { return orderId; }
        public String getCustomerId() { return customerId; }
        public String getItems() { return items; }
        public double getAmount() { return amount; }
        public String getStatus() { return status; }
    }
}