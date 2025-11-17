# e-MEDpharma Database Setup Instructions

## Step 1: Update Database Credentials

**File:** `src/com/emedpharma/dao/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/drugdatabase";
private static final String USERNAME = "YOUR_MYSQL_USERNAME";  // Change this
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";  // Change this
```

## Step 2: Common MySQL Setups

### XAMPP Users:
```java
private static final String USERNAME = "root";
private static final String PASSWORD = "";  // Empty password
```

### MySQL Workbench Users:
```java
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password_here";
```

### Custom MySQL Installation:
```java
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

## Step 3: Create Database

Run this in MySQL:
```sql
SOURCE c:\Users\nares\OneDrive\Desktop\e-med pharma\drugdatabase.sql
```

Or manually:
```sql
CREATE SCHEMA drugdatabase;
USE drugdatabase;
-- Then run all the CREATE TABLE statements from drugdatabase.sql
```

## Step 4: Test Connection

1. Start MySQL server (XAMPP/MySQL Workbench)
2. Run your project
3. Try to login/register

## Common Issues:

**Error: "Access denied"**
- Wrong username/password in DatabaseConnection.java

**Error: "Unknown database"**
- Database not created, run the SQL file

**Error: "Connection refused"**
- MySQL server not running

**Error: "Driver not found"**
- MySQL connector JAR missing in WEB-INF/lib