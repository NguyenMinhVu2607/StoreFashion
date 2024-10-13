package com.actvn.at170557.storefashion.ui.main.mycart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String imageUrl;
    private String name;
    private String size;
    private String price;
    private String quantity;
    private boolean isChecked;
    public CartItem() {
        // Constructor mặc định
    }
    // Constructor
    public CartItem(String imageUrl, String name, String size, String price, String quantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.isChecked = false; // Mặc định chưa được chọn
    }

    // Getter and Setter methods
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    // Parcelable methods
    protected CartItem(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
        size = in.readString();
        price = in.readString();
        quantity = in.readString();
        isChecked = in.readByte() != 0; // Chuyển đổi từ byte sang boolean
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(price);
        dest.writeString(quantity);
        dest.writeByte((byte) (isChecked ? 1 : 0)); // Chuyển đổi boolean sang byte
    }
}
