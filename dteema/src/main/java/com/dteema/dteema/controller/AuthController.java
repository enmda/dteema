package com.dteema.dteema.controller;

import com.dteema.dteema.dto.auth.JwtResponse;
import com.dteema.dteema.dto.auth.LoginRequest;
import com.dteema.dteema.dto.auth.RefreshTokenRequest;
import com.dteema.dteema.dto.auth.RefreshTokenResponse;
import com.dteema.dteema.dto.auth.RegisterRequest;
import com.dteema.dteema.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse register(@Valid @RequestBody RegisterRequest request,
                                HttpServletRequest httpRequest) {
        return authService.register(request, deviceInfo(httpRequest), ipAddress(httpRequest));
    }

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request,
                             HttpServletRequest httpRequest) {
        return authService.login(request, deviceInfo(httpRequest), ipAddress(httpRequest));
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
    }

    private String deviceInfo(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    private String ipAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
