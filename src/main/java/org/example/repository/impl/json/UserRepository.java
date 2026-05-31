package org.example.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.User;
import org.example.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Repository
@Profile("json")
public class UserRepository implements IUserRepository {

    private List<User> users;
    private final Path path;

    public UserRepository(@Value("${example.json.users-file}") String fileName) {
        this.path = Path.of(fileName);
        users = new ArrayList<>();
        try {
            if (Files.notExists(path)) {
                Files.writeString(path,"[]");
            }
            String json = Files.readString(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            users = gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w ladowaniu userRepo");
        }
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> u = new ArrayList<>();
        for(User user: users){
            User newU = user.copy();
            u.add(newU);
        }
        return u;
    }

    public User save(User user) {
        users.remove(user);
        users.add(user);
        this.writeToFile();
        return user;
    }


    public Optional<User> findByLogin(String login) {
        for(User user: users) {
            if (user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(String id) {
        for(User user: users) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    public boolean deleteById (String id) {
        for(User user: users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                this.writeToFile();
                return true;
            }
        }
        return false;
    }
    public void writeToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(users);
            Files.writeString(path, json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w zapisywaniu userRepo");
        }
    }

}
