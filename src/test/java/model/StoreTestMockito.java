package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreTestMockito {

    private Store store;
    private Cashier mockedCashier;
    private Customer customer;
    private Goods bread;

    @BeforeEach
    public void setup() {
        store = new Store("TestStore", 30.0, 50.0, 3, 20.0);

        // Mocked cashier setup
        mockedCashier = mock(Cashier.class);
        when(mockedCashier.getId()).thenReturn("C999");
        when(mockedCashier.getName()).thenReturn("Mocky");
        when(mockedCashier.getMonthlySalary()).thenReturn(1500.0);

        store.addCashier(mockedCashier); 

        customer = new Customer ("John", 100.0);
        bread = new FoodItem("F001", "Bread", 1.0, 10, LocalDate.now().plusDays(5));
        store.loadGoods(bread);
    }

    @Test
    public void testCashierInjectedIntoReceipt() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);

        Receipt receipt = store.sellGoods(cart, customer, mockedCashier);

        assertEquals("Mocky", receipt.getCashier().getName()); 
        assertEquals("C999", receipt.getCashier().getId());    
        assertEquals(1, store.getTotalReceiptsIssued());
    }

    @Test
    public void testCashierSalaryIsUsedInProfitCalc() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);

        store.sellGoods(cart, customer, mockedCashier);

        double salaryExpenses = store.getSalaryExpenses();
        assertEquals(1500.0, salaryExpenses); 
    }
}

