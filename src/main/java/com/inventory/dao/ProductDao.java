package com.inventory.dao;

import com.inventory.database.DatabaseConnection;
import com.inventory.model.Product;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public int addProduct(Product product) {
        String sql = "INSERT INTO products (name, category, quantity, price, last_modified) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getLastModified());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
        return -1;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                product.setId(rs.getInt("id"));
                product.setLastModified(rs.getString("last_modified"));
                products.add(product);
            }


        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=?, last_modified=? WHERE id=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getLastModified());
            stmt.setInt(6, product.getId());
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
    public Product getProductByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                product.setId(rs.getInt("id"));
                product.setLastModified(rs.getString("last_modified"));
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product by name: " + e.getMessage());
        }
        return null;
    }
}
