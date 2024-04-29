package util;

import org.BobBuilders.FrenzyPenguins.util.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    @Test
    public void nullUser(){
        Assertions.assertFalse(Database.createUser(null, null));
    }
    @Test
    public void emptyUser(){
        Assertions.assertFalse(Database.createUser("", ""));
    }
    @Test
    public void nullLogIn(){
        Assertions.assertEquals(-1,Database.loginUser(null, null));
    }
    @Test
    public void emptyLogin(){
        Assertions.assertEquals(-1,Database.loginUser("", ""));
    }
    @Test
    public void nullSave(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.save(0,null);
        });
    }
    @Test
    public void emptySave(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.save(-1,null);
        });
    }

    @Test
    public void invalidDelete(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.delete(0);
        });
    }

    @Test
    public void negativeDelete(){
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.delete(-1);
        });
    }

    @Test
    public void invalidAdminStatus() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.getAdminStatus(0);
        });
    }

    @Test
    public void negativeAdminStatus() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.getAdminStatus(-1);
        });
    }

    @Test
    public void invalidLoad() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.loadInventory(0);
        });
    }

    @Test
    public void negativeLoad() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.loadInventory(-1);
        });
    }

    @Test
    public void nullLoadTable() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Database.loadTable(null,null);
        });
    }

}
