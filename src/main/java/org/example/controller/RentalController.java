package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.services.IRentalService;
import org.example.services.IUserService;
import org.example.services.IVehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final IRentalService rentalService;
    private final IUserService userService;
    private final IVehicleService vehicleService;

    @GetMapping
    public List<Rental> list() {
        return rentalService.findAllRentals();
    }

    @GetMapping("/users/{userId}")
    public List<Rental> userRentals(@PathVariable String userId) {
        return rentalService.findUserRentals(userId);
    }

    @PostMapping("/users/{userId}/rent/{vehicleId}")
    public Rental rent(@PathVariable String userId, @PathVariable String vehicleId) {
        User user = userService.findById(userId);
        Vehicle vehicle = vehicleService.findById(vehicleId).orElse(null);

        if (user != null && vehicle != null) {
            return rentalService.rentVehicle(user, vehicle);
        }
        throw new IllegalArgumentException("Couldn't find user or vehicle.");
    }

    @PostMapping("/users/{userId}/return")
    public Rental returnVehicle(@PathVariable String userId) {
        return rentalService.returnVehicle(userId);
    }
}