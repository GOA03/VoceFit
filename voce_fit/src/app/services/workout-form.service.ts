import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Workout } from '../models/workout.model';

export interface WorkoutFormState {
  isOpen: boolean;
  mode: 'create' | 'edit';
  workout?: Workout;
}

@Injectable({
  providedIn: 'root'
})
export class WorkoutFormService {
  private formStateSubject = new BehaviorSubject<WorkoutFormState>({
    isOpen: false,
    mode: 'create'
  });

  formState$ = this.formStateSubject.asObservable();

  openForm(mode: 'create' | 'edit', workout?: Workout): void {
    this.formStateSubject.next({
      isOpen: true,
      mode,
      workout
    });
  }

  closeForm(): void {
    this.formStateSubject.next({
      isOpen: false,
      mode: 'create'
    });
  }

  getFormState(): WorkoutFormState {
    return this.formStateSubject.value;
  }
}