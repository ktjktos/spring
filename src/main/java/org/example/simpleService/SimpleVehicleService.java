package org.example.simpleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.*;
import org.example.model.Rental;
import org.example.repository.IVehicleRepository;
import org.example.validator.VehicleValidator;
import org.example.model.Vehicle;

@AllArgsConstructor

public class SimpleVehicleService implements IVehicleService {
    private VehicleValidator vehicleValidator;
    private IVehicleRepository vehicleRepo;
    private IRentalService rentalService;

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

    public Optional<Vehicle> findById(String id) {
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
