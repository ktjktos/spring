package org.example;

import lombok.RequiredArgsConstructor;
import org.example.model.Vehicle;
import org.example.repository.IVehicleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GpsTrackerScheduler {

    private final IVehicleRepository vehicleRepo;
    private final Random random = new Random();

    @Scheduled(fixedRate = 15000) //15s
    public void simulateGpsMovement() {
        List<Vehicle> vehicles = vehicleRepo.findAll();
        boolean changed = false;

        for (Vehicle vehicle : vehicles) {
            if (vehicle.isRented()) {
                double latDelta = (random.nextDouble() - 0.5) * 0.1;
                double lngDelta = (random.nextDouble() - 0.5) * 0.1;

                vehicle.setLatitude(vehicle.getLatitude() + latDelta);
                vehicle.setLongitude(vehicle.getLongitude() + lngDelta);
                vehicleRepo.save(vehicle);
                changed = true;

                System.out.println("[GPS TRACKER] Pojazd ID: " + vehicle.getId() + " zmienił pozycję na: "
                        + vehicle.getLatitude() + ", " + vehicle.getLongitude());
            }
        }
    }
}