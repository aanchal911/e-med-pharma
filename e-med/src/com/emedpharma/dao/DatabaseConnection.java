package com.emedpharma.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton pattern for database connection
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "A@nchal911";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    
    // Singleton getInstance method
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    // Get connection method
    public static Connection getConnection() {
        try {
            if (getInstance().connection == null || getInstance().connection.isClosed()) {
                getInstance().connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            return getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Error getting connection: " + e.getMessage());
            return null;
        }
    }
    
    // Close connection method
    public static void closeConnection() {
        try {
            if (getInstance().connection != null && !getInstance().connection.isClosed()) {
                getInstance().connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}