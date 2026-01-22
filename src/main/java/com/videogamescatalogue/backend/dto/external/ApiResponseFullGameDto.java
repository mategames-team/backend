package com.videogamescatalogue.backend.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record ApiResponseFullGameDto(
        Long id,
        String name,
        String description,
        String released,
        @JsonProperty(value = "background_image")
        String backgroundImage,
        List<ApiPlatformWrapper> platforms,

        List<ApiResponseGenreDto> genres,

        List<ApiResponseDeveloperDto> developers,

        BigDecimal rating
) {
}
