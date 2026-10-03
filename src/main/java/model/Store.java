package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//POJO

public class Store {
    private String name;
    private List<Goods> inventory = new ArrayList<>();
    private Set<Cashier> cashiers = new HashSet<>();
    private List<Receipt> receipts = new ArrayList<>();

    private double foodMarkup;
    private double nonFoodMarkup;
    private int expirationThresholdDays;
    private double nearExpiryDiscount;

    private double totalDeliveryCost = 0.0;

public Store(String name, double foodMarkup, double nonFoodMarkup, int expirationThresholdDays, double nearExpiryDiscount) {
    this.name = name;
    this.foodMarkup = foodMarkup;
    this.nonFoodMarkup = nonFoodMarkup;
    this.expirationThresholdDays = expirationThresholdDays;
    this.nearExpiryDiscount = nearExpiryDiscount;
}


    // Getters
    public String getName() { return name; }
    public List<Goods> getInventory() { return inventory; }
    public Set<Cashier> getCashiers() { return cashiers; }
    public List<Receipt> getReceipts() { return receipts; }

    public double getFoodMarkup() { return foodMarkup; }
    public double getNonFoodMarkup() { return nonFoodMarkup; }
    public int getExpirationThresholdDays() { return expirationThresholdDays; }
    public double getNearExpiryDiscount() { return nearExpiryDiscount; }

    public double getTotalDeliveryCost() { return totalDeliveryCost; }

    public void setTotalDeliveryCost(double totalDeliveryCost) {
    this.totalDeliveryCost = totalDeliveryCost;
}

    @Override
    public String toString() {
        return "Store{" +
                "name='" + name + '\'' +
                ", foodMarkup=" + foodMarkup +
                ", nonFoodMarkup=" + nonFoodMarkup +
                ", expirationThresholdDays=" + expirationThresholdDays +
                ", nearExpiryDiscount=" + nearExpiryDiscount +
                '}';
    }
}
