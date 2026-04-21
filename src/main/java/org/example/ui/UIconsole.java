package org.example.ui;

import lombok.*;

import org.example.model.User;
import org.example.model.Vehicle;

import org.example.service.AuthService;
import org.example.service.RentalService;
import org.example.service.UserService;
import org.example.service.VehicleService;

import java.util.*;

@AllArgsConstructor
@Builder

public class UIconsole {

    VehicleService vehicleService;
    UserService userService;
    AuthService authService;
    RentalService rentalService;

    InputHandler inputHandler;
    User user = null;

    public void run() {
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
                choice = inputHandler.readSingleChoice("Possible options:\ninfo | show | rent | return | exit","info","show","rent","return","exit");
                switch (choice) {
                    case "info":
                        System.out.println(userService.displayCredentials(user));
                        System.out.println(rentalService.whatVehicleIsRented(user.getId()));
                        break;
                    case "show":
                        List<Vehicle> list = rentalService.getAvailableVehicles();
                        if (list.isEmpty()) {
                            System.out.println("No vehicles available currently.");
                        } else {
                            for(Vehicle vehicle: list) {System.out.println(vehicle);}
                        }
                        break;
                    case "rent":
                        multipleChoice = inputHandler.getMultipleStrings("Input ID of the car that you want to rent.",1);
                        if(rentalService.rent(user.getId(),multipleChoice[0])) {
                            System.out.println("Rented the car successfully.");
                        } else {
                            System.out.println("Couldn't rent the car.");
                        }
                        break;
                    case "return":
                        if (rentalService.returnVehicle(user.getId())) {
                            System.out.println("Vehicle returned successfully.");
                        } else {
                            System.out.println("Catalog: ");
                            System.out.println("No rentals to return.");
                        }

                        break;
                    case "exit":
                        return;
                }
            }
        }
        else if (user.getRole().equals("ADMIN")) {
            while(true){
                choice = inputHandler.readSingleChoice("Possible options: \n" +
                        "show | add | remove | exit","show","add","remove","exit");
                switch (choice) {
                    case "show":
                        List<Vehicle> list = rentalService.getAvailableVehicles();
                        if (list.isEmpty()) {
                            System.out.println("No vehicles available currently.");
                        } else {
                            System.out.println("Catalog: ");
                            for(Vehicle vehicle: list) {System.out.println(vehicle);}
                        }
                        break;
                    case "add":
                        Vehicle vehicle = vehicleService.createVehicle(inputHandler.getMultipleStrings("Please input these field separated by whitespace.\nTYPE_OF_VEHICLE BRAND MODEL YEAR PLATE PRICE",6));
                        choice = inputHandler.readSingleChoice("Do you want to add attributes? [Y/N]","Y","N");
                        if (choice.equals("Y")) {
                            multipleChoice = inputHandler.getMultipleStrings("How many attributes do you want to add? Number: ", 1);
                            int num = Integer.parseInt(multipleChoice[0]);
                            for (int i = 0; i<num; i++) {
                                multipleChoice = inputHandler.getMultipleStrings("Input attribute and a value separated by whitespace",2);
                                vehicleService.addAttributes(multipleChoice[0],multipleChoice[1],vehicle);
                            }
                        } else {
                            System.out.println("No attributes will be added.");
                        }
                        vehicleService.addVehicle(vehicle);
                        break;
                    case "remove":
                        multipleChoice = inputHandler.getMultipleStrings("Which vehicle do you want to delete? ID: ", 1);
                        if (rentalService.findByVehicleIdAndReturnDateIsNull(multipleChoice[0]).isEmpty()) {
                            vehicleService.deleteVehicleById(multipleChoice[0]);
                            System.out.println("Successfully deleted selected vehicle.");
                        } else {
                            System.out.println("Something went wrong while deleting selected vehicle.");
                        }
                        break;
                    case "exit":
                        return;
                }
            }
        }
    }
}
