package model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Receipt implements Serializable {
    private static int receiptCounter = 0;
    private static final String COUNTER_FILE = "receipt_counter.txt";

    static {
        // Load counter from file at class load time
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                receiptCounter = Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            receiptCounter = 0; // Start fresh if file missing or unreadable
        }
    }

    private static void saveCounter() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(String.valueOf(receiptCounter));
        } catch (IOException e) {
            System.err.println("Failed to persist receipt counter: " + e.getMessage());
        }
    }

    private final int serialNumber;
    private final Cashier cashier;
    private final LocalDateTime timestamp;
    private final List<ReceiptItem> items;
    private final double totalAmount;

    public Receipt(Cashier cashier, List<ReceiptItem> items) {
        this.serialNumber = ++receiptCounter;
        saveCounter(); // FIXED: Save after incrementing
        this.cashier = cashier;
        this.timestamp = LocalDateTime.now();
        this.items = new ArrayList<>(items);
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(ReceiptItem::getTotalPrice).sum();
    }

    public int getSerialNumber() { return serialNumber; }
    public Cashier getCashier() { return cashier; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<ReceiptItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }

    public void printToConsole() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt #").append(serialNumber).append("\n")
          .append("Cashier: ").append(cashier.getName()).append(" (ID: ").append(cashier.getId()).append(")\n")
          .append("Issued: ").append(timestamp).append("\n\n");

        for (ReceiptItem item : items) {
            sb.append(item).append("\n");
        }

        sb.append("\nTotal: ").append(String.format("%.2f", totalAmount));
        return sb.toString();
    }

}