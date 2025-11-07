package com.emedpharma.model;

import java.util.Date;

public class Expense {
    private int expenseId;
    private String vendorId;
    private String expenseType; // PURCHASE, OPERATIONAL, MAINTENANCE
    private double amount;
    private String description;
    private Date expenseDate;
    private String category;
    
    public Expense() {}
    
    public Expense(String vendorId, String expenseType, double amount, String description) {
        this.vendorId = vendorId;
        this.expenseType = expenseType;
        this.amount = amount;
        this.description = description;
        this.expenseDate = new Date();
    }
    
    // Getters and Setters (Encapsulation)
    public int getExpenseId() { return expenseId; }
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    
    public String getExpenseType() { return expenseType; }
    public void setExpenseType(String expenseType) { this.expenseType = expenseType; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getExpenseDate() { return expenseDate; }
    public void setExpenseDate(Date expenseDate) { this.expenseDate = expenseDate; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    // Business methods
    public boolean isPurchaseExpense() { return "PURCHASE".equals(expenseType); }
    public boolean isOperationalExpense() { return "OPERATIONAL".equals(expenseType); }
    
    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", vendorId='" + vendorId + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                '}';
    }
}