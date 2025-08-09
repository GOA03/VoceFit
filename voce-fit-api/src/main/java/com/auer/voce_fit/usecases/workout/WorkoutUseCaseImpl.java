package com.auer.voce_fit.usecases.workout;

import com.auer.voce_fit.domain.entities.Workout;
import com.auer.voce_fit.infrastructure.persistence.JpaWorkoutRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WorkoutUseCaseImpl implements WorkoutUseCase {

    private final JpaWorkoutRepository workoutRepository;

    public WorkoutUseCaseImpl(JpaWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
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
    public void createWorkout(Workout workout) {

    }
}
