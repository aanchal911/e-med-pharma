package com.emedpharma.model;

/**
 * ABSTRACTION: Abstract User class - cannot be instantiated directly
 * INHERITANCE: Base class for Customer, Vendor, and Admin classes
 * ENCAPSULATION: Private fields with controlled access through getters/setters
 * 
 * This abstract class defines common properties and behaviors for all users
 * Forces child classes to implement getUserType() method (Contract enforcement)
 */
public abstract class User {
    // ENCAPSULATION: All fields are private - data hiding principle
    private String userId;      // Unique identifier for user
    private String name;        // Full name of user
    private String email;       // Email address for communication
    private String password;    // Password for authentication (should be hashed)
    private String address;     // Physical address
    private long phoneNumber;   // Contact phone number
    
    /**
     * Default constructor - required for inheritance
     */
    public User() {}
    
    /**
     * CONSTRUCTOR OVERLOADING: Parameterized constructor
     * Initializes all user properties at object creation
     */
    public User(String userId, String name, String email, String password, String address, long phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    // ENCAPSULATION: Getter and Setter methods provide controlled access to private fields
    // These methods can include validation logic and maintain data integrity
    
    /**
     * ENCAPSULATION: Getter for userId - provides read access to private field
     */
    public String getUserId() { return userId; }
    
    /**
     * ENCAPSULATION: Setter for userId - provides controlled write access
     * Can add validation logic here (e.g., format checking)
     */
    public void setUserId(String userId) { this.userId = userId; }
    
    /**
     * ENCAPSULATION: Getter for name
     */
    public String getName() { return name; }
    
    /**
     * ENCAPSULATION: Setter for name with potential validation
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * ENCAPSULATION: Getter for email
     */
    public String getEmail() { return email; }
    
    /**
     * ENCAPSULATION: Setter for email - could add email format validation
     */
    public void setEmail(String email) { this.email = email; }
    
    /**
     * ENCAPSULATION: Getter for password (should return hashed version in production)
     */
    public String getPassword() { return password; }
    
    /**
     * ENCAPSULATION: Setter for password - should hash password before storing
     */
    public void setPassword(String password) { this.password = password; }
    
    /**
     * ENCAPSULATION: Getter for address
     */
    public String getAddress() { return address; }
    
    /**
     * ENCAPSULATION: Setter for address
     */
    public void setAddress(String address) { this.address = address; }
    
    /**
     * ENCAPSULATION: Getter for phoneNumber
     */
    public long getPhoneNumber() { return phoneNumber; }
    
    /**
     * ENCAPSULATION: Setter for phoneNumber - could add phone format validation
     */
    public void setPhoneNumber(long phoneNumber) { this.phoneNumber = phoneNumber; }
    
    /**
     * ABSTRACTION: Abstract method that must be implemented by all child classes
     * POLYMORPHISM: Each child class will provide its own implementation
     * 
     * This creates a contract - all User subclasses must define their user type
     * Enables polymorphic behavior where same method call works for different user types
     * 
     * @return String representing the type of user (CUSTOMER, VENDOR, ADMIN)
     */
    public abstract String getUserType();
}