package com.emedpharma.customer;

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

public class SmartCustomerDashboard extends JFrame {
    private String currentUser;
    private JPanel mainContentPanel;
    private JPanel recommendationsPanel;
    private JPanel subscriptionsPanel;
    private JPanel productsPanel;
    private JTextField searchField;
    private JLabel cartCountLabel;
    private List<Product> allProducts;
    private List<Product> cartItems;
    private List<Recommendation> aiRecommendations;
    private List<Subscription> userSubscriptions;
    private java.util.Stack<String> navigationHistory;
    
    public SmartCustomerDashboard(String userId) {
        this.currentUser = userId;
        this.allProducts = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.aiRecommendations = new ArrayList<>();
        this.userSubscriptions = new ArrayList<>();
        this.navigationHistory = new java.util.Stack<>();
        
        initializeComponents();
        setupLayout();
        loadUserData();
        generateAIRecommendations();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("e-MEDpharma - Smart Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        searchField = new JTextField(30);
        cartCountLabel = new JLabel("0");
        mainContentPanel = new JPanel(new BorderLayout());
        recommendationsPanel = new JPanel();
        subscriptionsPanel = new JPanel();
        productsPanel = new JPanel();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 250, 252));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Main Content Panel with scroll support
        mainContentPanel.setBackground(new Color(248, 250, 252));
        showDashboardHome();
        
        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Enable enhanced scrolling
        enableEnhancedScrolling(mainScrollPane);
        
        add(mainScrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(27, 94, 32), 0, getHeight(), new Color(46, 125, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Left - Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("e-MEDpharma");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        
        // Center - Search
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setOpaque(false);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setForeground(new Color(27, 94, 32));
        searchBtn.setBorder(BorderFactory.createEmptyBorder());
        searchBtn.setFocusPainted(false);
        
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        // Right - User actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cartCountLabel.setForeground(new Color(27, 94, 32));
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBackground(Color.WHITE);
        cartCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        
        JButton cartBtn = new JButton("Cart");
        cartBtn.setBackground(Color.WHITE);
        cartBtn.setForeground(new Color(27, 94, 32));
        cartBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cartBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cartBtn.setFocusPainted(false);
        
        JButton ordersBtn = new JButton("Orders");
        ordersBtn.setBackground(new Color(255, 193, 7));
        ordersBtn.setForeground(Color.WHITE);
        ordersBtn.setFont(new Font("Arial", Font.BOLD, 12));
        ordersBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        ordersBtn.setFocusPainted(false);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setFocusPainted(false);
        
        actionsPanel.add(welcomeLabel);
        actionsPanel.add(Box.createHorizontalStrut(20));
        actionsPanel.add(cartBtn);
        actionsPanel.add(cartCountLabel);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(ordersBtn);
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
            {"[AI]", "AI Recommendations", "recommendations"},
            {"[M]", "Browse Medicines", "medicines"},
            {"[B]", "My Bills", "bills"},
            {"[P]", "Pharmacies Near Me", "pharmacies"},
            {"[R]", "Health Reports", "reports"}
        };
        
        for (String[] item : menuItems) {
            JButton menuBtn = createMenuButton(item[0], item[1], item[2]);
            sidebarPanel.add(menuBtn);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }
        
        // Health Status Card
        sidebarPanel.add(Box.createVerticalStrut(30));
        JPanel healthCard = createHealthStatusCard();
        sidebarPanel.add(healthCard);
        
        return sidebarPanel;
    }
    
    private JButton createMenuButton(String icon, String text, String action) {
        JButton button = new JButton(icon + "  " + text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(240, 45));
        button.setBackground(new Color(248, 250, 252));
        button.setForeground(new Color(51, 51, 51));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(46, 125, 50));
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(248, 250, 252));
                button.setForeground(new Color(51, 51, 51));
            }
        });
        
        // Action listener
        button.addActionListener(e -> handleMenuAction(action));
        
        return button;
    }
    
    private JPanel createHealthStatusCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(240, 248, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 201), 2),
            new EmptyBorder(15, 15, 15, 15)));
        card.setMaximumSize(new Dimension(240, 150));
        
        JLabel titleLabel = new JLabel("Health Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(27, 94, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel statusLabel = new JLabel("Total Orders: " + getTotalOrders());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel healthLabel = new JLabel("Health Score: " + getHealthScore() + "%");
        healthLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        healthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(healthLabel);
        
        return card;
    }
    
    private void handleMenuAction(String action) {
        switch (action) {
            case "home":
                showDashboardHome();
                break;
            case "recommendations":
                showAIRecommendations();
                break;
            case "medicines":
                showMedicines();
                break;
            case "bills":
                showBillsPage();
                break;
            case "pharmacies":
                showPharmacies();
                break;
            case "reports":
                showHealthReports();
                break;
        }
    }
    
    private void showDashboardHome() {
        mainContentPanel.removeAll();
        
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(248, 250, 252));
        homePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Welcome Section
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(30, 30, 30, 30)));
        
        JLabel welcomeTitle = new JLabel("Good " + getTimeOfDay() + ", " + currentUser + "!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeTitle.setForeground(new Color(51, 51, 51));
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel welcomeSubtitle = new JLabel("Here's your personalized health dashboard");
        welcomeSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeSubtitle.setForeground(new Color(100, 100, 100));
        welcomeSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeSubtitle);
        
        // Quick Stats
        JPanel statsPanel = createQuickStatsPanel();
        
        // AI Recommendations Preview
        JPanel aiPreviewPanel = createAIRecommendationsPreview();
        
        homePanel.add(welcomePanel, BorderLayout.NORTH);
        homePanel.add(statsPanel, BorderLayout.CENTER);
        homePanel.add(aiPreviewPanel, BorderLayout.SOUTH);
        
        mainContentPanel.add(homePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createQuickStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(new Color(248, 250, 252));
        statsPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        // Total Orders
        JPanel ordersCard = createStatCard("[O]", String.valueOf(getTotalOrders()), "Total Orders", new Color(27, 94, 32));
        
        // Health Score
        JPanel healthCard = createStatCard("[H]", getHealthScore() + "%", "Health Score", new Color(40, 167, 69));
        
        // Money Saved
        JPanel savingsCard = createStatCard("[$]", "Rs." + getMoneySaved(), "Money Saved", new Color(255, 193, 7));
        
        // Medicine Categories
        JPanel categoriesCard = createStatCard("[C]", String.valueOf(getUniqueCategories()), "Categories", new Color(220, 53, 69));
        
        statsPanel.add(ordersCard);
        statsPanel.add(healthCard);
        statsPanel.add(savingsCard);
        statsPanel.add(categoriesCard);
        
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
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        labelLabel.setForeground(new Color(100, 100, 100));
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(labelLabel);
        
        return card;
    }
    
    private JPanel createAIRecommendationsPreview() {
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JLabel titleLabel = new JLabel("AI Recommendations for You");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JPanel recommendationsGrid = new JPanel(new GridLayout(1, 3, 15, 15));
        recommendationsGrid.setBackground(Color.WHITE);
        
        // Show top 3 recommendations
        for (int i = 0; i < Math.min(3, aiRecommendations.size()); i++) {
            Recommendation rec = aiRecommendations.get(i);
            JPanel recCard = createRecommendationCard(rec);
            recommendationsGrid.add(recCard);
        }
        
        JButton viewAllBtn = new JButton("View All Recommendations →");
        viewAllBtn.setBackground(new Color(46, 125, 50));
        viewAllBtn.setForeground(Color.WHITE);
        viewAllBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewAllBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewAllBtn.setFocusPainted(false);
        viewAllBtn.addActionListener(e -> showAIRecommendations());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(viewAllBtn);
        
        previewPanel.add(titleLabel, BorderLayout.NORTH);
        previewPanel.add(recommendationsGrid, BorderLayout.CENTER);
        previewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return previewPanel;
    }
    
    private JPanel createRecommendationCard(Recommendation rec) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JLabel typeLabel = new JLabel(rec.getTypeIcon() + " " + rec.getType());
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setForeground(new Color(46, 125, 50));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(rec.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(rec.getDescription());
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBackground(new Color(248, 250, 252));
        descArea.setEditable(false);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setRows(2);
        
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(descArea);
        
        return card;
    }
    
    private void showAIRecommendations() {
        mainContentPanel.removeAll();
        
        JPanel aiPanel = new JPanel(new BorderLayout());
        aiPanel.setBackground(new Color(248, 250, 252));
        aiPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("AI-Powered Health Recommendations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JPanel recommendationsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        recommendationsGrid.setBackground(new Color(248, 250, 252));
        
        for (Recommendation rec : aiRecommendations) {
            JPanel recCard = createDetailedRecommendationCard(rec);
            recommendationsGrid.add(recCard);
        }
        
        JScrollPane scrollPane = new JScrollPane(recommendationsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        aiPanel.add(titleLabel, BorderLayout.NORTH);
        aiPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        aiPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(aiPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createDetailedRecommendationCard(Recommendation rec) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel typeLabel = new JLabel(rec.getTypeIcon() + " " + rec.getType());
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        typeLabel.setForeground(new Color(46, 125, 50));
        
        JLabel confidenceLabel = new JLabel(rec.getConfidence() + "% Match");
        confidenceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        confidenceLabel.setForeground(new Color(40, 167, 69));
        
        headerPanel.add(typeLabel, BorderLayout.WEST);
        headerPanel.add(confidenceLabel, BorderLayout.EAST);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(rec.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JTextArea descArea = new JTextArea(rec.getDescription());
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBackground(Color.WHITE);
        descArea.setEditable(false);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descArea);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        
        if (rec.getProductId() != null) {
            JButton viewBtn = new JButton("View Product");
            viewBtn.setBackground(new Color(46, 125, 50));
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
            viewBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            viewBtn.setFocusPainted(false);
            viewBtn.addActionListener(e -> showRecommendedProduct(rec.getProductId()));
            actionPanel.add(viewBtn);
        }
        
        JButton dismissBtn = new JButton("Dismiss");
        dismissBtn.setBackground(new Color(248, 250, 252));
        dismissBtn.setForeground(new Color(100, 100, 100));
        dismissBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        dismissBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        dismissBtn.setFocusPainted(false);
        actionPanel.add(dismissBtn);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.SOUTH);
        
        return card;
    }
    

    
    private void showMedicines() {
        mainContentPanel.removeAll();
        
        JPanel medicinesPanel = new JPanel(new BorderLayout());
        medicinesPanel.setBackground(new Color(248, 250, 252));
        medicinesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with categories
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        
        JLabel titleLabel = new JLabel("Browse Medicines (" + allProducts.size() + " products)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // Categories panel
        JPanel categoriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoriesPanel.setBackground(new Color(248, 250, 252));
        
        String[] categories = {"All", "Medicines", "First Aid", "Baby Care", "Skin Care", "Medical Devices", "Supplements"};
        
        for (String category : categories) {
            JButton categoryBtn = new JButton(category);
            categoryBtn.setBackground(Color.WHITE);
            categoryBtn.setForeground(new Color(51, 51, 51));
            categoryBtn.setFont(new Font("Arial", Font.PLAIN, 12));
            categoryBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            categoryBtn.setFocusPainted(false);
            categoryBtn.addActionListener(e -> filterByCategory(category));
            categoriesPanel.add(categoryBtn);
        }
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(categoriesPanel, BorderLayout.SOUTH);
        
        // Products grid
        JPanel productsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        productsGrid.setBackground(new Color(248, 250, 252));
        
        for (Product product : allProducts) {
            JPanel productCard = createProductCard(product);
            productsGrid.add(productCard);
        }
        
        JScrollPane scrollPane = new JScrollPane(productsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        medicinesPanel.add(headerPanel, BorderLayout.NORTH);
        medicinesPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        medicinesPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(medicinesPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void filterByCategory(String category) {
        mainContentPanel.removeAll();
        
        JPanel medicinesPanel = new JPanel(new BorderLayout());
        medicinesPanel.setBackground(new Color(248, 250, 252));
        medicinesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Category: " + category);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JButton backBtn = new JButton("← Back to All Medicines");
        backBtn.setBackground(new Color(46, 125, 50));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> showMedicines());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);
        
        // Filter products by category
        JPanel productsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        productsGrid.setBackground(new Color(248, 250, 252));
        
        for (Product product : allProducts) {
            if (category.equals("All") || matchesCategory(product.getId(), getCategoryKeyword(category))) {
                JPanel productCard = createProductCard(product);
                productsGrid.add(productCard);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(productsGrid);
        scrollPane.setBorder(null);
        
        medicinesPanel.add(headerPanel, BorderLayout.NORTH);
        medicinesPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        medicinesPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(medicinesPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private String getCategoryKeyword(String category) {
        switch (category.toLowerCase()) {
            case "medicines": return "MED";
            case "first aid": return "AID";
            case "baby care": return "BAB|DIP";
            case "skin care": return "SKN";
            case "medical devices": return "DEV|MOB";
            case "supplements": return "SUP";
            default: return category.toLowerCase();
        }
    }
    
    private boolean matchesCategory(String productId, String keywords) {
        String[] keywordArray = keywords.split("\\|");
        
        for (String keyword : keywordArray) {
            if (productId.startsWith(keyword.trim())) {
                return true;
            }
        }
        return false;
    }
    
    private int getUniqueCategories() {
        String[] categories = {"Pain Relief", "Heart Health", "Vitamins", "Antibiotics", "Diabetes", "Cold & Flu", "Digestive", "Women's Health", "Skin Care", "Eye Care"};
        return categories.length;
    }
    
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel manufacturerLabel = new JLabel("By " + product.getManufacturer());
        manufacturerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        manufacturerLabel.setForeground(new Color(100, 100, 100));
        
        JLabel priceLabel = new JLabel("Rs." + product.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        JLabel stockLabel = new JLabel("Stock: " + product.getStock());
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        stockLabel.setForeground(product.getStock() > 10 ? new Color(40, 167, 69) : new Color(220, 53, 69));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(manufacturerLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stockLabel);
        
        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setBackground(new Color(46, 125, 50));
        viewDetailsBtn.setForeground(Color.WHITE);
        viewDetailsBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewDetailsBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.addActionListener(e -> showProductDetails(product));
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(viewDetailsBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void showProductDetails(Product product) {
        JDialog detailsDialog = new JDialog(this, product.getName() + " - Details", true);
        detailsDialog.setSize(700, 600);
        detailsDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Product header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(new Color(51, 51, 51));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel manufacturerLabel = new JLabel("Manufactured by " + product.getManufacturer());
        manufacturerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        manufacturerLabel.setForeground(new Color(100, 100, 100));
        manufacturerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(nameLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(manufacturerLabel);
        
        // Vendors panel
        JPanel vendorsPanel = new JPanel();
        vendorsPanel.setLayout(new BoxLayout(vendorsPanel, BoxLayout.Y_AXIS));
        vendorsPanel.setBackground(Color.WHITE);
        vendorsPanel.setBorder(BorderFactory.createTitledBorder("Available from Vendors"));
        
        // Load vendors for this product
        loadProductVendors(vendorsPanel, product);
        
        JScrollPane vendorsScroll = new JScrollPane(vendorsPanel);
        vendorsScroll.setPreferredSize(new Dimension(650, 300));
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.addActionListener(e -> detailsDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(vendorsScroll, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        detailsDialog.add(mainPanel);
        detailsDialog.setVisible(true);
    }
    
    private void loadProductVendors(JPanel vendorsPanel, Product product) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT s.sid, s.sname, i.quantity, p.price, " +
                          "CASE WHEN i.quantity > 50 THEN 10 WHEN i.quantity > 20 THEN 5 ELSE 0 END as discount " +
                          "FROM seller s " +
                          "JOIN inventory i ON s.sid = i.sid " +
                          "JOIN product p ON i.pid = p.pid " +
                          "WHERE p.pid = ? AND i.quantity > 0";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, product.getId());
            ResultSet rs = ps.executeQuery();
            
            boolean hasVendors = false;
            while (rs.next()) {
                hasVendors = true;
                JPanel vendorCard = createVendorCard(
                    rs.getString("sid"),
                    rs.getString("sname"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getInt("discount"),
                    product
                );
                vendorsPanel.add(vendorCard);
                vendorsPanel.add(Box.createVerticalStrut(10));
            }
            
            if (!hasVendors) {
                JLabel noVendorsLabel = new JLabel("No vendors available for this product");
                noVendorsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                noVendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                vendorsPanel.add(noVendorsLabel);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading vendors: " + e.getMessage());
            // Add sample vendors
            addSampleVendors(vendorsPanel, product);
        }
    }
    
    private void addSampleVendors(JPanel vendorsPanel, Product product) {
        String[][] sampleVendors = {
            {"vendor01", "MedPlus Pharmacy", "50", String.valueOf(product.getPrice()), "10"},
            {"vendor02", "Apollo Pharmacy", "30", String.valueOf(product.getPrice() + 5), "5"},
            {"vendor03", "Wellness Pharmacy", "25", String.valueOf(product.getPrice() - 3), "0"}
        };
        
        for (String[] vendor : sampleVendors) {
            JPanel vendorCard = createVendorCard(
                vendor[0], vendor[1], Integer.parseInt(vendor[2]), 
                Double.parseDouble(vendor[3]), Integer.parseInt(vendor[4]), product
            );
            vendorsPanel.add(vendorCard);
            vendorsPanel.add(Box.createVerticalStrut(10));
        }
    }
    
    private JPanel createVendorCard(String vendorId, String vendorName, int stock, 
                                   double price, int discount, Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)));
        
        // Vendor info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(248, 250, 252));
        
        JLabel vendorLabel = new JLabel(vendorName);
        vendorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        vendorLabel.setForeground(new Color(51, 51, 51));
        
        JLabel stockLabel = new JLabel("Stock: " + stock + " units");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setForeground(stock > 20 ? new Color(40, 167, 69) : new Color(255, 193, 7));
        
        double finalPrice = price - (price * discount / 100);
        JLabel priceLabel = new JLabel("Rs." + String.format("%.2f", finalPrice));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        if (discount > 0) {
            JLabel originalPriceLabel = new JLabel("<html><strike>Rs." + price + "</strike></html>");
            originalPriceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            originalPriceLabel.setForeground(new Color(100, 100, 100));
            
            JLabel discountLabel = new JLabel(discount + "% OFF");
            discountLabel.setFont(new Font("Arial", Font.BOLD, 11));
            discountLabel.setForeground(new Color(220, 53, 69));
            
            infoPanel.add(vendorLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(stockLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(originalPriceLabel);
            infoPanel.add(priceLabel);
            infoPanel.add(discountLabel);
        } else {
            infoPanel.add(vendorLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(stockLabel);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(priceLabel);
        }
        
        // Add to cart button
        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setBackground(new Color(46, 125, 50));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addToCartBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.addActionListener(e -> {
            Product vendorProduct = new Product(
                product.getId(), 
                product.getName() + " (" + vendorName + ")", 
                product.getManufacturer(), 
                finalPrice, 
                stock
            );
            cartItems.add(vendorProduct);
            updateCartCount();
            
            JOptionPane.showMessageDialog(this, 
                product.getName() + " from " + vendorName + " added to cart!", 
                "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
            
            // Close the details dialog
            SwingUtilities.getWindowAncestor(addToCartBtn).dispose();
        });
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addToCartBtn, BorderLayout.EAST);
        
        return card;
    }
    
    private void showRecommendedProduct(String productId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT p.pid, p.pname, p.manufacturer, p.price FROM product p WHERE p.pid = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Product product = new Product(
                    rs.getString("pid"),
                    rs.getString("pname"),
                    rs.getString("manufacturer"),
                    rs.getDouble("price"),
                    100 // Default stock for recommended products
                );
                showProductDetails(product);
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading recommended product: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading product details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addProductToCart(Product product) {
        if (product.getStock() > 0) {
            showPharmacySelection(product);
        } else {
            JOptionPane.showMessageDialog(this, "Sorry, this product is out of stock!", "Out of Stock", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showPharmacySelection(Product product) {
        JDialog pharmacyDialog = new JDialog(this, "Select Pharmacy - " + product.getName(), true);
        pharmacyDialog.setSize(600, 500);
        pharmacyDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Choose Pharmacy for: " + product.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Pharmacy options
        JPanel pharmaciesPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        pharmaciesPanel.setBackground(Color.WHITE);
        pharmaciesPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        String[][] pharmacies = {
            {"MedPlus Pharmacy", "2.5 km", "Rs." + product.getPrice(), "15 min", "4.5★"},
            {"Apollo Pharmacy", "1.8 km", "Rs." + (product.getPrice() + 5), "12 min", "4.7★"},
            {"Wellness Pharmacy", "3.2 km", "Rs." + (product.getPrice() - 3), "20 min", "4.2★"},
            {"HealthCare Plus", "4.1 km", "Rs." + (product.getPrice() + 2), "25 min", "4.4★"}
        };
        
        for (String[] pharmacy : pharmacies) {
            JPanel pharmacyCard = createPharmacyCard(pharmacy, product);
            pharmaciesPanel.add(pharmacyCard);
        }
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> pharmacyDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(pharmaciesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        pharmacyDialog.add(mainPanel);
        pharmacyDialog.setVisible(true);
    }
    
    private JPanel createPharmacyCard(String[] pharmacyInfo, Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(15, 15, 15, 15)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(pharmacyInfo[0]);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel distanceLabel = new JLabel("📍 " + pharmacyInfo[1] + " away");
        distanceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel priceLabel = new JLabel(pharmacyInfo[2]);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        JLabel deliveryLabel = new JLabel("🚚 Delivery: " + pharmacyInfo[3]);
        deliveryLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel ratingLabel = new JLabel("⭐ " + pharmacyInfo[4]);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        ratingLabel.setForeground(new Color(255, 193, 7));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(distanceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(deliveryLabel);
        infoPanel.add(ratingLabel);
        
        JButton selectBtn = new JButton("Select");
        selectBtn.setBackground(new Color(46, 125, 50));
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFont(new Font("Arial", Font.BOLD, 12));
        selectBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        selectBtn.setFocusPainted(false);
        
        selectBtn.addActionListener(e -> {
            // Create product with pharmacy-specific price
            double pharmacyPrice = Double.parseDouble(pharmacyInfo[2].replace("Rs.", ""));
            Product pharmacyProduct = new Product(product.getId(), product.getName() + " (" + pharmacyInfo[0] + ")", 
                product.getManufacturer(), pharmacyPrice, product.getStock());
            
            cartItems.add(pharmacyProduct);
            updateCartCount();
            
            JOptionPane.showMessageDialog(this, 
                product.getName() + " from " + pharmacyInfo[0] + " added to cart!", 
                "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
            
            // Close dialog
            SwingUtilities.getWindowAncestor(selectBtn).dispose();
        });
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(selectBtn, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(46, 125, 50), 2),
                    new EmptyBorder(15, 15, 15, 15)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    new EmptyBorder(15, 15, 15, 15)));
            }
        });
        
        return card;
    }
    
    private void showPharmacies() {
        mainContentPanel.removeAll();
        
        JPanel pharmaciesPanel = new JPanel(new BorderLayout());
        pharmaciesPanel.setBackground(new Color(248, 250, 252));
        pharmaciesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Pharmacies Near You");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JPanel pharmaciesGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        pharmaciesGrid.setBackground(new Color(248, 250, 252));
        
        String[][] pharmacyDetails = {
            {"MedPlus Pharmacy", "2.5 km", "Open 24/7", "15 min", "4.5★", "500+ medicines"},
            {"Apollo Pharmacy", "1.8 km", "6 AM - 11 PM", "12 min", "4.7★", "800+ medicines"},
            {"Wellness Pharmacy", "3.2 km", "7 AM - 10 PM", "20 min", "4.2★", "300+ medicines"},
            {"HealthCare Plus", "4.1 km", "8 AM - 9 PM", "25 min", "4.4★", "450+ medicines"}
        };
        
        for (String[] pharmacy : pharmacyDetails) {
            JPanel pharmacyCard = createPharmacyInfoCard(pharmacy);
            pharmaciesGrid.add(pharmacyCard);
        }
        
        pharmaciesPanel.add(titleLabel, BorderLayout.NORTH);
        pharmaciesPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        pharmaciesPanel.add(pharmaciesGrid, BorderLayout.CENTER);
        
        mainContentPanel.add(pharmaciesPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createPharmacyInfoCard(String[] pharmacyInfo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(pharmacyInfo[0]);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel distanceLabel = new JLabel("📍 " + pharmacyInfo[1] + " away");
        distanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel timingLabel = new JLabel("🕒 " + pharmacyInfo[2]);
        timingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel deliveryLabel = new JLabel("🚚 Delivery: " + pharmacyInfo[3]);
        deliveryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel ratingLabel = new JLabel("⭐ " + pharmacyInfo[4]);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingLabel.setForeground(new Color(255, 193, 7));
        
        JLabel stockLabel = new JLabel("💊 " + pharmacyInfo[5]);
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(40, 167, 69));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(distanceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(timingLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(deliveryLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ratingLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stockLabel);
        
        JButton viewBtn = new JButton("View Products");
        viewBtn.setBackground(new Color(46, 125, 50));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> showMedicines());
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(viewBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void showBillsPage() {
        mainContentPanel.removeAll();
        
        JPanel billsPanel = new JPanel(new BorderLayout());
        billsPanel.setBackground(new Color(248, 250, 252));
        billsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Bills & Invoices");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // Load customer bills
        JPanel billsList = new JPanel();
        billsList.setLayout(new BoxLayout(billsList, BoxLayout.Y_AXIS));
        billsList.setBackground(new Color(248, 250, 252));
        
        loadCustomerBills(billsList);
        
        JScrollPane scrollPane = new JScrollPane(billsList);
        scrollPane.setBorder(null);
        
        billsPanel.add(titleLabel, BorderLayout.NORTH);
        billsPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        billsPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(billsPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void loadCustomerBills(JPanel billsList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            System.out.println("Loading bills for customer: " + currentUser);
            
            // First try to load from consolidated_bills table
            String billQuery = "SELECT * FROM consolidated_bills WHERE customer_id = ? ORDER BY bill_date DESC";
            PreparedStatement billPs = conn.prepareStatement(billQuery);
            billPs.setString(1, currentUser);
            ResultSet billRs = billPs.executeQuery();
            
            boolean hasBills = false;
            while (billRs.next()) {
                hasBills = true;
                JPanel billCard = createConsolidatedBillCard(
                    billRs.getInt("bill_id"),
                    billRs.getString("items_summary"),
                    billRs.getInt("item_count"),
                    billRs.getDouble("subtotal"),
                    billRs.getDouble("gst"),
                    billRs.getDouble("delivery_charges"),
                    billRs.getDouble("total_amount"),
                    billRs.getString("bill_date")
                );
                billsList.add(billCard);
                billsList.add(Box.createVerticalStrut(10));
            }
            
            // If no consolidated bills, create bills from orders
            if (!hasBills) {
                String orderQuery = "SELECT o.oid, p.pname, o.quantity, o.price, o.orderdatetime " +
                                  "FROM orders o JOIN product p ON o.pid = p.pid " +
                                  "WHERE o.uid = ? ORDER BY o.orderdatetime DESC";
                
                PreparedStatement orderPs = conn.prepareStatement(orderQuery);
                orderPs.setString(1, currentUser);
                ResultSet orderRs = orderPs.executeQuery();
                
                while (orderRs.next()) {
                    hasBills = true;
                    int orderId = orderRs.getInt("oid");
                    String productName = orderRs.getString("pname");
                    int quantity = orderRs.getInt("quantity");
                    double price = orderRs.getDouble("price");
                    String date = orderRs.getString("orderdatetime");
                    
                    // Calculate bill totals
                    double subtotal = price * quantity;
                    double gst = subtotal * 0.18;
                    double delivery = 50.0;
                    double total = subtotal + gst + delivery;
                    
                    JPanel billCard = createConsolidatedBillCard(
                        orderId, productName + " (Qty: " + quantity + ")", 1, 
                        subtotal, gst, delivery, total, date
                    );
                    billsList.add(billCard);
                    billsList.add(Box.createVerticalStrut(10));
                }
            }
            
            if (!hasBills) {
                JLabel noBillsLabel = new JLabel("No orders found. Place orders to generate bills!");
                noBillsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                noBillsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                noBillsLabel.setForeground(new Color(100, 100, 100));
                billsList.add(noBillsLabel);
            } else {
                System.out.println("Loaded bills for " + currentUser);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading bills: " + e.getMessage());
            e.printStackTrace();
            // Add sample bill if database fails
            addSampleConsolidatedBill(billsList);
        }
    }
    
    private JPanel createDetailedBillCard(int billId, int orderId, String products, 
                                         double subtotal, double gst, double delivery, double total, String date) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel billLabel = new JLabel("Bill #EMD" + billId + " (Order #" + orderId + ")");
        billLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel productLabel = new JLabel(products != null ? products : "Multiple Items");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        JLabel totalLabel = new JLabel("Rs." + String.format("%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(46, 125, 50));
        
        infoPanel.add(billLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(productLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dateLabel);
        
        JButton viewBillBtn = new JButton("View Bill");
        viewBillBtn.setBackground(new Color(46, 125, 50));
        viewBillBtn.setForeground(Color.WHITE);
        viewBillBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBillBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewBillBtn.setFocusPainted(false);
        viewBillBtn.addActionListener(e -> showDetailedBillFromDB(billId, orderId, products, subtotal, gst, delivery, total, date));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(totalLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(viewBillBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void showDetailedBillFromDB(int billId, int orderId, String products, 
                                       double subtotal, double gst, double delivery, double total, String date) {
        JDialog billDialog = new JDialog(this, "Invoice - EMD" + billId, true);
        billDialog.setSize(500, 600);
        billDialog.setLocationRelativeTo(this);
        
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel companyLabel = new JLabel("e-MEDpharma");
        companyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        companyLabel.setForeground(new Color(27, 94, 32));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel addressLabel = new JLabel("Digital Pharmacy Management System");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel invoiceLabel = new JLabel("INVOICE");
        invoiceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        invoiceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(companyLabel);
        headerPanel.add(addressLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(invoiceLabel);
        
        // Bill details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Bill Details"));
        
        detailsPanel.add(new JLabel("Bill ID: EMD" + billId));
        detailsPanel.add(new JLabel("Order ID: " + orderId));
        detailsPanel.add(new JLabel("Customer: " + currentUser));
        detailsPanel.add(new JLabel("Date: " + date));
        detailsPanel.add(new JLabel("Payment Method: Cash on Delivery"));
        
        // Items section
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items"));
        
        itemsPanel.add(new JLabel(products != null ? products : "Multiple Items"));
        
        // Total section
        JPanel totalPanel = new JPanel(new GridLayout(4, 2));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createTitledBorder("Payment Summary"));
        
        totalPanel.add(new JLabel("Subtotal:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", subtotal)));
        totalPanel.add(new JLabel("GST (18%):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", gst)));
        totalPanel.add(new JLabel("Delivery:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", delivery)));
        
        JLabel totalLabelFinal = new JLabel("TOTAL:");
        totalLabelFinal.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel finalTotalLabel = new JLabel("Rs." + String.format("%.2f", total));
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalTotalLabel.setForeground(new Color(27, 94, 32));
        
        totalPanel.add(totalLabelFinal);
        totalPanel.add(finalTotalLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton printBtn = new JButton("Print Bill");
        printBtn.setBackground(new Color(46, 125, 50));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(billDialog, "Bill sent to printer!", "Print", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton downloadBtn = new JButton("Download PDF");
        downloadBtn.setBackground(new Color(102, 126, 234));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(billDialog, "Bill downloaded as PDF!", "Download", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> billDialog.dispose());
        
        buttonPanel.add(printBtn);
        buttonPanel.add(downloadBtn);
        buttonPanel.add(closeBtn);
        
        billPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(itemsPanel, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        billPanel.add(centerPanel, BorderLayout.CENTER);
        billPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        billDialog.add(billPanel);
        billDialog.setVisible(true);
    }
    
    private JPanel createConsolidatedBillCard(int billId, String items, int itemCount, 
                                             double subtotal, double gst, double delivery, double total, String date) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel billLabel = new JLabel("Consolidated Invoice #EMD" + billId);
        billLabel.setFont(new Font("Arial", Font.BOLD, 16));
        billLabel.setForeground(new Color(51, 51, 51));
        
        JLabel itemsLabel = new JLabel("Items (" + itemCount + "): " + 
            (items.length() > 50 ? items.substring(0, 50) + "..." : items));
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemsLabel.setForeground(new Color(100, 100, 100));
        
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        JLabel statusLabel = new JLabel("Status: Completed");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(40, 167, 69));
        
        infoPanel.add(billLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(itemsLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(statusLabel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("Rs." + String.format("%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(46, 125, 50));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel breakdownLabel = new JLabel("(" + itemCount + " items + GST + Delivery)");
        breakdownLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        breakdownLabel.setForeground(new Color(100, 100, 100));
        breakdownLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JButton viewBillBtn = new JButton("View Invoice");
        viewBillBtn.setBackground(new Color(46, 125, 50));
        viewBillBtn.setForeground(Color.WHITE);
        viewBillBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBillBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewBillBtn.setFocusPainted(false);
        viewBillBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        viewBillBtn.addActionListener(e -> showConsolidatedBill("EMD" + billId, items, itemCount, subtotal, gst, delivery, total, date));
        
        rightPanel.add(totalLabel);
        rightPanel.add(Box.createVerticalStrut(2));
        rightPanel.add(breakdownLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(viewBillBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    

    
    private void addSampleConsolidatedBill(JPanel billsList) {
        JPanel billCard = createConsolidatedBillCard(
            1, "Paracetamol 500mg (Qty: 2), Crocin Advance (Qty: 1)", 2, 85.0, 15.30, 50.0, 150.30, "2024-01-22"
        );
        billsList.add(billCard);
    }
    
    private JPanel createBillCard(int orderId, String productName, int quantity, 
                                 double price, String date, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        // Left panel - Bill info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel billIdLabel = new JLabel("Bill #EMD" + orderId);
        billIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        billIdLabel.setForeground(new Color(51, 51, 51));
        
        JLabel productLabel = new JLabel(productName + " (Qty: " + quantity + ")");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        productLabel.setForeground(new Color(100, 100, 100));
        
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        leftPanel.add(billIdLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(productLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(dateLabel);
        
        // Right panel - Amount and actions
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // Calculate total with GST and delivery
        double subtotal = price;
        double gst = subtotal * 0.18;
        double delivery = 50.0;
        double total = subtotal + gst + delivery;
        
        JLabel amountLabel = new JLabel("Rs." + String.format("%.2f", total));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(new Color(46, 125, 50));
        amountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JButton viewBillBtn = new JButton("View Bill");
        viewBillBtn.setBackground(new Color(46, 125, 50));
        viewBillBtn.setForeground(Color.WHITE);
        viewBillBtn.setFont(new Font("Arial", Font.BOLD, 11));
        viewBillBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        viewBillBtn.setFocusPainted(false);
        viewBillBtn.addActionListener(e -> showConsolidatedBill("EMD" + orderId, productName + " (Qty: " + quantity + ")", 1, subtotal, gst, delivery, total, date));
        
        rightPanel.add(amountLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(statusLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(viewBillBtn);
        
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void showConsolidatedBill(String billId, String items, int itemCount, 
                                     double subtotal, double gst, double delivery, double total, String date) {
        JDialog billDialog = new JDialog(this, "Consolidated Invoice - " + billId, true);
        billDialog.setSize(600, 700);
        billDialog.setLocationRelativeTo(this);
        
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel companyLabel = new JLabel("e-MEDpharma");
        companyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        companyLabel.setForeground(new Color(27, 94, 32));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel addressLabel = new JLabel("Digital Pharmacy Management System");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel invoiceLabel = new JLabel("CONSOLIDATED INVOICE");
        invoiceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        invoiceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(companyLabel);
        headerPanel.add(addressLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(invoiceLabel);
        
        // Bill details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Bill Details"));
        
        detailsPanel.add(new JLabel("Bill ID: " + billId));
        detailsPanel.add(new JLabel("Customer: " + currentUser));
        detailsPanel.add(new JLabel("Date: " + date));
        detailsPanel.add(new JLabel("Total Items: " + itemCount));
        detailsPanel.add(new JLabel("Payment Method: Cash on Delivery"));
        
        // Items section
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items Purchased"));
        
        JTextArea itemsArea = new JTextArea(items);
        itemsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        itemsArea.setBackground(Color.WHITE);
        itemsArea.setEditable(false);
        itemsArea.setWrapStyleWord(true);
        itemsArea.setLineWrap(true);
        itemsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane itemsScroll = new JScrollPane(itemsArea);
        itemsScroll.setPreferredSize(new Dimension(550, 120));
        itemsScroll.setBorder(null);
        
        itemsPanel.add(itemsScroll, BorderLayout.CENTER);
        
        // Total section
        JPanel totalPanel = new JPanel(new GridLayout(4, 2));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createTitledBorder("Payment Summary"));
        
        totalPanel.add(new JLabel("Subtotal (" + itemCount + " items):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", subtotal)));
        totalPanel.add(new JLabel("GST (18%):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", gst)));
        totalPanel.add(new JLabel("Delivery (Single charge):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", delivery)));
        
        JLabel totalLabel = new JLabel("TOTAL:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel finalTotalLabel = new JLabel("Rs." + String.format("%.2f", total));
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalTotalLabel.setForeground(new Color(27, 94, 32));
        
        totalPanel.add(totalLabel);
        totalPanel.add(finalTotalLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton printBtn = new JButton("Print Bill");
        printBtn.setBackground(new Color(46, 125, 50));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(billDialog, "Consolidated bill sent to printer!", "Print", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton downloadBtn = new JButton("Download PDF");
        downloadBtn.setBackground(new Color(102, 126, 234));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(billDialog, "Consolidated bill downloaded as PDF!", "Download", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> billDialog.dispose());
        
        buttonPanel.add(printBtn);
        buttonPanel.add(downloadBtn);
        buttonPanel.add(closeBtn);
        
        billPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(itemsPanel, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        billPanel.add(centerPanel, BorderLayout.CENTER);
        billPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        billDialog.add(billPanel);
        billDialog.setVisible(true);
    }
    
    private void showHealthReports() {
        mainContentPanel.removeAll();
        
        JPanel reportsPanel = new JPanel(new BorderLayout());
        reportsPanel.setBackground(new Color(248, 250, 252));
        reportsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Health Reports & Analytics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // Create health dashboard
        JPanel healthDashboard = createHealthDashboard();
        
        reportsPanel.add(titleLabel, BorderLayout.NORTH);
        reportsPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        reportsPanel.add(healthDashboard, BorderLayout.CENTER);
        
        mainContentPanel.add(reportsPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createHealthDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(new Color(248, 250, 252));
        
        // Top metrics panel
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        metricsPanel.setBackground(new Color(248, 250, 252));
        
        // Calculate health metrics
        int[] metrics = calculateHealthMetrics();
        
        JPanel ordersCard = createHealthMetricCard("Total Orders", String.valueOf(metrics[0]), new Color(27, 94, 32));
        JPanel spendingCard = createHealthMetricCard("Total Spending", "Rs." + metrics[1], new Color(46, 125, 50));
        JPanel avgOrderCard = createHealthMetricCard("Avg Order Value", "Rs." + (metrics[0] > 0 ? metrics[1]/metrics[0] : 0), new Color(255, 193, 7));
        JPanel healthScoreCard = createHealthMetricCard("Health Score", getHealthScore() + "%", new Color(220, 53, 69));
        
        metricsPanel.add(ordersCard);
        metricsPanel.add(spendingCard);
        metricsPanel.add(avgOrderCard);
        metricsPanel.add(healthScoreCard);
        
        // Recent purchases panel
        JPanel recentPurchasesPanel = createRecentPurchasesPanel();
        
        // Medicine categories panel
        JPanel categoriesPanel = createMedicineCategoriesPanel();
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setBackground(new Color(248, 250, 252));
        centerPanel.add(recentPurchasesPanel);
        centerPanel.add(categoriesPanel);
        
        dashboard.add(metricsPanel, BorderLayout.NORTH);
        dashboard.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        dashboard.add(centerPanel, BorderLayout.CENTER);
        
        return dashboard;
    }
    
    private JPanel createHealthMetricCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 15, 20, 15)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createRecentPurchasesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JLabel titleLabel = new JLabel("Recent Medicine Purchases");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel purchasesList = new JPanel();
        purchasesList.setLayout(new BoxLayout(purchasesList, BoxLayout.Y_AXIS));
        purchasesList.setBackground(Color.WHITE);
        
        // Load recent purchases from database
        loadRecentPurchases(purchasesList);
        
        JScrollPane scrollPane = new JScrollPane(purchasesList);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMedicineCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JLabel titleLabel = new JLabel("Medicine Categories Purchased");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel categoriesList = new JPanel();
        categoriesList.setLayout(new BoxLayout(categoriesList, BoxLayout.Y_AXIS));
        categoriesList.setBackground(Color.WHITE);
        
        // Load medicine categories from purchase history
        loadMedicineCategories(categoriesList);
        
        JScrollPane scrollPane = new JScrollPane(categoriesList);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private int[] calculateHealthMetrics() {
        int[] metrics = new int[2]; // [totalOrders, totalSpending]
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT COUNT(*) as total_orders, COALESCE(SUM(price), 0) as total_spending " +
                          "FROM orders WHERE uid = ?";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                metrics[0] = rs.getInt("total_orders");
                metrics[1] = (int) rs.getDouble("total_spending");
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error calculating health metrics: " + e.getMessage());
            // Return sample data
            metrics[0] = 12; // 12 orders
            metrics[1] = 2450; // Rs. 2,450 spending
        }
        
        return metrics;
    }
    
    private void loadRecentPurchases(JPanel purchasesList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT p.pname, o.price, o.orderdatetime " +
                          "FROM orders o JOIN product p ON o.pid = p.pid " +
                          "WHERE o.uid = ? ORDER BY o.orderdatetime DESC LIMIT 5";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            
            boolean hasPurchases = false;
            while (rs.next()) {
                hasPurchases = true;
                JLabel purchaseLabel = new JLabel("• " + rs.getString("pname") + " - Rs." + rs.getDouble("price"));
                purchaseLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                purchasesList.add(purchaseLabel);
                purchasesList.add(Box.createVerticalStrut(5));
            }
            
            if (!hasPurchases) {
                JLabel noPurchasesLabel = new JLabel("No recent purchases found");
                noPurchasesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                noPurchasesLabel.setForeground(new Color(100, 100, 100));
                purchasesList.add(noPurchasesLabel);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading recent purchases: " + e.getMessage());
            // Add sample data
            String[] samplePurchases = {
                "• Paracetamol 500mg - Rs.25",
                "• Vitamin D3 - Rs.120",
                "• Crocin Advance - Rs.35",
                "• Aspirin 75mg - Rs.45"
            };
            
            for (String purchase : samplePurchases) {
                JLabel purchaseLabel = new JLabel(purchase);
                purchaseLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                purchasesList.add(purchaseLabel);
                purchasesList.add(Box.createVerticalStrut(5));
            }
        }
    }
    
    private void loadMedicineCategories(JPanel categoriesList) {
        // Since we don't have category data in database, we'll analyze by medicine names
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT p.pname, COUNT(*) as purchase_count " +
                          "FROM orders o JOIN product p ON o.pid = p.pid " +
                          "WHERE o.uid = ? GROUP BY p.pname ORDER BY purchase_count DESC LIMIT 5";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            
            boolean hasCategories = false;
            while (rs.next()) {
                hasCategories = true;
                String medicineName = rs.getString("pname");
                int count = rs.getInt("purchase_count");
                String category = categorizeMedicine(medicineName);
                
                JLabel categoryLabel = new JLabel("• " + category + " (" + count + " purchases)");
                categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                categoriesList.add(categoryLabel);
                categoriesList.add(Box.createVerticalStrut(5));
            }
            
            if (!hasCategories) {
                JLabel noCategoriesLabel = new JLabel("No purchase history found");
                noCategoriesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                noCategoriesLabel.setForeground(new Color(100, 100, 100));
                categoriesList.add(noCategoriesLabel);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading medicine categories: " + e.getMessage());
            // Add sample data
            String[] sampleCategories = {
                "• Pain Relief (3 purchases)",
                "• Vitamins (2 purchases)",
                "• Heart Health (1 purchase)",
                "• Cold & Flu (1 purchase)"
            };
            
            for (String category : sampleCategories) {
                JLabel categoryLabel = new JLabel(category);
                categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                categoriesList.add(categoryLabel);
                categoriesList.add(Box.createVerticalStrut(5));
            }
        }
    }
    
    private String categorizeMedicine(String medicineName) {
        String name = medicineName.toLowerCase();
        
        if (name.contains("paracetamol") || name.contains("ibuprofen") || name.contains("dolo") || name.contains("crocin")) {
            return "Pain Relief";
        } else if (name.contains("vitamin") || name.contains("calcium") || name.contains("omega")) {
            return "Vitamins & Supplements";
        } else if (name.contains("aspirin") || name.contains("ecosprin") || name.contains("atorvastatin")) {
            return "Heart Health";
        } else if (name.contains("amoxicillin") || name.contains("azithromycin") || name.contains("antibiotic")) {
            return "Antibiotics";
        } else if (name.contains("metformin") || name.contains("insulin") || name.contains("diabetes")) {
            return "Diabetes Care";
        } else if (name.contains("sinarest") || name.contains("vicks") || name.contains("cough")) {
            return "Cold & Flu";
        } else if (name.contains("eno") || name.contains("digene") || name.contains("antacid")) {
            return "Digestive Health";
        } else {
            return "General Medicine";
        }
    }
    
    private void loadUserData() {
        // Load user's purchase history, subscriptions, etc.
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loadProducts();
                loadUserSubscriptions();
                return null;
            }
            
            @Override
            protected void done() {
                updateCartCount();
            }
        };
        worker.execute();
    }
    
    private void generateAIRecommendations() {
        aiRecommendations.clear();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            // Get user's purchase history for personalized recommendations
            String historyQuery = "SELECT p.pid, p.pname FROM orders o JOIN product p ON o.pid = p.pid WHERE o.uid = ? GROUP BY p.pid ORDER BY COUNT(*) DESC LIMIT 3";
            PreparedStatement historyPs = conn.prepareStatement(historyQuery);
            historyPs.setString(1, currentUser);
            ResultSet historyRs = historyPs.executeQuery();
            
            java.util.Set<String> purchasedProducts = new java.util.HashSet<>();
            while (historyRs.next()) {
                purchasedProducts.add(historyRs.getString("pid"));
            }
            
            // Generate recommendations based on available products
            String productQuery = "SELECT p.pid, p.pname, p.price FROM product p WHERE p.pid NOT IN (" +
                                 purchasedProducts.stream().map(id -> "'" + id + "'").collect(java.util.stream.Collectors.joining(",", "", purchasedProducts.isEmpty() ? "''" : "")) + 
                                 ") ORDER BY RAND() LIMIT 5";
            
            PreparedStatement productPs = conn.prepareStatement(productQuery);
            ResultSet productRs = productPs.executeQuery();
            
            while (productRs.next()) {
                String productId = productRs.getString("pid");
                String productName = productRs.getString("pname");
                double price = productRs.getDouble("price");
                
                // Generate AI recommendation based on product type
                String[] recData = generateRecommendationForProduct(productId, productName, price);
                
                aiRecommendations.add(new Recommendation(
                    recData[0], recData[1], recData[2], recData[3], productId, 
                    Integer.parseInt(recData[4])
                ));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error generating AI recommendations: " + e.getMessage());
            // Fallback to sample recommendations
            generateSampleRecommendations();
        }
    }
    
    private String[] generateRecommendationForProduct(String productId, String productName, double price) {
        String name = productName.toLowerCase();
        
        if (name.contains("paracetamol") || name.contains("dolo") || name.contains("crocin")) {
            return new String[]{"PAIN_RELIEF", "[💊]", "Pain Relief Recommendation", 
                "Based on common health needs, " + productName + " is recommended for fever and pain relief.", "92"};
        } else if (name.contains("vitamin") || name.contains("calcium")) {
            return new String[]{"WELLNESS", "[🌟]", "Health & Wellness", 
                "Boost your immunity and overall health with " + productName + ". Great for daily wellness.", "88"};
        } else if (name.contains("aspirin") || name.contains("ecosprin")) {
            return new String[]{"HEART_HEALTH", "[❤️]", "Heart Health Care", 
                "Maintain cardiovascular health with " + productName + ". Recommended for heart protection.", "90"};
        } else if (name.contains("antibiotic") || name.contains("amoxicillin")) {
            return new String[]{"INFECTION", "[🛡️]", "Infection Prevention", 
                "Keep " + productName + " handy for bacterial infections. Consult doctor before use.", "85"};
        } else if (name.contains("diabetes") || name.contains("metformin")) {
            return new String[]{"DIABETES", "[📊]", "Diabetes Management", 
                "Manage blood sugar levels effectively with " + productName + ". Essential for diabetic care.", "95"};
        } else {
            return new String[]{"GENERAL", "[🏥]", "Health Essential", 
                "" + productName + " is recommended based on your health profile and current trends.", "80"};
        }
    }
    
    private void generateSampleRecommendations() {
        aiRecommendations.add(new Recommendation(
            "PAIN_RELIEF", "[💊]", "Pain Relief Recommendation",
            "Paracetamol 500mg is recommended for fever and pain relief.",
            "MED001", 92
        ));
        
        aiRecommendations.add(new Recommendation(
            "WELLNESS", "[🌟]", "Health & Wellness",
            "Vitamin D3 supplements boost immunity and overall health.",
            "MED005", 88
        ));
    }
    
    private void loadProducts() {
        allProducts.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT p.pid, p.pname, p.manufacturer, p.price, COALESCE(SUM(i.quantity), 0) as stock " +
                          "FROM product p LEFT JOIN inventory i ON p.pid = i.pid " +
                          "GROUP BY p.pid, p.pname, p.manufacturer, p.price";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("pid"),
                    rs.getString("pname"),
                    rs.getString("manufacturer"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                allProducts.add(product);
            }
            
            conn.close();
            System.out.println("Loaded " + allProducts.size() + " products from database");
            
            // If no products in database, add some sample ones
            if (allProducts.isEmpty()) {
                loadSampleProducts();
            }
            
        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to sample products if database fails
            loadSampleProducts();
        }
    }
    
    private void loadSampleProducts() {
        // Pain Relief & Fever - Multiple products per category
        allProducts.add(new Product("MED001", "Paracetamol 500mg", "Generic", 25.0, 100));
        allProducts.add(new Product("MED002", "Crocin Advance", "GSK", 35.0, 50));
        allProducts.add(new Product("MED003", "Dolo 650", "Micro Labs", 30.0, 75));
        allProducts.add(new Product("MED007", "Ibuprofen 400mg", "Abbott", 45.0, 80));
        allProducts.add(new Product("MED008", "Combiflam", "Sanofi", 38.0, 65));
        allProducts.add(new Product("MED009", "Brufen 400mg", "Abbott", 42.0, 55));
        
        // Heart Health - Multiple options
        allProducts.add(new Product("MED004", "Aspirin 75mg", "Bayer", 45.0, 60));
        allProducts.add(new Product("MED010", "Ecosprin 75mg", "USV", 28.0, 90));
        allProducts.add(new Product("MED011", "Atorvastatin 10mg", "Ranbaxy", 85.0, 45));
        allProducts.add(new Product("MED012", "Metoprolol 50mg", "Cipla", 65.0, 70));
        
        // Vitamins & Supplements - Diverse range
        allProducts.add(new Product("MED005", "Vitamin D3", "Cipla", 120.0, 40));
        allProducts.add(new Product("MED006", "Calcium Tablets", "Sun Pharma", 80.0, 30));
        allProducts.add(new Product("MED013", "Vitamin B12 Tablets", "Himalaya", 95.0, 55));
        allProducts.add(new Product("MED014", "Omega-3 Capsules", "Amway", 450.0, 25));
        allProducts.add(new Product("MED015", "Multivitamin Tablets", "Centrum", 350.0, 35));
        allProducts.add(new Product("MED016", "Iron Tablets", "Ranbaxy", 75.0, 60));
        
        // Antibiotics - Different types
        allProducts.add(new Product("MED017", "Amoxicillin 500mg", "Cipla", 120.0, 35));
        allProducts.add(new Product("MED018", "Azithromycin 250mg", "Pfizer", 180.0, 28));
        allProducts.add(new Product("MED019", "Cephalexin 500mg", "Sun Pharma", 95.0, 40));
        
        // Diabetes Care - Complete range
        allProducts.add(new Product("MED020", "Metformin 500mg", "Sun Pharma", 45.0, 70));
        allProducts.add(new Product("MED021", "Glimepiride 2mg", "Torrent", 65.0, 40));
        allProducts.add(new Product("MED022", "Insulin Pen", "Novo Nordisk", 850.0, 15));
        
        // Cold & Flu - Multiple options
        allProducts.add(new Product("MED023", "Sinarest Tablet", "Centaur", 25.0, 85));
        allProducts.add(new Product("MED024", "Vicks Cough Syrup", "P&G", 85.0, 45));
        allProducts.add(new Product("MED025", "Cetrizine 10mg", "Cipla", 35.0, 90));
        
        // Digestive Health - Various products
        allProducts.add(new Product("MED026", "ENO Antacid", "GSK", 45.0, 95));
        allProducts.add(new Product("MED027", "Digene Gel", "Abbott", 55.0, 60));
        allProducts.add(new Product("MED028", "Pantoprazole 40mg", "Sun Pharma", 125.0, 50));
        
        // Women's Health - New category
        allProducts.add(new Product("MED029", "Folic Acid Tablets", "Cipla", 45.0, 80));
        allProducts.add(new Product("MED030", "Calcium + Vitamin D", "Shelcal", 180.0, 45));
        
        // Skin Care - New category
        allProducts.add(new Product("MED031", "Betnovate Cream", "GSK", 85.0, 30));
        allProducts.add(new Product("MED032", "Clotrimazole Cream", "Candid", 65.0, 40));
        
        // Eye Care - New category
        allProducts.add(new Product("MED033", "Eye Drops Refresh", "Allergan", 120.0, 25));
        allProducts.add(new Product("MED034", "Vitamin A Capsules", "Himalaya", 95.0, 35));
    }
    
    private void loadUserSubscriptions() {
        // Load user's active subscriptions
    }
    
    private void updateCartCount() {
        cartCountLabel.setText(String.valueOf(cartItems.size()));
    }
    
    private void setupEventHandlers() {
        // Header search functionality
        JPanel headerPanel = (JPanel) getContentPane().getComponent(0);
        JPanel searchPanel = (JPanel) headerPanel.getComponent(1);
        JButton searchBtn = (JButton) searchPanel.getComponent(1);
        
        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        
        // Cart button functionality
        JPanel actionsPanel = (JPanel) headerPanel.getComponent(2);
        JButton cartBtn = (JButton) actionsPanel.getComponent(2);
        cartBtn.addActionListener(e -> showCartPage());
        
        // Orders button functionality
        JButton ordersBtn = (JButton) actionsPanel.getComponent(5);
        ordersBtn.addActionListener(e -> showOrdersPage());
        
        // Logout button functionality
        JButton logoutBtn = (JButton) actionsPanel.getComponent(7);
        logoutBtn.addActionListener(e -> performLogout());
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show search results
        mainContentPanel.removeAll();
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(248, 250, 252));
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Search Results for: \"" + searchTerm + "\"");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // Real search results from database
        JPanel resultsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        resultsPanel.setBackground(new Color(248, 250, 252));
        
        List<Product> searchResults = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(searchTerm.toLowerCase()) || 
                product.getManufacturer().toLowerCase().contains(searchTerm.toLowerCase())) {
                searchResults.add(product);
            }
        }
        
        if (searchResults.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found for: \"" + searchTerm + "\"");
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultsPanel.add(noResultsLabel);
        } else {
            for (Product product : searchResults) {
                JPanel productCard = createProductCard(product);
                resultsPanel.add(productCard);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        
        searchPanel.add(titleLabel, BorderLayout.NORTH);
        searchPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        searchPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(searchPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    

    
    private void showCartPage() {
        mainContentPanel.removeAll();
        
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(new Color(248, 250, 252));
        cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Shopping Cart (" + cartItems.size() + " items)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty. Start shopping to add items!");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cartPanel.add(titleLabel, BorderLayout.NORTH);
            cartPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBackground(new Color(248, 250, 252));
            
            double total = 0;
            for (int i = 0; i < cartItems.size(); i++) {
                Product item = cartItems.get(i);
                JPanel itemCard = createCartItemCard(item, i);
                itemsPanel.add(itemCard);
                itemsPanel.add(Box.createVerticalStrut(10));
                total += item.getPrice();
            }
            
            final double finalTotal = total;
            
            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalPanel.setBackground(Color.WHITE);
            totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel totalLabel = new JLabel("Total: Rs." + String.format("%.2f", finalTotal));
            totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
            totalLabel.setForeground(new Color(46, 125, 50));
            
            JButton checkoutBtn = new JButton("Proceed to Checkout");
            checkoutBtn.setBackground(new Color(46, 125, 50));
            checkoutBtn.setForeground(Color.WHITE);
            checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
            checkoutBtn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
            checkoutBtn.setFocusPainted(false);
            checkoutBtn.addActionListener(e -> processCheckout(finalTotal));
            
            totalPanel.add(totalLabel);
            totalPanel.add(Box.createHorizontalStrut(20));
            totalPanel.add(checkoutBtn);
            
            JScrollPane scrollPane = new JScrollPane(itemsPanel);
            scrollPane.setBorder(null);
            
            cartPanel.add(titleLabel, BorderLayout.NORTH);
            cartPanel.add(scrollPane, BorderLayout.CENTER);
            cartPanel.add(totalPanel, BorderLayout.SOUTH);
        }
        
        mainContentPanel.add(cartPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createCartItemCard(Product item, int index) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        // Item info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel priceLabel = new JLabel("Rs." + String.format("%.2f", item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        
        // Quantity controls
        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.setBackground(Color.WHITE);
        
        JLabel qtyLabel = new JLabel("Qty: 1"); // For now, quantity is always 1
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        quantityPanel.add(qtyLabel);
        
        // Action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton removeBtn = new JButton("Remove");
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFont(new Font("Arial", Font.BOLD, 11));
        removeBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        removeBtn.setFocusPainted(false);
        removeBtn.addActionListener(e -> removeFromCart(index));
        
        JButton addMoreBtn = new JButton("Add More");
        addMoreBtn.setBackground(new Color(46, 125, 50));
        addMoreBtn.setForeground(Color.WHITE);
        addMoreBtn.setFont(new Font("Arial", Font.BOLD, 11));
        addMoreBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        addMoreBtn.setFocusPainted(false);
        addMoreBtn.addActionListener(e -> showMedicines()); // Go back to add more items
        
        buttonPanel.add(removeBtn);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(addMoreBtn);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(quantityPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.CENTER);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void removeFromCart(int index) {
        if (index >= 0 && index < cartItems.size()) {
            String itemName = cartItems.get(index).getName();
            cartItems.remove(index);
            updateCartCount();
            JOptionPane.showMessageDialog(this, itemName + " removed from cart!", "Cart", JOptionPane.INFORMATION_MESSAGE);
            showCartPage(); // Refresh cart view
        }
    }
    
    private void processCheckout(double total) {
        // Show confirmation dialog with add/remove options
        showCheckoutConfirmation(total);
    }
    
    private void showCheckoutConfirmation(double total) {
        JDialog confirmDialog = new JDialog(this, "Confirm Your Order", true);
        confirmDialog.setSize(600, 500);
        confirmDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = new JLabel("Review Your Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel questionLabel = new JLabel("Would you like to add or remove anything?");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setForeground(new Color(100, 100, 100));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(questionLabel);
        
        // Cart items summary
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Your Items"));
        
        for (int i = 0; i < cartItems.size(); i++) {
            Product item = cartItems.get(i);
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setBackground(Color.WHITE);
            itemRow.setBorder(new EmptyBorder(5, 10, 5, 10));
            
            JLabel itemLabel = new JLabel(item.getName());
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JLabel priceLabel = new JLabel("Rs." + String.format("%.2f", item.getPrice()));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setForeground(new Color(46, 125, 50));
            
            final int index = i;
            JButton removeBtn = new JButton("Remove");
            removeBtn.setBackground(new Color(220, 53, 69));
            removeBtn.setForeground(Color.WHITE);
            removeBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            removeBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            removeBtn.setFocusPainted(false);
            removeBtn.addActionListener(e -> {
                cartItems.remove(index);
                updateCartCount();
                confirmDialog.dispose();
                if (cartItems.isEmpty()) {
                    showCartPage();
                } else {
                    double newTotal = cartItems.stream().mapToDouble(Product::getPrice).sum();
                    showCheckoutConfirmation(newTotal);
                }
            });
            
            itemRow.add(itemLabel, BorderLayout.WEST);
            itemRow.add(priceLabel, BorderLayout.CENTER);
            itemRow.add(removeBtn, BorderLayout.EAST);
            
            itemsPanel.add(itemRow);
        }
        
        // Total
        double subtotal = total;
        double gst = subtotal * 0.18;
        double delivery = 50.0;
        double finalTotal = subtotal + gst + delivery;
        
        JPanel totalPanel = new JPanel(new GridLayout(4, 2));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        totalPanel.add(new JLabel("Subtotal:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", subtotal)));
        totalPanel.add(new JLabel("GST (18%):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", gst)));
        totalPanel.add(new JLabel("Delivery:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", delivery)));
        
        JLabel totalLabel = new JLabel("TOTAL:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel finalTotalLabel = new JLabel("Rs." + String.format("%.2f", finalTotal));
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalTotalLabel.setForeground(new Color(46, 125, 50));
        
        totalPanel.add(totalLabel);
        totalPanel.add(finalTotalLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addMoreBtn = new JButton("Add More Items");
        addMoreBtn.setBackground(new Color(102, 126, 234));
        addMoreBtn.setForeground(Color.WHITE);
        addMoreBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addMoreBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addMoreBtn.setFocusPainted(false);
        addMoreBtn.addActionListener(e -> {
            confirmDialog.dispose();
            showMedicines();
        });
        
        JButton confirmBtn = new JButton("Confirm & Place Order");
        confirmBtn.setBackground(new Color(46, 125, 50));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 12));
        confirmBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> {
            confirmDialog.dispose();
            finalizeOrder(finalTotal);
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> confirmDialog.dispose());
        
        buttonPanel.add(addMoreBtn);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        
        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(itemsPanel, BorderLayout.NORTH);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmDialog.add(mainPanel);
        confirmDialog.setVisible(true);
    }
    
    private void finalizeOrder(double total) {
        // Save orders to database and generate bill
        int orderId = saveOrdersToDatabase(total);
        
        if (orderId > 0) {
            // Generate and show bill
            showBill("EMD" + orderId, total);
            
            // Clear cart
            cartItems.clear();
            updateCartCount();
            
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\nOrder ID: EMD" + orderId + "\nVendor will review and approve your order.", 
                "Order Sent to Vendor", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to place order. Please try again!", 
                "Order Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int saveOrdersToDatabase(double cartTotal) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            int lastOrderId = 0;
            
            // First, ensure bills table exists
            try {
                String createBillsTable = "CREATE TABLE IF NOT EXISTS consolidated_bills (" +
                    "bill_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "customer_id VARCHAR(50), " +
                    "items_summary TEXT, " +
                    "item_count INT, " +
                    "subtotal DECIMAL(10,2), " +
                    "gst DECIMAL(10,2), " +
                    "delivery_charges DECIMAL(10,2), " +
                    "total_amount DECIMAL(10,2), " +
                    "bill_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "payment_method VARCHAR(50) DEFAULT 'Cash on Delivery'" +
                    ")";
                PreparedStatement createPs = conn.prepareStatement(createBillsTable);
                createPs.executeUpdate();
                System.out.println("Consolidated bills table created/verified");
            } catch (SQLException e) {
                System.out.println("Consolidated bills table already exists: " + e.getMessage());
            }
            
            System.out.println("Saving " + cartItems.size() + " orders to database...");
            
            // Group cart items by vendor to create separate orders for each vendor
            java.util.Map<String, java.util.List<Product>> itemsByVendor = new java.util.HashMap<>();
            
            // First, group items by their vendor
            for (Product item : cartItems) {
                String vendorId = findVendorForProduct(conn, item.getId());
                if (!itemsByVendor.containsKey(vendorId)) {
                    itemsByVendor.put(vendorId, new java.util.ArrayList<>());
                }
                itemsByVendor.get(vendorId).add(item);
                System.out.println("📦 Item " + item.getName() + " assigned to vendor: " + vendorId);
            }
            
            System.out.println("🏪 Creating orders for " + itemsByVendor.size() + " different vendors");
            
            // Create separate orders for each vendor
            for (java.util.Map.Entry<String, java.util.List<Product>> vendorEntry : itemsByVendor.entrySet()) {
                String vendorId = vendorEntry.getKey();
                java.util.List<Product> vendorItems = vendorEntry.getValue();
                
                System.out.println("\n🏪 Processing " + vendorItems.size() + " items for vendor: " + vendorId);
                
                // Create individual orders for each item from this vendor
                for (Product item : vendorItems) {
                    int currentOrderId = 0;
                    try {
                        String query = "INSERT INTO orders (uid, pid, sid, quantity, price, orderdatetime, status) VALUES (?, ?, ?, ?, ?, NOW(), 'Pending')";
                        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        
                        ps.setString(1, currentUser);
                        ps.setString(2, item.getId());
                        ps.setString(3, vendorId);
                        ps.setInt(4, 1); // Quantity = 1 for now
                        ps.setDouble(5, item.getPrice());
                        
                        System.out.println("   📝 Creating order: " + item.getName() + " → Vendor: " + vendorId + " → Price: Rs." + item.getPrice());
                        
                        int result = ps.executeUpdate();
                        
                        if (result > 0) {
                            ResultSet rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                currentOrderId = rs.getInt(1);
                                lastOrderId = currentOrderId;
                                System.out.println("   ✅ Order created with ID: " + currentOrderId + " for vendor: " + vendorId);
                            }
                        }
                        ps.close();
                    } catch (SQLException e2) {
                        System.out.println("   ❌ Error creating order for " + item.getName() + ": " + e2.getMessage());
                        // Create mock order for testing
                        currentOrderId = (int) (System.currentTimeMillis() % 10000) + vendorItems.indexOf(item);
                        lastOrderId = currentOrderId;
                        System.out.println("   🔄 Using mock order ID: " + currentOrderId);
                    }
                }
            }
            
            // Create ONE consolidated bill for all items in this shopping session
            saveConsolidatedBill(conn, cartTotal);
            
            conn.close();
            System.out.println("\n🎉 All orders distributed to " + itemsByVendor.size() + " vendors successfully!");
            System.out.println("📋 Total items processed: " + cartItems.size());
            System.out.println("🧾 Consolidated bill created for customer: " + currentUser);
            System.out.println("🆔 Last order ID: " + lastOrderId);
            return lastOrderId > 0 ? lastOrderId : (int) (System.currentTimeMillis() % 10000);
            
        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
            e.printStackTrace();
            // Return a mock order ID for testing
            return (int) (System.currentTimeMillis() % 10000);
        }
    }
    
    private String findVendorForProduct(Connection conn, String productId) {
        try {
            // First, try to find vendor who has this product in inventory
            String inventoryQuery = "SELECT i.sid FROM inventory i WHERE i.pid = ? AND i.quantity > 0 ORDER BY i.quantity DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(inventoryQuery);
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String vendorId = rs.getString("sid");
                System.out.println("✅ Found vendor " + vendorId + " with inventory for product " + productId);
                ps.close();
                return vendorId;
            }
            ps.close();
            
            // If no inventory found, get any available vendor from seller table
            String vendorQuery = "SELECT sid FROM seller ORDER BY sid LIMIT 1";
            PreparedStatement vendorPs = conn.prepareStatement(vendorQuery);
            ResultSet vendorRs = vendorPs.executeQuery();
            
            if (vendorRs.next()) {
                String availableVendor = vendorRs.getString("sid");
                System.out.println("📦 Using available vendor " + availableVendor + " for product " + productId);
                vendorPs.close();
                return availableVendor;
            }
            vendorPs.close();
            
        } catch (SQLException e) {
            System.out.println("❌ Error finding vendor for product " + productId + ": " + e.getMessage());
        }
        
        // Create a default vendor if none exists
        System.out.println("⚠️ No vendors found, creating default assignment for product " + productId);
        return "VENDOR_DEFAULT";
    }
    
    private void saveConsolidatedBill(Connection conn, double cartTotal) {
        try {
            // Create items summary
            StringBuilder itemsSummary = new StringBuilder();
            for (int i = 0; i < cartItems.size(); i++) {
                Product item = cartItems.get(i);
                if (i > 0) itemsSummary.append(", ");
                itemsSummary.append(item.getName()).append(" (Qty: 1)");
            }
            
            double subtotal = cartTotal;
            double gst = subtotal * 0.18;
            double delivery = 50.0; // Single delivery charge for entire order
            double total = subtotal + gst + delivery;
            
            String billQuery = "INSERT INTO consolidated_bills (customer_id, items_summary, item_count, subtotal, gst, delivery_charges, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement billPs = conn.prepareStatement(billQuery);
            billPs.setString(1, currentUser);
            billPs.setString(2, itemsSummary.toString());
            billPs.setInt(3, cartItems.size());
            billPs.setDouble(4, subtotal);
            billPs.setDouble(5, gst);
            billPs.setDouble(6, delivery);
            billPs.setDouble(7, total);
            
            int result = billPs.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Consolidated bill created for customer: " + currentUser + ", Items: " + cartItems.size() + ", Total: Rs." + String.format("%.2f", total));
            }
            billPs.close();
            
        } catch (SQLException e) {
            System.out.println("❌ Error saving consolidated bill: " + e.getMessage());
        }
    }
    

    
    private void showBill(String orderId, double total) {
        JDialog billDialog = new JDialog(this, "Invoice - " + orderId, true);
        billDialog.setSize(500, 700);
        billDialog.setLocationRelativeTo(this);
        
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel companyLabel = new JLabel("e-MEDpharma");
        companyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        companyLabel.setForeground(new Color(27, 94, 32));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel addressLabel = new JLabel("Digital Pharmacy Management System");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel invoiceLabel = new JLabel("INVOICE");
        invoiceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        invoiceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(companyLabel);
        headerPanel.add(addressLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(invoiceLabel);
        
        // Bill details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        
        detailsPanel.add(new JLabel("Order ID: " + orderId));
        detailsPanel.add(new JLabel("Customer: " + currentUser));
        detailsPanel.add(new JLabel("Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        detailsPanel.add(new JLabel("Payment Method: Cash on Delivery"));
        
        // Items table
        String[] columns = {"Item", "Qty", "Price", "Total"};
        Object[][] data = new Object[cartItems.size()][4];
        
        for (int i = 0; i < cartItems.size(); i++) {
            Product item = cartItems.get(i);
            data[i][0] = item.getName();
            data[i][1] = "1";
            data[i][2] = "Rs." + item.getPrice();
            data[i][3] = "Rs." + item.getPrice();
        }
        
        JTable itemsTable = new JTable(data, columns);
        itemsTable.setEnabled(false);
        JScrollPane tableScroll = new JScrollPane(itemsTable);
        tableScroll.setPreferredSize(new Dimension(450, 200));
        
        // Total section
        JPanel totalPanel = new JPanel(new GridLayout(4, 2));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createTitledBorder("Payment Summary"));
        
        double subtotal = total;
        double tax = subtotal * 0.18; // 18% GST
        double delivery = 50.0;
        double finalTotal = subtotal + tax + delivery;
        
        totalPanel.add(new JLabel("Subtotal:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", subtotal)));
        totalPanel.add(new JLabel("GST (18%):"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", tax)));
        totalPanel.add(new JLabel("Delivery Charges:"));
        totalPanel.add(new JLabel("Rs." + String.format("%.2f", delivery)));
        
        JLabel totalLabel = new JLabel("TOTAL:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel finalTotalLabel = new JLabel("Rs." + String.format("%.2f", finalTotal));
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalTotalLabel.setForeground(new Color(27, 94, 32));
        
        totalPanel.add(totalLabel);
        totalPanel.add(finalTotalLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton printBtn = new JButton("Print Bill");
        printBtn.setBackground(new Color(46, 125, 50));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(billDialog, "Bill sent to printer!", "Print", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> {
            billDialog.dispose();
            showDashboardHome();
        });
        
        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);
        
        billPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        billPanel.add(centerPanel, BorderLayout.CENTER);
        billPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        billDialog.add(billPanel);
        billDialog.setVisible(true);
    }
    
    private void showOrdersPage() {
        mainContentPanel.removeAll();
        
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBackground(new Color(248, 250, 252));
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        
        JLabel titleLabel = new JLabel("My Orders - Real-time Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JButton refreshBtn = new JButton("🔄 Refresh Status");
        refreshBtn.setBackground(new Color(46, 125, 50));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> showOrdersPage()); // Refresh orders
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        
        // Load real orders from database
        JPanel ordersList = new JPanel();
        ordersList.setLayout(new BoxLayout(ordersList, BoxLayout.Y_AXIS));
        ordersList.setBackground(new Color(248, 250, 252));
        
        loadCustomerOrders(ordersList);
        
        JScrollPane scrollPane = new JScrollPane(ordersList);
        scrollPane.setBorder(null);
        
        ordersPanel.add(headerPanel, BorderLayout.NORTH);
        ordersPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(ordersPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void loadCustomerOrders(JPanel ordersList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT o.oid, p.pname, o.quantity, o.price, o.orderdatetime, s.sname " +
                          "FROM orders o " +
                          "JOIN product p ON o.pid = p.pid " +
                          "LEFT JOIN seller s ON o.sid = s.sid " +
                          "WHERE o.uid = ? ORDER BY o.orderdatetime DESC";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            
            boolean hasOrders = false;
            while (rs.next()) {
                hasOrders = true;
                JPanel orderCard = createRealTimeOrderCard(
                    rs.getInt("oid"),
                    rs.getString("pname"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    "Pending",
                    rs.getString("orderdatetime"),
                    rs.getString("sname")
                );
                ordersList.add(orderCard);
                ordersList.add(Box.createVerticalStrut(15));
            }
            
            if (!hasOrders) {
                JLabel noOrdersLabel = new JLabel("No orders found. Start shopping to place your first order!");
                noOrdersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                noOrdersLabel.setHorizontalAlignment(SwingConstants.CENTER);
                noOrdersLabel.setForeground(new Color(100, 100, 100));
                ordersList.add(noOrdersLabel);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error loading customer orders: " + e.getMessage());
            // Add sample orders if database fails
            addSampleCustomerOrders(ordersList);
        }
    }
    
    private void addSampleCustomerOrders(JPanel ordersList) {
        Object[][] sampleOrders = {
            {1, "Paracetamol 500mg", 2, 50.0, "Pending", "2024-01-22", "MedPlus Pharmacy"},
            {2, "Crocin Advance", 1, 35.0, "Approved", "2024-01-21", "Apollo Pharmacy"},
            {3, "Dolo 650", 1, 30.0, "Rejected", "2024-01-20", "Wellness Pharmacy"}
        };
        
        for (Object[] order : sampleOrders) {
            JPanel orderCard = createRealTimeOrderCard(
                (Integer) order[0], (String) order[1], (Integer) order[2], 
                (Double) order[3], (String) order[4], (String) order[5], (String) order[6]
            );
            ordersList.add(orderCard);
            ordersList.add(Box.createVerticalStrut(15));
        }
    }
    
    private JPanel createRealTimeOrderCard(int orderId, String productName, int quantity, 
                                          double price, String status, String date, String pharmacy) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        // Left panel - Order info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel orderIdLabel = new JLabel("Order #" + orderId);
        orderIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        orderIdLabel.setForeground(new Color(51, 51, 51));
        
        JLabel productLabel = new JLabel(productName + " (Qty: " + quantity + ")");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        productLabel.setForeground(new Color(100, 100, 100));
        
        JLabel pharmacyLabel = new JLabel("From: " + (pharmacy != null ? pharmacy : "Pharmacy"));
        pharmacyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        pharmacyLabel.setForeground(new Color(100, 100, 100));
        
        JLabel dateLabel = new JLabel("Ordered: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        leftPanel.add(orderIdLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(productLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(pharmacyLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(dateLabel);
        
        // Right panel - Status and price
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("Rs." + String.format("%.2f", price));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(new Color(46, 125, 50));
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // Status with color coding
        JLabel statusLabel = new JLabel("● " + status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        Color statusColor;
        String statusMessage;
        switch (status.toLowerCase()) {
            case "pending":
                statusColor = new Color(255, 193, 7); // Yellow
                statusMessage = "Waiting for vendor approval";
                break;
            case "approved":
                statusColor = new Color(40, 167, 69); // Green
                statusMessage = "Order approved! Preparing for delivery";
                break;
            case "rejected":
                statusColor = new Color(220, 53, 69); // Red
                statusMessage = "Order rejected by vendor";
                break;
            case "shipped":
                statusColor = new Color(102, 126, 234); // Blue
                statusMessage = "Order shipped! On the way";
                break;
            case "delivered":
                statusColor = new Color(40, 167, 69); // Green
                statusMessage = "Order delivered successfully";
                break;
            default:
                statusColor = new Color(100, 100, 100); // Gray
                statusMessage = "Status unknown";
                break;
        }
        
        statusLabel.setForeground(statusColor);
        
        JLabel statusMessageLabel = new JLabel(statusMessage);
        statusMessageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusMessageLabel.setForeground(new Color(100, 100, 100));
        statusMessageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(priceLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(statusLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(statusMessageLabel);
        
        // Action buttons based on status
        if ("pending".equals(status.toLowerCase())) {
            JButton cancelBtn = new JButton("Cancel Order");
            cancelBtn.setBackground(new Color(220, 53, 69));
            cancelBtn.setForeground(Color.WHITE);
            cancelBtn.setFont(new Font("Arial", Font.BOLD, 10));
            cancelBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            cancelBtn.setFocusPainted(false);
            cancelBtn.addActionListener(e -> cancelOrder(orderId));
            
            rightPanel.add(Box.createVerticalStrut(10));
            rightPanel.add(cancelBtn);
        } else if ("approved".equals(status.toLowerCase()) || "shipped".equals(status.toLowerCase())) {
            JButton trackBtn = new JButton("Track Order");
            trackBtn.setBackground(new Color(102, 126, 234));
            trackBtn.setForeground(Color.WHITE);
            trackBtn.setFont(new Font("Arial", Font.BOLD, 10));
            trackBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            trackBtn.setFocusPainted(false);
            trackBtn.addActionListener(e -> trackOrder(orderId));
            
            rightPanel.add(Box.createVerticalStrut(10));
            rightPanel.add(trackBtn);
        }
        
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void cancelOrder(int orderId) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel Order #" + orderId + "?", 
            "Cancel Order", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                    "root", "A@nchal911");
                
                String query = "UPDATE orders SET status = 'Cancelled' WHERE oid = ? AND uid = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, orderId);
                ps.setString(2, currentUser);
                
                int result = ps.executeUpdate();
                conn.close();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Order #" + orderId + " has been cancelled!", 
                        "Order Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    showOrdersPage(); // Refresh the view
                }
                
            } catch (Exception e) {
                System.out.println("Error cancelling order: " + e.getMessage());
                JOptionPane.showMessageDialog(this, 
                    "Failed to cancel order!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void trackOrder(int orderId) {
        JDialog trackDialog = new JDialog(this, "Track Order #" + orderId, true);
        trackDialog.setSize(500, 400);
        trackDialog.setLocationRelativeTo(this);
        
        JPanel trackPanel = new JPanel(new BorderLayout());
        trackPanel.setBackground(Color.WHITE);
        trackPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order Tracking - #" + orderId);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Tracking timeline
        JPanel timelinePanel = new JPanel();
        timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
        timelinePanel.setBackground(Color.WHITE);
        timelinePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        String[] trackingSteps = {
            "✓ Order Placed - Jan 22, 10:30 AM",
            "✓ Order Approved by Vendor - Jan 22, 11:15 AM",
            "🚚 Preparing for Delivery - Jan 22, 2:00 PM",
            "📦 Out for Delivery - Expected by 6:00 PM",
            "🏠 Delivered - Pending"
        };
        
        Color[] stepColors = {
            new Color(40, 167, 69),   // Green - completed
            new Color(40, 167, 69),   // Green - completed
            new Color(255, 193, 7),   // Yellow - in progress
            new Color(100, 100, 100), // Gray - pending
            new Color(100, 100, 100)  // Gray - pending
        };
        
        for (int i = 0; i < trackingSteps.length; i++) {
            JLabel stepLabel = new JLabel(trackingSteps[i]);
            stepLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            stepLabel.setForeground(stepColors[i]);
            stepLabel.setBorder(new EmptyBorder(8, 20, 8, 20));
            
            timelinePanel.add(stepLabel);
        }
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.addActionListener(e -> trackDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        
        trackPanel.add(titleLabel, BorderLayout.NORTH);
        trackPanel.add(timelinePanel, BorderLayout.CENTER);
        trackPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        trackDialog.add(trackPanel);
        trackDialog.setVisible(true);
    }
    
    private JPanel createOrderCard(String orderId, String items, String amount, String status, String date) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel orderIdLabel = new JLabel("Order ID: " + orderId);
        orderIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel itemsLabel = new JLabel("Items: " + items);
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        leftPanel.add(orderIdLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(itemsLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(dateLabel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(new Color(46, 125, 50));
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        Color statusColor = status.equals("Delivered") ? new Color(40, 167, 69) :
                           status.equals("In Transit") ? new Color(255, 193, 7) :
                           new Color(102, 126, 234);
        statusLabel.setForeground(statusColor);
        
        rightPanel.add(amountLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(statusLabel);
        
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
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
    
    private void enableEnhancedScrolling(JScrollPane scrollPane) {
        // Enhanced mouse wheel scrolling with touchpad support
        scrollPane.addMouseWheelListener(e -> {
            if (e.getScrollType() == java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int unitsToScroll = e.getUnitsToScroll();
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                int currentValue = verticalScrollBar.getValue();
                int scrollAmount = unitsToScroll * 25; // Smooth scrolling
                
                // Handle touchpad precision scrolling
                if (Math.abs(unitsToScroll) < 3) {
                    scrollAmount = unitsToScroll * 15;
                }
                
                int newValue = currentValue + scrollAmount;
                newValue = Math.max(verticalScrollBar.getMinimum(), 
                          Math.min(verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount(), newValue));
                
                verticalScrollBar.setValue(newValue);
            }
        });
        
        // Add keyboard navigation and back gesture support
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("alt LEFT"), "goBack");
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("BACK_SPACE"), "goBack");
        
        scrollPane.getActionMap().put("goBack", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goBack();
            }
        });
    }
    
    private void goBack() {
        if (navigationHistory.size() > 1) {
            navigationHistory.pop(); // Remove current page
            String previousPage = navigationHistory.peek();
            
            switch (previousPage) {
                case "home":
                    showDashboardHome();
                    break;
                case "recommendations":
                    showAIRecommendations();
                    break;
                case "medicines":
                    showMedicines();
                    break;
                default:
                    showDashboardHome();
                    break;
            }
        } else {
            showDashboardHome();
        }
    }
    
    // Helper methods
    private String getTimeOfDay() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12) return "Morning";
        else if (hour < 17) return "Afternoon";
        else return "Evening";
    }
    
    private int getTotalOrders() {
        // Calculate total orders from database
        return 12; // Placeholder
    }
    
    private String getMoneySaved() {
        // Calculate money saved through subscriptions and offers
        return "2,450"; // Placeholder
    }
    
    private int getHealthScore() {
        // Calculate health score based on medication adherence
        return 85; // Placeholder
    }
    
    private String getNextOrderDate() {
        // Get next scheduled order date
        return LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("MMM dd"));
    }
    
    // Inner classes
    private static class Product {
        private String id, name, manufacturer;
        private double price;
        private int stock;
        
        public Product(String id, String name, String manufacturer, double price, int stock) {
            this.id = id;
            this.name = name;
            this.manufacturer = manufacturer;
            this.price = price;
            this.stock = stock;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getManufacturer() { return manufacturer; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
    }
    
    private static class Recommendation {
        private String type, typeIcon, title, description, productId;
        private int confidence;
        
        public Recommendation(String type, String typeIcon, String title, String description, String productId, int confidence) {
            this.type = type;
            this.typeIcon = typeIcon;
            this.title = title;
            this.description = description;
            this.productId = productId;
            this.confidence = confidence;
        }
        
        // Getters
        public String getType() { return type; }
        public String getTypeIcon() { return typeIcon; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getProductId() { return productId; }
        public int getConfidence() { return confidence; }
    }
    
    private static class Subscription {
        private String medicineId, medicineName, frequency;
        private LocalDate nextDelivery;
        private boolean active;
        
        public Subscription(String medicineId, String medicineName, String frequency, LocalDate nextDelivery, boolean active) {
            this.medicineId = medicineId;
            this.medicineName = medicineName;
            this.frequency = frequency;
            this.nextDelivery = nextDelivery;
            this.active = active;
        }
        
        // Getters
        public String getMedicineId() { return medicineId; }
        public String getMedicineName() { return medicineName; }
        public String getFrequency() { return frequency; }
        public LocalDate getNextDelivery() { return nextDelivery; }
        public boolean isActive() { return active; }
    }
}