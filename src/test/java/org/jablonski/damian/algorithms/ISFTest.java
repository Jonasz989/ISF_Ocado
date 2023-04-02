package org.jablonski.damian.algorithms;

import org.jablonski.damian.data.Order;
import org.jablonski.damian.data.Store;
import org.jablonski.damian.parsers.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ISFTest {

    File fileOrders = new File("self-test-data/isf-end-time/orders.json");
    File fileStore = new File("self-test-data/isf-end-time/store.json");
    private final ArrayList<Order> expectedOrders = new ArrayList<>();
    Store expectedStore = new Store();
    Parser parser = new Parser();


    @Before
    public void setUp() {

        expectedStore.setPickers(new LinkedList<>(){});
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
    public void testIfAllPickersHaveTheSameStartingTime() throws IOException {

        Store store = parser.readStore(fileStore.getAbsolutePath());

        ISF isf = new ISF();
        isf.start(new String[]{fileStore.getAbsolutePath(), fileOrders.getAbsolutePath()});
        assertEquals(expectedStore.getOurPickers().get("P1"), store.getOurPickers().get("P1"));
        assertEquals(expectedStore.getOurPickers().get("P1"), store.getOurPickers().get("P2"));
        assertEquals(expectedStore.getOurPickers().get("P2"), store.getOurPickers().get("P1"));
    }

    @Test
    public void isResultOkay() {
        ISF isf = new ISF();
        isf.start(new String[]{fileStore.getAbsolutePath(), fileOrders.getAbsolutePath()});
        assertTrue(isf.biggestList.get(0).equals("P1 order-1 09:00") || isf.biggestList.get(0).equals("P1 order-2 09:00"));
    }
}
