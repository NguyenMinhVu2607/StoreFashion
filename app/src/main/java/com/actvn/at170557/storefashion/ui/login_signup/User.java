package com.actvn.at170557.storefashion.ui.login_signup;

public class User {
    private String name;

    public User() {
        // Required for Firestore
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}