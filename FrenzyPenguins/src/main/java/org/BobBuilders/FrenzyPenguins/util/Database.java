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
        }
    }

}
