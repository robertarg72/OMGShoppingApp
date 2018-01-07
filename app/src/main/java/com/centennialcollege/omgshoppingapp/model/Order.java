package com.centennialcollege.omgshoppingapp.model;



public class Order {
    private int id;
    private int employeeId;
    private int customerId;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String cardType;
    private String cardOwner;
    private String cardNumber;
    private String cardExpirationMonth;
    private String getCardExpirationYear;
    private String cardSecurityCode;
    private String orderDate;
    private String status;
    private String totalPrice;


    public Order() {}

    public Order(int id, int employeeId, int customerId, String shippingAddress, String shippingCity,
                 String shippingPostalCode, String cardType, String cardOwner, String cardNumber,
                 String cardExpirationMonth, String getCardExpirationYear, String cardSecurityCode,
                 String orderDate, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingPostalCode = shippingPostalCode;
        this.cardType = cardType;
        this.cardOwner = cardOwner;
        this.cardNumber = cardNumber;
        this.cardExpirationMonth = cardExpirationMonth;
        this.getCardExpirationYear = getCardExpirationYear;
        this.cardSecurityCode = cardSecurityCode;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = "0";
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
