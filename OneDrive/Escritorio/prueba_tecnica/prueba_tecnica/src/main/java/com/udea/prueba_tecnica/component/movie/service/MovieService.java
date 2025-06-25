package com.udea.prueba_tecnica.component.movie.service;

import com.udea.prueba_tecnica.component.movie.io.gateway.TmdbGateway;
import com.udea.prueba_tecnica.component.movie.io.gateway.dto.TmdbMovieResponse;
import com.udea.prueba_tecnica.component.movie.io.gateway.dto.TmdbSearchResponse;
import com.udea.prueba_tecnica.component.movie.io.mapper.MovieMapper;
import com.udea.prueba_tecnica.component.movie.io.repository.MovieRepository;
import com.udea.prueba_tecnica.component.movie.io.repository.projection.MovieListDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieListItemDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieSearchRequest;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;
import com.udea.prueba_tecnica.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService implements MovieQuery, MovieCommand {

    private final MovieRepository movieRepository;
    private final TmdbGateway tmdbGateway;
    private final MovieMapper movieMapper;

    // ============ QUERY OPERATIONS ============

    @Override
    public MovieResponse getMovieById(Long id) {
        log.debug("Getting movie by ID: {}", id);
        return movieRepository.findById(id)
                .map(movieMapper::toMovieResponse)
                .orElse(null);
    }

    @Override
    public MovieResponse getMovieByTmdbId(Long tmdbId) {
        log.debug("Getting movie by TMDB ID: {}", tmdbId);
        return movieRepository.findByTmdbId(tmdbId)
                .map(movieMapper::toMovieResponse)
                .orElse(null);
    }

    @Override
    public PagedResponse<MovieListItemDTO> searchMovies(MovieSearchRequest searchRequest) {
        log.debug("Searching movies with request: {}", searchRequest);
        
        Pageable pageable = createPageable(searchRequest.getPage(), searchRequest.getSize(), 
                searchRequest.getSortBy(), searchRequest.getSortDirection());
        
        Page<MovieListDTO> moviesPage = movieRepository.findMoviesWithFilters(
                searchRequest.getQuery(),
                searchRequest.getTitle(),
                searchRequest.getOriginalLanguage(),
                searchRequest.getReleaseYear(),
                searchRequest.getReleaseDateFrom(),
                searchRequest.getReleaseDateTo(),
                searchRequest.getVoteAverageMin(),
                searchRequest.getVoteAverageMax(),
                searchRequest.getVoteCountMin(),
                searchRequest.getPopularityMin(),
                searchRequest.getPopularityMax(),
                searchRequest.getAdult(),
                searchRequest.getRuntimeMin(),
                searchRequest.getRuntimeMax(),
                searchRequest.getStatus(),
                pageable
        );

        return movieMapper.toPagedResponse(moviesPage);
    }

    @Override
    public PagedResponse<MovieListItemDTO> getPopularMovies(int page, int size, Double minPopularity) {
        log.debug("Getting popular movies - page: {}, size: {}, minPopularity: {}", page, size, minPopularity);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieListDTO> moviesPage = movieRepository.findPopularMovies(
                minPopularity != null ? minPopularity : 10.0, pageable);
        
        return movieMapper.toPagedResponse(moviesPage);
    }

    @Override
    public PagedResponse<MovieListItemDTO> getTopRatedMovies(int page, int size, Double minRating, Integer minVoteCount) {
        log.debug("Getting top rated movies - page: {}, size: {}, minRating: {}, minVoteCount: {}", 
                page, size, minRating, minVoteCount);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieListDTO> moviesPage = movieRepository.findTopRatedMovies(
                minRating != null ? minRating : 7.0,
                minVoteCount != null ? minVoteCount : 1000,
                pageable);
        
        return movieMapper.toPagedResponse(moviesPage);
    }

    @Override
    public PagedResponse<MovieListItemDTO> getRecentMovies(int page, int size, int daysBack) {
        log.debug("Getting recent movies - page: {}, size: {}, daysBack: {}", page, size, daysBack);
        
        LocalDate fromDate = LocalDate.now().minusDays(daysBack);
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieListDTO> moviesPage = movieRepository.findRecentMovies(fromDate, pageable);
        
        return movieMapper.toPagedResponse(moviesPage);
    }

    @Override
    public PagedResponse<MovieListItemDTO> getAllMovies(int page, int size) {
        log.debug("Getting all movies - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "popularity"));
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        
        return movieMapper.toPagedResponseFromEntities(moviesPage);
    }

    // ============ COMMAND OPERATIONS ============

    @Override
    @Transactional
    public MovieResponse fetchAndSaveMovieFromTmdb(Long tmdbId) {
        log.info("Fetching and saving movie from TMDB with ID: {}", tmdbId);
        
        // Check if movie already exists
        Optional<Movie> existingMovie = movieRepository.findByTmdbId(tmdbId);
        if (existingMovie.isPresent()) {
            log.debug("Movie with TMDB ID {} already exists, returning existing movie", tmdbId);
            return movieMapper.toMovieResponse(existingMovie.get());
        }

        // Fetch from TMDB
        Optional<TmdbMovieResponse> tmdbResponse = tmdbGateway.getMovieById(tmdbId);
        if (!tmdbResponse.isPresent()) {
            log.warn("Movie with TMDB ID {} not found in TMDB API", tmdbId);
            return null;
        }

        // Convert and save
        Movie movie = movieMapper.toMovie(tmdbResponse.get());
        Movie savedMovie = movieRepository.save(movie);
        
        log.info("Successfully saved movie: {} (TMDB ID: {})", savedMovie.getTitle(), tmdbId);
        return movieMapper.toMovieResponse(savedMovie);
    }

    @Override
    @Transactional
    public List<MovieResponse> fetchAndSavePopularMoviesFromTmdb(Integer pages) {
        log.info("Fetching and saving popular movies from TMDB - pages: {}", pages);
        return fetchAndSaveMoviesFromTmdbEndpoint("popular", pages != null ? pages : 5);
    }

    @Override
    @Transactional
    public List<MovieResponse> fetchAndSaveTopRatedMoviesFromTmdb(Integer pages) {
        log.info("Fetching and saving top rated movies from TMDB - pages: {}", pages);
        return fetchAndSaveMoviesFromTmdbEndpoint("top_rated", pages != null ? pages : 5);
    }

    @Override
    @Transactional
    public List<MovieResponse> fetchAndSaveNowPlayingMoviesFromTmdb(Integer pages) {
        log.info("Fetching and saving now playing movies from TMDB - pages: {}", pages);
        return fetchAndSaveMoviesFromTmdbEndpoint("now_playing", pages != null ? pages : 3);
    }

    @Override
    @Transactional
    public List<MovieResponse> fetchAndSaveUpcomingMoviesFromTmdb(Integer pages) {
        log.info("Fetching and saving upcoming movies from TMDB - pages: {}", pages);
        return fetchAndSaveMoviesFromTmdbEndpoint("upcoming", pages != null ? pages : 3);
    }

    @Override
    @Transactional
    public List<MovieResponse> searchAndSaveMoviesFromTmdb(String query, Integer pages) {
        log.info("Searching and saving movies from TMDB - query: {}, pages: {}", query, pages);
        
        List<MovieResponse> savedMovies = new ArrayList<>();
        int pagesToFetch = pages != null ? pages : 3;
        
        for (int page = 1; page <= pagesToFetch; page++) {
            Optional<TmdbSearchResponse> searchResponse = tmdbGateway.searchMovies(query, page);
            
            if (!searchResponse.isPresent() || searchResponse.get().getResults() == null) {
                log.warn("No search results found for query: {} (page: {})", query, page);
                continue;
            }
            
            for (TmdbMovieResponse tmdbMovie : searchResponse.get().getResults()) {
                if (!movieRepository.existsByTmdbId(tmdbMovie.getId())) {
                    Movie movie = movieMapper.toMovie(tmdbMovie);
                    Movie savedMovie = movieRepository.save(movie);
                    savedMovies.add(movieMapper.toMovieResponse(savedMovie));
                    log.debug("Saved movie from search: {} (TMDB ID: {})", savedMovie.getTitle(), tmdbMovie.getId());
                }
            }
        }
        
        log.info("Successfully saved {} movies from search query: {}", savedMovies.size(), query);
        return savedMovies;
    }

    @Override
    @Transactional
    public MovieResponse updateMoviePopularityFromTmdb(Long tmdbId) {
        log.info("Updating movie popularity from TMDB for ID: {}", tmdbId);
        
        Optional<Movie> existingMovie = movieRepository.findByTmdbId(tmdbId);
        if (!existingMovie.isPresent()) {
            log.warn("Movie with TMDB ID {} not found in database", tmdbId);
            return null;
        }

        Optional<TmdbMovieResponse> tmdbResponse = tmdbGateway.getMovieById(tmdbId);
        if (!tmdbResponse.isPresent()) {
            log.warn("Movie with TMDB ID {} not found in TMDB API", tmdbId);
            return null;
        }

        // Update popularity using native query
        int updatedRows = movieRepository.updateMoviePopularity(tmdbId, tmdbResponse.get().getPopularity());
        
        if (updatedRows > 0) {
            log.info("Successfully updated popularity for movie TMDB ID: {}", tmdbId);
            return movieRepository.findByTmdbId(tmdbId)
                    .map(movieMapper::toMovieResponse)
                    .orElse(null);
        }
        
        return null;
    }

    @Override
    @Transactional
    public boolean deleteMovie(Long id) {
        log.info("Deleting movie with ID: {}", id);
        
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            log.info("Successfully deleted movie with ID: {}", id);
            return true;
        }
        
        log.warn("Movie with ID {} not found for deletion", id);
        return false;
    }

    @Override
    @Transactional
    public boolean deleteMovieByTmdbId(Long tmdbId) {
        log.info("Deleting movie with TMDB ID: {}", tmdbId);
        
        Optional<Movie> movie = movieRepository.findByTmdbId(tmdbId);
        if (movie.isPresent()) {
            movieRepository.delete(movie.get());
            log.info("Successfully deleted movie with TMDB ID: {}", tmdbId);
            return true;
        }
        
        log.warn("Movie with TMDB ID {} not found for deletion", tmdbId);
        return false;
    }

    @Override
    @Transactional
    public List<MovieResponse> bulkImportTrendingMoviesFromTmdb(String timeWindow, Integer pages) {
        log.info("Bulk importing trending movies from TMDB - timeWindow: {}, pages: {}", timeWindow, pages);
        
        List<MovieResponse> savedMovies = new ArrayList<>();
        int pagesToFetch = pages != null ? pages : 5;
        
        for (int page = 1; page <= pagesToFetch; page++) {
            Optional<TmdbSearchResponse> trendingResponse = tmdbGateway.getTrendingMovies(timeWindow, page);
            
            if (!trendingResponse.isPresent() || trendingResponse.get().getResults() == null) {
                log.warn("No trending results found for timeWindow: {} (page: {})", timeWindow, page);
                continue;
            }
            
            for (TmdbMovieResponse tmdbMovie : trendingResponse.get().getResults()) {
                if (!movieRepository.existsByTmdbId(tmdbMovie.getId())) {
                    Movie movie = movieMapper.toMovie(tmdbMovie);
                    Movie savedMovie = movieRepository.save(movie);
                    savedMovies.add(movieMapper.toMovieResponse(savedMovie));
                    log.debug("Saved trending movie: {} (TMDB ID: {})", savedMovie.getTitle(), tmdbMovie.getId());
                }
            }
        }
        
        log.info("Successfully imported {} trending movies", savedMovies.size());
        return savedMovies;
    }

    // ============ HELPER METHODS ============

    private List<MovieResponse> fetchAndSaveMoviesFromTmdbEndpoint(String endpoint, int pages) {
        List<MovieResponse> savedMovies = new ArrayList<>();
        
        for (int page = 1; page <= pages; page++) {
            Optional<TmdbSearchResponse> response;
            switch (endpoint) {
                case "popular":
                    response = tmdbGateway.getPopularMovies(page);
                    break;
                case "top_rated":
                    response = tmdbGateway.getTopRatedMovies(page);
                    break;
                case "now_playing":
                    response = tmdbGateway.getNowPlayingMovies(page);
                    break;
                case "upcoming":
                    response = tmdbGateway.getUpcomingMovies(page);
                    break;
                default:
                    response = Optional.empty();
                    break;
            }
            
            if (!response.isPresent() || response.get().getResults() == null) {
                log.warn("No results found for {} endpoint (page: {})", endpoint, page);
                continue;
            }
            
            for (TmdbMovieResponse tmdbMovie : response.get().getResults()) {
                if (!movieRepository.existsByTmdbId(tmdbMovie.getId())) {
                    Movie movie = movieMapper.toMovie(tmdbMovie);
                    Movie savedMovie = movieRepository.save(movie);
                    savedMovies.add(movieMapper.toMovieResponse(savedMovie));
                    log.debug("Saved {} movie: {} (TMDB ID: {})", endpoint, savedMovie.getTitle(), tmdbMovie.getId());
                }
            }
        }
        
        log.info("Successfully saved {} movies from {} endpoint", savedMovies.size(), endpoint);
        return savedMovies;
    }

    private Pageable createPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "popularity"));
        }
        
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
    }
}
