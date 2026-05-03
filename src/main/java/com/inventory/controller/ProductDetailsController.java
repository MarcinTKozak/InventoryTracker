package com.inventory.controller;

import com.inventory.model.Delivery;
import com.inventory.model.Product;
import com.inventory.service.DeliveryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProductDetailsController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label quantityLabel;
    @FXML private Label priceLabel;
    @FXML private TableView<Delivery> deliveriesTable;
    @FXML private TableColumn<Delivery, String> deliveryDateColumn;
    @FXML private TableColumn<Delivery, Integer> deliveryQuantityColumn;
    @FXML private TableColumn<Delivery, Double> deliveryPriceColumn;

    private final DeliveryService deliveryService = new DeliveryService();

    public void setProduct(Product product) {
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
}