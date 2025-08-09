import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

export type FloatingButtonVariant = 'default' | 'success' | 'primary';

@Component({
  selector: 'app-floating-add-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './floating-add-button.component.html',
  styleUrls: ['./floating-add-button.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class]': 'cssClass'
  }
})
export class FloatingAddButtonComponent {
  @Input() ariaLabel = 'Adicionar novo item';
  @Input() variant: FloatingButtonVariant = 'default';
  @Input() disabled = false;

  @Output() buttonClick = new EventEmitter<void>();

  get cssClass(): string {
    const classes = ['floating-add-btn'];

    if (this.variant !== 'default') {
      classes.push(this.variant);
    }

    if (this.disabled) {
      classes.push('disabled');
    }

    return classes.join(' ');
  }

  onClick(): void {
    if (!this.disabled) {
      this.buttonClick.emit();
    }
  }
}
