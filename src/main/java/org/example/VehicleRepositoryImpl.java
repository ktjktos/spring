package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class VehicleRepositoryImpl implements IVehicleRepository{
    List<Vehicle> vehicles;
    Set<Integer> existingIDs = new HashSet<>();
    public VehicleRepositoryImpl() {
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
        if (this.vehicles != null) {
            for (Vehicle v : vehicles) {
                existingIDs.add(Integer.parseInt(v.getId()));
            }
        } else {
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
        int id = 1;
        if (vehicle.getId() == null) {
            while(existingIDs.contains(id)) { id+=1;}
            existingIDs.add(id);
            vehicle.setId(Integer.toString(id));
            vehicles.add(vehicle);
        } else {
            vehicles.remove(vehicle);
            vehicles.add(vehicle);
        }
        this.writeToFile();
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        for (Vehicle v: vehicles) {
            if (v.getId().equals(id)) {
                this.vehicles.remove(v);
                this.existingIDs.remove(Integer.parseInt(id));
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
