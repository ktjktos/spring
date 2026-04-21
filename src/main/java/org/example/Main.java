package org.example;

import org.example.repository.*;
import org.example.service.*;
import org.example.ui.InputHandler;
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
        UserService userService = new UserService(userRepo);
        RentalService rentalService = new RentalService(rentalRepo,vehicleService,userService);

        AuthService authService = new AuthService(userService);

        UIconsole console = UIconsole.builder()
                .vehicleService(vehicleService)
                .userService(userService)
                .authService(authService)
                .rentalService(rentalService)
                .inputHandler(new InputHandler())
                .build();
        console.run();
    }
}