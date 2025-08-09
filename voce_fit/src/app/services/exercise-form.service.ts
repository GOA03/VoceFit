import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Exercise } from '../models/exercise.model';

export interface ExerciseFormState {
  isOpen: boolean;
  mode: 'create' | 'edit';
  exercise?: Exercise;
}

@Injectable({
  providedIn: 'root'
})
export class ExerciseFormService {
  private formStateSubject = new BehaviorSubject<ExerciseFormState>({
    isOpen: false,
    mode: 'create'
  });

  formState$ = this.formStateSubject.asObservable();

  openForm(mode: 'create' | 'edit', exercise?: Exercise): void {
    this.formStateSubject.next({
      isOpen: true,
      mode,
      exercise
    });
  }

  closeForm(): void {
    this.formStateSubject.next({
      isOpen: false,
      mode: 'create'
    });
  }

  getFormState(): ExerciseFormState {
    return this.formStateSubject.value;
  }
}
