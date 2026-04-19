package org.example.service;

import java.util.List;
import java.util.Optional;
import lombok.*;
import org.example.validator.VehicleValidator;
import org.example.model.Vehicle;
import org.example.repository.VehicleRepository;

@AllArgsConstructor

public class VehicleService {
    private VehicleValidator vehicleValidator;
    private VehicleRepository vehicleRepo;

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicleValidator.validate(vehicle);
        vehicleRepo.save(vehicle);
        return vehicle;
    }
    public List<Vehicle> findAllVehicles() {
       return vehicleRepo.findAll();
    }

    public void deleteVehicleById(String id) {
        vehicleRepo.deleteById(id);
    }

    public Optional<Vehicle> findVehicleById(String id) {
        return vehicleRepo.findById(id);
    }
}
