import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { map, catchError, delay } from 'rxjs/operators';

export interface User {
  id: string;
  name: string;
  email: string;
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  access_token: string;
  refresh_token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  public currentUser$ = this.currentUserSubject.asObservable();
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {
    // Verificar se há um usuário logado no localStorage ao inicializar
    this.checkStoredAuth();
  }

  private checkStoredAuth(): void {
    const storedAccessToken = localStorage.getItem('vocefit_access_token');
    const storedRefreshToken = localStorage.getItem('vocefit_refresh_token');

    if (storedAccessToken && storedRefreshToken) {
      try {
        const user = this.decodeUserFromToken(storedAccessToken);
        this.currentUserSubject.next(user);
        this.isAuthenticatedSubject.next(true);
      } catch (error) {
        // Se houver erro ao decodificar, limpar o localStorage
        this.clearStoredAuth();
      }
    }
  }

  private clearStoredAuth(): void {
    localStorage.removeItem('vocefit_access_token');
    localStorage.removeItem('vocefit_refresh_token');
  }

  private setStoredAuth(accessToken: string, refreshToken: string): void {
    localStorage.setItem('vocefit_access_token', accessToken);
    localStorage.setItem('vocefit_refresh_token', refreshToken);
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      map((response: AuthResponse) => {
        this.setStoredAuth(response.access_token, response.refresh_token);
        const user = this.decodeUserFromToken(response.access_token);
        this.currentUserSubject.next(user);
        this.isAuthenticatedSubject.next(true);
        return response;
      }),
      catchError(error => {
        console.error('Erro no login:', error);
        return throwError(() => error);
      })
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    // Mantém a implementação atual para registro (simulação ou futura integração)
    return of(null).pipe(
      delay(1000),
      map(() => {
        // Simulação simples - sempre sucesso
        const user: User = {
          id: Date.now().toString(),
          name: userData.name,
          email: userData.email,
          createdAt: new Date().toISOString()
        };
        const response: AuthResponse = {
          access_token: 'demo-access-token-' + Date.now(),
          refresh_token: 'demo-refresh-token-' + Date.now()
        };
        return response;
      })
    );
  }

  logout(): void {
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    this.clearStoredAuth();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('vocefit_access_token');
  }

  private decodeUserFromToken(token: string): User {
    // Decodifica o payload do JWT (parte do meio)
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    const payload = JSON.parse(payloadJson);

    // Extrai as informações do usuário do payload
    const user: User = {
      id: payload.userId || payload.sub || '',
      name: payload.name || '',
      email: payload.email || '',
      createdAt: '' // Não disponível no token, pode ser ajustado conforme necessário
    };
    return user;
  }
}
