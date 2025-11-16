-- Sample Data for e-MEDpharma Database
-- Run this after creating the database schema

USE drugdatabase;

-- Insert Sample Customers
INSERT INTO customer (uid, pass, fname, lname, email, address, phno) VALUES
('aanchal01', 'pass123', 'Aanchal', 'Bhatt', 'aanchal@email.com', '123 Health Street, Ahmedabad, Gujarat', 9876543210),
('shagun02', 'pass456', 'Shagun', 'Patel', 'shagun@email.com', '456 Medical Lane, Surat, Gujarat', 9876543211),
('dhara03', 'pass789', 'Dhara', 'Shah', 'dhara@email.com', '789 Wellness Road, Vadodara, Gujarat', 9876543212);

-- Insert Sample Vendors/Pharmacies
INSERT INTO seller (sid, sname, pass, address, phno) VALUES
('vendor01', 'Apollo Pharmacy', 'vendor123', 'Apollo Complex, CG Road, Ahmedabad, Gujarat', 7901234567),
('vendor02', 'MedPlus Health Services', 'vendor456', 'MedPlus Building, Ring Road, Surat, Gujarat', 7901234568),
('vendor03', 'HealthGuard Pharmacy', 'health123', 'HealthGuard Plaza, RC Dutt Road, Vadodara, Gujarat', 7901234569),
('vendor04', 'Guardian Pharmacy', 'guard456', 'Guardian Tower, Kalawad Road, Rajkot, Gujarat', 7901234570),
('vendor05', 'Netmeds Store', 'net789', 'Netmeds Center, Waghawadi Road, Bhavnagar, Gujarat', 7901234571),
('vendor06', '1mg Pharmacy', 'onemg321', '1mg Hub, Jamnagar Road, Jamnagar, Gujarat', 7901234572),
('vendor07', 'EasyMedico', 'easy654', 'EasyMedico Complex, ST Road, Junagadh, Gujarat', 7901234573);

-- Insert Sample Products
INSERT INTO product (pid, pname, manufacturer, mfg, exp, price) VALUES
('MED001', 'Paracetamol 500mg', 'Generic Pharma', '2024-01-01', '2025-12-31', 25),
('MED002', 'Crocin Advance', 'GSK', '2024-02-01', '2025-10-15', 35),
('MED003', 'Dolo 650', 'Micro Labs', '2024-01-15', '2025-11-20', 30),
('MED004', 'Aspirin 75mg', 'Bayer', '2024-03-01', '2025-09-30', 45),
('MED005', 'Cetirizine 10mg', 'Cipla', '2024-02-15', '2025-08-25', 20),
('MED006', 'Omeprazole 20mg', 'Dr Reddys', '2024-01-20', '2025-07-15', 55),
('MED007', 'Azithromycin 500mg', 'Zydus', '2024-03-10', '2025-06-30', 85),
('MED008', 'Metformin 500mg', 'Sun Pharma', '2024-02-20', '2025-12-15', 40),
('MED009', 'Amlodipine 5mg', 'Lupin', '2024-01-25', '2025-11-10', 35),
('MED010', 'Vitamin D3', 'Abbott', '2024-03-05', '2025-10-20', 65);

-- Insert Sample Inventory (distribute products across vendors)
-- Apollo Pharmacy (vendor01)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED001', 'Paracetamol 500mg', 250, 'vendor01'),
('MED002', 'Crocin Advance', 180, 'vendor01'),
('MED003', 'Dolo 650', 120, 'vendor01'),
('MED008', 'Metformin 500mg', 90, 'vendor01');

-- MedPlus Health Services (vendor02)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED001', 'Paracetamol 500mg', 200, 'vendor02'),
('MED004', 'Aspirin 75mg', 150, 'vendor02'),
('MED005', 'Cetirizine 10mg', 300, 'vendor02'),
('MED009', 'Amlodipine 5mg', 80, 'vendor02');

-- HealthGuard Pharmacy (vendor03)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED002', 'Crocin Advance', 160, 'vendor03'),
('MED006', 'Omeprazole 20mg', 100, 'vendor03'),
('MED007', 'Azithromycin 500mg', 75, 'vendor03'),
('MED010', 'Vitamin D3', 120, 'vendor03');

-- Guardian Pharmacy (vendor04)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED001', 'Paracetamol 500mg', 300, 'vendor04'),
('MED003', 'Dolo 650', 200, 'vendor04'),
('MED005', 'Cetirizine 10mg', 250, 'vendor04'),
('MED008', 'Metformin 500mg', 110, 'vendor04');

-- Netmeds Store (vendor05)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED004', 'Aspirin 75mg', 180, 'vendor05'),
('MED006', 'Omeprazole 20mg', 90, 'vendor05'),
('MED009', 'Amlodipine 5mg', 70, 'vendor05'),
('MED010', 'Vitamin D3', 140, 'vendor05');

-- 1mg Pharmacy (vendor06)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED002', 'Crocin Advance', 220, 'vendor06'),
('MED007', 'Azithromycin 500mg', 60, 'vendor06'),
('MED008', 'Metformin 500mg', 95, 'vendor06'),
('MED010', 'Vitamin D3', 100, 'vendor06');

-- EasyMedico (vendor07)
INSERT INTO inventory (pid, pname, quantity, sid) VALUES
('MED001', 'Paracetamol 500mg', 280, 'vendor07'),
('MED004', 'Aspirin 75mg', 130, 'vendor07'),
('MED005', 'Cetirizine 10mg', 200, 'vendor07'),
('MED006', 'Omeprazole 20mg', 85, 'vendor07');

-- Insert Sample Orders
INSERT INTO orders (pid, sid, uid, quantity, price) VALUES
('MED001', 'vendor01', 'aanchal01', 2, 50),
('MED002', 'vendor01', 'shagun02', 1, 35),
('MED003', 'vendor04', 'dhara03', 1, 30),
('MED005', 'vendor02', 'aanchal01', 3, 60),
('MED008', 'vendor01', 'shagun02', 2, 80);

-- Display summary
SELECT 'Database populated successfully!' as Status;
SELECT COUNT(*) as 'Total Customers' FROM customer;
SELECT COUNT(*) as 'Total Vendors' FROM seller;
SELECT COUNT(*) as 'Total Products' FROM product;
SELECT COUNT(*) as 'Total Inventory Items' FROM inventory;
SELECT COUNT(*) as 'Total Orders' FROM orders;