-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    category_id VARCHAR(10) PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);

-- Insert product categories
INSERT IGNORE INTO categories (category_id, category_name) VALUES 
('AID', 'First Aid'),
('BAB', 'Baby Care'),
('BOD', 'Body Care'),
('DEV', 'Medical Devices'),
('DIA', 'Diabetes Care'),
('DIP', 'Diapers'),
('FOO', 'Foot Care'),
('HAR', 'Hair Care'),
('HER', 'Herbal Products'),
('HYG', 'Hygiene'),
('INT', 'Intimate Care'),
('MED', 'Medicines'),
('MEN', 'Men\'s Care'),
('MOB', 'Mobility Aids'),
('ORA', 'Oral Care'),
('PAD', 'Sanitary Pads'),
('RES', 'Respiratory'),
('SKN', 'Skin Care'),
('SUP', 'Supplements'),
('WOM', 'Women\'s Care');

-- Add category column to product table
ALTER TABLE product ADD COLUMN IF NOT EXISTS category VARCHAR(10);

-- Update product categories based on product ID prefix
UPDATE product SET category = SUBSTRING(pid, 1, 3);

-- View categories
SELECT * FROM categories ORDER BY category_id;

-- View products with categories
SELECT pid, pname, category, price FROM product ORDER BY category, pid LIMIT 20;