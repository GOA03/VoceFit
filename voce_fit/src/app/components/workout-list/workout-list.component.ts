import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkoutCardComponent } from '../workout-card/workout-card.component';
import { WorkoutService } from '../../services/workout.service';
import { HttpClientModule } from '@angular/common/http';

interface WorkoutCardData {
  id: string;
  title: string;
  amount: number;
  createdAt: string;
  updatedAt: string;
}

@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [CommonModule, WorkoutCardComponent, HttpClientModule],
  templateUrl: './workout-list.component.html',
  styleUrls: ['./workout-list.component.scss']
})
export class WorkoutListComponent implements OnInit {
  workouts: WorkoutCardData[] = [];
  loading = true;
  error: string | null = null;

  constructor(private workoutService: WorkoutService) { }

  ngOnInit(): void {
    this.loadWorkouts();
  }

  loadWorkouts(): void {
    console.log('passando aqui!')
    this.workoutService.getWorkouts().subscribe({
      next: (workouts) => {
        // Converter dados da API para o formato esperado pelo workout-card
        this.workouts = workouts.map(workout => ({
          id: workout.id,
          title: workout.title,
          amount: workout.amount || 0,
          createdAt: workout.createdAt,
          updatedAt: workout.updatedAt
        }));
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading workouts:', error);
        this.error = 'Erro ao carregar treinos. Por favor, tente novamente.';
        this.loading = false;
      }
    });
  }
}
