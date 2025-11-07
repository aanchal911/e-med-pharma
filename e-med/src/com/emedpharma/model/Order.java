package com.emedpharma.model;

import java.util.Date;

public class Order {
    private int orderId;
    private String customerId;
    private String vendorId;
    private String medicineId;
    private int quantity;
    private double totalPrice;
    private Date orderDateTime;
    private String status; // PENDING, CONFIRMED, DISPATCHED, DELIVERED
    private String prescriptionPath;
    
    public Order() {}
    
    public Order(String customerId, String vendorId, String medicineId, 
                int quantity, double totalPrice) {
        this.customerId = customerId;
        this.vendorId = vendorId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDateTime = new Date();
        this.status = "PENDING";
    }
    
    // Getters and Setters (Encapsulation)
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    
    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public Date getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(Date orderDateTime) { this.orderDateTime = orderDateTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPrescriptionPath() { return prescriptionPath; }
    public void setPrescriptionPath(String prescriptionPath) { this.prescriptionPath = prescriptionPath; }
    
    // Business methods
    public boolean isPending() { return "PENDING".equals(status); }
    public boolean isConfirmed() { return "CONFIRMED".equals(status); }
    public boolean isDispatched() { return "DISPATCHED".equals(status); }
    public boolean isDelivered() { return "DELIVERED".equals(status); }
    
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Order " + orderId + " status updated to: " + newStatus);
    }
}