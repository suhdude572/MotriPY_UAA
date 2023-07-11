package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;

public class Application extends AbstractVerticle {

    private UserService userService;
    private EmailService emailService;

    public Application() {
        UserRepository userRepository = new UserRepository();
        TokenRepository tokenRepository = new TokenRepository();
        this.emailService = new EmailService();
        this.userService = new UserService(userRepository, tokenRepository, emailService);
    }

    @Override
    public void start() {
        // Create an instance of the Router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
        .allowedMethod(HttpMethod.GET)
        .allowedMethod(HttpMethod.POST)
        .allowedMethod(HttpMethod.OPTIONS)
        .allowedHeader("Content-Type"));

        // Create an instance of the RegistrationController and configure it with the router
        RegistrationController registrationController = new RegistrationController(userService, emailService, vertx);

        // Configure the routes for the RegistrationController
        router.post("/register").handler(registrationController::handleRegistration);
        router.get("/validate/:token").handler(registrationController::handleValidation);
        router.get("/user/:userId").handler(registrationController::handleGetUser); 
        router.post("/login").handler(registrationController::handleLogin);  
        router.post("/password-reset-request").handler(registrationController::handlePasswordResetRequest);
        router.post("/password-reset/:token").handler(registrationController::handlePasswordReset);
        // Start the HTTP server
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080, result -> {
                if (result.succeeded()) {
                    System.out.println("Server started on port 8080");
                } else {
                    System.err.println("Failed to start server: " + result.cause());
                }
            });
    }

    public static void main(String[] args) {
        // Configurar las opciones de Vert.x
        VertxOptions options = new VertxOptions()
            .setMaxEventLoopExecuteTime(Long.MAX_VALUE);

        // Crear la instancia de Vert.x con las opciones configuradas
        Vertx vertx = Vertx.vertx(options);

        vertx.deployVerticle(new Application());
    }
}