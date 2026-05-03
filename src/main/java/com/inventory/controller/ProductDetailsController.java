package com.inventory.controller;

import com.inventory.model.Delivery;
import com.inventory.model.Product;
import com.inventory.service.DeliveryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.inventory.controller.EditProductController;

import java.io.IOException;

public class ProductDetailsController {

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private TableView<Delivery> deliveriesTable;
    @FXML
    private TableColumn<Delivery, String> deliveryDateColumn;
    @FXML
    private TableColumn<Delivery, Integer> deliveryQuantityColumn;
    @FXML
    private TableColumn<Delivery, Double> deliveryPriceColumn;

    private Runnable onProductUpdated;
    private Product product;

    public void setOnProductUpdated(Runnable callback) {
        this.onProductUpdated = callback;
    }

    private final DeliveryService deliveryService = new DeliveryService();

    public void setProduct(Product product) {
        this.product = product;
        idLabel.setText(String.valueOf(product.getId()));
        nameLabel.setText(product.getName());
        categoryLabel.setText(product.getCategory());
        quantityLabel.setText(String.valueOf(product.getQuantity()));
        priceLabel.setText(String.valueOf(product.getPrice()));
        ObservableList<Delivery> deliveries = FXCollections.observableArrayList();
        deliveries.addAll(deliveryService.getDeliveriesByProductId(product.getId()));
        deliveriesTable.setItems(deliveries);
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        deliveryQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        deliveryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    public void handleAddStock() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/AddStockView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Stock");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            AddStockController controller = loader.getController();
            controller.setProduct(product);
            controller.setOnStockAdded(() -> {
                if (onProductUpdated != null) {
                    onProductUpdated.run();
                }
                refreshProduct();
            });

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Add Stock window: " + e.getMessage());
        }
    }

    @FXML
    public void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/view/EditProductView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            EditProductController controller = loader.getController();
            controller.setProduct(product);
            controller.setOnProductUpdated(() -> {
                if (onProductUpdated != null) {
                    onProductUpdated.run();
                }
                refreshProduct();
            });

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening Edit Product window: " + e.getMessage());
        }
    }

    private void refreshProduct() {
        quantityLabel.setText(String.valueOf(product.getQuantity()));
        ObservableList<Delivery> deliveries = FXCollections.observableArrayList();
        deliveries.addAll(deliveryService.getDeliveriesByProductId(product.getId()));
        deliveriesTable.setItems(deliveries);
    }
}