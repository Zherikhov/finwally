package com.myeasybudget.user.presentation;

import com.myeasybudget.user.application.AuthResult;
import com.myeasybudget.user.application.AuthService;
import com.myeasybudget.user.application.UserSummary;
import com.myeasybudget.user.security.AuthenticatedUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = authService.register(request.toCommand());
        return AuthResponse.from(result);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request.toCommand());
        return AuthResponse.from(result);
    }

    @GetMapping("/me")
    public UserSummary me(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        return authService.findActiveUser(principal.userId());
    }

    public record AuthResponse(
            String tokenType,
            String accessToken,
            Instant expiresAt,
            UserSummary user
    ) {

        static AuthResponse from(AuthResult result) {
            return new AuthResponse("Bearer", result.accessToken(), result.expiresAt(), result.user());
        }
    }
}
