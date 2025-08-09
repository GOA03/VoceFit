import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, Save, X } from 'lucide-angular';
import { Workout } from '../../models/workout.model';

export interface WorkoutFormData {
  title: string;
}

@Component({
  selector: 'app-workout-form',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './workout-form.component.html',
  styleUrl: './workout-form.component.scss'
})
export class WorkoutFormComponent implements OnChanges {
  @Input() isOpen = false;
  @Input() initialData?: Workout;
  @Input() isEditing = false;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<WorkoutFormData>();

  // Lucide icons
  SaveIcon = Save;
  XIcon = X;

  formData: WorkoutFormData = {
    title: ''
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['initialData'] || changes['isOpen']) {
      this.resetForm();
    }
  }

  private resetForm(): void {
    if (this.initialData) {
      this.formData = {
        title: this.initialData.title || ''
      };
    } else {
      this.formData = {
        title: ''
      };
    }
  }

  handleSubmit(): void {
    if (this.formData.title.trim()) {
      this.onSave.emit(this.formData);
      this.onClose.emit();
    }
  }

  onOverlayClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.onClose.emit();
    }
  }
}
