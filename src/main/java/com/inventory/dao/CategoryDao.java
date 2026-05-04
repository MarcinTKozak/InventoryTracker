package com.inventory.dao;

import com.inventory.database.DatabaseConnection;
import com.inventory.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public void addCategory(Category category) {
        String sql = "INSERT INTO categories (name, parent_id) VALUES (?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            if (category.getParentId() == 0) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, category.getParentId());
            }
            stmt.executeUpdate();
            System.out.println("Category added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getString("name"),
                        rs.getInt("parent_id")
                );
                category.setId(rs.getInt("id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    public List<Category> getSubcategories(int parentId) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, parentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Category category = new Category(
                        rs.getString("name"),
                        rs.getInt("parent_id")
                );
                category.setId(rs.getInt("id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching subcategories: " + e.getMessage());
        }
        return categories;
    }
    public List<Category> getRootCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getString("name"),
                        0
                );
                category.setId(rs.getInt("id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching root categories: " + e.getMessage());
        }
        return categories;
    }
}