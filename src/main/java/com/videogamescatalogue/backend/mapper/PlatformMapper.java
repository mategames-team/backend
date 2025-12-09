package com.videogamescatalogue.backend.mapper;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.PlatformDto;
import com.videogamescatalogue.backend.model.Platform;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface PlatformMapper {
    Set<PlatformDto> toPlatfromDtosSet(Set<Platform> platforms);

    @Mapping(source = "generalName", target = "generalName", qualifiedByName = "enumToString")
    PlatformDto toDto(Platform platform);

    @Named("enumToString")
    default String enumToString(Platform.GeneralName generalName) {
        return generalName.name();
    }
}
