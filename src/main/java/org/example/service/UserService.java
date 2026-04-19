package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.Optional;

public class UserService {

    UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User save(User user) {
        return this.userRepo.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    public String displayCredentials(User user) {
        return user.getLogin() + " " + user.getRole();
    }
}
