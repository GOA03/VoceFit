import { Component } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-gym-header',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './gym-header.component.html',
  styleUrls: ['./gym-header.component.scss']
})
export class GymHeaderComponent {
  userName = 'Guilherme';

  // Getter para a primeira letra maiúscula do nome do usuário
  get firstLetter(): string {
    return this.userName?.charAt(0).toUpperCase() ?? '?';
  }

  onLogout(): void {
    console.log('Logout clicado');
    // Adicione sua lógica de logout ou navegação aqui
  }
}
