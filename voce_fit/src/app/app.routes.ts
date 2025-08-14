import { Routes } from '@angular/router';
import { IndexComponent } from './pages/index/index.component';
import { WorkoutDetailComponent } from './pages/workout-detail/workout-detail.component';
import { AuthComponent } from './pages/auth/auth.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { 
    path: '', 
    component: IndexComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'workout/:id', 
    component: WorkoutDetailComponent,
    canActivate: [AuthGuard]
  },
  { path: 'auth', component: AuthComponent },
  { path: '**', redirectTo: '' }
];
