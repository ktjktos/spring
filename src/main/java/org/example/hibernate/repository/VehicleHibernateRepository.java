package org.example.hibernate.repository;

import org.example.model.Rental;
import org.example.model.Vehicle;
import org.example.repository.IVehicleRepository;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class VehicleHibernateRepository implements IVehicleRepository {
    private Session session;
    @Override
    public List<Vehicle> findAll() {
        return session.createQuery("FROM Vehicle", Vehicle.class).list();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(session.get(Vehicle.class,id));
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return session.merge(vehicle);
    }

    @Override
    public void deleteById(String id) {
        Vehicle vehicle = session.get(Vehicle.class,id);

        if (vehicle != null) {
            session.remove(vehicle);
        }
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
