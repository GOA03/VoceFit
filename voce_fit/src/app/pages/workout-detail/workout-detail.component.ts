import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { GymHeaderComponent } from '../../components/gym-header/gym-header.component';
import { ExerciseCardComponent } from "../../components/exercise-card/exercise-card.component";
import { ExerciseStatsComponent } from "../../components/exercise-stats/exercise-stats.component";
import { ExerciseFormComponent } from "../../components/exercise-form/exercise-form.component";
import { FloatingAddButtonComponent } from "../../components/floating-add-button/floating-add-button.component";
import { ExerciseService } from '../../services/exercise.service';
import { WorkoutService, Workout } from '../../services/workout.service';
import { Exercise } from '../../models/exercise.model';
import { LucideAngularModule } from "lucide-angular";

@Component({
  selector: 'app-workout-detail',
  standalone: true,
  imports: [
    CommonModule,
    GymHeaderComponent,
    ExerciseCardComponent,
    ExerciseStatsComponent,
    ExerciseFormComponent,
    FloatingAddButtonComponent,
    LucideAngularModule
],
  templateUrl: './workout-detail.component.html',
  styleUrls: ['./workout-detail.component.scss']
})
export class WorkoutDetailComponent implements OnInit {
  workoutId: string | null = null;
  workout: Workout | null = null;
  exercises: Exercise[] = [];
  loading = true;
  error: string | null = null;

  formState = {
    isOpen: false,
    mode: 'create' as 'create' | 'edit',
    exercise: undefined as Exercise | undefined
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private exerciseService: ExerciseService,
    private workoutService: WorkoutService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.workoutId = params.get('id');
      if (this.workoutId) {
        this.loadWorkout();
      } else {
        this.error = 'ID do treino não fornecido';
        this.loading = false;
      }
    });
  }

  loadWorkout(): void {
    if (!this.workoutId) return;

    this.loading = true;
    this.error = null;

    this.workoutService.getWorkoutById(this.workoutId).subscribe({
      next: (workout) => {
        this.workout = workout;
        this.exercises = workout.exercises || [];
        this.loading = false;
        console.log('Workout carregado:', workout);
      },
      error: (error) => {
        console.error('Erro ao carregar workout:', error);
        this.error = 'Erro ao carregar o treino. Por favor, tente novamente.';
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  openExerciseForm(mode: 'create' | 'edit', exercise?: Exercise): void {
    this.formState = {
      isOpen: true,
      mode,
      exercise
    };
  }

  closeExerciseForm(): void {
    this.formState = {
      isOpen: false,
      mode: 'create',
      exercise: undefined
    };
  }

  saveExercise(data: any): void {
    if (!this.workoutId) return;

    const exerciseData: Exercise = {
      ...this.formState.exercise,
      ...data,
      workoutId: this.workoutId
    };

    if (this.formState.mode === 'edit' && this.formState.exercise?.id) {
      // Edit existing exercise
      this.exerciseService.updateExercise(this.formState.exercise.id, exerciseData).subscribe({
        next: (updatedExercise) => {
          // Update the exercise in the local array
          const index = this.exercises.findIndex(ex => ex.id === updatedExercise.id);
          if (index !== -1) {
            this.exercises[index] = updatedExercise;
          }
          this.loadWorkout(); // Reload to ensure consistency
        },
        error: (error) => {
          console.error('Erro ao atualizar exercício:', error);
          alert('Erro ao atualizar exercício. Tente novamente.');
        }
      });
    } else {
      // Create new exercise
      this.exerciseService.createExercise(exerciseData).subscribe({
        next: (newExercise) => {
          this.exercises.push(newExercise);
          this.loadWorkout(); // Reload to ensure consistency
        },
        error: (error) => {
          console.error('Erro ao criar exercício:', error);
          alert('Erro ao criar exercício. Tente novamente.');
        }
      });
    }
  }

  onAddExercise(): void {
    this.openExerciseForm('create');
  }

  onEditExercise(exercise: Exercise): void {
    this.openExerciseForm('edit', exercise);
  }

  onSelectExercise(id: string): void {
    console.log('Exercício selecionado:', id);
  }

  onDeleteExercise(id: string): void {
    console.log('Excluir exercício:', id);
  }

  onDuplicateExercise(exercise: Exercise): void {
    console.log('Duplicar exercício:', exercise);
  }
}
