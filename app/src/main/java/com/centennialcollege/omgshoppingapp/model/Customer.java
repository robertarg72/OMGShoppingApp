package com.centennialcollege.omgshoppingapp.model;


public class Customer {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String city;
    private String address;
    private String postalcode;

    public Customer() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.firstname = "";
        this.lastname = "";
        this.city = "";
        this.address = "";
        this.postalcode="";
    }

    public Customer(int id, String username, String password, String firstname, String lastname, String city, String address, String postalcode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.city = city;
        this.address = address;
        this.postalcode = postalcode;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getLastname() {return lastname;}

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String  getAddress() {return address;}

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
    public String  getPostalcode() {return postalcode;}
}
