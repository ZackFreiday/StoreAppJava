package model;

import java.io.Serializable;
import java.util.Objects;

public class Cashier implements Serializable {
    private String id;
    private String name;
    private double monthlySalary;

    public Cashier(String id, String name, double monthlySalary) {
        this.id = id;
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", monthlySalary=" + monthlySalary +
                '}';
    }

        // Used to compare Cashiers by ID only (IDs are unique)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cashier)) return false;
        Cashier cashier = (Cashier) o;
        return id.equals(cashier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
