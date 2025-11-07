package com.emedpharma.gui;

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
        
        JLabel titleLabel = new JLabel("Inventory Management");
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
        
        // Inventory Table
        String[] columnNames = {"Medicine Name", "Category", "Stock", "Price", "Expiry Date", "Actions"};
        Object[][] data = {
            {"Paracetamol 500mg", "Pain Relief", "250", "Rs.25", "2025-12-31", "Edit | Delete"},
            {"Crocin Advance", "Fever", "180", "Rs.35", "2025-10-15", "Edit | Delete"},
            {"Dolo 650", "Pain Relief", "120", "Rs.30", "2025-11-20", "Edit | Delete"},
            {"Aspirin 75mg", "Heart Health", "90", "Rs.45", "2025-09-10", "Edit | Delete"}
        };
        
        JTable inventoryTable = new JTable(data, columnNames);
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inventoryTable.setRowHeight(30);
        inventoryTable.getTableHeader().setBackground(LIGHT_GREEN);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        inventoryPanel.add(headerPanel, BorderLayout.NORTH);
        inventoryPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        inventoryPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(inventoryPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showOrderManagement() {
        mainContentPanel.removeAll();
        
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBackground(LIGHT_GRAY);
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        
        // Orders Table
        String[] columnNames = {"Order ID", "Customer", "Items", "Amount", "Status", "Date", "Actions"};
        Object[][] data = {
            {"ORD001", "aanchal01", "Paracetamol, Crocin", "Rs.60", "Pending", "2024-01-22", "Process | Cancel"},
            {"ORD002", "shagun02", "Dolo 650", "Rs.30", "Processing", "2024-01-22", "Ship | Cancel"},
            {"ORD003", "dhara03", "Aspirin", "Rs.45", "Shipped", "2024-01-21", "Track | View"}
        };
        
        JTable ordersTable = new JTable(data, columnNames);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ordersTable.setRowHeight(35);
        ordersTable.getTableHeader().setBackground(LIGHT_GREEN);
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        ordersPanel.add(titleLabel, BorderLayout.NORTH);
        ordersPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        ordersPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(ordersPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
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
        // Load mock data
        inventory.add(new Medicine("MED001", "Paracetamol 500mg", "Pain Relief", 250, 25.0, "2025-12-31"));
        inventory.add(new Medicine("MED002", "Crocin Advance", "Fever", 180, 35.0, "2025-10-15"));
        inventory.add(new Medicine("MED003", "Dolo 650", "Pain Relief", 120, 30.0, "2025-11-20"));
        
        pendingOrders.add(new Order("ORD001", "aanchal01", "Paracetamol, Crocin", 60.0, "Pending"));
        pendingOrders.add(new Order("ORD002", "shagun02", "Dolo 650", 30.0, "Processing"));
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
            new com.emedpharma.gui.MainApplication();
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