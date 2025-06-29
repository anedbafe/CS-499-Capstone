package com.zybooks.inventoryapp.model;

// PRODUCT MODEL CLASS TO MATCH BACKEND FIELDS
public class Product {

    // UNIQUE IDENTIFIER (REQUIRED FOR UPDATE AND DELETE)
    private String _id;

    // PRODUCT NAME
    private String product_name;

    // PRODUCT QUANTITY
    private int product_quantity;

    // PRODUCT LOCATION
    private String product_location;

    // EMPTY CONSTRUCTOR REQUIRED FOR RETROFIT
    public Product() {}

    // CONSTRUCTOR USED TO CREATE A PRODUCT INSTANCE
    public Product(String product_name, int product_quantity, String product_location) {
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_location = product_location;
    }

    // GETTERS
    public String get_id() {
        return _id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_location() {
        return product_location;
    }

    // SETTERS
    public void set_id(String _id) {
        this._id = _id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public void setProduct_location(String product_location) {
        this.product_location = product_location;
    }

    // DISPLAY PRODUCT AS STRING IN LIST VIEWS
    @Override
    public String toString() {
        return product_name + " - Qty: " + product_quantity + " - " + product_location;
    }
}
