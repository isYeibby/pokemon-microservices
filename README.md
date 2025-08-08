# Pokemon Microservices

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=flat-square&logo=apache-maven)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue?style=flat-square&logo=postgresql)

Arquitectura de microservicios con Spring Boot y Spring Cloud para un sistema de gestión de Pokémon.

## Arquitectura

```bash
pokemon-microservices/
├── config-server/           # Configuración centralizada
├── eureka-server/           # Service Discovery
├── api-gateway/             # API Gateway
├── pokemon-service/         # Gestión de Pokémon
├── trainer-service/         # Gestión de entrenadores
└── team-service/            # Gestión de equipos
```

## Tecnologías

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Cloud 2022.0.4**
- **PostgreSQL**
- **Maven**

## Prerequisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 13+
- Git

## Instalación

### 1. Clonar repositorio
```bash
git clone <repository-url>
cd pokemon-microservices-parent
```

### 2. Compilar proyecto padre
```bash
mvn clean install
```

### 3. Configurar bases de datos
```sql
CREATE DATABASE pokemon_db;
CREATE DATABASE trainer_db;
CREATE DATABASE team_db;
```

## Ejecución

### Orden de arranque:

1. **Config Server** (Puerto 8888)
```bash
cd config-server
mvn spring-boot:run
```

2. **Eureka Server** (Puerto 8761)
```bash
cd eureka-server
mvn spring-boot:run
```

3. **Microservicios** (Puertos 8081-8083)
```bash
cd pokemon-service && mvn spring-boot:run
cd trainer-service && mvn spring-boot:run
cd team-service && mvn spring-boot:run
```

4. **API Gateway** (Puerto 8080)
```bash
cd api-gateway
mvn spring-boot:run
```

## Endpoints Principales

### Pokemon Service (8081)
- `GET /api/pokemon` - Listar pokémon
- `GET /api/pokemon/{id}` - Obtener por ID
- `GET /api/pokemon/random` - Pokémon aleatorio

### Trainer Service (8082)
- `GET /api/trainers` - Listar entrenadores
- `POST /api/trainers` - Crear entrenador
- `POST /api/trainers/{id}/capture` - Capturar pokémon

### Team Service (8083)
- `GET /api/teams/trainer/{trainerId}` - Equipos del entrenador
- `POST /api/teams` - Crear equipo

### API Gateway (8080)
- Todas las rutas pasan por: `http://localhost:8080/api/**`

## URLs de Servicios

- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888
