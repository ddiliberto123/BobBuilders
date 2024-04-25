package org.BobBuilders.FrenzyPenguins.translators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.BobBuilders.FrenzyPenguins.Inventory;

/**
 * Maps the {@code Inventory} class into the database. <br> In otherwords, takes the {@code Inventory} class turns it into a string and places it inside the database
 */
public class InventoryMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts an instance of the {@code Inventory} class into a string
     * @param inventory the {@code Inventory} to be converted
     * @return the string version of the {@code Inventory}
     */
    public static String convert(Inventory inventory) {
        try {
            return mapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a string into an instance of the {@code Inventory} class
     * @param s the string to be converted
     * @return the {@code Inventory} version of the string
     */
    public static Inventory unconvert(String s) {
        try {
            return mapper.readValue(s, Inventory.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
