package org.example.ui;

import lombok.*;

import org.example.model.User;

import org.example.repository.RentalRepository;
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
            while(true){ // TODO: DOKONCZYC CZYSZCZENIE UICONSOLE
                choice = inputHandler.readSingleChoice("Possible options:\ninfo | show | rent | return | exit","info","show","rent","return","exit");
                switch (choice) {
                    case "info":
                        System.out.println(userService.displayCredentials(user));
                        System.out.println(rentalService.whatVehicleIsRented(user.getId()));
                        break;
                    case "show":
                        List<Vehicle> list = vehicleService.findAllVehicles();
                        System.out.println("Catalog:");
                        for(Vehicle vehicle: list) {
                            r = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicle.getId());
                            if (r.isEmpty()) {
                                System.out.println(vehicle);
                            }
                        }
                        break;
                    case "rent":
                        r = rentalRepo.findByUserIdAndReturnDateIsNull(user.getId());
                        if (r.isEmpty()) {
                            Rental rental = Rental.builder()
                                    .id(null)
                                    .userId(user.getId())
                                    .vehicleId(split[1])
                                    .rentDateTime(new Date())
                                    .returnDateTime(null)
                                    .build();
                            rentalRepo.save(rental);
                        }
                        break;
                    case "return":
                        r = rentalRepo.findByUserIdAndReturnDateIsNull(user.getId());
                        if (r.isPresent()) {
                            r.get().setReturnDateTime(new Date());
                            rentalRepo.save(r.get());
                        }
                        break;
                    case "exit":
                        return;
                }
            }
        }
        else if (user.getRole().equals("ADMIN")) {
            while(true){
                System.out.println("Possible options: \n" +
                        "show | add :TYPE_OF_VEHICLE;BRAND;MODEL;YEAR;PLATE;PRICE;(ATTRIBUTE;ATTRIBUTE2): | remove :id: | exit");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                switch (split[0]) {
                    case "show":
                        List<Vehicle> list = vehicleService.findAllVehicles();
                        System.out.println("Catalog:");
                        for(Vehicle vehicle: list) {
                            System.out.println(vehicle);
                        }
                        break;
                    case "add":
                        String[] sp = split[1].split(";");
                        Vehicle vehicle = Vehicle.builder()
                                    .id(null)
                                    .typeOfVehicle(sp[0])
                                    .brand(sp[1])
                                    .model(sp[2])
                                    .year(Integer.parseInt(sp[3]))
                                    .plate(sp[4])
                                    .price(Integer.parseInt(sp[5]))
                                    .attributes(null)
                                    .build();
                        if (sp.length > 6) {
                            int i = 6;
                            while (i+1 < sp.length) {
                                vehicle.addAttribute(sp[i],sp[i+1]);
                                i = i+2;
                            }
                        }
                        vehicleService.addVehicle(vehicle);
                        break;
                    case "remove":
                        r = rentalRepo.findByVehicleIdAndReturnDateIsNull(split[1]);
                        if (r.isEmpty()) {
                            vehicleService.deleteVehicleById(split[1]);
                        }
                        break;
                    case "exit":
                        return;
                }
            }
        }
    }
}
