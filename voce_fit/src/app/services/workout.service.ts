import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

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

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  getWorkouts(): Observable<Workout[]> {
    return this.http.get<Workout[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getWorkoutById(id: string): Observable<Workout> {
    return this.http.get<Workout>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  createWorkout(workout: Partial<Workout>): Observable<Workout> {
    return this.http.post<Workout>(this.apiUrl, workout, { headers: this.getHeaders() });
  }

  updateWorkout(id: string, workout: Partial<Workout>): Observable<Workout> {
    return this.http.put<Workout>(`${this.apiUrl}/${id}`, workout, { headers: this.getHeaders() });
  }

  deleteWorkout(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  duplicateWorkout(id: string): Observable<Workout> {
    return this.http.post<Workout>(`${this.apiUrl}/${id}/duplicate`, {}, { headers: this.getHeaders() });
  }
}
