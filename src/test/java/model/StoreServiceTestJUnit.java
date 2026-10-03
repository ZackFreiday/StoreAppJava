package model;

import model.exceptions.InsufficientQuantityException;
import model.service.StoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreServiceTestJUnit {

    private Store store;
    private StoreService storeService;
    private Cashier cashier;
    private Customer customer;
    private Goods bread;
    private Goods shampoo;

    @BeforeEach
    public void setup() {
        store = new Store("TestStore", 30.0, 50.0, 3, 20.0);
        storeService = new StoreService(store);

        cashier = new Cashier("C001", "Alice", 1500.0);
        customer = new Customer("John", 100.0);

        storeService.addCashier(cashier);

        bread = new FoodItem("F001", "Bread", 1.0, 10, LocalDate.now().plusDays(2));
        shampoo = new NonFoodItem("N001", "Shampoo", 2.0, 5, LocalDate.now().plusDays(10));

        storeService.loadGoods(bread);
        storeService.loadGoods(shampoo);
    }

    @Test
    public void testSuccessfulSale() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);
        cart.put("N001", 1);

        Receipt receipt = storeService.sellGoods(cart, customer, cashier);

        assertNotNull(receipt);
        assertEquals(1, storeService.getTotalReceiptsIssued());
        assertEquals(3, receipt.getItems().stream().mapToInt(ReceiptItem::getQuantity).sum());
    }

    @Test
    public void testInsufficientQuantityThrowsException() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 20); 

        Exception exception = assertThrows(InsufficientQuantityException.class, () -> {
            storeService.sellGoods(cart, customer, cashier);
        });

        assertTrue(exception.getMessage().contains("Insufficient quantity"));
    }

    @Test
    public void testSellingExpiredItemFails() {
        Goods expiredItem = new FoodItem("F002", "Old Bread", 1.0, 5, LocalDate.now().minusDays(1));
        storeService.loadGoods(expiredItem);

        Map<String, Integer> cart = new HashMap<>();
        cart.put("F002", 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.sellGoods(cart, customer, cashier);
        });

        assertTrue(exception.getMessage().contains("expired"));
    }

    @Test
    public void testCustomerBalanceTooLow() {
        Customer poorCustomer = new Customer("Poor", 0.5);
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.sellGoods(cart, poorCustomer, cashier);
        });

        assertTrue(exception.getMessage().contains("enough funds"));
    }

    @Test
    public void testProfitCalculation() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("F001", 2);
        cart.put("N001", 1);

        storeService.sellGoods(cart, customer, cashier);

        double turnover = storeService.getTotalTurnover();
        double profit = storeService.getProfit();

        assertTrue(turnover > 0);
        assertTrue(profit < turnover);
    }
}
