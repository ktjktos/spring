package org.example.hibernate.repository;

import org.example.model.Rental;
import org.example.repository.IRentalRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RentalHibernateRepository implements IRentalRepository {

    private Session session;
    @Override
    public List<Rental> findAll() {
        return session.createQuery("FROM Rental",Rental.class).list();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.ofNullable(session.get(Rental.class,id));
    }

    @Override
    public Rental save(Rental rental) {
        return session.merge(rental);
    }

    @Override
    public void deleteById(String id) {
        Rental rental = session.get(Rental.class,id);

        if (rental != null) {
            session.remove(rental);
        }
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        Query<Rental> query = session.createQuery("""
                FROM Rental r
                WHERE r.user.id = :userId
                AND r.returnDateTime IS NULL
                """,Rental.class);
        query.setParameter("userId",userId);
        return query.uniqueResultOptional();
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        Query<Rental> query = session.createQuery("""
                FROM Rental r
                WHERE r.vehicle.id = :vehicleId
                AND r.returnDateTime IS NULL
                """,Rental.class);
        query.setParameter("vehicleId",vehicleId);
        return query.uniqueResultOptional();
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        return session.createQuery("""
                FROM Rental r
                WHERE r.user.id = :userId
                """,Rental.class).setParameter("userId",userId).list();
    }

    @Override
    public void deleteByVehicleId(String vehicleId) {
        Rental rental = session.get(Rental.class,vehicleId);

        if (rental != null) {
            session.remove(rental);
        }
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
