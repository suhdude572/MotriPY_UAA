package com.example;

import java.sql.Timestamp;
import java.util.Base64;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private boolean isConfirmed;
    private boolean isDeleted;
    private String address;
    private String city;
    private String Telefono;
    private String resetToken;
    private Timestamp resetTokenExpiration;

    // Constructor, getters, setters, etc.

    public User(int userId, String firstName, String lastName, String email, byte[] passwordHash, byte[] passwordSalt, boolean isConfirmed, boolean isDeleted, String address, String city, String telefono) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = encodeBase64(passwordHash);
        this.passwordSalt = encodeBase64(passwordSalt);
        this.isConfirmed = isConfirmed;
        this.isDeleted = isDeleted;
        this.address = address;
        this.city = city;
        this.Telefono = telefono;
        //this.shippingAddress = shippingAddress;
    }

    public User() {

    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setaddress(String address) {
        this.address = address;
    }

    public String getaddress() {
        return address;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }


   /*  public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }*/

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPasswordHash() {
        return decodeBase64(passwordHash);
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = encodeBase64(passwordHash);
    }

    public byte[] getPasswordSalt() {
        return decodeBase64(passwordSalt);
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = encodeBase64(passwordSalt);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // Helper method to encode byte array to Base64 string
    private String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // Helper method to decode Base64 string to byte array
    private byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getResetToken() {
        return resetToken;
    }
    public void setResetTokenExpiration(Timestamp resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }
    public Timestamp getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public String getTelefono() {
        return Telefono;
    }
    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

}