package com.auer.voce_fit.usecases.exercise;

import com.auer.voce_fit.domain.entities.Exercise;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface ExerciseUseCase {

    List<Exercise> getExercises();
    List<Exercise> getExercisesByWorkoutId(UUID id);
}
