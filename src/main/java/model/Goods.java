package model;

import java.time.LocalDate;
import java.io.Serializable;

public abstract class Goods implements Serializable{
    protected String id;
    protected String name;
    protected double unitDeliveryPrice;
    protected int quantity;
    protected LocalDate expirationDate;

public Goods(String id, String name, double unitDeliveryPrice, int quantity, LocalDate expirationDate) {
    if (unitDeliveryPrice < 0) {
        throw new IllegalArgumentException("Delivery price must be non-negative.");
    }
    if (quantity < 0) {
        throw new IllegalArgumentException("Quantity must be non-negative.");
    }
    this.id = id;
    this.name = name;
    this.unitDeliveryPrice = unitDeliveryPrice;
    this.quantity = quantity;
    this.expirationDate = expirationDate;
}


    public abstract double calculateSellingPrice(LocalDate today, double markupPercentage, int expirationThresholdDays, double discountPercentage);

    public boolean isExpired(LocalDate today) {
        return today.isAfter(expirationDate);
    }

    public boolean isNearExpiry(LocalDate today, int thresholdDays) {
        return !isExpired(today) && expirationDate.minusDays(thresholdDays).isBefore(today);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getUnitDeliveryPrice() { return unitDeliveryPrice; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }

    public void reduceQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity below zero!");
        }
        quantity -= amount;
    }

    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", Qty: " + quantity + ", Expires: " + expirationDate + ")";
    }
}