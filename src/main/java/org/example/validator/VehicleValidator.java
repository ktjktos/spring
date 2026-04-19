package org.example.validator;

import org.example.model.VehicleCategoryConfig;
import org.example.model.Vehicle;
import org.example.service.VehicleCategoryConfigService;

import java.util.Map;

public class VehicleValidator {
    private final VehicleCategoryConfigService configService;

    public VehicleValidator(VehicleCategoryConfigService configService) {
        this.configService = configService;
    }

    public void validate(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("pojazd nie moze byc nullem");
        validateBaseFields(vehicle);
        validateAttributes(vehicle.getAttributes(),configService.getByCategory(vehicle.getTypeOfVehicle()));
    }

    public void validateBaseFields(Vehicle vehicle) {
        requireNonBlank(vehicle.getTypeOfVehicle(), "musi byc kategoria");
        requireNonBlank(vehicle.getBrand(), "musi byc marka");
        requireNonBlank(vehicle.getModel(), "musi byc model");
        requireNonBlank(vehicle.getPlate(), "musi byc numer rejestracyjny");
        if (vehicle.getYear() <= 0) throw new IllegalArgumentException("rok musi byc dodatni");
        if (vehicle.getPrice() < 0) throw new IllegalArgumentException("cena nie moze byc ujemna");
    }
    private void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateAttributes(Map<String,Object> actualAttributes, VehicleCategoryConfig config) {
        Map<String,String> expectedAttributes = config.getAttributes();
        for (String actualName: actualAttributes.keySet()) {
            if (!expectedAttributes.containsKey(actualName)) {
                throw new IllegalArgumentException("nieobslugiwany atrybut dla kategorii" + config.getCategory() + ": " + actualName);
            }
        }

        expectedAttributes.forEach((attrName,expectedType) -> {
            Object value = actualAttributes.get(attrName);
            if (value == null) {
                throw new IllegalArgumentException("Brak wymaganego atrybutu: " + attrName);
            }
            if (expectedType.equalsIgnoreCase("string") && value instanceof String str) {
                requireNonBlank(str,"atrybut " + attrName + " nie moze byc pusty");
            }
            boolean isValidType = switch (expectedType.toLowerCase()) {
                case "string" -> value instanceof String;
                case "number" -> value instanceof Number;
                case "boolean" -> value instanceof Boolean;
                case "integer" -> value instanceof Integer;
                default -> throw new IllegalArgumentException("nieobslugiwany typ w configu " + expectedType);
            };
            if (!isValidType) {
                throw new IllegalArgumentException("atrybut " + attrName + " musi byc typu " + expectedType);
            }
        });
    }
}
