-- Check database structure and data
USE drugdatabase;

-- Show all tables
SHOW TABLES;

-- Check orders table structure
DESCRIBE orders;

-- Check if status column exists
SHOW COLUMNS FROM orders LIKE 'status';

-- Check all orders
SELECT * FROM orders;

-- Check orders with vendor assignment
SELECT o.oid, o.uid, o.pid, o.sid, o.quantity, o.price, o.orderdatetime, 
       COALESCE(o.status, 'Pending') as status,
       p.pname, s.sname
FROM orders o 
LEFT JOIN product p ON o.pid = p.pid 
LEFT JOIN seller s ON o.sid = s.sid 
ORDER BY o.orderdatetime DESC;

-- Check vendor inventory
SELECT i.sid, i.pid, i.pname, i.quantity, s.sname
FROM inventory i 
LEFT JOIN seller s ON i.sid = s.sid
ORDER BY i.sid;

-- Check if vendors exist
SELECT * FROM seller;

-- Check if products exist
SELECT * FROM product LIMIT 10;

-- Check customer orders by vendor
SELECT o.sid, s.sname, COUNT(*) as order_count
FROM orders o 
LEFT JOIN seller s ON o.sid = s.sid
GROUP BY o.sid, s.sname;