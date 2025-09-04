package com.auer.voce_fit.infrastructure.controller.workout;

import com.auer.voce_fit.application.services.ExerciseService;
import com.auer.voce_fit.domain.dtos.ExerciseRequestDTO;
import com.auer.voce_fit.domain.dtos.ExerciseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createExercise(@RequestBody ExerciseRequestDTO exerciseRequestDTO) {
        try {
            ExerciseResponseDTO exercise = exerciseService.createExercise(exerciseRequestDTO);
            return ResponseEntity.status(201).body(exercise);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllWorkouts() {
        List<ExerciseResponseDTO> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable UUID id) {
        ExerciseResponseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(@PathVariable UUID id, @RequestBody ExerciseRequestDTO exerciseRequestDTO) {
        try {
            ExerciseResponseDTO exercise = exerciseService.updateExercise(id, exerciseRequestDTO);
            return ResponseEntity.ok(exercise);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
