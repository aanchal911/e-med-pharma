-- Admin Setup for e-MEDpharma System
-- Copy and paste these commands in MySQL command line

USE drugdatabase;

-- Create admin table
CREATE TABLE admin (
    aid VARCHAR(20) NOT NULL PRIMARY KEY,
    aname VARCHAR(50) NOT NULL,
    pass VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Insert default admin accounts
INSERT INTO admin (aid, aname, pass, email, role) VALUES 
('admin01', 'System Administrator', 'admin123', 'admin@emedpharma.com', 'SUPER_ADMIN'),
('admin02', 'Support Admin', 'support123', 'support@emedpharma.com', 'ADMIN');

-- Verify admin table creation
SELECT * FROM admin;

-- Show all tables to confirm
SHOW TABLES;