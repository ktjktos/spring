package org.example.repository.impl.jpa;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class UserRepositoryJpaAdapter implements IUserRepository {

    private final UserJpaRepository delegate;

    @Override
    public User save(User user) {
        return delegate.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return delegate.findByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return delegate.findAll();
    }

    @Override
    public boolean deleteById(String id) {
        delegate.deleteById(id);
        return true;
    }
}