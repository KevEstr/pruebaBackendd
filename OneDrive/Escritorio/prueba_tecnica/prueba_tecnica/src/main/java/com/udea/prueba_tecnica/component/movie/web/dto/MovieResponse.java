package com.udea.prueba_tecnica.component.movie.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Complete movie information data transfer object")
public class MovieResponse {

    @Schema(description = "Internal movie ID", example = "1")
    private Long id;

    @Schema(description = "TMDB movie ID", example = "550")
    private Long tmdbId;

    @Schema(description = "Movie title", example = "Fight Club")
    private String title;

    @Schema(description = "Original movie title", example = "Fight Club")
    private String originalTitle;

    @Schema(description = "Movie overview", example = "A ticking-time-bomb insomniac...")
    private String overview;

    @Schema(description = "Release date", example = "1999-10-15")
    private LocalDate releaseDate;

    @Schema(description = "Poster path", example = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg")
    private String posterPath;

    @Schema(description = "Backdrop path", example = "/hZkgoQYus5vegHoetLkCJzb17zJ.jpg")
    private String backdropPath;

    @Schema(description = "Movie popularity", example = "63.416")
    private Double popularity;

    @Schema(description = "Vote average", example = "8.433")
    private Double voteAverage;

    @Schema(description = "Vote count", example = "26280")
    private Integer voteCount;

    @Schema(description = "Adult content flag", example = "false")
    private Boolean adult;

    @Schema(description = "Original language", example = "en")
    private String originalLanguage;

    @Schema(description = "Runtime in minutes", example = "139")
    private Integer runtime;

    @Schema(description = "Movie budget", example = "63000000")
    private Long budget;

    @Schema(description = "Movie revenue", example = "100853753")
    private Long revenue;

    @Schema(description = "Movie status", example = "Released")
    private String status;

    @Schema(description = "Movie tagline", example = "Mischief. Mayhem. Soap.")
    private String tagline;

    @Schema(description = "Movie homepage", example = "http://www.foxmovies.com/movies/fight-club")
    private String homepage;

    @Schema(description = "IMDB ID", example = "tt0137523")
    private String imdbId;

    @Schema(description = "Full poster URL", example = "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg")
    private String fullPosterUrl;

    @Schema(description = "Full backdrop URL", example = "https://image.tmdb.org/t/p/w500/hZkgoQYus5vegHoetLkCJzb17zJ.jpg")
    private String fullBackdropUrl;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
