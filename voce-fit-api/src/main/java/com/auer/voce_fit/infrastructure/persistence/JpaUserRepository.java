package com.auer.voce_fit.infrastructure.persistence;

import com.auer.voce_fit.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {
}
