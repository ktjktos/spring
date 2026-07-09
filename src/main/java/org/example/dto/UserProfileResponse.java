package org.example.dto;

import org.example.model.Rental;
import java.util.List;

public record UserProfileResponse(
        String id,
        String login,
        String address,
        String role,
        List<Rental> rentals
) {}