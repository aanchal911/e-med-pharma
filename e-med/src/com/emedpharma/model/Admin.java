package com.emedpharma.model;

public class Admin extends User {
    private String adminId;
    private String role;
    
    public Admin() {}
    
    public Admin(String adminId, String name, String email, String password, String role) {
        super(adminId, name, email, password, "Admin Office", 0);
        this.adminId = adminId;
        this.role = role;
    }
    
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    @Override
    public String getUserType() {
        return "ADMIN";
    }
    
    // Admin-specific methods
    public void verifyVendor(String vendorId) {
        // Logic for vendor verification
        System.out.println("Vendor verified: " + vendorId);
    }
    
    public void monitorTransactions() {
        // Logic for monitoring system transactions
        System.out.println("Monitoring all system transactions");
    }
    
    public void generateReports() {
        // Logic for generating system reports
        System.out.println("Generating system reports");
    }
    
    public void manageSystemData() {
        // Logic for managing system data integrity
        System.out.println("Managing system data integrity");
    }
}