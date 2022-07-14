package org.rzldev.quarkus;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel="cdi",
        nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE
)
public interface MovieMapper {

    @Mapping(
            target="promotion",
            expression="java(movie.getTitle() + ' ' + movie.getDirector())"
    )
    MovieDTO toDTO(Movie movie);

    @Mapping(
            target="id",
            ignore=true
    )
    Movie toDAO(MovieDTO movieDTO);

    @Mapping(
            target="id",
            ignore=true
    )
    void merge(@MappingTarget Movie target, Movie source);
}
