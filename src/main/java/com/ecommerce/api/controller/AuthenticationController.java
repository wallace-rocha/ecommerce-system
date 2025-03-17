package com.ecommerce.api.controller;

import com.ecommerce.domain.dto.AuthenticationRequest;
import com.ecommerce.domain.dto.AuthenticationResponse;
import com.ecommerce.domain.dto.RegisterRequest;
import com.ecommerce.domain.dto.RegisterResponse;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        logger.info("Received request to login user.");
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            logger.info("User login successfully.");
            return response;
        } catch (Exception e) {
            logger.error("Error retrieving login user.", e);
            throw e;
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@AuthenticationPrincipal User user, @Valid @RequestBody RegisterRequest request) {
        logger.info("Received request to register user.");
        try {
            User userResponse = authenticationService.registerUser(user, request);
            logger.info("User registered successfully.");
            return authenticationService.getUserResponse(userResponse);
        } catch (Exception e) {
            logger.error("Error retrieving register user.", e);
            throw e;
        }
    }
}