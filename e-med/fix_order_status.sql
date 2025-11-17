-- Fix order status column in database
USE drugdatabase;

-- Add status column if it doesn't exist
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Pending';

-- Update existing orders to have 'Pending' status
UPDATE orders SET status = 'Pending' WHERE status IS NULL OR status = '';

-- Verify the change
SELECT oid, uid, pid, status, orderdatetime FROM orders LIMIT 5;