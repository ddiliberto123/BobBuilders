package util;

import org.BobBuilders.FrenzyPenguins.util.Database;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntitySpawnerTest {
    @Test
    public void zeroWidth() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnRectangle(0,0,0,100);
        });
    }

    @Test
    public void zeroHeight() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnRectangle(0,0,100,0);
        });
    }

    @Test
    public void zeroWidthAndHeight() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnRectangle(0,0,0,0);
        });
    }

    @Test
    public void zeroRadiusCircle() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnCircle(0,0,0);
        });
    }

    @Test
    public void zeroRadiusCurve() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnCurve(0,0,0,90,180);
        });
    }

    @Test
    public void sameAngleCurve() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnCurve(0,0,100,90,90);
        });
    }

    @Test
    public void sameAngleAndZeroRadiusCurve() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            EntitySpawner.spawnCurve(0,0,0,90,90);
        });
    }


}
