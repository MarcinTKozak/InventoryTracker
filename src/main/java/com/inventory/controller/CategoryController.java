package com.inventory.controller;

import com.inventory.model.Category;
import com.inventory.service.CategoryService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CategoryController {

    @FXML private ComboBox<Category> mainCategoryComboBox;
    @FXML private ComboBox<Category> subCategoryComboBox;
    @FXML private ComboBox<Category> variantComboBox;

    private final CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {
        loadMainCategories();

        mainCategoryComboBox.setOnAction(e -> {
            Category selected = mainCategoryComboBox.getValue();
            if (selected != null) {
                loadSubCategories(selected.getId());
            }
        });

        subCategoryComboBox.setOnAction(e -> {
            Category selected = subCategoryComboBox.getValue();
            if (selected != null) {
                loadVariants(selected.getId());
            }
        });
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

    private String showAddCategoryDialog(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Name:");
        return dialog.showAndWait().orElse(null);
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) mainCategoryComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}