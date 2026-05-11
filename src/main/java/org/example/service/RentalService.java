package org.example.service;

import org.example.model.Rental;
import org.example.repository.IRentalRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RentalService implements IRentalService {

    IRentalRepository rentalRepo;

    public RentalService(IRentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public Optional<Rental> findActiveRentalByUserId(String userID) {
        return rentalRepo.findByUserIdAndReturnDateIsNull(userID);
    }

    public List<Rental> findAllRentals() {
        return rentalRepo.findAll();
    }

    public Rental rentVehicle(String userID,String vehicleID) {
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(userID);
        if (r.isEmpty()) {
            Rental rental = Rental.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .userId(userID)
                    .vehicleId(vehicleID)
                    .rentDateTime(new Date())
                    .returnDateTime(null)
                    .build();
            rentalRepo.save(rental);
            return rental;
        }
        return null;
    }

    public Rental returnVehicle(String userId) {
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(userId);
        if (r.isPresent()) {
            r.get().setReturnDateTime(new Date());
            rentalRepo.save(r.get());
            return r.get();
        }
        return null;
    }

    public Optional<Rental> findActiveRentalByVehicleId(String vehicleID) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleID);
    }

    public List<Rental> findUserRentals(String userId) {
        return rentalRepo.findUserRentals(userId);
    }

    public boolean userHasActiveRental(String userId) {
        return findActiveRentalByUserId(userId).isPresent();
    }

    public boolean vehicleHasActiveRental(String vehicleId) {
        return findActiveRentalByVehicleId(vehicleId).isPresent();
    }
}
