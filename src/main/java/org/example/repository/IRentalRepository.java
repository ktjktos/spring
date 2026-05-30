package org.example.repository;

import org.example.model.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);
    Rental save(Rental rental);
    void deleteById(String id);
    Optional<Rental> findByUserIdAndReturnDateIsNull(String userId);
    Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId);
    List<Rental> findUserRentals(String userId);

    void deleteByVehicleId(String VehicleId);
}
