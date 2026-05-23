package com.careerflow.infrastructure.security;

import com.careerflow.domain.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${careerflow.jwt.secret}") String secret,
            @Value("${careerflow.jwt.expiration-ms}") long accessTokenExpirationMs,
            @Value("${careerflow.jwt.refresh-expiration-ms}") long refreshTokenExpirationMs) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpirationMs);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpirationMs);
    }

    public Long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    private String generateToken(User user, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }

    public SecretKey getKey() {
        return key;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
