package org.example.service;

import org.example.model.User;
import org.example.repository.IUserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    IUserRepository userRepo;

    public UserService(IUserRepository userRepo) {
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

    public boolean deleteUser(String id) {
        return this.userRepo.deleteById(id);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }
}
