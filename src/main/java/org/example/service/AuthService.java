package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor

public class AuthService {

    UserService userService;

    public User register(String login, String password) {
        return userService.save(User.builder()
                .id(java.util.UUID.randomUUID().toString())
                .login(login)
                .password(BCrypt.hashpw(password,BCrypt.gensalt()))
                .role("USER")
                .build());
    }

    public User login(String login, String password) {
        Optional<User> u = userService.findByLogin(login);
        if (u.isPresent()) {
            if (BCrypt.checkpw(password,u.get().getPassword()))  return u.get();
        }
        return null;
    }

    public Optional<User> findByLogin(String login) {
        return this.userService.findByLogin(login);
    }
}
