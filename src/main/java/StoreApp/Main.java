package StoreApp;

import model.*;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Step 1: Create store with pricing rules
        Store store = new Store("SuperShop", 30.0, 50.0, 3, 20.0);
        
        // Step 2: Add a cashier
        Cashier cashier = new Cashier("C001", "Alice", 1500.0);
        store.addCashier(cashier);
        
        // Step 3: Load goods
        Goods bread = new FoodItem("F001", "Bread", 1.0, 100, LocalDate.now().plusDays(2));
        Goods shampoo = new NonFoodItem("N001", "Shampoo", 2.0, 50, LocalDate.now().plusDays(10));
        store.loadGoods(bread);
        store.loadGoods(shampoo);
        
        // Step 4: Create customer
        Customer customer = new Customer("John", 20.0);

        // Step 5: Customer wants to buy 2 bread and 1 shampoo
        Map<String, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put("F001", 2);
        shoppingCart.put("N001", 1);
        
        try {
            Receipt receipt = store.sellGoods(shoppingCart, customer, cashier);
            System.out.println("\n--- Store Stats ---");
            System.out.println("Receipts issued: " + store.getTotalReceiptsIssued());
            System.out.printf("Turnover: %.2f%n", store.getTotalTurnover());
            System.out.printf("Delivery cost: %.2f%n", store.getDeliveryCost());
            System.out.printf("Salaries: %.2f%n", store.getSalaryExpenses());
            System.out.printf("Profit: %.2f%n", store.getProfit());
        } catch (InsufficientQuantityException | IllegalArgumentException e) {
            System.err.println("Sale failed: " + e.getMessage());
        }
    }
}