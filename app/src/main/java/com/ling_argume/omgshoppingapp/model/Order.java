package com.ling_argume.omgshoppingapp.model;



public class Order {
    private int id;
    private int employeeId;
    private int customerId;
    private int productId;
    private int quantity;
    private String shippingAddress;
    private String cardType;
    private String cardOwner;
    private String cardNumber;
    private String cardExpirationMonth;
    private String getCardExpirationYear;
    private String cardSecurityCode;
    private String orderDate;
    private String status;

    public Order() {

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order(int id, int employeeId, int customerId, int productId, int quantity, String shippingAddress, String cardType, String cardOwner, String cardNumber, String cardExpirationMonth, String getCardExpirationYear, String cardSecurityCode, String orderDate, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.shippingAddress = shippingAddress;
        this.cardType = cardType;
        this.cardOwner = cardOwner;
        this.cardNumber = cardNumber;
        this.cardExpirationMonth = cardExpirationMonth;
        this.getCardExpirationYear = getCardExpirationYear;
        this.cardSecurityCode = cardSecurityCode;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(String cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public String getGetCardExpirationYear() {
        return getCardExpirationYear;
    }

    public void setGetCardExpirationYear(String getCardExpirationYear) {
        this.getCardExpirationYear = getCardExpirationYear;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
