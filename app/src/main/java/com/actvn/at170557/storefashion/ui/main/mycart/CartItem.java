package com.actvn.at170557.storefashion.ui.main.mycart;

public class CartItem {
    private int imageResource;
    private String title;
    private String size;
    private String price;
    private String quantity;

    public CartItem(int imageResource, String title, String size, String price, String quantity) {
        this.imageResource = imageResource;
        this.title = title;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
