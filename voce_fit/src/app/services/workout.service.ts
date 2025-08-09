import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Workout {
  id: string;
  title: string;
  createdAt: string;
  updatedAt: string;
  amount?: number;
  exercises?: any[];
}

@Injectable({
  providedIn: 'root'
})
export class WorkoutService {
  private apiUrl = 'http://localhost:8081/api/workouts';

  constructor(private http: HttpClient) { }

  getWorkouts(): Observable<Workout[]> {
    return this.http.get<Workout[]>(this.apiUrl);
  }

  getWorkoutById(id: string): Observable<Workout> {
    return this.http.get<Workout>(`${this.apiUrl}/${id}`);
  }
}
