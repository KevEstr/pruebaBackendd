# Movie Database API - Prueba Técnica

Una API REST completa para gestión de películas con integración a TMDB, desarrollada con Spring Boot 3.5.3 y Java 21.

##  Características Principales

###  Requerimientos Implementados
- ** API con 6+ endpoints RESTful**
- ** Swagger/OpenAPI 3 documentación completa**
- ** Interfaces y clases abstractas** con código auto-generado via MapStruct
- ** Servicios que crean entidades** desde TMDB API
- ** JPA con Hibernate** para persistencia
- ** PostgreSQL** como base de datos
- ** Endpoint POST paginado** con filtros avanzados 
- ** Queries nativas PostgreSQL** con proyecciones
- ** MapStruct** para mapeo de DTOs
- ** Consumo de 6+ endpoints** de TMDB API

##  Configuración

### 1. Base de Datos PostgreSQL
```sql
CREATE DATABASE movies_db;
CREATE USER movies_user WITH PASSWORD 
'
password
'
;
GRANT ALL PRIVILEGES ON DATABASE movies_db TO movies_user;
```

### 2. Configurar TMDB API Key
Editar `src/main/resources/application.properties`:
```properties
tmdb.api.key=TU_TMDB_API_KEY_AQUI
```

### 3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### 4. Acceder a Swagger
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

##  Endpoints Principales

###  Búsqueda Avanzada Paginada 
```http
POST /api/v1/movies/search
Content-Type: application/json

{
  "query": "batman",
  "voteAverageMin": 7.0,
  "page": 0,
  "size": 20,
  "sortBy": "popularity",
  "sortDirection": "DESC"
}
```

### Otros Endpoints
- `GET /api/v1/movies/{id}` - Obtener película por ID
- `GET /api/v1/movies/popular` - Películas populares paginadas
- `GET /api/v1/movies/top-rated` - Películas mejor valoradas
- `GET /api/v1/movies` - Todas las películas paginadas
- `POST /api/v1/movies/fetch-from-tmdb/{tmdbId}` - Importar de TMDB

##  Funcionalidades Destacadas

- **Búsqueda avanzada** con múltiples filtros y paginación
- **Queries nativas PostgreSQL** optimizadas con proyecciones
- **MapStruct** para mapeo automático de DTOs
- **Integración TMDB** completa con 6+ endpoints
- **Swagger** con documentación interactiva
- **Arquitectura limpia** con interfaces Command/Query

¡Proyecto completamente funcional y listo para demostración! 
