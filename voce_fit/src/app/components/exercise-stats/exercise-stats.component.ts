import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { Exercise } from '../../models/exercise.model';

@Component({
  selector: 'app-exercise-stats',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './exercise-stats.component.html',
  styleUrls: ['./exercise-stats.component.scss']
})
export class ExerciseStatsComponent {
  @Input() exercises: Exercise[] = [];

  get totalExercises(): number {
    return this.exercises.length;
  }

  get totalTime(): string {
    return `tempo holder`;
  }
}
