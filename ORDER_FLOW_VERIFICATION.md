# 🔄 e-MEDpharma Order Flow Verification Guide

## 📋 Overview

This document explains how orders flow from customers to vendors in the e-MEDpharma system and provides verification steps to ensure everything works correctly.

## 🔍 Order Flow Process

### 1. **Customer Places Order**
```
Customer Dashboard → Browse Products → Add to Cart → Checkout → Place Order
```

**What happens:**
- Customer selects products from different vendors
- System groups items by vendor (each vendor gets separate orders)
- Orders are saved to `orders` table with:
  - `uid` = customer ID
  - `pid` = product ID  
  - `sid` = vendor ID (automatically assigned)
  - `quantity` = ordered quantity
  - `price` = product price
  - `status` = 'Pending' (default)

### 2. **Vendor Receives Order**
```
Vendor Dashboard → Order Management → View Pending Orders
```

**What happens:**
- Vendor logs into their dashboard
- System loads orders where `sid = currentVendor`
- Orders appear in "Order Management" section
- Vendor can approve/reject each order

### 3. **Order Status Updates**
```
Vendor Action → Database Update → Customer Notification
```

**What happens:**
- Vendor clicks "Approve" or "Reject"
- System updates `orders.status` field
- Customer can see updated status in "My Orders"

## 🧪 Verification Steps

### Step 1: Database Setup
```sql
-- Run this to ensure status column exists
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Pending';
UPDATE orders SET status = 'Pending' WHERE status IS NULL;
```

### Step 2: Test Customer Order Placement

1. **Login as Customer:**
   - Username: `aanchal01`
   - Password: `pass123`

2. **Place an Order:**
   - Go to "Browse Medicines"
   - Select a product (e.g., Paracetamol)
   - Click "View Details" → Select vendor → "Add to Cart"
   - Go to Cart → "Proceed to Checkout" → "Confirm & Place Order"

3. **Verify Order in Database:**
```sql
SELECT o.oid, o.uid, p.pname, o.quantity, o.price, o.status, s.sname
FROM orders o 
JOIN product p ON o.pid = p.pid 
JOIN seller s ON o.sid = s.sid
WHERE o.uid = 'aanchal01' 
ORDER BY o.orderdatetime DESC LIMIT 1;
```

### Step 3: Test Vendor Order Management

1. **Login as Vendor:**
   - Username: `vendor01` (Apollo Pharmacy)
   - Password: `vendor123`

2. **Check Orders:**
   - Go to "Order Management"
   - Verify the customer's order appears
   - Status should be "Pending"

3. **Approve/Reject Order:**
   - Click "Approve" or "Reject" button
   - Verify status updates in database

### Step 4: Test Customer Order Tracking

1. **Login as Customer Again:**
   - Go to "My Orders"
   - Verify order status has updated
   - Should show "Approved" or "Rejected"

## 🔧 Automated Verification

### Run the Verification Tool

1. **Navigate to project directory:**
```bash
cd e-med-pharma/e-med
```

2. **Run verification:**
```bash
verify_order_flow.bat
```

This will:
- ✅ Ensure database schema is correct
- ✅ Place a test order
- ✅ Verify vendor can see the order
- ✅ Test vendor approval process
- ✅ Verify customer can see status updates

## 🐛 Common Issues & Solutions

### Issue 1: Orders not appearing in vendor dashboard
**Cause:** Incorrect vendor ID assignment
**Solution:** Check `findVendorForProduct()` method in customer dashboard

### Issue 2: Status column missing
**Cause:** Original database schema doesn't include status
**Solution:** Run the fix script:
```sql
ALTER TABLE orders ADD COLUMN status VARCHAR(20) DEFAULT 'Pending';
```

### Issue 3: Orders showing but can't approve/reject
**Cause:** Database connection issues or SQL errors
**Solution:** Check vendor dashboard `updateOrderStatus()` method

## 📊 Database Schema Verification

### Check Orders Table Structure:
```sql
DESCRIBE orders;
```

Should show:
- `oid` (Primary Key, Auto Increment)
- `pid` (Product ID, Foreign Key)
- `sid` (Seller/Vendor ID, Foreign Key)  
- `uid` (Customer ID, Foreign Key)
- `orderdatetime` (Timestamp)
- `quantity` (Order quantity)
- `price` (Order price)
- `status` (Order status - should exist)

### Check Sample Data:
```sql
-- View all orders with details
SELECT 
    o.oid,
    c.fname as customer,
    p.pname as product,
    s.sname as vendor,
    o.quantity,
    o.price,
    o.status,
    o.orderdatetime
FROM orders o
JOIN customer c ON o.uid = c.uid
JOIN product p ON o.pid = p.pid
JOIN seller s ON o.sid = s.sid
ORDER BY o.orderdatetime DESC;
```

## ✅ Success Criteria

The order flow is working correctly when:

1. ✅ **Customer can place orders** - Orders appear in database with correct vendor assignment
2. ✅ **Vendor can see orders** - Orders appear in vendor dashboard filtered by vendor ID
3. ✅ **Vendor can approve/reject** - Status updates successfully in database
4. ✅ **Customer can track status** - Updated status visible in customer dashboard
5. ✅ **No duplicate orders** - Each cart checkout creates appropriate number of orders
6. ✅ **Proper vendor assignment** - Orders go to vendors who have the products in stock

## 🚀 Testing Checklist

- [ ] Database schema includes `status` column
- [ ] Sample data is loaded (customers, vendors, products, inventory)
- [ ] Customer can login and browse products
- [ ] Customer can add items to cart from different vendors
- [ ] Customer can complete checkout process
- [ ] Orders are created in database with correct vendor IDs
- [ ] Vendor can login and access order management
- [ ] Vendor sees only their orders (filtered by vendor ID)
- [ ] Vendor can approve/reject orders
- [ ] Order status updates in database
- [ ] Customer can see updated order status
- [ ] No errors in console/logs during the process

## 📞 Support

If you encounter any issues:

1. Check the console output for error messages
2. Verify database connection settings
3. Ensure all sample data is loaded
4. Run the automated verification tool
5. Check the database directly using SQL queries

---

**Note:** This verification ensures the complete order flow works as designed, from customer order placement to vendor approval and customer tracking.