package com.udea.prueba_tecnica.component.movie.service;

import com.udea.prueba_tecnica.component.movie.web.dto.MovieResponse;

import java.util.List;

/**
 * Interface for movie command operations
 * This interface defines all write operations for movies (Create, Update, Delete)
 */
public interface MovieCommand {

    /**
     * Fetch and save movie from TMDB API by ID
     * @param tmdbId TMDB movie ID
     * @return Saved movie details
     */
    MovieResponse fetchAndSaveMovieFromTmdb(Long tmdbId);

    /**
     * Fetch and save popular movies from TMDB API
     * @param pages Number of pages to fetch (default: 5)
     * @return List of saved movies
     */
    List<MovieResponse> fetchAndSavePopularMoviesFromTmdb(Integer pages);

    /**
     * Fetch and save top rated movies from TMDB API
     * @param pages Number of pages to fetch (default: 5)
     * @return List of saved movies
     */
    List<MovieResponse> fetchAndSaveTopRatedMoviesFromTmdb(Integer pages);

    /**
     * Fetch and save now playing movies from TMDB API
     * @param pages Number of pages to fetch (default: 3)
     * @return List of saved movies
     */
    List<MovieResponse> fetchAndSaveNowPlayingMoviesFromTmdb(Integer pages);

    /**
     * Fetch and save upcoming movies from TMDB API
     * @param pages Number of pages to fetch (default: 3)
     * @return List of saved movies
     */
    List<MovieResponse> fetchAndSaveUpcomingMoviesFromTmdb(Integer pages);

    /**
     * Search and save movies from TMDB API
     * @param query Search query
     * @param pages Number of pages to fetch (default: 3)
     * @return List of saved movies
     */
    List<MovieResponse> searchAndSaveMoviesFromTmdb(String query, Integer pages);

    /**
     * Update movie popularity from TMDB API
     * @param tmdbId TMDB movie ID
     * @return Updated movie details or null if not found
     */
    MovieResponse updateMoviePopularityFromTmdb(Long tmdbId);

    /**
     * Delete movie by internal ID
     * @param id Internal movie ID
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteMovie(Long id);

    /**
     * Delete movie by TMDB ID
     * @param tmdbId TMDB movie ID
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteMovieByTmdbId(Long tmdbId);

    /**
     * Bulk import movies from TMDB trending
     * @param timeWindow "day" or "week"
     * @param pages Number of pages to fetch (default: 5)
     * @return List of saved movies
     */
    List<MovieResponse> bulkImportTrendingMoviesFromTmdb(String timeWindow, Integer pages);
}
