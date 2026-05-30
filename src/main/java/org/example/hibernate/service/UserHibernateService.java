package org.example.hibernate.service;

import org.example.hibernate.HibernateConfig;
import org.example.hibernate.repository.UserHibernateRepository;
import org.example.model.User;
import org.example.simpleService.IUserService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UserHibernateService implements IUserService {

    private final UserHibernateRepository userRepo;
    private final RentalHibernateService rentalService;

    public UserHibernateService(UserHibernateRepository userRepo, RentalHibernateService rentalService) {
        this.userRepo = userRepo;
        this.rentalService = rentalService;
    }

    @Override
    public List<User> findAllUsers() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findAll();
        }
    }

    @Override
    public User findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findById(id).orElse(null);
        }
    }

    @Override
    public void deleteUser(String id, String loggedUserId) {
        User loggedUser = findById(loggedUserId);

        if (loggedUser == null || !"ADMIN".equals(loggedUser.getRole())) {
            throw new SecurityException("Only ADMIN role can delete other users.");
        }
        if (!rentalService.findUserRentals(id).isEmpty()) {
            throw new IllegalStateException("Can't delete the user (currently renting a vehicle).");
        }
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);

            boolean deleted = userRepo.deleteById(id);
            if (!deleted) {
                throw new IllegalArgumentException("No user found with given ID.");
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public User save(User user) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);
            User sUser = userRepo.save(user);
            tx.commit();
            return sUser;
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findByLogin(login);
        }
    }
}
