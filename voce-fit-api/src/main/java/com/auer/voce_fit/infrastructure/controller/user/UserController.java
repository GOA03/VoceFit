package com.auer.voce_fit.infrastructure.controller.user;

import com.auer.voce_fit.application.services.UserService;
import com.auer.voce_fit.domain.dtos.UserRequestDTO;
import com.auer.voce_fit.domain.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepository userRepository;
    private UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
        try {

        userService.register(userRequestDTO.name(), userRequestDTO.email(), userRequestDTO.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Successfully registered user!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));

        }
    }
}
