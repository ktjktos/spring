package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.Rental;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class RentalRepository {
    private List<Rental> rentals;
    private Set<Integer> existingIDs = new HashSet<>();

    public RentalRepository() {
        Path path = Path.of("rentals.json");
        rentals = new ArrayList<>();
        try {
            if (Files.notExists(path)) {
                Files.writeString(path,"[]");
            }
            String json = Files.readString(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            rentals = gson.fromJson(json, new TypeToken<List<Rental>>() {}.getType());
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w ladowaniu rentalRepo");
        }
        if (this.rentals != null) {
            for (Rental r : rentals) {
                existingIDs.add(Integer.parseInt(r.getId()));
            }
        } else {
            this.rentals = new ArrayList<>();
        }
    }

    public List<Rental> findAll() {
        List<Rental> r = new ArrayList<>();
        for(Rental rental: rentals){
            Rental newR = rental.copy();
            r.add(newR);
        }
        return r;
    }

    public Optional<Rental> findById(String id) {
        for(Rental rental: rentals) {
            if (rental.getId().equals(id)) {
                return Optional.of(rental);
            }
        }
        return Optional.empty();
    }

    public Rental save(Rental rental) {
        int id = 1;
        if (rental.getId() == null) {
            while(existingIDs.contains(id)) { id+=1;}
            existingIDs.add(id);
            rental.setId(Integer.toString(id));
            rentals.add(rental);
        } else {
            rentals.remove(rental);
            rentals.add(rental);
        }
        this.writeToFile();
        return rental;
    }

    public void deleteById(String id) {
        for(Rental rental: rentals) {
            if (rental.getId().equals(id)) {
                rentals.remove(rental);
                existingIDs.remove(Integer.parseInt(id));
                this.writeToFile();
                break;
            }
        }
    }

    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        for(Rental rental: rentals) {
            if (rental.getUserId().equals(userId) && rental.getReturnDateTime() == null) {
                return Optional.of(rental);
            }
        }
        return Optional.empty();
    }

    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        for(Rental rental: rentals) {
            if (rental.getVehicleId().equals(vehicleId) && rental.getReturnDateTime() == null) {
                return Optional.of(rental);
            }
        }
        return Optional.empty();
    }

    public void writeToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(rentals);
            Files.writeString(Path.of("rentals.json"), json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w zapisywaniu rentalsRepo");
        }
    }
}
