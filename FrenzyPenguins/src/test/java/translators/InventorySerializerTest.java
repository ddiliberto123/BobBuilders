package translators;

import org.BobBuilders.FrenzyPenguins.data.TableData;
import org.BobBuilders.FrenzyPenguins.translators.InventorySerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventorySerializerTest {
    @Test
    public void nullSerializer(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            InventorySerializer inventorySerializer = null;
            inventorySerializer.serialize(null,null,null);
        });
    }
}
