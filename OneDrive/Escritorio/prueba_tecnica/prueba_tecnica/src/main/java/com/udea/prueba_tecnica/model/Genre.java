package com.udea.prueba_tecnica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres",
       indexes = {
           @Index(name = "idx_genre_name", columnList = "name"),
           @Index(name = "idx_genre_tmdb_id", columnList = "tmdb_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", unique = true)
    private Integer tmdbId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    // Relación OneToMany con Movie - SIN AFECTAR la entidad Movie existente
    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Movie> movies = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods para mantener sincronía en la relación bidireccional
    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.setGenre(this);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.setGenre(null);
    }
} 