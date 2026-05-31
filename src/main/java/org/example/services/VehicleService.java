package org.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.*;
import org.example.model.Rental;
import org.example.repository.IVehicleRepository;
import org.example.validator.VehicleValidator;
import org.example.model.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService implements IVehicleService {
    private final VehicleValidator vehicleValidator;
    private final IVehicleRepository vehicleRepo;
    private final IRentalService rentalService;

    public Vehicle addVehicle(Vehicle vehicle) {
        boolean plateExists = this.findAllVehicles().stream()
                .anyMatch(v -> v.getPlate().equalsIgnoreCase(vehicle.getPlate()));
        if (plateExists) {
            throw new IllegalStateException("pojazd z rejestracja " + vehicle.getPlate() + " juz istnieje w systemie!");
        }
        vehicle.setId(java.util.UUID.randomUUID().toString());
        vehicleValidator.validate(vehicle);
        vehicleRepo.save(vehicle);
        return vehicle;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAllVehicles() {
        return vehicleRepo.findAll();
    }

    public void deleteVehicleById(String id) {
        vehicleRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> findById(String id) {
        return vehicleRepo.findById(id);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public boolean isVehicleRented(String vehicleId) {
        return rentalService.findActiveRentalByVehicleId(vehicleId).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean vehicleExists(String vehicleId) {
        return vehicleRepo.findById(vehicleId).isPresent();
    }
}
