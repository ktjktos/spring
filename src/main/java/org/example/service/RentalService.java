package org.example.service;

import org.example.model.Rental;
import org.example.repository.RentalRepository;
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
}
