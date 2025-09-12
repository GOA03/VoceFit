package com.auer.voce_fit.infrastructure.security;

import com.auer.voce_fit.domain.entities.User;
import com.auer.voce_fit.domain.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Autowired
    public TokenValidationFilter(JwtDecoder jwtDecoder, UserRepository userRepository) {
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o token do cabeçalho Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Decodifica e valida o token
                Jwt jwt = jwtDecoder.decode(token);

                // Extrai claims
                String email = jwt.getSubject();
                String userIdStr = jwt.getClaimAsString("userId");
                String scope = jwt.getClaimAsString("scope");

                if (email != null && userIdStr != null) {
                    UUID userId = UUID.fromString(userIdStr);

                    // Carrega o usuário do banco
                    User user = userRepository.findById(userId).orElse(null);

                    if (user != null) {
                        // Cria authorities a partir do scope
                        List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                        // Cria o objeto Authentication
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);

                        // Define no contexto de segurança
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // Token inválido ou expirado - continua sem definir autenticação
                SecurityContextHolder.clearContext();
            }
        }

        // Continua a cadeia do filtro
        filterChain.doFilter(request, response);
    }
}
