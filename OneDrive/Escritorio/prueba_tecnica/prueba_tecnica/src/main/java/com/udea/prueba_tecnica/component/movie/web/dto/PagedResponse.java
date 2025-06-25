package com.udea.prueba_tecnica.component.movie.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated response wrapper")
public class PagedResponse<T> {

    @Schema(description = "List of items in current page")
    private List<T> content;

    @Schema(description = "Current page number (0-based)", example = "0")
    private int page;

    @Schema(description = "Page size", example = "20")
    private int size;

    @Schema(description = "Total number of elements", example = "150")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "8")
    private int totalPages;

    @Schema(description = "Whether this is the first page", example = "true")
    private boolean first;

    @Schema(description = "Whether this is the last page", example = "false")
    private boolean last;

    @Schema(description = "Number of elements in current page", example = "20")
    private int numberOfElements;

    @Schema(description = "Whether the page is empty", example = "false")
    private boolean empty;

    @Schema(description = "Sort information")
    private SortInfo sort;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Sort information")
    public static class SortInfo {
        @Schema(description = "Whether content is sorted", example = "true")
        private boolean sorted;

        @Schema(description = "Sort direction", example = "DESC")
        private String direction;

        @Schema(description = "Sort property", example = "popularity")
        private String property;
    }
} 