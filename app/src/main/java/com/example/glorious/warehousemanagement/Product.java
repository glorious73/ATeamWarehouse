package com.example.glorious.warehousemanagement;

import android.media.Image;

import java.util.UUID;

public class Product {
    // Attributes
    private String name;
    private String manufacturer;
    private String description;
    private double price;
    private int quantity;
    private String userAddedProduct;
    private String productFirebaseId;

    // Constructors
    public Product() {}
    public Product(String name, String manufacturer, String description, double price, int quantity, String userAddedProduct, String imageFirebaseId) {
        this.name              = name;
        this.manufacturer      = manufacturer;
        this.description       = description;
        this.price             = price;
        this.quantity          = quantity;
        this.userAddedProduct  = userAddedProduct;
        this.productFirebaseId = imageFirebaseId;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setUserAddedProduct(String userAddedProduct) {
        this.userAddedProduct = userAddedProduct;
    }
    public void setProductFirebaseId(String imageFirebaseId) {
        this.productFirebaseId = imageFirebaseId;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getUserAddedProduct() {
        return userAddedProduct;
    }
    public String getProductFirebaseId() {
        return productFirebaseId;
    }
}
