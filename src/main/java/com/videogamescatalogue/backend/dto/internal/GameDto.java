package com.videogamescatalogue.backend.dto.internal;

import java.math.BigDecimal;
import java.util.Set;

public record GameDto(
        Long apiId,
        String name,
        int year,
        String backgroundImage,
        Set<PlatformDto> platforms,
        Set<GenreDto> genres,
        BigDecimal apiRating
) {
}
