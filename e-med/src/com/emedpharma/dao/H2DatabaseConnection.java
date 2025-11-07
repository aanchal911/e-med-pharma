package com.emedpharma.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DatabaseConnection {
    private static final String URL = "jdbc:h2:mem:emedpharma;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    private static Connection connection;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                createTables();
            }
            return connection;
        } catch (Exception e) {
            System.out.println("H2 Connection Error: " + e.getMessage());
            return null;
        }
    }
    
    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create tables automatically
            stmt.execute("CREATE TABLE IF NOT EXISTS customer (" +
                "uid VARCHAR(20) PRIMARY KEY, " +
                "pass VARCHAR(20), " +
                "fname VARCHAR(15), " +
                "lname VARCHAR(15), " +
                "email VARCHAR(30), " +
                "address VARCHAR(128), " +
                "phno BIGINT)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS seller (" +
                "sid VARCHAR(15) PRIMARY KEY, " +
                "sname VARCHAR(20), " +
                "pass VARCHAR(20), " +
                "address VARCHAR(128), " +
                "phno BIGINT)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS product (" +
                "pid VARCHAR(15) PRIMARY KEY, " +
                "pname VARCHAR(20) UNIQUE, " +
                "manufacturer VARCHAR(20), " +
                "mfg DATE, " +
                "exp DATE, " +
                "price INT)");
            
            System.out.println("H2 Database initialized successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}