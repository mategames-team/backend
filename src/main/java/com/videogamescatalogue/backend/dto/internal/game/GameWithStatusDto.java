package com.videogamescatalogue.backend.dto.internal.game;

import com.videogamescatalogue.backend.dto.internal.genre.GenreDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import com.videogamescatalogue.backend.model.UserGame;
import java.math.BigDecimal;
import java.util.Set;

public record GameWithStatusDto(
        Long apiId,
        String name,
        int year,
        String backgroundImage,
        Set<PlatformDto> platforms,
        Set<GenreDto> genres,
        BigDecimal apiRating,
        String description,
        UserGame.GameStatus status
) {
}
