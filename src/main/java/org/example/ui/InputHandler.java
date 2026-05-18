package org.example.ui;

import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.model.VehicleCategoryConfig;
import org.example.service.IRentalService;
import org.example.service.IUserService;
import org.example.service.IVehicleService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class InputHandler {
    Scanner scanner;
    IVehicleService vehicleService;
    IRentalService rentalService;
    IUserService userService;

    public InputHandler(IVehicleService vehicleService, IRentalService rentalService, IUserService userService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.userService = userService;
        scanner = new Scanner(System.in);
    }

    public String readSingleChoice(String output, String... allowedWords) {
        while(true) {
            System.out.println(output);
            String input = scanner.nextLine().trim();

            for (String word : allowedWords) {
                if (input.equals(word)) {
                    return input;
                }
            }
            System.out.println("Niepoprawny wybor, sprobuj ponownie.");
        }
    }

    public String[] getMultipleStrings(String output, int amountOfWords) {
        while(true) {
            System.out.println(output);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Wartosc nie moze byc pusta.");
                continue;
            }
            String[] split = input.split(" ");
            if (split.length == amountOfWords) {
                return split;
            } else {
                System.out.println("Podano zla ilosc slow.");
            }
        }
    }

    public void displayUserRental(Optional<Rental> rental) {
        if (rental.isPresent()) {
            System.out.println("Active rentals:");
            System.out.println(vehicleService.findById(rental.get().getVehicleId()));
        } else {
            System.out.println("No active rentals.");
        }
    }


    public void displayUserRentals(List<Rental> rentals) { // to bd do historii
        int num = 1;
        try {
            System.out.println("Rental history:");
            for(Rental rental: rentals) {
                System.out.println(num + "| " + vehicleService.findById(rental.getVehicleId()));
                num++;
            }
        } catch (Exception e) {
            System.out.println("No active rentals.");
        }
    }

    public void displayVehicle(Vehicle vehicle) {
        StringBuilder sb = new StringBuilder();;
        sb.append(vehicle.getId()).append("| ").append(vehicle.getBrand()).append(" ").append(vehicle.getModel()).append(" ").append(vehicle.getYear()).append(" price: ").append(vehicle.getPrice()).append(" PLN");
        Map<String,Object> attributes = vehicle.getAttributes();
        if (!attributes.isEmpty()) {
            sb.append(" | Attributes: ").append(attributes);
        }
        System.out.println(sb);
    }
    public void displayAvailableVehicles(List<Vehicle> vehicles) {
        System.out.println("Available vehicles:");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available currently.");
        } else {
            for(Vehicle vehicle: vehicles) {
                displayVehicle(vehicle);
            }
        }
    }
    public void displayAllVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        }
        for(Vehicle vehicle: vehicles) {
            displayVehicle(vehicle);
            if (rentalService.vehicleHasActiveRental(vehicle.getId())) {
                System.out.println("^^^ CURRENTLY RENTED ^^^");
            } else {
                System.out.println("^^^ NOT RENTED ^^^");
            }

        }
    }

    public void displayAllUsersToRemove(List<User> users) {
        System.out.println("Users: ");
        for(User user: users) {
            System.out.println(user.getId() + " " + user.getLogin() + " " + user.getRole());
            displayUserRental(rentalService.findActiveRentalByUserId(user.getId()));
            System.out.println("");
        }
    }

    public void displayAllUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users are currently registered.");
        } else {
            System.out.println("Users: ");
            for(User user: users) {
                System.out.println(user.getLogin() + " " + user.getRole());
                displayUserRental(rentalService.findActiveRentalByUserId(user.getId()));
                System.out.println("");
            }
        }
    }
    public void displayAllUsersWithHistory(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users are currently registered.");
        } else {
            System.out.println("Users: ");
            for(User user: users) {
                System.out.println(user.getLogin() + " " + user.getRole());
                displayUserRentals(rentalService.findUserRentals(user.getId()));
                System.out.println("");
            }
        }
    }

    public void displayRequiredAttributes(List<VehicleCategoryConfig> attributes) {
        for (VehicleCategoryConfig config: attributes) {
            System.out.println(config);
        }
    }
}
