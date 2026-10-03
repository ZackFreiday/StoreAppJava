package StoreApp;

import model.*;
import model.exceptions.InsufficientQuantityException;
import model.service.StoreService;
import model.util.ReceiptUtils;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Store store = new Store("SuperShop", 30.0, 50.0, 3, 20.0);
        StoreService storeService = new StoreService(store);

        // --- Add multiple cashiers
        Cashier cashier1 = new Cashier("C001", "Alice", 1500.0);
        Cashier cashier2 = new Cashier("C002", "Bob", 1400.0);
        storeService.addCashier(cashier1);
        storeService.addCashier(cashier2);

        // --- Add customers
        Customer john = new Customer("John", 50.0);
        Customer emily = new Customer("Emily", 100.0);

        // --- Load goods
        Goods bread = new FoodItem("F001", "Bread", 1.0, 30, LocalDate.now().plusDays(2));
        Goods shampoo = new NonFoodItem("N001", "Shampoo", 2.0, 20, LocalDate.now().plusDays(15));
        Goods milk = new FoodItem("F002", "Milk", 1.5, 25, LocalDate.now().plusDays(1));
        storeService.loadGoods(bread);
        storeService.loadGoods(shampoo);
        storeService.loadGoods(milk);

        // --- First transaction (John)
        Map<String, Integer> cart1 = new HashMap<>();
        cart1.put("F001", 2); // Bread
        cart1.put("N001", 1); // Shampoo

        try {
            Receipt r1 = storeService.sellGoods(cart1, john, cashier1);
            System.out.println("\n--- Receipt 1 ---");
            r1.printToConsole(); //Original receipt
        } catch (Exception e) {
            System.err.println("Transaction 1 failed: " + e.getMessage());
        }

        // --- Second transaction (Emily)
        Map<String, Integer> cart2 = new HashMap<>();
        cart2.put("F002", 3); // Milk

        try {
            Receipt r2 = storeService.sellGoods(cart2, emily, cashier2);
            System.out.println("\n--- Receipt 2 ---");
            r2.printToConsole(); //Original receipt
        } catch (Exception e) {
            System.err.println("Transaction 2 failed: " + e.getMessage());
        }

        // --- Attempt to load receipt from file (deserialization)
        System.out.println("\n--- Load Receipt from File ---");
        Receipt loadedReceipt = ReceiptUtils.loadSerialized(1); // Try loading receipt #1
        if (loadedReceipt != null) {
            loadedReceipt.printToConsole(); //Loaded receipt
        }

        // --- Show final store stats
        System.out.println("\n--- Final Store Statistics ---");
        System.out.println("Receipts issued: " + storeService.getTotalReceiptsIssued());
        System.out.printf("Turnover: %.2f%n", storeService.getTotalTurnover());
        System.out.printf("Delivery cost: %.2f%n", storeService.getTotalDeliveryCost());
        System.out.printf("Salaries: %.2f%n", storeService.getSalaryExpenses());
        System.out.printf("Profit: %.2f%n", storeService.getProfit());
    }
}
