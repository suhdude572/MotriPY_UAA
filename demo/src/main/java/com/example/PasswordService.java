package com.example;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {

    public PasswordResult hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainTextPassword, salt);
        return new PasswordResult(hashedPassword.getBytes(), salt.getBytes());
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword, String salt) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}