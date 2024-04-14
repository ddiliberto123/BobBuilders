package org.BobBuilders.FrenzyPenguins.util;

import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.translators.InventoryMapper;
import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Database {

    private static final String CONNECTION_FILE = "jdbc:sqlite:FrenzyPenguins.db";

    public static Connection connect() {
        Connection con = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            con = DriverManager.getConnection(CONNECTION_FILE, config.toProperties());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public static void dbInit() {
        try (Connection con = Database.connect()) {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Users (\n" +
                            "id INTEGER NOT NULL, " +
                            "username TEXT NOT NULL UNIQUE, " +
                            "password TEXT, " +
                            "PRIMARY KEY(id AUTOINCREMENT)" +
                            ");"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Inventories (\n" +
                            "id INTEGER NOT NULL, " +
                            "user_id INTEGER NOT NULL UNIQUE, " +
                            "inventory BLOB, " +
                            "PRIMARY KEY(id AUTOINCREMENT), " +
                            "FOREIGN KEY(user_id) REFERENCES Users(id)" +
                            ");"
            );
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static boolean createUser(String username, String password) {
        String statement = "INSERT INTO Users (username,password) VALUES (?,?)";

        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            try {
                pstatement.executeUpdate();
                return true;
            } catch (SQLException ex) {
                return false;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static int loginUser(String username, String password) {
        String statement = "SELECT id FROM Users WHERE username = ? AND password = ?";

        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            ResultSet rs = pstatement.executeQuery();
            if (rs.getInt("id") != 0) {
                System.out.println("ID" + rs.getInt("id"));
                return rs.getInt("id");
            } else {
                System.out.println("ID" + rs.getInt("id"));
                return -1;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static void save(int userId, Inventory inventory) {
        String statement = "SELECT id FROM Inventories WHERE user_id = ?";

        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setInt(1, userId);
            ResultSet rs = pstatement.executeQuery();
            if (rs.getInt("id") != 0) {
                statement = "UPDATE Inventories SET inventory = ? WHERE user_id = ?";
                pstatement = con.prepareStatement(statement);
                pstatement.setString(1, InventoryMapper.convert(inventory));
                pstatement.setInt(2, userId);
                pstatement.executeUpdate();
            } else {
                statement = "INSERT INTO Inventories (user_id,inventory) VALUES (?,?)";
                pstatement = con.prepareStatement(statement);
                pstatement.setInt(1, userId);
                pstatement.setString(2, InventoryMapper.convert(inventory));
                pstatement.executeUpdate();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static Inventory load(int userId) {
        String statement = "SELECT inventory, id FROM Inventories WHERE user_id = ?";
        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setInt(1, userId);
            ResultSet rs = pstatement.executeQuery();
            if (rs.getInt("id") != 0) {
                return InventoryMapper.unconvert(rs.getString("inventory"));
            } else {
                System.out.println("No inventory found!");
                return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
