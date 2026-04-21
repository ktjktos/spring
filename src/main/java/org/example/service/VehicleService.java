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

    public Vehicle createVehicle (String[] split) {
        Vehicle vehicle = Vehicle.builder()
                .id(null)
                .typeOfVehicle(split[0])
                .brand(split[1])
                .model(split[2])
                .year(Integer.parseInt(split[3]))
                .plate(split[4])
                .price(Integer.parseInt(split[5]))
                .attributes(null)
                .build();
        return vehicle;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicleValidator.validate(vehicle);
        vehicleRepo.save(vehicle);
        return vehicle;
    }

    public void addAttributes(String attribute, String value, Vehicle vehicle) {
       vehicle.addAttribute(attribute,value);
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
