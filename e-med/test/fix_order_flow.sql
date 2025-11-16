-- Fix Order Flow - Add status column and ensure proper order management
USE drugdatabase;

-- Add status column to orders table if it doesn't exist
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Pending';

-- Update existing orders to have 'Pending' status if they don't have one
UPDATE orders SET status = 'Pending' WHERE status IS NULL OR status = '';

SELECT 'Order flow fix applied successfully!' as Status;