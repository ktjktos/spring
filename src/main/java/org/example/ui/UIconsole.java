package org.example.ui;

import lombok.*;

import org.example.model.User;
import org.example.model.Vehicle;

import org.example.model.VehicleCategoryConfig;
import org.example.repository.RentalRepository;
import org.example.service.*;

import java.util.*;

@AllArgsConstructor
@Builder

public class UIconsole {

    VehicleCategoryConfigService vehicleCategoryConfigService;
    VehicleService vehicleService;
    UserService userService;
    AuthService authService;
    RentalService rentalService;

    InputHandler inputHandler;
    User user = null;

    public void run() { // TODO: bugfixowac konsole
        String[] multipleChoice;
        String choice;
        loginLoop:
        while(true) {
            choice = inputHandler.readSingleChoice("Type \"login\" to login and \"register\" to register.","login","register");
            multipleChoice = inputHandler.getMultipleStrings("Input your login and password",2);
            switch(choice) {
                case "register":
                    if (authService.findByLogin(multipleChoice[0]).isPresent()) {
                        System.out.println("This login already exists.");
                    } else {
                        authService.register(multipleChoice[0],multipleChoice[1]);
                    }
                    break;
                case "login":
                    user = authService.login(multipleChoice[0],multipleChoice[1]);
                    if (user != null) {
                        System.out.println("Successful authentication.");
                        break loginLoop;
                    }  else {
                        System.out.println("Please try again.");
                    }
                    break;
            }
        }
        if (user.getRole().equals("USER")) {
            while(true){
                choice = inputHandler.readSingleChoice("Possible options:\ninfo | show | rent | return | showRentalHistory | exit","info","show","rent","return","showRentalHistory","exit");
                switch (choice) {
                    case "info":
                        System.out.println("ID: " + user.getId() + "\nLogin: " + user.getLogin());
                        inputHandler.displayUserRental(rentalService.findActiveRentalByUserId(user.getId()));
                        break;
                    case "show":
                        inputHandler.displayAvailableVehicles(vehicleService.findAvailableVehicles());
                        break;
                    case "rent":
                        multipleChoice = inputHandler.getMultipleStrings("Input ID number of the car that you want to rent.",1);
                        if (!vehicleService.isVehicleRented(multipleChoice[0])) {
                            if(rentalService.rentVehicle(user.getId(),multipleChoice[0]) != null) {
                                System.out.println("Rented the car successfully.");
                            } else {
                                System.out.println("Couldn't rent the car.");
                            }
                        } else {
                            System.out.println("Couldn't rent the car.");
                        }
                        break;
                    case "return":
                        if (rentalService.returnVehicle(user.getId()) != null) {
                            System.out.println("Vehicle returned successfully.");
                        } else {
                            System.out.println("Couldn't return the car.");
                        }
                        break;
                    case "showRentalHistory":
                        System.out.println("ID: " + user.getId() + "\nLogin: " + user.getLogin());
                        inputHandler.displayUserRentals(rentalService.findUserRentals(user.getId()));
                        break;
                    case "exit":
                        return;
                }
            }
        }
        else if (user.getRole().equals("ADMIN")) {
            while(true){
                choice = inputHandler.readSingleChoice("Possible options: \n" +
                        "showVehicles | showUsers | add | removeVehicle | removeUser | showUserRentalHistory | exit","showVehicles","showUsers","add","removeVehicle","removeUser","showUserRentalHistory","exit");
                switch (choice) {
                    case "showVehicles":
                        inputHandler.displayAllVehicles(vehicleService.findAllVehicles());
                        break;
                    case "showUsers":
                        inputHandler.displayAllUsers(userService.findAllUsers());
                        break;
                    case "add":
                        Vehicle vehicle = vehicleService.createVehicle(inputHandler.getMultipleStrings("Please input these fields separated by whitespace.\nTYPE_OF_VEHICLE BRAND MODEL YEAR PLATE PRICE" +
                                "\nExample: Car Chevrolet Impala 1967 KAZ2Y5 1000",6));
                        inputHandler.displayRequiredAttributes(vehicleCategoryConfigService.findAllCategories()); //TODO: BUGFIX
                        while(true) {
                            multipleChoice = inputHandler.getMultipleStrings("Input attribute and a value separated by whitespace",2);
                            vehicleService.addAttributes(multipleChoice[0],multipleChoice[1],vehicle);
                            choice = inputHandler.readSingleChoice("Do you want to add more attributes? [Y/N]","Y","N");
                            if (choice.equals("N")) {
                                try {
                                    vehicleService.addVehicle(vehicle);
                                    break;
                                }
                                catch(Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Wrong attributes! try again.");
                                    vehicle.clearAttributes();
                                }
                            }
                        }
                        break;
                    case "removeVehicle":
                        inputHandler.displayAllVehicles(vehicleService.findAllVehicles());
                        multipleChoice = inputHandler.getMultipleStrings("Which vehicle do you want to delete? ID: ", 1);
                        vehicleService.removeVehicle(multipleChoice[0]);
                        break;
                    case "removeUser":
                        inputHandler.displayAllUsersToRemove(userService.findAllUsers());
                        multipleChoice = inputHandler.getMultipleStrings("Which user do you want to delete? ID: ", 1);
                        userService.deleteUser(multipleChoice[0],user.getId());
                        break;
                    case "showUserRentalHistory":
                        inputHandler.displayAllUsersWithHistory(userService.findAllUsers());
                        break;
                    case "exit":
                        return;
                }
            }
        }
    }
}
