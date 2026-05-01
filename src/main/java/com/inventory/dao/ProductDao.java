package com.inventory.dao;

import com.inventory.database.DatabaseConnection;
import com.inventory.model.Product;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public void addProduct(Product product) {

        String sql = "INSERT INTO products (name, category, quantity, price) VALUES (?,?,?,?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());
            stmt.executeUpdate();
            System.out.println("Product updated successfully");

        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery(sql) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    product.setId(rs.getInt("id"));
                    products.add(product);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public void updateProducts(Product product) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=? WHERE id=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());
            stmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating product " + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}
