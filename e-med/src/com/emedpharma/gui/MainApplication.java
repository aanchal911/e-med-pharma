package com.emedpharma.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class MainApplication extends JFrame {
    private static final Color PRIMARY_GREEN = new Color(27, 94, 32);
    private static final Color ACCENT_GREEN = new Color(46, 125, 50);
    private static final Color LIGHT_GREEN = new Color(200, 230, 201);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_TEXT = new Color(33, 37, 41);
    
    public MainApplication() {
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("e-MEDpharma - Your Digital Pharmacy Solution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(WHITE);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content with Scroll
        JPanel mainPanel = createMainPanel();
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        // Enable mouse wheel scrolling
        enableMouseWheelScrolling(mainScrollPane);
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        
        // Logo and Title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(PRIMARY_GREEN);
        
        JLabel logoLabel = new JLabel("[+]");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(WHITE);
        
        JLabel titleLabel = new JLabel("e-MEDpharma");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        JLabel subtitleLabel = new JLabel("Your Digital Health Partner");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_GREEN);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        logoPanel.add(subtitleLabel);
        
        // Navigation Buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navPanel.setBackground(PRIMARY_GREEN);
        
        JButton loginBtn = createNavButton("Get Started");
        JButton aboutBtn = createNavButton("About Us");
        JButton contactBtn = createNavButton("Contact");
        
        // Add action listeners for header buttons
        aboutBtn.addActionListener(e -> showAboutPage());
        contactBtn.addActionListener(e -> showContactPage());
        
        navPanel.add(aboutBtn);
        navPanel.add(contactBtn);
        navPanel.add(loginBtn);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void drawFloatingIcons(Graphics2D g2d) {
        g2d.setColor(new Color(ACCENT_GREEN.getRed(), ACCENT_GREEN.getGreen(), ACCENT_GREEN.getBlue(), 30));
        
        // Medical cross icons
        int[] xPos = {100, 300, 500, 700, 900};
        int[] yPos = {50, 150, 80, 120, 60};
        
        for (int i = 0; i < xPos.length; i++) {
            // Animated floating effect
            int animOffset = (int)(Math.sin(System.currentTimeMillis() * 0.001 + i) * 10);
            drawMedicalCross(g2d, xPos[i], yPos[i] + animOffset, 20);
        }
        
        // Pills
        g2d.setColor(new Color(LIGHT_GREEN.getRed(), LIGHT_GREEN.getGreen(), LIGHT_GREEN.getBlue(), 40));
        int[] pillX = {150, 400, 650, 850};
        int[] pillY = {200, 100, 180, 140};
        
        for (int i = 0; i < pillX.length; i++) {
            int animOffset = (int)(Math.cos(System.currentTimeMillis() * 0.0015 + i) * 8);
            drawPill(g2d, pillX[i], pillY[i] + animOffset, 15, 8);
        }
        
        // Start animation timer
        Timer animTimer = new Timer(50, e -> repaint());
        animTimer.start();
    }
    
    private void drawMedicalCross(Graphics2D g2d, int x, int y, int size) {
        // Vertical bar
        g2d.fillRect(x + size/3, y, size/3, size);
        // Horizontal bar
        g2d.fillRect(x, y + size/3, size, size/3);
    }
    
    private void drawPill(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.fillRoundRect(x, y, width, height, height, height);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(PRIMARY_GREEN);
        button.setBackground(WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 2),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
            }
        });
        
        if (text.equals("Get Started")) {
            button.setBackground(ACCENT_GREEN);
            button.setForeground(WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
            ));
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_GREEN);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(ACCENT_GREEN);
                }
            });
            button.addActionListener(e -> {
                setVisible(false);
                new AuthenticationFrame(this).setVisible(true);
            });
        }
        
        return button;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(WHITE);
        
        // Hero Section
        JPanel heroPanel = createHeroSection();
        mainPanel.add(heroPanel);
        
        // Features Section
        JPanel featuresPanel = createFeaturesSection();
        mainPanel.add(featuresPanel);
        
        // Stats Section
        JPanel statsPanel = createStatsSection();
        mainPanel.add(statsPanel);
        
        return mainPanel;
    }
    
    private JPanel createHeroSection() {
        JPanel heroPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Animated gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, LIGHT_GRAY,
                    getWidth(), getHeight(), new Color(240, 255, 240)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Floating medical icons
                drawFloatingIcons(g2d);
            }
        };
        heroPanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("<html><center>Smart Healthcare<br>at Your Fingertips</center></html>");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 52));
        mainTitle.setForeground(PRIMARY_GREEN);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add fade-in animation
        Timer fadeTimer = new Timer(50, null);
        fadeTimer.addActionListener(new ActionListener() {
            float alpha = 0.0f;
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadeTimer.stop();
                }
                mainTitle.setForeground(new Color(PRIMARY_GREEN.getRed(), PRIMARY_GREEN.getGreen(), PRIMARY_GREEN.getBlue(), (int)(255 * alpha)));
                mainTitle.repaint();
            }
        });
        fadeTimer.start();
        
        JLabel subtitle = new JLabel("<html><center>AI-powered medicine recommendations, subscription management,<br>and seamless multi-pharmacy access for better health outcomes</center></html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(DARK_TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(25, 0, 40, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton ctaButton = new JButton("Start Your Health Journey") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient button background
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_GREEN,
                    0, getHeight(), PRIMARY_GREEN
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Button text
                g2d.setColor(WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        ctaButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ctaButton.setForeground(WHITE);
        ctaButton.setBackground(ACCENT_GREEN);
        ctaButton.setBorder(BorderFactory.createEmptyBorder(18, 35, 18, 35));
        ctaButton.setFocusPainted(false);
        ctaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ctaButton.setContentAreaFilled(false);
        
        // Pulse animation
        Timer pulseTimer = new Timer(1000, e -> {
            ctaButton.setBackground(ctaButton.getBackground().equals(ACCENT_GREEN) ? PRIMARY_GREEN : ACCENT_GREEN);
            ctaButton.repaint();
        });
        pulseTimer.start();
        
        JButton learnButton = new JButton("Learn More") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Outline button
                g2d.setColor(WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.setColor(ACCENT_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);
                
                // Button text
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        learnButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        learnButton.setForeground(ACCENT_GREEN);
        learnButton.setBackground(WHITE);
        learnButton.setBorder(BorderFactory.createEmptyBorder(18, 35, 18, 35));
        learnButton.setFocusPainted(false);
        learnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        learnButton.setContentAreaFilled(false);
        
        learnButton.addActionListener(e -> showFeaturesPage());
        
        ctaButton.addActionListener(e -> {
            setVisible(false);
            new AuthenticationFrame(this).setVisible(true);
        });
        
        buttonPanel.add(ctaButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(learnButton);
        
        textPanel.add(mainTitle);
        textPanel.add(subtitle);
        textPanel.add(buttonPanel);
        
        heroPanel.add(textPanel, BorderLayout.CENTER);
        
        return heroPanel;
    }
    
    private JPanel createFeaturesSection() {
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setBackground(WHITE);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));
        
        JLabel sectionTitle = new JLabel("Why Choose e-MEDpharma?");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        sectionTitle.setForeground(PRIMARY_GREEN);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 40, 40));
        cardsPanel.setBackground(WHITE);
        
        cardsPanel.add(createFeatureCard("[AI]", "Smart Recommendations", "AI analyzes your health patterns for personalized medicine suggestions"));
        cardsPanel.add(createFeatureCard("[S]", "Subscription Management", "Never run out of medicines with automated refill reminders"));
        cardsPanel.add(createFeatureCard("[M]", "Multi-Pharmacy Access", "Compare prices and availability across multiple pharmacies"));
        cardsPanel.add(createFeatureCard("[H]", "Health Tracking", "Monitor your health journey with comprehensive reports"));
        
        featuresPanel.add(sectionTitle);
        featuresPanel.add(cardsPanel);
        
        return featuresPanel;
    }
    
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(5, 5, getWidth()-5, getHeight()-5, 20, 20);
                
                // Card background
                g2d.setColor(WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 20, 20);
                
                // Card border
                g2d.setColor(LIGHT_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-7, getHeight()-7, 20, 20);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));
        
        // Hover animation
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Timer hoverTimer = new Timer(20, new ActionListener() {
                    int offset = 0;
                    public void actionPerformed(ActionEvent e) {
                        offset += 1;
                        if (offset >= 5) {
                            ((Timer)e.getSource()).stop();
                        }
                        card.setBorder(BorderFactory.createEmptyBorder(35-offset, 30, 35+offset, 30));
                        card.repaint();
                    }
                });
                hoverTimer.start();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));
                card.repaint();
            }
        });
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        iconLabel.setForeground(ACCENT_GREEN);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(DARK_TEXT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descLabel);
        
        return card;
    }
    
    private JPanel createStatsSection() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(ACCENT_GREEN);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        
        JLabel sectionTitle = new JLabel("Trusted by Thousands");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        sectionTitle.setForeground(WHITE);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel numbersPanel = new JPanel(new GridLayout(1, 3, 60, 0));
        numbersPanel.setBackground(ACCENT_GREEN);
        
        numbersPanel.add(createStatCard("10,000+", "Happy Customers"));
        numbersPanel.add(createStatCard("500+", "Partner Pharmacies"));
        numbersPanel.add(createStatCard("50,000+", "Medicines Delivered"));
        
        statsPanel.add(sectionTitle);
        statsPanel.add(numbersPanel);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String number, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ACCENT_GREEN);
        
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        numberLabel.setForeground(WHITE);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textLabel.setForeground(LIGHT_GREEN);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        card.add(numberLabel);
        card.add(textLabel);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(PRIMARY_GREEN);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        
        JLabel copyrightLabel = new JLabel("© 2024 e-MEDpharma. All rights reserved. | Empowering Health Through Technology");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        copyrightLabel.setForeground(LIGHT_GREEN);
        
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        linksPanel.setBackground(PRIMARY_GREEN);
        
        JLabel privacyLabel = new JLabel("Privacy Policy");
        privacyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        privacyLabel.setForeground(WHITE);
        privacyLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel termsLabel = new JLabel("Terms of Service");
        termsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        termsLabel.setForeground(WHITE);
        termsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel supportLabel = new JLabel("Support");
        supportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        supportLabel.setForeground(WHITE);
        supportLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click listeners for footer links
        privacyLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPrivacyPage();
            }
        });
        
        termsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTermsPage();
            }
        });
        
        supportLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSupportPage();
            }
        });
        
        linksPanel.add(privacyLabel);
        linksPanel.add(termsLabel);
        linksPanel.add(supportLabel);
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(linksPanel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    public static void main(String[] args) {
        // Enhanced UI with animations
        
        SwingUtilities.invokeLater(() -> {
            // Splash screen effect
            JWindow splash = new JWindow();
            splash.setSize(400, 200);
            splash.setLocationRelativeTo(null);
            
            JPanel splashPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(27, 94, 32),
                        getWidth(), getHeight(), new Color(46, 125, 50)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
                    String text = "e-MEDpharma";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = getHeight() / 2;
                    g2d.drawString(text, x, y);
                    
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    String subtitle = "Loading your health companion...";
                    fm = g2d.getFontMetrics();
                    x = (getWidth() - fm.stringWidth(subtitle)) / 2;
                    g2d.drawString(subtitle, x, y + 30);
                }
            };
            
            splash.add(splashPanel);
            splash.setVisible(true);
            
            // Show splash for 2 seconds then open main app
            Timer splashTimer = new Timer(2000, e -> {
                splash.dispose();
                new MainApplication();
            });
            splashTimer.setRepeats(false);
            splashTimer.start();
        });
    }
    
    private void showAboutPage() {
        JFrame aboutFrame = new JFrame("About e-MEDpharma");
        aboutFrame.setSize(600, 500);
        aboutFrame.setLocationRelativeTo(this);
        aboutFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("About e-MEDpharma");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        String[] aboutText = {
            "We are India's leading digital pharmacy platform,",
            "committed to making healthcare accessible and affordable.",
            "",
            "Founded: 2024",
            "Mission: Revolutionizing healthcare through technology",
            "Vision: Health for all, powered by AI",
            "",
            "Our Services:",
            "• AI-powered medicine recommendations",
            "• Subscription management for regular medicines",
            "• Multi-pharmacy price comparison",
            "• Health tracking and reports",
            "• Secure prescription management",
            "• 24/7 customer support"
        };
        
        for (String line : aboutText) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(DARK_TEXT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> aboutFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        aboutFrame.add(headerPanel, BorderLayout.NORTH);
        aboutFrame.add(contentPanel, BorderLayout.CENTER);
        aboutFrame.add(buttonPanel, BorderLayout.SOUTH);
        aboutFrame.setVisible(true);
    }
    
    private void showContactPage() {
        JFrame contactFrame = new JFrame("Contact e-MEDpharma");
        contactFrame.setSize(500, 400);
        contactFrame.setLocationRelativeTo(this);
        contactFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Contact Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        String[] contactInfo = {
            "Head Office:",
            "123 Health Street, Medical District",
            "Ahmedabad, Gujarat 380001",
            "",
            "Phone: +91-9876543210",
            "Email: info@emedpharma.com",
            "Website: www.emedpharma.com",
            "",
            "Customer Support:",
            "Available 24/7 for your assistance",
            "Emergency Helpline: +91-9876543211"
        };
        
        for (String line : contactInfo) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(DARK_TEXT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(8));
        }
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> contactFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        contactFrame.add(headerPanel, BorderLayout.NORTH);
        contactFrame.add(contentPanel, BorderLayout.CENTER);
        contactFrame.add(buttonPanel, BorderLayout.SOUTH);
        contactFrame.setVisible(true);
    }
    
    private void showFeaturesPage() {
        JFrame featuresFrame = new JFrame("e-MEDpharma Features");
        featuresFrame.setSize(700, 600);
        featuresFrame.setLocationRelativeTo(this);
        featuresFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Our Features & Services");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String[][] features = {
            {"AI Recommendations", "Smart medicine suggestions based on your health history and patterns"},
            {"Subscription Management", "Never run out of medicines with automated refill reminders"},
            {"Multi-Pharmacy Access", "Compare prices and availability across multiple pharmacies"},
            {"Health Tracking", "Monitor your health journey with comprehensive reports"},
            {"Secure Prescriptions", "Upload and manage prescriptions with bank-level security"},
            {"24/7 Support", "Round-the-clock customer support for all your needs"}
        };
        
        for (String[] feature : features) {
            JPanel featureCard = new JPanel();
            featureCard.setLayout(new BoxLayout(featureCard, BoxLayout.Y_AXIS));
            featureCard.setBackground(LIGHT_GRAY);
            featureCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREEN, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            
            JLabel titleLbl = new JLabel(feature[0]);
            titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLbl.setForeground(PRIMARY_GREEN);
            titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JTextArea descArea = new JTextArea(feature[1]);
            descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descArea.setForeground(DARK_TEXT);
            descArea.setBackground(LIGHT_GRAY);
            descArea.setEditable(false);
            descArea.setWrapStyleWord(true);
            descArea.setLineWrap(true);
            descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            featureCard.add(titleLbl);
            featureCard.add(Box.createVerticalStrut(10));
            featureCard.add(descArea);
            
            contentPanel.add(featureCard);
        }
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> featuresFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        featuresFrame.add(headerPanel, BorderLayout.NORTH);
        featuresFrame.add(contentPanel, BorderLayout.CENTER);
        featuresFrame.add(buttonPanel, BorderLayout.SOUTH);
        featuresFrame.setVisible(true);
    }
    
    private void showPrivacyPage() {
        JFrame privacyFrame = new JFrame("Privacy Policy");
        privacyFrame.setSize(600, 500);
        privacyFrame.setLocationRelativeTo(this);
        privacyFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Privacy Policy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText(
            "Privacy Policy - e-MEDpharma\n\n" +
            "Your privacy is important to us. This policy explains how we collect, use, and protect your information.\n\n" +
            "Information We Collect:\n" +
            "• Personal information (name, email, phone)\n" +
            "• Health information (prescriptions, medical history)\n" +
            "• Usage data (app interactions, preferences)\n\n" +
            "How We Use Your Information:\n" +
            "• To provide personalized healthcare services\n" +
            "• To process orders and prescriptions\n" +
            "• To improve our AI recommendations\n" +
            "• To communicate important updates\n\n" +
            "Data Protection:\n" +
            "• Industry-standard encryption\n" +
            "• Secure servers and databases\n" +
            "• Regular security audits\n" +
            "• Compliance with healthcare regulations\n\n" +
            "Your Rights:\n" +
            "• Access your personal data\n" +
            "• Request data correction or deletion\n" +
            "• Opt-out of marketing communications\n\n" +
            "Contact us at privacy@emedpharma.com for any privacy concerns."
        );
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentArea.setForeground(DARK_TEXT);
        contentArea.setBackground(WHITE);
        contentArea.setEditable(false);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> privacyFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        privacyFrame.add(headerPanel, BorderLayout.NORTH);
        privacyFrame.add(scrollPane, BorderLayout.CENTER);
        privacyFrame.add(buttonPanel, BorderLayout.SOUTH);
        privacyFrame.setVisible(true);
    }
    
    private void showTermsPage() {
        JFrame termsFrame = new JFrame("Terms of Service");
        termsFrame.setSize(600, 500);
        termsFrame.setLocationRelativeTo(this);
        termsFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Terms of Service");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText(
            "Terms of Service - e-MEDpharma\n\n" +
            "By using e-MEDpharma services, you agree to these terms and conditions.\n\n" +
            "Service Agreement:\n" +
            "• e-MEDpharma provides digital pharmacy services\n" +
            "• All prescriptions must be valid and verified\n" +
            "• Services are available 24/7 across Gujarat\n\n" +
            "User Responsibilities:\n" +
            "• Provide accurate personal and medical information\n" +
            "• Use services only for legitimate healthcare needs\n" +
            "• Maintain confidentiality of account credentials\n" +
            "• Report any suspicious activities\n\n" +
            "Medical Disclaimer:\n" +
            "• AI recommendations are for informational purposes\n" +
            "• Always consult healthcare professionals\n" +
            "• Emergency situations require immediate medical attention\n\n" +
            "Payment & Refunds:\n" +
            "• Secure payment processing\n" +
            "• Refunds processed within 7-10 business days\n" +
            "• Prescription medicines may have return restrictions\n\n" +
            "Limitation of Liability:\n" +
            "• Services provided 'as is'\n" +
            "• Not liable for medical decisions based on AI recommendations\n\n" +
            "Contact legal@emedpharma.com for legal inquiries."
        );
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentArea.setForeground(DARK_TEXT);
        contentArea.setBackground(WHITE);
        contentArea.setEditable(false);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> termsFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        termsFrame.add(headerPanel, BorderLayout.NORTH);
        termsFrame.add(scrollPane, BorderLayout.CENTER);
        termsFrame.add(buttonPanel, BorderLayout.SOUTH);
        termsFrame.setVisible(true);
    }
    
    private void showSupportPage() {
        JFrame supportFrame = new JFrame("Customer Support");
        supportFrame.setSize(500, 400);
        supportFrame.setLocationRelativeTo(this);
        supportFrame.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("24/7 Customer Support");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        String[] supportInfo = {
            "We're here to help you 24/7!",
            "",
            "Contact Methods:",
            "Phone: +91-9876543210",
            "WhatsApp: +91-9876543210",
            "Email: support@emedpharma.com",
            "Live Chat: Available in app",
            "",
            "Emergency Helpline: +91-9876543211",
            "",
            "Support Hours:",
            "24/7 for emergencies",
            "9 AM - 9 PM for general queries",
            "",
            "Average Response Time:",
            "Phone: Immediate",
            "Email: Within 2 hours",
            "Chat: Within 5 minutes"
        };
        
        for (String line : supportInfo) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(DARK_TEXT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(ACCENT_GREEN);
        closeBtn.setForeground(WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> supportFrame.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(closeBtn);
        
        supportFrame.add(headerPanel, BorderLayout.NORTH);
        supportFrame.add(contentPanel, BorderLayout.CENTER);
        supportFrame.add(buttonPanel, BorderLayout.SOUTH);
        supportFrame.setVisible(true);
    }
    
    private void enableMouseWheelScrolling(JScrollPane scrollPane) {
        // Enhanced mouse wheel scrolling
        scrollPane.addMouseWheelListener(e -> {
            if (e.getScrollType() == java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int unitsToScroll = e.getUnitsToScroll();
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                int currentValue = verticalScrollBar.getValue();
                int newValue = currentValue + (unitsToScroll * 20); // Increase scroll speed
                
                newValue = Math.max(verticalScrollBar.getMinimum(), 
                          Math.min(verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount(), newValue));
                
                verticalScrollBar.setValue(newValue);
            }
        });
        
        // Add keyboard navigation
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("UP"), "scrollUp");
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("DOWN"), "scrollDown");
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("PAGE_UP"), "pageUp");
        scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke("PAGE_DOWN"), "pageDown");
        
        scrollPane.getActionMap().put("scrollUp", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() - sb.getUnitIncrement());
            }
        });
        
        scrollPane.getActionMap().put("scrollDown", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() + sb.getUnitIncrement());
            }
        });
        
        scrollPane.getActionMap().put("pageUp", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() - sb.getBlockIncrement());
            }
        });
        
        scrollPane.getActionMap().put("pageDown", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() + sb.getBlockIncrement());
            }
        });
    }
}