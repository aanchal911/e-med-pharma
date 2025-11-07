package com.emedpharma.service;

import com.emedpharma.dao.OrderDAO;
import com.emedpharma.model.Order;
import java.util.List;

// Implementation of OrderService interface
public class OrderServiceImpl implements OrderService {
    
    private OrderDAO orderDAO;
    
    public OrderServiceImpl() {
        this.orderDAO = new OrderDAO();
    }
    
    @Override
    public boolean placeOrder(Order order) {
        try {
            // Business logic for placing order
            if (order.getQuantity() <= 0) {
                System.out.println("Invalid quantity");
                return false;
            }
            return orderDAO.insertOrder(order);
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        try {
            return orderDAO.updateOrderStatus(orderId, status);
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Order getOrderById(int orderId) {
        try {
            return orderDAO.getOrderById(orderId);
        } catch (Exception e) {
            System.out.println("Error fetching order: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        try {
            return orderDAO.getOrdersByCustomer(customerId);
        } catch (Exception e) {
            System.out.println("Error fetching customer orders: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Order> getOrdersByVendor(String vendorId) {
        try {
            return orderDAO.getOrdersByVendor(vendorId);
        } catch (Exception e) {
            System.out.println("Error fetching vendor orders: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean cancelOrder(int orderId) {
        try {
            return orderDAO.updateOrderStatus(orderId, "CANCELLED");
        } catch (Exception e) {
            System.out.println("Error cancelling order: " + e.getMessage());
            return false;
        }
    }
    
    // Overloaded methods (Polymorphism)
    @Override
    public List<Order> getOrders() {
        try {
            return orderDAO.getAllOrders();
        } catch (Exception e) {
            System.out.println("Error fetching all orders: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Order> getOrders(String status) {
        try {
            return orderDAO.getOrdersByStatus(status);
        } catch (Exception e) {
            System.out.println("Error fetching orders by status: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Order> getOrders(String customerId, String status) {
        try {
            return orderDAO.getOrdersByCustomerAndStatus(customerId, status);
        } catch (Exception e) {
            System.out.println("Error fetching orders by customer and status: " + e.getMessage());
            return null;
        }
    }
}