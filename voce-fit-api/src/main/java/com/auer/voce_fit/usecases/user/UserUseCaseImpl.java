package com.auer.voce_fit.usecases.user;


import com.auer.voce_fit.domain.entities.User;
import com.auer.voce_fit.infrastructure.persistence.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserUseCaseImpl implements UserUseCase {

    private final JpaUserRepository userRepository;

    public UserUseCaseImpl(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUsersById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }
}
