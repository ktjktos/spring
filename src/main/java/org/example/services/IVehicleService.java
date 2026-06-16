package org.example.services;

import org.example.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleService {

    List<Vehicle> findAllVehicles();

    List<Vehicle> findAvailableVehicles();

    Optional<Vehicle> findById(String id);

    Vehicle addVehicle(Vehicle vehicle);

    void removeVehicle(String vehicleId);

    boolean isVehicleRented(String vehicleId);
    boolean vehicleExists(String vehicleId);
}