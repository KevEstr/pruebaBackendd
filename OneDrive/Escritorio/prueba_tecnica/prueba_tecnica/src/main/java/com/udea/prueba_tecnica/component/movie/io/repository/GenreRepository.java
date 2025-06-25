package com.udea.prueba_tecnica.component.movie.io.repository;

import com.udea.prueba_tecnica.component.movie.io.repository.projection.GenreProjection;
import com.udea.prueba_tecnica.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Métodos derivados de JPA
    Optional<Genre> findByTmdbId(Integer tmdbId);
    
    List<Genre> findByActiveTrue();
    
    Page<Genre> findByActiveTrueOrderByName(Pageable pageable);
    
    boolean existsByTmdbId(Integer tmdbId);

    // Query nativa con projection
    @Query(value = "SELECT " +
            "g.id as id, " +
            "g.name as name, " +
            "g.description as description, " +
            "g.tmdb_id as tmdbId, " +
            "COUNT(m.id) as movieCount, " +
            "AVG(m.vote_average) as averageRating " +
            "FROM genres g " +
            "LEFT JOIN movies m ON g.id = m.genre_id " +
            "WHERE g.active = true " +
            "GROUP BY g.id, g.name, g.description, g.tmdb_id " +
            "ORDER BY COUNT(m.id) DESC", 
        nativeQuery = true)
    List<GenreProjection> findGenresWithMovieStats();

    // Query nativa para buscar géneros con estadísticas paginado
    @Query(value = "SELECT " +
            "g.id as id, " +
            "g.name as name, " +
            "g.description as description, " +
            "g.tmdb_id as tmdbId, " +
            "COUNT(m.id) as movieCount, " +
            "AVG(m.vote_average) as averageRating " +
            "FROM genres g " +
            "LEFT JOIN movies m ON g.id = m.genre_id " +
            "WHERE g.active = true " +
            "AND (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "GROUP BY g.id, g.name, g.description, g.tmdb_id", 
        countQuery = "SELECT COUNT(DISTINCT g.id) " +
            "FROM genres g " +
            "WHERE g.active = true " +
            "AND (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')))",
        nativeQuery = true)
    Page<GenreProjection> findGenresWithMovieStatsFiltered(
        @Param("name") String name, 
        Pageable pageable
    );
} 