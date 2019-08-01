package com.secretdevelopersltd.dexian.iothomedevice;

public class User {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String DID;

    public User() {
    }

    public User(String name, String email, String phone, String address, String DID) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.DID = DID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String pg) {
        this.DID = pg;
    }
}
