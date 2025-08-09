import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';

export interface Exercise {
  id: string;
  name: string;
  sets: string;
  reps: string;
  weight: string;
  restTime: number;
  notes?: string;
  workoutId: string;
  completed?: boolean;
}

@Component({
  selector: 'app-exercise-card',
  standalone: true,
  imports: [CommonModule, NgIf, LucideAngularModule],
  templateUrl: './exercise-card.component.html',
  styleUrls: ['./exercise-card.component.scss']
})
export class ExerciseCardComponent {
  private _exercise!: Exercise;
  @Input()
  set exercise(value: Exercise) {
    this._exercise = value;
  }
  get exercise(): Exercise {
    return this._exercise;
  }
  @Output() edit = new EventEmitter<Exercise>();
  @Output() delete = new EventEmitter<string>();
  @Output() duplicate = new EventEmitter<Exercise>();
  @Output() conclude = new EventEmitter<Exercise>();

  dropdownOpen = false;

  get stats() {
    return [
      { label: 'Séries', value: this.exercise.sets },
      { label: 'Repetições', value: this.exercise.reps },
      { label: 'Carga', value: this.exercise.weight + ' kg' }
    ];
  }

  onConclude() {
    this.conclude.emit(this.exercise);
  }

  get isCompleted(): boolean {
    return this.exercise.completed || false;
  }

  toggleDropdown(event: MouseEvent) {
    event.stopPropagation();
    this.dropdownOpen = !this.dropdownOpen;
  }

  closeDropdown() {
    this.dropdownOpen = false;
  }

  onEdit() {
    this.edit.emit(this.exercise);
    this.closeDropdown();
  }

  onDelete() {
    this.delete.emit(this.exercise.id);
    this.closeDropdown();
  }

  onDuplicate() {
    this.duplicate.emit(this.exercise);
    this.closeDropdown();
  }
}
