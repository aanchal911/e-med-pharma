package com.emedpharma.customer;

import java.util.List;
import com.emedpharma.model.User;

/**
 * INHERITANCE: Customer class extends User class (IS-A relationship)
 * ENCAPSULATION: Private fields with public getter/setter methods
 * POLYMORPHISM: Overrides abstract methods from parent User class
 * 
 * This class represents a Customer in the pharmacy system
 * Inherits common user properties from User class and adds customer-specific features
 */
public class Customer extends User {
    // ENCAPSULATION: Private fields - data hiding principle
    private String firstName;  // Customer's first name
    private String lastName;   // Customer's last name
    
    // Default constructor - required for object creation
    public Customer() {}
    
    /**
     * CONSTRUCTOR OVERLOADING: Parameterized constructor
     * INHERITANCE: Calls parent class constructor using super()
     */
    public Customer(String userId, String firstName, String lastName, String email, 
                   String password, String address, long phoneNumber) {
        // INHERITANCE: Call parent class constructor to initialize inherited fields
        super(userId, firstName + " " + lastName, email, password, address, phoneNumber);
        // Initialize customer-specific fields
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // ENCAPSULATION: Getter and Setter methods for controlled access to private fields
    
    /**
     * Getter method for firstName - provides controlled access to private field
     * ENCAPSULATION: External classes can read but not directly modify the field
     */
    public String getFirstName() { return firstName; }
    
    /**
     * Setter method for firstName - provides controlled modification of private field
     * ENCAPSULATION: Can add validation logic here if needed
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    /**
     * Getter method for lastName
     */
    public String getLastName() { return lastName; }
    
    /**
     * Setter method for lastName
     */
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    /**
     * METHOD OVERRIDING: Overrides abstract method from parent User class
     * POLYMORPHISM: Same method name but different implementation for Customer
     * @Override annotation ensures we're actually overriding a parent method
     */
    @Override
    public String getUserType() {
        return "CUSTOMER"; // Customer-specific implementation
    }
    
    // CUSTOMER-SPECIFIC METHODS: These methods are unique to Customer class
    // ENCAPSULATION: Business logic is encapsulated within appropriate methods
    
    /**
     * Customer-specific method for uploading prescription
     * ENCAPSULATION: Hides the complexity of prescription handling
     */
    public void uploadPrescription(String prescriptionPath) {
        // Logic for prescription upload (would integrate with file system/database)
        System.out.println("Prescription uploaded: " + prescriptionPath);
    }
    
    /**
     * Customer-specific method for placing orders
     * ENCAPSULATION: Order placement logic is contained within this method
     */
    public void placeOrder(String medicineId, int quantity) {
        // Logic for placing order (would integrate with order management system)
        System.out.println("Order placed for medicine: " + medicineId + ", quantity: " + quantity);
    }
    
    /**
     * Customer-specific method for tracking orders
     * ENCAPSULATION: Order tracking logic is encapsulated here
     */
    public void trackOrder(int orderId) {
        // Logic for order tracking (would integrate with tracking system)
        System.out.println("Tracking order: " + orderId);
    }
}