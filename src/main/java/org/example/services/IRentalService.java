package org.example.services;


import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IRentalService {

    Rental rentVehicle(User user, Vehicle vehicle);

    Rental returnVehicle(String userId);

    Optional<Rental> findActiveRentalByUserId(String userId);

    List<Rental> findAllRentals();

    List<Rental> findUserRentals(String userId);

    boolean userHasActiveRental(String userId);

    boolean vehicleHasActiveRental(String vehicleId);

    Optional<Rental> findActiveRentalByVehicleId(String vehicleID);
    void deleteByVehicleId(String vehicleId);
}