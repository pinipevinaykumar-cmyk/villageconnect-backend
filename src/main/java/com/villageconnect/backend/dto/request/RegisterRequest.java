package com.villageconnect.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank @Email(message = "Valid email is required")
    private String email;

    @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$", message = "Valid Indian phone number required")
    private String phone;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;

    private String village;
}