package org.example;


import org.example.hibernate.repository.RentalHibernateRepository;
import org.example.hibernate.repository.UserHibernateRepository;
import org.example.hibernate.repository.VehicleHibernateRepository;
import org.example.hibernate.service.AuthHibernateService;
import org.example.hibernate.service.RentalHibernateService;
import org.example.hibernate.service.UserHibernateService;
import org.example.hibernate.service.VehicleHibernateService;
import org.example.repository.*;
import org.example.simpleService.*;
import org.example.ui.InputHandler;
import org.example.ui.UIconsole;
import org.example.validator.VehicleValidator;

public class Main {
    public static void main(String[] args) {
        IVehicleCategoryConfigRepository configRepository = new VehicleCategoryConfigRepository();
        vehicleCategoryConfigService configService = new vehicleCategoryConfigService(configRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(configService);

        IVehicleRepository vehicleRepo;
        IUserRepository userRepo;
        IRentalRepository rentalRepo;

        IRentalService rentalService;
        IUserService userService;
        IVehicleService vehicleService;
        IAuthService authService;

        if (args[0].equals("jdbc")) {
            vehicleRepo = new VehicleJdbcRepository();
            userRepo = new UserJdbcRepository();
            rentalRepo = new RentalJdbcRepository(userRepo,vehicleRepo);

            rentalService = new SimpleRentalService(rentalRepo);
            userService = new SimpleUserService(userRepo, rentalService);
            vehicleService = new SimpleVehicleService(vehicleValidator, vehicleRepo, rentalService);
            authService = new SimpleAuthService(userService);
        } else if (args[0].equals("json")){
            vehicleRepo = new VehicleRepository();
            userRepo = new UserRepository();
            rentalRepo = new RentalRepository(userRepo,vehicleRepo);

            rentalService = new SimpleRentalService(rentalRepo);
            userService = new SimpleUserService(userRepo, rentalService);
            vehicleService = new SimpleVehicleService(vehicleValidator, vehicleRepo, rentalService);
            authService = new SimpleAuthService(userService);
        } else {
            vehicleRepo = new VehicleHibernateRepository();
            userRepo = new UserHibernateRepository();
            rentalRepo= new RentalHibernateRepository();

            rentalService = new RentalHibernateService((RentalHibernateRepository) rentalRepo, (VehicleHibernateRepository) vehicleRepo, (UserHibernateRepository) userRepo);
            userService = new UserHibernateService((UserHibernateRepository) userRepo, (RentalHibernateService) rentalService);
            vehicleService = new VehicleHibernateService((VehicleHibernateRepository) vehicleRepo, (RentalHibernateService) rentalService);
            authService = new AuthHibernateService((UserHibernateRepository) userRepo);
        }
            UIconsole console = UIconsole.builder()
                    .vehicleService(vehicleService)
                    .userService(userService)
                    .authService(authService)
                    .rentalService(rentalService)
                    .vehicleCategoryConfigService(configService)
                    .inputHandler(new InputHandler(vehicleService, rentalService, userService))
                    .build();
            console.run();
    }
}