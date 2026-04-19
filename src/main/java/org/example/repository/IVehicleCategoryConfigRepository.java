package org.example.repository;

import org.example.model.VehicleCategoryConfig;

import java.util.List;
import java.util.Optional;

public interface IVehicleCategoryConfigRepository {
    List<VehicleCategoryConfig> findAll();
    Optional<VehicleCategoryConfig> findByCategory(String category);
}
