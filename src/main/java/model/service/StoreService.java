package model.service;

import model.*;
import model.exceptions.InsufficientQuantityException;
import model.util.ReceiptUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreService {
    private final Store store;


    public StoreService(Store store) {
        this.store = store;
    }

    public void addCashier(Cashier cashier) {
        store.getCashiers().add(cashier);
    }

    public void loadGoods(Goods item) {
        Goods existing = findItemById(item.getId());
        if (existing != null) {
            throw new IllegalArgumentException("Product with ID '" + item.getId() + "' already exists.");
        }

        store.getInventory().add(item);
        increaseDeliveryCost(item.getUnitDeliveryPrice() * item.getQuantity());
    }

    public Receipt sellGoods(Map<String, Integer> purchaseMap, Customer customer, Cashier cashier) {
        LocalDate today = LocalDate.now();
        List<ReceiptItem> soldItems = new ArrayList<>();
        double totalCost = 0.0;

        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productId = entry.getKey();
            int quantityRequested = entry.getValue();
            Goods item = findItemById(productId);

            if (quantityRequested <= 0) {
                throw new IllegalArgumentException("Requested quantity must be positive.");
            }

            if (item == null || item.isExpired(today)) {
                throw new IllegalArgumentException("Product unavailable or expired: " + productId);
            }

            if (item.getQuantity() < quantityRequested) {
               throw new InsufficientQuantityException(item.getName(), quantityRequested, item.getQuantity());
            }

            double markup = (item instanceof FoodItem) ? store.getFoodMarkup() : store.getNonFoodMarkup();
            double unitPrice = item.calculateSellingPrice(today, markup, store.getExpirationThresholdDays(), store.getNearExpiryDiscount());

            soldItems.add(new ReceiptItem(item, quantityRequested, unitPrice));
            totalCost += unitPrice * quantityRequested;
        }

        if (customer.getBalance() < totalCost) {
            throw new IllegalArgumentException("Customer doesn't have enough funds. Required: " + totalCost);
        }

        // Apply sale
        for (ReceiptItem line : soldItems) {
            line.getProduct().reduceQuantity(line.getQuantity());
        }
        customer.decreaseBalance(totalCost);

        // Create and record receipt
        Receipt receipt = new Receipt(cashier, soldItems);
        ReceiptUtils.saveToTextFile(receipt);
        ReceiptUtils.saveSerialized(receipt);

        store.getReceipts().add(receipt);
        return receipt;
    }

    public double getTotalTurnover() {
        return store.getReceipts().stream()
                .mapToDouble(Receipt::getTotalAmount)
                .sum();
    }

    public double getSalaryExpenses() {
        return store.getCashiers().stream()
                .mapToDouble(Cashier::getMonthlySalary)
                .sum();
    }

    public double getTotalDeliveryCost() {
    return store.getTotalDeliveryCost();
    }

    public double getProfit() {
        return getTotalTurnover() - store.getTotalDeliveryCost() - getSalaryExpenses();
    }

    public void increaseDeliveryCost(double cost) {
    if (cost < 0) throw new IllegalArgumentException("Cost must be non-negative");
    double newTotal = store.getTotalDeliveryCost() + cost;
    store.setTotalDeliveryCost(newTotal);
    }


    public int getTotalReceiptsIssued() {
        return store.getReceipts().size();
    }

    private Goods findItemById(String id) {
        return store.getInventory().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
