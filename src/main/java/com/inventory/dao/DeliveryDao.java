package com.inventory.dao;

import com.inventory.database.DatabaseConnection;
import com.inventory.model.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDao {

    public void addDelivery(Delivery delivery) {
        String sql = "INSERT INTO deliveries (product_id, quantity, price, delivery_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, delivery.getProductId());
            stmt.setInt(2, delivery.getQuantity());
            stmt.setDouble(3, delivery.getPrice());
            stmt.setString(4, delivery.getDeliveryDate());
            stmt.executeUpdate();
            System.out.println("Delivery added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding delivery: " + e.getMessage());
        }
    }

    public List<Delivery> getDeliveriesByProductId(int productId) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries WHERE product_id = ? ORDER BY delivery_date DESC";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Delivery delivery = new Delivery(
                        rs.getInt("product_id"),
                        rs.getString("delivery_date"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                delivery.setId(rs.getInt("id"));
                deliveries.add(delivery);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching deliveries: " + e.getMessage());
        }
        return deliveries;
    }
}