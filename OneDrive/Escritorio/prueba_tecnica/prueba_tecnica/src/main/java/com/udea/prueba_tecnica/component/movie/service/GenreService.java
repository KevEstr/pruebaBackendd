package com.udea.prueba_tecnica.component.movie.service;

import com.udea.prueba_tecnica.component.movie.io.mapper.GenreMapper;
import com.udea.prueba_tecnica.component.movie.io.repository.GenreRepository;
import com.udea.prueba_tecnica.component.movie.io.repository.projection.GenreProjection;
import com.udea.prueba_tecnica.component.movie.web.dto.GenreResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.GenreSearchRequest;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;
import com.udea.prueba_tecnica.model.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejo de géneros - NUEVA FUNCIONALIDAD
 * NO afecta la lógica existente de MovieService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    /**
     * Buscar género por ID
     */
    public Optional<GenreResponse> findById(Long id) {
        log.debug("Finding genre by ID: {}", id);
        return genreRepository.findById(id)
                .map(genreMapper::toResponse);
    }

    /**
     * Obtener todos los géneros activos
     */
    public List<GenreResponse> findAllActive() {
        log.debug("Finding all active genres");
        List<Genre> genres = genreRepository.findByActiveTrue();
        return genreMapper.toResponseList(genres);
    }

    /**
     * Búsqueda paginada con filtros
     */
    public PagedResponse<GenreResponse> searchGenres(GenreSearchRequest request) {
        log.debug("Searching genres with request: {}", request);
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        
        // Usar query nativa con projection
        Page<GenreProjection> genrePage = genreRepository.findGenresWithMovieStatsFiltered(
            request.getName(), 
            pageable
        );
        
        List<GenreResponse> content = genreMapper.toResponseListFromProjections(genrePage.getContent());
        
        return PagedResponse.<GenreResponse>builder()
            .content(content)
            .page(genrePage.getNumber())
            .size(genrePage.getSize())
            .totalElements(genrePage.getTotalElements())
            .totalPages(genrePage.getTotalPages())
            .first(genrePage.isFirst())
            .last(genrePage.isLast())
            .numberOfElements(genrePage.getNumberOfElements())
            .empty(genrePage.isEmpty())
            .build();
    }

    /**
     * Obtener géneros con estadísticas de películas
     */
    public List<GenreResponse> getGenresWithMovieStats() {
        log.debug("Getting genres with movie statistics");
        List<GenreProjection> projections = genreRepository.findGenresWithMovieStats();
        return genreMapper.toResponseListFromProjections(projections);
    }

    /**
     * Crear un nuevo género
     */
    @Transactional
    public GenreResponse createGenre(String name, String description, Integer tmdbId) {
        log.debug("Creating genre: {}", name);
        
        Genre genre = Genre.builder()
                .name(name)
                .description(description)
                .tmdbId(tmdbId)
                .active(true)
                .build();
        
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toResponse(savedGenre);
    }

    /**
     * Verificar si existe género por TMDB ID
     */
    public boolean existsByTmdbId(Integer tmdbId) {
        return genreRepository.existsByTmdbId(tmdbId);
    }
} 