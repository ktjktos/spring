package org.example.hibernate.repository;

import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repository.IUserRepository;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserHibernateRepository implements IUserRepository {

    Session session;
    @Override
    public List<User> findAll() {
        return session.createQuery("FROM User", User.class).list();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(session.get(User.class,id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return session.createQuery("FROM User WHERE login = :userLogin", User.class)
                .setParameter("userLogin", login)
                .uniqueResultOptional();
    }

    @Override
    public User save(User user) {
        return session.merge(user);
    }

    @Override
    public boolean deleteById(String id) {
        User user = session.get(User.class,id);

        if (user != null) {
            session.remove(user);
            return true;
        }
        return false;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
