import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { Subject, takeUntil } from 'rxjs';
import { AuthService, User } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-gym-header',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './gym-header.component.html',
  styleUrls: ['./gym-header.component.scss']
})
export class GymHeaderComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  currentUser: User | null = null;
  isAuthenticated = false;

  constructor(
    private authService: AuthService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Observar mudanças no estado de autenticação
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.currentUser = user;
      });

    this.authService.isAuthenticated$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isAuth => {
        this.isAuthenticated = isAuth;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Getter para a primeira letra maiúscula do nome do usuário
  get firstLetter(): string {
    return this.currentUser?.name?.charAt(0).toUpperCase() ?? '?';
  }

  get userName(): string {
    return this.currentUser?.name ?? 'Usuário';
  }

  onLogin(): void {
    this.router.navigate(['/auth']);
  }

  onLogout(): void {
    this.authService.logout();
    this.alertService.success('Logout realizado com sucesso!');
    this.router.navigate(['/auth']);
  }
}
