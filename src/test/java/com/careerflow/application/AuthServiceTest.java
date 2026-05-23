package com.careerflow.application;

import com.careerflow.application.dto.AuthResponse;
import com.careerflow.application.dto.RegisterRequest;
import com.careerflow.application.services.AuthService;
import com.careerflow.domain.entities.User;
import com.careerflow.domain.repositories.UserRepository;
import com.careerflow.domain.valueobjects.Role;
import com.careerflow.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest("Test User", "test@test.com", "password123", Role.STUDENT);

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@test.com")
                .password("encoded")
                .role(Role.STUDENT)
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals("Test User", response.user().name());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("Test", "existing@test.com", "password123", Role.STUDENT);

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }
}
