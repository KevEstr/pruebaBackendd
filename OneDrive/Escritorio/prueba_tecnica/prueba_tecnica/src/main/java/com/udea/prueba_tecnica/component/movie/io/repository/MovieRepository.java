package com.udea.prueba_tecnica.component.movie.io.repository;

import com.udea.prueba_tecnica.component.movie.io.repository.projection.MovieListDTO;
import com.udea.prueba_tecnica.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTmdbId(Long tmdbId);
    
    boolean existsByTmdbId(Long tmdbId);

    @Query(value = "SELECT m.id, m.tmdb_id as tmdbId, m.title, m.original_title as originalTitle, m.overview, " +
                   "m.release_date as releaseDate, m.poster_path as posterPath, m.backdrop_path as backdropPath, " +
                   "m.popularity, m.vote_average as voteAverage, m.vote_count as voteCount, " +
                   "m.adult, m.original_language as originalLanguage " +
                   "FROM movies m " +
                   "WHERE (:query IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
                   "AND (:title IS NULL OR LOWER(m.title) = LOWER(:title)) " +
                   "AND (:originalLanguage IS NULL OR m.original_language = :originalLanguage) " +
                   "AND (:releaseYear IS NULL OR EXTRACT(YEAR FROM m.release_date) = :releaseYear) " +
                   "AND (:releaseDateFrom IS NULL OR m.release_date >= :releaseDateFrom) " +
                   "AND (:releaseDateTo IS NULL OR m.release_date <= :releaseDateTo) " +
                   "AND (:voteAverageMin IS NULL OR m.vote_average >= :voteAverageMin) " +
                   "AND (:voteAverageMax IS NULL OR m.vote_average <= :voteAverageMax) " +
                   "AND (:voteCountMin IS NULL OR m.vote_count >= :voteCountMin) " +
                   "AND (:popularityMin IS NULL OR m.popularity >= :popularityMin) " +
                   "AND (:popularityMax IS NULL OR m.popularity <= :popularityMax) " +
                   "AND (:adult IS NULL OR m.adult = :adult) " +
                   "AND (:runtimeMin IS NULL OR m.runtime >= :runtimeMin) " +
                   "AND (:runtimeMax IS NULL OR m.runtime <= :runtimeMax) " +
                   "AND (:status IS NULL OR LOWER(m.status) = LOWER(:status)) " +
                   "ORDER BY m.popularity DESC",
            countQuery = "SELECT COUNT(*) " +
                        "FROM movies m " +
                        "WHERE (:query IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
                        "AND (:title IS NULL OR LOWER(m.title) = LOWER(:title)) " +
                        "AND (:originalLanguage IS NULL OR m.original_language = :originalLanguage) " +
                        "AND (:releaseYear IS NULL OR EXTRACT(YEAR FROM m.release_date) = :releaseYear) " +
                        "AND (:releaseDateFrom IS NULL OR m.release_date >= :releaseDateFrom) " +
                        "AND (:releaseDateTo IS NULL OR m.release_date <= :releaseDateTo) " +
                        "AND (:voteAverageMin IS NULL OR m.vote_average >= :voteAverageMin) " +
                        "AND (:voteAverageMax IS NULL OR m.vote_average <= :voteAverageMax) " +
                        "AND (:voteCountMin IS NULL OR m.vote_count >= :voteCountMin) " +
                        "AND (:popularityMin IS NULL OR m.popularity >= :popularityMin) " +
                        "AND (:popularityMax IS NULL OR m.popularity <= :popularityMax) " +
                        "AND (:adult IS NULL OR m.adult = :adult) " +
                        "AND (:runtimeMin IS NULL OR m.runtime >= :runtimeMin) " +
                        "AND (:runtimeMax IS NULL OR m.runtime <= :runtimeMax) " +
                        "AND (:status IS NULL OR LOWER(m.status) = LOWER(:status))",
            nativeQuery = true)
    Page<MovieListDTO> findMoviesWithFilters(
            @Param("query") String query,
            @Param("title") String title,
            @Param("originalLanguage") String originalLanguage,
            @Param("releaseYear") Integer releaseYear,
            @Param("releaseDateFrom") LocalDate releaseDateFrom,
            @Param("releaseDateTo") LocalDate releaseDateTo,
            @Param("voteAverageMin") Double voteAverageMin,
            @Param("voteAverageMax") Double voteAverageMax,
            @Param("voteCountMin") Integer voteCountMin,
            @Param("popularityMin") Double popularityMin,
            @Param("popularityMax") Double popularityMax,
            @Param("adult") Boolean adult,
            @Param("runtimeMin") Integer runtimeMin,
            @Param("runtimeMax") Integer runtimeMax,
            @Param("status") String status,
            Pageable pageable);

    @Query(value = "SELECT m.id, m.tmdb_id as tmdbId, m.title, m.original_title as originalTitle, m.overview, " +
                   "m.release_date as releaseDate, m.poster_path as posterPath, m.backdrop_path as backdropPath, " +
                   "m.popularity, m.vote_average as voteAverage, m.vote_count as voteCount, " +
                   "m.adult, m.original_language as originalLanguage " +
                   "FROM movies m " +
                   "WHERE m.popularity >= :minPopularity " +
                   "ORDER BY m.popularity DESC", 
            nativeQuery = true)
    Page<MovieListDTO> findPopularMovies(@Param("minPopularity") Double minPopularity, Pageable pageable);

    @Query(value = "SELECT m.id, m.tmdb_id as tmdbId, m.title, m.original_title as originalTitle, m.overview, " +
                   "m.release_date as releaseDate, m.poster_path as posterPath, m.backdrop_path as backdropPath, " +
                   "m.popularity, m.vote_average as voteAverage, m.vote_count as voteCount, " +
                   "m.adult, m.original_language as originalLanguage " +
                   "FROM movies m " +
                   "WHERE m.vote_average >= :minRating AND m.vote_count >= :minVoteCount " +
                   "ORDER BY m.vote_average DESC, m.vote_count DESC", 
            nativeQuery = true)
    Page<MovieListDTO> findTopRatedMovies(
            @Param("minRating") Double minRating, 
            @Param("minVoteCount") Integer minVoteCount, 
            Pageable pageable);

    @Query(value = "SELECT m.id, m.tmdb_id as tmdbId, m.title, m.original_title as originalTitle, m.overview, " +
                   "m.release_date as releaseDate, m.poster_path as posterPath, m.backdrop_path as backdropPath, " +
                   "m.popularity, m.vote_average as voteAverage, m.vote_count as voteCount, " +
                   "m.adult, m.original_language as originalLanguage " +
                   "FROM movies m " +
                   "WHERE m.release_date >= :fromDate " +
                   "ORDER BY m.release_date DESC, m.popularity DESC", 
            nativeQuery = true)
    Page<MovieListDTO> findRecentMovies(@Param("fromDate") LocalDate fromDate, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE movies " +
                   "SET popularity = :newPopularity, updated_at = CURRENT_TIMESTAMP " +
                   "WHERE tmdb_id = :tmdbId", 
            nativeQuery = true)
    int updateMoviePopularity(@Param("tmdbId") Long tmdbId, @Param("newPopularity") Double newPopularity);
} 