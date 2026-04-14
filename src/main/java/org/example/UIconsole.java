package org.example;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Builder

public class UIconsole {

    VehicleService vehicleService;
    UserRepository userRepo;
    AuthService authService;
    RentalRepository rentalRepo;

    User user;
    Optional<Rental> r;
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Type \"login\" to login and \"register\" to register." );
            String choice = scanner.nextLine();
            if (choice.equals("register")) {
                System.out.println("Input login and password");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                Optional<User> u = userRepo.findByLogin(split[0]);
                if (u.isPresent()) {
                    System.out.println("This login already exists.");
                } else {
                    System.out.println(authService.register(split[0],split[1]));
                }
            } else if (choice.equals("login")) {
                System.out.println("Please input your login and password: ");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                user = authService.login(split[0],split[1]);
                if (user != null) {
                    System.out.println("Successful authentication.");
                    break;
                }  else {
                    System.out.println("Please try again.");
                }
            } else {
                System.out.println("try again");
            }
        }
        if (user.getRole().equals("USER")) {
            while(true){
                System.out.println("Possible options: \n" +
                        "info | show | rent :id: | return :id: | exit");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                switch (split[0]) {
                    case "info":
                        System.out.println(user.getLogin() + " " + user.getRole());
                        r = rentalRepo.findByUserIdAndReturnDateIsNull(user.getId());
                        if (r.isPresent()) {
                            System.out.println(vehicleService.findVehicleById(r.get().getVehicleId()));
                        } else {
                            System.out.println("brak wypozyczonego pojazdu");
                        }
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
                        "show | add :TYPE_OF_VEHICLE;BRAND;MODEL;YEAR;PLATE;PRICE;(ATTRIBUTE ATTRIBUTE2): | remove :id: | exit");
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
