package com.example;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private EmailService emailService;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }
    public UserService() {
        
    }

    public void registerUser2(User user) {
        // Check if the user's first name is null or empty
        if (user.getfirstName() == null || user.getfirstName().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");}}
    
            public int registerUser(User user) {
                System.out.println("Debugging");
                // Check if the email is already registered
                if (userRepository.getUserByEmail(user.getEmail()) != null) {
                    throw new IllegalArgumentException("El correo electrónico ya está registrado");
                }
                
                // Create the user in the database and get the userId
                int userID = userRepository.addUser(user);
                
                // Check if the user was added successfully
                if (userID <= 0) {
                    throw new IllegalArgumentException("User not found after insertion");
                }
                
                ConfirmationToken confirmationToken = generateConfirmationToken(userID);
                    
                // Send the confirmation email
                emailService.sendConfirmationEmail(user.getEmail(), confirmationToken.getTokenString());
                return userID;
            }

    private ConfirmationToken generateConfirmationToken(int userId) {
        // Generate the confirmation token and set the expiration time
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUserId(userId);
        confirmationToken.setTokenString(generateTokenString());
        confirmationToken.setExpirationTime(generateExpirationTime());
        confirmationToken.setUsed(false);
    
        // Save the token in the database
        tokenRepository.addConfirmationToken(confirmationToken);
    
        return confirmationToken;
    }

    private String generateTokenString() {
        // Generar un UUID aleatorio
        UUID uuid = UUID.randomUUID();
    
        // Convertir el UUID a una cadena
        String tokenString = uuid.toString();
    
        // Devolver la cadena del token
        return tokenString;
    }

    
    private LocalDateTime generateExpirationTime() {
    // Get the current date and time
    LocalDateTime currentDateTime = LocalDateTime.now();

    // Add a duration of 24 hours
    LocalDateTime expirationDateTime = currentDateTime.plusHours(24);

    return expirationDateTime;
}
    public boolean validateUser(User user) {
        if (user != null) {
            user.setConfirmed(true);
            userRepository.updateUser(user);
            return true;
        }
        return false;
    }
    public User findUserByToken(String tokenString) {
        // Fetch the token from the database
        ConfirmationToken token = tokenRepository.getTokenByString(tokenString);
        
        // Check if the token exists and has not been used
        if (token == null || token.isUsed()) {
            return null;
        }
    
        // Mark the token as used
        token.setUsed(true);
        tokenRepository.updateToken(token);
    
        // Fetch the user associated with the token
        return userRepository.getUserById(token.getUserId());
    }

    public User findUserById(String userId) {
        try {
            int id = Integer.parseInt(userId);
            return userRepository.getUserById(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    public void saveResetToken(User user, String resetToken) {
        user.setResetToken(resetToken);
        userRepository.updateUser(user);
    }
    public User findUserByResetToken(String resetToken) {
        return userRepository.getUserByResetToken(resetToken);
    }
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
    public void clearResetToken(User user) {
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.updateUser(user);
    }
}
