package com.example.rentforme.Models;

public class Vehicles {

    String id, name, number,perday , milage, location, phone, description;

    public Vehicles() {
    }

    public Vehicles(String id, String name, String number, String perday, String milage, String location, String phone, String description) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.perday = perday;
        this.milage = milage;
        this.location = location;
        this.phone = phone;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPerday() {
        return perday;
    }

    public void setPerday(String perday) {
        this.perday = perday;
    }

    public String getMilage() {
        return milage;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
