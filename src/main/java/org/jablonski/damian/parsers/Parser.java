package org.jablonski.damian.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.jablonski.damian.data.Order;
import org.jablonski.damian.data.Store;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    /**
     * Creating object mapper with modules to read Duration, LocalTime and BigDecimal
     */
    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();


    /**
     * Reading store from JSON file
     * Argument is a path specified to file
     */
    public Store readStore(String store) throws IOException {

        try {

            File newFile = new File(store);
            FileInputStream inputStream = new FileInputStream(newFile);

            if (inputStream.available() > 0) {
                return objectMapper.readValue(inputStream, Store.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Store();
    }

    /**
     * Reading orders from JSON file
     * Argument is a path specified to file
     */
    public ArrayList<Order> readOrder(String order) throws IOException {

        try {

            File newFile = new File(order);
            FileInputStream inputStream = new FileInputStream(newFile);
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class);

            if (inputStream.available() > 0) {
                return objectMapper.readValue(inputStream, collectionType);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
