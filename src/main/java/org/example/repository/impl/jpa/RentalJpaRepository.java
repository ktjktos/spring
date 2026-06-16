package org.example.repository.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.model.Rental;
import java.util.List;
import java.util.Optional;

public interface RentalJpaRepository extends JpaRepository<Rental, String> {
    Optional<Rental> findByUser_IdAndReturnDateTimeIsNull(String userId);
    Optional<Rental> findByVehicle_IdAndReturnDateTimeIsNull(String vehicleId);
    List<Rental> findByUser_Id(String userId);
    void deleteByVehicle_Id(String vehicleId);
}