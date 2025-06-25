package com.udea.prueba_tecnica.component.movie.service;

import com.udea.prueba_tecnica.component.movie.web.dto.MovieListItemDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieSearchRequest;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;

/**
 * Interface for movie query operations
 * This interface defines all read operations for movies
 */
public interface MovieQuery {

    /**
     * Get movie by internal ID
     * @param id Internal movie ID
     * @return Movie details or null if not found
     */
    MovieResponse getMovieById(Long id);

    /**
     * Get movie by TMDB ID
     * @param tmdbId TMDB movie ID
     * @return Movie details or null if not found
     */
    MovieResponse getMovieByTmdbId(Long tmdbId);

    /**
     * Search movies with filters and pagination
     * @param searchRequest Search criteria and pagination parameters
     * @return Paginated list of movies
     */
    PagedResponse<MovieListItemDTO> searchMovies(MovieSearchRequest searchRequest);

    /**
     * Get popular movies from local database
     * @param page Page number (0-based)
     * @param size Page size
     * @param minPopularity Minimum popularity threshold
     * @return Paginated list of popular movies
     */
    PagedResponse<MovieListItemDTO> getPopularMovies(int page, int size, Double minPopularity);

    /**
     * Get top rated movies from local database
     * @param page Page number (0-based)
     * @param size Page size
     * @param minRating Minimum rating threshold
     * @param minVoteCount Minimum vote count threshold
     * @return Paginated list of top rated movies
     */
    PagedResponse<MovieListItemDTO> getTopRatedMovies(int page, int size, Double minRating, Integer minVoteCount);

    /**
     * Get recent movies from local database
     * @param page Page number (0-based)
     * @param size Page size
     * @param daysBack Number of days back from today
     * @return Paginated list of recent movies
     */
    PagedResponse<MovieListItemDTO> getRecentMovies(int page, int size, int daysBack);

    /**
     * Get all movies with basic pagination
     * @param page Page number (0-based)
     * @param size Page size
     * @return Paginated list of all movies
     */
    PagedResponse<MovieListItemDTO> getAllMovies(int page, int size);
}
