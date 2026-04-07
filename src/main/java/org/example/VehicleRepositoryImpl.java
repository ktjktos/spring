package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class VehicleRepositoryImpl implements IVehicleRepository{
    List<Vehicle> vehicles;
    Set<Integer> existingIDs = new HashSet<>();
    public VehicleRepositoryImpl() {
        load();
    }

    @Override
    public Boolean rentVehicle(String id) {
        for(Vehicle vehicle: vehicles){
            if (vehicle.getId().equals(id) && !vehicle.isRented()){
                vehicle.setRented(true);
                this.save();
                return true;
            }
        }
        return false;
    }


    @Override
    public Boolean returnVehicle(String id) {
        for(Vehicle vehicle: vehicles){
            if (vehicle.getId().equals(id) && vehicle.isRented()){
                vehicle.setRented(false);
                this.save();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> v = new ArrayList<>();
        for(Vehicle vehicle: vehicles){
            if(vehicle.getTypeOfVehicle().equals("CAR")) {
                Car newC = new Car(vehicle);
                v.add(newC);
            } else {
                Motorcycle newM = new Motorcycle(vehicle);
                v.add(newM);
            }
        }
        return v;
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter("vehicles.txt")){
            for (Vehicle vehicle: vehicles) {
                writer.println(vehicle.toCSV());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        this.vehicles = new ArrayList<>();
        try {
            String path = "vehicles.txt";
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] obj = line.split(";");
                this.existingIDs.add(Integer.parseInt(obj[1]));
                if (obj[0].equals("CAR")){
                    vehicles.add(new Car(obj[1],obj[2],obj[3],Integer.parseInt(obj[4]),
                            (int)Double.parseDouble(obj[5]),Boolean.parseBoolean(obj[6])));
                } else {
                    vehicles.add(new Motorcycle(obj[1],obj[2],obj[3],Integer.parseInt(obj[4]),
                            (int)Double.parseDouble(obj[5]),Boolean.parseBoolean(obj[6]),obj[7]));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku.(VehicleRepo)");
        }
    }

    public void add(String data) {
        //CAR;Audi;1;A4;2022;200
        //MOTORCYCLE;5;Honda;CBR1000;2019;300;false;A
        //:CAR;BRAND;MODEL;YEAR;PRICE:
        //:MOTORCYCLE;BRAND;MODEL;YEAR;PRICE;CATEGORY:
        int id = 1;
        while(existingIDs.contains(id)) { id+=1;}
        existingIDs.add(id);

        String[] s = data.split(";");
        if (s[0].equals("CAR")) {
            Car car = new Car(Integer.toString(id),s[1],s[2],Integer.parseInt(s[3]),Integer.parseInt(s[4]),false);
            vehicles.add(car);
        } else {
            Motorcycle motorcycle = new Motorcycle(Integer.toString(id),s[1],s[2],Integer.parseInt(s[3]),Integer.parseInt(s[4]),false,s[5]);
            vehicles.add(motorcycle);
        }
        this.save();
    }
    public void remove(String id) {
        for (Vehicle v: vehicles) {
            if (v.getId().equals(id)) {
                this.vehicles.remove(v);
                this.existingIDs.remove(Integer.parseInt(id));
                this.save();
                break;
            }
        }
    }
    public Vehicle getVehicle(String id) {
        for(Vehicle vehicle: vehicles){
            if (vehicle.getId().equals(id)) return vehicle;
        }
        return null;
    }

    public void stopRentingVehicle(Vehicle vehicle) {
        vehicle.setRented(false);
    }
}
