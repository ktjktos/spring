package org.example.service;

import org.example.model.Vehicle;

import java.util.List;

public interface IVehicleService {

    List<Vehicle> findAllVehicles();

    List<Vehicle> findAvailableVehicles();

    Vehicle findById(String id);

    Vehicle addVehicle(Vehicle vehicle);

    void removeVehicle(String vehicleId);

    boolean isVehicleRented(String vehicleId);
    boolean vehicleExists(String vehicleId);
}