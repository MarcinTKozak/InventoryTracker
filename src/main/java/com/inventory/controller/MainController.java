package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class MainController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(productList);
        loadProducts();

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
                categoryField.setText(newVal.getCategory());
                quantityField.setText(String.valueOf(newVal.getQuantity()));
                priceField.setText(String.valueOf(newVal.getPrice()));
            }
        });
    }

    @FXML
    public void handleAddProduct() {
        try {
            String name = nameField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            productService.addProduct(name, category, quantity, price);
            loadProducts();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Quantity must be a number and price must be a decimal.");
        } catch (IllegalArgumentException e) {
            showAlert("Validation error", e.getMessage());
        }
    }

    @FXML
    public void handleUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a product to update.");
            return;
        }
        try {
            selected.setName(nameField.getText());
            selected.setCategory(categoryField.getText());
            selected.setQuantity(Integer.parseInt(quantityField.getText()));
            selected.setPrice(Double.parseDouble(priceField.getText()));

            System.out.println("Updating product with ID: " + selected.getId());

            productService.updateProduct(selected);
            loadProducts();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Quantity must be a number and price must be a decimal.");
        } catch (IllegalArgumentException e) {
            showAlert("Validation error", e.getMessage());
        }
    }

    @FXML
    public void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a product to delete.");
            return;
        }
        productService.deleteProduct(selected.getId());
        loadProducts();
        clearFields();
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productService.getAllProducts());
        productTable.refresh();
    }

    private void clearFields() {
        nameField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleReset() {
        productTable.getSelectionModel().clearSelection();
        clearFields();
    }
}
