package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.DeliveryService;
import com.inventory.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStockController {

    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    private final DeliveryService deliveryService = new DeliveryService();
    private final ProductService productService = new ProductService();
    private Product product;
    private Runnable onStockAdded;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOnStockAdded(Runnable callback) {
        this.onStockAdded = callback;
    }

    @FXML
    public void handleSave() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            deliveryService.addDelivery(product.getId(), quantity, price);

            product.setQuantity(product.getQuantity() + quantity);
            productService.updateProduct(product);

            if (onStockAdded != null) {
                onStockAdded.run();
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
        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}