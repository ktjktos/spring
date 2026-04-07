package org.example;
public class Motorcycle extends Vehicle{
    private Category category;
    enum Category {
        A,
        A1,
        A2,
        AM,
        B
    }

    public Motorcycle(String id,String brand, String model, Integer year, Integer price, Boolean rented, String category) {
        super(id,brand, model, year, price, rented);
        this.category = Category.valueOf(category);
        this.setTypeOfVehicle("MOTORCYCLE");
    }

    public Motorcycle(Vehicle moto){
        super(moto.getId(),moto.getBrand(),moto.getModel(),moto.getYear(),moto.getPrice(),moto.isRented());
        this.category = ((Motorcycle)moto).getCategory();
        this.setTypeOfVehicle("MOTORCYCLE");
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = Category.valueOf(category);
    }

    @Override
    public String toCSV() {
        return this.getTypeOfVehicle() + ";" + this.getId()  + ";"+ this.getBrand()  + ";" + this.getModel()  + ";" + this.getYear()  + ";" + this.getPrice() + ";" + this.isRented() + ";" + this.getCategory();
    }
}

