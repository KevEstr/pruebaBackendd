package com.udea.prueba_tecnica.component.movie.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Movie search request with filters and pagination")
public class MovieSearchRequest {

    @Schema(description = "Search query for title", example = "fight club")
    private String query;

    @Schema(description = "Exact title match", example = "Fight Club")
    private String title;

    @Schema(description = "Original language", example = "en")
    private String originalLanguage;

    @Schema(description = "Release year", example = "1999")
    private Integer releaseYear;

    @Schema(description = "Release date from", example = "1999-01-01")
    private LocalDate releaseDateFrom;

    @Schema(description = "Release date to", example = "1999-12-31")
    private LocalDate releaseDateTo;

    @Schema(description = "Minimum vote average", example = "7.0")
    @Min(value = 0, message = "Vote average must be at least 0")
    @Max(value = 10, message = "Vote average must be at most 10")
    private Double voteAverageMin;

    @Schema(description = "Maximum vote average", example = "10.0")
    @Min(value = 0, message = "Vote average must be at least 0")
    @Max(value = 10, message = "Vote average must be at most 10")
    private Double voteAverageMax;

    @Schema(description = "Minimum vote count", example = "1000")
    @Min(value = 0, message = "Vote count must be non-negative")
    private Integer voteCountMin;

    @Schema(description = "Minimum popularity", example = "10.0")
    @Min(value = 0, message = "Popularity must be non-negative")
    private Double popularityMin;

    @Schema(description = "Maximum popularity", example = "100.0")
    @Min(value = 0, message = "Popularity must be non-negative")
    private Double popularityMax;

    @Schema(description = "Adult content filter", example = "false")
    private Boolean adult;

    @Schema(description = "Runtime minimum in minutes", example = "90")
    @Min(value = 0, message = "Runtime must be non-negative")
    private Integer runtimeMin;

    @Schema(description = "Runtime maximum in minutes", example = "180")
    @Min(value = 0, message = "Runtime must be non-negative")
    private Integer runtimeMax;

    @Schema(description = "Movie status", example = "Released")
    private String status;

    // Pagination parameters
    @Schema(description = "Page number (0-based)", example = "0", defaultValue = "0")
    @Min(value = 0, message = "Page number must be non-negative")
    private Integer page = 0;

    @Schema(description = "Page size", example = "20", defaultValue = "20")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must be at most 100")
    private Integer size = 20;

    @Schema(description = "Sort field", example = "popularity", 
            allowableValues = {"title", "releaseDate", "popularity", "voteAverage", "voteCount", "createdAt"})
    private String sortBy = "popularity";

    @Schema(description = "Sort direction", example = "DESC", allowableValues = {"ASC", "DESC"})
    private String sortDirection = "DESC";
}
