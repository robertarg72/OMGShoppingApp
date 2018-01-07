package com.centennialcollege.omgshoppingapp.model;

import android.graphics.Bitmap;


public class Product {
    private int id;
    private String name;
    private String description;
    private String price;
    private int quantity;
    private String category;
    private Bitmap image;


    public Product() {
        this.id = 0;
        this.name = "";
        this.description = "";
        this.price = "";
        this.quantity = 0;
        this.category = "";
        this.image = null;
    }


    public Product(int id, String name, String description, String price, int quantity, String category, Bitmap image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public Bitmap getImage() {
        return image;
    }
}
