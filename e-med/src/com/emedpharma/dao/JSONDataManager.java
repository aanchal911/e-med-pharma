package com.emedpharma.dao;

import com.emedpharma.model.Customer;
import com.emedpharma.model.Order;
import java.io.*;
import java.util.*;

// Simple JSON-like file storage - 100% FREE
public class JSONDataManager {
    private static final String DATA_DIR = "data/";
    
    static {
        new File(DATA_DIR).mkdirs();
    }
    
    public static void saveCustomer(Customer customer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + "customers.txt", true))) {
            writer.println(customer.getUserId() + "|" + customer.getPassword() + "|" + 
                          customer.getFirstName() + "|" + customer.getLastName() + "|" + 
                          customer.getEmail() + "|" + customer.getAddress() + "|" + 
                          customer.getPhoneNumber());
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    public static Customer getCustomer(String userId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(userId)) {
                    Customer customer = new Customer();
                    customer.setUserId(parts[0]);
                    customer.setPassword(parts[1]);
                    customer.setFirstName(parts[2]);
                    customer.setLastName(parts[3]);
                    customer.setEmail(parts[4]);
                    customer.setAddress(parts[5]);
                    customer.setPhoneNumber(Long.parseLong(parts[6]));
                    return customer;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading customer: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean authenticateUser(String userId, String password) {
        Customer customer = getCustomer(userId);
        return customer != null && customer.getPassword().equals(password);
    }
}