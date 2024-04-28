package org.BobBuilders.FrenzyPenguins;

import lombok.Getter;
import lombok.Setter;
import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.FallingPenguinGame;

@Getter
@Setter
public class User {

    private static User instance = null;

    private int userId;
    private String username;
    private boolean admin;


    private User(){
        this.userId = 0;
        this.username = "";
        this.admin = false;
    }

    public static User getInstance() {
        return instance = (instance == null) ? new User() : instance;
    }

    //TODO: Connect user to their inventory and instantiate the inventory class
    private void accessInventory() {

    }
}
