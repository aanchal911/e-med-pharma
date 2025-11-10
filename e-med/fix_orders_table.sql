-- Fix orders table by adding missing columns
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Pending';
ALTER TABLE orders ADD COLUMN IF NOT EXISTS order_date DATE DEFAULT (CURRENT_DATE);

-- Update existing orders
UPDATE orders SET status = 'Pending' WHERE status IS NULL;
UPDATE orders SET order_date = CURRENT_DATE WHERE order_date IS NULL;

-- View updated table structure
DESCRIBE orders;

-- View sample orders
SELECT * FROM orders LIMIT 5;