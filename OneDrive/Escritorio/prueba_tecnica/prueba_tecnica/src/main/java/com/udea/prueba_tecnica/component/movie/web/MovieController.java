package com.udea.prueba_tecnica.component.movie.web;

import com.udea.prueba_tecnica.component.movie.service.MovieCommand;
import com.udea.prueba_tecnica.component.movie.service.MovieQuery;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieListItemDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieSearchRequest;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Movies", description = "Movie management API with TMDB integration")
public class MovieController {

    private final MovieQuery movieQuery;
    private final MovieCommand movieCommand;

    @Operation(
        summary = "Search movies with filters and pagination",
        description = "Advanced search endpoint with multiple filters and pagination support. This is the main paginated search endpoint."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movies found successfully", 
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/search")
    public ResponseEntity<PagedResponse<MovieListItemDTO>> searchMovies(
            @Valid @RequestBody MovieSearchRequest searchRequest) {
        
        log.info("Searching movies with filters: {}", searchRequest);
        PagedResponse<MovieListItemDTO> result = movieQuery.searchMovies(searchRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Get movie by ID",
        description = "Retrieve detailed movie information by internal database ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie found", 
                    content = @Content(schema = @Schema(implementation = MovieResponse.class))),
        @ApiResponse(responseCode = "404", description = "Movie not found"),
        @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(
            @Parameter(description = "Internal movie ID", example = "1")
            @PathVariable Long id) {
        
        log.info("Getting movie by ID: {}", id);
        MovieResponse movie = movieQuery.getMovieById(id);
        
        return movie != null 
                ? ResponseEntity.ok(movie)
                : ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Get popular movies",
        description = "Retrieve popular movies from the local database with pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular movies retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/popular")
    public ResponseEntity<PagedResponse<MovieListItemDTO>> getPopularMovies(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            
            @Parameter(description = "Minimum popularity threshold", example = "10.0")
            @RequestParam(required = false) Double minPopularity) {
        
        log.info("Getting popular movies - page: {}, size: {}, minPopularity: {}", page, size, minPopularity);
        PagedResponse<MovieListItemDTO> result = movieQuery.getPopularMovies(page, size, minPopularity);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Get top rated movies",
        description = "Retrieve top rated movies from the local database with pagination"
    )
    @GetMapping("/top-rated")
    public ResponseEntity<PagedResponse<MovieListItemDTO>> getTopRatedMovies(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            
            @Parameter(description = "Minimum rating threshold", example = "7.0")
            @RequestParam(defaultValue = "7.0") @Min(0) @Max(10) Double minRating,
            
            @Parameter(description = "Minimum vote count threshold", example = "1000")
            @RequestParam(defaultValue = "1000") @Min(0) Integer minVoteCount) {
        
        log.info("Getting top rated movies - page: {}, size: {}, minRating: {}, minVoteCount: {}", 
                page, size, minRating, minVoteCount);
        PagedResponse<MovieListItemDTO> result = movieQuery.getTopRatedMovies(page, size, minRating, minVoteCount);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Get all movies with pagination",
        description = "Retrieve all movies from the local database with basic pagination"
    )
    @GetMapping
    public ResponseEntity<PagedResponse<MovieListItemDTO>> getAllMovies(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        
        log.info("Getting all movies - page: {}, size: {}", page, size);
        PagedResponse<MovieListItemDTO> result = movieQuery.getAllMovies(page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Fetch movie from TMDB API",
        description = "Fetch a specific movie from TMDB API and save it to the local database"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movie fetched and saved successfully"),
        @ApiResponse(responseCode = "200", description = "Movie already exists in database"),
        @ApiResponse(responseCode = "404", description = "Movie not found in TMDB API"),
        @ApiResponse(responseCode = "400", description = "Invalid TMDB ID")
    })
    @PostMapping("/fetch-from-tmdb/{tmdbId}")
    public ResponseEntity<MovieResponse> fetchMovieFromTmdb(
            @Parameter(description = "TMDB movie ID", example = "550")
            @PathVariable Long tmdbId) {
        
        log.info("Fetching movie from TMDB with ID: {}", tmdbId);
        MovieResponse movie = movieCommand.fetchAndSaveMovieFromTmdb(tmdbId);
        
        return movie != null 
                ? ResponseEntity.status(HttpStatus.CREATED).body(movie)
                : ResponseEntity.notFound().build();
    }

    // ============ ADDITIONAL COMMAND ENDPOINTS FOR BULK OPERATIONS ============

    @Operation(
        summary = "Bulk import popular movies from TMDB",
        description = "Fetch multiple pages of popular movies from TMDB API and save them to the database"
    )
    @PostMapping("/bulk-import/popular")
    public ResponseEntity<List<MovieResponse>> bulkImportPopularMovies(
            @Parameter(description = "Number of pages to fetch", example = "5")
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer pages) {
        
        log.info("Bulk importing popular movies - pages: {}", pages);
        List<MovieResponse> movies = movieCommand.fetchAndSavePopularMoviesFromTmdb(pages);
        return ResponseEntity.status(HttpStatus.CREATED).body(movies);
    }

    @Operation(
        summary = "Bulk import top rated movies from TMDB",
        description = "Fetch multiple pages of top rated movies from TMDB API and save them to the database"
    )
    @PostMapping("/bulk-import/top-rated")
    public ResponseEntity<List<MovieResponse>> bulkImportTopRatedMovies(
            @Parameter(description = "Number of pages to fetch", example = "5")
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer pages) {
        
        log.info("Bulk importing top rated movies - pages: {}", pages);
        List<MovieResponse> movies = movieCommand.fetchAndSaveTopRatedMoviesFromTmdb(pages);
        return ResponseEntity.status(HttpStatus.CREATED).body(movies);
    }

    @Operation(
        summary = "Search and import movies from TMDB",
        description = "Search movies in TMDB API by query and save results to the database"
    )
    @PostMapping("/search-and-import")
    public ResponseEntity<List<MovieResponse>> searchAndImportMovies(
            @Parameter(description = "Search query", example = "Batman")
            @RequestParam String query,
            
            @Parameter(description = "Number of pages to fetch", example = "3")
            @RequestParam(defaultValue = "3") @Min(1) @Max(10) Integer pages) {
        
        log.info("Searching and importing movies - query: {}, pages: {}", query, pages);
        List<MovieResponse> movies = movieCommand.searchAndSaveMoviesFromTmdb(query, pages);
        return ResponseEntity.status(HttpStatus.CREATED).body(movies);
    }

    @Operation(
        summary = "Bulk import trending movies from TMDB",
        description = "Fetch trending movies from TMDB API and save them to the database"
    )
    @PostMapping("/bulk-import/trending")
    public ResponseEntity<List<MovieResponse>> bulkImportTrendingMovies(
            @Parameter(description = "Time window: day or week", example = "day")
            @RequestParam(defaultValue = "day") String timeWindow,
            
            @Parameter(description = "Number of pages to fetch", example = "5")
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer pages) {
        
        log.info("Bulk importing trending movies - timeWindow: {}, pages: {}", timeWindow, pages);
        List<MovieResponse> movies = movieCommand.bulkImportTrendingMoviesFromTmdb(timeWindow, pages);
        return ResponseEntity.status(HttpStatus.CREATED).body(movies);
    }

    // ============ UTILITY ENDPOINTS ============

    @Operation(
        summary = "Update movie popularity from TMDB",
        description = "Update the popularity score of a specific movie from TMDB API"
    )
    @PutMapping("/update-popularity/{tmdbId}")
    public ResponseEntity<MovieResponse> updateMoviePopularity(
            @Parameter(description = "TMDB movie ID", example = "550")
            @PathVariable Long tmdbId) {
        
        log.info("Updating movie popularity for TMDB ID: {}", tmdbId);
        MovieResponse movie = movieCommand.updateMoviePopularityFromTmdb(tmdbId);
        
        return movie != null 
                ? ResponseEntity.ok(movie)
                : ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Delete movie by ID",
        description = "Delete a movie from the database by internal ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "Internal movie ID", example = "1")
            @PathVariable Long id) {
        
        log.info("Deleting movie with ID: {}", id);
        boolean deleted = movieCommand.deleteMovie(id);
        
        return deleted 
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
