package com.udea.prueba_tecnica.component.movie.io.mapper;

import com.udea.prueba_tecnica.component.movie.io.repository.projection.GenreProjection;
import com.udea.prueba_tecnica.component.movie.web.dto.GenreResponse;
import com.udea.prueba_tecnica.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper abstracto para Genre
 * Implementa el requisito de usar MapStruct con clases abstractas
 */
@Mapper(componentModel = "spring")
public abstract class GenreMapper {

    public static final GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    // Mapeo de entity a DTO response
    @Mapping(target = "movieCount", constant = "0L")
    @Mapping(target = "averageRating", constant = "0.0")
    public abstract GenreResponse toResponse(Genre genre);

    // Mapeo de projection a DTO response
    public abstract GenreResponse toResponse(GenreProjection projection);

    // Mapeo de listas
    public abstract List<GenreResponse> toResponseList(List<Genre> genres);
    
    public abstract List<GenreResponse> toResponseListFromProjections(List<GenreProjection> projections);
} 