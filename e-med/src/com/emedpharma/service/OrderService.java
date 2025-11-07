package com.emedpharma.service;

import com.emedpharma.model.Order;
import java.util.List;

// Interface for Abstraction
public interface OrderService {
    
    // Abstract methods for order operations
    boolean placeOrder(Order order);
    boolean updateOrderStatus(int orderId, String status);
    Order getOrderById(int orderId);
    List<Order> getOrdersByCustomer(String customerId);
    List<Order> getOrdersByVendor(String vendorId);
    boolean cancelOrder(int orderId);
    
    // Overloaded methods for Polymorphism
    List<Order> getOrders(); // Get all orders
    List<Order> getOrders(String status); // Get orders by status
    List<Order> getOrders(String customerId, String status); // Get orders by customer and status
}