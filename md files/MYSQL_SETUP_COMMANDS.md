# MySQL Setup Commands for Admin Dashboard

## Copy and paste these commands in MySQL Command Line:

```sql
-- Connect to MySQL and use the database
USE drugdatabase;

-- Create admin table
CREATE TABLE IF NOT EXISTS admin (
    aid VARCHAR(20) NOT NULL PRIMARY KEY,
    aname VARCHAR(50) NOT NULL,
    pass VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Insert admin accounts
INSERT INTO admin (aid, aname, pass, email, role) VALUES 
('admin01', 'System Administrator', 'admin123', 'admin@emedpharma.com', 'SUPER_ADMIN'),
('admin02', 'Support Admin', 'support123', 'support@emedpharma.com', 'ADMIN');

-- Add status column to seller table for suspension feature
ALTER TABLE seller ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Add status column to customer table for suspension feature  
ALTER TABLE customer ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Verify admin table
SELECT * FROM admin;

-- Show all tables
SHOW TABLES;

-- Check table structures
DESCRIBE admin;
DESCRIBE seller;
DESCRIBE customer;
```

## Quick Copy Commands:

### 1. Basic Setup:
```sql
USE drugdatabase;
CREATE TABLE IF NOT EXISTS admin (aid VARCHAR(20) PRIMARY KEY, aname VARCHAR(50), pass VARCHAR(20), email VARCHAR(100), role VARCHAR(20) DEFAULT 'ADMIN', created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
INSERT INTO admin VALUES ('admin01', 'System Administrator', 'admin123', 'admin@emedpharma.com', 'SUPER_ADMIN', NOW());
INSERT INTO admin VALUES ('admin02', 'Support Admin', 'support123', 'support@emedpharma.com', 'ADMIN', NOW());
```

### 2. Add Status Columns:
```sql
ALTER TABLE seller ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
ALTER TABLE customer ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
```

### 3. Verify Setup:
```sql
SELECT * FROM admin;
SELECT COUNT(*) as customer_count FROM customer;
SELECT COUNT(*) as vendor_count FROM seller;
SELECT COUNT(*) as product_count FROM product;
SELECT COUNT(*) as order_count FROM orders;
```