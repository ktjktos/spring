package org.example;

import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {
    IUserRepository userRepo;

    public Authentication(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public User authenticate(String login, String password) {
        return userRepo.getUser(login,password);
    }
    public void updateUserRepo(UserRepository userRepo) {this.userRepo = userRepo;}
}
