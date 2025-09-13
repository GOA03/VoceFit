package com.auer.voce_fit.usecases.user;


import com.auer.voce_fit.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserUseCase {
    List<User> getUsers();
    Optional<User> getUsersById(UUID id);
    void registerUser(User user);
}
