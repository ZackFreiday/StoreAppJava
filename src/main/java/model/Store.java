package model;

import java.time.LocalDate;
import java.util.*;

public class Store {
    private String name;
    private List<Goods> inventory = new ArrayList<>();
    private List<Cashier> cashiers = new ArrayList<>();
    private List<Receipt> receipts = new ArrayList<>();

    private double foodMarkup;
    private double nonFoodMarkup;
    private int expirationThresholdDays;
    private double nearExpiryDiscount;

    private double totalDeliveryCost = 0.0; // FIXED: Track real delivery cost

    public Store(String name, double foodMarkup, double nonFoodMarkup, int expirationThresholdDays, double nearExpiryDiscount) {
        this.name = name;
        this.foodMarkup = foodMarkup;
        this.nonFoodMarkup = nonFoodMarkup;
        this.expirationThresholdDays = expirationThresholdDays;
        this.nearExpiryDiscount = nearExpiryDiscount;
    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
    }

    public void loadGoods(Goods item) {
        // FIXED: Prevent duplicates by ID
        Goods existing = findItemById(item.getId());
        if (existing != null) {
            throw new IllegalArgumentException("Product with ID '" + item.getId() + "' already exists.");
        }

        inventory.add(item);
        totalDeliveryCost += item.getUnitDeliveryPrice() * item.getQuantity(); // FIXED: Track on arrival
    }

    public Receipt sellGoods(Map<String, Integer> purchaseMap, Customer customer, Cashier cashier) {
        LocalDate today = LocalDate.now();
        List<ReceiptItem> soldItems = new ArrayList<>();
        double totalCost = 0.0;

        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productId = entry.getKey();
            int quantityRequested = entry.getValue();
            Goods item = findItemById(productId);

            if (item == null || item.isExpired(today)) {
                throw new IllegalArgumentException("Product unavailable or expired: " + productId);
            }
            if (item.getQuantity() < quantityRequested) {
                throw new InsufficientQuantityException(item.getName(), quantityRequested, item.getQuantity());
            }

            double markup = (item instanceof FoodItem) ? foodMarkup : nonFoodMarkup;
            double unitPrice = item.calculateSellingPrice(today, markup, expirationThresholdDays, nearExpiryDiscount);
            totalCost += unitPrice * quantityRequested;
            soldItems.add(new ReceiptItem(item, quantityRequested, unitPrice));
        }

        if (customer.getBalance() < totalCost) {
            throw new IllegalArgumentException("Customer doesn't have enough funds. Required: " + totalCost);
        }

        // Reduce stock and charge customer
        for (ReceiptItem line : soldItems) {
            line.getProduct().reduceQuantity(line.getQuantity());
        }
        customer.decreaseBalance(totalCost);

        // Create and track receipt
        Receipt receipt = new Receipt(cashier, soldItems);
        receipt.printToConsole();
        receipt.saveToTextFile();
        receipt.saveSerialized();
        receipts.add(receipt);
        return receipt;
    }

    private Goods findItemById(String id) {
        for (Goods item : inventory) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }

    public double getTotalTurnover() {
        return receipts.stream().mapToDouble(Receipt::getTotalAmount).sum();
    }

    public double getDeliveryCost() {
        return totalDeliveryCost; // FIXED: Return tracked cost
    }

    public double getSalaryExpenses() {
        return cashiers.stream().mapToDouble(Cashier::getMonthlySalary).sum();
    }

    public double getProfit() {
        return getTotalTurnover() - getDeliveryCost() - getSalaryExpenses();
    }

    public int getTotalReceiptsIssued() {
        return receipts.size();
    }
}