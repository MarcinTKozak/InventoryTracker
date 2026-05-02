package com.inventory.controller;

import com.inventory.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProductDetailsController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label quantityLabel;
    @FXML private Label priceLabel;

    public void setProduct(Product product) {
        idLabel.setText(String.valueOf(product.getId()));
        nameLabel.setText(product.getName());
        categoryLabel.setText(product.getCategory());
        quantityLabel.setText(String.valueOf(product.getQuantity()));
        priceLabel.setText(String.valueOf(product.getPrice()));
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }
}