package com.udea.prueba_tecnica.component.movie.io.mapper;

import com.udea.prueba_tecnica.component.movie.io.gateway.dto.TmdbMovieResponse;
import com.udea.prueba_tecnica.component.movie.io.repository.projection.MovieListDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieListItemDTO;
import com.udea.prueba_tecnica.component.movie.web.dto.MovieResponse;
import com.udea.prueba_tecnica.component.movie.web.dto.PagedResponse;
import com.udea.prueba_tecnica.model.Movie;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {

    @Value("${tmdb.api.image-base-url}")
    protected String imageBaseUrl;

    // Entity to Response DTOs
    @Mapping(target = "fullPosterUrl", expression = "java(buildImageUrl(movie.getPosterPath()))")
    @Mapping(target = "fullBackdropUrl", expression = "java(buildImageUrl(movie.getBackdropPath()))")
    public abstract MovieResponse toMovieResponse(Movie movie);

    @Mapping(target = "fullPosterUrl", expression = "java(buildImageUrl(movie.getPosterPath()))")
    @Mapping(target = "fullBackdropUrl", expression = "java(buildImageUrl(movie.getBackdropPath()))")
    public abstract MovieListItemDTO toMovieListItemDTO(Movie movie);

    // Projection to DTO
    @Mapping(target = "fullPosterUrl", expression = "java(buildImageUrl(projection.getPosterPath()))")
    @Mapping(target = "fullBackdropUrl", expression = "java(buildImageUrl(projection.getBackdropPath()))")
    public abstract MovieListItemDTO toMovieListItemDTO(MovieListDTO projection);

    // TMDB Response to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tmdbId", source = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Movie toMovie(TmdbMovieResponse tmdbResponse);

    // List mappings
    public abstract List<MovieResponse> toMovieResponseList(List<Movie> movies);
    public abstract List<MovieListItemDTO> toMovieListItemDTOList(List<Movie> movies);
    public abstract List<MovieListItemDTO> projectionListToMovieListItemDTOList(List<MovieListDTO> projections);

    // Page mapping
    public PagedResponse<MovieListItemDTO> toPagedResponse(Page<MovieListDTO> page) {
        List<MovieListItemDTO> content = projectionListToMovieListItemDTOList(page.getContent());
        
        PagedResponse.SortInfo sortInfo = PagedResponse.SortInfo.builder()
                .sorted(page.getSort().isSorted())
                .direction(page.getSort().isSorted() ? 
                    page.getSort().iterator().next().getDirection().name() : null)
                .property(page.getSort().isSorted() ? 
                    page.getSort().iterator().next().getProperty() : null)
                .build();

        return PagedResponse.<MovieListItemDTO>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .numberOfElements(page.getNumberOfElements())
                .empty(page.isEmpty())
                .sort(sortInfo)
                .build();
    }

    public PagedResponse<MovieListItemDTO> toPagedResponseFromEntities(Page<Movie> page) {
        List<MovieListItemDTO> content = toMovieListItemDTOList(page.getContent());
        
        PagedResponse.SortInfo sortInfo = PagedResponse.SortInfo.builder()
                .sorted(page.getSort().isSorted())
                .direction(page.getSort().isSorted() ? 
                    page.getSort().iterator().next().getDirection().name() : null)
                .property(page.getSort().isSorted() ? 
                    page.getSort().iterator().next().getProperty() : null)
                .build();

        return PagedResponse.<MovieListItemDTO>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .numberOfElements(page.getNumberOfElements())
                .empty(page.isEmpty())
                .sort(sortInfo)
                .build();
    }

    // Helper method to build full image URLs
    protected String buildImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        return imageBaseUrl + imagePath;
    }

    @AfterMapping
    protected void enhanceMovieResponse(@MappingTarget MovieResponse movieResponse, Movie movie) {
        // Additional enhancements can be added here
        if (movieResponse.getFullPosterUrl() == null && movie.getPosterPath() != null) {
            movieResponse.setFullPosterUrl(buildImageUrl(movie.getPosterPath()));
        }
        if (movieResponse.getFullBackdropUrl() == null && movie.getBackdropPath() != null) {
            movieResponse.setFullBackdropUrl(buildImageUrl(movie.getBackdropPath()));
        }
    }

    @AfterMapping
    protected void enhanceMovieListItemDTO(@MappingTarget MovieListItemDTO dto, MovieListDTO projection) {
        // Additional enhancements can be added here
        if (dto.getFullPosterUrl() == null && projection.getPosterPath() != null) {
            dto.setFullPosterUrl(buildImageUrl(projection.getPosterPath()));
        }
        if (dto.getFullBackdropUrl() == null && projection.getBackdropPath() != null) {
            dto.setFullBackdropUrl(buildImageUrl(projection.getBackdropPath()));
        }
    }
}
