package com.auer.voce_fit.application.services;

import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.Exercise;
import com.auer.voce_fit.domain.entities.User;
import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.domain.exceptions.InvalidExerciseOrderException;
import com.auer.voce_fit.domain.exceptions.WorkoutNotFoundException;
import com.auer.voce_fit.usecases.workout.WorkoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutUseCase workoutUseCase;

    // Método auxiliar para validar propriedade do workout
    private void validateWorkoutOwnership(Workout workout, User user) throws Throwable {
        if (!workout.getUser().getId().equals(user.getId())) throw new Throwable("Access denied!");
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponseDTO> getAllWorkouts(User user) {
        return workoutUseCase.getWorkoutsByUser(user.getId());
    }

    @Transactional(readOnly = true)
    public ExerciseByWorkoutResponseDTO getWorkout(UUID workoutId, User user) throws Throwable {
        Workout workout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        validateWorkoutOwnership(workout, user);

        List<ExerciseResponseDTO> exercises = workout.getExercises()
                .stream()
                .sorted(Comparator.comparing(Exercise::getSequence))
                .map(this::mapToExerciseResponseDTO)
                .toList();

        return new ExerciseByWorkoutResponseDTO(
                workout.getId(),
                workout.getTitle(),
                exercises.size(),
                exercises
        );
    }

    @Transactional
    public WorkoutResponseDTO createWorkout(String title, User user) {
        Optional<Workout> workout = Optional.ofNullable(Workout.builder()
                .title(title)
                .user(user)
                .build()); // CreatedAt e UpdatedAt podem ser gerenciados por @CreatedDate/@LastModifiedDate

        workout = workoutUseCase.createWorkout(workout.orElse(null));

        assert workout.orElse(null) != null;
        return mapToWorkoutResponseDTO(workout.orElse(null));
    }

    @Transactional
    public WorkoutResponseDTO updateWorkout(UUID workoutId, String title, User user) throws Throwable {
        Workout existingWorkout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        validateWorkoutOwnership(existingWorkout, user);

        Workout workout = workoutUseCase.updateWorkout(workoutId, title);

        return mapToWorkoutResponseDTO(workout);
    }

    @Transactional
    public void deleteWorkout(UUID workoutId, User user) throws Throwable {
        Workout workout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        validateWorkoutOwnership(workout, user);

        workoutUseCase.deleteWorkout(workoutId);
    }

    @Transactional
    public WorkoutResponseDTO duplicateWorkout(UUID workoutId, User user) throws Throwable {
        Workout originalWorkout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        validateWorkoutOwnership(originalWorkout, user);

        Workout duplicated = workoutUseCase.duplicateWorkout(workoutId);

        return mapToWorkoutResponseDTO(duplicated);
    }

    @Transactional
    public ExerciseByWorkoutResponseDTO reorderExercises(UUID workoutId, List<ReorderRequestDTO> reorderRequests, User user) throws Throwable {
        if (reorderRequests == null || reorderRequests.isEmpty()) {
            throw new InvalidExerciseOrderException("Lista de reordenação não pode estar vazia");
        }

        Workout workout = workoutUseCase.getWorkout(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout não encontrado: " + workoutId));

        validateWorkoutOwnership(workout, user);

        workoutUseCase.reorderExercises(workoutId, reorderRequests);
        return getWorkout(workoutId, user);
    }

    // Métodos de mapeamento centralizados
    private WorkoutResponseDTO mapToWorkoutResponseDTO(Workout workout) {
        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getTitle(),
                workout.getExercises() != null ? workout.getExercises().size() : 0,
                workout.getCreatedAt(),
                workout.getUpdatedAt()
        );
    }

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