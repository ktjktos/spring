package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class UserRepository implements IUserRepository{

    List<User> users;
    Set<Integer> existingIDs = new HashSet<>();

    public UserRepository() {
        Path path = Path.of("users.json");
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
        if (this.users != null) {
            for (User u : users) {
                existingIDs.add(Integer.parseInt(u.getId()));
            }
        } else {
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
        int id = 1;
        if (user.getId() == null) {
            while(existingIDs.contains(id)) { id+=1;}
            existingIDs.add(id);
            user.setId(Integer.toString(id));
            users.add(user);
        } else {
            users.remove(user);
            users.add(user);
        }
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
    public void deleteById (String id) {
        for(User user: users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                existingIDs.remove(Integer.parseInt(id));
                this.writeToFile();
                break;
            }
        }
    }
    public void writeToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(users);
            Files.writeString(Path.of("users.json"), json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w zapisywaniu userRepo");
        }
    }

}
