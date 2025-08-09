package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.usecases.workout.WorkoutUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutUseCase workoutUseCase;

    public WorkoutService (WorkoutUseCase workoutUseCase){
        this.workoutUseCase = workoutUseCase;
    }

    public List<WorkoutResponseDTO> getAllWorkouts() {
        return workoutUseCase.getWorkouts().stream()
                .map(workout -> new WorkoutResponseDTO(
                        workout.getId(),
                        workout.getTitle(),
                        workout.getExercises() != null ? workout.getExercises().size() : 0,
                        workout.getCreatedAt(),
                        workout.getUpdatedAt()
                ))
                .toList();
    }

    public ExerciseByWorkoutResponseDTO getWorkout(UUID workoutId) {
        Optional<Workout> workout = workoutUseCase.getWorkout(workoutId);

        List<ExerciseResponseDTO> exercises = workout.get().getExercises() != null
                ? workout.get().getExercises().stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getSets(),
                        exercise.getReps(),
                        exercise.getWeight(),
                        exercise.getRestTime(),
                        exercise.getNotes(),
                        exercise.getWorkout().getId()
                ))
                .collect(Collectors.toList())
                : List.of(); // lista vazia se for null

        return new ExerciseByWorkoutResponseDTO(
                workout.get().getId(),
                workout.get().getTitle(),
                exercises.size(),
                exercises
        );
    }
}
