package com.videogamescatalogue.backend.mapper.developer;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.external.ApiResponseDeveloperDto;
import com.videogamescatalogue.backend.dto.internal.developer.DeveloperDto;
import com.videogamescatalogue.backend.model.Developer;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface DeveloperMapper {
    @Mapping(source = "id", target = "apiId")
    @Mapping(target = "id", ignore = true)
    Developer toModel(ApiResponseDeveloperDto apiResponseDeveloperDto);

    Set<Developer> toModelSet(List<ApiResponseDeveloperDto> developers);

    Set<DeveloperDto> toDtoSet(Set<Developer> developers);

    DeveloperDto toDto(Developer developer);

}
