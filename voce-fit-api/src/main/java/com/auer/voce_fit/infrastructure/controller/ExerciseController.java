package com.auer.voce_fit.infrastructure.controller;

import com.auer.voce_fit.application.services.ExerciseService;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllWorkouts() {
        List<ExerciseResponseDTO> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ExerciseResponseDTO>> getExercisesByWorkoutId(@PathVariable UUID id) {
        List<ExerciseResponseDTO> exercises = exerciseService.getExercisesByWorkoutId(id);
        return ResponseEntity.ok(exercises);

    }
}
