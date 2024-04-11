import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import org.BobBuilders.FrenzyPenguins.CustomEntityFactory;
import org.BobBuilders.FrenzyPenguins.util.Constant;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CustomEntityFactoryTest {
    CustomEntityFactory customEntityFactory;

    @BeforeAll
    public void initialization(){
        CustomEntityFactory customEntityFactory = new CustomEntityFactory();
    }

    @Test
    public void notNull() {
        SpawnData data = new SpawnData(0,0); // input
        data.put(Constant.HEIGHT,0);
        data.put(Constant.WIDTH,0);
        Entity expectedResult = new Entity();       // expectedResult
        Entity result = customEntityFactory.createRectangle(data);

        Assertions.assertEquals(expectedResult, result);
    }

}
