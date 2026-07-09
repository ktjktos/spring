package org.example.services;

import org.example.dto.RegisterRequest;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthService{

    private final IUserService simpleUserService;

    public boolean register(RegisterRequest request) {
        User user = simpleUserService.save(User.builder()
                .id(java.util.UUID.randomUUID().toString())
                .login(request.login())
                .password(BCrypt.hashpw(request.password(), BCrypt.gensalt()))
                .address(request.address())
                .role("USER")
                .build());
        return user != null;
    }

    @Transactional(readOnly = true)
    public Optional<User> login(String login, String password) {
        Optional<User> u = simpleUserService.findByLogin(login);
        if (u.isPresent()) {
            if (BCrypt.checkpw(password,u.get().getPassword()))  return u;
        }
        return Optional.empty();
    }
    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.simpleUserService.findByLogin(login);
    }
}