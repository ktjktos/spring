package org.example.service;

import org.example.model.Rental;
import org.example.model.Vehicle;
import org.example.repository.RentalRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RentalService {
    RentalRepository rentalRepo;
    VehicleService vehicleService;
    public RentalService(RentalRepository rentalRepo, VehicleService vehicleService) {
        this.vehicleService = vehicleService;
        this.rentalRepo = rentalRepo;
    }

    public String whatVehicleIsRented(String userID) {
        Optional<Rental> rental = rentalRepo.findByUserIdAndReturnDateIsNull(userID);
        if (rental.isPresent()) {
           return vehicleService.findVehicleById(rental.get().getVehicleId()).get().toString();
        } else {
            return "Obecnie nie ma wypozyczonego pojazdu.";
        }
    }

    public List<String> getRentalVehicleIDs() {
        List<String> ret = new ArrayList<>();
        List<Rental> l = rentalRepo.findAll();

        for (Rental rental: l) {
            if (rental.getReturnDateTime() == null) {
                if (!ret.contains(rental.getVehicleId())) {
                    ret.add(rental.getVehicleId());
                }
            }
        }
        return ret;
    }

    public List<Vehicle> getAvailableVehicles() {
        List<String> rentedVehicleIDs = getRentalVehicleIDs();
        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        List<Vehicle> ret = new ArrayList<>();

        for (Vehicle vehicle: vehicles) {
            if(!rentedVehicleIDs.contains(vehicle.getId())) {
                ret.add(vehicle);
            }
        }

        return ret;

    }
    
    public boolean rent(String userID,String vehicleID) {
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(userID);
        if (r.isEmpty()) {
            Rental rental = Rental.builder()
                    .id(null)
                    .userId(userID)
                    .vehicleId(vehicleID)
                    .rentDateTime(new Date())
                    .returnDateTime(null)
                    .build();
            rentalRepo.save(rental);
            return true;
        }
        return false;
    }

    public boolean returnVehicle(String userID) {
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(userID);
        if (r.isPresent()) {
            r.get().setReturnDateTime(new Date());
            rentalRepo.save(r.get());
            return true;
        }
        return false;
    }

    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleID) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleID);
    }
}
