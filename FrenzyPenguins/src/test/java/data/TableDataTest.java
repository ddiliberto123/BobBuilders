package data;

import org.BobBuilders.FrenzyPenguins.data.TableData;
import org.BobBuilders.FrenzyPenguins.util.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableDataTest {

    @Test
    public void invalidTableData(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            new TableData(0);
        });
    }

    @Test
    public void negativeDelete(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            new TableData(-1);
        });
    }
}
