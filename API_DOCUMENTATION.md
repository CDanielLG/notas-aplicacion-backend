# Backend de Aplicación de Notas - Documentación

## Descripción

Backend desarrollado con Spring Boot 3.2.4 que implementa una API REST para una aplicación de gestión de notas con autenticación JWT y base de datos MySQL.

## Arquitectura Implementada

### 1. **Autenticación y Seguridad**

**Endpoints de Autenticación** (`/api/auth`)
- `POST /api/auth/login` - Login de usuario
  - Request: `{ "email": "user@example.com", "password": "password123" }`
  - Response: `{ "token": "jwt_token_here" }`
  
- `POST /api/auth/register` - Registro de nuevo usuario
  - Request: `{ "email": "user@example.com", "password": "password123", "passwordConfirm": "password123" }`
  - Response: `{ "message": "Registration successful! Please log in." }`

**Configuración de Seguridad**
- JWT (JSON Web Tokens) para autenticación
- BCrypt para cifrado de contraseñas
- CORS habilitado para localhost:8080 y Firebase
- Filtro de autenticación en todos los endpoints de `/api/notes`

### 2. **Gestión de Notas**

**Endpoints de Notas** (`/api/notes`)
- `GET /api/notes` - Obtener todas las notas del usuario actual
- `GET /api/notes/status/{archived}` - Obtener notas por estado (true/false)
- `GET /api/notes/categories` - Obtener lista de categorías únicas
- `POST /api/notes` - Crear una nueva nota
- `GET /api/notes/{id}` - Obtener una nota específica
- `PUT /api/notes/{id}` - Actualizar una nota
- `PATCH /api/notes/{id}/archive` - Cambiar estado de archivo
- `DELETE /api/notes/{id}` - Eliminar una nota

**Modelos**
- Note: id, title, content, category, archived, createdAt, user
- User: user_id, email, password (cifrada), enabled, role
- Role: id_role, nombre

### 3. **Base de Datos**

**Tablas**
- users: almacena usuarios con contraseña cifrada
- notes: almacena notas asociadas a usuarios
- roles: almacena roles de usuario

**Relaciones**
- users N:1 roles (muchos usuarios a un rol)
- notes N:1 users (muchas notas a un usuario)

### 4. **Estructura del Proyecto**

```
src/main/java/com/ensolver/springboot/app/notes/
├── controllers/
│   ├── AuthController.java        (Endpoints de autenticación)
│   ├── NotesApiController.java    (Endpoints de notas - /api/notes)
│   ├── NotesController.java       (Antiguo - /v1/notes)
│   └── UserController.java        (Antiguo - /v1/user)
├── entity/
│   ├── User.java
│   ├── Note.java
│   └── Role.java
├── DTO/
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── AuthResponse.java
│   ├── NotesDTO.java
│   └── UserDTO.java
├── security/
│   ├── JwtUtil.java               (Generación y validación de JWT)
│   ├── JwtAuthFilter.java         (Filtro de autenticación)
│   └── SecurityConfig.java        (Configuración de seguridad)
├── service/
│   ├── UserService.java
│   └── NoteService.java
├── repo/
│   ├── IUserRepo.java
│   └── INoteRepo.java
└── SpringbootApplicationNoteappApplication.java
```

## Configuración

### Dependencias Principales
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- MySQL Connector
- JJWT (JWT)
- Lombok
- Jakarta Validation

### Propiedades de Configuración

**Desarrollo** (`application-dev.properties`)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_backend_notes
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
app.jwt.secret=uPa1RKLW7Rz5aH1N7W+XQOa1mlqY9FZ7ZyW/fKRckl8=
app.jwt.expiration-ms=86400000  # 24 horas
```

## Inicio Rápido

### Requisitos
- Java 21+
- Maven 3.8+
- MySQL 8.0+

### Pasos
1. Crear base de datos MySQL
```sql
CREATE DATABASE db_backend_notes;
```

2. Compilar el proyecto
```bash
mvn clean compile
```

3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Características de Seguridad

1. **Cifrado de Contraseñas**: BCryptPasswordEncoder
2. **JWT Stateless**: Sin sesiones en servidor
3. **CORS**: Configurado para desarrollo y producción
4. **Validación de Entrada**: @NotBlank, @Email, @Size
5. **Autorización por Usuario**: Cada usuario solo ve sus propias notas

## Flujo de Autenticación

1. Usuario se registra con email y contraseña
2. Contraseña se cifra con BCrypt
3. Usuario inicia sesión con email y contraseña
4. Backend valida y genera token JWT
5. Cliente almacena token en localStorage
6. Cliente envía token en header `Authorization: Bearer {token}`
7. Filtro valida token para cada petición
8. Si es válido, se extrae el email del token para obtener el usuario

## Notas de Implementación

- Los endpoints `/v1/notes` y `/v1/user` son antiguos y pueden ser removidos
- El DTO RegisterRequest incluye validación de coincidencia de contraseñas
- Las notas se asocian automáticamente al usuario autenticado
- Los timestamps de creación se generan automáticamente con @PrePersist
- Las categorías se retornan como lista de strings únicos

## Integración con Frontend

El frontend accede a la API desde:
- **Desarrollo**: `http://localhost:8080/api`
- **Producción**: `https://notas-aplicacion-backend.onrender.com/api`

Los endpoints esperados coinciden exactamente con los implementados en este backend.

