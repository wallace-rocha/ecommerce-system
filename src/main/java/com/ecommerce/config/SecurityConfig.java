package com.ecommerce.config;

import com.ecommerce.domain.exception.UserNotFoundException;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/top-five-users-by-orders").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/find-average-ticket-per-user").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/total-monthly-revenue/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(userDetailsService(null)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found: %s", username)));
    }
}