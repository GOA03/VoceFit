import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Exercise {
  id: string;
  name: string;
  sets: string;
  reps: string;
  weight: string;
  restTime: number;
  notes?: string;
  workoutId: string;
}

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {

  private baseUrl = 'http://localhost:8081/api/exercises';
  constructor(private http: HttpClient) { }

  getExercisesByWorkoutId(workoutId: string): Observable<Exercise[]> {
    return this.http.get<Exercise[]>(`${this.baseUrl}/${workoutId}`);
  }

  getExerciseById(id: string): Observable<Exercise> {
    return this.http.get<Exercise>(`${this.baseUrl}/exercise/${id}`);
  }

  createExercise(exercise: Exercise): Observable<Exercise> {
    return this.http.post<Exercise>(this.baseUrl, exercise);
  }

  updateExercise(id: string, exercise: Exercise): Observable<Exercise> {
    return this.http.put<Exercise>(`${this.baseUrl}/${id}`, exercise);
  }

  deleteExercise(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
