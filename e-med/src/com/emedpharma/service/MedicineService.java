package com.emedpharma.service;

import com.emedpharma.model.Medicine;
import java.util.List;

// Interface for Abstraction
public interface MedicineService {
    
    // Abstract methods for medicine operations
    boolean addMedicine(Medicine medicine);
    boolean updateMedicine(Medicine medicine);
    boolean deleteMedicine(String medicineId);
    Medicine getMedicineById(String medicineId);
    List<Medicine> getAllMedicines();
    List<Medicine> searchMedicines(String searchTerm);
    boolean updateStock(String medicineId, int quantity);
    int getStock(String medicineId, String vendorId);
    
    // Overloaded methods for Polymorphism
    List<Medicine> getMedicines(); // Get all medicines
    List<Medicine> getMedicines(String type); // Get medicines by type
    List<Medicine> getMedicines(String vendorId, boolean inStock); // Get medicines by vendor and stock status
}