package com.udea.prueba_tecnica.component.movie.io.gateway;

import com.udea.prueba_tecnica.component.movie.io.gateway.dto.TmdbMovieResponse;
import com.udea.prueba_tecnica.component.movie.io.gateway.dto.TmdbSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmdbGateway {

    private final WebClient tmdbWebClient;

    /**
     * Get movie details by TMDB ID
     */
    public Optional<TmdbMovieResponse> getMovieById(Long movieId) {
        try {
            TmdbMovieResponse response = tmdbWebClient
                    .get()
                    .uri("/movie/{movie_id}", movieId)
                    .retrieve()
                    .bodyToMono(TmdbMovieResponse.class)
                    .block();
            
            log.debug("Successfully fetched movie with ID: {}", movieId);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching movie with ID {}: {} - {}", movieId, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching movie with ID {}: {}", movieId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Search movies by query
     */
    public Optional<TmdbSearchResponse> searchMovies(String query, Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("query", query)
                            .queryParam("page", page != null ? page : 1)
                            .queryParam("include_adult", false)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully searched movies with query: {} (page: {})", query, page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error searching movies with query '{}': {} - {}", query, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error searching movies with query '{}': {}", query, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get popular movies
     */
    public Optional<TmdbSearchResponse> getPopularMovies(Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully fetched popular movies (page: {})", page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching popular movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching popular movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get top rated movies
     */
    public Optional<TmdbSearchResponse> getTopRatedMovies(Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/top_rated")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully fetched top rated movies (page: {})", page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching top rated movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching top rated movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get now playing movies
     */
    public Optional<TmdbSearchResponse> getNowPlayingMovies(Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/now_playing")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully fetched now playing movies (page: {})", page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching now playing movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching now playing movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get upcoming movies
     */
    public Optional<TmdbSearchResponse> getUpcomingMovies(Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/upcoming")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully fetched upcoming movies (page: {})", page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching upcoming movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching upcoming movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Discover movies with filters
     */
    public Optional<TmdbSearchResponse> discoverMovies(String sortBy, Integer year, String withGenres, Integer page) {
        try {
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> {
                        if (sortBy != null) {
                            uriBuilder.queryParam("sort_by", sortBy);
                        }
                        if (year != null) {
                            uriBuilder.queryParam("year", year);
                        }
                        if (withGenres != null) {
                            uriBuilder.queryParam("with_genres", withGenres);
                        }
                        
                        return uriBuilder
                                .path("/discover/movie")
                                .queryParam("page", page != null ? page : 1)
                                .queryParam("include_adult", false)
                                .build();
                    })
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully discovered movies with filters (page: {})", page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error discovering movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error discovering movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get trending movies
     */
    public Optional<TmdbSearchResponse> getTrendingMovies(String timeWindow, Integer page) {
        try {
            String validTimeWindow = (timeWindow != null && timeWindow.equals("week")) ? "week" : "day";
            
            TmdbSearchResponse response = tmdbWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trending/movie/{time_window}")
                            .queryParam("page", page != null ? page : 1)
                            .build(validTimeWindow))
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
            
            log.debug("Successfully fetched trending movies for {} (page: {})", validTimeWindow, page);
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            log.error("Error fetching trending movies (page {}): {} - {}", page, e.getStatusCode(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching trending movies (page {}): {}", page, e.getMessage());
            return Optional.empty();
        }
    }
}
