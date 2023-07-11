package com.example;
import java.util.UUID;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;


public class RegistrationController extends AbstractVerticle {

    private UserService userService;
    private EmailService emailService;
    private PasswordService passwordService;

    public RegistrationController(UserService userService, EmailService emailService, Vertx vertx) {
        this.userService = userService;
        this.emailService = emailService;
        this.vertx = vertx;
        this.passwordService = new PasswordService();
        
    }

    @Override
public void start() {
    Router router = Router.router(vertx);

    // Configurar el manejador estático
    router.route("/*").handler(StaticHandler.create());

    // Route for user registration
    router.post("/register").handler(this::handleRegistration);
    
    // Route for validating user account
    router.get("/validate/:token").handler(this::handleValidation);

    // Route for getting user by user ID
    router.get("/user/:userId").handler(this::handleGetUser);

    // Route for getting logging in
    router.post("/login").handler(this::handleLogin);
    
    // Route for passwordManagement
    router.post("/password-reset-request").handler(this::handlePasswordResetRequest);
    router.post("/password-reset/:token").handler(this::handlePasswordReset);

    vertx.createHttpServer().requestHandler(router).listen(8080, result -> {
        if (result.succeeded()) {
            System.out.println("HTTP server started on port 8080");
        } else {
            System.err.println("Failed to start HTTP server");
            result.cause().printStackTrace();
        }
    });
}

    public void handleGetUser(RoutingContext context) {
        String userId = context.request().getParam("userId");
        User user = userService.findUserById(userId);
    
        if (user != null) {
            context.response().setStatusCode(200).end(Json.encodePrettily(user));
        } else {
            context.response().setStatusCode(404).end();
        }
    }
    public void handleValidation(RoutingContext context) {
        System.out.println("Handling validation");
        String token = context.request().getParam("token");
        System.out.println("Token: " + token);
    
        // Retrieve the user by the token
        User user = userService.findUserByToken(token);
    
        if (user == null) {
            // The token is invalid
            context.response().setStatusCode(400).end("Invalid token");
            return;
        }
    
        // Validate the user
        boolean success = userService.validateUser(user);
    
        if (success) {
            context.response().setStatusCode(200).end("User validated successfully");
        } else {
            context.response().setStatusCode(500).end("Could not validate user");
        }
    }
    public void handleRegistration(RoutingContext routingContext) {

        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");
    
        try {
            // Convert the JSON string to a JSON object
            JsonObject jsonData = new JsonObject(routingContext.getBodyAsString());
    
            // Extract the data from the JSON object
            String firstName = jsonData.getString("firstName");
            String lastName = jsonData.getString("lastName");
            String email = jsonData.getString("email");
            String password = jsonData.getString("password");
            String address = jsonData.getString("address");
            String city = jsonData.getString("city");
            String telefono= jsonData.getString("telefono");
    
            // Create a new User object with the form data
            User user = new User();
            user.setfirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setaddress(address);
            user.setCity(city);
            user.setTelefono(telefono);
            // Hash the password and set the hash and salt
            PasswordResult passwordResult = passwordService.hashPassword(password);
            user.setPasswordHash(passwordResult.getHash());
            user.setPasswordSalt(passwordResult.getSalt());
    
            try {
                // Register the user and get the userId
                int userId = userService.registerUser(user);
                
    
                // Create a response JSON object containing the user ID
                JsonObject responseData = new JsonObject();
                responseData.put("userId", userId);
    
                // Set the response status code and send the response
                response.setStatusCode(201)
                .putHeader("Content-Type", "application/json")
                .end(responseData.encode());
    
            }catch (IllegalArgumentException e) {
                String errorMessage = "Error: " + e.getMessage();
                response.setStatusCode(400)
                        .putHeader("Content-Type", "text/plain")
                        .end(errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatusCode(500).end("Internal Server Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(400).end("Invalid JSON format");
        }
    }
    public void handleLogin(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        String email = body.getString("email");
        String password = body.getString("password");
    HttpServerResponse response = routingContext.response();
    response.putHeader("content-type", "application/json");

    try {
        // Validar las credenciales del usuario
        User user = userService.findUserByEmail(email);
        if (user == null) {
            // El usuario no existe
            JsonObject errorResponse = new JsonObject()
                    .put("error", "Credenciales inválidas");
            response.setStatusCode(401).end(errorResponse.encode());
            return;
        }

        // Verificar la contraseña
        if (passwordService.checkPassword(password, new String(user.getPasswordHash()), new String(user.getPasswordSalt()))) {
            // Las credenciales son correctas, el inicio de sesión es exitoso
            
            // Create a response JSON object containing the user ID
            JsonObject successResponse = new JsonObject()
                    .put("message", "Inicio de sesión exitoso")
                    .put("userId", user.getUserId());

            response.setStatusCode(200).end(successResponse.encode());
        } else {
            // La contraseña es incorrecta
            JsonObject errorResponse = new JsonObject().put("error", "Credenciales inválidas");
            response.setStatusCode(401).end(errorResponse.encode());
        }
    } catch (Exception e) {
        e.printStackTrace();
        JsonObject errorResponse = new JsonObject().put("error", "Error interno del servidor");
        response.setStatusCode(500).end(errorResponse.encode());
    }
}
public void handlePasswordResetRequest(RoutingContext routingContext) {
    JsonObject requestBody = routingContext.getBodyAsJson();
    String email = requestBody.getString("email");

    HttpServerResponse response = routingContext.response();
    response.putHeader("content-type", "application/json");

    try {
        // Check if the user exists
        User user = userService.findUserByEmail(email);
        if (user == null) {
            JsonObject errorResponse = new JsonObject()
                    .put("error", "User not found");
            response.setStatusCode(404).end(errorResponse.encode());
            return;
        }

        // Generate a password reset token
        String resetToken = generateResetToken();

        // Save the reset token for the user
        userService.saveResetToken(user, resetToken);

        // Send the password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        JsonObject successResponse = new JsonObject()
                .put("message", "Reset link sent");
        response.setStatusCode(200).end(successResponse.encode());
    } catch (Exception e) {
        e.printStackTrace();
        JsonObject errorResponse = new JsonObject()
                .put("error", "Internal Server Error");
        response.setStatusCode(500).end(errorResponse.encode());
    }
}

private String generateResetToken() {
    // Generate a random token, you can use a library like UUID or any other method you prefer
    // Example using UUID:
    return UUID.randomUUID().toString();
}

public void handlePasswordReset(RoutingContext routingContext) {
    String resetToken = routingContext.request().getParam("token");

    HttpServerResponse response = routingContext.response();

    try {
        // Validate the reset token
        boolean isValidToken = validateResetToken(resetToken);

        if (isValidToken) {
            // Redirect to the new reset-password.html form with the token as a query parameter
            response.setStatusCode(302);
            response.putHeader("Location", "http://localhost:8080/reset-password.html?token=" + resetToken);
            response.end();
        } else {
            response.setStatusCode(400).end("Invalid reset token");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatusCode(500).end("Internal Server Error");
    }
}
private boolean validateResetToken(String resetToken) {
    // Retrieve the user record based on the reset token from your database or any other storage
    User user = userService.findUserByResetToken(resetToken);

    // Check if the user exists and the reset token is valid
    return user != null && user.getResetToken().equals(resetToken);
}
}