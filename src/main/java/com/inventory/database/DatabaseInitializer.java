package com.inventory.database;

import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        String crateProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                quantity INTEGER NOT NULL,
                price REAL NOT NULL
                )
                
                """;
        String createCategoriesTable = """
        CREATE TABLE IF NOT EXISTS categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            parent_id INTEGER,
            FOREIGN KEY (parent_id) REFERENCES categories(id)
        )
        """;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.execute(createCategoriesTable);
            System.out.println("Categories table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating categories table: " + e.getMessage());
        }

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.execute(crateProductsTable);
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
        String addLastModifiedColumn = """
        ALTER TABLE products ADD COLUMN last_modified TEXT
        """;

        String createDeliveriesTable = """
        CREATE TABLE IF NOT EXISTS deliveries (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            product_id INTEGER NOT NULL,
            quantity INTEGER NOT NULL,
            price REAL NOT NULL,
            delivery_date TEXT NOT NULL,
            FOREIGN KEY (product_id) REFERENCES products(id)
        )
        """;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.execute(addLastModifiedColumn);
        } catch (SQLException e) {
            System.out.println("Column already exists, skipping.");
        }

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.execute(createDeliveriesTable);
            System.out.println("Deliveries table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating deliveries table: " + e.getMessage());
        }
    }
}
