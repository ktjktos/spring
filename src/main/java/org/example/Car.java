package org.example;
public class Car extends Vehicle{
    public Car(String id,String brand, String model, Integer year, Integer price, Boolean rented) {
        super(id,brand, model, year, price, rented);
        this.setTypeOfVehicle("CAR");
    }
    public Car(Vehicle car){
        super(car.getId(),car.getBrand(),car.getModel(),car.getYear(),car.getPrice(),car.isRented());
        this.setTypeOfVehicle("CAR");
    }

}
