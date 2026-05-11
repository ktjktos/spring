package org.example.service;

import org.example.model.User;

import java.util.Optional;

public interface IAuthService {

    boolean register(String login, String rawPassword);

    Optional<User> login(String login, String rawPassword);
}