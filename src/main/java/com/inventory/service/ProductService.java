package com.inventory.service;

import com.inventory.dao.ProductDao;
import com.inventory.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDao productDao = new ProductDao();

    public void addProduct(String name, String category, int quantity, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannto be negative");
        }
        Product product = new Product(name.trim(), category.trim(), quantity, price);
        productDao.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public void updateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        productDao.updateProduct(product);
    }

    public void deleteProduct(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product ID.");
        }
        productDao.deleteProduct(id);
    }

}
