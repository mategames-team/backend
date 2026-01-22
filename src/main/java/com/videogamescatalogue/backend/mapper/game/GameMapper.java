package com.videogamescatalogue.backend.mapper.game;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.exception.ParsingException;
import com.videogamescatalogue.backend.mapper.developer.DeveloperProvider;
import com.videogamescatalogue.backend.mapper.genre.GenreProvider;
import com.videogamescatalogue.backend.mapper.platform.PlatformProvider;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.UserGame;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {
        PlatformProvider.class,
        GenreProvider.class,
        DeveloperProvider.class
})
public interface GameMapper {
    List<Game> toModelList(List<ApiResponseGameDto> games);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "apiId")
    @Mapping(source = "released", target = "year", qualifiedByName = "toYear")
    @Mapping(source = "platforms", target = "platforms", qualifiedByName = "toPlatformsSet")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "toGenresSet")
    @Mapping(source = "rating", target = "apiRating")
    Game toModel(ApiResponseGameDto apiResponseGameDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "apiId")
    @Mapping(source = "released", target = "year", qualifiedByName = "toYear")
    @Mapping(source = "platforms", target = "platforms", qualifiedByName = "toPlatformsSet")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "toGenresSet")
    @Mapping(source = "developers", target = "developers", qualifiedByName = "toDevelopersSet")
    @Mapping(source = "rating", target = "apiRating")
    Game toModel(ApiResponseFullGameDto apiResponseGameDto);

    @Mapping(source = "platforms", target = "platforms", qualifiedByName = "toPlatformDtosSet")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "toGenreDtosSet")
    @Mapping(source = "developers", target = "developers", qualifiedByName = "toDeveloperDtosSet")
    GameDto toDto(Game game);

    @Mapping(target = "status", source = "status")
    @Mapping(
            source = "game.developers", target = "developers",
            qualifiedByName = "toDeveloperDtosSet"
    )
    GameWithStatusDto toDtoWithStatus(Game game, UserGame.GameStatus status);

    @Named("toYear")
    default Integer toYear(String releasedDate) {
        if (releasedDate == null || releasedDate.isBlank()) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(releasedDate);
            return localDate.getYear();
        } catch (DateTimeParseException e) {
            throw new ParsingException("Cannot parse release date to get release year", e);
        }
    }
}
