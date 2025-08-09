import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { LucideAngularModule, Save, X } from "lucide-angular";

export interface Exercise {
  id?: string;
  name: string;
  sets: string;
  reps: string;
  weight: string;
  restTime: number;
  notes?: string;
}

export interface ExerciseFormData {
  name: string;
  sets: string;
  reps: string;
  weight: string;
  restTime: number;
  notes: string;
}

@Component({
  selector: 'app-exercise-form',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './exercise-form.component.html',
  styleUrls: ['./exercise-form.component.scss']
})
export class ExerciseFormComponent implements OnChanges {
  @Input() isOpen = false;
  @Input() initialData?: Exercise;
  @Input() isEditing = false;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<ExerciseFormData>();

  // Lucide icons
  SaveIcon = Save;
  XIcon = X;

  formData: ExerciseFormData = {
    name: '',
    sets: '3',
    reps: '10',
    weight: '0',
    restTime: 60,
    notes: ''
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['initialData'] || changes['isOpen']) {
      this.resetForm();
    }
  }

  private resetForm(): void {
    if (this.initialData) {
      this.formData = {
        name: this.initialData.name,
        sets: this.initialData.sets || '3',
        reps: this.initialData.reps || '10',
        weight: this.initialData.weight || '0',
        restTime: this.initialData.restTime || 60,
        notes: this.initialData.notes || ''
      };
    } else {
      this.formData = {
        name: '',
        sets: '3',
        reps: '10',
        weight: '0',
        restTime: 60,
        notes: ''
      };
    }
  }

  handleSubmit(): void {
    if (this.formData.name.trim()) {
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
