package com.auer.voce_fit.infrastructure.controller;

import com.auer.voce_fit.application.services.WorkoutService;
import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.ReorderRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutRequestDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import com.auer.voce_fit.domain.exceptions.InvalidExerciseOrderException;
import com.auer.voce_fit.domain.exceptions.WorkoutNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> createWorkout (@RequestBody WorkoutRequestDTO request) {
        try {
            WorkoutResponseDTO workout = workoutService.createWorkout(request.title());
            return ResponseEntity.status(201).body(workout);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> findAll() {
        List<WorkoutResponseDTO> workouts = workoutService.getAllWorkouts();
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExerciseByWorkoutResponseDTO> findAll(@PathVariable UUID id) {
        ExerciseByWorkoutResponseDTO workouts = workoutService.getWorkout(id);
        return ResponseEntity.ok(workouts);
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkoutResponseDTO> updateWorkout(@PathVariable UUID id,  @RequestBody WorkoutRequestDTO request) {
        try {
            WorkoutResponseDTO workout = workoutService.updateWorkout(id, request.title());
            return ResponseEntity.ok(workout);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{workoutId}/exercises/order")
    public ResponseEntity<ExerciseByWorkoutResponseDTO> reorderExercises(
            @PathVariable UUID workoutId,
            @RequestBody @Valid @NotEmpty List<ReorderRequestDTO> reorderRequests) {

        try {
            ExerciseByWorkoutResponseDTO updatedWorkout = workoutService.reorderExercises(workoutId, reorderRequests);
            return ResponseEntity.ok(updatedWorkout);
        } catch (WorkoutNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidExerciseOrderException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable UUID id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}
