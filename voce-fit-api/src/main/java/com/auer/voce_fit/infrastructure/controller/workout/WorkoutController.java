package com.auer.voce_fit.infrastructure.controller.workout;

import com.auer.voce_fit.application.services.WorkoutService;
import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.entities.User;
import com.auer.voce_fit.domain.exceptions.UnauthorizedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor // Lombok para constructor injection
public class WorkoutController {

    private final WorkoutService workoutService;

    // Função auxiliar para obter usuário autenticado
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new UnauthorizedException("User not authorized!");
    }

    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> createWorkout(@Valid @RequestBody WorkoutRequestDTO request) {
        User user = getAuthenticatedUser();
        WorkoutResponseDTO workout = workoutService.createWorkout(request.title(), user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(workout.id())
                .toUri();
        return ResponseEntity.created(location).body(workout);
    }

    @PostMapping("/{workoutId}/duplicate")
    public ResponseEntity<WorkoutResponseDTO> duplicateWorkout(@PathVariable UUID workoutId) throws Throwable {
        User user = getAuthenticatedUser();
        WorkoutResponseDTO duplicatedWorkout = workoutService.duplicateWorkout(workoutId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedWorkout);
    }

    // Retorna todos os treinos do usuário autenticado pelo token JWT
    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> findAllByUser() {
        User user = getAuthenticatedUser();
        List<WorkoutResponseDTO> workouts = workoutService.getAllWorkouts(user);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseByWorkoutResponseDTO> findWorkout(@PathVariable UUID id) throws Throwable {
        User user = getAuthenticatedUser();
        ExerciseByWorkoutResponseDTO workout = workoutService.getWorkout(id, user);
        return ResponseEntity.ok(workout);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> updateWorkout(
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutRequestDTO request) throws Throwable {
        User user = getAuthenticatedUser();
        WorkoutResponseDTO workout = workoutService.updateWorkout(id, request.title(), user);
        return ResponseEntity.ok(workout);
    }

    @PutMapping("/{workoutId}/exercises/order")
    public ResponseEntity<ExerciseByWorkoutResponseDTO> reorderExercises(
            @PathVariable UUID workoutId,
            @RequestBody @Valid @NotEmpty List<ReorderRequestDTO> reorderRequests) throws Throwable {
        User user = getAuthenticatedUser();
        ExerciseByWorkoutResponseDTO updatedWorkout = workoutService.reorderExercises(workoutId, reorderRequests, user);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@PathVariable UUID id) throws Throwable {
        User user = getAuthenticatedUser();
        workoutService.deleteWorkout(id, user);
    }
}