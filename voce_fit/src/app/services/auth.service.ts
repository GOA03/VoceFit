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
  user: User;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  public currentUser$ = this.currentUserSubject.asObservable();
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {
    // Verificar se há um usuário logado no localStorage ao inicializar
    this.checkStoredAuth();
  }

  private checkStoredAuth(): void {
    const storedUser = localStorage.getItem('vocefit_user');
    const storedToken = localStorage.getItem('vocefit_token');

    if (storedUser && storedToken) {
      try {
        const user = JSON.parse(storedUser);
        this.currentUserSubject.next(user);
        this.isAuthenticatedSubject.next(true);
      } catch (error) {
        // Se houver erro ao parsear, limpar o localStorage
        this.clearStoredAuth();
      }
    }
  }

  private clearStoredAuth(): void {
    localStorage.removeItem('vocefit_user');
    localStorage.removeItem('vocefit_token');
  }

  private setStoredAuth(user: User, token: string): void {
    localStorage.setItem('vocefit_user', JSON.stringify(user));
    localStorage.setItem('vocefit_token', token);
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    // Simulação de API - remover quando integrar com backend real
    return this.simulateLogin(credentials).pipe(
      map((response: AuthResponse) => {
        this.currentUserSubject.next(response.user);
        this.isAuthenticatedSubject.next(true);
        this.setStoredAuth(response.user, response.token);
        return response;
      }),
      catchError(error => {
        console.error('Erro no login:', error);
        return throwError(() => error);
      })
    );

    // Implementação real da API (descomente quando o backend estiver pronto)
    /*
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      map((response: AuthResponse) => {
        this.currentUserSubject.next(response.user);
        this.isAuthenticatedSubject.next(true);
        this.setStoredAuth(response.user, response.token);
        return response;
      }),
      catchError(error => {
        console.error('Erro no login:', error);
        return throwError(() => error);
      })
    );
    */
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    // Simulação de API - remover quando integrar com backend real
    return this.simulateRegister(userData).pipe(
      map((response: AuthResponse) => {
        // Não fazer login automático após registro
        return response;
      }),
      catchError(error => {
        console.error('Erro no registro:', error);
        return throwError(() => error);
      })
    );

    // Implementação real da API (descomente quando o backend estiver pronto)
    /*
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      map((response: AuthResponse) => {
        return response;
      }),
      catchError(error => {
        console.error('Erro no registro:', error);
        return throwError(() => error);
      })
    );
    */
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
    return localStorage.getItem('vocefit_token');
  }

  // Métodos de simulação - remover quando integrar com backend real
  private simulateLogin(credentials: LoginRequest): Observable<AuthResponse> {
    return of(null).pipe(
      delay(1000), // Simular delay da rede
      map(() => {
        // Validação simples para demonstração
        if (credentials.email === 'demo@vocefit.com' && credentials.password === '123456') {
          const user: User = {
            id: '1',
            name: 'Usuário Demo',
            email: credentials.email,
            createdAt: new Date().toISOString()
          };
          
          const response: AuthResponse = {
            user,
            token: 'demo-jwt-token-' + Date.now()
          };
          
          return response;
        } else {
          throw new Error('Credenciais inválidas');
        }
      })
    );
  }

  private simulateRegister(userData: RegisterRequest): Observable<AuthResponse> {
    return of(null).pipe(
      delay(1000), // Simular delay da rede
      map(() => {
        // Simulação simples - sempre sucesso
        const user: User = {
          id: Date.now().toString(),
          name: userData.name,
          email: userData.email,
          createdAt: new Date().toISOString()
        };
        
        const response: AuthResponse = {
          user,
          token: 'demo-jwt-token-' + Date.now()
        };
        
        return response;
      })
    );
  }
}