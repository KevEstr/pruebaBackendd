package com.udea.prueba_tecnica.component.movie.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de Genre - REQUISITO TÉCNICO convertido a clase regular para Java 8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse {
    
    private Long id;
    private Integer tmdbId;
    private String name;
    private String description;
    private Boolean active;
    private Long movieCount;
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 