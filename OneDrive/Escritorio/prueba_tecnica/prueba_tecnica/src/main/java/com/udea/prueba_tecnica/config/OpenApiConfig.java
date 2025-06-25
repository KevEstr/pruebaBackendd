package com.udea.prueba_tecnica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Database API")
                        .description("Comprehensive Movie Database API with TMDB integration\n\n" +
                                "## Features\n" +
                                "- Advanced search with multiple filters and pagination\n" +
                                "- TMDB API integration for fetching movie data\n" +
                                "- PostgreSQL native queries with projections\n" +
                                "- MapStruct for DTO mapping\n" +
                                "- Full CRUD operations\n\n" +
                                "## Key Endpoints\n" +
                                "- **POST /api/v1/movies/search** - Advanced paginated search (Main requirement)\n" +
                                "- **GET /api/v1/movies** - Get all movies with pagination\n" +
                                "- **GET /api/v1/movies/popular** - Get popular movies\n" +
                                "- **GET /api/v1/movies/top-rated** - Get top rated movies\n" +
                                "- **POST /api/v1/movies/fetch-from-tmdb/{tmdbId}** - Fetch from TMDB API\n" +
                                "- **POST /api/v1/movies/bulk-import/****** - Bulk import operations\n\n" +
                                "## Database Features\n" +
                                "- JPA with Hibernate\n" +
                                "- PostgreSQL native queries\n" +
                                "- Database projections for optimized queries\n" +
                                "- Indexed fields for better performance\n\n" +
                                "## External API Integration\n" +
                                "- The Movie Database (TMDB) API\n" +
                                "- WebClient for reactive HTTP calls\n" +
                                "- Comprehensive error handling")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Prueba Técnica Team")
                                .email("contact@pruebatecnica.com")
                                .url("https://github.com/prueba-tecnica"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.pruebatecnica.com")
                                .description("Production Server")
                ));
    }
} 