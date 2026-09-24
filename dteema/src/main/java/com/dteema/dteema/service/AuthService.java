package com.dteema.dteema.service;

import com.dteema.dteema.dto.auth.JwtResponse;
import com.dteema.dteema.dto.auth.LoginRequest;
import com.dteema.dteema.dto.auth.RefreshTokenRequest;
import com.dteema.dteema.dto.auth.RefreshTokenResponse;
import com.dteema.dteema.dto.auth.RegisterRequest;
import com.dteema.dteema.exception.RefreshTokenException;
import com.dteema.dteema.model.RefreshToken;
import com.dteema.dteema.model.Role;
import com.dteema.dteema.model.User;
import com.dteema.dteema.repository.UserRepository;
import com.dteema.dteema.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request, String deviceInfo, String ipAddress) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.ROLE_USER))
                .enabled(true)
                .build();
        log.info("User created: Device info:{}\nIp address:{}",deviceInfo,ipAddress);
        userRepository.save(user);

    }

    @Transactional
    public JwtResponse login(LoginRequest request, String deviceInfo, String ipAddress) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                user.getId(),
                deviceInfo,
                ipAddress
        );

        return toJwtResponse(user, refreshToken.getToken());
    }

    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RefreshTokenException("Refresh token is not found"));

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));
    }

    private JwtResponse toJwtResponse(User user, String refreshToken) {
        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(refreshToken)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
