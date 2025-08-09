package com.auer.voce_fit.usecases.exercise;

import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.infrastructure.persistence.JpaExerciseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ExerciseUseCaseImpl implements ExerciseUseCase {

    public final JpaExerciseRepository exerciseRepository;

    public ExerciseUseCaseImpl(JpaExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> getExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getExercisesByWorkoutId(UUID id) {
        return exerciseRepository.findByWorkoutId(id);
    }

}
