package org.jablonski.damian.algorithms;

import org.jablonski.damian.data.Order;
import org.jablonski.damian.data.Store;
import org.jablonski.damian.parsers.Parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ISF {

    String storePath;
    String orderPath;
    Parser parse = new Parser();
    Store store;
    List<Order> ordersCount;
    List<Order> ordersValue = new ArrayList<>();
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //List for most orders
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    List<String> resultsForMostOptimal = new ArrayList<>();
    List<String> resultsForCompleteBy = new ArrayList<>();
    List<String> resultsForPickingTime = new ArrayList<>();
    List<String> resultsForPickingAndCompleteTime = new ArrayList<>();
    List<String> resultsForNoSorting = new ArrayList<>();
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Lists for most value
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    List<String> resultsForMostValueNoSort = new ArrayList<>();
    List<String> resultsForMostValueSorted = new ArrayList<>();
    List<String> resultsForMostValueValueDividedByPickingTime = new ArrayList<>();
    List<String> resultsForMostValueSortedByCompleteBy = new ArrayList<>();
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Values
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    BigDecimal currentBiggestValue = BigDecimal.ZERO;
    BigDecimal valueOfTheListNoSort = BigDecimal.ZERO;
    BigDecimal valueOfTheListSortedByValue = BigDecimal.ZERO;
    BigDecimal valueOfTheListOrderValueDividedByPickingTime = BigDecimal.ZERO;
    BigDecimal valueOfTheListSortedByCompleteBy = BigDecimal.ZERO;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Final lists
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    List<String> biggestList;
    List<String> mostValuableList;
    List<BigDecimal> listOfValues = new ArrayList<>();
    List<List<String>> listOfResultsMostValuable = new ArrayList<>();
    List<List<String>> listOfResultsMostOrders = new ArrayList<>();


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Whole process
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ISF() {
    }

    public void start(String[] args) {
        //setting paths from given arguments
        storePath = args[0];
        orderPath = args[1];

        //reading data from files
        readData(orderPath, storePath);

        ordersValue.addAll(ordersCount); //copying ordersCount to ordersValue

        System.out.println("MOST ORDERS\n");
        //algorithm for getting most orders
        algorithmForMostNumberOfOrders();

        System.out.println("\nBIGGEST VALUE\n");

        //algorithm for getting most value
        algorithmMostValue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //BIGGEST VALUE
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method which contains all the algorithms for getting the biggest value
     */
    private void algorithmMostValue() {

        noSortedValueResult(ordersValue);
        currentBiggestValue = valueOfTheListNoSort;
        mostValuableList = resultsForMostValueNoSort;

        sortedByValueValueResult(ordersValue);
        listOfResultsMostValuable.add(resultsForMostValueSorted);
        listOfValues.add(valueOfTheListSortedByValue);

        orderValueDividedByPickingTimeValueResult(ordersValue);
        listOfResultsMostValuable.add(resultsForMostValueValueDividedByPickingTime);
        listOfValues.add(valueOfTheListOrderValueDividedByPickingTime);

        completeByValueResult(ordersValue);
        listOfResultsMostValuable.add(resultsForMostValueSortedByCompleteBy);
        listOfValues.add(valueOfTheListSortedByCompleteBy);


        for (int i = 0; i < listOfResultsMostValuable.size(); i++) {
            if (listOfValues.get(i).compareTo(currentBiggestValue) > 0) {
                mostValuableList = listOfResultsMostValuable.get(i);
                currentBiggestValue = listOfValues.get(i);
            }
        }

        for (String result : mostValuableList) {
            System.out.println(result);
        }


    }

    /**
     * Method which assigns orders to pickers and calculates the value of the list, without sorting the list
     */
    private void noSortedValueResult(List<Order> orders) {

        //resetting pickers
        resetPickers();

        //assigning orders to pickers
        for (Order order : orders) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForMostValueNoSort.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
                valueOfTheListNoSort = valueOfTheListNoSort.add(order.getOrderValue());
            }
        }
    }

    /**
     * Method which assigns orders to pickers and calculates the value of the list, sorted by value
     */
    private void sortedByValueValueResult(List<Order> orders) {
        //sort orders by value from highest to lowest
        orders.sort((o1, o2) -> o2.getOrderValue().compareTo(o1.getOrderValue()));

        //resetting pickers
        resetPickers();

        //assigning orders to pickers
        for (Order order : orders) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForMostValueSorted.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
                valueOfTheListSortedByValue = valueOfTheListSortedByValue.add(order.getOrderValue());
            }
        }
    }

    /**
     * Method which assigns orders to pickers and calculates the value of the list, sorted by value divided by picking time
     */
    private void orderValueDividedByPickingTimeValueResult(List<Order> orders) {

        List<Order> zeroTime = orders.stream()
                .filter(o -> o.getPickingTime().toSeconds() == 0)
                .toList();

        List<Order> nonZeroTime = orders.stream()
                .filter(o -> o.getPickingTime().toSeconds() != 0)
                .sorted((o1, o2) -> {
                    BigDecimal value1 = o1.getOrderValue().divide(BigDecimal.valueOf(o1.getPickingTime().toSeconds()), RoundingMode.HALF_UP);
                    BigDecimal value2 = o2.getOrderValue().divide(BigDecimal.valueOf(o2.getPickingTime().toSeconds()), RoundingMode.HALF_UP);
                    return value2.compareTo(value1);
                })
                .collect(Collectors.toList());

        nonZeroTime.addAll(zeroTime);

        //resetting pickers
        resetPickers();

        //assigning orders to pickers
        for (Order order : nonZeroTime) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForMostValueValueDividedByPickingTime.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
                valueOfTheListOrderValueDividedByPickingTime = valueOfTheListOrderValueDividedByPickingTime.add(order.getOrderValue());
            }
        }
    }

    /**
     * Method which assigns orders to pickers and calculates the value of the list, sorted by complete by and value
     */
    private void completeByValueResult(List<Order> orders) {
        //sorted orders using complete by from lowest to highest and by order value from highest to lowest
        orders.sort((o1, o2) -> {
            if (o1.getCompleteBy().isBefore(o2.getCompleteBy())) {
                return -1;
            } else if (o1.getCompleteBy().isAfter(o2.getCompleteBy())) {
                return 1;
            } else {
                return o2.getOrderValue().compareTo(o1.getOrderValue());
            }
        });

        //resetting pickers
        resetPickers();

        //assigning orders to pickers
        for (Order order : orders) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForMostValueSortedByCompleteBy.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
                valueOfTheListSortedByCompleteBy = valueOfTheListSortedByCompleteBy.add(order.getOrderValue());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //MOST ORDERS
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Algorithm for getting most orders
     */
    public void algorithmForMostNumberOfOrders() {
        noSortedCountResult(ordersCount);
        mostOptimalCountResults(ordersCount);
        pickingTimeCountResult(ordersCount);
        completeByCountResult(ordersCount);
        completeByAndPickingTimeCountResult(ordersCount);

        biggestList = resultsForMostOptimal;
        listOfResultsMostOrders.add(resultsForCompleteBy);
        listOfResultsMostOrders.add(resultsForPickingTime);
        listOfResultsMostOrders.add(resultsForPickingAndCompleteTime);
        listOfResultsMostOrders.add(resultsForNoSorting);

        //finding the list with most orders
        for (List<String> list : listOfResultsMostOrders) {
            if (biggestList.size() < list.size()) biggestList = list;
        }

        //printing results
        for (String result : biggestList) {
            System.out.println(result);
        }
    }

    /**
     * In this method we sort our list by complete by - picking time and then choosing the shortest ones
     */
    public void mostOptimalCountResults(List<Order> orderList) {
        resetPickers();


        //sort orders by complete by minus picking time from lowest to highest and then by picking time from lowest to highest
        orderList.sort((o1, o2) -> {
            if (o1.getCompleteBy().minus(o1.getPickingTime()).equals(o2.getCompleteBy().minus(o2.getPickingTime()))) {
                return o1.getPickingTime().compareTo(o2.getPickingTime());
            } else {
                return o1.getCompleteBy().minus(o1.getPickingTime()).compareTo(o2.getCompleteBy().minus(o2.getPickingTime()));
            }
        });

        //assigning orders to pickers
        for (Order order : orderList) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForMostOptimal.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
            }
        }
    }
    /**
     * In this method we sort our list by picking times and then choosing the shortest ones
     */
    public void pickingTimeCountResult(List<Order> orderList) {
        resetPickers();

        //sorting orders by picking time from lowest to highest
        orderList.sort(Comparator.comparing(Order::getPickingTime));

        //assigning orders to pickers
        for (Order order : orderList) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForPickingTime.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
            }
        }
    }
    /**
     * In this method we sort our list by complete by
     */
    public void completeByCountResult(List<Order> orderList) {
        resetPickers();

        //sorting orders by picking time from lowest to highest
        orderList.sort(Comparator.comparing(Order::getCompleteBy));


        //assigning orders to pickers
        for (Order order : orderList) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForCompleteBy.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
            }
        }
    }

    /**
     * In this method we sort our list by complete by and if complete times are the same we choose the shortest picking time
     */
    public void completeByAndPickingTimeCountResult(List<Order> orderList) {
        resetPickers();

        //sorting orders by complete by and picking time from lowest to highest
        orderList.sort((o1, o2) -> {
            if (o1.getCompleteBy().equals(o2.getCompleteBy())) {
                return o1.getPickingTime().compareTo(o2.getPickingTime());
            } else {
                return o1.getCompleteBy().compareTo(o2.getCompleteBy());
            }
        });

        //assigning orders to pickers
        for (Order order : orderList) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = null;

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForPickingAndCompleteTime.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
            }
        }
    }

    /**
     * In this method we don't sort our list
     */
    public void noSortedCountResult(List<Order> orderList) {
        resetPickers();

        //assigning orders to pickers
        for (Order order : orderList) {
            LocalTime earliestTime = store.getPickingEndTime();
            String earliestPicker = "P1";

            for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
                if (!picker.getValue().isAfter(earliestTime)) {
                    earliestTime = picker.getValue();
                    earliestPicker = picker.getKey();
                }
            }
            if (!earliestTime.plus(order.getPickingTime()).isAfter(order.getCompleteBy())) {
                store.getOurPickers().put(earliestPicker, earliestTime.plus(order.getPickingTime()));
                resultsForNoSorting.add(earliestPicker + " " + order.getOrderId() + " " + earliestTime);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //UTILITY
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to reset the pickers to shop start time
     * No arguments needed
     */
    public void resetPickers() {
        //Create map for pickers and their start times
        for (String pickerID : store.getPickers()) {
            store.getOurPickers().put(pickerID, store.getPickingStartTime());
        }
    }

    /**
     * Given two parameters from java -jar app.jar param1=storePath.json param2=orderPath.json
     * The method reads data from files and saves it in store and orders variables
     */
    public void readData(String orderPath, String storePath) {
        try {
            ordersCount = parse.readOrder(orderPath);
            store = parse.readStore(storePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to print out the data we have read in
     * No arguments needed
     */
    public void readInfo() {
        //printing pickers
        for (Map.Entry<String, LocalTime> picker : store.getOurPickers().entrySet()) {
            System.out.println("ID: " + picker.getKey());
            System.out.println("Time: " + picker.getValue());
        }
        System.out.println("START: " + store.getPickingStartTime());
        System.out.println("END: " + store.getPickingEndTime());

        //print orders
        for (Order order : ordersCount) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Order value: " + order.getOrderValue());
            System.out.println("Picking time: " + order.getPickingTime().toMinutes());
            System.out.println("Complete by: " + order.getCompleteBy());
        }
    }
}
