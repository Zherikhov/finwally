package com.finwally.api.auth;

import com.finwally.application.service.UserService;
import com.finwally.domain.entity.UserEntity;
import com.finwally.domain.enums.UserTimeZoneSettings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserService users;
    private final PasswordEncoder passwordEncoder;


    public AuthController(UserService users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String normalizedEmail = req.email().trim().toLowerCase();

        if (users.existsByEmailNormalized(normalizedEmail)) {
            log.warn("User with email {} already exists", normalizedEmail);
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User with this email already exists"));
        }

        try {
            UserTimeZoneSettings settings = UserTimeZoneSettings.fromTimezone(req.timezone());

            UserEntity user = new UserEntity();
            user.setEmail(req.email());
            user.setEmailNormalized(normalizedEmail);
            user.setPasswordHash(passwordEncoder.encode(req.password()));
            user.setDisplayName(req.name());
            user.setTimezone(settings.getTimezone());
            user.setLocale(settings.getLocale());
            user.setBaseCurrencyCode(settings.getBaseCurrencyCode());

            users.save(user);

            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } catch (Exception e) {
            log.error("Registration error", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "An error occurred during registration: " + e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    public record RegisterRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotBlank String password,
            String timezone
    ) {
    }


}
