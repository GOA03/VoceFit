package com.auer.voce_fit.usecases.workout;

import com.auer.voce_fit.domain.entities.Workout;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutUseCase {
    List<Workout> getWorkouts();
    Optional<Workout> getWorkout(UUID id);
    void createWorkout(Workout workout);
}