import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getWorkouts(): Observable<Workout[]> {
    return this.http.get<Workout[]>(this.apiUrl);
  }

  getWorkoutById(id: string): Observable<Workout> {
    return this.http.get<Workout>(`${this.apiUrl}/${id}`);
  }

  createWorkout(workout: Partial<Workout>): Observable<Workout> {
    return this.http.post<Workout>(this.apiUrl, workout, this.httpOptions);
  }

  updateWorkout(id: string, workout: Partial<Workout>): Observable<Workout> {
    return this.http.put<Workout>(`${this.apiUrl}/${id}`, workout, this.httpOptions);
  }

  deleteWorkout(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
