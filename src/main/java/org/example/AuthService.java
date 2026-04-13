package org.example;

import org.mindrot.jbcrypt.BCrypt;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor

public class AuthService {

    UserRepository userRepo;

    public String register(String login, String password) {
        userRepo.save(User.builder()
                .login(login)
                .password(BCrypt.hashpw(password,BCrypt.gensalt()))
                .role("USER")
                .build());
        return "Udalo sie dodac nowego uzytkownika.";
    }

    public User login(String login, String password) {
        Optional<User> u = userRepo.findByLogin(login);
        if (u.isPresent()) {
            if (BCrypt.checkpw(password,u.get().getPassword()))  return u.get();
        }
        return null;
    }
}
