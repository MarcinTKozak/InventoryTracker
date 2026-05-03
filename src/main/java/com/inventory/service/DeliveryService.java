package com.inventory.service;

import com.inventory.dao.DeliveryDao;
import com.inventory.model.Delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DeliveryService {

    private final DeliveryDao deliveryDao = new DeliveryDao();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void addDelivery(int productId, int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        String deliveryDate = LocalDateTime.now().format(FORMATTER);
        Delivery delivery = new Delivery(productId, deliveryDate, quantity, price);
        deliveryDao.addDelivery(delivery);
    }

    public List<Delivery> getDeliveriesByProductId(int productId) {
        return deliveryDao.getDeliveriesByProductId(productId);
    }

    public String getCurrentDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }
}