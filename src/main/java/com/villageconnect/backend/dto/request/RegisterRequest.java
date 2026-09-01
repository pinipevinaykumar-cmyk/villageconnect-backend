package com.villageconnect.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phone;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;

    private String village;
}
