package com.auer.voce_fit.domain.repositories;

import com.auer.voce_fit.domain.entities.Workout;

import java.util.List;

public interface WorkoutRepository {
    List<Workout> findAll();
}
