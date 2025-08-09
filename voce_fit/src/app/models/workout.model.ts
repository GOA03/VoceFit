import { Exercise } from './exercise.model';

export interface Workout {
  id: string;
  title: string;
  amount?: number;
  exercises?: Exercise[];
  createdAt?: string;
  updatedAt?: string;
}
