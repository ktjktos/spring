package org.example.repository.impl.jpa;

import org.example.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, String> {

}