-- Test queries to verify order flow
USE drugdatabase;

-- Check orders for vendor01
SELECT 'Orders for vendor01:' as Info;
SELECT o.oid, o.uid, p.pname, o.quantity, o.price, o.status
FROM orders o JOIN product p ON o.pid = p.pid 
WHERE o.sid = 'vendor01' 
ORDER BY o.orderdatetime DESC;

-- Check all pending orders
SELECT 'All pending orders:' as Info;
SELECT o.oid, s.sname as vendor, o.uid as customer, p.pname as product, o.status
FROM orders o 
JOIN product p ON o.pid = p.pid 
JOIN seller s ON o.sid = s.sid
WHERE o.status = 'Pending';