package com.videogamescatalogue.backend.mapper.usergame;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.UserGame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = GameProvider.class)
public interface UserGameMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game", target = "gameDto", qualifiedByName = "toGameDto")
    UserGameDto toDto(UserGame userGame);

}
