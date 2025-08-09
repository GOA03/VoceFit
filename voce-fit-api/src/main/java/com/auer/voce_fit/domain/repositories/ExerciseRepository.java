package com.auer.voce_fit.domain.repositories;

import com.auer.voce_fit.domain.entities.Exercise;
import java.util.List;
import java.util.UUID;

public interface ExerciseRepository {

    Exercise findByExerciseId(UUID exerciseId);
    List<Exercise> findByWorkoutId(UUID workoutId);
}