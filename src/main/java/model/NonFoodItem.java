package model;

import java.time.LocalDate;

public class NonFoodItem extends Goods {
    public NonFoodItem(String id, String name, double unitDeliveryPrice, int quantity, LocalDate expirationDate) {
        super(id, name, unitDeliveryPrice, quantity, expirationDate);
    }

    @Override
    public double calculateSellingPrice(LocalDate today, double markupPercentage, int expirationThresholdDays, double discountPercentage) {
        double basePrice = unitDeliveryPrice * (1 + markupPercentage / 100.0);
        if (isNearExpiry(today, expirationThresholdDays)) {
            basePrice *= (1 - discountPercentage / 100.0);
        }
        return basePrice;
    }
}

