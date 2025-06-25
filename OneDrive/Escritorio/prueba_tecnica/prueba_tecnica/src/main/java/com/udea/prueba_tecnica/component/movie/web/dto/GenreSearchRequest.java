package com.udea.prueba_tecnica.component.movie.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para búsqueda de géneros - REQUISITO TÉCNICO convertido a clase regular para Java 8
 */
@Data
@Builder
@NoArgsConstructor
public class GenreSearchRequest {
    
    private String name;
    private Boolean active;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    
    // Constructor con validaciones y valores por defecto
    public GenreSearchRequest(String name, Boolean active, Integer page, Integer size, String sortBy, String sortDirection) {
        this.name = name;
        this.active = active != null ? active : true;
        this.page = (page != null && page >= 0) ? page : 0;
        this.size = (size != null && size > 0 && size <= 100) ? size : 20;
        this.sortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : "name";
        this.sortDirection = (sortDirection != null && 
                           (sortDirection.equalsIgnoreCase("asc") || sortDirection.equalsIgnoreCase("desc"))) 
                           ? sortDirection : "asc";
    }
} 