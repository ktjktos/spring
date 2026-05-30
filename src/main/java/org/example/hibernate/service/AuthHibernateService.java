package org.example.hibernate.service;

import org.example.hibernate.HibernateConfig;
import org.example.hibernate.repository.UserHibernateRepository;
import org.example.model.User;
import org.example.simpleService.IAuthService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

public class AuthHibernateService implements IAuthService {

    private final UserHibernateRepository userRepo;

    public AuthHibernateService(UserHibernateRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean register(String login, String rawPassword) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);
            if (userRepo.findByLogin(login).isPresent()) {
                return false;
            }

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(login)
                    .password(BCrypt.hashpw(rawPassword, BCrypt.gensalt()))
                    .role("USER")
                    .build();

            userRepo.save(user);
            tx.commit();
            return true;

        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            Optional<User> u = userRepo.findByLogin(login);

            if (u.isPresent()) {
                User user = u.get();
                if (BCrypt.checkpw(rawPassword, user.getPassword())) {
                    return Optional.of(user);
                }
            }

            return Optional.empty();
        }
    }
}
