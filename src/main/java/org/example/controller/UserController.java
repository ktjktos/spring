package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserProfileResponse;
import org.example.model.Rental;
import org.example.model.User;
import org.example.services.IRentalService;
import org.example.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IRentalService rentalService;

    @GetMapping
    public List<User> list() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        String currentLogin = authentication.getName();

        User user = userService.findByLogin(currentLogin)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zalogowanego użytkownika"));
        List<Rental> userRentals = rentalService.findUserRentals(user.getId());
        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getLogin(),
                user.getAddress(),
                user.getRole(),
                userRentals
        );

        return ResponseEntity.ok(profile);
    }
}