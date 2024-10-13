package com.actvn.at170557.storefashion.ui.order;

import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;

import java.util.ArrayList;

public class Order {
    private String id;
    private String status;
    private String addressName;
    private String fullAddress;
    private String phoneNumber;
    private double totalAmount;
    private ArrayList<CartItem> items; // CartItem là model sản phẩm trong đơn hàng

    // Constructor không tham số (cho Firestore deserialization)
    public Order() {
        // Khởi tạo items là một ArrayList rỗng nếu không có dữ liệu
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
