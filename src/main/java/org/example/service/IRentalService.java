package org.example.service;


import org.example.model.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalService {

    Rental rentVehicle(String userId, String vehicleId);

    Rental returnVehicle(String userId);

    Optional<Rental> findActiveRentalByUserId(String userId);

    List<Rental> findAllRentals();

    List<Rental> findUserRentals(String userId);

    boolean userHasActiveRental(String userId);

    boolean vehicleHasActiveRental(String vehicleId);

    Optional<Rental> findActiveRentalByVehicleId(String vehicleID);
}