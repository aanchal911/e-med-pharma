package com.emedpharma.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InventoryDAO {
    
    public boolean addToInventory(String productId, String productName, int quantity, String vendorId) {
        String sql = "INSERT INTO inventory (pid, pname, quantity, sid) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, productId);
            ps.setString(2, productName);
            ps.setInt(3, quantity);
            ps.setString(4, vendorId);
            ps.setInt(5, quantity);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding to inventory: " + e.getMessage());
            return false;
        }
    }
    
    public int getStock(String productId, String vendorId) {
        String sql = "SELECT quantity FROM inventory WHERE pid = ? AND sid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, productId);
            ps.setString(2, vendorId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            System.out.println("Error getting stock: " + e.getMessage());
        }
        return 0;
    }
    
    public boolean updateStock(String productId, String vendorId, int newQuantity) {
        String sql = "UPDATE inventory SET quantity = ? WHERE pid = ? AND sid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newQuantity);
            ps.setString(2, productId);
            ps.setString(3, vendorId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
            return false;
        }
    }
    
    public Map<String, Integer> getVendorInventory(String vendorId) {
        String sql = "SELECT pid, pname, quantity FROM inventory WHERE sid = ?";
        Map<String, Integer> inventory = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vendorId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                inventory.put(rs.getString("pname"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching vendor inventory: " + e.getMessage());
        }
        return inventory;
    }
    
    public boolean reduceStock(String productId, String vendorId, int quantity) {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE pid = ? AND sid = ? AND quantity >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setString(2, productId);
            ps.setString(3, vendorId);
            ps.setInt(4, quantity);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error reducing stock: " + e.getMessage());
            return false;
        }
    }
}