package com.example;

public class PasswordResult {
    private byte[] hash;
    private byte[] salt;

    public PasswordResult(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }
}