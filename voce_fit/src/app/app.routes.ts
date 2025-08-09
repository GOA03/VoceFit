import { Routes } from '@angular/router';
import { IndexComponent } from './pages/index/index.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { WorkoutDetailComponent } from './pages/workout-detail/workout-detail.component';

export const routes: Routes = [
  { path: '', component: IndexComponent },
  { path: 'workouts/:id', component: WorkoutDetailComponent },
  { path: '**', component: NotFoundComponent }
];
