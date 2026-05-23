package com.careerflow.application.services;

import com.careerflow.application.dto.*;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.UserRepository;
import com.careerflow.infrastructure.security.JwtTokenProvider;
import com.careerflow.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        user = userRepository.save(user);
        log.info("User registered: {}", user.getEmail());

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationMs(),
                toUserResponse(user)
        );
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.user();

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("User logged in: {}", user.getEmail());

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationMs(),
                toUserResponse(user)
        );
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
