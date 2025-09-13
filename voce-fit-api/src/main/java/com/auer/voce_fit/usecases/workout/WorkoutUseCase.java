package com.auer.voce_fit.usecases.workout;

import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.Workout;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutUseCase {
    List<Workout> getWorkouts();
    Optional<Workout> getWorkout(UUID id);
    Optional<Workout> createWorkout(Workout workout);
    Workout updateWorkout(UUID workoutId, String title);
    void deleteWorkout(UUID workoutId);
    Workout duplicateWorkout(UUID workoutId);

    List<Exercise> getExercisesByWorkout(UUID workoutId);
    Workout reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests);

    List<WorkoutResponseDTO> getWorkoutsByUser(UUID id);
}