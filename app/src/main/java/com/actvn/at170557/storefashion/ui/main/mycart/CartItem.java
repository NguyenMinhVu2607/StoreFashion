package com.actvn.at170557.storefashion.ui.main.mycart;

public class CartItem {
    private String imageUrl;
    private String name;
    private String size;
    private String price;
    private String quantity;

    // Constructor
    public CartItem(String imageUrl, String name, String size, String price, String quantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
}
