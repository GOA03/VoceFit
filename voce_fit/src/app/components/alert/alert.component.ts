import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { AnimationBuilder, AnimationFactory, AnimationPlayer, style, animate } from '@angular/animations';

export type AlertType = 'success' | 'error' | 'info' | 'warning';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss'
})
export class AlertComponent implements OnInit, OnDestroy {
  @Input() message = '';
  @Input() type: AlertType = 'info';
  @Input() duration = 5000; // Duração em ms (5s padrão)
  @Input() showIcon = true;
  @Input() closable = true;
  @Input() autoClose = true;
  
  @Output() closed = new EventEmitter<void>();
  
  visible = false;
  private autoCloseTimeout?: number;
  private player?: AnimationPlayer;
  
  constructor(private builder: AnimationBuilder) {}
  
  ngOnInit(): void {
    this.show();
    
    if (this.autoClose && this.duration > 0) {
      this.autoCloseTimeout = window.setTimeout(() => {
        this.close();
      }, this.duration);
    }
  }
  
  ngOnDestroy(): void {
    if (this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
    }
    
    if (this.player) {
      this.player.destroy();
    }
  }
  
  show(): void {
    this.visible = true;
    this.createShowAnimation().play();
  }
  
  close(): void {
    if (!this.visible) return;
    
    const player = this.createHideAnimation();
    player.onDone(() => {
      this.visible = false;
      this.closed.emit();
    });
    player.play();
  }
  
  get iconName(): string {
    switch (this.type) {
      case 'success': return 'check';
      case 'error': return 'x';
      case 'warning': return 'zap';
      case 'info': 
      default: return 'info';
    }
  }
  
  private createShowAnimation(): AnimationPlayer {
    const factory = this.builder.build([
      style({ opacity: 0, transform: 'translateY(-20px)' }),
      animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
    ]);
    
    return factory.create(document.querySelector('.alert-container'));
  }
  
  private createHideAnimation(): AnimationPlayer {
    const factory = this.builder.build([
      style({ opacity: 1 }),
      animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(-20px)' }))
    ]);
    
    return factory.create(document.querySelector('.alert-container'));
  }
}
