package com.centennialcollege.omgshoppingapp.model;

public class OrderItem {

    private int id;
    private int productId;
    private int orderId;
    private String date;
    private String status;
    private int quantity;

    public OrderItem() {}

    public OrderItem(int id, int productId, int orderId, String date, String status, int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.date = date;
        this.status = status;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
