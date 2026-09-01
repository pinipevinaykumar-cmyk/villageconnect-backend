package com.villageconnect.backend.service;

import com.villageconnect.backend.dto.request.*;
import com.villageconnect.backend.dto.response.*;
import com.villageconnect.backend.entity.User;
import com.villageconnect.backend.repository.UserRepository;
import com.villageconnect.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        String cleanName = request.getName().trim().toLowerCase().replaceAll("\\s+", "_");

        // Auto-generate email and phone if not provided
        boolean emailProvided = request.getEmail() != null && !request.getEmail().isBlank();
        boolean phoneProvided = request.getPhone() != null && !request.getPhone().isBlank();

        String normalizedEmail = emailProvided
                ? normalizeEmail(request.getEmail())
                : cleanName + "_" + UUID.randomUUID().toString().substring(0, 8) + "@villageconnect.test";

        String normalizedPhone = phoneProvided
                ? normalizePhone(request.getPhone())
                : "vc_" + System.currentTimeMillis();

        // Check duplicate only for user-supplied values
        if (emailProvided && userRepository.existsByEmail(normalizedEmail)) {
            throw new RuntimeException("Email already registered");
        }
        if (phoneProvided && userRepository.existsByPhone(normalizedPhone)) {
            throw new RuntimeException("Phone already registered");
        }

        // Check duplicate username
        if (userRepository.existsByName(request.getName().trim())) {
            throw new RuntimeException("Username already taken");
        }

        User.Role role = User.Role.CUSTOMER;
        if ("MERCHANT".equalsIgnoreCase(request.getRole())) {
            role = User.Role.MERCHANT;
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(normalizedEmail)
                .phone(normalizedPhone)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .role(role)
                .village(request.getVillage() == null ? null : request.getVillage().trim())
                .isActive(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        String emailOrPhone = request.getEmailOrPhone() == null ? null : request.getEmailOrPhone().trim();
        User user = userRepository.findByEmail(normalizeEmail(emailOrPhone))
                .orElseGet(() -> userRepository.findByPhone(normalizePhone(emailOrPhone))
                .orElseGet(() -> userRepository.findByName(emailOrPhone)
                        .orElseThrow(() -> new RuntimeException("User not found"))));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }

        String digits = phone.replaceAll("\\D+", "");
        if (digits.length() > 10 && digits.startsWith("91")) {
            digits = digits.substring(2);
        }
        return digits;
    }
}