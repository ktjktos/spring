package org.example.repository.impl.jpa;

import lombok.RequiredArgsConstructor;
import org.example.model.Rental;
import org.example.repository.IRentalRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class RentalRepositoryJpaAdapter implements IRentalRepository {

    private final RentalJpaRepository delegate;

    @Override
    public Rental save(Rental rental) {
        return delegate.save(rental);
    }

    @Override
    public List<Rental> findAll() {
        return delegate.findAll();
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        return delegate.findByUser_IdAndReturnDateTimeIsNull(userId);
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return delegate.findByVehicle_IdAndReturnDateTimeIsNull(vehicleId);
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        return delegate.findByUser_Id(userId);
    }

    @Override
    public void deleteByVehicleId(String vehicleId) {
        delegate.deleteByVehicle_Id(vehicleId);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public void deleteById(String id) {
        delegate.deleteById(id);
    }
}