import { Component, ViewChild } from '@angular/core';
import { GymHeaderComponent } from '../../components/gym-header/gym-header.component';
import { WorkoutListComponent } from '../../components/workout-list/workout-list.component';
import { FloatingAddButtonComponent } from "../../components/floating-add-button/floating-add-button.component";
import { WorkoutFormComponent, WorkoutFormData } from "../../components/workout-form/workout-form.component";
import { Workout } from '../../models/workout.model';

import { WorkoutService } from '../../services/workout.service';
import { WorkoutFormService } from '../../services/workout-form.service';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [
    GymHeaderComponent,
    WorkoutListComponent,
    FloatingAddButtonComponent,
    WorkoutFormComponent
  ],
  templateUrl: './index.component.html',
  styleUrl: './index.component.scss'
})
export class IndexComponent {
  @ViewChild(WorkoutListComponent) workoutListComponent?: WorkoutListComponent;

  constructor(
    private workoutService: WorkoutService,
    private workoutFormService: WorkoutFormService
  ) {}

  formState = {
    isOpen: false,
    mode: 'create' as 'create' | 'edit',
    workout: undefined as any
  };

  onAddWorkout(): void {
    this.openWorkoutForm('create');
  }

  openWorkoutForm(mode: 'create' | 'edit', workout?: any): void {
    this.formState = {
      isOpen: true,
      mode,
      workout
    };
  }

  closeWorkoutForm(): void {
    this.formState.isOpen = false;
  }

  saveWorkout(formData: WorkoutFormData): void {
    if (this.formState.mode === 'create') {
      // Criar um novo treino
      const newWorkout = {
        title: formData.title
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
    } else if (this.formState.mode === 'edit' && this.formState.workout) {
      // Editar um treino existente
      const updatedWorkout = {
        ...this.formState.workout,
        title: formData.title,
        updatedAt: new Date().toISOString()
      };

      this.workoutService.updateWorkout(this.formState.workout.id, updatedWorkout).subscribe({
        next: (workout) => {
          console.log('Treino atualizado com sucesso:', workout);
          // Recarregar a lista de treinos
          if (this.workoutListComponent) {
            this.workoutListComponent.loadWorkouts();
          }
        },
        error: (error) => {
          console.error('Erro ao atualizar treino:', error);
          alert('Erro ao atualizar treino. Por favor, tente novamente.');
        }
      });
    }
  }

  deleteWorkout(workoutId: string): void {
    this.workoutService.deleteWorkout(workoutId).subscribe({
      next: () => {
        console.log('Treino excluído com sucesso');
        // Recarregar a lista de treinos
        if (this.workoutListComponent) {
          this.workoutListComponent.loadWorkouts();
        }
      },
      error: (error) => {
        console.error('Erro ao excluir treino:', error);
        alert('Erro ao excluir treino. Por favor, tente novamente.');
      }
    });
  }

  duplicateWorkout(workout: Workout): void {
    this.workoutService.createWorkout(workout).subscribe({
      next: (newWorkout) => {
        console.log('Treino duplicado com sucesso:', newWorkout);
        // Recarregar a lista de treinos
        if (this.workoutListComponent) {
          this.workoutListComponent.loadWorkouts();
        }
      },
      error: (error) => {
        console.error('Erro ao duplicar treino:', error);
        alert('Erro ao duplicar treino. Por favor, tente novamente.');
      }
    });
  }
}
