# 🏋️‍♂️ VoceFit API

Uma API RESTful moderna e robusta para gerenciamento de treinos e exercícios físicos, desenvolvida com Spring Boot e arquitetura limpa.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso](#uso)
- [Documentação da API](#documentação-da-api)
- [Banco de Dados](#banco-de-dados)
- [Testes](#testes)
- [Contribuição](#contribuição)
- [Licença](#licença)

## 🎯 Visão Geral

O VoceFit API é uma solução completa para gerenciamento de treinos e exercícios, permitindo que usuários criem, organizem e reordenen seus treinos de forma eficiente. O sistema foi projetado seguindo os princípios de Clean Architecture e Domain-Driven Design (DDD), garantindo escalabilidade e manutenibilidade.

### Principais Características:
- ✅ CRUD completo para treinos (Workouts) e exercícios (Exercises)
- ✅ Reordenação inteligente de exercícios com transações atômicas
- ✅ Duplicação de treinos com todos os exercícios
- ✅ Validações robustas com mensagens de erro claras
- ✅ API RESTful com padrões REST
- ✅ Documentação clara e exemplos práticos

## 🚀 Tecnologias

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 3.5.4 | Framework principal |
| **Spring Data JPA** | Latest | Persistência de dados |
| **PostgreSQL** | 15+ | Banco de dados relacional |
| **Lombok** | Latest | Redução de boilerplate |
| **Maven** | Latest | Gerenciamento de dependências |
| **Hibernate** | Latest | ORM e mapeamento objeto-relacional |
| **Validation** | Latest | Validações de entrada |

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas limpa, separando responsabilidades de forma clara:

```
com.auer.voce_fit/
├── domain/              # Camada de Domínio (Core Business)
│   ├── entities/        # Entidades JPA
│   ├── dtos/           # Data Transfer Objects
│   ├── exceptions/     # Exceções customizadas
│   └── repositories/   # Interfaces de repositório
├── application/        # Camada de Aplicação
│   └── services/       # Lógica de aplicação
├── infrastructure/     # Camada de Infraestrutura
│   ├── config/         # Configurações
│   ├── controller/     # Controllers REST
│   └── persistence/    # Implementações JPA
└── usecases/          # Casos de Uso (Clean Architecture)
    ├── exercise/
    └── workout/
```

### Princípios Arquiteturais:
- **Separação de Concerns**: Cada camada tem responsabilidade única
- **Inversão de Dependência**: Dependências apontam para dentro
- **Testabilidade**: Facilita testes unitários e de integração
- **Escalabilidade**: Arquitetura preparada para crescimento

## ✨ Funcionalidades

### 🏋️‍♂️ Gestão de Treinos (Workouts)
- **Criar treino**: `POST /api/workouts`
- **Listar todos treinos**: `GET /api/workouts`
- **Buscar treino por ID**: `GET /api/workouts/{id}`
- **Atualizar treino**: `PUT /api/workouts/{id}`
- **Deletar treino**: `DELETE /api/workouts/{id}`
- **Duplicar treino**: `POST /api/workouts/{id}/duplicate`

### 💪 Gestão de Exercícios
- **Criar exercício**: `POST /api/exercises`
- **Listar todos exercícios**: `GET /api/exercises`
- **Buscar exercício por ID**: `GET /api/exercises/{id}`
- **Atualizar exercício**: `PUT /api/exercises/{id}`
- **Deletar exercício**: `DELETE /api/exercises/{id}`

### 🔄 Reordenação Inteligente
- **Reordenar exercícios em lote**: `PUT /api/workouts/{workoutId}/exercises/order`
- Sistema inteligente que ajusta automaticamente as posições
- Transações atômicas garantem consistência
- Validações preventivas contra conflitos

## 🛠️ Instalação

### Pré-requisitos
- Java 21 ou superior
- Maven 3.8+
- PostgreSQL 15+
- Git

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/GOA03/VoceFit.git
cd voce-fit-api
```

2. **Configure o banco de dados**
```sql
-- Criar banco de dados
CREATE DATABASE vocefit_db;
CREATE USER {USER} WITH PASSWORD {PASSWORD};
GRANT ALL PRIVILEGES ON DATABASE vocefit_db TO {USER};
```

3. **Configure as variáveis de ambiente**
```bash
# application.properties já está configurado para:
# spring.datasource.url=jdbc:postgresql://localhost:5432/vocefit_db
# spring.datasource.username={USER}
# spring.datasource.password={PASSWORD}
# server.port=8081
```

4. **Instale as dependências**
```bash
mvn clean install
```

5. **Execute a aplicação**
```bash
# Modo desenvolvimento
mvn spring-boot:run

# Ou execute o JAR
java -jar target/voce-fit-0.0.1-SNAPSHOT.jar
```

## ⚙️ Configuração

### Configuração do Banco de Dados
O sistema usa PostgreSQL com as seguintes configurações padrão:

```properties
# application.properties
spring.application.name=voce-fit
spring.datasource.url=jdbc:postgresql://localhost:5432/vocefit_db
spring.datasource.username={USER}
spring.datasource.password={PASSWORD}
server.port=8081
```

### Configuração de CORS
A aplicação está configurada para aceitar requisições de qualquer origem em ambiente de desenvolvimento:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

## 📖 Uso

### Exemplos de Requisições

#### 1. Criar um novo treino
```bash
curl -X POST http://localhost:8081/api/workouts \
  -H "Content-Type: application/json" \
  -d '{"title": "Treino de Peito e Tríceps"}'
```

**Resposta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Treino de Peito e Tríceps",
  "exerciseCount": 0,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### 2. Adicionar exercícios ao treino
```bash
curl -X POST http://localhost:8081/api/exercises \
  -H "Content-Type: application/json" \
  -d '{
    "workoutId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Supino Reto",
    "sets": "4",
    "reps": "10-12",
    "weight": "60kg",
    "restTime": 90,
    "notes": "Foco na execução lenta",
    "sequence": 1
  }'
```

#### 3. Reordenar exercícios
```bash
curl -X PUT http://localhost:8081/api/workouts/550e8400-e29b-41d4-a716-446655440000/exercises/order \
  -H "Content-Type: application/json" \
  -d '[
    {"exerciseId": "exercise-uuid-1", "newSequence": 3},
    {"exerciseId": "exercise-uuid-2", "newSequence": 1},
    {"exerciseId": "exercise-uuid-3", "newSequence": 2}
  ]'
```

#### 4. Duplicar um treino completo
```bash
curl -X POST http://localhost:8081/api/workouts/550e8400-e29b-41d4-a716-446655440000/duplicate
```

## 📚 Documentação da API

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| **Workouts** |
| GET | `/api/workouts` | Listar todos treinos |
| GET | `/api/workouts/{id}` | Buscar treino específico |
| POST | `/api/workouts` | Criar novo treino |
| PUT | `/api/workouts/{id}` | Atualizar treino |
| DELETE | `/api/workouts/{id}` | Deletar treino |
| POST | `/api/workouts/{id}/duplicate` | Duplicar treino |
| PUT | `/api/workouts/{id}/exercises/order` | Reordenar exercícios |
| **Exercises** |
| GET | `/api/exercises` | Listar todos exercícios |
| GET | `/api/exercises/{id}` | Buscar exercício específico |
| POST | `/api/exercises` | Criar novo exercício |
| PUT | `/api/exercises/{id}` | Atualizar exercício |
| DELETE | `/api/exercises/{id}` | Deletar exercício |

### Estrutura dos DTOs

#### WorkoutRequestDTO
```json
{
  "title": "string"
}
```

#### ExerciseRequestDTO
```json
{
  "workoutId": "uuid",
  "name": "string",
  "sets": "string",
  "reps": "string",
  "weight": "string",
  "restTime": 90,
  "notes": "string",
  "sequence": 1
}
```

#### ReorderRequestDTO
```json
{
  "exerciseId": "uuid",
  "newSequence": 2
}
```

## 🗄️ Banco de Dados

### Modelo de Dados

#### Tabela `workouts`
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Chave primária |
| title | VARCHAR(255) | Nome do treino |
| created_at | TIMESTAMP | Data de criação |
| updated_at | TIMESTAMP | Última atualização |

#### Tabela `exercises`
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Chave primária |
| workout_id | UUID | FK para workouts |
| name | VARCHAR(255) | Nome do exercício |
| sets | VARCHAR(50) | Número de séries |
| reps | VARCHAR(50) | Número de repetições |
| weight | VARCHAR(50) | Peso utilizado |
| rest_time | INTEGER | Tempo de descanso (segundos) |
| notes | TEXT | Observações |
| sequence | INTEGER | Ordem no treino |
| created_at | TIMESTAMP | Data de criação |
| updated_at | TIMESTAMP | Última atualização |

### Constraints
- `UNIQUE(workout_id, sequence)`: Garante que não há duplicatas de sequência por treino

## 🧪 Testes

### Executar testes
```bash
# Testes unitários
mvn test

# Testes com cobertura
mvn test jacoco:report

# Testes de integração
mvn verify
```

### Estrutura de Testes
```
src/test/java/
└── com.auer.voce_fit/
    └── VoceFitApplicationTests.java
```

## 🤝 Contribuição

1. **Fork** o projeto
2. **Crie** sua feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. **Abra** um Pull Request

### Diretrizes de Código
- Siga os padrões de código Java
- Use Lombok para reduzir boilerplate
- Mantenha a arquitetura limpa
- Adicione testes para novas funcionalidades
- Documente métodos públicos

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👥 Autor

**GOA03** - *Desenvolvedor Principal* - [GitHub](https://github.com/GOA03)

## 🙏 Agradecimentos
- Spring Boot team pela excelente framework
- PostgreSQL pela robustez do banco de dados
- Comunidade Java pelas melhores práticas

---

<div align="center">
  <p>Desenvolvido com ❤️ para a comunidade fitness</p>
  <p><strong>VoceFit API</strong> - Transformando treinos em experiências digitais</p>
</div>
