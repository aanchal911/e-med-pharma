package com.emedpharma.service;

import com.emedpharma.model.Expense;
import java.util.ArrayList;
import java.util.List;

// Encapsulates all financial calculations
public class ExpenseManager {
    
    private List<Expense> expenses;
    private double totalRevenue;
    private double totalExpenses;
    
    public ExpenseManager() {
        this.expenses = new ArrayList<>();
        this.totalRevenue = 0.0;
        this.totalExpenses = 0.0;
    }
    
    // Method overloading (Polymorphism)
    public void addExpense(Expense expense) {
        expenses.add(expense);
        totalExpenses += expense.getAmount();
    }
    
    public void addExpense(String vendorId, String type, double amount, String description) {
        Expense expense = new Expense(vendorId, type, amount, description);
        addExpense(expense);
    }
    
    public void addRevenue(double amount) {
        totalRevenue += amount;
    }
    
    public double calculateProfit() {
        return totalRevenue - totalExpenses;
    }
    
    public double getTotalExpenses() {
        return totalExpenses;
    }
    
    public double getTotalRevenue() {
        return totalRevenue;
    }
    
    public List<Expense> getExpensesByVendor(String vendorId) {
        List<Expense> vendorExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getVendorId().equals(vendorId)) {
                vendorExpenses.add(expense);
            }
        }
        return vendorExpenses;
    }
    
    public void generateMonthlyReport(String vendorId) {
        System.out.println("=== Monthly Report for Vendor: " + vendorId + " ===");
        System.out.println("Total Revenue: $" + totalRevenue);
        System.out.println("Total Expenses: $" + totalExpenses);
        System.out.println("Net Profit: $" + calculateProfit());
        System.out.println("=======================================");
    }
}