package org.example.service;

import org.example.model.Rental;
import org.example.model.Vehicle;
import org.example.repository.IRentalRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RentalService {
    IRentalRepository rentalRepo;
    VehicleService vehicleService;
    UserService userService;
    public RentalService(IRentalRepository rentalRepo, VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.rentalRepo = rentalRepo;
        this.userService = userService;
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
                    .id(java.util.UUID.randomUUID().toString())
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

    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userID) {
        return rentalRepo.findByUserIdAndReturnDateIsNull(userID);
    }

    public boolean removeVehicle(String id) {
        if (findByVehicleIdAndReturnDateIsNull(id).isEmpty()) {
            vehicleService.deleteVehicleById(id);
            System.out.println("Successfully deleted selected vehicle.");
            return true;
        } else {
            System.out.println("Something went wrong while deleting selected vehicle.");
        }
        return false;
    }

    public boolean removeUser(String id) {
        if (findByUserIdAndReturnDateIsNull(id).isEmpty()) {
            if (userService.deleteUser(id)) {
                System.out.println("Successfully deleted selected user.");
                return true;
            }
        } else {
            System.out.println("Something went wrong while deleting selected user.");
        }
        return false;
    }
}
