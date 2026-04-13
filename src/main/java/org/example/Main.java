package org.example;

public class Main {
    public static void main(String[] args) {
        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl();
        UserRepository userRepo = new UserRepository();
        AuthService authService = new AuthService(userRepo);
        RentalRepository rentalRepo = new RentalRepository();

        UIconsole console = UIconsole.builder()
                        .vehicleRepo(vehicleRepo)
                        .userRepo(userRepo)
                        .authService(authService)
                        .rentalRepo(rentalRepo)
                        .build();
        console.run();
    }
}