package org.example.services;

import org.example.dto.RegisterRequest;
import org.example.model.User;

import java.util.Optional;

public interface IAuthService {

    boolean register(RegisterRequest request);

    Optional<User> login(String login, String rawPassword);
}