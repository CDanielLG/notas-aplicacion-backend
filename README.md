# README - Backend de Notas (Arquitectura Actualizada)

## 📋 Resumen de Cambios

El backend ha sido completamente refactorizado según la arquitectura del frontend. Se han agregado:

✅ **Autenticación JWT** - Sistema seguro de tokens
✅ **Endpoints `/api/auth`** - Login y registro
✅ **Endpoints `/api/notes`** - Gestión de notas con filtros
✅ **Seguridad CORS** - Configurado para desarrollo y producción
✅ **Cifrado BCrypt** - Contraseñas seguras
✅ **Filtro de Autenticación** - Validación en cada petición

## 🚀 Inicio Rápido

### 1. Base de Datos
Crear base de datos MySQL:
```sql
CREATE DATABASE db_backend_notes;
```

### 2. Instalar y Ejecutar

**Con Maven:**
```bash
cd backend
mvn clean spring-boot:run
```

**Con JAR compilado:**
```bash
java -jar target/springboot-noteapp-0.0.1-SNAPSHOT.jar
```

El servidor estará disponible en: `http://localhost:8080`

## 🔐 Autenticación

### Registro
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123",
  "passwordConfirm": "password123"
}
```

Respuesta:
```json
{
  "message": "Registration successful! Please log in."
}
```

### Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 📝 Gestión de Notas

### Obtener todas las notas
```bash
GET http://localhost:8080/api/notes
Authorization: Bearer {token}
```

### Filtrar por estado (activas/archivadas)
```bash
# Notas activas (archived = false)
GET http://localhost:8080/api/notes/status/false
Authorization: Bearer {token}

# Notas archivadas (archived = true)
GET http://localhost:8080/api/notes/status/true
Authorization: Bearer {token}
```

### Obtener categorías
```bash
GET http://localhost:8080/api/notes/categories
Authorization: Bearer {token}
```

### Crear nota
```bash
POST http://localhost:8080/api/notes
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Mi primera nota",
  "content": "Contenido de la nota",
  "category": "Personal",
  "archived": false
}
```

### Actualizar nota
```bash
PUT http://localhost:8080/api/notes/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Título actualizado",
  "content": "Contenido actualizado",
  "category": "Trabajo"
}
```

### Cambiar estado archivo
```bash
PATCH http://localhost:8080/api/notes/{id}/archive
Authorization: Bearer {token}
```

### Eliminar nota
```bash
DELETE http://localhost:8080/api/notes/{id}
Authorization: Bearer {token}
```

## 📂 Estructura de Archivos

```
backend/
├── src/main/java/com/ensolver/springboot/app/notes/
│   ├── controllers/
│   │   ├── AuthController.java        ← Endpoints de autenticación
│   │   ├── NotesApiController.java    ← Endpoints de notas (/api/notes)
│   │   ├── NotesController.java       ← Antiguo (/v1/notes)
│   │   └── UserController.java        ← Antiguo (/v1/user)
│   ├── entity/
│   │   ├── User.java
│   │   ├── Note.java
│   │   └── Role.java
│   ├── DTO/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── AuthResponse.java
│   │   ├── NotesDTO.java
│   │   └── UserDTO.java
│   ├── security/
│   │   ├── JwtUtil.java               ← Generación/validación JWT
│   │   ├── JwtAuthFilter.java         ← Filtro de autenticación
│   │   └── SecurityConfig.java        ← Configuración de seguridad
│   ├── service/
│   │   ├── UserService.java
│   │   └── NoteService.java
│   └── repo/
│       ├── IUserRepo.java
│       └── INoteRepo.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── pom.xml
├── target/
│   └── springboot-noteapp-0.0.1-SNAPSHOT.jar
└── README.md
```

## 🔧 Configuración

### application-dev.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_backend_notes
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
app.jwt.secret=uPa1RKLW7Rz5aH1N7W+XQOa1mlqY9FZ7ZyW/fKRckl8=
app.jwt.expiration-ms=86400000
```

## 🧪 Testing con Postman/Curl

1. **Registrarse:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "passwordConfirm": "password123"
  }'
```

2. **Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

3. **Crear nota (usar token del login):**
```bash
curl -X POST http://localhost:8080/api/notes \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi nota",
    "content": "Contenido",
    "category": "Personal"
  }'
```

## 📊 Modelo de Datos

### Usuario
```
user_id (Integer) - Clave primaria
email (String) - Único
password (String) - Cifrada con BCrypt
enabled (Boolean)
role (Role) - Relación N:1
```

### Nota
```
id (Long) - Clave primaria
title (String)
content (String)
category (String)
archived (Boolean)
createdAt (LocalDateTime) - Automático
user (User) - Relación N:1
```

## 🔒 Características de Seguridad

- ✅ JWT Stateless (sin sesiones)
- ✅ BCrypt para cifrado de contraseñas
- ✅ CORS configurado
- ✅ Validación de entrada con Jakarta Validation
- ✅ Autorización por usuario (solo ve sus propias notas)
- ✅ Token con expiración de 24 horas

## 🐛 Troubleshooting

**Error: "Unknown database"**
- Crear la base de datos: `CREATE DATABASE db_backend_notes;`

**Error: "Cannot find port 3306"**
- Verificar que MySQL está corriendo
- Cambiar usuario/contraseña en `application-dev.properties`

**Error 401 "No token"**
- Incluir header: `Authorization: Bearer {token}`

**Error 403 "Forbidden"**
- Solo puedes acceder a tus propias notas
- Verificar que el ID pertenece al usuario autenticado

## 📚 Documentación Completa

Ver `API_DOCUMENTATION.md` para detalles completos de la API.

## 🎯 Próximos Pasos

1. Verificar que la base de datos MySQL está corriendo
2. Ejecutar: `mvn clean spring-boot:run`
3. Probar endpoints con Postman o cURL
4. Integrar con el frontend

¡Listo para usar! 🚀

