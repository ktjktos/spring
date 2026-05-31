package org.example.repository.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.model.User;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
}