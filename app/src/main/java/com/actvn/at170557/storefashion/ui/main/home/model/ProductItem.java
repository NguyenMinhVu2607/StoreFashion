package com.actvn.at170557.storefashion.ui.main.home.model;

import java.util.List;

public class ProductItem {
    private String id;
    private String brand;
    private String description;
    private String desciption;
    private String name;
    private double price;
    private String cate;
    private String linkImg;

    private List<String> size;

    public ProductItem() {
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }
}
