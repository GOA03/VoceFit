import { Component, ViewChild } from '@angular/core';
import { GymHeaderComponent } from '../../components/gym-header/gym-header.component';
import { WorkoutListComponent } from '../../components/workout-list/workout-list.component';
import { FloatingAddButtonComponent } from "../../components/floating-add-button/floating-add-button.component";
import { WorkoutService } from '../../services/workout.service';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [
    GymHeaderComponent,
    WorkoutListComponent,
    FloatingAddButtonComponent
  ],
  templateUrl: './index.component.html',
  styleUrl: './index.component.scss'
})
export class IndexComponent {
  @ViewChild(WorkoutListComponent) workoutListComponent?: WorkoutListComponent;

  constructor(private workoutService: WorkoutService) {}

  onAddWorkout(): void {
    console.log('Adicionando novo treino...');
    
    // Criar um novo treino com dados básicos
    const newWorkout = {
      title: 'Novo Treino',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      amount: 0
    };

    this.workoutService.createWorkout(newWorkout).subscribe({
      next: (workout) => {
        console.log('Treino criado com sucesso:', workout);
        // Recarregar a lista de treinos
        if (this.workoutListComponent) {
          this.workoutListComponent.loadWorkouts();
        }
      },
      error: (error) => {
        console.error('Erro ao criar treino:', error);
        alert('Erro ao criar treino. Por favor, tente novamente.');
      }
    });
  }
}
