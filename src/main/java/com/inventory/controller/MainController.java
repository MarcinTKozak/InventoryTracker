package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

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
    @FXML
    private TableColumn<Product, String> lastModifiedColumn;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));

        productTable.setItems(productList);
        loadProducts();
        productTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleViewDetails();
            }
        });
    }

    @FXML
    public void handleAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/AddProductView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            AddProductController controller = loader.getController();
            controller.setOnProductAdded(this::loadProducts);

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Add Product window: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a product to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/EditProductView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            EditProductController controller = loader.getController();
            controller.setProduct(selected);
            controller.setOnProductUpdated(this::loadProducts);

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Edit Product window: " + e.getMessage());
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
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productService.getAllProducts());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleViewDetails() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a product to view.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/ProductDetailsView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Product Details");
            Stage mainStage = (Stage) productTable.getScene().getWindow();
            stage.setWidth(mainStage.getWidth());
            stage.setHeight(mainStage.getHeight());
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            ProductDetailsController controller = loader.getController();
            controller.setProduct(selected);
            controller.setOnProductUpdated(this::loadProducts);

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Product Details window: " + e.getMessage());
        }
    }
    @FXML
    public void handleManageCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/CategoryView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Manage Categories");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Categories window: " + e.getMessage());
        }
    }
}