package translators;

import org.BobBuilders.FrenzyPenguins.translators.InventoryDeserializer;
import org.BobBuilders.FrenzyPenguins.translators.InventoryMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryMapperTest {

    @Test
    public void nullInventoryConvert(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            InventoryMapper.convert(null);
        });
    }
    @Test
    public void nullStringUnconvert(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            InventoryMapper.unconvert(null);
        });
    }
    @Test
    public void emptyStringUnconvert(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            InventoryMapper.unconvert("");
        });
    }
}
