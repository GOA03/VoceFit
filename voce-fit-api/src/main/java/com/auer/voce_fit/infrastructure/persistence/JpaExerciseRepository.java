package com.auer.voce_fit.infrastructure.persistence;

import com.auer.voce_fit.domain.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findByWorkoutId(UUID workoutId);
}
