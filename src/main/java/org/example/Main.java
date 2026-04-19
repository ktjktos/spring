package org.example;

import org.example.repository.*;
import org.example.service.*;
import org.example.ui.UIconsole;
import org.example.validator.VehicleValidator;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepo = new VehicleRepository();
        UserRepository userRepo = new UserRepository();
        RentalRepository rentalRepo = new RentalRepository();

        IVehicleCategoryConfigRepository configRepository = new VehicleCategoryConfigRepository();
        VehicleCategoryConfigService configService = new VehicleCategoryConfigService(configRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(configService);
        VehicleService vehicleService = new VehicleService(vehicleValidator,vehicleRepo);
        RentalService rentalService = new RentalService(rentalRepo,vehicleService);
        UserService userService = new UserService(userRepo);

        AuthService authService = new AuthService(userService);
        //TODO: UI nie moze wiedziec o repo, musi korzystac z service
        UIconsole console = UIconsole.builder()
                        .vehicleService(vehicleService)
                        .userService(userService)
                        .authService(authService)
                        .rentalService(rentalRepo)
                        .build();
        console.run();
    }
}