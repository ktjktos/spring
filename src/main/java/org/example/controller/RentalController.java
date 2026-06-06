package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.RentalRequest;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.services.IRentalService;
import org.example.services.IUserService;
import org.example.services.IVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/rent")
    public ResponseEntity<Rental> rent(
            @RequestBody RentalRequest rentalRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono takiego usera!"));
        Vehicle vehicle = vehicleService.findById(rentalRequest.vehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono takiego pojazdu!"));
        Rental rental = rentalService.rentVehicle(user, vehicle);

        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnVehicle(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono takiego usera!"));
        rentalService.returnVehicle(user.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}