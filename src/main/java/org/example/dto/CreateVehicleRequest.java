package org.example.dto;

import java.util.Map;

public record CreateVehicleRequest (
        String typeOfVehicle,
        String brand,
        String model,
        int year,
        String plate,
        double price,
        Map<String, Object> attributes
) {}