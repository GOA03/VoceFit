package com.auer.voce_fit.domain.repositories;

import com.auer.voce_fit.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}
