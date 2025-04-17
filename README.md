
# 📝 API TodoList - Spring Boot

## 📌 Descrição do Projeto

O **API-TodoList-Spring-Boot** é um aplicativo de gerenciamento de tarefas e sprints desenvolvido em **Java** com **Spring Boot** para a lógica de negócios e persistência de dados, e **Swing** para a interface gráfica.

O objetivo é permitir que usuários gerenciem tarefas, organizem-nas em sprints e acompanhem o progresso com relatórios detalhados.

---

## 🔁 Fluxo da Aplicação

### 1. Interface de Usuário (Swing UI)
Interface desenvolvida com **Swing**, composta por painéis:
- **SprintPanel**: Criar, editar, ativar/desativar sprints.
- **TaskBoardPanel**: Adicionar, editar, excluir e alterar status de tarefas.
- **ReportsPanel**: Visualizar progresso, story points e tempo restante.

### 2. Camada de Serviço
Implementa a lógica de negócios:
- **SprintService**: Lida com a ativação e recuperação de sprints.
- **TaskService**: Opera tarefas (CRUD, status, story points, etc).

### 3. Camada de Repositório
Utiliza **Spring Data JPA**:
- **SprintRepository**: Gerencia persistência de sprints.
- **TaskRepository**: Gerencia persistência de tarefas.

### 4. Banco de Dados
Utiliza banco relacional MySQL
- **Tabela `sprint`**: Guarda dados de sprints.
- **Tabela `task`**: Guarda dados de tarefas com chave estrangeira referenciando `sprint`.

---

## ⚙️ Configuração do Ambiente

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL (ou outro banco relacional)

### Configuração do `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Instruções de Execução

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/API-TodoList-Spring-Boot.git
cd API-TodoList-Spring-Boot

# Compile o projeto
mvn clean install

# Execute a interface gráfica
# (Executar diretamente a classe MainFrame.java)
```

---

## ✅ Como Usar

### 1. Gerenciamento de Sprints
- Acesse a aba **Sprints**.
- Clique em **Add Sprint**.
- Edite/ative uma sprint com **Edit Sprint**.

### 2. Gerenciamento de Tarefas
- Vá para **Task Board**.
- Adicione tarefas clicando em **Add Task**.
- Edite ou exclua tarefas diretamente no painel.

### 3. Relatórios
- Acesse a aba **Reports**.
- Selecione uma sprint:
    - Veja o progresso.
    - Pontos de história concluídos.
    - Tempo restante.

---

## 🧩 Funcionalidades

### 🔹 Lista de Tarefas
- Adicionar, editar, excluir e mudar status.
- Status: `TODO`, `IN_PROGRESS`, `IN_REVIEW`, `DONE`
- Prioridade: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

### 🔹 Gerenciamento de Sprints
- Criar e editar sprints (título, descrição, datas).
- Apenas uma sprint ativa por vez.

### 🔹 Relatórios
- Progresso em tempo real.
- Pontuação e tempo restante.

### 🔹 Persistência
- Todos os dados são armazenados no banco de dados e persistem entre execuções.

---

## 🔐 Segurança

- Validação de entrada contra dados inválidos.
- Possibilidade de implementar autenticação para proteção de dados futuros.

---

## 🧱 Estrutura do Projeto

```plaintext
API-TodoList-Spring-Boot
└── src
    └── main
        └── java
            └── db2
                └── todolistapi
                    ├── model
                    │   ├── Sprint.java
                    │   ├── TaskItem.java
                    │   ├── TaskPriority.java
                    │   └── TaskStatus.java
                    │
                    ├── repository
                    │   ├── SprintRepository.java
                    │   └── TaskRepository.java
                    │
                    ├── service
                    │   ├── SprintService.java
                    │   └── TaskService.java
                    │
                    ├── swing
                    │   ├── components
                    │   │   ├── ReportsCard.java
                    │   │   └── TaskCard.java
                    │   │
                    │   ├── frames
                    │   │   ├── MainFrame.java
                    │   │   ├── ReportsPanel.java
                    │   │   ├── SprintPanel.java
                    │   │   └── TaskBoardPanel.java
                    │   │
                    │   └── utils
                    │       └── SwingUtils.java
```

---

## 🗂️ Diagrama ER (Entidade-Relacionamento)

```plaintext
+---------------+        +----------------+
|    Sprint     |        |      Task      |
+---------------+        +----------------+
| id (PK)       |<------>| id (PK)        |
| title         |        | title          |
| description   |        | description    |
| start_date    |        | status         |
| end_date      |        | priority       |
| active        |        | story_points   |
+---------------+        | created_at     |
                         | sprint_id (FK) |
                         +----------------+
```

---

## 👥 Autores

Desenvolvido como parte do curso de **Banco de Dados II**:

- Fernanda Brito Costa da Silva
- Grazielle Nascimento Ferreira
- Laura Mendes Selva
- Victor Santos De Paula
- William Martins
