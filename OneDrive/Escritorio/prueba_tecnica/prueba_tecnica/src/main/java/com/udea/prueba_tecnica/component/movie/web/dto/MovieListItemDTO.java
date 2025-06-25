package com.udea.prueba_tecnica.component.movie.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Movie list item data transfer object")
public class MovieListItemDTO {

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

    @Schema(description = "Full poster URL", example = "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg")
    private String fullPosterUrl;

    @Schema(description = "Full backdrop URL", example = "https://image.tmdb.org/t/p/w500/hZkgoQYus5vegHoetLkCJzb17zJ.jpg")
    private String fullBackdropUrl;
}
