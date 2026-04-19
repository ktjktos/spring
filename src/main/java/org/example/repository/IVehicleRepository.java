package org.example.repository;
import org.example.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleRepository {
    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);
}
