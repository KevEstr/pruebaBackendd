package com.udea.prueba_tecnica.component.movie.io.repository.projection;

/**
 * Projection interface para consultas nativas de Genre con estadísticas
 * Requisito técnico: Interface projection para queries nativas
 */
public interface GenreProjection {
    
    Long getId();
    String getName();
    String getDescription();
    Integer getTmdbId();
    Long getMovieCount();
    Double getAverageRating();
} 