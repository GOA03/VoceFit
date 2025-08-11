package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.dtos.ExerciseRequestDTO;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.usecases.exercise.ExerciseUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ExerciseService {

    private final ExerciseUseCase exerciseUseCase;

    public ExerciseService(ExerciseUseCase exerciseUseCase) {
        this.exerciseUseCase = exerciseUseCase;
    }

    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO) {
        Exercise exercise = Exercise.builder()
                .workout(Workout.builder()
                        .id(exerciseRequestDTO.workoutId())
                        .build())
                .name(exerciseRequestDTO.name())
                .sets(exerciseRequestDTO.sets())
                .reps(exerciseRequestDTO.reps())
                .weight(exerciseRequestDTO.weight())
                .restTime(exerciseRequestDTO.restTime())
                .notes(exerciseRequestDTO.notes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exerciseUseCase.createExercise(exercise);

        return new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getWeight(),
                exercise.getRestTime(),
                exercise.getNotes(),
                exercise.getWorkout().getId(),
                exercise.getSequence()
        );
    }

    public List<ExerciseResponseDTO> getAllExercises() {
        return exerciseUseCase.getExercises().stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getSets(),
                        exercise.getReps(),
                        exercise.getWeight(),
                        exercise.getRestTime(),
                        exercise.getNotes(),
                        exercise.getWorkout().getId(),
                        exercise.getSequence()
                ))
                .toList();
    }

    public ExerciseResponseDTO getExerciseById(UUID id) {

        Exercise exercise = exerciseUseCase.getExerciseById(id);

        return new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getWeight(),
                exercise.getRestTime(),
                exercise.getNotes(),
                exercise.getWorkout().getId(),
                exercise.getSequence()
        );
    }
}
