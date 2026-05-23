package org.example.simpleService;

import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor

public class SimpleAuthService implements IAuthService{

    IUserService simpleUserService;

    public boolean register(String login, String password) {
        User user = simpleUserService.save(User.builder()
                .id(java.util.UUID.randomUUID().toString())
                .login(login)
                .password(BCrypt.hashpw(password,BCrypt.gensalt()))
                .role("USER")
                .build());
        return user != null;
    }

    public Optional<User> login(String login, String password) {
        Optional<User> u = simpleUserService.findByLogin(login);
        if (u.isPresent()) {
            if (BCrypt.checkpw(password,u.get().getPassword()))  return u;
        }
        return Optional.empty();
    }

    public Optional<User> findByLogin(String login) {
        return this.simpleUserService.findByLogin(login);
    }
}