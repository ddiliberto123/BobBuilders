package org.BobBuilders.FrenzyPenguins.util;

import org.sqlite.SQLiteConfig;
import java.sql.*;

public class Database {

    private static final String CONNECTION_FILE = "jdbc:sqlite:FrenzyPenguins.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(CONNECTION_FILE,config.toProperties());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void dbInit() {
        try (Connection conn = Database.connect()) {
            Statement statement = conn.createStatement();
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
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static boolean CreateUser(String username, String password) {
        String statement = "INSERT INTO Users (username,password)VALUES (?,?);";

        try (Connection conn = Database.connect()) {
            PreparedStatement pstatement = conn.prepareStatement(statement);
            pstatement.setString(1,username);
            pstatement.setString(2,password);
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

    public static int Login(String username, String password) {
        String statement = "SELECT id FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = Database.connect()) {
            PreparedStatement pstatement = conn.prepareStatement(statement);
            pstatement.setString(1,username);
            pstatement.setString(2,password);
            ResultSet rs = pstatement.executeQuery();
            if (rs.getInt("id") != 0){
                System.out.println("ID" + rs.getInt("id") );
                return rs.getInt("id");
            } else {
                System.out.println("ID" + rs.getInt("id") );
                return -1;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
