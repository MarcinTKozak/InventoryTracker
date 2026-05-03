package com.inventory.controller;

import com.inventory.service.DeliveryService;
import com.inventory.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProductController {

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    private final ProductService productService = new ProductService();
    private Runnable onProductAdded;

    public void setOnProductAdded(Runnable callback) {
        this.onProductAdded = callback;
    }

    @FXML
    public void handleSave() {
        try {
            String name = nameField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            int productId = productService.addProduct(name, category, quantity, price);
            DeliveryService deliveryService = new DeliveryService();
            deliveryService.addDelivery(productId, quantity, price);

            if (onProductAdded != null) {
                onProductAdded.run();
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