package com.example.rentforme.Models;

public class BookingDetails {

    String id, uname, contact, address;

    public BookingDetails() {
    }

    public BookingDetails(String id, String uname, String contact, String address) {
        this.id = id;
        this.uname = uname;
        this.contact = contact;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
