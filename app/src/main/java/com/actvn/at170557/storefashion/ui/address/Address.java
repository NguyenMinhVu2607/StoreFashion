package com.actvn.at170557.storefashion.ui.address;

public class Address {
    private String addressName;
    private String street;
    private String ward;
    private String district;
    private String city;
    public Address() {
    }
    // Constructor
    public Address(String addressName, String street, String ward, String district, String city) {
        this.addressName = addressName;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
    }

    public String getAddressName() { return addressName; }
    public void setAddressName(String addressName) { this.addressName = addressName; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
