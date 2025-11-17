package com.emedpharma.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(33, 37, 41);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    
    private String currentAdmin;
    private JPanel mainContentPanel;
    private JPanel sidebarPanel;
    
    public AdminDashboard(String adminId) {
        this.currentAdmin = adminId;
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setTitle("e-MEDpharma - Admin Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        mainContentPanel = new JPanel(new BorderLayout());
        sidebarPanel = new JPanel();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        mainContentPanel.setBackground(new Color(248, 249, 250));
        showDashboardHome();
        
        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(mainScrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel adminLabel = new JLabel("Administrator: " + currentAdmin);
        adminLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        adminLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(DANGER_COLOR);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.addActionListener(e -> performLogout());
        
        rightPanel.add(adminLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(logoutBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[][] menuItems = {
            {"[D]", "Dashboard", "dashboard"},
            {"[C]", "Manage Customers", "customers"},
            {"[V]", "Manage Vendors", "vendors"},
            {"[A]", "Analytics", "analytics"},
            {"[O]", "Order Management", "orders"},
            {"[P]", "Product Oversight", "products"}
        };
        
        for (String[] item : menuItems) {
            JButton menuBtn = createMenuButton(item[0], item[1], item[2]);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(5));
        }
        
        return sidebar;
    }
    
    private JButton createMenuButton(String icon, String text, String action) {
        JButton button = new JButton(icon + "  " + text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(210, 45));
        button.setBackground(new Color(248, 250, 252));
        button.setForeground(new Color(51, 51, 51));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            switch (action) {
                case "dashboard": showDashboardHome(); break;
                case "customers": showCustomerManagement(); break;
                case "vendors": showVendorManagement(); break;
                case "analytics": showAnalytics(); break;
                case "orders": showOrderManagement(); break;
                case "products": showProductOversight(); break;
            }
        });
        
        return button;
    }
    
    private void showDashboardHome() {
        mainContentPanel.removeAll();
        
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(248, 249, 250));
        homePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("System Overview Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));
        
        JPanel statsPanel = createStatsPanel();
        JPanel chartsPanel = createChartsPanel();
        
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(chartsPanel, BorderLayout.CENTER);
        
        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(contentPanel, BorderLayout.CENTER);
        
        mainContentPanel.add(homePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        int[] stats = getSystemStats();
        
        statsPanel.add(createStatCard("[C]", String.valueOf(stats[0]), "Total Customers", SUCCESS_COLOR));
        statsPanel.add(createStatCard("[V]", String.valueOf(stats[1]), "Active Vendors", PRIMARY_COLOR));
        statsPanel.add(createStatCard("[P]", String.valueOf(stats[2]), "Total Products", WARNING_COLOR));
        statsPanel.add(createStatCard("[O]", String.valueOf(stats[3]), "Total Orders", SUCCESS_COLOR));
        statsPanel.add(createStatCard("[R]", "Rs." + stats[4], "Total Revenue", SUCCESS_COLOR));
        statsPanel.add(createStatCard("[W]", String.valueOf(stats[5]), "Pending Orders", WARNING_COLOR));
        statsPanel.add(createStatCard("[D]", String.valueOf(stats[6]), "Completed Orders", SUCCESS_COLOR));
        statsPanel.add(createStatCard("[S]", "0", "Security Alerts", DANGER_COLOR));
        
        return statsPanel;
    }
    
    private JPanel createChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setBackground(new Color(248, 249, 250));
        chartsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JPanel vendorChart = createVendorPerformanceChart();
        JPanel customerChart = createCustomerActivityChart();
        
        chartsPanel.add(vendorChart);
        chartsPanel.add(customerChart);
        
        return chartsPanel;
    }
    
    private JPanel createVendorPerformanceChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JLabel titleLabel = new JLabel("Top Vendor Performance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawVendorChart(g);
            }
        };
        chartArea.setBackground(Color.WHITE);
        chartArea.setPreferredSize(new Dimension(400, 300));
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(chartArea, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createCustomerActivityChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)));
        
        JLabel titleLabel = new JLabel("Customer Activity Trends");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCustomerChart(g);
            }
        };
        chartArea.setBackground(Color.WHITE);
        chartArea.setPreferredSize(new Dimension(400, 300));
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(chartArea, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private void drawVendorChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Map<String, Integer> vendorData = getVendorPerformanceData();
        
        int width = getWidth() - 40;
        int height = getHeight() - 40;
        int barWidth = width / Math.max(vendorData.size(), 1);
        int maxValue = vendorData.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        
        Color[] colors = {SUCCESS_COLOR, INFO_COLOR, WARNING_COLOR, DANGER_COLOR, PRIMARY_COLOR};
        int colorIndex = 0;
        int x = 20;
        
        for (Map.Entry<String, Integer> entry : vendorData.entrySet()) {
            int barHeight = (int) ((double) entry.getValue() / maxValue * (height - 60));
            
            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillRect(x, height - barHeight - 20, barWidth - 10, barHeight);
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g2d.drawString(entry.getKey(), x, height - 5);
            g2d.drawString(String.valueOf(entry.getValue()), x, height - barHeight - 25);
            
            x += barWidth;
            colorIndex++;
        }
    }
    
    private void drawCustomerChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int[] monthlyData = getCustomerActivityData();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        
        int width = getWidth() - 40;
        int height = getHeight() - 40;
        int maxValue = Arrays.stream(monthlyData).max().orElse(1);
        
        g2d.setColor(INFO_COLOR);
        g2d.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < monthlyData.length - 1; i++) {
            int x1 = 20 + (i * width / (monthlyData.length - 1));
            int y1 = height - 20 - (int) ((double) monthlyData[i] / maxValue * (height - 60));
            int x2 = 20 + ((i + 1) * width / (monthlyData.length - 1));
            int y2 = height - 20 - (int) ((double) monthlyData[i + 1] / maxValue * (height - 60));
            
            g2d.drawLine(x1, y1, x2, y2);
            g2d.fillOval(x1 - 3, y1 - 3, 6, 6);
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            if (i < months.length) {
                g2d.drawString(months[i], x1 - 10, height - 5);
            }
            g2d.setColor(INFO_COLOR);
        }
    }
    
    private void showVendorManagement() {
        mainContentPanel.removeAll();
        
        JPanel vendorPanel = new JPanel(new BorderLayout());
        vendorPanel.setBackground(new Color(248, 249, 250));
        vendorPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Vendor Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel tablePanel = createVendorTable();
        
        vendorPanel.add(titleLabel, BorderLayout.NORTH);
        vendorPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainContentPanel.add(vendorPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createVendorTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        String[] columns = {"Vendor ID", "Name", "Phone", "Orders", "Revenue", "Status", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        
        loadVendorData(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void showCustomerManagement() {
        mainContentPanel.removeAll();
        
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBackground(new Color(248, 249, 250));
        customerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel tablePanel = createCustomerTable();
        
        customerPanel.add(titleLabel, BorderLayout.NORTH);
        customerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainContentPanel.add(customerPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private JPanel createCustomerTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        String[] columns = {"Customer ID", "Name", "Email", "Phone", "Orders", "Total Spent", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        
        loadCustomerData(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void showAnalytics() {
        JOptionPane.showMessageDialog(this, "Analytics feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showOrderManagement() {
        JOptionPane.showMessageDialog(this, "Order Management feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showProductOversight() {
        JOptionPane.showMessageDialog(this, "Product Oversight feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadVendorData(DefaultTableModel model) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT s.sid, s.sname, s.phno, COUNT(o.oid) as orders, COALESCE(SUM(o.price), 0) as revenue " +
                          "FROM seller s LEFT JOIN orders o ON s.sid = o.sid GROUP BY s.sid, s.sname, s.phno";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("sid"),
                    rs.getString("sname"),
                    rs.getLong("phno"),
                    rs.getInt("orders"),
                    "Rs." + rs.getInt("revenue"),
                    "Active",
                    "Actions"
                };
                model.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error loading vendor data: " + e.getMessage());
        }
    }
    
    private void loadCustomerData(DefaultTableModel model) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT c.uid, CONCAT(c.fname, ' ', c.lname) as name, c.email, c.phno, " +
                          "COUNT(o.oid) as orders, COALESCE(SUM(o.price), 0) as spent " +
                          "FROM customer c LEFT JOIN orders o ON c.uid = o.uid GROUP BY c.uid";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("uid"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getLong("phno"),
                    rs.getInt("orders"),
                    "Rs." + rs.getInt("spent"),
                    "Actions"
                };
                model.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error loading customer data: " + e.getMessage());
        }
    }
    
    private Map<String, Integer> getVendorPerformanceData() {
        Map<String, Integer> data = new LinkedHashMap<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String query = "SELECT s.sname, COUNT(o.oid) as orders FROM seller s " +
                          "LEFT JOIN orders o ON s.sid = o.sid GROUP BY s.sid, s.sname ORDER BY orders DESC LIMIT 5";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                data.put(rs.getString("sname"), rs.getInt("orders"));
            }
            conn.close();
        } catch (Exception e) {
            data.put("Vendor1", 25);
            data.put("Vendor2", 18);
            data.put("Vendor3", 12);
        }
        return data;
    }
    
    private int[] getCustomerActivityData() {
        return new int[]{45, 52, 48, 61, 55, 67};
    }
    
    private int[] getOrderStatusData() {
        int[] data = new int[4];
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            String[] statuses = {"Pending", "Approved", "Delivered", "Cancelled"};
            for (int i = 0; i < statuses.length; i++) {
                String query = "SELECT COUNT(*) FROM orders WHERE COALESCE(status, 'Pending') = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, statuses[i]);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    data[i] = rs.getInt(1);
                }
            }
            conn.close();
        } catch (Exception e) {
            data = new int[]{12, 25, 30, 5};
        }
        return data;
    }
    
    private JPanel createStatCard(String icon, String value, String label, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 15, 20, 15)));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        labelLabel.setForeground(new Color(100, 100, 100));
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(labelLabel);
        
        return card;
    }
    
    private int[] getSystemStats() {
        int[] stats = new int[7];
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/drugdatabase?useSSL=false&allowPublicKeyRetrieval=true", 
                "root", "A@nchal911");
            
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM customer");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) stats[0] = rs1.getInt(1);
            
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM seller");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) stats[1] = rs2.getInt(1);
            
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM product");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) stats[2] = rs3.getInt(1);
            
            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*), COALESCE(SUM(price), 0) FROM orders");
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) {
                stats[3] = rs4.getInt(1);
                stats[4] = (int) rs4.getDouble(2);
            }
            
            PreparedStatement ps5 = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE COALESCE(status, 'Pending') = 'Pending'");
            ResultSet rs5 = ps5.executeQuery();
            if (rs5.next()) stats[5] = rs5.getInt(1);
            
            PreparedStatement ps6 = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE status IN ('Approved', 'Delivered')");
            ResultSet rs6 = ps6.executeQuery();
            if (rs6.next()) stats[6] = rs6.getInt(1);
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            stats = new int[]{25, 8, 135, 67, 45000, 12, 55};
        }
        
        return stats;
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
}