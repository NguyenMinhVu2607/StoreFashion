package com.actvn.at170557.storefashion.ui.address;

public class Address {
    private String street;
    private String ward;
    private String district;
    private String city;

    // Constructor rỗng cho Firestore
    public Address() {
    }

    public Address(String street, String ward, String district, String city) {
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
    }

    // Getter và Setter
    public String getStreet() {
        return street;
    }

    public String getWard() {
        return ward;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }
}
