package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.domain.exceptions.InvalidExerciseOrderException;
import com.auer.voce_fit.domain.exceptions.WorkoutNotFoundException;
import com.auer.voce_fit.usecases.workout.WorkoutUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    private final WorkoutUseCase workoutUseCase;

    public WorkoutService(WorkoutUseCase workoutUseCase) {
        this.workoutUseCase = workoutUseCase;
    }

    // Buscar todos os workouts
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

    // Buscar workout por ID
    public ExerciseByWorkoutResponseDTO getWorkout(UUID workoutId) {
        Workout workout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        List<ExerciseResponseDTO> exercises = workoutUseCase.getExercisesByWorkout(workoutId)
                .stream()
                .map(this::mapToExerciseResponseDTO)
                .toList();

        return new ExerciseByWorkoutResponseDTO(
                workout.getId(),
                workout.getTitle(),
                exercises.size(),
                exercises
        );
    }

    // Criar novo workout
    @Transactional
    public WorkoutResponseDTO createWorkout(String title) {
        Workout workout = Workout.builder()
                .title(title)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        workoutUseCase.createWorkout(workout);

        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getTitle(),
                0,
                workout.getCreatedAt(),
                workout.getUpdatedAt()
        );
    }

    // Atualizar workout
    @Transactional
    public WorkoutResponseDTO updateWorkout(UUID workoutId, String title) {
        Workout workout = workoutUseCase.updateWorkout(workoutId, title);

        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getTitle(),
                workout.getExercises() != null ? workout.getExercises().size() : 0,
                workout.getCreatedAt(),
                workout.getUpdatedAt()
        );
    }

    // Deletar workout
    @Transactional
    public void deleteWorkout(UUID workoutId) {
        workoutUseCase.deleteWorkout(workoutId);
    }

    // Duplicar workout
    @Transactional
    public WorkoutResponseDTO duplicateWorkout(UUID workoutId) {
        Workout workoutDuplicated = workoutUseCase.duplicateWorkout(workoutId);

        return new WorkoutResponseDTO(
                workoutDuplicated.getId(),
                workoutDuplicated.getTitle(),
                workoutDuplicated.getExercises() != null ? workoutDuplicated.getExercises().size() : 0,
                workoutDuplicated.getCreatedAt(),
                workoutDuplicated.getUpdatedAt()
        );
    }

    // === OPERAÇÕES DE EXERCÍCIOS ===

    // Reordenar exercícios em lote
    @Transactional
    public ExerciseByWorkoutResponseDTO reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests) {
        if (reorderRequests == null || reorderRequests.isEmpty()) {
            throw new InvalidExerciseOrderException("Lista de reordenação não pode estar vazia");
        }

        workoutUseCase.reorderExercises(workoutId, reorderRequests);
        return getWorkout(workoutId);
    }

    // === MÉTODOS AUXILIARES ===

    // Mapear entidade para DTO
    private ExerciseResponseDTO mapToExerciseResponseDTO(Exercise exercise) {
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