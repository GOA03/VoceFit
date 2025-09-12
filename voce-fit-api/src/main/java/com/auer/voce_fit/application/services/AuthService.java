package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.entities.User;
import com.auer.voce_fit.domain.repositories.UserRepository;
import com.auer.voce_fit.infrastructure.security.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtDecoder = jwtDecoder;
    }

    public Map<String, String> authenticate(String email, String password){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect credentials!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect credentials!");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(
                        new SimpleGrantedAuthority("READ"),
                        new SimpleGrantedAuthority("WRITE"),
                        new SimpleGrantedAuthority("DELETE")
                )
        );

        String accessToken = jwtService.generateAccessToken(authentication, user.getId());
        String refreshToken = jwtService.generateRefreshToken(authentication, user.getId());

        return Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken
        );
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            Jwt jwt = jwtDecoder.decode(refreshToken);
            String userIdStr = jwt.getClaimAsString("userId");
            String email = jwt.getSubject();

            if (userIdStr != null && email != null) {
                UUID userId = UUID.fromString(userIdStr);
                User user = userRepository.findById(userId).orElse(null);

                if (user != null && user.getEmail().equals(email)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            List.of(
                                    new SimpleGrantedAuthority("READ"),
                                    new SimpleGrantedAuthority("WRITE"),
                                    new SimpleGrantedAuthority("DELETE")
                            )
                    );

                    return jwtService.generateAccessToken(authentication, user.getId());
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }
}
