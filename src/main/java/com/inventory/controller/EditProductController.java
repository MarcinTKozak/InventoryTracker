package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.DeliveryService;
import com.inventory.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProductController {

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    private final ProductService productService = new ProductService();
    private Product product;
    private Runnable onProductUpdated;

    public void setProduct(Product product) {
        this.product = product;
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPrice()));
    }

    public void setOnProductUpdated(Runnable callback) {
        this.onProductUpdated = callback;
    }

    @FXML
    public void handleUpdate() {
        try {
            product.setName(nameField.getText());
            product.setCategory(categoryField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            product.setPrice(Double.parseDouble(priceField.getText()));

            productService.updateProduct(product);
            DeliveryService deliveryService = new DeliveryService();
            deliveryService.addDelivery(product.getId(), product.getQuantity(), product.getPrice());

            if (onProductUpdated != null) {
                onProductUpdated.run();
            }

            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Quantity must be a whole number and price must be a decimal.");
        } catch (IllegalArgumentException e) {
            showAlert("Validation error", e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}