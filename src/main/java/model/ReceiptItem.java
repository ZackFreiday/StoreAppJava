package model;

import java.io.Serializable;

public class ReceiptItem implements Serializable {
    private Goods product;
    private int quantity;
    private double unitPrice;

    public ReceiptItem(Goods product, int quantity, double unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Goods getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity +
               " @ " + String.format("%.2f", unitPrice) +
               " = " + String.format("%.2f", getTotalPrice());
    }
}
