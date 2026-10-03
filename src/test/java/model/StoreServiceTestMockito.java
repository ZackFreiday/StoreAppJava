package model;

import model.service.StoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreServiceTestMockito {

    private Store store;
    private StoreService storeService;
    private Cashier mockedCashier;
    private Customer customer;
    private Goods bread;

    @BeforeEach
    public void setup() {
        store = new Store("TestStore", 30.0, 50.0, 3, 20.0);
        storeService = new StoreService(store);

        mockedCashier = mock(Cashier.class);
        when(mockedCashier.getId()).thenReturn("C999");
        when(mockedCashier.getName()).thenReturn("Mocky");
        when(mockedCashier.getMonthlySalary()).thenReturn(1500.0);

        storeService.addCashier(mockedCashier);

        customer = new Customer("John", 100.0);
        bread = new FoodItem("F001", "Bread", 1.0, 10, LocalDate.now().plusDays(5));
        storeService.loadGoods(bread);
    }

    @Test
    public void testCashierInjectedIntoReceipt() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);

        Receipt receipt = storeService.sellGoods(cart, customer, mockedCashier);

        assertEquals("Mocky", receipt.getCashier().getName());
        assertEquals("C999", receipt.getCashier().getId());
        assertEquals(1, storeService.getTotalReceiptsIssued());
    }

    @Test
    public void testCashierSalaryIsUsedInProfitCalc() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);

        storeService.sellGoods(cart, customer, mockedCashier);

        double salaryExpenses = storeService.getSalaryExpenses();
        assertEquals(1500.0, salaryExpenses);
    }
}

