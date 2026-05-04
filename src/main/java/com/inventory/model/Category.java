package com.inventory.model;

public class Category {

    private int id;
    private String name;
    private int parentId;


    public Category() {
    }

    public Category(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String toString() {
        return "Category{id=" + id + "name='" + name + "', parentId='" + parentId + "}";
    }


}
