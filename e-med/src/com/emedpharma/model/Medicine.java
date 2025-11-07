package com.emedpharma.model;

import java.util.Date;

public class Medicine {
    private String medicineId;
    private String name;
    private String manufacturer;
    private Date manufacturingDate;
    private Date expiryDate;
    private double price;
    private String type; // SCHEDULED or GENERAL
    private String description;
    
    public Medicine() {}
    
    public Medicine(String medicineId, String name, String manufacturer, 
                   Date manufacturingDate, Date expiryDate, double price, String type) {
        this.medicineId = medicineId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.type = type;
    }
    
    // Getters and Setters (Encapsulation)
    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public Date getManufacturingDate() { return manufacturingDate; }
    public void setManufacturingDate(Date manufacturingDate) { this.manufacturingDate = manufacturingDate; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // Business methods
    public boolean isExpired() {
        return new Date().after(expiryDate);
    }
    
    public boolean requiresPrescription() {
        return "SCHEDULED".equalsIgnoreCase(type);
    }
    
    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId='" + medicineId + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}