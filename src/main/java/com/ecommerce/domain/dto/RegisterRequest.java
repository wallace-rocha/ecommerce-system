package com.ecommerce.domain.dto;

import com.ecommerce.domain.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Name is required.")

    private String name;
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "CPF is required.")
    private String cpf;

    @NotNull(message = "Role is required.")
    private Role role;
}
