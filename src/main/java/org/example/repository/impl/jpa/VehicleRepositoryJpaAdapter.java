package org.example.repository.impl.jpa;

import lombok.RequiredArgsConstructor;
import org.example.model.Vehicle;
import org.example.repository.IVehicleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class VehicleRepositoryJpaAdapter implements IVehicleRepository {

    private final VehicleJpaRepository delegate;

    @Override
    public List<Vehicle> findAll() {
        return delegate.findAll();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return delegate.save(vehicle);
    }

    @Override
    public void deleteById(String id) {
        delegate.deleteById(id);
    }
}