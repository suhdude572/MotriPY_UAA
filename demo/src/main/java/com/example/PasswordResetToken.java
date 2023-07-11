package com.example;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PasswordResetToken {
    private int tokenId;
    private int userId;
    private String tokenString;
    private LocalDateTime expirationTime;
    private boolean isUsed;

    // constructor, getters, setters, etc.

    public PasswordResetToken(int tokenId, int userId, String tokenString, LocalDateTime expirationTime, boolean isUsed) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.tokenString = tokenString;
        this.expirationTime = expirationTime;
        this.isUsed = isUsed;
    }
    public PasswordResetToken() {
        
    }

    // Getters and Setters
    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
    public void setExpirationTime(Timestamp timestamp) {
        
    }
    
}