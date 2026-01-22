package com.videogamescatalogue.backend.dto.internal.game;

import com.videogamescatalogue.backend.dto.internal.developer.DeveloperDto;
import com.videogamescatalogue.backend.dto.internal.genre.GenreDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import java.math.BigDecimal;
import java.util.Set;

public record GameDto(
        Long apiId,
        String name,
        int year,
        String backgroundImage,
        Set<PlatformDto> platforms,
        Set<GenreDto> genres,
        Set<DeveloperDto> developers,
        BigDecimal apiRating,
        String description
) {
}
