package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final IUserRepository userRepo;
    private final IRentalService rentalService;

    public User save(User user) {
        return this.userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    public void deleteUser(String id, String loggedUserId) {
        if (findById(loggedUserId).getRole().equals("ADMIN") && this.rentalService.findUserRentals(id).isEmpty()) {
            this.userRepo.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public User findById(String id) {
        return this.userRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }
}