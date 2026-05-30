package org.example.hibernate.service;

import org.example.hibernate.HibernateConfig;
import org.example.hibernate.repository.VehicleHibernateRepository;
import org.example.model.Vehicle;
import org.example.simpleService.IVehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleHibernateService implements IVehicleService {

    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateService rentalService;

    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo, RentalHibernateService rentalService) {
        this.vehicleRepo = vehicleRepo;
        this.rentalService = rentalService;
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return findAllVehicles().stream()
                .filter(v -> !isVehicleRented(v.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id);
        }
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            Vehicle savedVehicle = vehicleRepo.save(vehicle);
            tx.commit();
            return savedVehicle;
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if (!isVehicleRented(vehicleId)) {
            Transaction tx = null;
            try (Session session = HibernateConfig.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                vehicleRepo.setSession(session);
                vehicleRepo.deleteById(vehicleId);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalService.vehicleHasActiveRental(vehicleId);
    }

    @Override
    public boolean vehicleExists(String vehicleId) {
        return findById(vehicleId).isPresent();
    }
}
