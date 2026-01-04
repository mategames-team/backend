package com.videogamescatalogue.backend.mapper.usergame;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameStatusDto;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.UserGame;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = GameProvider.class)
public interface UserGameMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game", target = "gameDto", qualifiedByName = "toGameDto")
    UserGameDto toDto(UserGame userGame);

    List<UserGameStatusDto> toStatusDtoList(List<UserGame> userGames);

    @Mapping(source = "game", target = "apiId", qualifiedByName = "toApiId")
    UserGameStatusDto toStatusDto(UserGame userGame);

    @Named("toApiId")
    default Long toApiId(Game game) {
        return game.getApiId();
    }
}
