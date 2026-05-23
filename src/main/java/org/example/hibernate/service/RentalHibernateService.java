package org.example.hibernate.service;

import org.example.hibernate.HibernateConfig;
import org.example.hibernate.repository.RentalHibernateRepository;
import org.example.hibernate.repository.UserHibernateRepository;
import org.example.hibernate.repository.VehicleHibernateRepository;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.simpleService.IRentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements IRentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo,
                                  VehicleHibernateRepository vehicleRepo,
                                  UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Rental rentVehicle(User userr, Vehicle vehiclee) {
        String userId = userr.getId();
        String vehicleId = vehiclee.getId();
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            setSession(session);

            boolean userHasActiveRental = rentalRepo.findAll().stream()
                    .anyMatch(r -> userId.equals(r.getUserId()) && r.isActive());
            if (userHasActiveRental) {
                throw new IllegalStateException("You already have an active rental.");
            }
            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Couldn't find vehicle with the given id."));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Couldn't find user with the given id."));
            boolean vehicleIsRented = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicle.getId()).isPresent();
            if (vehicleIsRented) {
                throw new IllegalStateException("This vehicle is already rented.");
            }
            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDateTime(LocalDateTime.now().toString())
                    .returnDateTime(null)
                    .build();
            Rental savedRental = rentalRepo.save(rental);
            tx.commit();

            return savedRental;
        } catch (RuntimeException e) {
            rollback(tx);
            throw e;
        }
    }

    @Override
    public Rental returnVehicle(String userId) {
        Transaction tx = null;

        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            setSession(session);

            Rental rental = rentalRepo.findAll().stream()
                    .filter(r -> userId.equals(r.getUserId()))
                    .filter(Rental::isActive)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No car currently rented."));
            rental.setReturnDateTime(LocalDateTime.now().toString());
            Rental savedRental = rentalRepo.save(rental);
            tx.commit();
            return savedRental;
        } catch (RuntimeException e) {
            rollback(tx);
            throw e;
        }
    }

    @Override
    public Optional<Rental> findActiveRentalByUserId(String userId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            setSession(session);

            return rentalRepo.findAll().stream()
                    .filter(r -> userId.equals(r.getUserId()))
                    .filter(Rental::isActive)
                    .findFirst();
        }
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            setSession(session);

            return rentalRepo.findAll().stream()
                    .filter(r -> vehicleId.equals(r.getVehicleId()))
                    .filter(Rental::isActive)
                    .findFirst();
        }
    }

    @Override
    public void deleteByVehicleId(String vehicleId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            setSession(session);
            boolean vehicleIsRented = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
            if (vehicleIsRented) {
                throw new IllegalStateException("Can't delete, as this vehicle is rented.");
            }
            rentalRepo.deleteByVehicleId(vehicleId);
            tx.commit();
        } catch (RuntimeException e){
            rollback(tx);
            throw e;
        }
    }

    @Override
    public List<Rental> findAllRentals() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            setSession(session);

            return rentalRepo.findAll();
        }
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            setSession(session);

            return rentalRepo.findAll().stream()
                    .filter(r -> userId.equals(r.getUserId()))
                    .toList();
        }
    }

    @Override
    public boolean userHasActiveRental(String userId) {
        return findActiveRentalByUserId(userId).isPresent();
    }

    @Override
    public boolean vehicleHasActiveRental(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            setSession(session);

            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    private void setSession(Session session) {
        rentalRepo.setSession(session);
        vehicleRepo.setSession(session);
        userRepo.setSession(session);
    }

    private void rollback(Transaction tx) {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }
}
