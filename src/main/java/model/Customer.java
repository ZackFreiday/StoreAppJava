package model;

public class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void decreaseBalance(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough balance!");
        }
        balance -= amount;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', balance=" + String.format("%.2f", balance) + "}";
    }
}
