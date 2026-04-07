package org.example;

import java.util.List;

public interface IUserRepository {
    User getUser(String login, String password);
    List<User> getUsers();
    void save();
    void load();
    void update(User user,String id);
}
