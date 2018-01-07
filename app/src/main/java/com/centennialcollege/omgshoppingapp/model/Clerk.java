package com.centennialcollege.omgshoppingapp.model;


public class Clerk {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;


    public Clerk() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.firstname = "";
        this.lastname = "";

    }

    public Clerk(int id, String username, String password, String firstname, String lastname, String city, String address, String postalcode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;

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

}
