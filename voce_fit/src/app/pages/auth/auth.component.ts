import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LucideAngularModule, Eye, EyeOff, Mail, Lock, User, LogIn, UserPlus } from 'lucide-angular';
import { AlertService } from '../../services/alert.service';
import { AuthService, LoginRequest, RegisterRequest } from '../../services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {
  // Lucide icons
  EyeIcon = Eye;
  EyeOffIcon = EyeOff;
  MailIcon = Mail;
  LockIcon = Lock;
  UserIcon = User;
  LogInIcon = LogIn;
  UserPlusIcon = UserPlus;

  isLoginMode = true;
  showPassword = false;
  showConfirmPassword = false;
  isLoading = false;

  loginForm: FormGroup;
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private alertService: AlertService,
    private authService: AuthService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }

    if (confirmPassword?.hasError('passwordMismatch')) {
      confirmPassword.setErrors(null);
    }

    return null;
  }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.showPassword = false;
    this.showConfirmPassword = false;
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onSubmit() {
    const form = this.isLoginMode ? this.loginForm : this.registerForm;

    if (form.invalid) {
      this.markFormGroupTouched(form);
      return;
    }

    this.isLoading = true;

    if (this.isLoginMode) {
      const loginData: LoginRequest = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password
      };

      this.authService.login(loginData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.alertService.success(`Bem-vindo, ${this.authService.getCurrentUser()?.name || 'Usuário'}!`);
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.isLoading = false;
          this.alertService.error('Credenciais inválidas!');
        }
      });
    } else {
      const registerData: RegisterRequest = {
        name: this.registerForm.value.name,
        email: this.registerForm.value.email,
        password: this.registerForm.value.password
      };

      this.authService.register(registerData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.alertService.success('Conta criada com sucesso! Faça login para continuar.');
          this.isLoginMode = true;
          this.registerForm.reset();
        },
        error: (error) => {
          this.isLoading = false;
          this.alertService.error('Erro ao criar conta. Tente novamente.');
        }
      });
    }
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  getErrorMessage(fieldName: string): string {
    const form = this.isLoginMode ? this.loginForm : this.registerForm;
    const field = form.get(fieldName);

    if (field?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)} é obrigatório`;
    }

    if (field?.hasError('email')) {
      return 'Email inválido';
    }

    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `${this.getFieldLabel(fieldName)} deve ter pelo menos ${minLength} caracteres`;
    }

    if (field?.hasError('passwordMismatch')) {
      return 'As senhas não coincidem';
    }

    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      name: 'Nome',
      email: 'Email',
      password: 'Senha',
      confirmPassword: 'Confirmação de senha'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const form = this.isLoginMode ? this.loginForm : this.registerForm;
    const field = form.get(fieldName);
    return !!(field?.invalid && field?.touched);
  }
}
