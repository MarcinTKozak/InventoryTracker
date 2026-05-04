package com.inventory.service;

import com.inventory.dao.CategoryDao;
import com.inventory.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDao();

    public void addCategory(String name, int parentId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        Category category = new Category(name.trim(), parentId);
        categoryDao.addCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public List<Category> getSubcategories(int parentId) {
        return categoryDao.getSubcategories(parentId);
    }

    public List<Category> getRootCategories() {
        return categoryDao.getRootCategories();
    }
}