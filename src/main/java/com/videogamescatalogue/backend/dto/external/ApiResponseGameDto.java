package com.videogamescatalogue.backend.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record ApiResponseGameDto(
        Long id,
        String name,
        String released,
        @JsonProperty(value = "background_image")
        String backgroundImage,
        List<ApiPlatformWrapper> platforms,

        List<ApiResponseGenreDto> genres,

        BigDecimal rating
) {
}
