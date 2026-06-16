package org.example.services;

import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> findAllUsers();

    User findById(String id);

    void deleteUser(String id, String loggedUserId);

    User save(User user);

    Optional<User> findByLogin(String login);


}