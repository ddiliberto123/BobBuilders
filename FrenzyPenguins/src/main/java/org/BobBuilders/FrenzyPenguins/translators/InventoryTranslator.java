package org.BobBuilders.FrenzyPenguins.translators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.BobBuilders.FrenzyPenguins.Inventory;

import java.sql.SQLType;

public class InventoryTranslator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String convert(Inventory inventory) {
        try {
            return mapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Inventory unconvert(String s) {
        try {
            return mapper.readValue(s, Inventory.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
