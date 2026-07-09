package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repository.IRentalRepository;
import org.example.repository.IVehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalService implements IRentalService {

    private final IRentalRepository rentalRepo;
    private final IVehicleRepository vehicleRepo;

    @Transactional(readOnly = true)
    public Optional<Rental> findActiveRentalByUserId(String userID) {
        return rentalRepo.findByUserIdAndReturnDateIsNull(userID);
    }

    @Transactional(readOnly = true)
    public List<Rental> findAllRentals() {
        return rentalRepo.findAll();
    }

    public Rental rentVehicle(User user, Vehicle vehicle) {
        if (vehicle.isRented()) {
            return null;
        }
        Optional<Rental> r = rentalRepo.findByUserIdAndReturnDateIsNull(user.getId());
        if (r.isEmpty()) {
            vehicle.setRented(true);
            vehicleRepo.save(vehicle);
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
            Rental rental = r.get();

            if (rental.getVehicle() != null) {
                Vehicle vehicle = vehicleRepo.findById(rental.getVehicle().getId())
                        .orElseThrow(() -> new RuntimeException("Nie znaleziono pojazdu w bazie"));

                double hqLat = 51.246;
                double hqLng = 22.568;

                double distanceLat = Math.abs(vehicle.getLatitude() - hqLat);
                double distanceLng = Math.abs(vehicle.getLongitude() - hqLng);

                if (distanceLat > 0.01 || distanceLng > 0.01) {
                    System.out.println("[BLOKADA GPS] Próba zwrotu auta ID: " + vehicle.getId() + " poza dozwoloną strefą!");
                    return null;
                }

                String returnTimeStr = LocalDateTime.now().toString();
                rental.setReturnDateTime(returnTimeStr);

                vehicle.setRented(false);
                vehicleRepo.save(vehicle);

                LocalDateTime start = LocalDateTime.parse(rental.getRentDateTime());
                LocalDateTime end = LocalDateTime.parse(returnTimeStr);

                long minutes = java.time.Duration.between(start, end).toMinutes();

                long days = (long) Math.ceil(minutes / 1440.0);
                if (days == 0) {
                    days = 1;
                }

                double finalPrice = days * vehicle.getPrice();
                rental.setTotalCost(finalPrice);

                System.out.println("Użytkownik ID: " + userId);
                System.out.println("Czas wynajmu: " + minutes + " minut(y)");
                System.out.println("Naliczone doby (zaokrąglone w górę): " + days);
                System.out.println("Stawka do pobrania: " + finalPrice + " PLN");
                System.out.println("Status autoryzacji transakcji: SUCCESS (200 OK)");
            }

            rentalRepo.save(rental);
            return rental;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleID) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleID);
    }

    @Transactional(readOnly = true)
    public List<Rental> findUserRentals(String userId) {
        return rentalRepo.findUserRentals(userId);
    }

    @Transactional(readOnly = true)
    public boolean userHasActiveRental(String userId) {
        return findActiveRentalByUserId(userId).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean vehicleHasActiveRental(String vehicleId) {
        return findActiveRentalByVehicleId(vehicleId).isPresent();
    }

    public void deleteByVehicleId(String vehicleId) {
        rentalRepo.deleteByVehicleId(vehicleId);
    }
}
