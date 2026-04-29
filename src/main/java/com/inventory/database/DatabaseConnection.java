package com.inventory.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:inventory.db";
    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Connected with database");
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection disconnected");
            } catch (SQLException e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }
}
