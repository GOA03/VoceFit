import {
  Component,
  Input,
  Output,
  EventEmitter,
  ChangeDetectionStrategy,
  ElementRef,
  ViewChild,
  OnDestroy,
  AfterViewInit
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';

export interface Workout {
  id: string;
  title: string;
  amount: number;
  createdAt: string;
  updatedAt: string;
}

@Component({
  selector: 'app-workout-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './workout-card.component.html',
  styleUrls: ['./workout-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class WorkoutCardComponent implements AfterViewInit, OnDestroy {
  @Input() workout: Workout | undefined;
  @Output() edit = new EventEmitter<string>();
  @Output() delete = new EventEmitter<string>();
  @Output() duplicate = new EventEmitter<string>();

  @ViewChild('cardRef') cardRef!: ElementRef;

  dropdownOpen = false;

  private boundClickHandler = this.handleClickOutside.bind(this);

  constructor(private router: Router) {}

  ngAfterViewInit(): void {
    document.addEventListener('click', this.boundClickHandler);
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', this.boundClickHandler);
  }

  toggleDropdown(event: MouseEvent): void {
    event.stopPropagation();
    this.dropdownOpen = !this.dropdownOpen;
  }

  onCardClickWrapper(event: MouseEvent): void {
    if (this.dropdownOpen) {
      event.stopPropagation();
      return;
    }
    this.navigateToWorkout();
  }

  private navigateToWorkout(): void {
    if (this.workout) {
      this.router.navigate(['/workouts', this.workout.id]);
    }
  }

  handleClickOutside(event: MouseEvent): void {
    if (!this.cardRef?.nativeElement.contains(event.target) && this.dropdownOpen) {
      this.dropdownOpen = false;
    }
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Escape' && this.dropdownOpen) {
      this.dropdownOpen = false;
      event.preventDefault();
      event.stopPropagation();
    }
  }

  onEdit(event: MouseEvent): void {
    event.stopPropagation();
    if (this.workout) this.edit.emit(this.workout.id);
    this.dropdownOpen = false;
  }

  onDelete(event: MouseEvent): void {
    event.stopPropagation();
    if (this.workout) this.delete.emit(this.workout.id);
    this.dropdownOpen = false;
  }

  onDuplicate(event: MouseEvent): void {
    event.stopPropagation();
    if (this.workout) this.duplicate.emit(this.workout.id);
    this.dropdownOpen = false;
  }

  get cardClass(): string {
    return this.dropdownOpen ? 'dropdown-active' : '';
  }
}
