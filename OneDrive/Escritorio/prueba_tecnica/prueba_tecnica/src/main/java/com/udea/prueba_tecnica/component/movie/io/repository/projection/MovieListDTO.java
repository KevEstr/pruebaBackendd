package com.udea.prueba_tecnica.component.movie.io.repository.projection;

import java.time.LocalDate;

/**
 * Projection for movie list operations
 * This interface is used for optimized database queries that return only necessary fields
 */
public interface MovieListDTO {
    Long getId();
    Long getTmdbId();
    String getTitle();
    String getOriginalTitle();
    String getOverview();
    LocalDate getReleaseDate();
    String getPosterPath();
    String getBackdropPath();
    Double getPopularity();
    Double getVoteAverage();
    Integer getVoteCount();
    Boolean getAdult();
    String getOriginalLanguage();
}
