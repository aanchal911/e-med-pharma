package com.emedpharma.dao;

import com.emedpharma.model.Customer;
import java.sql.*;

public class CustomerDAO {
    
    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO customer (uid, pass, fname, lname, email, address, phno) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customer.getUserId());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getFirstName());
            ps.setString(4, customer.getLastName());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getAddress());
            ps.setLong(7, customer.getPhoneNumber());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting customer: " + e.getMessage());
            return false;
        }
    }
    
    public Customer getCustomerById(String customerId) {
        String sql = "SELECT * FROM customer WHERE uid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setUserId(rs.getString("uid"));
                customer.setPassword(rs.getString("pass"));
                customer.setFirstName(rs.getString("fname"));
                customer.setLastName(rs.getString("lname"));
                customer.setEmail(rs.getString("email"));
                customer.setAddress(rs.getString("address"));
                customer.setPhoneNumber(rs.getLong("phno"));
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching customer: " + e.getMessage());
        }
        return null;
    }
    
    public boolean authenticateCustomer(String userId, String password) {
        String sql = "SELECT uid, pass FROM customer WHERE uid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return password.equals(rs.getString("pass"));
            }
        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        return false;
    }
}