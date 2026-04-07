package org.example;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Scanner;

public class UIconsole {
    public void run() {
        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl();
        Scanner scanner = new Scanner(System.in);
        UserRepository userRepo = new UserRepository();
        Authentication auth = new Authentication(userRepo);
        User user;
        while(true) {
            System.out.println("Please input your login and password: ");
            String input = scanner.nextLine();
            String[] split = input.split(" ");
            user = auth.authenticate(split[0],split[1]);
            if (user != null) {
                System.out.println("Successful authentication.");
                break;
            }  else {
                System.out.println("Please try again.");
            }
        }
        if (user.getRole().equals("USER")) {
            while(true){
                auth.updateUserRepo(userRepo);
                System.out.println("Possible options: \n" +
                        "info | show | rent :id: | return :id: | exit");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                switch (split[0]) {
                    case "info":
                        System.out.println(user.getLogin() + " " + user.getRole());
                        if (user.getRentedVehicleId() != null) {
                            System.out.println(vehicleRepo.getVehicle(user.getRentedVehicleId()));
                        } else {
                            System.out.println("brak wypozyczonego pojazdu");
                        }
                        break;
                    case "show":
                        List<Vehicle> list = vehicleRepo.getVehicles();
                        System.out.println("Catalog:");
                        for(Vehicle vehicle: list) {
                            if (!vehicle.isRented()) {
                                System.out.println(vehicle);
                            }
                        }
                        break;
                    case "rent":
                        if (user.getRentedVehicleId() == null) {
                            boolean rented = vehicleRepo.rentVehicle(split[1]);
                            if (rented) {
                                userRepo.update(user,split[1]);
                            }
                        }
                        break;
                    case "return":
                        boolean returned = vehicleRepo.returnVehicle(split[1]);
                        if (returned) {
                            userRepo.update(user,null);
                        }
                        break;
                    case "exit":
                        vehicleRepo.save();
                        return;
                }
            }
        }
        else if (user.getRole().equals("ADMIN")) {
            while(true){
                auth.updateUserRepo(userRepo);
                System.out.println("Possible options: \n" +
                        "show | add :(CAR);BRAND;MODEL;YEAR;PRICE: | add :(MOTORCYCLE);BRAND;MODEL;YEAR;PRICE;CATEGORY: | remove :id: | showAll | exit");
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                switch (split[0]) {
                    case "show":
                        List<Vehicle> list = vehicleRepo.getVehicles();
                        System.out.println("Catalog:");
                        for(Vehicle vehicle: list) {
                            System.out.println(vehicle);
                        }
                        break;
                    case "add":
                        vehicleRepo.add(split[1]);
                        break;
                    case "remove":
                        Vehicle v = vehicleRepo.getVehicle(split[1]);
                        if (!v.isRented()) {
                            vehicleRepo.remove(split[1]);
                        }
                        break;
                    case "showAll":
                        List<User> users = userRepo.getUsers();
                        for(User u: users) {
                            if (u.getRentedVehicleId() != null) {
                                System.out.println(vehicleRepo.getVehicle(u.getRentedVehicleId()) + ";;;" + u.getLogin() + ";" + u.getRole());
                            }
                        }
                        break;
                    case "exit":
                        vehicleRepo.save();
                        return;
                }
            }
        }
    }
}
