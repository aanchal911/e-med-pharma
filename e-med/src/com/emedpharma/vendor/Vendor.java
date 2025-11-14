package com.emedpharma.model;

public class Vendor extends User {
    private String vendorId;
    private String shopName;
    
    public Vendor() {}
    
    public Vendor(String vendorId, String shopName, String email, String password, 
                 String address, long phoneNumber) {
        super(vendorId, shopName, email, password, address, phoneNumber);
        this.vendorId = vendorId;
        this.shopName = shopName;
    }
    
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    
    @Override
    public String getUserType() {
        return "VENDOR";
    }
    
    // Vendor-specific methods
    public void addProduct(Medicine medicine, int quantity) {
        // Logic for adding product to inventory
        System.out.println("Product added: " + medicine.getName() + ", quantity: " + quantity);
    }
    
    public void updateStock(String medicineId, int newQuantity) {
        // Logic for updating stock
        System.out.println("Stock updated for medicine: " + medicineId + ", new quantity: " + newQuantity);
    }
    
    public void verifyPrescription(String prescriptionId) {
        // Logic for prescription verification
        System.out.println("Prescription verified: " + prescriptionId);
    }
    
    public void updateOrderStatus(int orderId, String status) {
        // Logic for updating order status
        System.out.println("Order " + orderId + " status updated to: " + status);
    }
}