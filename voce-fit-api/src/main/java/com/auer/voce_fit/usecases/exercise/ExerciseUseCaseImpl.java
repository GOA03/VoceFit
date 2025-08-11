package com.auer.voce_fit.usecases.exercise;

import com.auer.voce_fit.domain.dtos.ExerciseRequestDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.exceptions.ExerciseNotFoundException;
import com.auer.voce_fit.infrastructure.persistence.JpaExerciseRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    /*
    @Override
    public List<Exercise> getExercisesByWorkoutId(UUID id) {
        return exerciseRepository.findByWorkoutId(id);
    }
    */

    @Override
    @Transactional
    public void createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public Exercise updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercício não encontrado: " + id));

        exercise.setName(exerciseRequestDTO.name());
        exercise.setSets(exerciseRequestDTO.sets());
        exercise.setReps(exerciseRequestDTO.reps());
        exercise.setWeight(exerciseRequestDTO.weight());
        exercise.setRestTime(exerciseRequestDTO.restTime());
        exercise.setNotes(exerciseRequestDTO.notes());
        exercise.setUpdatedAt(LocalDateTime.now());

        return exercise;
    }

    @Override
    public void deleteExercise(UUID id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ExerciseNotFoundException("Exercício não encontrado: " + id);
        }
        exerciseRepository.deleteById(id);
    }
}
