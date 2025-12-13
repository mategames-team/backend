package com.videogamescatalogue.backend.dto.internal.game;

import com.videogamescatalogue.backend.dto.internal.genre.GenreDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import java.math.BigDecimal;
import java.util.Set;

public record GameDto(
        Long id,
        Long apiId,
        String name,
        int year,
        String backgroundImage,
        Set<PlatformDto> platforms,
        Set<GenreDto> genres,
        BigDecimal apiRating,
        String description
) {
}
