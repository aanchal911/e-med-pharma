package com.emedpharma.model;

import java.util.List;

public class Customer extends User {
    private String firstName;
    private String lastName;
    
    public Customer() {}
    
    public Customer(String userId, String firstName, String lastName, String email, 
                   String password, String address, long phoneNumber) {
        super(userId, firstName + " " + lastName, email, password, address, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    @Override
    public String getUserType() {
        return "CUSTOMER";
    }
    
    // Customer-specific methods
    public void uploadPrescription(String prescriptionPath) {
        // Logic for prescription upload
        System.out.println("Prescription uploaded: " + prescriptionPath);
    }
    
    public void placeOrder(String medicineId, int quantity) {
        // Logic for placing order
        System.out.println("Order placed for medicine: " + medicineId + ", quantity: " + quantity);
    }
    
    public void trackOrder(int orderId) {
        // Logic for order tracking
        System.out.println("Tracking order: " + orderId);
    }
}