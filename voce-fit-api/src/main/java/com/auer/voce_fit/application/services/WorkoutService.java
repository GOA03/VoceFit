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

    /**
     * Busca todos os workouts
     */
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

    /**
     * Busca um workout específico com seus exercícios
     */
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

    /**
     * Reordena exercícios de um workout em lote
     */
    @Transactional
    public ExerciseByWorkoutResponseDTO reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests) {

        // Validação básica
        if (reorderRequests == null || reorderRequests.isEmpty()) {
            throw new InvalidExerciseOrderException("Lista de reordenação não pode estar vazia");
        }

        // Delegar lógica para o UseCase
        workoutUseCase.reorderExercises(workoutId, reorderRequests);

        // Retornar workout atualizado
        return getWorkout(workoutId);
    }

    /**
     * Move um exercício para cima ou para baixo
     */
    @Transactional
    public ExerciseByWorkoutResponseDTO moveExercise(UUID workoutId, UUID exerciseId, String direction) {

        // Validação de parâmetros
        if (!"up".equals(direction) && !"down".equals(direction)) {
            throw new InvalidExerciseOrderException("Direção deve ser 'up' ou 'down'");
        }

        // Delegar para o UseCase
        workoutUseCase.moveExercise(workoutId, exerciseId, direction);

        // Retornar workout atualizado
        return getWorkout(workoutId);
    }

    /**
     * Cria um novo workout
     */
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
                0, // Sem exercícios inicialmente
                workout.getCreatedAt(),
                workout.getUpdatedAt()
        );
    }

    /**
     * Atualiza um workout existente
     */
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

    /**
     * Deleta um workout
     */
    @Transactional
    public void deleteWorkout(UUID workoutId) {
        workoutUseCase.deleteWorkout(workoutId);
    }

    /**
     * Mapeia Exercise para ExerciseResponseDTO
     */
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