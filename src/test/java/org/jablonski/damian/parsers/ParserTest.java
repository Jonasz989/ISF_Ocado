package org.jablonski.damian.parsers;

import org.jablonski.damian.data.Order;
import org.jablonski.damian.data.Store;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    File fileOrders = new File("self-test-data/isf-end-time/orders.json");
    File fileStore = new File("self-test-data/isf-end-time/store.json");
    private final ArrayList<Order> expectedOrders = new ArrayList<>();
    Store expectedStore = new Store();
    Parser parser = new Parser();

    @Before
    public void setUp() throws IOException {

        Store store = parser.readStore(fileStore.getAbsolutePath());

        expectedStore.setPickers(new LinkedList<String>() {
        });
        expectedStore.getPickers().add("P1");
        expectedStore.setPickingStartTime(LocalTime.of(9, 0));
        expectedStore.setPickingEndTime(LocalTime.of(9, 30));


        // Creating test file with orders
        Order order1 = new Order();
        order1.setOrderId("order-1");
        order1.setOrderValue(BigDecimal.valueOf(0.00));
        order1.setPickingTime(Duration.ofMinutes(20));
        order1.setCompleteBy(LocalTime.of(9, 30));
        Order order2 = new Order();
        order2.setOrderId("order-2");
        order2.setOrderValue(BigDecimal.valueOf(0.00));
        order2.setPickingTime(Duration.ofMinutes(20));
        order2.setCompleteBy(LocalTime.of(9, 30));

        expectedOrders.add(order1);
        expectedOrders.add(order2);
    }

    @Test
    public void testReadOrderFromFile() throws IOException {
        ArrayList<Order> orders = parser.readOrder(fileOrders.getAbsolutePath());
        assertEquals(expectedOrders.get(0).getOrderId(), orders.get(0).getOrderId());
        assertEquals(expectedOrders.get(1).getOrderId(), orders.get(1).getOrderId());
        assertEquals(expectedOrders.get(0).getPickingTime(), orders.get(0).getPickingTime());
    }

    @Test
    public void testReadStoreFromFile() throws IOException {
        Store store = parser.readStore(fileStore.getAbsolutePath());
        assertEquals(expectedStore.getPickers().get(0), store.getPickers().get(0));
        assertEquals(expectedStore.getPickingStartTime(), store.getPickingStartTime());
        assertEquals(expectedStore.getPickingEndTime(), store.getPickingEndTime());
    }

    @Test
    public void testReadOrderFromEmptyFile() throws IOException {
        // Creating empty file
        String TEST_FILE = "self-test-data/advanced-allocation/orders2.json";
        File emptyFile = new File(TEST_FILE);
        emptyFile.createNewFile();
        ArrayList<Order> orders2 = parser.readOrder(emptyFile.getAbsolutePath());
        assertThrows(NullPointerException.class, () -> orders2.isEmpty());
    }
}
