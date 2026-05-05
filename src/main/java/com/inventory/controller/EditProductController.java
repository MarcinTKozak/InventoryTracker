package com.inventory.controller;

import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.service.CategoryService;
import com.inventory.service.DeliveryService;
import com.inventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EditProductController {

    @FXML private ComboBox<Category> mainCategoryComboBox;
    @FXML private ComboBox<Category> subCategoryComboBox;
    @FXML private ComboBox<Category> variantComboBox;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private Product product;
    private Runnable onProductUpdated;

    @FXML
    public void initialize() {
        loadMainCategories();

        mainCategoryComboBox.setOnAction(e -> {
            Category selected = mainCategoryComboBox.getValue();
            if (selected != null) {
                loadSubCategories(selected.getId());
                generateProductName();
            }
        });

        subCategoryComboBox.setOnAction(e -> {
            Category selected = subCategoryComboBox.getValue();
            if (selected != null) {
                loadVariants(selected.getId());
                generateProductName();
            }
        });

        variantComboBox.setOnAction(e -> generateProductName());
    }

    public void setProduct(Product product) {
        this.product = product;
        nameField.setText(product.getName());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPrice()));
    }

    public void setOnProductUpdated(Runnable callback) {
        this.onProductUpdated = callback;
    }

    private void loadMainCategories() {
        List<Category> categories = categoryService.getRootCategories();
        mainCategoryComboBox.setItems(FXCollections.observableArrayList(categories));
        setCellFactory(mainCategoryComboBox);
    }

    private void loadSubCategories(int parentId) {
        List<Category> subcategories = categoryService.getSubcategories(parentId);
        subCategoryComboBox.setItems(FXCollections.observableArrayList(subcategories));
        subCategoryComboBox.setValue(null);
        variantComboBox.setItems(FXCollections.observableArrayList());
        variantComboBox.setValue(null);
        setCellFactory(subCategoryComboBox);
    }

    private void loadVariants(int parentId) {
        List<Category> variants = categoryService.getSubcategories(parentId);
        variantComboBox.setItems(FXCollections.observableArrayList(variants));
        variantComboBox.setValue(null);
        setCellFactory(variantComboBox);
    }

    private void generateProductName() {
        StringBuilder name = new StringBuilder();
        if (mainCategoryComboBox.getValue() != null) {
            name.append(mainCategoryComboBox.getValue().getName());
        }
        if (subCategoryComboBox.getValue() != null) {
            name.append(" ").append(subCategoryComboBox.getValue().getName());
        }
        if (variantComboBox.getValue() != null) {
            name.append(" ").append(variantComboBox.getValue().getName());
        }
        nameField.setText(name.toString().trim());
    }

    private void setCellFactory(ComboBox<Category> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }

    @FXML
    public void handleAddMainCategory() {
        String name = showAddCategoryDialog("Add Main Category");
        if (name != null) {
            categoryService.addCategory(name, 0);
            loadMainCategories();
        }
    }

    @FXML
    public void handleAddSubCategory() {
        Category parent = mainCategoryComboBox.getValue();
        if (parent == null) {
            showAlert("No selection", "Please select a main category first.");
            return;
        }
        String name = showAddCategoryDialog("Add Subcategory under " + parent.getName());
        if (name != null) {
            categoryService.addCategory(name, parent.getId());
            loadSubCategories(parent.getId());
        }
    }

    @FXML
    public void handleAddVariant() {
        Category parent = subCategoryComboBox.getValue();
        if (parent == null) {
            showAlert("No selection", "Please select a subcategory first.");
            return;
        }
        String name = showAddCategoryDialog("Add Variant under " + parent.getName());
        if (name != null) {
            categoryService.addCategory(name, parent.getId());
            loadVariants(parent.getId());
        }
    }

    @FXML
    public void handleUpdate() {
        try {
            product.setName(nameField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            product.setPrice(Double.parseDouble(priceField.getText()));

            if (mainCategoryComboBox.getValue() != null) {
                product.setCategory(mainCategoryComboBox.getValue().getName());
            }

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

    private String showAddCategoryDialog(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Name:");
        return dialog.showAndWait().orElse(null);
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