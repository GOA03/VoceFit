package com.auer.voce_fit.usecases.exercise;

import com.auer.voce_fit.domain.dtos.ExerciseRequestDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface ExerciseUseCase {

    Exercise getExerciseById(UUID id);
    List<Exercise> getExercises();
    void createExercise(Exercise exercise);
    Exercise updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO);
    void deleteExercise(UUID id);

    // List<Exercise> getExercisesByWorkoutId(UUID id);
}
