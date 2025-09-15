package com.auer.voce_fit.usecases.workout;

import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.domain.exceptions.ExerciseNotFoundException;
import com.auer.voce_fit.domain.exceptions.InvalidExerciseOrderException;
import com.auer.voce_fit.domain.exceptions.WorkoutNotFoundException;
import com.auer.voce_fit.infrastructure.persistence.JpaExerciseRepository;
import com.auer.voce_fit.infrastructure.persistence.JpaWorkoutRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Transactional // Abre transação, garante commit/rollback
    public Optional<Workout> createWorkout(Workout workout) {
        workoutRepository.save(workout);
        return Optional.of(workout);
    }

    @Override
    @Transactional // Abre transação
    public Workout updateWorkout(UUID workoutId, String title) {
        // Busca entidade existente → já gerenciada pelo Hibernate
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        // Alteração detectada automaticamente (dirty checking)
        workout.setTitle(title);

        // Não precisa save(), Hibernate fará o UPDATE no commit
        return workout;
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
    public Workout duplicateWorkout(UUID workoutId) {
        Workout originalWorkout  = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Treino não encontrado: " + workoutId));

        Workout newWorkout = Workout.builder()
            .title(originalWorkout.getTitle() + " (Copia)")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(originalWorkout.getUser())
                .build();

        if (originalWorkout.getExercises() != null) {
            List<Exercise> newExercises = originalWorkout.getExercises().stream()
                    .map(originalExercise -> Exercise.builder()
                            .workout(newWorkout)
                            .name(originalExercise.getName())
                            .sets(originalExercise.getSets())
                            .reps(originalExercise.getReps())
                            .weight(originalExercise.getWeight())
                            .restTime(originalExercise.getRestTime())
                            .notes(originalExercise.getNotes())
                            .sequence(originalExercise.getSequence())
                            .build()
                    ).collect(Collectors.toList());

            newWorkout.setExercises(newExercises);
        }

        return workoutRepository.save(newWorkout);
    }

    @Override
    public List<Exercise> getExercisesByWorkout(UUID workoutId) {
        return exerciseRepository.findByWorkoutId(workoutId);
    }

    @Override
    @Transactional
    public Workout reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests) {

        // 1. Validar se workout existe
        workoutRepository.findById(workoutId)
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
    public List<WorkoutResponseDTO> getWorkoutsByUser(UUID id) {
        List<Workout> workouts = workoutRepository.findByUserId(id);
        return workouts.stream()
                .map(workout -> new WorkoutResponseDTO(
                        workout.getId(),
                        workout.getTitle(),
                        workout.getExercises() != null ? workout.getExercises().size() : 0,
                        workout.getCreatedAt(),
                        workout.getUpdatedAt()
                ))
                .toList();
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
}