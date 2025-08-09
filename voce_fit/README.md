# VoceFit

## Sobre o Projeto

VoceFit é uma aplicação web desenvolvida com Angular que ajuda os usuários a gerenciar seus exercícios físicos e acompanhar seu progresso fitness. A plataforma oferece uma interface intuitiva para criar e monitorar rotinas de exercícios personalizadas.

## Funcionalidades

- Cadastro e gerenciamento de exercícios
- Criação de rotinas de treino personalizadas
- Acompanhamento de progresso
- Interface responsiva para uso em diferentes dispositivos

## Tecnologias Utilizadas

- Angular (versão 18.0.0)
- TypeScript
- SCSS
- HTML5

## Pré-requisitos

- Node.js (versão 14.x ou superior)
- npm (versão 6.x ou superior)

## Instalação e Desenvolvimento

1. Clone o repositório:
   ```bash
   git clone https://github.com/GOA03/VoceFit
   cd voce_fit
   ```

2. Instale as dependências:
   ```bash
   npm install
   ```

3. Inicie o servidor de desenvolvimento:
   ```bash
   ng serve
   ```

4. Acesse a aplicação em seu navegador:
   ```
   http://localhost:4200
   ```

## Comandos Úteis

### Gerar Componentes

Run `ng generate component component-name` para gerar um novo componente. Você também pode usar `ng generate directive|pipe|service|class|guard|interface|enum|module`.

### Build

Run `ng build` para compilar o projeto. Os artefatos de build serão armazenados no diretório `dist/`.

### Testes Unitários

Run `ng test` para executar os testes unitários via [Karma](https://karma-runner.github.io).

### Testes End-to-End

Run `ng e2e` para executar os testes end-to-end através de uma plataforma de sua escolha.

## Estrutura do Projeto

- `/src/app/components`: Componentes reutilizáveis
- `/src/app/models`: Interfaces e classes de modelo
- `/src/app/pages`: Componentes de página
- `/src/app/services`: Serviços para lógica de negócios e comunicação com APIs

## Como Contribuir

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova funcionalidade'`)
4. Faça push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Ajuda Adicional

Para obter mais ajuda sobre o Angular CLI, use `ng help` ou consulte a página [Angular CLI Overview and Command Reference](https://angular.io/cli).

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE para mais detalhes.
