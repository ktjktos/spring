package org.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository implements IUserRepository{

    List<User> users;
    public UserRepository() {
        load();
    }

    @Override
    public User getUser(String login, String password) {
        for (User user: users) {
            if (user.getLogin().equals(login) && DigestUtils.sha256Hex(password).equals(user.getPassword())) {
                return new User(user);
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> u = new ArrayList<>();
        for(User user: users){
            User newU = new User(user);
            u.add(newU);
        }
        return u;
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter("users.csv")){
            for (User user: users) {
                // admin;240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9;ADMIN;
                if (user.getRentedVehicleId() != null) {
                    writer.println(user.getLogin()+";"+user.getPassword()+";"+user.getRole()+";"+user.getRentedVehicleId());
                } else {
                    writer.println(user.getLogin()+";"+user.getPassword()+";"+user.getRole()+";");
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        this.users = new ArrayList<>();
        try {
            String path = "users.csv";
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] obj = line.split(";");
                User u = new User(obj[0],obj[1],obj[2],false);
                if (obj.length > 3 && !obj[3].isEmpty()) {
                    u.setRentedVehicleId(obj[3]);
                }
                users.add(u);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku.");
        }
    }

    @Override
    public void update(User user,String id) {
        for(User u: users) {
            if (u.getLogin().equals(user.getLogin())) {
                u.setRentedVehicleId(id);
                user.setRentedVehicleId(id);
                this.save();
                break;
            }
        }
    }
}
