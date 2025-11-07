package com.emedpharma.dao;

import com.emedpharma.model.Medicine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    
    public boolean insertMedicine(Medicine medicine) {
        String sql = "INSERT INTO product (pid, pname, manufacturer, mfg, exp, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, medicine.getMedicineId());
            ps.setString(2, medicine.getName());
            ps.setString(3, medicine.getManufacturer());
            ps.setDate(4, new java.sql.Date(medicine.getManufacturingDate().getTime()));
            ps.setDate(5, new java.sql.Date(medicine.getExpiryDate().getTime()));
            ps.setDouble(6, medicine.getPrice());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting medicine: " + e.getMessage());
            return false;
        }
    }
    
    public List<Medicine> getAllMedicines() {
        String sql = "SELECT * FROM product";
        List<Medicine> medicines = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getString("pid"));
                medicine.setName(rs.getString("pname"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setManufacturingDate(rs.getDate("mfg"));
                medicine.setExpiryDate(rs.getDate("exp"));
                medicine.setPrice(rs.getDouble("price"));
                medicines.add(medicine);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching medicines: " + e.getMessage());
        }
        return medicines;
    }
    
    public Medicine getMedicineById(String medicineId) {
        String sql = "SELECT * FROM product WHERE pid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, medicineId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getString("pid"));
                medicine.setName(rs.getString("pname"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setManufacturingDate(rs.getDate("mfg"));
                medicine.setExpiryDate(rs.getDate("exp"));
                medicine.setPrice(rs.getDouble("price"));
                return medicine;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching medicine: " + e.getMessage());
        }
        return null;
    }
    
    public List<Medicine> searchMedicines(String searchTerm) {
        String sql = "SELECT * FROM product WHERE pname LIKE ? OR manufacturer LIKE ?";
        List<Medicine> medicines = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getString("pid"));
                medicine.setName(rs.getString("pname"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setManufacturingDate(rs.getDate("mfg"));
                medicine.setExpiryDate(rs.getDate("exp"));
                medicine.setPrice(rs.getDouble("price"));
                medicines.add(medicine);
            }
        } catch (SQLException e) {
            System.out.println("Error searching medicines: " + e.getMessage());
        }
        return medicines;
    }
    
    public boolean updateMedicinePrice(String medicineId, double newPrice) {
        String sql = "UPDATE product SET price = ? WHERE pid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, newPrice);
            ps.setString(2, medicineId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating medicine price: " + e.getMessage());
            return false;
        }
    }
}