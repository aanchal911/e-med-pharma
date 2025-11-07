# e-MEDpharma - OOP Implementation Summary

## Core OOP Concepts Implemented

### 1. **Encapsulation**
- **User.java**: Private fields (userId, name, email, password) with public getter/setter methods
- **Medicine.java**: Private medicine details with controlled access through methods
- **Order.java**: Private order data with business logic methods
- **DatabaseConnection.java**: Encapsulates database connection logic

### 2. **Inheritance**
- **Base Class**: `User.java` (abstract class)
- **Derived Classes**: 
  - `Customer.java` extends User
  - `Vendor.java` extends User  
  - `Admin.java` extends User
- **Shared Properties**: userId, name, email, password, address, phoneNumber
- **Specialized Methods**: Each subclass has role-specific methods

### 3. **Polymorphism**
- **Method Overriding**: `getUserType()` method implemented differently in each User subclass
- **Method Overloading**: 
  - `OrderService.getOrders()` - multiple versions with different parameters
  - `MedicineService.getMedicines()` - overloaded for different search criteria
  - `ExpenseManager.addExpense()` - multiple ways to add expenses

### 4. **Abstraction**
- **Abstract Class**: `User.java` with abstract method `getUserType()`
- **Interfaces**: 
  - `OrderService.java` - defines contract for order operations
  - `MedicineService.java` - defines contract for medicine operations
- **Implementation Classes**: 
  - `OrderServiceImpl.java` implements OrderService

## Class Structure

```
com.emedpharma.model/
├── User.java (Abstract Base Class)
├── Customer.java (Inherits User)
├── Vendor.java (Inherits User)
├── Admin.java (Inherits User)
├── Medicine.java
├── Order.java
└── Expense.java

com.emedpharma.service/
├── OrderService.java (Interface)
├── OrderServiceImpl.java (Implementation)
├── MedicineService.java (Interface)
└── ExpenseManager.java

com.emedpharma.dao/
├── DatabaseConnection.java (Singleton Pattern)
└── OrderDAO.java

com.emedpharma.servlet/
└── LoginServlet.java
```

## Key Features Implemented

### For Customers:
- Browse and search medicines
- Upload prescriptions
- Place and track orders
- View order history

### For Vendors:
- Manage product inventory
- Verify prescriptions
- Update order status
- Track expenses and revenue

### For Admin:
- Monitor system operations
- Verify vendor registrations
- Generate reports
- Manage system integrity

## Database Integration (JDBC)
- **Connection Management**: Singleton pattern in DatabaseConnection.java
- **SQL Operations**: PreparedStatement for security (prevents SQL injection)
- **DAO Pattern**: Separates database logic from business logic
- **Triggers**: Automatic inventory updates and timestamp management

## Technology Stack
- **Frontend**: HTML, CSS, JavaScript, JSP
- **Backend**: Java (Servlets, JDBC, OOP)
- **Database**: MySQL with triggers and stored procedures
- **Server**: Apache Tomcat
- **Architecture**: MVC Pattern with DAO Layer

## Business Logic Examples

### Order Processing Flow:
1. Customer places order → `OrderService.placeOrder()`
2. System validates → `Order.java` business methods
3. Database updated → `OrderDAO.insertOrder()`
4. Inventory auto-updated → MySQL trigger
5. Vendor notified → Status tracking

### Prescription Verification:
1. Customer uploads prescription
2. Vendor verifies for scheduled drugs
3. System ensures compliance
4. Order processed only after verification

This implementation demonstrates a complete understanding of OOP principles while solving real-world pharmacy management challenges.