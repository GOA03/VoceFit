package com.auer.voce_fit.usecases.exercise;

import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.infrastructure.persistence.JpaExerciseRepository;
import org.springframework.transaction.annotation.Transactional;
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
    public Exercise getExerciseById(UUID id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    @Override
    public List<Exercise> getExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getExercisesByWorkoutId(UUID id) {
        return exerciseRepository.findByWorkoutId(id);
    }

    @Override
    @Transactional
    public void createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

}
