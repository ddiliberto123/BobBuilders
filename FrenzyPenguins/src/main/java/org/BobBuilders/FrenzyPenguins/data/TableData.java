package org.BobBuilders.FrenzyPenguins.data;

import javafx.scene.control.CheckBox;
import lombok.Data;
import lombok.Getter;
import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Getter
public class TableData {
    public static double TABLEWIDTH = 800;
    private int userId;
    private String username;
    private int maxDistanceFlown;
    private int totalDistanceFlown;
    private int networth;
    private CheckBox delete;

    public TableData(int userId){
        try (Connection con = Database.connect()){
            this.userId = userId;
            PreparedStatement preparedStatement = con.prepareStatement(Database.USERNAME_REQUEST);
            preparedStatement.setInt(1,userId);
            ResultSet rs = preparedStatement.executeQuery();
            this.username = rs.getString(1);

            Inventory inventory = Inventory.getInstance();
            inventory = Database.loadInventory(userId);
            if (inventory == null){
                this.maxDistanceFlown = 0;
                this.totalDistanceFlown = 0;
                this.networth = 0;
            } else {
                this.maxDistanceFlown = inventory.getMaxDistanceFlown();
                this.totalDistanceFlown = inventory.getTotalDistanceFlown();
                this.networth = inventory.getNetworth();
            }

            delete = new CheckBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
