package org.example.service;

import org.example.model.User;
import org.example.repository.IUserRepository;

import java.util.List;
import java.util.Optional;

public class UserService implements IUserService {

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

    public void deleteUser(String id, String loggedUserId) {
        if (findById(loggedUserId).getRole().equals("Admin")) {
            this.userRepo.deleteById(id);
        }
    }

    public User findById(String id) {
        return this.userRepo.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }
}