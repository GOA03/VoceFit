package com.auer.voce_fit.infrastructure.controller;

import com.auer.voce_fit.application.services.WorkoutService;
import com.auer.voce_fit.domain.dtos.ExerciseByWorkoutResponseDTO;
import com.auer.voce_fit.domain.dtos.WorkoutResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
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
}
