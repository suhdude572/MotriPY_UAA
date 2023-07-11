package com.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.sql.Timestamp; 


public class TokenRepository {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=MotriPy;TrustServerCertificate=True";
    private static final String USER = "sa";
    private static final String PASS = "josias5726610";

    public void addPasswordResetToken(PasswordResetToken token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO PasswordResetTokens (TokenId, UserId, TokenString, ExpirationTime, IsUsed) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, token.getTokenId());
            stmt.setInt(2, token.getUserId());
            stmt.setString(3, token.getTokenString());
            stmt.setTimestamp(4, Timestamp.valueOf(token.getExpirationTime()));
            stmt.setBoolean(5, token.isUsed());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PasswordResetToken getPasswordResetTokenByString(String tokenString) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM PasswordResetTokens WHERE TokenString = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tokenString);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PasswordResetToken token = new PasswordResetToken();
                token.setTokenId(rs.getInt("TokenId"));
                token.setUserId(rs.getInt("UserId"));
                token.setTokenString(rs.getString("TokenString"));
                Timestamp expirationTime = rs.getTimestamp("ExpirationTime");
                token.setExpirationTime(expirationTime.toLocalDateTime());
                token.setUsed(rs.getBoolean("IsUsed"));
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePasswordResetToken(PasswordResetToken token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "UPDATE PasswordResetTokens SET UserId = ?, TokenString = ?, ExpirationTime = ?, IsUsed = ? WHERE TokenId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getTokenString());
            stmt.setTimestamp(3, Timestamp.valueOf(token.getExpirationTime()));
            stmt.setBoolean(4, token.isUsed());
            stmt.setInt(5, token.getTokenId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePasswordResetToken(PasswordResetToken token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM PasswordResetTokens WHERE TokenId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, token.getTokenId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addConfirmationToken(ConfirmationToken token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO ConfirmationTokens (UserId, TokenString, ExpirationTime, IsUsed) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getTokenString());
            stmt.setTimestamp(3, Timestamp.valueOf(token.getExpirationTime())); // Set the Timestamp value
            stmt.setBoolean(4, token.isUsed());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConfirmationToken getTokenByString(String tokenString) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM ConfirmationTokens WHERE TokenString = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tokenString);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ConfirmationToken token = new ConfirmationToken();
                token.setTokenId(rs.getInt("TokenId"));
                token.setUserId(rs.getInt("UserId"));
                token.setTokenString(rs.getString("TokenString"));
                Timestamp expirationTime = rs.getTimestamp("ExpirationTime"); // Get the Timestamp value
                token.setExpirationTime(expirationTime.toLocalDateTime()); // Convert to LocalDateTime
                token.setUsed(rs.getBoolean("IsUsed"));
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   public void updateToken(ConfirmationToken token) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        String query = "UPDATE ConfirmationTokens SET UserId = ?, TokenString = ?, ExpirationTime = ?, IsUsed = ? WHERE TokenId = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, token.getUserId());
        stmt.setString(2, token.getTokenString());
        stmt.setTimestamp(3, Timestamp.valueOf(token.getExpirationTime())); // Set the Timestamp value
        stmt.setBoolean(4, token.isUsed());
        stmt.setInt(5, token.getTokenId());
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void deleteConfirmationToken(ConfirmationToken token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM ConfirmationTokens WHERE TokenId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, token.getTokenId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserByToken(String token) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT UserId FROM ConfirmationTokens WHERE TokenString = ? AND IsUsed = 0";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserId");
                UserRepository userRepository = new UserRepository();
                return userRepository.getUserById(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}