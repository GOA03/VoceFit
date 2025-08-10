import { Injectable, ComponentRef, ApplicationRef, createComponent, EnvironmentInjector } from '@angular/core';
import { AlertComponent, AlertType } from '../components/alert/alert.component';

export interface AlertOptions {
  message: string;
  type?: AlertType;
  duration?: number;
  showIcon?: boolean;
  closable?: boolean;
  autoClose?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private alertRefs: ComponentRef<AlertComponent>[] = [];

  constructor(
    private appRef: ApplicationRef,
    private injector: EnvironmentInjector
  ) {}

  /**
   * Exibe um alerta com as opções especificadas
   * @param options Opções do alerta
   * @returns Referência ao componente de alerta criado
   */
  show(options: AlertOptions): ComponentRef<AlertComponent> {
    const alertRef = this.createAlertComponent(options);
    
    // Adiciona o componente à lista de alertas ativos
    this.alertRefs.push(alertRef);
    
    // Configura o evento de fechamento para remover o componente
    alertRef.instance.closed.subscribe(() => {
      this.removeAlert(alertRef);
    });
    
    return alertRef;
  }

  /**
   * Exibe um alerta de sucesso
   * @param message Mensagem do alerta
   * @param options Opções adicionais do alerta
   */
  success(message: string, options: Partial<AlertOptions> = {}): ComponentRef<AlertComponent> {
    return this.show({
      message,
      type: 'success',
      ...options
    });
  }

  /**
   * Exibe um alerta de erro
   * @param message Mensagem do alerta
   * @param options Opções adicionais do alerta
   */
  error(message: string, options: Partial<AlertOptions> = {}): ComponentRef<AlertComponent> {
    return this.show({
      message,
      type: 'error',
      ...options
    });
  }

  /**
   * Exibe um alerta de aviso
   * @param message Mensagem do alerta
   * @param options Opções adicionais do alerta
   */
  warning(message: string, options: Partial<AlertOptions> = {}): ComponentRef<AlertComponent> {
    return this.show({
      message,
      type: 'warning',
      ...options
    });
  }

  /**
   * Exibe um alerta informativo
   * @param message Mensagem do alerta
   * @param options Opções adicionais do alerta
   */
  info(message: string, options: Partial<AlertOptions> = {}): ComponentRef<AlertComponent> {
    return this.show({
      message,
      type: 'info',
      ...options
    });
  }

  /**
   * Remove todos os alertas ativos
   */
  clear(): void {
    this.alertRefs.forEach(ref => {
      ref.instance.close();
    });
  }

  /**
   * Cria um componente de alerta com as opções especificadas
   * @param options Opções do alerta
   * @returns Referência ao componente criado
   */
  private createAlertComponent(options: AlertOptions): ComponentRef<AlertComponent> {
    // Cria o componente de alerta
    const alertRef = createComponent(AlertComponent, {
      environmentInjector: this.injector
    });
    
    // Configura as propriedades do componente
    const instance = alertRef.instance;
    instance.message = options.message;
    instance.type = options.type || 'info';
    instance.duration = options.duration !== undefined ? options.duration : 5000;
    instance.showIcon = options.showIcon !== undefined ? options.showIcon : true;
    instance.closable = options.closable !== undefined ? options.closable : true;
    instance.autoClose = options.autoClose !== undefined ? options.autoClose : true;
    
    // Define a duração da animação da barra de progresso via CSS
    if (instance.autoClose && instance.duration > 0) {
      const container = alertRef.location.nativeElement as HTMLElement;
      container.style.setProperty('--duration', `${instance.duration}ms`);
    }
    
    // Anexa o componente ao DOM
    document.body.appendChild(alertRef.location.nativeElement);
    
    // Registra o componente na aplicação
    this.appRef.attachView(alertRef.hostView);
    
    return alertRef;
  }

  /**
   * Remove um alerta da lista de alertas ativos e do DOM
   * @param alertRef Referência ao componente de alerta
   */
  private removeAlert(alertRef: ComponentRef<AlertComponent>): void {
    // Remove o componente da lista de alertas ativos
    const index = this.alertRefs.indexOf(alertRef);
    if (index !== -1) {
      this.alertRefs.splice(index, 1);
    }
    
    // Remove o componente do DOM e da aplicação
    const element = alertRef.location.nativeElement as HTMLElement;
    if (element.parentNode) {
      element.parentNode.removeChild(element);
    }
    
    this.appRef.detachView(alertRef.hostView);
    alertRef.destroy();
  }
}
