package com.actvn.at170557.storefashion.ui.main.home.model;

public class ProductItem {
    public  int imageResourceId;
    public  String title;
    public  String subtitle;
    public  String description;

    public ProductItem(int imageResourceId, String title, String subtitle, String description) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

    public ProductItem() {
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }
}
