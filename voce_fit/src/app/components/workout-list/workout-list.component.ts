import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkoutCardComponent } from '../workout-card/workout-card.component';
import { WorkoutService } from '../../services/workout.service';
import { Workout } from '../../models/workout.model';
import { AlertService } from '../../services/alert.service';
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
  imports: [CommonModule, WorkoutCardComponent],
  templateUrl: './workout-list.component.html',
  styleUrls: ['./workout-list.component.scss']
})
export class WorkoutListComponent implements OnInit {
  @Output() editWorkout = new EventEmitter<Workout>();
  @Output() deleteWorkout = new EventEmitter<string>();
  @Output() duplicateWorkout = new EventEmitter<Workout>();
  workouts: WorkoutCardData[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private workoutService: WorkoutService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.loadWorkouts();
  }

  loadWorkouts(): void {
    this.loading = true;
    this.error = null;

    this.workoutService.getWorkouts().subscribe({
      next: (workouts) => {
        // Converter dados da API para o formato esperado pelo workout-card
        this.workouts = workouts.map(workout => ({
          id: workout.id,
          title: workout.title,
          amount: workout.amount || 0,
          createdAt: workout.createdAt,
          updatedAt: workout.updatedAt,
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

  onEditWorkout(workoutId: string): void {
    this.workoutService.getWorkoutById(workoutId).subscribe({
      next: (workout) => {
        this.editWorkout.emit(workout);
      },
      error: (error) => {
        console.error('Erro ao carregar treino para edição:', error);
        this.alertService.error('Erro ao carregar treino para edição. Por favor, tente novamente.');
      }
    });
  }

  onDeleteWorkout(workoutId: string): void {
    if (confirm('Tem certeza que deseja excluir este treino?')) {
      this.deleteWorkout.emit(workoutId);
    }
  }

  onDuplicateWorkout(workoutId: string): void {
    // Agora apenas emitimos o ID do workout para o componente pai
    // O serviço de duplicação será chamado no componente pai
    this.workoutService.getWorkoutById(workoutId).subscribe({
      next: (workout) => {
        this.duplicateWorkout.emit(workout);
      },
      error: (error) => {
        console.error('Erro ao duplicar treino:', error);
        this.alertService.error('Erro ao duplicar treino. Por favor, tente novamente.');
      }
    });
  }
}
