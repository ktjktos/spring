package org.example;

public class Main {
    public static void main(String[] args) {
        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl();
        UserRepository userRepo = new UserRepository();
        RentalRepository rentalRepo = new RentalRepository();

        VehicleCategoryConfigRepository configRepository = new VehicleCategoryConfigJsonRepsitory();
        VehicleCategoryConfigService configService = new VehicleCategoryConfigService(configRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(configService);
        VehicleService vehicleService = new VehicleService(vehicleValidator,vehicleRepo);

        AuthService authService = new AuthService(userRepo);
        //TODO: UI nie moze wiedziec o repo, musi korzystac z service
        UIconsole console = UIconsole.builder()
                        .vehicleService(vehicleService)
                        .userRepo(userRepo)
                        .authService(authService)
                        .rentalRepo(rentalRepo)
                        .build();
        console.run();
    }
}