package org.example.simpleService;

import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repository.IRentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SimpleRentalService implements IRentalService {

    IRentalRepository rentalRepo;

    public SimpleRentalService(IRentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public Optional<Rental> findActiveRentalByUserId(String userID) {
        return rentalRepo.findByUserIdAndReturnDateIsNull(userID);
    }

    public List<Rental> findAllRentals() {
        return rentalRepo.findAll();
    }

    public Rental rentVehicle(User user, Vehicle vehicle) {
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(user.getId());
        if (r.isEmpty()) {
            Rental rental = Rental.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .user(user)
                    .vehicle(vehicle)
                    .rentDateTime(LocalDateTime.now().toString())
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
            r.get().setReturnDateTime(LocalDateTime.now().toString());
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
    public void deleteByVehicleId(String vehicleId) {
        rentalRepo.deleteByVehicleId(vehicleId);
    }
}
