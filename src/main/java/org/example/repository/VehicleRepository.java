package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class VehicleRepository implements IVehicleRepository{
    private List<Vehicle> vehicles;
    public VehicleRepository() {
        Path path = Path.of("vehicles.json");
        vehicles = new ArrayList<>();
        try {
            if (Files.notExists(path)) {
                Files.writeString(path,"[]");
            }
            String json = Files.readString(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            vehicles = gson.fromJson(json, new TypeToken<List<Vehicle>>() {}.getType());
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w ladowaniu vehicleRepo");
        }
        if (this.vehicles == null) {
            this.vehicles = new ArrayList<>();
        }
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> v = new ArrayList<>();
        for(Vehicle vehicle: vehicles){
            v.add(vehicle.copy());
        }
        return v;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicles.add(vehicle);
        this.writeToFile();
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        for (Vehicle v: vehicles) {
            if (v.getId().equals(id)) {
                this.vehicles.remove(v);
                this.writeToFile();
                break;
            }
        }
    }

    public void writeToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(vehicles);
            Files.writeString(Path.of("vehicles.json"), json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w zapisywaniu vehicleRepo");
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        for(Vehicle vehicle: vehicles){
            if (vehicle.getId().equals(id)) return Optional.of(vehicle);
        }
        return Optional.empty();
    }
}
