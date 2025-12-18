package com.videogamescatalogue.backend.dto.external;

import java.util.List;

public record ApiResponseGames(
        Long count,
        String next,
        List<ApiResponseGameDto> results
) {
}
