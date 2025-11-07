package com.emedpharma.dao;

import com.emedpharma.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    
    public boolean insertOrder(Order order) {
        String sql = "INSERT INTO orders (pid, sid, uid, quantity, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, order.getMedicineId());
            ps.setString(2, order.getVendorId());
            ps.setString(3, order.getCustomerId());
            ps.setInt(4, order.getQuantity());
            ps.setDouble(5, order.getTotalPrice());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            return false;
        }
    }
    
    public List<Order> getOrdersByCustomer(String customerId) {
        String sql = "SELECT * FROM orders WHERE uid = ?";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("oid"));
                order.setMedicineId(rs.getString("pid"));
                order.setVendorId(rs.getString("sid"));
                order.setCustomerId(rs.getString("uid"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalPrice(rs.getDouble("price"));
                order.setOrderDateTime(rs.getTimestamp("orderdatetime"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }
    
    public List<Order> getOrdersByVendor(String vendorId) {
        String sql = "SELECT * FROM orders WHERE sid = ?";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vendorId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("oid"));
                order.setMedicineId(rs.getString("pid"));
                order.setVendorId(rs.getString("sid"));
                order.setCustomerId(rs.getString("uid"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalPrice(rs.getDouble("price"));
                order.setOrderDateTime(rs.getTimestamp("orderdatetime"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching vendor orders: " + e.getMessage());
        }
        return orders;
    }
    
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE oid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("oid"));
                order.setMedicineId(rs.getString("pid"));
                order.setVendorId(rs.getString("sid"));
                order.setCustomerId(rs.getString("uid"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalPrice(rs.getDouble("price"));
                order.setOrderDateTime(rs.getTimestamp("orderdatetime"));
                return order;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateOrderStatus(int orderId, String status) {
        // Note: Current DB doesn't have status column, this is for future enhancement
        System.out.println("Order " + orderId + " status updated to: " + status);
        return true;
    }
    
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("oid"));
                order.setMedicineId(rs.getString("pid"));
                order.setVendorId(rs.getString("sid"));
                order.setCustomerId(rs.getString("uid"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalPrice(rs.getDouble("price"));
                order.setOrderDateTime(rs.getTimestamp("orderdatetime"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all orders: " + e.getMessage());
        }
        return orders;
    }
    
    public List<Order> getOrdersByStatus(String status) {
        // Placeholder for future implementation
        return new ArrayList<>();
    }
    
    public List<Order> getOrdersByCustomerAndStatus(String customerId, String status) {
        // Placeholder for future implementation
        return getOrdersByCustomer(customerId);
    }
}