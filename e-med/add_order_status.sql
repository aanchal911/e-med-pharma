-- Add status column to orders table for order tracking
USE drugdatabase;

-- Add status column with default value 'Pending'
ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending';

-- Update existing orders to have 'Pending' status
UPDATE orders SET status = 'Pending' WHERE status IS NULL;

-- Show the updated table structure
DESCRIBE orders;