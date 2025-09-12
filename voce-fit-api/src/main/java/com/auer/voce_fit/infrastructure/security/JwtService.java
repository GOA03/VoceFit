package com.auer.voce_fit.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateAccessToken(Authentication authentication, UUID userId) {
        Instant now = Instant.now();
        long expirySeconds = Duration.ofHours(2).getSeconds();

        String scope = authentication
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirySeconds))
                .subject(authentication.getName())
                .claim("userId", userId)
                .claim("scope", scope)
                .build();

        return encoder.encode(
                JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication, UUID userId) {
        Instant now = Instant.now();
        long expirySeconds = Duration.ofDays(14).getSeconds();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirySeconds))
                .subject(authentication.getName())
                .claim("userId", userId)
                .build();

        return encoder.encode(
                JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
