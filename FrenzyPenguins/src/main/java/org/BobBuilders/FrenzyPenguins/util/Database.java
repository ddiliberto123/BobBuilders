package org.BobBuilders.FrenzyPenguins.util;

import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.translators.InventoryMapper;
import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Database {

    private static final String CONNECTION_URL = "jdbc:sqlite:FrenzyPenguins.db";
    public static final String USERNAME_REQUEST = "SELECT username FROM Users WHERE id = ?";
    public static final String USERNAME = "username";
    public static final String INVENTORY = "inventory";

    /**
     * Connects to the database
     * @return a connection with the database
     */
    public static Connection connect() {
        Connection con = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            con = DriverManager.getConnection(CONNECTION_URL, config.toProperties());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }

    /**
     * Creates all default tables inside the database if they don't already exist
     */
    public static void dbInit() {
        try (Connection con = Database.connect()) {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Users (\n" +
                            "id INTEGER NOT NULL, " +
                            "username TEXT NOT NULL UNIQUE, " +
                            "password TEXT, " +
                            "admin INTEGER NOT NULL, " +
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

    /**
     * Creates and Inserts a user into the database
     * @param username the username of the user
     * @param password the password of the user
     * @return true if user created <br> false if user not created
     */
    public static boolean createUser(String username, String password) {
        String statement = "INSERT INTO Users (username,password,admin) VALUES (?,?,?)";

        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            pstatement.setInt(3, 0);
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

    /**
     * Retrieves the user's userId
     * @param username the username of the user
     * @param password the password of the user
     * @return id of the user <br> -1 if user non existant
     */
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

    /**
     * Assigns and saves an inventory to a user
     * @param userId userId of the user
     * @param inventory inventory to be attached to the user
     */
    public static void save(int userId, Inventory inventory) {
        if (userId <= 0){
            throw new RuntimeException("Invalid UserID");
        }

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

    /**
     * Loads the inventory of a user
     * @param userId userId of the user
     * @return the inventory of the user
     */
    public static Inventory loadInventory(int userId) {
        if (userId <= 0){
            throw new RuntimeException("Invalid UserID");
        }

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

    /**
     * Retrieves the admin status of a user
     * @param userId userId of the user
     * @return true if an admin <br> false if not an admin
     */
    public static boolean getAdminStatus(int userId) {
        if (userId <= 0){
            throw new RuntimeException("Invalid UserID");
        }

        String statement = "SELECT id, admin FROM Users WHERE id = ?";
        try (Connection con = Database.connect()) {
            PreparedStatement pstatement = con.prepareStatement(statement);
            pstatement.setInt(1, userId);
            ResultSet rs = pstatement.executeQuery();
            if (rs.getInt("id") != 0) {
                return rs.getInt("admin") != 0;
            } else {
                System.out.println("No user found!");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
