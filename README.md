# Sistema de Gerenciamento de Projetos

Um sistema web completo para gerenciamento de projetos, pessoas e membros de equipe, desenvolvido com Spring Boot e interface web moderna.

## 📋 Descrição

Este projeto é um sistema de gerenciamento de portfólio de projetos que permite:

- **Gestão de Projetos**: Criar, editar, visualizar e excluir projetos
- **Gestão de Pessoas**: Cadastrar funcionários e gerentes
- **Gestão de Membros**: Associar pessoas aos projetos com datas de início e fim
- **Dashboard**: Visualização de estatísticas e métricas do portfólio
- **Interface Web**: Interface moderna e responsiva

## 🏗️ Arquitetura

- **Backend**: Spring Boot 2.7.18
- **Frontend**: JSP + Bootstrap + JavaScript
- **Banco de Dados**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build**: Maven
- **Java**: 17
- **Docker**: PostgreSQL em container

## 📦 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6** ou superior
- **Docker** e **Docker Compose** (opcional, para banco de dados)
- **Git** (para clonar o repositório)

### Verificando as instalações:

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Docker
docker --version
docker-compose --version
```

## 🚀 Instalação e Configuração

### Opção 1: Com Docker (Recomendado)

#### 1. Clone o repositório

```bash
git clone https://github.com/ronneycst/portifolio.git
cd portfolio
```

#### 2. Inicie o banco de dados com Docker

```bash
# Iniciar o PostgreSQL
docker-compose up -d postgres

# Verificar se está rodando
docker-compose ps
```

#### 3. Execute o projeto

#### 4. Acesse a aplicação

Abra seu navegador e acesse:
```
http://localhost:8080
```

### Opção 2: PostgreSQL Local

#### 1. Clone o repositório

```bash
git clone https://github.com/ronneycst/portifolio.git
cd portfolio
```

#### 2. Configure o banco de dados

1. **Crie o banco de dados PostgreSQL:**
```sql
CREATE DATABASE portfoliomanager;
```

2. **Configure as credenciais** no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/portfoliomanager
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

#### 3. Execute o projeto

#### 4. Acesse a aplicação

Abra seu navegador e acesse:
```
http://localhost:8080
```
## 🔧 Configurações

### Configurações do Banco de Dados

O arquivo `application.properties` contém as principais configurações:

```properties
# Configurações do banco
spring.datasource.url=jdbc:postgresql://localhost:5432/portfoliomanager
spring.datasource.username=postgres
spring.datasource.password=password

# Configurações JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Configurações de view
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

# Configurações de log
logging.level.com.ronney.portfolio=DEBUG
```




## 📊 Funcionalidades

### Gestão de Projetos
- ✅ Criar projetos
- ✅ Editar projetos existentes
- ✅ Visualizar detalhes do projeto
- ✅ Excluir projetos (com validações de negócio)
- ✅ Listar todos os projetos

### Gestão de Pessoas
- ✅ Cadastrar pessoas (funcionários e gerentes)
- ✅ Editar informações de pessoas
- ✅ Visualizar detalhes de pessoas
- ✅ Excluir pessoas
- ✅ Listar todas as pessoas

### Gestão de Membros
- ✅ Associar pessoas aos projetos
- ✅ Definir datas de início e fim
- ✅ Editar associações
- ✅ Remover membros de projetos
- ✅ Listar membros por projeto

### Dashboard
- ✅ Estatísticas gerais
- ✅ Métricas de projetos
- ✅ Visualização de dados
