package translators;

import org.BobBuilders.FrenzyPenguins.translators.InventoryDeserializer;
import org.BobBuilders.FrenzyPenguins.translators.InventorySerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryDeserializerTest {
    @Test
    public void nullDeserializer(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            InventoryDeserializer inventoryDeserializer = null;
            inventoryDeserializer.deserialize(null,null,null);
        });
    }
}
