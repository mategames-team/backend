package com.videogamescatalogue.backend.mapper.usergame;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.UserGame;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserGameMapper {
    UserGameDto toDto(UserGame userGame);

}
