package com.david.monitoring.auth;

import com.david.monitoring.auth.dto.AuthResponse;
import com.david.monitoring.auth.dto.LoginRequest;
import com.david.monitoring.auth.dto.RegisterRequest;
import com.david.monitoring.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService,
                          JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(toAuthResponse(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(toAuthResponse(authService.login(request)));
    }

    private AuthResponse toAuthResponse(User user) {
        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getEmail());
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), token);
    }

}
