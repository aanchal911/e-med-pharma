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
            {"[S]", "My Subscriptions", "subscriptions"},
            {"[M]", "Browse Medicines", "medicines"},
            {"[P]", "Pharmacies Near Me", "pharmacies"},
            {"[R]", "Health Reports", "reports"},
            {"[SET]", "Settings", "settings"}
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
        
        JLabel statusLabel = new JLabel("Active Subscriptions: " + userSubscriptions.size());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nextOrderLabel = new JLabel("Next Order: " + getNextOrderDate());
        nextOrderLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nextOrderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nextOrderLabel);
        
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
            case "subscriptions":
                showSubscriptions();
                break;
            case "medicines":
                showMedicines();
                break;
            case "pharmacies":
                showPharmacies();
                break;
            case "reports":
                showHealthReports();
                break;
            case "settings":
                showSettings();
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
        
        // Active Subscriptions
        JPanel subsCard = createStatCard("[S]", String.valueOf(userSubscriptions.size()), "Active Subscriptions", new Color(40, 167, 69));
        
        // Money Saved
        JPanel savingsCard = createStatCard("[$]", "Rs." + getMoneySaved(), "Money Saved", new Color(255, 193, 7));
        
        // Health Score
        JPanel healthCard = createStatCard("[H]", getHealthScore() + "%", "Health Score", new Color(220, 53, 69));
        
        statsPanel.add(ordersCard);
        statsPanel.add(subsCard);
        statsPanel.add(savingsCard);
        statsPanel.add(healthCard);
        
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
    
    private void showSubscriptions() {
        // Implementation for subscription management
        mainContentPanel.removeAll();
        
        JPanel subsPanel = new JPanel(new BorderLayout());
        subsPanel.setBackground(new Color(248, 250, 252));
        subsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Medicine Subscriptions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Add subscription management UI here
        JLabel comingSoonLabel = new JLabel("Subscription management coming soon...");
        comingSoonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        subsPanel.add(titleLabel, BorderLayout.NORTH);
        subsPanel.add(comingSoonLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(subsPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showMedicines() {
        // Implementation for medicine browsing
        mainContentPanel.removeAll();
        
        JPanel medicinesPanel = new JPanel(new BorderLayout());
        medicinesPanel.setBackground(new Color(248, 250, 252));
        medicinesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Browse Medicines");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Add medicine browsing UI here
        JLabel comingSoonLabel = new JLabel("Medicine catalog coming soon...");
        comingSoonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        medicinesPanel.add(titleLabel, BorderLayout.NORTH);
        medicinesPanel.add(comingSoonLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(medicinesPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void showPharmacies() {
        // Implementation for pharmacy selection
    }
    
    private void showHealthReports() {
        // Implementation for health reports
    }
    
    private void showSettings() {
        // Implementation for user settings
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
        // AI Algorithm for generating recommendations
        aiRecommendations.clear();
        
        // Rule 1: Chronic condition management
        aiRecommendations.add(new Recommendation(
            "CHRONIC_CARE", "[BP]", "Blood Pressure Management",
            "Based on your purchase history, consider subscribing to monthly BP medication delivery.",
            "MED001", 95
        ));
        
        // Rule 2: Seasonal recommendations
        aiRecommendations.add(new Recommendation(
            "SEASONAL", "[W]", "Winter Health Protection",
            "Cold season is approaching. Stock up on immunity boosters and flu prevention medicines.",
            "MED002", 88
        ));
        
        // Rule 3: Preventive care
        aiRecommendations.add(new Recommendation(
            "PREVENTIVE", "[V]", "Vitamin D Supplement",
            "Your health profile suggests you may benefit from Vitamin D supplementation.",
            "MED003", 82
        ));
        
        // Rule 4: Refill reminder
        aiRecommendations.add(new Recommendation(
            "REFILL", "[R]", "Medication Refill Due",
            "Your diabetes medication is running low. Time for a refill to maintain your health routine.",
            "MED004", 98
        ));
        
        // Rule 5: Health optimization
        aiRecommendations.add(new Recommendation(
            "OPTIMIZATION", "[+]", "Health Score Improvement",
            "Add omega-3 supplements to boost your cardiovascular health score by 15%.",
            "MED005", 75
        ));
    }
    
    private void loadProducts() {
        // Load products from database
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
        
        // Mock search results
        JPanel resultsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        resultsPanel.setBackground(new Color(248, 250, 252));
        
        String[][] mockMedicines = {
            {"Paracetamol 500mg", "Rs.25", "Pain Relief"},
            {"Crocin Advance", "Rs.35", "Fever & Pain"},
            {"Dolo 650", "Rs.30", "Fever Reducer"},
            {"Aspirin 75mg", "Rs.45", "Heart Health"}
        };
        
        for (String[] medicine : mockMedicines) {
            if (medicine[0].toLowerCase().contains(searchTerm.toLowerCase()) || 
                medicine[2].toLowerCase().contains(searchTerm.toLowerCase())) {
                JPanel medicineCard = createMedicineCard(medicine[0], medicine[1], medicine[2]);
                resultsPanel.add(medicineCard);
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
    
    private JPanel createMedicineCard(String name, String price, String category) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(new Color(100, 100, 100));
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setBackground(new Color(46, 125, 50));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addToCartBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.addActionListener(e -> addToCart(name, price));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(priceLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addToCartBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void addToCart(String name, String price) {
        Product product = new Product("MED" + (cartItems.size() + 1), name, "Generic", 
            Double.parseDouble(price.replace("Rs.", "")), 10);
        cartItems.add(product);
        updateCartCount();
        
        JOptionPane.showMessageDialog(this, name + " added to cart!", "Cart", JOptionPane.INFORMATION_MESSAGE);
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
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel priceLabel = new JLabel("Rs." + item.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(46, 125, 50));
        
        JButton removeBtn = new JButton("Remove");
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        removeBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        removeBtn.setFocusPainted(false);
        removeBtn.addActionListener(e -> removeFromCart(index));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(removeBtn, BorderLayout.EAST);
        
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
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Confirm order for Rs." + String.format("%.2f", total) + "?", 
            "Checkout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear cart
            cartItems.clear();
            updateCartCount();
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\n\nYour order has been dispatched and will be delivered within 24 hours.\nOrder ID: EMD" + System.currentTimeMillis(),
                "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            
            // Return to dashboard
            showDashboardHome();
        }
    }
    
    private void showOrdersPage() {
        mainContentPanel.removeAll();
        
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBackground(new Color(248, 250, 252));
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Orders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // Mock order history
        JPanel ordersList = new JPanel();
        ordersList.setLayout(new BoxLayout(ordersList, BoxLayout.Y_AXIS));
        ordersList.setBackground(new Color(248, 250, 252));
        
        String[][] mockOrders = {
            {"EMD1234567890", "Paracetamol 500mg, Crocin Advance", "Rs.60", "Delivered", "2024-01-15"},
            {"EMD1234567891", "Dolo 650, Aspirin 75mg", "Rs.75", "In Transit", "2024-01-20"},
            {"EMD1234567892", "Vitamin D3, Calcium", "Rs.120", "Processing", "2024-01-22"}
        };
        
        for (String[] order : mockOrders) {
            JPanel orderCard = createOrderCard(order[0], order[1], order[2], order[3], order[4]);
            ordersList.add(orderCard);
            ordersList.add(Box.createVerticalStrut(15));
        }
        
        JScrollPane scrollPane = new JScrollPane(ordersList);
        scrollPane.setBorder(null);
        
        ordersPanel.add(titleLabel, BorderLayout.NORTH);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContentPanel.add(ordersPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
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
            new com.emedpharma.gui.MainApplication();
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
                case "subscriptions":
                    showSubscriptions();
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