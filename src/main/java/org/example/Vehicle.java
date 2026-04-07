package org.example;
public abstract class Vehicle {
    private String typeOfVehicle;
    private String id;
    private String brand;
    private String model;
    private Integer year;
    private Integer price;
    private Boolean rented;

    public Vehicle(String id, String brand, String model, Integer year, Integer price, Boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(String typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    public String toCSV() {
        return typeOfVehicle + ";" + id + ";" + brand + ";"+ model + ";" + year + ";" + price + ";" + rented;
    }
    public String toString() {
        return id + "| "+ brand + " " + model + " " + year + " price: " + price + " PLN";
    }
}
