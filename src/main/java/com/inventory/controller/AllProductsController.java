package com.inventory.controller;

import com.inventory.model.Product;
import com.inventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class AllProductsController {

    @FXML private TextField searchField;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> lastModifiedColumn;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private List<Product> allProducts;

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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterProducts(newValue));
    }

    private void loadProducts() {
        allProducts = productService.getAllProducts();
        productList.clear();
        productList.addAll(allProducts);
    }

    @FXML
    public void handleSearch() {
        filterProducts(searchField.getText());
    }

    @FXML
    public void handleClear() {
        searchField.clear();
        productList.clear();
        productList.addAll(allProducts);
    }

    private void filterProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            productList.clear();
            productList.addAll(allProducts);
            return;
        }

        String lowerQuery = query.toLowerCase();
        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery)
                        || p.getCategory().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());

        productList.clear();
        productList.addAll(filtered);
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) searchField.getScene().getWindow();
        stage.close();
    }
}