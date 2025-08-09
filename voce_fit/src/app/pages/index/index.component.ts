import { Component } from '@angular/core';
import { GymHeaderComponent } from '../../components/gym-header/gym-header.component';
import { WorkoutListComponent } from '../../components/workout-list/workout-list.component';
import { FloatingAddButtonComponent } from "../../components/floating-add-button/floating-add-button.component";

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [
    GymHeaderComponent,
    WorkoutListComponent,
    FloatingAddButtonComponent
  ],
  templateUrl: './index.component.html',
  styleUrl: './index.component.scss'
})
export class IndexComponent {

  constructor() {}

  onAddWorkout(): void {

    // Abrir componente (descomente se preferir)
    console.log('Adicionando novo treino...');
  }
}
