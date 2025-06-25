package com.udea.prueba_tecnica.component.movie.web;

import com.udea.prueba_tecnica.component.movie.service.GenreService;
import com.udea.prueba_tecnica.component.movie.web.dto.GenreResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.GenreSearchRequest;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Genre Management", description = "API endpoints for managing movie genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    @Operation(summary = "Get genre by ID")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id) {
        return genreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active genres")
    public ResponseEntity<List<GenreResponse>> getAllActiveGenres() {
        List<GenreResponse> genres = genreService.findAllActive();
        return ResponseEntity.ok(genres);
    }

    @PostMapping("/search")
    @Operation(summary = "Search genres with pagination")
    public ResponseEntity<PagedResponse<GenreResponse>> searchGenres(@Valid @RequestBody GenreSearchRequest request) {
        PagedResponse<GenreResponse> response = genreService.searchGenres(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get genres with movie statistics")
    public ResponseEntity<List<GenreResponse>> getGenresWithStats() {
        List<GenreResponse> genres = genreService.getGenresWithMovieStats();
        return ResponseEntity.ok(genres);
    }

    @PostMapping
    @Operation(summary = "Create new genre")
    public ResponseEntity<GenreResponse> createGenre(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer tmdbId) {
        
        if (tmdbId != null && genreService.existsByTmdbId(tmdbId)) {
            return ResponseEntity.badRequest().build();
        }
        
        GenreResponse response = genreService.createGenre(name, description, tmdbId);
        return ResponseEntity.ok(response);
    }
}
