package com.inventory.model;

public class Delivery {

    private int id;
    private int productId;
    private String deliveryDate;
    private int quantity;
    private double price;

    public Delivery() {
    }

    public Delivery(int productId, String deliveryDate, int quantity, double price) {
        this.productId = productId;
        this.deliveryDate = deliveryDate;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + "productId='" + productId + "', deliveryDate='" + deliveryDate + "', quantity=" + quantity + ", price=" + price + "}";
    }
}
