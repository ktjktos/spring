package org.example.repository;

import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByLogin(String login);
    User save(User user);
    void deleteById(String id);
}
