package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import com.auer.voce_fit.usecases.exercise.ExerciseUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExerciseService {

    private final ExerciseUseCase exerciseUseCase;

    public ExerciseService(ExerciseUseCase exerciseUseCase) {
        this.exerciseUseCase = exerciseUseCase;
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
                        exercise.getWorkout().getId()
                ))
                .toList();
    }

    public List<ExerciseResponseDTO> getExercisesByWorkoutId(UUID id) {
        return exerciseUseCase.getExercisesByWorkoutId(id).stream()
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
                .toList();
    }
}
