package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateVehicleRequest;
import org.example.model.Vehicle;
import org.example.services.IVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.example.model.Vehicle.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final IVehicleService vehicleService;

    @GetMapping
    public List<Vehicle> list(@RequestParam(name = "available", required = false, defaultValue = "false") boolean available) {
        if (available) {
            return vehicleService.findAvailableVehicles();
        }
        return vehicleService.findAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable String id) {
        return vehicleService.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Vehicle> create(@RequestBody CreateVehicleRequest request) {
        Vehicle newVehicle = builder()
                .id(java.util.UUID.randomUUID().toString())
                .typeOfVehicle(request.typeOfVehicle())
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .plate(request.plate())
                .price((int) request.price())
                .attributes(request.attributes())
                .rented(false)
                .latitude(51.246)
                .longitude(22.568)
                .build();

        Vehicle saved = vehicleService.addVehicle(newVehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleService.removeVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<String> updateLocation(
            @PathVariable String id,
            @RequestParam double lat,
            @RequestParam double lng) {

        Optional<Vehicle> vehicleOpt = vehicleService.findById(id);
        if (vehicleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lng);

        vehicleService.addVehicle(vehicle);

        return ResponseEntity.ok("Ręcznie zaktualizowano pozycję GPS pojazdu.");
    }
}