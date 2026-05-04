package com.inventory.controller;

import com.inventory.model.Category;
import com.inventory.service.CategoryService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CategoryController {

    @FXML private TextField categoryNameField;
    @FXML private ComboBox<Category> parentCategoryComboBox;
    @FXML private TreeView<String> categoryTreeView;

    private final CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {
        loadParentCategories();
        loadCategoryTree();
    }

    private void loadParentCategories() {
        List<Category> rootCategories = categoryService.getRootCategories();
        parentCategoryComboBox.setItems(FXCollections.observableArrayList(rootCategories));
        parentCategoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        parentCategoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Select parent (optional)" : item.getName());
            }
        });
    }

    private void loadCategoryTree() {
        TreeItem<String> root = new TreeItem<>("Categories");
        root.setExpanded(true);

        List<Category> rootCategories = categoryService.getRootCategories();
        for (Category category : rootCategories) {
            TreeItem<String> categoryItem = new TreeItem<>(category.getName());
            List<Category> subcategories = categoryService.getSubcategories(category.getId());
            for (Category sub : subcategories) {
                TreeItem<String> subItem = new TreeItem<>(sub.getName());
                List<Category> subSubcategories = categoryService.getSubcategories(sub.getId());
                for (Category subSub : subSubcategories) {
                    subItem.getChildren().add(new TreeItem<>(subSub.getName()));
                }
                categoryItem.getChildren().add(subItem);
            }
            root.getChildren().add(categoryItem);
        }
        categoryTreeView.setRoot(root);
    }

    @FXML
    public void handleAddCategory() {
        try {
            String name = categoryNameField.getText();
            Category selectedParent = parentCategoryComboBox.getValue();
            int parentId = selectedParent != null ? selectedParent.getId() : 0;

            categoryService.addCategory(name, parentId);
            categoryNameField.clear();
            parentCategoryComboBox.setValue(null);
            loadParentCategories();
            loadCategoryTree();
        } catch (IllegalArgumentException e) {
            showAlert("Validation error", e.getMessage());
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) categoryNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}