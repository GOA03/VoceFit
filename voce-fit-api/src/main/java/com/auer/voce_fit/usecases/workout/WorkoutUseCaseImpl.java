package com.auer.voce_fit.usecases.workout;

import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.domain.exceptions.ExerciseNotFoundException;
import com.auer.voce_fit.domain.exceptions.InvalidExerciseOrderException;
import com.auer.voce_fit.domain.exceptions.WorkoutNotFoundException;
import com.auer.voce_fit.infrastructure.persistence.JpaExerciseRepository;
import com.auer.voce_fit.infrastructure.persistence.JpaWorkoutRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WorkoutUseCaseImpl implements WorkoutUseCase {

    private final JpaWorkoutRepository workoutRepository;
    private final JpaExerciseRepository exerciseRepository;

    public WorkoutUseCaseImpl(JpaWorkoutRepository workoutRepository,
                              JpaExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Workout> getWorkouts() {
        return workoutRepository.findAll();
    }

    @Override
    public Optional<Workout> getWorkout(UUID id) {
        return workoutRepository.findById(id);
    }

    @Override
    @Transactional
    public void createWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    @Override
    @Transactional
    public Workout updateWorkout(UUID workoutId, String title) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        workout.setTitle(title);
        return workoutRepository.save(workout);
    }

    @Override
    @Transactional
    public void deleteWorkout(UUID workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            throw new WorkoutNotFoundException("Workout não encontrado: " + workoutId);
        }
        workoutRepository.deleteById(workoutId);
    }

    @Override
    public List<Exercise> getExercisesByWorkout(UUID workoutId) {
        return exerciseRepository.findByWorkoutId(workoutId);
    }

    @Override
    @Transactional
    public Workout reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests) {

        // 1. Validar se workout existe
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        // 2. Validar se todos os exercícios pertencem ao workout
        validateExercisesBelongToWorkout(workoutId, reorderRequests);

        // 3. Aplicar reordenação
        for (ReorderRequestDTO request : reorderRequests) {
            Exercise exercise = exerciseRepository.findById(request.exerciseId())
                    .orElse(null);
            if (exercise != null && exercise.getWorkout().getId().equals(workoutId)) {
                exercise.setSequence(request.newSequence());
                exerciseRepository.save(exercise);
            }
        }

        // 4. Retornar workout atualizado
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));
    }

    @Override
    @Transactional
    public Workout moveExercise(UUID workoutId, UUID exerciseId, String direction) {

        // Validar se workout existe
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        // Buscar exercício
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercício não encontrado: " + exerciseId));

        // Validar se exercício pertence ao workout
        if (!exercise.getWorkout().getId().equals(workoutId)) {
            throw new InvalidExerciseOrderException("Exercício não pertence ao workout informado");
        }

        Integer currentSequence = exercise.getSequence();
        Integer newSequence;

        if ("up".equals(direction)) {
            newSequence = Math.max(1, currentSequence - 1);
        } else { // "down"
            newSequence = currentSequence + 1;
        }

        // Se não mudou, não faz nada
        if (currentSequence.equals(newSequence)) {
            return workout;
        }

        // Trocar posições
        swapExerciseSequences(workoutId, currentSequence, newSequence);

        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));
    }

    private void validateExercisesBelongToWorkout(UUID workoutId, List<ReorderRequestDTO> requests) {
        for (ReorderRequestDTO request : requests) {
            Exercise exercise = exerciseRepository.findById(request.exerciseId())
                    .orElseThrow(() -> new ExerciseNotFoundException("Exercício não encontrado: " + request.exerciseId()));

            if (!exercise.getWorkout().getId().equals(workoutId)) {
                throw new InvalidExerciseOrderException(
                        "Exercício " + request.exerciseId() + " não pertence ao workout " + workoutId);
            }
        }
    }

    private void swapExerciseSequences(UUID workoutId, Integer seq1, Integer seq2) {
        List<Exercise> exercises = exerciseRepository.findByWorkoutId(workoutId);

        Exercise exercise1 = exercises.stream()
                .filter(e -> e.getSequence().equals(seq1))
                .findFirst().orElse(null);

        Exercise exercise2 = exercises.stream()
                .filter(e -> e.getSequence().equals(seq2))
                .findFirst().orElse(null);

        if (exercise1 != null && exercise2 != null) {
            // Trocar sequences
            exercise1.setSequence(seq2);
            exercise2.setSequence(seq1);
            exerciseRepository.save(exercise1);
            exerciseRepository.save(exercise2);
        } else if (exercise1 != null) {
            // Apenas mover o exercício
            exercise1.setSequence(seq2);
            exerciseRepository.save(exercise1);
        }
    }
}