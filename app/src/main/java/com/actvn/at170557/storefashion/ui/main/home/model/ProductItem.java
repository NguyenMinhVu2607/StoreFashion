package com.actvn.at170557.storefashion.ui.main.home.model;

import java.util.List;

public class ProductItem {
    private String id; // Add this field
    private String brand;
    private String description;
    private String desciption;
    private String name;
    private String price;
    private String cate;

    private List<String> size;

    // Default constructor (required for Firestore)
    public ProductItem() {
    }

    public String getcate() {
        return cate;
    }

    public void setcate(String cate) {
        this.cate = cate;
    }

    // Getter and Setter methods for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter methods for other fields
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }
}
