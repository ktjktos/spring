package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.*;
import org.example.model.Rental;
import org.example.repository.IVehicleRepository;
import org.example.validator.VehicleValidator;
import org.example.model.Vehicle;

@AllArgsConstructor

public class VehicleService implements IVehicleService {
    private VehicleValidator vehicleValidator;
    private IVehicleRepository vehicleRepo;
    private IRentalService rentalService;

    public Vehicle createVehicle (String[] split) {
        Vehicle vehicle = Vehicle.builder()
                .id(java.util.UUID.randomUUID().toString())
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

    public Vehicle findById(String id) {
        return vehicleRepo.findById(id);
    }

    public List<Vehicle> findAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle: vehicleRepo.findAll()) {
            Optional<Rental> rental = rentalService.findActiveRentalByVehicleId(vehicle.getId());
            if (rental.isEmpty()) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    public void removeVehicle(String vehicleId) {
        if(!isVehicleRented((vehicleId))) {
            vehicleRepo.deleteById(vehicleId);
        }
    }

    public boolean isVehicleRented(String vehicleId) {
        return rentalService.findActiveRentalByVehicleId(vehicleId).isPresent();
    }

    public boolean vehicleExists(String vehicleId) {
        return vehicleRepo.findById(vehicleId) != null;
    }
}
