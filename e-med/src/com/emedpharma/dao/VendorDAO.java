package com.emedpharma.dao;

import com.emedpharma.model.Vendor;
import java.sql.*;

public class VendorDAO {
    
    public boolean insertVendor(Vendor vendor) {
        String sql = "INSERT INTO seller (sid, sname, pass, address, phno) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vendor.getVendorId());
            ps.setString(2, vendor.getShopName());
            ps.setString(3, vendor.getPassword());
            ps.setString(4, vendor.getAddress());
            ps.setLong(5, vendor.getPhoneNumber());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting vendor: " + e.getMessage());
            return false;
        }
    }
    
    public Vendor getVendorById(String vendorId) {
        String sql = "SELECT * FROM seller WHERE sid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vendorId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Vendor vendor = new Vendor();
                vendor.setVendorId(rs.getString("sid"));
                vendor.setShopName(rs.getString("sname"));
                vendor.setPassword(rs.getString("pass"));
                vendor.setAddress(rs.getString("address"));
                vendor.setPhoneNumber(rs.getLong("phno"));
                return vendor;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching vendor: " + e.getMessage());
        }
        return null;
    }
    
    public boolean authenticateVendor(String vendorId, String password) {
        String sql = "SELECT sid, pass FROM seller WHERE sid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vendorId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return password.equals(rs.getString("pass"));
            }
        } catch (SQLException e) {
            System.out.println("Vendor authentication error: " + e.getMessage());
        }
        return false;
    }
}