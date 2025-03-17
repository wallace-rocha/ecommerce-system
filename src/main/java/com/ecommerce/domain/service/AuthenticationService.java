package com.ecommerce.domain.service;

import com.ecommerce.domain.dto.AuthenticationRequest;
import com.ecommerce.domain.dto.AuthenticationResponse;
import com.ecommerce.domain.dto.RegisterRequest;
import com.ecommerce.domain.dto.RegisterResponse;
import com.ecommerce.domain.exception.UserAlreadyRegisteredException;
import com.ecommerce.domain.exception.UserNotFoundException;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getUsername());
                    return new UserNotFoundException("User not found.");
                });

        var token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public User registerUser(User user, RegisterRequest request) {

        if (userRepository.findByCpf(request.getCpf()).isPresent()) {
            throw new UserAlreadyRegisteredException("User already registered.");
        }

        User userToCreate = new User();
        userToCreate.setName(request.getName());
        userToCreate.setUsername(request.getUsername());
        userToCreate.setCpf(request.getCpf());
        userToCreate.setPassword(passwordEncoder.encode(request.getPassword()));
        userToCreate.setRole(request.getRole());
        userToCreate.setCreatedAt(LocalDateTime.now());

        return userRepository.save(userToCreate);
    }

    public RegisterResponse getUserResponse(User user) {
        return RegisterResponse.builder()
                .username(user.getUsername())
                .cpf(user.getCpf())
                .build();
    }
}
