package org.example.simpleService;

import org.example.model.User;
import org.example.repository.IUserRepository;

import java.util.List;
import java.util.Optional;

public class SimpleUserService implements IUserService {

    IUserRepository userRepo;
    IRentalService rentalService;

    public SimpleUserService(IUserRepository userRepo, IRentalService rentalService) {
        this.userRepo = userRepo;
        this.rentalService = rentalService;
    }

    public User save(User user) {
        return this.userRepo.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    public void deleteUser(String id, String loggedUserId) {
        if (findById(loggedUserId).getRole().equals("ADMIN") && this.rentalService.findUserRentals(id).isEmpty()) {
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